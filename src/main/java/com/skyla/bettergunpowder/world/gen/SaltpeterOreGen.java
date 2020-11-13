package com.skyla.bettergunpowder.world.gen;

import com.skyla.bettergunpowder.init.BlockInit;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.ConfiguredPlacement;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.registries.ForgeRegistries;

public class SaltpeterOreGen {

	public static void generateOre() {
		for (Biome biome : ForgeRegistries.BIOMES) {
			ConfiguredPlacement<CountRangeConfig> customConfig = Placement.COUNT_RANGE
					.configure(new CountRangeConfig(15, 44, 0, 128));
			biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES,
					Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE,
							BlockInit.SALTPETER_ORE.getDefaultState(), 9)).withPlacement(customConfig));
		}
	}
}
