package com.github.salvadormg15.rubber_duck.constants;

import com.github.salvadormg15.rubber_duck.config.CommonConfigs;

public class CommonConstants {
    private static Double ENTITY_SPAWN_CHANCE;
    private static Double DROP_CHANCE;
    private static Boolean SPAWNS_ON_ZOMBIES;
    private static Boolean SPAWNS_ON_SKELETONS;
    private static Boolean CHEST_LOOT_ENABLED;

    public static void initConstants(){
        ENTITY_SPAWN_CHANCE = CommonConfigs.ENTITY_SPAWN_CHANCE.get();
        DROP_CHANCE = CommonConfigs.ENTITY_DROP_CHANCE.get();
        SPAWNS_ON_ZOMBIES = CommonConfigs.SPAWNABLE_ON_ZOMBIES.get();
        SPAWNS_ON_SKELETONS = CommonConfigs.SPAWNABLE_ON_SKELETONS.get();
        CHEST_LOOT_ENABLED = CommonConfigs.CHEST_LOOT_ENABLED.get();
    }

    public static Double getEntitySpawnChance() {
        return ENTITY_SPAWN_CHANCE;
    }

    public static Double getDropChance() {
        return DROP_CHANCE;
    }

    public static Boolean getSpawnsOnZombies() { return SPAWNS_ON_ZOMBIES; }

    public static Boolean getSpawnsOnSkeletons() {
        return SPAWNS_ON_SKELETONS;
    }

    public static Boolean getChestLootEnabled() { return CHEST_LOOT_ENABLED; }
}
