package com.github.salvadormg15.rubber_duck.core;

import com.github.salvadormg15.rubber_duck.RubberDuck;
import com.github.salvadormg15.rubber_duck.RubberDuckBlock;
import com.github.salvadormg15.rubber_duck.RubberDuckItem;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Registries {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
			RubberDuck.MODID);
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, RubberDuck.MODID);
	public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister
			.create(ForgeRegistries.SOUND_EVENTS, RubberDuck.MODID);

	// Blocks
	public static final RegistryObject<RubberDuckBlock> RUBBER_DUCK_BLOCK = BLOCKS.register("rubber_duck_block",
			() -> new RubberDuckBlock());
	// Items
	public static final RegistryObject<RubberDuckItem> RUBBER_DUCK_ITEM = ITEMS.register("rubber_duck_item",
			() -> new RubberDuckItem(RUBBER_DUCK_BLOCK.get(),
					new Item.Properties().stacksTo(4).rarity(Rarity.RARE)));
	// Sound Events
	public static final RegistryObject<SoundEvent> RUBBER_DUCK_USE = SOUND_EVENTS.register("rubber_duck_use",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(RubberDuck.MODID, "rubber_duck_use")));
	public static final RegistryObject<SoundEvent> RUBBER_DUCK_PLACE = SOUND_EVENTS.register("rubber_duck_place",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(RubberDuck.MODID, "rubber_duck_place")));

    @SubscribeEvent
    public static void buildContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            event.accept(Registries.RUBBER_DUCK_BLOCK);
        }
    }
}
