package ru.leoltron.lambda.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import ru.leoltron.lambda.blocks.JumpBlock;

public class ItemJumpBlock extends ItemBlock {

	public ItemJumpBlock(Block block) {
		super(block);
		this.setHasSubtypes(true);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemstack){
		int damage = itemstack.getItemDamage();
		if(damage < 0 || damage >= JumpBlock.names.length){
			damage = 0;
		}
		
		return super.getUnlocalizedName()+"."+JumpBlock.names[damage];
	}
	
	@Override
	public int getMetadata(int meta){
		return meta;
	}

}
