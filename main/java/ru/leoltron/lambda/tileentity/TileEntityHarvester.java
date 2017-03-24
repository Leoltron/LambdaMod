package ru.leoltron.lambda.tileentity;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.item.IC2Items;
import java.util.Random;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import ru.leoltron.lambda.Lambda;
import ru.leoltron.lambda.blocks.Harvester;
import ru.leoltron.lambda.blocks.HarvesterSetPoint;

public class TileEntityHarvester extends TileEntity implements ISidedInventory,IEnergySink{
	
	private String localizedName;
	private int outputChance;
	private int rareChance;
	private int eUConsumption;
	private int type;
	private boolean tickedOnce = false;
	private boolean innet=false;
	
	private static Random rand = new Random();
	
	private static final int[] slots_ = new int[]{0};
	
	private ItemStack[] slots = new ItemStack[1];
	
	public int receiveTime = 0;
	public double energyAmount = 0;
	public double eUPerTickConsumption;
	
	public int harvesterFullTime = 20*60; //Время работы сборщика (в тиках)
	
	public static enum OutputItems{ // Железо Медь Олово   Алмаз Уран   Иридий
		IRON(0,new String[]{"ironDust"},new String[]{"crushedIronOre","purifiedCrushedIronOre"},new String[]{"stoneDust"}),
		COPPER(1,new String[]{"copperDust"},new String[]{"crushedCopperOre","purifiedCrushedCopperOre"},new String[]{"stoneDust"}),
		TIN(2,new String[]{"tinDust"},new String[]{"crushedTinOre","purifiedCrushedTinOre"},new String[]{"stoneDust"}),
		
		DIAMOND(3,new String[]{"diamondDust"},new String[]{"diamondDust"},new String[]{"coalDust"}),
		URANIUM(4,new String[]{"smallUran235","Uran238"},new String[]{"crushedUraniumOre","purifiedCrushedUraniumOre"},new String[]{"stoneDust"}),
		
		IRIDIUM(5,new String[]{"stoneDust"/*"smallIronDust","smallCopperDust","smallGoldDust","smallTinDust","smallLeadDust"*/},new String[]{"iridiumOre"},new String[]{"stoneDust"});
		
		private final int type;

		private final String[] outputItems;
		private final String[] rareItems;
		private final String[] trashItems;
		
		
		private OutputItems(int type, String[] outputItems, String[] rareItems, String[] trashItems){
			this.type = type;
			this.outputItems = outputItems;
			this.rareItems = rareItems;
			this.trashItems = trashItems;
		}
		
		public int getType(){
			return this.type;
		}
		
		public ItemStack getOutputItem(){
			return IC2Items.getItem(this.outputItems[rand.nextInt(this.outputItems.length)]).copy();
		}
		
		public ItemStack getRareItem(){
			if (this.type ==3){
				return new ItemStack(IC2Items.getItem("diamondDust").getItem(),1,2);
			} else{
				return IC2Items.getItem(this.rareItems[rand.nextInt(this.rareItems.length)]).copy();
			}
		}
		
		public ItemStack getTrashItem(){
			return IC2Items.getItem(this.trashItems[rand.nextInt(this.trashItems.length)]).copy();
		}
		
	}; 
	
	public TileEntityHarvester(){
		super();
	}
	
	public TileEntityHarvester(int type) {
		super();
		this.type = type;
		this.outputChance = Lambda.outputChance[type];
		this.rareChance = Lambda.rareChance[type];
		this.eUConsumption = Lambda.eUConsumption[type];
		this.eUPerTickConsumption = eUConsumption/harvesterFullTime;
	}

	public void setGuiDisplayName(String displayName){
		this.localizedName = displayName;
	}
	
	@Override
	public String getInventoryName(){
		return this.hasCustomInventoryName() ? this.localizedName : "container.harvester";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return this.localizedName != null && this.localizedName.length() > 0;
	}
	
	@Override
	public int getSizeInventory(){
		return this.slots.length;
	}
	
	public int getType(){
		return this.type;
	}
	

	@Override
	public ItemStack getStackInSlot(int slot) {
		return this.slots[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int num) {
		if (this.slots[slot]!= null){
			ItemStack itemstack;
			if(this.slots[slot].stackSize <= num){
				itemstack = this.slots[slot];
				this.slots[slot] = null;
				return itemstack;
			}else{
				itemstack = this.slots[slot].splitStack(num);
				
				if(this.slots[slot].stackSize == 0){
					this.slots[slot] = null;
				}
				return itemstack;
			}
		}
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		if(this.slots[slot] != null){
			ItemStack itemstack = this.slots[slot];
			this.slots[slot] = null;
			return itemstack;
		}
		return null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemstack) {
		this.slots[slot] = itemstack;
		if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit())
			itemstack.stackSize = this.getInventoryStackLimit();
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ?  false : player.getDistanceSq(xCoord +0.5D, yCoord+0.5D,zCoord+0.5D) <= 64.0D ;
	}

