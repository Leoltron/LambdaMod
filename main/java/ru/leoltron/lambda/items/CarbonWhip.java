package ru.leoltron.lambda.items;

import java.util.List;
import java.util.Set;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;

public class CarbonWhip extends Item {
	
	protected int maxStackSize = 1;
	
	public CarbonWhip(){
		this.bFull3D = true;
		this.setCreativeTab(CreativeTabs.tabTools);
		this.setTextureName("seasonevent:carbon_whip");
	}
	
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List p_77624_3_, boolean p_77624_4_) {
    	if (p_77624_2_.getDisplayName().indexOf("Cationa") != -1){
    		p_77624_3_.add("\u041E\u0431\u043B\u0430\u0434\u0430\u0435\u0442 \u0437\u043D\u0430\u0447\u0438\u0442\u0435\u043B\u044C\u043D\u043E\u0439 \u043C\u043E\u0449\u044C\u044E...");
    	}else{p_77624_3_.add("\u0412 \u043D\u0443\u0436\u043D\u044B\u0445 \u0440\u0443\u043A\u0430\u0445 \u043E\u0431\u043B\u0430\u0434\u0430\u0435\u0442 \u0437\u043D\u0430\u0447\u0438\u0442\u0435\u043B\u044C\u043D\u043E\u0439 \u043C\u043E\u0449\u044C\u044E...");
    	}
    }
    
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
    {
    	if (entity instanceof EntityPlayer){
    		EntityPlayer pl = (EntityPlayer) entity;
    		if (player.getDisplayName().indexOf("Cationa") != -1 && pl.getDisplayName().indexOf("Leolt") == -1){   	
    			pl.attackEntityFrom(DamageSource.causePlayerDamage(player), 6);
    			pl.addPotionEffect(new PotionEffect(Potion.moveSpeed.getId(), 100,1));
    		}else{
    			pl.addPotionEffect(new PotionEffect(Potion.moveSpeed.getId(), 60,0));
    		}
    	}
    	entity.worldObj.playSoundAtEntity(entity,"seasonevent:whip_crack", 1.0F, entity.worldObj.rand.nextFloat() * 0.1F + 0.9F);
        return true;
    }
}
