package com.skyla.bettergunpowder.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.skyla.bettergunpowder.init.BlockInit;
import com.skyla.bettergunpowder.objects.entities.GunpowderBlockEntity;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.TNTMinecartRenderer;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GunpowderBlockRenderer extends EntityRenderer<GunpowderBlockEntity>{

	public GunpowderBlockRenderer(EntityRendererManager renderManager) {
		super(renderManager);
		this.shadowSize = 0.5f;
	}
	
	@Override
	public void render(GunpowderBlockEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn,
			IRenderTypeBuffer bufferIn, int packedLightIn) {
		matrixStackIn.push();
		matrixStackIn.translate(0.0D, 0.5D, 0.0D);
		if((float)entityIn.getFuse() - partialTicks + 1.0F < 10.0F) {
			float f = 1.0F - ((float)entityIn.getFuse() - partialTicks + 1.0F) / 10.0F;
			f = MathHelper.clamp(f, 0.0f, 1.0f);
			f = f * f;
			f = f * f;
			float f1 = 1.0f + f * 0.3f;
			matrixStackIn.scale(f1, f1, f1);
		}
		
		matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-90.0F));
		matrixStackIn.translate(-0.5D, -0.5D, 0.5D);
		matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90.0F));
		TNTMinecartRenderer.renderTntFlash(BlockInit.GUNPOWDER_BLOCK.getDefaultState(), matrixStackIn, bufferIn, packedLightIn, entityIn.getFuse() / 5 % 2 == 0);
		matrixStackIn.pop();
		super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	@Override
	public ResourceLocation getEntityTexture(GunpowderBlockEntity entity) {
		return PlayerContainer.LOCATION_BLOCKS_TEXTURE;
	}

	
}
