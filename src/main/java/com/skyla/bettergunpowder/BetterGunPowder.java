package com.skyla.bettergunpowder;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.skyla.bettergunpowder.world.gen.SaltpeterOreGen;
import com.skyla.bettergunpowder.world.gen.SulfurOreGen;


// The value here should match an entry in the META-INF/mods.toml file
@Mod("bettergunpowder")
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Bus.MOD)
public class BetterGunPowder {
	// Directly reference a log4j logger.
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LogManager.getLogger();

	public BetterGunPowder() {
		
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
		
		// Register ourselves for server and other game events we are interested in
		MinecraftForge.EVENT_BUS.register(this);
	}

	private void setup(final FMLCommonSetupEvent event) {

	}

	private void doClientStuff(final FMLClientSetupEvent event) {

	}
	
	@SubscribeEvent
	public static void loadCompleteEvent(FMLLoadCompleteEvent event) {
		SulfurOreGen.generateOre();
		SaltpeterOreGen.generateOre();
	}
	
	

}
