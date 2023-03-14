package com.github.salvadormg15.rubber_duck.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfigs {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.DoubleValue ENTITY_SPAWN_CHANCE;
    public static final ForgeConfigSpec.DoubleValue ENTITY_DROP_CHANCE;
    public static final ForgeConfigSpec.BooleanValue SPAWNABLE_ON_ZOMBIES;
    public static final ForgeConfigSpec.BooleanValue SPAWNABLE_ON_SKELETONS;
    public static final ForgeConfigSpec.BooleanValue CHEST_LOOT_ENABLED;

    
    static {
        ENTITY_SPAWN_CHANCE = BUILDER.comment("Spawn chance on mobs")
                .defineInRange("EntitySpawnChance", 0.02, 0.0, 1);
        ENTITY_DROP_CHANCE = BUILDER.comment("Drop chance when mob is killed")
                .defineInRange("DropChance", 1, 0.0, 1);

        BUILDER.push("Spawns on this mob");
        SPAWNABLE_ON_ZOMBIES = BUILDER.define("Zombies", true);
        SPAWNABLE_ON_SKELETONS = BUILDER.define("Skeletons", true);
        BUILDER.pop();

        CHEST_LOOT_ENABLED = BUILDER.comment("Loot added to Vanilla chests")
                .define("ChestLootEnabled", true);
        
        SPEC = BUILDER.build();
    }
}
