package com.github.salvadormg15.rubber_duck.client;

import com.github.salvadormg15.rubber_duck.client.render.DuckHeadLayer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraftforge.eventbus.api.IEventBus;

public class ClientEventHandler {
	
	public static void initClient(IEventBus ModBus, IEventBus ForgeBus) {
		EntityRendererManager manager = Minecraft.getInstance().getEntityRenderDispatcher();
    	for(PlayerRenderer renderer: manager.getSkinMap().values()) {
			renderer.addLayer(new DuckHeadLayer<>(renderer));
		}
	}
}
