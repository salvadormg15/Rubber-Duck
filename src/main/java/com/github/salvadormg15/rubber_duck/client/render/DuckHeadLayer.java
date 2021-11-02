package com.github.salvadormg15.rubber_duck.client.render;

import com.github.salvadormg15.rubber_duck.common.RubberDuckItem;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.HeadLayer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;


public class DuckHeadLayer extends HeadLayer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>>{
	final ItemStack itemstack;
	IBakedModel model;
	public DuckHeadLayer(IEntityRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> entityRenderer, 
			Item renderItem, IBakedModel model) {
		super(entityRenderer);
		this.itemstack = new ItemStack(renderItem);
		this.model = model;
	}
	
//  Imitating the Curios API function FollowHeadRotations
//  I know this isn't the correct way to do this, but I coudn't achieve it with normal layers
//  I'd appreciate if you correct it by forking it or send me help via mail
	@Override
	public void render(MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light,
			AbstractClientPlayerEntity playerEntity, float limbSwing, float limbSwingAmount, float partialTicks,
			float ageInTicks, float netHeadYaw, float headPitch) {
		Item item = itemstack.getItem();
		Double helmetAdjusmentY = 0D;
		Double helmetAdjustmentZ = 0D;
		matrixStack.pushPose();
        matrixStack.scale(1f, 1f, 1f);
        this.getParentModel().getHead().translateAndRotate(matrixStack);
        if (item instanceof RubberDuckItem) {
        	Boolean flag = !playerEntity.getItemBySlot(EquipmentSlotType.HEAD).isEmpty();
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
            if(flag) {
            	helmetAdjusmentY = -0.0625D +(headPitch*0.0625D/90D);
            	helmetAdjustmentZ = (headPitch * 0.04D) / 90;
            }
            if(Math.abs(headPitch) > 90F || Math.abs(netHeadYaw) > 90F) {
            	netHeadYaw = netHeadYaw+360;
            }
            matrixStack.translate((netHeadYaw*0.18D/-45D)*(headPitch/-90D), -0.25D+((Math.abs(headPitch)*0.125D)/90D)+helmetAdjusmentY, (headPitch*0.2D)/90D + helmetAdjustmentZ);	//Vertical Translation when moving head
            matrixStack.scale(0.625F, -0.625F, -0.625F);
            matrixStack.mulPose(Vector3f.YN.rotationDegrees(netHeadYaw));	//Horizontal Rotation
            matrixStack.mulPose(Vector3f.XN.rotationDegrees(headPitch));	//Vertical Rotation
            
            Minecraft.getInstance().getItemInHandRenderer().renderItem((LivingEntity)playerEntity, itemstack, 
            		ItemCameraTransforms.TransformType.HEAD, false, matrixStack, renderTypeBuffer, light);
         }

         matrixStack.popPose();
	}
	
}
