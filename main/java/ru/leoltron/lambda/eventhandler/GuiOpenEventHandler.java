package ru.leoltron.lambda.eventhandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.settings.GameSettings.Options;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import ru.leoltron.lambda.gui.*;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GuiOpenEventHandler {
	
	public static boolean debug = false;	
	

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onOpen(GuiOpenEvent event){
		if(event.gui == null)
			return;
		if(event.gui instanceof GuiIngameMenu){
				event.gui = new GuiIngameMenuOverride();
		}else if(event.gui instanceof GuiMainMenu){
			if(debug){
				debug = false;
				return;
			}else{
				event.gui = new GuiMainMenuOverride((GuiMainMenu) event.gui);
			}
		}
	}
		
	
}
