package ru.leoltron.lambda.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import ru.leoltron.lambda.Lambda;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Lamp extends Block {

	@SideOnly(Side.CLIENT)
	private IIcon[] texture;
	
	final static String[] subblocks = new String[] {"thuliumLampFill","thuliumLampMove","eleriumLampFill","eleriumLampMove","promethionLampFill","promethionLampMove","spaceAlloyLampFill","spaceAlloyLampMove"};
	public Lamp() {
		super(Material.rock);
		this.setHardness(3.5F);
		this.setResistance(5.0F);
		this.setCreativeTab(Lambda.seasoneventCreativeTab);
		this.setLightLevel(0.625F);
	}
	
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister register){
		texture = new IIcon[subblocks.length];
		
		for(int i=0;i<subblocks.length;i++){
			texture[i] = register.registerIcon("seasonevent:"+subblocks[i]);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item block,CreativeTabs tabs,List list){
		for(int i=0;i<subblocks.length;i++){
			list.add(new ItemStack(block,1,i));
		}
	}
	
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side,int meta){
		return texture[meta];
	}
	
	public int damageDropped(int meta){
		return meta;
	}
	
	
	
}
