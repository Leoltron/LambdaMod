package ru.leoltron.lambda.gui;

import ic2.api.item.IC2Items;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import ru.leoltron.lambda.Lambda;
import ru.leoltron.lambda.blocks.HarvesterSetPoint;
import ru.leoltron.lambda.container.ContainerHarvester;
import ru.leoltron.lambda.tileentity.TileEntityHarvester;

import org.lwjgl.opengl.GL11;

public class GuiHarvester extends GuiContainer {
	
	public static final ResourceLocation bground = new ResourceLocation("seasonevent","textures/gui/harvester_gui.png");
	
	public TileEntityHarvester harvester;
	
	private byte time = 0;

	public GuiHarvester(InventoryPlayer inentoryPlayer,TileEntityHarvester entity) {
		super(new ContainerHarvester(inentoryPlayer,entity));
		
		this.harvester = entity;
		
		this.xSize = 176;
		this.ySize = 166;
	}
	
	public void drawGuiContainerForegroundLayer(int par1,int par2){
		
		Block block = harvester.getWorldObj().getBlock(harvester.xCoord, harvester.yCoord-1, harvester.zCoord);
		String type = "";
		String minInput=I18n.format("gui.harvester.minInput",new Object[0])+" "+(int)harvester.eUPerTickConsumption+" EU/t";
		
		switch(harvester.getType()){
		case 0: 
			type = "tile.harvesterPrimitiveIdle.name";
			break;
		case 1:
			type = "tile.harvesterStableIdle.name";
			break;
		case 2:
			type = "tile.harvesterReinforcedIdle.name";
			break;
		case 3:
			type = "tile.harvesterPrototypeIdle.name";
			break;
		}
		if (block instanceof HarvesterSetPoint){
			int setPointType =((HarvesterSetPoint) block).getType();
			if (setPointType < 5){ // ќбычные ресурсы
				
				String chanceOrdinary = I18n.format("gui.harvester.chance.ordinary", new Object[0])+": "+(Lambda.outputChance[this.harvester.getType()])+"%";
				String chanceTrash = I18n.format("gui.harvester.chance.trash", new Object[0])+": "+(100-(Lambda.outputChance[this.harvester.getType()])+Lambda.rareChance[this.harvester.getType()])+"%";
				String chanceRare = I18n.format("gui.harvester.chance.rare", new Object[0])+": "+Lambda.rareChance[this.harvester.getType()]+"%";
				
				fontRendererObj.drawString(I18n.format(type, new Object[0]), this.xSize/2-this.fontRendererObj.getStringWidth(I18n.format(type, new Object[0]))/2, 4, 4210752);
				fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), this.xSize-8-this.fontRendererObj.getStringWidth(I18n.format("container.inventory", new Object[0])), this.ySize-96+2, 4210752);
				fontRendererObj.drawString(I18n.format(StatCollector.translateToLocal("gui.harvester.chance.title"), new Object[0]), 105, this.ySize-152, 4210752);
				
				fontRendererObj.drawString(chanceOrdinary, 105, this.ySize-142, 4210752);
				fontRendererObj.drawString(chanceTrash, 105, this.ySize-132, 4210752);
				fontRendererObj.drawString(chanceRare, 105, this.ySize-122, 4210752);
				
				this.fontRendererObj.drawString(minInput, 28, this.ySize-96, 4210752);
				
			} else if (setPointType == 5){ //»ридий
				String chanceOrdinary = I18n.format("gui.harvester.chance.ordinary", new Object[0])+": "+Lambda.rareChance[harvester.getType()]+"%";
				String chanceTrash = I18n.format("gui.harvester.chance.trash", new Object[0])+": "+(100-Lambda.rareChance[this.harvester.getType()])+"%";
				
				fontRendererObj.drawString(I18n.format(type, new Object[0]), this.xSize/2-this.fontRendererObj.getStringWidth(I18n.format(type, new Object[0]))/2, 4, 4210752);
				fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), this.xSize-8-this.fontRendererObj.getStringWidth(I18n.format("container.inventory", new Object[0])), this.ySize-96+2, 4210752);
				fontRendererObj.drawString(I18n.format(StatCollector.translateToLocal("gui.harvester.chance.title"), new Object[0]), 105, this.ySize-152, 4210752);
				
				fontRendererObj.drawString(chanceOrdinary, 105, this.ySize-142, 4210752);
				fontRendererObj.drawString(chanceTrash, 105, this.ySize-132, 4210752);
				
				this.fontRendererObj.drawString(minInput, 28, this.ySize-96, 4210752);

			} else{  //Ёвентовые ресурсы
				
				String chanceOrdinary = String.format(I18n.format("gui.harvester.chance.ordinary", new Object[0])+": %.2f",(float)(Lambda.rareChance[harvester.getType()])/3)+"%";
				String chanceTrash = String.format(I18n.format("gui.harvester.chance.trash", new Object[0])+": %.2f",(100-(float)(Lambda.rareChance[harvester.getType()])/3))+"%";
				
				fontRendererObj.drawString(I18n.format(type, new Object[0]), this.xSize/2-this.fontRendererObj.getStringWidth(I18n.format(type, new Object[0]))/2, 4, 4210752);
				fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), this.xSize-8-this.fontRendererObj.getStringWidth(I18n.format("container.inventory", new Object[0])), this.ySize-96+2, 4210752);
				fontRendererObj.drawString(I18n.format(StatCollector.translateToLocal("gui.harvester.chance.title"), new Object[0]), 105, this.ySize-152, 4210752);
				
				fontRendererObj.drawString(chanceOrdinary, 105, this.ySize-142, 4210752);
				fontRendererObj.drawString(chanceTrash, 105, this.ySize-132, 4210752);
				
				this.fontRendererObj.drawString(minInput, 28, this.ySize-96, 4210752);
			
			}
		}
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float par1,
			int par2, int par3) {
		//System.out.println(par1);
		if (time >=40){
			time = 0;
		} else{
			time++;
		}
		GL11.glColor4f(1F, 1F, 1F, 1F);		
	    Minecraft.getMinecraft().getTextureManager().bindTexture(bground);
	    drawTexturedModalRect(guiLeft,guiTop,0,0,xSize,ySize);
	    if (this.harvester.isWorking()){
	    	int k = harvester.getReceiveProgressRemainingScale(24);	
	    	int j = 23-k;
	    	drawTexturedModalRect(guiLeft+80,guiTop+45+k,176,15+k,15,j-1);

		    int l = MathHelper.floor_double(harvester.getEnergyAmountScaled(64));
	    	drawTexturedModalRect(guiLeft+11,guiTop+12+ 63 - l,176,37+ 63 - l,9,l);
	    }
	    
	    if(harvester.hasWorldObj()){
	    Block block = harvester.getWorldObj().getBlock(harvester.xCoord, harvester.yCoord-1, harvester.zCoord);
	    //ќпределение дропа
	    if (par2>104+guiLeft  && par2<guiLeft+xSize-4 && par3 < 51+guiTop && block instanceof HarvesterSetPoint){
	    	int setPointType = ((HarvesterSetPoint) block).getType();
	    	ItemStack item = null;
//	    	int ch = par3 > 130 ? 3 : (par3 > 120 ? 2:1);   
	    	int x = par2-guiLeft;
	    	int y = par3-guiTop;
	    	if(y >= 45 && setPointType < 5){
	    		boolean ch = time > 20;
	    		switch(setPointType){
	    		case 0:
	    			item = ch ? IC2Items.getItem("crushedIronOre") : IC2Items.getItem("purifiedCrushedIronOre");
	    			break;
	    		case 1:
	    			item = ch ? IC2Items.getItem("crushedCopperOre") : IC2Items.getItem("purifiedCrushedCopperOre");
	    			break;
	    		case 2:
	    			item = ch ? IC2Items.getItem("crushedTinOre") : IC2Items.getItem("purifiedCrushedTinOre");
	    			break;
	    		case 3:
	    			item = IC2Items.getItem("diamondDust").copy();
	    			item.setItemDamage(2);
	    			break;
	    		case 4:
	    			item = ch ? IC2Items.getItem("crushedUraniumOre") : IC2Items.getItem("purifiedCrushedUraniumOre");
	    			break;
	    		case 5:
	    			item = ch ? IC2Items.getItem("smallUran235") : IC2Items.getItem("Uran238");
	    			break;
	    		}
	    	}   else if (y > 33 && y < 41) {
	    		if(setPointType == 3){
	    			item = IC2Items.getItem("coalDust");					
	    		} else{
	    			item = IC2Items.getItem("stoneDust");	
	    		}	    		
	    	} else if(y > 23 && y < 31){
	    		switch(setPointType){
	    		case 0:
	    			item = IC2Items.getItem("ironDust");
	    			break;
	    		case 1:
	    			item = IC2Items.getItem("copperDust");
	    			break;
	    		case 2:
	    			item = IC2Items.getItem("tinDust");
	    			break;
	    		case 3:
	    			item = IC2Items.getItem("diamondDust");
	    			break;
	    		case 4:
	    			item = time > 20 ? IC2Items.getItem("smallUran235"):IC2Items.getItem("Uran238");
	    			break;
	    		case 5:
	    			item = IC2Items.getItem("iridiumOre");
	    			break;
	    		case 6:
	    			item = new ItemStack(Lambda.CrushedThuliumOre);
	    			break;
	    		case 7:
	    			item = new ItemStack(Lambda.CrushedEleriumOre);
	    			break;
	    		case 8:
	    			item = new ItemStack(Lambda.CrushedPromethionOre);
	    			break;
	    		}
	    	}
	    	if(item != null)
	    		this.itemRender.renderItemIntoGUI(fontRendererObj, mc.getTextureManager(),item, guiLeft+80, guiTop+19);	    	
	    	}
	    }
	    
		
	}

}
