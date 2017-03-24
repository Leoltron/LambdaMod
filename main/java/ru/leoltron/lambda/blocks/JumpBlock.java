package ru.leoltron.lambda.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import ru.leoltron.lambda.Lambda;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class JumpBlock extends Block{
	
	@SideOnly(Side.CLIENT)
	private IIcon[] bottomIcons;
	
	@SideOnly(Side.CLIENT)
	private IIcon[] sideIcons;
	
	@SideOnly(Side.CLIENT)
	private IIcon[] topIcons;
	
	public static final String[] names = {"weak","medium","powerful","superPowerful"};
	public static final String texturename = "seasonevent:jumpBlock/";
	
	public static final float[] jumpVelocity = {0.5F,1.0F,1.5F,3.0F};

	public JumpBlock() {
		super(Material.iron);
		setBlockUnbreakable();
		setCreativeTab(Lambda.seasoneventCreativeTab);
		setLightLevel(0.625F);
	}
	
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return side == 0 ? bottomIcons[meta] : (side == 1 ? topIcons[meta] : sideIcons[meta]);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item block, CreativeTabs ct, List list) {
		for(int i=0; i < names.length; i++){
			list.add(new ItemStack(block,1,i));
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister register){
		bottomIcons = new IIcon[names.length];		
		sideIcons = new IIcon[names.length];		
		topIcons = new IIcon[names.length];
		
		for(int i=0; i < names.length; i++){
			bottomIcons[i] = register.registerIcon(texturename+names[i]+"_bottom");
			sideIcons[i] = register.registerIcon(texturename+names[i]+"_side");
			topIcons[i] = register.registerIcon(texturename+names[i]+"_top");
		}
	}


	@Override
	public String getUnlocalizedName() {
		return "tile.jumpBlock";
	}
}
