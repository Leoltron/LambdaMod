package ru.leoltron.lambda.gui;

import java.net.URI;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiShareToLan;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.PotionEffect;

public class GuiIngameMenuOverride extends GuiIngameMenu {
	
	@Override
	public void initGui()
    {
		super.initGui();
        Iterator<GuiButton> it = this.buttonList.iterator();
        while(it.hasNext()){
        	GuiButton button = it.next();
        	if(button.id == 7){
        		button.displayString =  I18n.format("gui.stats", new Object[0]);
        		button.enabled = true;
        	}
        	else if(button.id == 5)
        		button.displayString =  this.mc.gameSettings.language.equalsIgnoreCase("ru_RU") ? "\u0421\u0430\u0439\u0442" : "Website";
        	else if(button.id == 6)
        		button.displayString =  this.mc.gameSettings.language.equalsIgnoreCase("ru_RU") ? "\u0424\u043E\u0440\u0443\u043C" : "Forum";
        	else if(button.id == 0)
        		button.width = 200;
        	else if(button.id == 12)
        		it.remove();
        		
        }
    }   
	
	 protected void actionPerformed(GuiButton p_146284_1_)
	    {
	        switch (p_146284_1_.id)
	        {
	            case 0:
	                this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
	                break;
	            case 1:
	                p_146284_1_.enabled = false;
	                this.mc.theWorld.sendQuittingDisconnectingPacket();
	                this.mc.loadWorld((WorldClient)null);
	                this.mc.displayGuiScreen(new GuiMainMenu());
	            case 2:
	            case 3:
	            default:
	                break;
	            case 4:
	                this.mc.displayGuiScreen((GuiScreen)null);
	                this.mc.setIngameFocus();
	                break;
	            case 5:
	            	try
	                {
	                    Class oclass = Class.forName("java.awt.Desktop");
	                    Object object = oclass.getMethod("getDesktop", new Class[0]).invoke((Object)null, new Object[0]);
	                    oclass.getMethod("browse", new Class[] {URI.class}).invoke(object, new Object[] {new URI(GuiMainMenuOverride.websiteUrl)});
	                }
	                catch (Throwable throwable)
	                {
	                	GuiMainMenuOverride.logger.error("Couldn\'t open link", throwable);
	                }
	                break;
	            case 6:
	            	try
	                {
	                    Class oclass = Class.forName("java.awt.Desktop");
	                    Object object = oclass.getMethod("getDesktop", new Class[0]).invoke((Object)null, new Object[0]);
	                    oclass.getMethod("browse", new Class[] {URI.class}).invoke(object, new Object[] {new URI(GuiMainMenuOverride.websiteUrl+"forum/index.php")});
	                }
	                catch (Throwable throwable)
	                {
	                	GuiMainMenuOverride.logger.error("Couldn\'t open link", throwable);
	                }
	                break;
	            case 7:
	            	if (this.mc.thePlayer != null)
		                this.mc.displayGuiScreen(new GuiStats(this, this.mc.thePlayer.getStatFileWriter()));
	                break;
	            case 12:
	                FMLClientHandler.instance().showInGameModOptions(this);
	                break;
	        }
	    }
	
	
}


