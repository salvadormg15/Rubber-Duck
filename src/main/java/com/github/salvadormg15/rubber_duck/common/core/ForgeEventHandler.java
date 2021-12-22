package com.github.salvadormg15.rubber_duck.common.core;

import java.util.Random;

import com.github.salvadormg15.rubber_duck.common.RubberDuck;
import com.github.salvadormg15.rubber_duck.common.RubberDuckBlock;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.TableLootEntry;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ForgeEventHandler {
	
	//Duck Spawn Event
	@SubscribeEvent
	public static void onLivingSpecialSpawn(LivingSpawnEvent.SpecialSpawn event) {
		LivingEntity entity = event.getEntityLiving();
		if (entity instanceof ZombieEntity || entity instanceof SkeletonEntity) {
			Random random = event.getWorld().getRandom();
			double chance = random.nextDouble();
			if (chance <= RubberDuckBlock.getOnEntitySpawnChance()) {
				if (entity.getItemBySlot(EquipmentSlotType.HEAD).isEmpty()) {
					entity.setItemSlot(EquipmentSlotType.HEAD, Registries.RUBBER_DUCK_ITEM.get().getDefaultInstance());
				}
			}
		}
	}

	//100% duck drop probability
	@SubscribeEvent
	public static void onDeathSpecialEvent(LivingDropsEvent event) {
		LivingEntity entity = event.getEntityLiving();
		BlockPos pos = entity.blockPosition();
		// If it's not a zombie or skeleton returns
		if (!(entity instanceof ZombieEntity || entity instanceof SkeletonEntity)) {
			return;
		}
		if (entity.getItemBySlot(EquipmentSlotType.HEAD).getItem().getRegistryName().getPath()
				.equals(Registries.RUBBER_DUCK_ITEM.get().getRegistryName().getPath())) {
			ItemEntity item = new ItemEntity(entity.level, pos.getX(), pos.getY(), pos.getZ(),
					Registries.RUBBER_DUCK_ITEM.get().getDefaultInstance());
			int ducks = 0;
			for (ItemEntity itemen : event.getDrops()) {
				if (itemen.getItem().getItem().getRegistryName().getPath()
						.equals(Registries.RUBBER_DUCK_ITEM.get().getRegistryName().getPath())) {
					ducks++;
				}
			}
			if (ducks == 0) {
				event.getDrops().add(item);
				ducks++;
			}
		}
	}

	//Generation on chests
	@SubscribeEvent
	public static void onLootLoad(LootTableLoadEvent event) {
		String chestName = event.getName().toString().substring(0,16);
		String wantedName = "minecraft:chests";
		if (chestName.equals(wantedName)) {
			event.getTable().addPool(LootPool.lootPool()
					.add(TableLootEntry.lootTableReference(new ResourceLocation(RubberDuck.MODID, "chests/rubber_duck")))
					.build());
		}
	}
}
