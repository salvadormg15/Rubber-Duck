package com.github.salvadormg15.rubber_duck.core;

import com.github.salvadormg15.rubber_duck.RubberDuck;

import com.github.salvadormg15.rubber_duck.constants.CommonConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ForgeEventHandler {

    //Duck Spawn Event
    @SubscribeEvent
    public static void onLivingSpecialSpawn(MobSpawnEvent.FinalizeSpawn event) {
        LivingEntity entity = event.getEntity();
        //Checks the Mod Config
        if (entity instanceof Zombie && CommonConstants.getSpawnsOnZombies() ||
                entity instanceof Skeleton && CommonConstants.getSpawnsOnSkeletons()) {
            RandomSource random = event.getLevel().getRandom();
            double chance = random.nextDouble();
            if (chance <= CommonConstants.getEntitySpawnChance()) {
                if (entity.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
                    entity.setItemSlot(EquipmentSlot.HEAD, Registries.RUBBER_DUCK_ITEM.get().getDefaultInstance());
                }
            }
        }
    }

    //100% duck drop probability
    @SubscribeEvent
    public static void onDeathSpecialEvent(LivingDropsEvent event) {
        LivingEntity entity = event.getEntity();
        BlockPos pos = entity.blockPosition();

        // If it's not a zombie or skeleton returns
        if (!(entity instanceof Zombie || entity instanceof Skeleton)) {
            return;
        }
        //Has a duck equipped?
        if (entity.getItemBySlot(EquipmentSlot.HEAD).getItem().toString()
                .equals(Registries.RUBBER_DUCK_ITEM.get().toString())) {
            ItemEntity duckItem = new ItemEntity(entity.level(), pos.getX(), pos.getY(), pos.getZ(),
                    Registries.RUBBER_DUCK_ITEM.get().getDefaultInstance());

            //Removes ducks from drop
            for (ItemEntity itemEntity : event.getDrops()) {
                if (itemEntity.getItem().getItem().toString()
                        .equals(Registries.RUBBER_DUCK_ITEM.get().toString())) {
                    event.getDrops().remove(itemEntity);
                }
            }

            //Drop randomised
            RandomSource random = event.getEntity().getRandom();
            double chance = random.nextDouble();
            if (chance <= CommonConstants.getDropChance()) {
                event.getDrops().add(duckItem);
            }
        }
    }

    //Generation on chests
    @SubscribeEvent
    public static void onLootLoad(LootTableLoadEvent event) {
        if (event.getName().toString().startsWith("minecraft:chests")) {
            event.getTable().addPool(LootPool.lootPool()
                .add(LootTableReference.lootTableReference(
                    new ResourceLocation(RubberDuck.MODID, "chests/rubber_duck")))
                .build());
        }
    }
}
