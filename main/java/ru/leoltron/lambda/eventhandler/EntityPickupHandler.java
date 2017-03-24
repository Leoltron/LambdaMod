package ru.leoltron.lambda.eventhandler;

import java.util.ArrayList;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import ru.leoltron.lambda.Lambda;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EntityPickupHandler {
	

	private ArrayList<Item> radiationItems;
	
	private Item getIC2Item_(String name){
		return Lambda.getIC2Item(name).getItem();
	}

	public EntityPickupHandler() {
        radiationItems = new ArrayList<Item>();
		
		radiationItems.add(getIC2Item_("UranFuel"));
		radiationItems.add(getIC2Item_("MOXFuel"));
		radiationItems.add(getIC2Item_("Plutonium"));
		radiationItems.add(getIC2Item_("smallPlutonium"));
		radiationItems.add(getIC2Item_("Uran235"));
		radiationItems.add(getIC2Item_("smallUran235"));
		radiationItems.add(getIC2Item_("Uran238"));
		radiationItems.add(getIC2Item_("reactorDepletedUraniumSimple"));
		radiationItems.add(getIC2Item_("reactorDepletedUraniumDual"));
		radiationItems.add(getIC2Item_("reactorDepletedUraniumQuad"));
		radiationItems.add(getIC2Item_("reactorDepletedMOXSimple"));
		radiationItems.add(getIC2Item_("reactorDepletedMOXDual"));
		radiationItems.add(getIC2Item_("reactorDepletedMOXQuad"));
		radiationItems.add(getIC2Item_("reactorMOXSimple"));
		radiationItems.add(getIC2Item_("reactorMOXDual"));
		radiationItems.add(getIC2Item_("reactorMOXQuad"));
		radiationItems.add(getIC2Item_("RTGPellets"));
		radiationItems.add(getIC2Item_("reactorUraniumSimple"));
		radiationItems.add(getIC2Item_("reactorUraniumDual"));
		radiationItems.add(getIC2Item_("reactorUraniumQuad"));
	}

	
	@SubscribeEvent
	public void onPickupEvent(EntityItemPickupEvent event){
		//event.entityPlayer.addChatMessage(new ChatComponentText("\u00A72"+"boom"));   	
		if(radiationItems.contains(event.item.getEntityItem().getItem())){
			
			ItemStack[] armor = event.entityPlayer.inventory.armorInventory;
			if(armor[0] == null || armor[1] == null || armor[2] == null || armor[3] == null){
				event.setCanceled(true);
			} else{
				if (!(armor[0].getItem().equals(getIC2Item_("hazmatBoots")) && armor[1].getItem().equals(getIC2Item_("hazmatLeggings")) && armor[2].getItem().equals(getIC2Item_("hazmatChestplate")) && armor[3].getItem().equals(getIC2Item_("hazmatHelmet")))){
					event.setCanceled(true);
				}
			}
		}
		
	}
	
}
