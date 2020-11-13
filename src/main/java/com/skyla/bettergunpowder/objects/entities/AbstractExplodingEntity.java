package com.skyla.bettergunpowder.objects.entities;

import javax.annotation.Nullable;

import com.skyla.bettergunpowder.init.ModEntityTypes;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.Pose;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
//import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class AbstractExplodingEntity extends Entity {

	protected static final DataParameter<Integer> FUSE = EntityDataManager.createKey(AbstractExplodingEntity.class,
			DataSerializers.VARINT);
	@Nullable
	private LivingEntity gpbPlacedBy;
	private int fuse;
	
	public AbstractExplodingEntity(EntityType<? extends AbstractExplodingEntity> entityTypeIn, World worldIn) {
		super(entityTypeIn, worldIn);
		this.preventEntitySpawning = true;
	}

	public AbstractExplodingEntity(World worldIn, double x, double y, double z, @Nullable LivingEntity igniter) {
		this(ModEntityTypes.BOOMY, worldIn);
		this.setPosition(x, y, z);
		double d0 = worldIn.rand.nextDouble() * (double) ((float) Math.PI * 2f);
		this.setMotion(-Math.sin(d0) * 0.02D, (double) 0.2f, -Math.cos(d0) * 0.02D);
		this.setFuse(80);
		this.prevPosX = x;
		this.prevPosY = y;
		this.prevPosZ = z;
		this.gpbPlacedBy = igniter;
	}

	@Override
	protected void registerData() {
		this.dataManager.register(FUSE, 80);
	}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean canBeCollidedWith() {
		return !this.removed;
	}

	@Override
	public void tick() {
		if (!this.hasNoGravity()) {
			this.setMotion(this.getMotion().add(0.0D, -0.04D, 0.0D));
		}

		this.move(MoverType.SELF, this.getMotion());
		this.setMotion(this.getMotion().scale(0.98D));
		if (this.onGround) {
			this.setMotion(this.getMotion().mul(0.7D, -0.5D, 0.7D));
		}

		--this.fuse;
		if (this.fuse <= 0) {
			this.remove();
			if (!this.world.isRemote) {
				this.explode();
			}
		} else {
			this.handleWaterMovement();
			if (this.world.isRemote) {
				this.world.addParticle(ParticleTypes.SMOKE, this.getPosX(), this.getPosY() + 0.5D, this.getPosZ(), 0.0D,
						0.0D, 0.0D);
			}
		}
	}

	protected void explode() {
		float f = 4.0f;
		this.world.createExplosion(this, this.getPosX(), this.getPosYHeight(0.0625d), this.getPosZ(), f,
				Explosion.Mode.DESTROY);
	}

	@Override
	protected void writeAdditional(CompoundNBT compound) {
		compound.putShort("Fuse", (short) this.getFuse());
	}

	@Override
	protected void readAdditional(CompoundNBT compound) {
		this.setFuse(compound.getShort("Fuse"));
	}
	
	@Nullable
	public LivingEntity getGPBPlacedBy() {
		return this.gpbPlacedBy;
	}
	
	@Override
	public float getEyeHeight(Pose poseIn, EntitySize sizeIn) {
		return 0.0f;
	}
	
	public void setFuse(int fuseIn) {
		this.dataManager.set(FUSE, fuseIn);
		this.fuse = fuseIn;
	}
	
	@Override
	public void notifyDataManagerChange(DataParameter<?> key) {
		if(FUSE.equals(key)) {
			this.fuse = this.getFuseDataManager();
		}
	}
	
	public int getFuseDataManager() {
		return this.dataManager.get(FUSE);
	}
	
	public int getFuse() {
		return this.fuse;
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
