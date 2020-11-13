package com.skyla.bettergunpowder.objects.entities;

import javax.annotation.Nullable;

import org.antlr.v4.runtime.misc.NotNull;

import com.skyla.bettergunpowder.init.ModEntityTypes;

//import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.IPacket;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

@SuppressWarnings("deprecation")
public class BoomSandEntity extends AbstractExplodingEntity {
	@Nullable
	private LivingEntity gpbPlacedBy;

	public BoomSandEntity(EntityType<? extends BoomSandEntity> entityTypeIn, World worldIn) {
		super(entityTypeIn, worldIn);
		this.preventEntitySpawning = true;
	}

	public BoomSandEntity(World worldIn, double x, double y, double z, @Nullable LivingEntity igniter) {
		this(ModEntityTypes.BOOM_SAND, worldIn);
		this.setPosition(x, y, z);
		double d0 = worldIn.rand.nextDouble() * (double) ((float) Math.PI * 1f);
		this.setMotion(-Math.sin(d0) * 0.02D, (double) 0.2f, -Math.cos(d0) * 0.01D);
		this.setFuse(10);
		this.prevPosX = x;
		this.prevPosY = y;
		this.prevPosZ = z;
		this.gpbPlacedBy = igniter;
	}

	@Override
	protected void registerData() {
		this.dataManager.register(FUSE, 10);
	}

	@Override
	protected void explode() {
		this.world.createExplosion(this, this.getPosX(), this.getPosYHeight(0.0625d), this.getPosZ(), 2.5f,
				Explosion.Mode.DESTROY);
	}

	@Override
	public @NotNull IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

}
