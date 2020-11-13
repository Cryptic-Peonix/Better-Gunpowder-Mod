package com.skyla.bettergunpowder.init;

import java.util.ArrayList;
import java.util.List;

import com.skyla.bettergunpowder.Reference;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ItemInit {

	// Create Item List
	private static final List<Item> ITEMS = new ArrayList<>();

	// Items
	public static final Item SALTPETER = register(new ResourceLocation(Reference.MOD_ID, "saltpeter"),
			new Item((new Item.Properties().maxStackSize(64).group(ItemGroup.MISC))));

	public static final Item SULFUR = register(new ResourceLocation(Reference.MOD_ID, "sulfur"),
			new Item(new Item.Properties().maxStackSize(64).group(ItemGroup.MISC)));

	// Constructor
	private static Item register(ResourceLocation key, Item itemIn) {
		itemIn.setRegistryName(key);
		ITEMS.add(itemIn);
		return itemIn;
	}

	// Register Items to registry
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();
		ITEMS.forEach(registry::register);
		ITEMS.clear();
	}
}
