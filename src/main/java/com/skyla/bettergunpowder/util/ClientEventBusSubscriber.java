package com.skyla.bettergunpowder.util;

import com.skyla.bettergunpowder.Reference;
import com.skyla.bettergunpowder.client.renderer.entity.BoomSandRenderer;
import com.skyla.bettergunpowder.client.renderer.entity.GunpowderBlockRenderer;
import com.skyla.bettergunpowder.init.ModEntityTypes;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Bus.MOD, value = Dist.CLIENT)
public class ClientEventBusSubscriber {

	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent event) {
		RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.GUNPOWDER_BLOCK, GunpowderBlockRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.BOOM_SAND, BoomSandRenderer::new);
	}
}
