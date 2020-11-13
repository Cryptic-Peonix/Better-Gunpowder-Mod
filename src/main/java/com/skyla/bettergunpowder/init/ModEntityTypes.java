package com.skyla.bettergunpowder.init;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fml.common.Mod;
import com.skyla.bettergunpowder.Reference;
import com.skyla.bettergunpowder.objects.entities.AbstractExplodingEntity;
import com.skyla.bettergunpowder.objects.entities.BoomSandEntity;
import com.skyla.bettergunpowder.objects.entities.GunpowderBlockEntity;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntityTypes {
	/*
	 * public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = new
	 * DeferredRegister<>(ForgeRegistries.ENTITIES, Reference.MOD_ID);
	 * 
	 * public static final RegistryObject<EntityType<GunpowderBlockEntity>>
	 * GUNPOWDER_BLOCK = ENTITY_TYPES.register( "gunpowder_block", () ->
	 * EntityType.Builder.<GunpowderBlockEntity>create(GunpowderBlockEntity::new,
	 * EntityClassification.MISC) .immuneToFire().size(0.98F, 0.98F) .build(new
	 * ResourceLocation(Reference.MOD_ID, "gunpowder_block").toString()));
	 */
	public static final EntityType<GunpowderBlockEntity> GUNPOWDER_BLOCK = register("gunpowder_block",
			EntityType.Builder.<GunpowderBlockEntity>create(GunpowderBlockEntity::new, EntityClassification.MISC)
					.immuneToFire().size(0.98f, 0.98f));

	public static final EntityType<AbstractExplodingEntity> BOOMY = register("boomy",
			EntityType.Builder.<AbstractExplodingEntity>create(AbstractExplodingEntity::new, EntityClassification.MISC)
					.immuneToFire().size(0.98f, 0.98f));

	public static final EntityType<BoomSandEntity> BOOM_SAND = register("boom_sand", EntityType.Builder
			.<BoomSandEntity>create(BoomSandEntity::new, EntityClassification.MISC).immuneToFire().size(0.98f, 0.98f));

	@SuppressWarnings("deprecation")
	private static <T extends Entity> EntityType<T> register(String key, EntityType.Builder<T> builder) {
		return Registry.register(Registry.ENTITY_TYPE, key, builder.build(key));
	}
}
