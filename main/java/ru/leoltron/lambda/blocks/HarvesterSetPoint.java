package ru.leoltron.lambda.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import ru.leoltron.lambda.Lambda;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class HarvesterSetPoint extends Block {
	
	private int type;
	
	private String[] typeName = {"iron","copper","tin","diamond","uranium","iridium","thulium","elerium","promethion"};
	
	@SideOnly(Side.CLIENT)
	private IIcon iconTop;

	public HarvesterSetPoint(int type) {
		super(Material.rock);
		this.type = type;
		this.setCreativeTab(Lambda.seasoneventCreativeTab);
		this.setHardness(3.5F);
	}
	
	public int getType(){
		return this.type;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister register){
		if(type < 3 || type == 6){
			blockIcon = register.registerIcon("seasonevent:harvester/stable/harvester_side");
		} else if (type <5 || type == 7){
			blockIcon = register.registerIcon("seasonevent:harvester/reinforced/harvester_side");
		} else{
			blockIcon = register.registerIcon("seasonevent:harvester/prototype/harvester_side");
		}
	    this.iconTop = 	register.registerIcon("seasonevent:"+typeName[type]+"_"+"harvester_set_point_top");

	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int metadata){
		return side == 1 ? this.iconTop : this.blockIcon;
	}
	
	@Override
	public int getMobilityFlag()
    {
        return 2;
    }
	
	@Override
	public void breakBlock(World world,int x, int y,int z,Block oldblock,int oldMetadata){
		super.breakBlock(world, x, y, z, oldblock, oldMetadata);
		if (world.getBlock(x, y+1, z) instanceof Harvester){
			float f = world.rand.nextFloat()*0.8F+0.1F;
			float f1 = world.rand.nextFloat()*0.8F+0.1F;
			float f2 = world.rand.nextFloat()*0.8F+0.1F;
			
			EntityItem item = new EntityItem(world,(x+f),(y+1+f1),(z+f2), new ItemStack(world.getBlock(x, y+1, z).getItem(world, x, y+1, z)));

			world.getBlock(x, y+1, z).breakBlock(world, x, y+1, z, world.getBlock(x, y+1, z), world.getBlockMetadata(x, y+1, z));
			
			world.setBlockToAir(x, y+1, z);
			world.spawnEntityInWorld(item);
		}
	}

}
