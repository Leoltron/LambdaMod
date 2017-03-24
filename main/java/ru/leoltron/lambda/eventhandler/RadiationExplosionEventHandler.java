package ru.leoltron.lambda.eventhandler;

import ic2.api.event.ExplosionEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class RadiationExplosionEventHandler {

	@SubscribeEvent
	public void onIC2Explode(ExplosionEvent event){
		event.setCanceled(true);
	}

}
