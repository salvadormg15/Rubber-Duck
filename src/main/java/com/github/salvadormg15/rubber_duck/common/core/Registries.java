package com.github.salvadormg15.rubber_duck.common.core;

import com.github.salvadormg15.rubber_duck.common.RubberDuck;
import com.github.salvadormg15.rubber_duck.common.RubberDuckBlock;
import com.github.salvadormg15.rubber_duck.common.RubberDuckItem;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
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
					new Item.Properties().tab(CreativeModeTab.TAB_MISC).stacksTo(4).rarity(Rarity.RARE)));
	// Sound Events
	public static final RegistryObject<SoundEvent> RUBBER_DUCK_USE = SOUND_EVENTS.register("rubber_duck_use",
			() -> new SoundEvent(new ResourceLocation(RubberDuck.MODID, "rubber_duck_use")));
	public static final RegistryObject<SoundEvent> RUBBER_DUCK_PLACE = SOUND_EVENTS.register("rubber_duck_place",
			() -> new SoundEvent(new ResourceLocation(RubberDuck.MODID, "rubber_duck_place")));
}
