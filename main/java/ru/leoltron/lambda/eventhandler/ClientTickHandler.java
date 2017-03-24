package ru.leoltron.lambda.eventhandler;

import java.io.File;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.crash.CrashReport;

public class ClientTickHandler {
	
	int time = 0;

	public ClientTickHandler() {}
	
	private static File fileXRay = new File("config/XRay");
	private static File fileCE = new File("config/Cheating Essentials");
	private static File fileC_E = new File("config/Cheating-Essentials");
	private static File file = new File("config");
	@SubscribeEvent
	public void onClientTickEvent(ClientTickEvent event){
		if(event.phase.equals(TickEvent.Phase.START)){
			if (time >=10){
			if(file.exists() && (fileXRay.exists()  || fileCE.exists() || fileC_E.exists())){
				Minecraft.getMinecraft().crashed(new CrashReport(null, null));
			}
			time = 0;
			}else{
				time++;
			}
		}		
	}

}
