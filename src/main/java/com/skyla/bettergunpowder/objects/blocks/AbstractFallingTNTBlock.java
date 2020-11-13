package com.skyla.bettergunpowder.objects.blocks;

import java.util.Random;

import javax.annotation.Nullable;

import com.skyla.bettergunpowder.objects.entities.AbstractExplodingEntity;

import net.minecraft.block.material.Material;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class AbstractFallingTNTBlock extends Block {
	public static final BooleanProperty UNSTABLE = BlockStateProperties.UNSTABLE;

	public AbstractFallingTNTBlock(Properties properties) {
		super(properties);
		this.setDefaultState(this.getDefaultState().with(UNSTABLE, Boolean.valueOf(false)));
	}

	@Override
	public void catchFire(BlockState state, World world, BlockPos pos, Direction face, LivingEntity igniter) {
		explode(world, pos, igniter);
	}

	@Override
	public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
		worldIn.getPendingBlockTicks().scheduleTick(pos, this, this.tickRate(worldIn));
		if (oldState.getBlock() != state.getBlock()) {
			if (worldIn.isBlockPowered(pos)) {
				catchFire(state, worldIn, pos, null, null);
				worldIn.removeBlock(pos, false);
			}
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn,
			BlockPos currentPos, BlockPos facingPos) {
		worldIn.getPendingBlockTicks().scheduleTick(currentPos, this, this.tickRate(worldIn));
		return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}

	@Override
	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
		if (worldIn.isAirBlock(pos.down()) || canFallThrough(worldIn.getBlockState(pos.down())) && pos.getY() >= 0) {
			FallingBlockEntity fallingblockentity = new FallingBlockEntity(worldIn, (double) pos.getX() + 0.5D,
					(double) pos.getY(), (double) pos.getZ() + 0.5D, worldIn.getBlockState(pos));
			this.onStartFalling(fallingblockentity);
			worldIn.addEntity(fallingblockentity);
		}
	}

	protected void onStartFalling(FallingBlockEntity fallingEntity) {

	}

	@Override
	public int tickRate(IWorldReader worldIn) {
		return 2;
	}

	@SuppressWarnings("deprecation")
	public static boolean canFallThrough(BlockState state) {
		Block block = state.getBlock();
		Material material = state.getMaterial();
		return state.isAir() || block == Blocks.FIRE || material.isLiquid() || material.isReplaceable();
	}

	public void onEndFalling(World worldIn, BlockPos pos, BlockState fallingState, BlockState hitState) {

	}

	public void onBroken(World worldIn, BlockPos pos) {

	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		if (rand.nextInt(16) == 0) {
			BlockPos blockpos = pos.down();
			if (worldIn.isAirBlock(blockpos) || canFallThrough(worldIn.getBlockState(blockpos))) {
				double d0 = (double) pos.getX() + (double) rand.nextFloat();
				double d1 = (double) pos.getY() - 0.05D;
				double d2 = (double) pos.getZ() + (double) rand.nextFloat();
				worldIn.addParticle(new BlockParticleData(ParticleTypes.FALLING_DUST, stateIn), d0, d1, d2, 0.0D, 0.0D,
						0.0D);
			}
		}

	}

	@OnlyIn(Dist.CLIENT)
	public int getDustColor(BlockState state) {
		return -16777216;
	}

	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos,
			boolean isMoving) {
		if (worldIn.isBlockPowered(pos)) {
			catchFire(state, worldIn, pos, null, null);
			worldIn.removeBlock(pos, false);
		}
	}

	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
		if (!worldIn.isRemote() && !player.isCreative() && state.get(UNSTABLE)) {
			catchFire(state, worldIn, pos, null, null);
		}

		super.onBlockHarvested(worldIn, pos, state, player);
	}

	@Override
	public void onExplosionDestroy(World worldIn, BlockPos pos, Explosion explosionIn) {
		if (!worldIn.isRemote) {
			AbstractExplodingEntity entity = new AbstractExplodingEntity(worldIn, (double) ((float) pos.getX() + 0.5f),
					(double) pos.getY(), (double) ((float) pos.getZ() + 0.5f), explosionIn.getExplosivePlacedBy());
			entity.setFuse((short) (worldIn.rand.nextInt(entity.getFuse() / 4) + entity.getFuse() / 8));
			worldIn.addEntity(entity);
		}
	}

	@Deprecated // Forge: Prefer using IForgeBlock#catchFire
	public static void explode(World p_196534_0_, BlockPos worldIn) {
		explode(p_196534_0_, worldIn, (LivingEntity) null);
	}

	@Deprecated
	public static void explode(World worldIn, BlockPos pos, @Nullable LivingEntity entityIn) {
		if (!worldIn.isRemote) {
			AbstractExplodingEntity entity = new AbstractExplodingEntity(worldIn, (double) pos.getX() + 0.5D,
					(double) pos.getY(), (double) pos.getZ() + 0.5d, entityIn);
			worldIn.addEntity(entity);
			worldIn.playSound((PlayerEntity) null, entity.getPosX(), entity.getPosY(), entity.getPosZ(),
					SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);

		}
	}

	@SuppressWarnings("deprecation")
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player,
			Hand handIn, BlockRayTraceResult hit) {
		ItemStack itemstack = player.getHeldItem(handIn);
		Item item = itemstack.getItem();
		if (item != Items.FLINT_AND_STEEL && item != Items.FIRE_CHARGE) {
			return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
		} else {
			catchFire(state, worldIn, pos, hit.getFace(), player);
			worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 11);
			if (!player.isCreative()) {
				if (item == Items.FLINT_AND_STEEL) {
					itemstack.damageItem(1, player, (p_220287_1_) -> {
						p_220287_1_.sendBreakAnimation(handIn);
					});
				} else {
					itemstack.shrink(1);
				}
			}

			return ActionResultType.SUCCESS;
		}
	}

	public void onProjectileCollision(World worldIn, BlockState state, BlockRayTraceResult hit, Entity projectile) {
		if (!worldIn.isRemote && projectile instanceof AbstractArrowEntity) {
			AbstractArrowEntity abstractarrowentity = (AbstractArrowEntity) projectile;
			Entity entity = abstractarrowentity.getShooter();
			if (abstractarrowentity.isBurning()) {
				BlockPos blockpos = hit.getPos();
				catchFire(state, worldIn, blockpos, null,
						entity instanceof LivingEntity ? (LivingEntity) entity : null);
				worldIn.removeBlock(blockpos, false);
			}
		}

	}

	/**
	 * Return whether this block can drop from an explosion.
	 */
	public boolean canDropFromExplosion(Explosion explosionIn) {
		return false;
	}

	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(UNSTABLE);
	}

}
