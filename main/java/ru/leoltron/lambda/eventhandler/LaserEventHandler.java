package ru.leoltron.lambda.eventhandler;

import ic2.api.event.LaserEvent;
import ic2.api.event.LaserEvent.LaserHitsBlockEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class LaserEventHandler {
	
//	@SubscribeEvent
//	public void onBlockHit(LaserHitsBlockEvent event){
//		event.setCanceled(true);
//	}
	
	@SubscribeEvent
	public void onEntityDamage(LaserEvent.LaserHitsEntityEvent event){
		event.lasershot.setDead();
		event.setCanceled(true);
	}
}
