package com.skyla.bettergunpowder.objects.blocks;

import java.util.Random;

import com.skyla.bettergunpowder.init.BlockInit;

import net.minecraft.block.OreBlock;
import net.minecraft.util.math.MathHelper;

public class ModOreBlock extends OreBlock {

	public ModOreBlock(Properties properties) {
		super(properties);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected int getExperience(Random rand) {
		if(this == BlockInit.SALTPETER_ORE) {
			return MathHelper.nextInt(rand, 0, 1);
		} else {
			return this == BlockInit.SULFUR_ORE ? MathHelper.nextInt(rand, 0, 1) : 0;
		}
	}

}
