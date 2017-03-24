package ru.leoltron.lambda.eventhandler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class PlayerInteractEventHandler {
	private static final String[] tools = new String[]{"item.appliedenergistics2.ToolNetworkTool","item.graviTool","ic2.itemToolWrenchElectric","item.appliedenergistics2.ToolNetherQuartzWrench","item.appliedenergistics2.ToolCertusQuartzWrench","ic2.itemToolWrench"};
	
	
	private boolean isTool(String s){
		for(String toolName:tools)
			if(toolName.equals(s)) 
				return true;
		
		return false;
	}
	
	@SubscribeEvent
	public void onPlayerInteract(PlayerInteractEvent event){
//		if(event.entityPlayer.inventory.getCurrentItem() != null)
//			event.entityPlayer.addChatMessage(new ChatComponentText(event.action.name()+" "+event.entityPlayer.inventory.getCurrentItem().getUnlocalizedName()));
//		event.entityPlayer.addChatComponentMessage(new ChatComponentText("Action: " + event.action.toString()));
//		if(event.entityPlayer.inventory.getCurrentItem() != null)
//			event.entityPlayer.addChatComponentMessage(new ChatComponentText("Current item/block: " + event.entityPlayer.inventory.getCurrentItem().getUnlocalizedName()+", damage:  "+event.entityPlayer.inventory.getCurrentItem().getItemDamage()));
		
		if(event.action.equals(PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)){
			if(event.entityPlayer.inventory.getCurrentItem() == null)
				return;
			
			ItemStack item = event.entityPlayer.inventory.getCurrentItem();
			
			if(isTool(item.getUnlocalizedName()) && event.entityPlayer.isSneaking()){
				event.setCanceled(true);
//				System.out.println("Canceled! Shift");
				return;
			}
			
			//event.entityPlayer.addChatComponentMessage(new ChatComponentText("Block: "+event.world.getBlock(event.x, event.y, event.z).getUnlocalizedName()));
			if(event.world.getBlock(event.x, event.y, event.z).getUnlocalizedName().equals("blockWall") && ( 
					item.getUnlocalizedName().contains("graviTool")
					|| item.getUnlocalizedName().contains("ic2.itemToolWrench"))
					){
				event.setCanceled(true);
//				System.out.println("Canceled! Wall");
				return;
			}
		}
	}
}
