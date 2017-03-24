package ru.leoltron.lambda.items;

import java.util.List;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import ru.leoltron.lambda.Lambda;

public class ResearchRelic extends Item {
	
	private int value;
	
	static final String[] subitems = {"small","medium","big"};
	private IIcon[] icons = new IIcon[subitems.length];

	public ResearchRelic(int val) {
		this.setCreativeTab(Lambda.seasoneventCreativeTab);
		this.setHasSubtypes(true);
		value = val; 
	}
	
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List p_77624_3_, boolean p_77624_4_) {
		int count = value;
		for(int i=0;i< stack.getItemDamage();i++){
			count = count * value;
		}
		p_77624_3_.add("\u0421\u043E\u0434\u0435\u0440\u0436\u0438\u0442 "+count+" \u043E\u0447\u043A\u043E\u0432 \u043F\u0440\u043E\u0433\u0440\u0435\u0441\u0441\u0430");
		/*if(Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
			p_77624_3_.add("Shift+\u041F\u041A\u041C, \u0447\u0442\u043E\u0431\u044B \u043F\u043E\u043B\u0443\u0447\u0438\u0442\u044C \u0432\u0441\u0435 \u043E\u0447\u043A\u0438");
			p_77624_3_.add("\u0441\u0440\u0430\u0437\u0443 \u0438\u043B\u0438 \u043F\u043E\u043C\u0435\u0441\u0442\u0438\u0442\u0435 \u0432 \u043E\u043A\u043D\u043E \u043A\u0440\u0430\u0444\u0442\u0430");
			p_77624_3_.add("\u0434\u043B\u044F \u043F\u043E\u0441\u0442\u0435\u043F\u0435\u043D\u043D\u043E\u0433\u043E \u043F\u0440\u0435\u043E\u0431\u0440\u0430\u0437\u043E\u0432\u0430\u043D\u0438\u044F.");
		}*/
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(ItemStack stack){
		switch(stack.getItemDamage()){
		case 2: return EnumRarity.rare;
		case 1: return EnumRarity.uncommon;
		default: return EnumRarity.common;
		}
	}
	
	public String getUnlocalizedName(ItemStack stack)
    {
        return "item." + subitems[stack.getItemDamage() > 2 ? 0 : stack.getItemDamage()]+"ResearchRelic";
    }
	
	@SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list)
    {
		for (int i=0;i<subitems.length;i++){
			list.add(new ItemStack(item, 1, i));
		}
    }
	
	@SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int damage)
    {
        return icons[damage];
    }
	
	@SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register)
    {
        for(int i=0;i<subitems.length;i++){
        	icons[i] = register.registerIcon("seasonevent:"+subitems[i]+"_research_relic");
        }
    }
	
	 public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	    { if (player.isSneaking()){
		    if(!player.capabilities.isCreativeMode){
		    	stack.stackSize--;
		    }
		    int damage = stack.getItemDamage();
		    if( damage > 0){
		    	if(damage >1){
		    		for(int i=0;i<value*value;i++){
		    			if (!player.inventory.addItemStackToInventory(new ItemStack(Lambda.RPCoin,value))){
		    		    	player.dropItem(Lambda.RPCoin, value);
		    		    	
		    			}
		    		}
		    	} else{
		    		for(int i=0;i<value;i++){
		    			if (!player.inventory.addItemStackToInventory(new ItemStack(Lambda.RPCoin,value))){
				    		player.dropItem(Lambda.RPCoin, value);
				    	}
		    		}
		    	}
		    } else{
		    	if (!player.inventory.addItemStackToInventory(new ItemStack(Lambda.RPCoin,value))){
		    		player.dropItem(Lambda.RPCoin, value);
		    	}
		    }
	    }
	        return stack;
	    }

}
