package ru.leoltron.lambda.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;
import ru.leoltron.lambda.tileentity.TileEntityHarvester;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerHarvester extends Container{

	private TileEntityHarvester harvester;
	
	public int lastReceiveTime = 0;
	public int lastEnergyAmount = 0;
	public int harvesterSpeed = 60*20;
	
	public ContainerHarvester(InventoryPlayer inventory,TileEntityHarvester entity){
		this.harvester = entity;
		
		//this.addSlotToContainer(new Slot(entity,0,8,63));
		this.addSlotToContainer(new SlotFurnace(inventory.player,entity,0,76+4,15+4));
		

		for(int i = 0; i< 3; i++){
			for(int g = 0; g<9; g++){
				this.addSlotToContainer(new Slot(inventory,g+i*9+9,8+g*18,84+i*18));
			}
		}
		
		
		for(int i = 0;i<9;i++){
			this.addSlotToContainer(new Slot(inventory,i,8+i*18,142));
		}
	}
	
	public void addCraftingToCrafters(ICrafting icrafting){
		super.addCraftingToCrafters(icrafting);
		icrafting.sendProgressBarUpdate(this, 0, this.harvester.receiveTime); 
		icrafting.sendProgressBarUpdate(this, 1, (int) this.harvester.energyAmount);
	} 
	
	public void detectAndSendChanges(){
		super.detectAndSendChanges();
		for (int i = 0; i< this.crafters.size(); i++){
			ICrafting icrafting = (ICrafting) this.crafters.get(i);
			
			if(this.lastReceiveTime != this.harvester.receiveTime){
				icrafting.sendProgressBarUpdate(this, 0, this.harvester.receiveTime);
			}
			
			if(this.lastEnergyAmount != (int) this.harvester.energyAmount || (this.harvester.energyAmount > this.harvester.getEUConsumption()-this.harvester.eUPerTickConsumption*2)){
				icrafting.sendProgressBarUpdate(this, 1, (int) this.harvester.energyAmount);
			}
		}
		this.lastReceiveTime = this.harvester.receiveTime;
		this.lastEnergyAmount = (int) this.harvester.energyAmount;
	}
	
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int slot, int newValue){
		if(slot == 0){
			harvester.receiveTime = newValue;
		} 
		if (slot == 1){
			harvester.energyAmount = newValue;
		}
	}
	
	
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}
	
	public ItemStack transferStackInSlot(EntityPlayer p_82846_1_, int p_82846_2_)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(p_82846_2_);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
             if ( p_82846_2_ != 0)
            {
               /* if (FurnaceRecipes.smelting().getSmeltingResult(itemstack1) != null)
                {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false))
                    {
                        return null;
                    }
                }
                else*/
            	 if (p_82846_2_ >= 1 && p_82846_2_ < 29)
                {
                    if (!this.mergeItemStack(itemstack1, 29, 37, false))
                    {
                        return null;
                    }
                }
                else if (p_82846_2_ >= 29 && p_82846_2_ < 37 && !this.mergeItemStack(itemstack1, 1, 29, false))
                {
                    return null;
                }
            }
             else
             {
                 if (!this.mergeItemStack(itemstack1, 1, 37, true))
                 {
                     return null;
                 }

                 slot.onSlotChange(itemstack1, itemstack);
             }

            if (itemstack1.stackSize == 0)
            {
                slot.putStack((ItemStack)null);
            }
            else
            {
                slot.onSlotChanged();
            }
            

            if (itemstack1.stackSize == itemstack.stackSize)
            {
                return null;
            }

            slot.onPickupFromSlot(p_82846_1_, itemstack1);
        }

        return itemstack;
    }

}
