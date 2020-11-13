package com.skyla.bettergunpowder.init;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FallingBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.List;

import com.skyla.bettergunpowder.Reference;
import com.skyla.bettergunpowder.objects.blocks.BoomSandBlock;
import com.skyla.bettergunpowder.objects.blocks.GunpowderBlock;
import com.skyla.bettergunpowder.objects.blocks.ModOreBlock;
import com.skyla.bettergunpowder.objects.blocks.RedBoomSandBlock;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BlockInit {

	// Create Lists for Items
	private static final List<Block> BLOCKS = new ArrayList<>();
	private static final List<Item> ITEMS = new ArrayList<>();

	// Blocks
	public static final Block SALTPETER_ORE = register(new ResourceLocation(Reference.MOD_ID, "saltpeter_ore"),
			new ModOreBlock(Block.Properties.from(Blocks.IRON_ORE)), ItemGroup.BUILDING_BLOCKS);

	public static final Block SALTPETER_BLOCK = register(new ResourceLocation(Reference.MOD_ID, "saltpeter_block"),
			new FallingBlock(Block.Properties.from(Blocks.SAND)), ItemGroup.BUILDING_BLOCKS);

	public static final Block SULFUR_ORE = register(new ResourceLocation(Reference.MOD_ID, "sulfur_ore"),
			new ModOreBlock(Block.Properties.from(Blocks.COAL_ORE)), ItemGroup.BUILDING_BLOCKS);

	public static final Block SULFUR_BLOCK = register(new ResourceLocation(Reference.MOD_ID, "sulfur_block"),
			new FallingBlock(Block.Properties.from(Blocks.SAND)), ItemGroup.BUILDING_BLOCKS);

	public static final Block GUNPOWDER_BLOCK = register(new ResourceLocation(Reference.MOD_ID, "gunpowder_block"),
			new GunpowderBlock(Block.Properties.from(Blocks.SAND)), ItemGroup.BUILDING_BLOCKS);

	public static final Block CHARCOAL_BLOCK = register(new ResourceLocation(Reference.MOD_ID, "charcoal_block"),
			new Block(Block.Properties.from(Blocks.COAL_BLOCK)), ItemGroup.BUILDING_BLOCKS);

	public static final Block EXPLOSIVE_SAND = register(new ResourceLocation(Reference.MOD_ID, "boom_sand"),
			new BoomSandBlock(Block.Properties.from(Blocks.SAND)), ItemGroup.REDSTONE);
	
	public static final Block EXPLOSIVE_SAND_RED = register(new ResourceLocation(Reference.MOD_ID, "red_boom_sand"),
			new RedBoomSandBlock(Block.Properties.from(Blocks.SAND)), null);

	// Create constructor and add blocks and blockitems to the lists
	private static Block register(ResourceLocation key, Block block, ItemGroup group) {
		block.setRegistryName(key);
		BLOCKS.add(block);
		BlockItem item = new BlockItem(block, new Item.Properties().group(group));
		item.setRegistryName(key);
		ITEMS.add(item);
		return block;
	}

	// Register Blocks in list to registry
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		IForgeRegistry<Block> registry = event.getRegistry();
		BLOCKS.forEach(registry::register);
		BLOCKS.clear();
	}

	// Register Items in list to Registry
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();
		ITEMS.forEach(registry::register);
		ITEMS.clear();
	}
}
