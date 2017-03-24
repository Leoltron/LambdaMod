package ru.leoltron.lambda.items;

import net.minecraft.block.Block;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemLamp extends ItemBlock {
	
	final static String[] subblocks = new String[] {"thuliumLampFill","thuliumLampMove","eleriumLampFill","eleriumLampMove","promethionLampFill","promethionLampMove","spaceAlloyLampFill","spaceAlloyLampMove"};

	public ItemLamp(Block block) {
		super(block);
		this.setHasSubtypes(true);
	}
	
	public String getUnlocalizedName(ItemStack stack){
		int i = stack.getItemDamage();
		if(i< 0 || i >= subblocks.length){
			i = 0;
		}
		
		return "tile."+subblocks[i];
	}
	
	public int getMetadata(int meta){
		return meta;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(ItemStack stack){
		/*int i = stack.getItemDamage();
		switch(i){
		case 7: return EnumRarity.rare;
		case 6: return EnumRarity.rare;
		case 5: return EnumRarity.uncommon;
		case 4: return EnumRarity.uncommon;
		default:*/ return EnumRarity.common;
		//}
	}

}
