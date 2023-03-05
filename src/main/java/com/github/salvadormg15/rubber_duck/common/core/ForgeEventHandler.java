package com.github.salvadormg15.rubber_duck.common.core;

import java.util.Random;

import com.github.salvadormg15.rubber_duck.common.RubberDuck;
import com.github.salvadormg15.rubber_duck.common.RubberDuckBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ForgeEventHandler {
	
	//Duck Spawn Event
	@SubscribeEvent
	public static void onLivingSpecialSpawn(LivingSpawnEvent.SpecialSpawn event) {
		LivingEntity entity = event.getEntityLiving();
		if (entity instanceof Zombie || entity instanceof Skeleton) {
			Random random = event.getWorld().getRandom();
			double chance = random.nextDouble();
			if (chance <= RubberDuckBlock.getOnEntitySpawnChance()) {
				if (entity.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
					entity.setItemSlot(EquipmentSlot.HEAD, Registries.RUBBER_DUCK_ITEM.get().getDefaultInstance());
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
		if (!(entity instanceof Zombie || entity instanceof Skeleton)) {
			return;
		}
		if (entity.getItemBySlot(EquipmentSlot.HEAD).getItem().getRegistryName().getPath()
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
					.add(LootTableReference.lootTableReference(new ResourceLocation(RubberDuck.MODID, "chests/rubber_duck")))
					.build());
		}
	}
}
