package com.github.salvadormg15.rubber_duck;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Rarity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

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
	public static final RegistryObject<BlockItem> RUBBER_DUCK_ITEM = ITEMS.register("rubber_duck_item",
			() -> new BlockItem(RUBBER_DUCK_BLOCK.get(),
					new Item.Properties().tab(ItemGroup.TAB_MISC).stacksTo(4).rarity(Rarity.RARE)));
	// Sound Events
	public static final RegistryObject<SoundEvent> RUBBER_DUCK_USE = SOUND_EVENTS.register("rubber_duck_use",
			() -> new SoundEvent(new ResourceLocation(RubberDuck.MODID, "rubber_duck_use")));
	public static final RegistryObject<SoundEvent> RUBBER_DUCK_PLACE = SOUND_EVENTS.register("rubber_duck_place",
			() -> new SoundEvent(new ResourceLocation(RubberDuck.MODID, "rubber_duck_place")));
}