	@Override
	public void openInventory() {}
	@Override
	public void closeInventory() {}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack item) {
		return false;
	}

	@Override
	public void updateEntity(){
		
		Block block = worldObj.getBlock(xCoord, yCoord, zCoord);
		boolean flag = block instanceof Harvester ? ((Harvester) block).isActive() : this.receiveTime > 0;
		boolean flag1 = false;
		
		if(flag){
			receiveTime--;
			if (energyAmount- eUPerTickConsumption < 0){
				energyAmount = 0;
			} else{
				energyAmount =energyAmount- eUPerTickConsumption;
			}
		}
		if(!this.worldObj.isRemote){
			if(!innet){
				MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
				//System.out.println("Loaded!");
				innet = true;
			}
			if (energyAmount == 0 && receiveTime != 0){
				receiveTime = 0;
				flag = false;
				this.markDirty();
				Harvester.updateHarvesterBlockState(false,worldObj,xCoord,yCoord,zCoord,type);
			}
			if(this.receiveTime == 0) {
				if (flag){
					this.produceResourses();
					flag1 = true;
				}
				if (this.energyAmount > this.eUPerTickConsumption){
					this.receiveTime = this.harvesterFullTime;					
				}
			}
				
			if (flag != this.receiveTime > 0){
				this.markDirty();
				Harvester.updateHarvesterBlockState(energyAmount>eUPerTickConsumption,worldObj,xCoord,yCoord,zCoord,type);
				} 
			if (this.receiveTime >0 && (this.receiveTime+2) % 80 == 0){
				this.worldObj.playSoundEffect(xCoord, yCoord, zCoord, "seasonevent:harvester_working", 1.0F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
			}
			
		}
		if(flag1){
			this.markDirty();
		}
	}
		
	

	private ItemStack getResourses(){
		Block src = this.worldObj.getBlock(xCoord, yCoord-1, zCoord);
		if(src instanceof HarvesterSetPoint){
			int oType = ((HarvesterSetPoint) src).getType();

			double ch = MathHelper.getRandomDoubleInRange(rand, 0D, 1D)*100;
//			if (ch > 98){
//				if(oType == 6 || oType <3){
//					return new ItemStack(Lambda.ResearchRelic,1,0);
//				}else if (oType == 7 || oType < 5){
//					return new ItemStack(Lambda.ResearchRelic,1,1);
//				}else{
//					return new ItemStack(Lambda.ResearchRelic,1,2);
//				}
//			}
			if (oType > 5){
				if (ch <= Lambda.rareChance[type]/3){
					switch (oType){
					case 6: return new ItemStack(Lambda.CrushedThuliumOre);
					case 7: return new ItemStack(Lambda.CrushedEleriumOre);
					case 8: return new ItemStack(Lambda.CrushedPromethionOre);
					}
				} else{
					return IC2Items.getItem("stoneDust").copy();
				}
			} 
			OutputItems outputType = OutputItems.values()[oType];
			if (ch<=Lambda.rareChance[type]){
				if(oType == 3){
					return new ItemStack(IC2Items.getItem("diamondDust").getItem(),1,2);
				} else{
					return outputType.getRareItem();
				}
				} 
			if (ch<=Lambda.outputChance[type]+Lambda.rareChance[type]){
				int num;
				ItemStack stack = outputType.getOutputItem();
				if (type == 3){ 
					num = rand.nextInt(2)+4;
				}
				else if (type == 2){ 
					num = rand.nextInt(3)+3;
					}
				else if (type == 1){ 
					num = rand.nextInt(3)+2;
					}
				else {
					num = rand.nextInt(3)+1;
					}
				stack.stackSize = num;
				return stack;
				}
			return outputType.getTrashItem();
			}
		return null;
		
	}
	
	private void produceResourses() {
		ItemStack itemstack = this.getResourses();
		//System.out.println("Receiving "+itemstack.getItem().getUnlocalizedName()+" x"+itemstack.stackSize);
		if (this.slots[0] == null){
			this.slots[0] = itemstack;
			return;
		} else
		if(this.slots[0].getItem().equals(itemstack.getItem())){
			if(itemstack.getItem().getHasSubtypes()){
				if(itemstack.getItemDamage() == this.slots[0].getItemDamage()){
					if((itemstack.stackSize+this.slots[0].stackSize)<= itemstack.getMaxStackSize()){
						this.slots[0].stackSize =(itemstack.stackSize+this.slots[0].stackSize);
						return;
					} else{
						this.worldObj.spawnEntityInWorld(new EntityItem(this.worldObj, this.xCoord, this.yCoord+1.5D, this.zCoord, new ItemStack(this.slots[0].getItem(),itemstack.stackSize+this.slots[0].stackSize-this.slots[0].getMaxStackSize(),this.slots[0].getItemDamage())));
						this.slots[0].stackSize = this.slots[0].getMaxStackSize();
						return;
					}
				} 
			} else{
				if((itemstack.stackSize+this.slots[0].stackSize)<= itemstack.getMaxStackSize()){
					this.slots[0].stackSize =(itemstack.stackSize+this.slots[0].stackSize);
					return;
				} else{
					this.worldObj.spawnEntityInWorld(new EntityItem(this.worldObj, this.xCoord, this.yCoord+1.5D, this.zCoord, new ItemStack(this.slots[0].getItem(),itemstack.stackSize+this.slots[0].stackSize-this.slots[0].getMaxStackSize(),this.slots[0].getItemDamage())));
					this.slots[0].stackSize = this.slots[0].getMaxStackSize();
					return;
				}
			}
		}
			this.worldObj.spawnEntityInWorld(new EntityItem(this.worldObj, this.xCoord, this.yCoord+1.5D, this.zCoord,itemstack));
			return;
		
		
	}
	
	public boolean isWorking(){
		return this.receiveTime > 0;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return slots_;
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack itemstack,
			int side) {
		return false;
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack itemstack,
			int side) {
		return true;
	}
	
	public int getReceiveProgressRemainingScale(int i){
		return receiveTime * i / harvesterFullTime;
	}
	
	public double getEnergyAmountScaled(int i){
		return energyAmount * i / eUConsumption;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt){
		super.readFromNBT(nbt);
		
		NBTTagList list = nbt.getTagList("Item",10);
		this.slots = new ItemStack[this.getSizeInventory()];
		
		for(int i = 0; i < list.tagCount(); i++){
			NBTTagCompound compound = list.getCompoundTagAt(i);
			byte b = compound.getByte("Slot");
			
			if(b >= 0 && b < this.slots.length){
				this.slots[b] = ItemStack.loadItemStackFromNBT(compound);
			}
		}
		
		this.receiveTime = nbt.getShort("ReceiveTime");
		this.energyAmount = nbt.getDouble("EnergyAmount");
		this.type = nbt.getShort("Type");
		this.outputChance = Lambda.outputChance[type];
		this.rareChance = Lambda.rareChance[type];
		this.eUConsumption = Lambda.eUConsumption[type];
		this.eUPerTickConsumption = eUConsumption/harvesterFullTime;
		
		//if(nbt.hasKey("CustomName")){
		//	this.localizedName =nbt.getString("CustomName");
		//}
		
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);
		
		nbt.setShort("ReceiveTime", (short)this.receiveTime);
		nbt.setDouble("EnergyAmount", this.energyAmount);
		nbt.setShort("Type", (short)this.type);
		
		NBTTagList list = new NBTTagList();
		for (int i = 0; i < this.slots.length; i++ ){
			if(this.slots[i] != null){
				NBTTagCompound compound = new NBTTagCompound();
				compound.setByte("Slot", (byte) i);
				this.slots[i].writeToNBT(compound);
				list.appendTag(compound);
			}
		}
		nbt.setTag("Item", list);
		
		//if (this.hasCustomInventoryName()){nbt.setString("CustomName",this.localizedName);}
		
	}
	

	@Override
	public void onChunkUnload()
    {
		if(!FMLCommonHandler.instance().getEffectiveSide().isClient()){
			MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
			innet = false;
			//System.out.println("Unloaded!");
		}
		super.onChunkUnload();
    }
	

	@Override
	public void invalidate(){
		if(!FMLCommonHandler.instance().getEffectiveSide().isClient()){
			MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
			innet = false;
			//System.out.println("Unloaded!");
		}
		super.invalidate();
	}
	
	@Override
	public boolean acceptsEnergyFrom(TileEntity emitter, ForgeDirection direction) {
		return direction != ForgeDirection.DOWN && direction != ForgeDirection.UP;
	}

	@Override
	public double getDemandedEnergy() {
		return 128;
	}

	@Override
	public int getSinkTier() {
		return Integer.MAX_VALUE;
	}

	@Override
	public double injectEnergy(ForgeDirection directionFrom, double amount, double voltage) {
		double leftover = 0;
		if (energyAmount + amount > eUConsumption){
			leftover = energyAmount + amount -eUConsumption;
			energyAmount = eUConsumption;
		} else{
			energyAmount = energyAmount+ amount;
		}
		//System.out.println((amount-leftover)+ " added, remains "+leftover+", now block stores "+ energyAmount);
		return leftover;
	}
	
	public int getEUConsumption(){
		return this.eUConsumption;
	}

}
