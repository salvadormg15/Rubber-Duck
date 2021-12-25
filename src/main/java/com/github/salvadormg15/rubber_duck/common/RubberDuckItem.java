package com.github.salvadormg15.rubber_duck.common;

import com.github.salvadormg15.rubber_duck.common.core.Registries;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class RubberDuckItem extends BlockItem implements ICurioItem{
	public RubberDuckItem(Block block, Properties properties) {
		super(block, properties);
	}
	@Override
	public boolean canEquip(ItemStack stack, EquipmentSlotType armorType, Entity entity) {
		if(armorType.equals(EquipmentSlotType.HEAD)) return true;
		return false;
	}
	
	//Curios stuff
	@Override
	public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
		return true;
	}
	@Override
	public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
		LivingEntity entity = slotContext.getWearer();
		entity.playSound(Registries.RUBBER_DUCK_PLACE.get(), 0.8f, 1f);
	}
	
	@Override
	public boolean canRender(String identifier, int index, LivingEntity livingEntity, ItemStack stack) {
		return false;
	}
	@Override
	public void render(String identifier, int index, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer,
			int light, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float partialTicks,
			float ageInTicks, float netHeadYaw, float headPitch, ItemStack stack) {
	}
}
