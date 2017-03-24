package ru.leoltron.lambda.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import ru.leoltron.lambda.Lambda;
import ru.leoltron.lambda.tileentity.TileEntityHarvester;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Harvester extends BlockContainer {

	private boolean isAcitve;
	private int outputChance;
	private int rareChance;
	private int eUConsumption;
	private int type;
	
	@SideOnly(Side.CLIENT)
	private IIcon iconFront;
	
	@SideOnly(Side.CLIENT)
	private IIcon iconTop;
	
	private String textureLoc = "seasonevent:harvester";
	
	private static boolean keepInventory;
	private Random rand = new Random();
	
	public Harvester(boolean isActive,int type) { // 0 - примитивный, 1 - стабильный, 2 - укрепленный, 3 - прототипный
		super(Material.iron);
		this.type=type;
		this.isAcitve = isActive;
		this.outputChance = Lambda.outputChance[type];
		this.rareChance = Lambda.rareChance[type];
		this.eUConsumption = Lambda.eUConsumption[type];
		if (!isActive)
			this.setCreativeTab(Lambda.seasoneventCreativeTab);
		
		if (this.type == 0)  textureLoc = textureLoc + "/primitive/";
		else if (this.type == 1)  textureLoc = textureLoc + "/stable/";
		else if (this.type == 2)  textureLoc = textureLoc + "/reinforced/";
		else if (this.type == 3)  textureLoc = textureLoc + "/prototype/";
		
		this.setHardness(3.5F);
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister register){
		
		this.blockIcon = register.registerIcon(textureLoc+"harvester_side");
		this.iconFront = register.registerIcon(textureLoc+"harvester_front");//(this.isAcitve ? "HarvesterFrontOn" : "HarvesterFrontOff"));
	    this.iconTop = 	register.registerIcon(textureLoc+(this.isAcitve ? "harvester_top_on" : "harvester_top_off"));

	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int metadata){
		if (metadata == 0)
			return side == 3 ? this.iconFront : (side == 1 ? this.iconTop : (side == 0 ? this.iconTop : (side != metadata ? this.blockIcon : this.iconFront)));
		else
			return side == metadata ? this.iconFront : (side == 1 ? this.iconTop : this.blockIcon);
	}
	
	@Override
	public Item getItemDropped(int a,Random random,int b){
		if (this.type == 0) return Item.getItemFromBlock(Lambda.HarvesterPrimitiveBlockIdle);
		else if (this.type == 1) return Item.getItemFromBlock(Lambda.HarvesterStableBlockIdle);
		else if (this.type == 2) return Item.getItemFromBlock(Lambda.HarvesterReinforcedBlockIdle);
		return Item.getItemFromBlock(Lambda.HarvesterPrototypeBlockIdle);
	} 
	
    @Override
	public Item getItem(World world,int x,int y,int z){
    	if (this.type == 0) return Item.getItemFromBlock(Lambda.HarvesterPrimitiveBlockIdle);
		else if (this.type == 1) return  Item.getItemFromBlock(Lambda.HarvesterStableBlockIdle);
		else if (this.type == 2) return  Item.getItemFromBlock(Lambda.HarvesterReinforcedBlockIdle);
		return Item.getItemFromBlock(Lambda.HarvesterPrototypeBlockIdle);	
	}
	
	@Override
	public void onBlockAdded(World world,int x,int y,int z){
		
		super.onBlockAdded(world,x,y,z);
		this.setDefaultDirection(world,x,y,z);
	}

	private void setDefaultDirection(World world, int x, int y, int z) {
		if (!world.isRemote){
			Block b1 = world.getBlock(x, y, z-1);
			Block b2 = world.getBlock(x, y, z+1);
			Block b3 = world.getBlock(x-1, y, z);
			Block b4 = world.getBlock(x+1, y, z);
			byte b0=3;
			
			if (b1.func_149730_j() && !b2.func_149730_j())
				b0 = 3;
			if (b2.func_149730_j() && !b1.func_149730_j())
				b0 = 2;
			if (b3.func_149730_j() && !b4.func_149730_j())
				b0 = 5;
			if (b4.func_149730_j() && !b3.func_149730_j())
				b0 = 4;
			
			world.setBlockMetadataWithNotify(x, y, z, b0, 2);
		}
	}
	
	@Override
	public boolean onBlockActivated(World world, int x,int y,int z,EntityPlayer player,int side,float hitX,float HitY,float hitZ){
		if(!world.isRemote){
			FMLNetworkHandler.openGui(player,Lambda.instance , Lambda.guiIDHarvester, world, x, y, z);
		}
		
		return true;
	}
	
	 @Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z)
	    {
		 if (world.getBlock(x, y - 1, z) instanceof HarvesterSetPoint){
			 HarvesterSetPoint block = (HarvesterSetPoint) world.getBlock(x, y - 1, z);
			 switch (this.type){
			 case 0: return block.getType() <3;
			 case 1: return block.getType() <3 || block.getType() == 6;
			 case 2: return block.getType() <5 || block.getType() == 6 || block.getType() == 7;
			 case 3: return true;
			 }
		 }
	        return false;
	    }
	 
	 @Override
	public boolean canBlockStay(World world, int x, int y, int z)
	 {
		 return canPlaceBlockAt( world,  x,  y,  z);
	 }

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityHarvester(this.type);
	}
	
	/*@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world,int x,int y,int z,Random random){
		if(this.isAcitve){
			int dir = world.getBlockMetadata(x, y, z);
			
			float x1= (float)x+0.5F;
			float y1= (float)x+random.nextFloat();
			float z1= (float)x+0.5F;
			
			float f = 0.52F;
			float f1 = random.nextFloat()*0.6F-0.3F;
			
			if(dir == 4){
				world.spawnParticle("smoke", (double)(x1-f), (double)(y1), (double)(z1+f1), 0D, 0D, 0D);
			} else if(dir == 5){
				world.spawnParticle("smoke", (double)(x1+f), (double)(y1), (double)(z1+f1), 0D, 0D, 0D);
			} else if(dir == 2){
				world.spawnParticle("smoke", (double)(x1+f1), (double)(y1), (double)(z1-f), 0D, 0D, 0D);
			} else if(dir == 3){
				world.spawnParticle("smoke", (double)(x1+f1), (double)(y1), (double)(z1+f), 0D, 0D, 0D);
			}
		}
	}*/
	
	@Override
	public void onBlockPlacedBy(World world,int x,int y,int z,EntityLivingBase entity,ItemStack stack){
	//	if(world.getBlock(x, y-1, z) instanceof HarvesterSetPoint){
			
		int l = MathHelper.floor_double(entity.rotationYaw*4.0F/360.F+0.5D) & 3;
		
		if (l == 0) {
			world.setBlockMetadataWithNotify(x, y, z,2, 2);
		}
		if (l == 1) {
			world.setBlockMetadataWithNotify(x, y, z,5, 2);
		}
		if (l == 2) {
			world.setBlockMetadataWithNotify(x, y, z,3, 2);
		}
		if (l == 3) {
			world.setBlockMetadataWithNotify(x, y, z,4, 2);
		}
		if (stack.hasDisplayName()){
			((TileEntityHarvester)world.getTileEntity(x, y, z)).setGuiDisplayName(stack.getDisplayName());
		}
		/*} else if(entity instanceof EntityPlayer && world.getBlock(x, y, z) instanceof Harvester){
			EntityPlayer player = (EntityPlayer) entity;
			int t = ((Harvester) world.getBlock(x, y, z)).type;
			Item b;
			if (t == 0) b=Item.getItemFromBlock(DzettaModpack.HarvesterExperimentalBlockIdle);
			else if (t == 1) b= Item.getItemFromBlock(DzettaModpack.HarvesterStableBlockIdle);
			else if (t == 3) b= Item.getItemFromBlock(DzettaModpack.HarvesterPrototypeBlockIdle);
			else b= Item.getItemFromBlock(DzettaModpack.HarvesterReinforcedBlockIdle);
			world.setBlock(x, y, z, Blocks.air);
			player.inventory.addItemStackToInventory(new ItemStack(b));
		}*/
	}

	public static void updateHarvesterBlockState(boolean active, World worldObj,
			int xCoord, int yCoord, int zCoord,int type) {
		int i = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		
		TileEntity tileentity = worldObj.getTileEntity(xCoord, yCoord, zCoord);
		
		keepInventory = true;
		
		Block blockActive;
		Block blockIdle;
		
		if (type == 1){
			blockIdle = Lambda.HarvesterStableBlockIdle;
			blockActive = Lambda.HarvesterStableBlockActive;
		}else if (type == 2){
			blockIdle = Lambda.HarvesterReinforcedBlockIdle;
			blockActive = Lambda.HarvesterReinforcedBlockActive;
		}else if (type == 3){
			blockIdle = Lambda.HarvesterPrototypeBlockIdle;
			blockActive = Lambda.HarvesterPrototypeBlockActive;
		}else {
			blockIdle = Lambda.HarvesterPrimitiveBlockIdle;
			blockActive = Lambda.HarvesterPrimitiveBlockActive;
		}
		
		if(active){
			worldObj.setBlock(xCoord, yCoord, zCoord, blockActive);
		}else{
			worldObj.setBlock(xCoord, yCoord, zCoord, blockIdle);
		}
		
		keepInventory = false;
		
		worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, i, 2);
		
		if(tileentity != null){
			tileentity.validate();
			worldObj.setTileEntity(xCoord, yCoord, zCoord, tileentity);
		}
		
	}
	
	@Override
	public void breakBlock(World world,int x, int y,int z,Block oldblock,int oldMetadata){
		if(!keepInventory){
			TileEntityHarvester tileentity = (TileEntityHarvester) world.getTileEntity(x, y, z);
			
			if(tileentity != null){
				for (int i = 0; i<tileentity.getSizeInventory();i++){
					ItemStack itemstack = tileentity.getStackInSlot(i);
					if(itemstack != null){
						float f = this.rand.nextFloat()*0.8F+0.1F;
						float f1 = this.rand.nextFloat()*0.8F+0.1F;
						float f2 = this.rand.nextFloat()*0.8F+0.1F;
						
						while(itemstack.stackSize >0){
							int j = this.rand.nextInt(21)+10;
							
							if(j > itemstack.stackSize){
								j = itemstack.stackSize;
							}
							itemstack.stackSize -=j;
							EntityItem item = new EntityItem(world,(x+f),(y+f1),(z+f2), new ItemStack(itemstack.getItem(),j,itemstack.getItemDamage()));
						
						    if(itemstack.hasTagCompound()){
						    	item.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
						    }
						    world.spawnEntityInWorld(item);
						}
					}
				}
				world.func_147453_f(x, y, z, oldblock);
			}
			
		}
		super.breakBlock(world, x, y, z, oldblock, oldMetadata);
	}
	
	public int getType(){
		return this.type;
	}
	
	public boolean isActive(){
		return this.isAcitve;
	}	

}
