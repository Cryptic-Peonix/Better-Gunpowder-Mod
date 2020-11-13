package com.skyla.bettergunpowder.objects.blocks;

import javax.annotation.Nullable;

import com.skyla.bettergunpowder.objects.entities.GunpowderBlockEntity;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class GunpowderBlock extends AbstractFallingTNTBlock {

	public GunpowderBlock(Properties properties) {
		super(properties);
		this.setDefaultState(this.getDefaultState().with(UNSTABLE, Boolean.valueOf(false)));
	}
	
	@Override
	public void catchFire(BlockState state, World world, BlockPos pos, Direction face, LivingEntity igniter) {
		explode(world, pos, igniter);
	}

	@Override
	public void onExplosionDestroy(World worldIn, BlockPos pos, Explosion explosionIn) {
		if (!worldIn.isRemote) {
			GunpowderBlockEntity entity = new GunpowderBlockEntity(worldIn, (double) ((float) pos.getX() + 0.5f),
					(double) pos.getY(), (double) ((float) pos.getZ() + 0.5f), explosionIn.getExplosivePlacedBy());
			entity.setFuse((short) (worldIn.rand.nextInt(entity.getFuse() / 4) + entity.getFuse() / 8));
			worldIn.addEntity(entity);
		}
	}

	@Deprecated
	public static void explode(World worldIn, BlockPos pos, @Nullable LivingEntity entityIn) {
		if (!worldIn.isRemote) {
			GunpowderBlockEntity entity = new GunpowderBlockEntity(worldIn, (double) pos.getX() + 0.5D,
					(double) pos.getY(), (double) pos.getZ() + 0.5d, entityIn);
			worldIn.addEntity(entity);
		}
	}
	
	@Override
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

	@Override
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
}
