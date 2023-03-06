package com.github.salvadormg15.rubber_duck.client.render;

import com.github.salvadormg15.rubber_duck.common.RubberDuckItem;
import com.github.salvadormg15.rubber_duck.common.core.Registries;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.HeadLayer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.IHasHead;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import top.theillusivec4.curios.api.CuriosApi;

@OnlyIn(Dist.CLIENT)
public class DuckHeadLayer<T extends LivingEntity, M extends EntityModel<T> & IHasHead> extends HeadLayer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>>{
	public DuckHeadLayer(IEntityRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> entityRenderer) {
		super(entityRenderer);
	}
	

	@Override
	public void render(MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light,
			AbstractClientPlayerEntity playerEntity, float limbSwing, float limbSwingAmount, float partialTicks,
			float ageInTicks, float netHeadYaw, float headPitch) {
		//If there is no duck equipped, it has nothing to render
		if(!CuriosApi.getCuriosHelper().findEquippedCurio(Registries.RUBBER_DUCK_ITEM.get().getItem(), playerEntity).isPresent()) {
			return;
		}
		ItemStack itemstack = new ItemStack(Registries.RUBBER_DUCK_ITEM.get().getItem());
		Item item = itemstack.getItem();
		matrixStack.pushPose();
        this.getParentModel().getHead().translateAndRotate(matrixStack);
        if (item instanceof RubberDuckItem) {
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
            //Places the duck a little bit upper if there is a helmet
            if(playerEntity.getItemBySlot(EquipmentSlotType.HEAD).isEmpty()) {
            	matrixStack.translate(0, -0.25D, 0);
            } else {
            	if(playerEntity.getItemBySlot(EquipmentSlotType.HEAD).getItem() instanceof RubberDuckItem) {
            		matrixStack.translate(0, -0.425D, 0.05D);
					matrixStack.mulPose(Vector3f.XP.rotationDegrees(20f));
            	}else {
            		matrixStack.translate(0, -0.313, 0);
            	}
            }
            matrixStack.scale(0.62F, -0.62F, -0.62F);
            
            Minecraft.getInstance().getItemInHandRenderer().renderItem((LivingEntity)playerEntity, itemstack, 
            		ItemCameraTransforms.TransformType.HEAD, false, matrixStack, renderTypeBuffer, light);
         }

         matrixStack.popPose();
	}
	
}
