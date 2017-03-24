package ru.leoltron.lambda.blocks;

import ic2.api.info.Info;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class RadiationAir extends Block {

	public RadiationAir() {
		super(Material.air);
	}
	
	public AxisAlignedBB getCollisionBoundingsu_boxFromPool(World par1World, int par2, int par3, int par4)
    {
        return null;
    }
 
    public boolean isOpaqueCube()
    {
        return false;
    }
 
    public boolean renderAsNormalBlock()
    {
        return false;
    }
    
    public int getRenderType()
    {
        return 8;
    }
    
    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        float f = 0.0000F;
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.0F, f, 0.0F);
    }
    
    public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity entity)
    {
        entity.attackEntityFrom(Info.DMG_RADIATION, 1.0F);
    }

}
