package ru.leoltron.lambda;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.item.IC2Items;
import ic2.api.recipe.RecipeInputItemStack;
import ic2.api.recipe.Recipes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCompressed;
import net.minecraft.block.material.MapColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import ru.leoltron.lambda.blocks.*;
import ru.leoltron.lambda.eventhandler.*;
import ru.leoltron.lambda.items.*;
import ru.leoltron.lambda.tileentity.TileEntityHarvester;

@Mod (modid = "season_event", name = "LambdaMod", version = "1.08", dependencies = "required-after:IC2;required-after:GalacticraftMars;required-after:GalacticraftCore")
public class Lambda {
	
	//private NBTTagCompound waterAmount = new NBTTagCompound();
	
	private int value = 16; //Кол-во монет в одной малой реликвии
	
	
	@Instance("season_event")
	public static Lambda instance;
	
	private static Logger logger;
	
	//Функциональные блоки
	
	public static Block RadiationAirBlock;
	
	public static Block HarvesterPrimitiveBlockIdle;
	public static Block HarvesterPrimitiveBlockActive;
	public static Block HarvesterStableBlockIdle;
	public static Block HarvesterStableBlockActive;
	public static Block HarvesterReinforcedBlockIdle;
	public static Block HarvesterReinforcedBlockActive;
	public static Block HarvesterPrototypeBlockIdle;
	public static Block HarvesterPrototypeBlockActive;
	
	public static Block jumpBlock;
	
	//SetPointBlock
	public static Block IronHarvesterSetPointBlock;
	public static Block CopperHarvesterSetPointBlock;
	public static Block TinHarvesterSetPointBlock;
	public static Block DiamondHarvesterSetPointBlock;
	public static Block UraniumHarvesterSetPointBlock;
	public static Block IridiumHarvesterSetPointBlock;
	
	public static Block ThuliumHarvesterSetPointBlock;
	public static Block EleriumHarvesterSetPointBlock;
	public static Block PromethionHarvesterSetPointBlock;
	
	//Слитки
	public static Item ThuliumIngot;
	public static Item EleriumIngot;
	public static Item PromethionIngot;
	
	public static Item SpaceAlloyIngot;
	public static Item SpaceAlloy;
	
	//Материальные блоки
	public static Block ThuliumBlock;
	public static Block EleriumBlock;
	public static Block PromethionBlock;
	public static Block SpaceAlloyBlock;
	
	//Декоративные блоки	
	public static Block RCLogoBlock;
	public static Block LampBlock;
	public static Block AlienBlockDark;
	public static Block AlienBlockLight;
	
	//Руды (измельченные)
	public static Item CrushedThuliumOre;
	public static Item CrushedEleriumOre;
	public static Item CrushedPromethionOre;
	
	public static Item carbonWhip;
	
	public static Item RPPlate;
	public static Item RPCoin;
	public static Item ResearchRelic;
	
	
	public static CreativeTabs seasoneventCreativeTab = new CreativeTabs("seasoneventCreativeTab"){
		@Override
		@SideOnly(Side.CLIENT)
		public Item getTabIconItem(){
			return Item.getItemFromBlock(HarvesterReinforcedBlockIdle);
		}
	};
	
	private Item getIC2Item_(String name){
		return getIC2Item(name).getItem();
	}
	
	public static ItemStack getIC2Item(String name){
		return getIC2Item(name,1);
	}
	
	private static ItemStack getIC2Item(String name,int amount){
		ItemStack stack = IC2Items.getItem(name).copy();
		stack.stackSize = amount;
		return stack;
	}
	
	private ItemStack getIC2Item(String name,int amount,int damage){
		ItemStack stack = IC2Items.getItem(name).copy();
		stack.stackSize = amount;
		stack.setItemDamage(damage);
		return stack;
	}
	
	private void register(Block block){
		GameRegistry.registerBlock(block, "seasonevent_"+block.getUnlocalizedName().substring(5));
	}
	
	private void register(Item item){
		GameRegistry.registerItem(item, "seasonevent_"+item.getUnlocalizedName().substring(5));
	}
	
    // Параметры сборщиков
	public static int[] outputChance = new int[4];
	public static int[] rareChance = new int[4];
	public static int[] eUConsumption = new int[4];

	
	public static final int guiIDHarvester = 0;
	
	private void loadLocalConfig(){
		
		outputChance[0] = 30;
		outputChance[1] = 40;
		outputChance[2] = 50;
		outputChance[3] = 70;
		
		rareChance[0] = 1;
		rareChance[1] = 2;
		rareChance[2] = 3;
		rareChance[3] = 6;
		
		eUConsumption[0] = 10000;
		eUConsumption[1] = 30000;
		eUConsumption[2] = 70000;
		eUConsumption[3] = 100000;
		
	}	
	
	@EventHandler
	public void PreLoad(FMLPreInitializationEvent event){
		logger = event.getModLog();
		//FMLCommonHandler.instance().bus().register(new ClientTickHandler());
		MinecraftForge.EVENT_BUS.register(new EntityPickupHandler());		
		MinecraftForge.EVENT_BUS.register(new RadiationExplosionEventHandler());			
		MinecraftForge.EVENT_BUS.register(new PlayerInteractEventHandler());
		MinecraftForge.EVENT_BUS.register(new LaserEventHandler());		
		MinecraftForge.EVENT_BUS.register(new GuiOpenEventHandler());
	}
	
	
	@EventHandler
	public void Load(FMLInitializationEvent event){
		logger.info("Starting Init...");
		loadLocalConfig();

		GameRegistry.registerTileEntity(TileEntityHarvester.class, "Harvester");
		NetworkRegistry.INSTANCE.registerGuiHandler(this,new GuiHandler());
		
		RadiationAirBlock = new RadiationAir().setBlockName("radiationAirBlock").setCreativeTab(seasoneventCreativeTab);
		
		HarvesterPrimitiveBlockIdle = new Harvester(false,0).setStepSound(Block.soundTypeWood).setBlockName("harvesterPrimitiveIdle").setHardness(3.5F);
		HarvesterPrimitiveBlockActive = new Harvester(true,0).setStepSound(Block.soundTypeWood).setBlockName("harvesterPrimitiveActive").setLightLevel(0.625F).setHardness(3.5F);
		HarvesterStableBlockIdle = new Harvester(false,1).setStepSound(Block.soundTypeMetal).setBlockName("harvesterStableIdle").setHardness(3.5F);
		HarvesterStableBlockActive = new Harvester(true,1).setStepSound(Block.soundTypeMetal).setBlockName("harvesterStableActive").setLightLevel(0.625F).setHardness(3.5F);
		HarvesterReinforcedBlockIdle = new Harvester(false,2).setStepSound(Block.soundTypeMetal).setBlockName("harvesterReinforcedIdle").setHardness(3.5F);
		HarvesterReinforcedBlockActive = new Harvester(true,2).setStepSound(Block.soundTypeMetal).setBlockName("harvesterReinforcedActive").setLightLevel(0.625F).setHardness(3.5F);
		HarvesterPrototypeBlockIdle = new Harvester(false,3).setStepSound(Block.soundTypeMetal).setBlockName("harvesterPrototypeIdle").setHardness(3.5F);
		HarvesterPrototypeBlockActive = new Harvester(true,3).setStepSound(Block.soundTypeMetal).setBlockName("harvesterPrototypeActive").setLightLevel(0.625F).setHardness(3.5F);
		
		jumpBlock = new JumpBlock().setBlockName("jumpBlock");
		
		IronHarvesterSetPointBlock= new HarvesterSetPoint(0).setStepSound(Block.soundTypeMetal).setBlockName("ironHarvesterSetPointBlock");
		CopperHarvesterSetPointBlock= new HarvesterSetPoint(1).setStepSound(Block.soundTypeMetal).setBlockName("copperHarvesterSetPointBlock");
		TinHarvesterSetPointBlock= new HarvesterSetPoint(2).setStepSound(Block.soundTypeMetal).setBlockName("tinHarvesterSetPointBlock");
		DiamondHarvesterSetPointBlock= new HarvesterSetPoint(3).setStepSound(Block.soundTypeMetal).setBlockName("diamondHarvesterSetPointBlock");
		UraniumHarvesterSetPointBlock= new HarvesterSetPoint(4).setStepSound(Block.soundTypeMetal).setBlockName("uraniumHarvesterSetPointBlock");
		IridiumHarvesterSetPointBlock= new HarvesterSetPoint(5).setStepSound(Block.soundTypeMetal).setBlockName("iridiumHarvesterSetPointBlock");
		
		ThuliumHarvesterSetPointBlock= new HarvesterSetPoint(6).setStepSound(Block.soundTypeMetal).setBlockName("thuliumHarvesterSetPointBlock");
		EleriumHarvesterSetPointBlock= new HarvesterSetPoint(7).setStepSound(Block.soundTypeMetal).setBlockName("eleriumHarvesterSetPointBlock");
		PromethionHarvesterSetPointBlock= new HarvesterSetPoint(8).setStepSound(Block.soundTypeMetal).setBlockName("promethionHarvesterSetPointBlock");

		register(RadiationAirBlock);
		
		register(HarvesterPrimitiveBlockIdle);
		register(HarvesterPrimitiveBlockActive);
		register(HarvesterStableBlockIdle);
		register(HarvesterStableBlockActive);
		register(HarvesterReinforcedBlockIdle);
		register(HarvesterReinforcedBlockActive);
		register(HarvesterPrototypeBlockIdle);
		register(HarvesterPrototypeBlockActive);
		
		//GameRegistry.registerBlock(jumpBlock, ItemJumpBlock.class, "jumpBlock");
				
		register(IronHarvesterSetPointBlock);
		register(CopperHarvesterSetPointBlock);
		register(TinHarvesterSetPointBlock);
		register(ThuliumHarvesterSetPointBlock);
		register(DiamondHarvesterSetPointBlock);
		register(UraniumHarvesterSetPointBlock);
		register(EleriumHarvesterSetPointBlock);
		register(IridiumHarvesterSetPointBlock);
		register(PromethionHarvesterSetPointBlock);
		
		//Слитки
		ThuliumIngot = new Item().setCreativeTab(seasoneventCreativeTab).setUnlocalizedName("thuliumIngot").setTextureName("seasonevent:thulium_ingot");
		EleriumIngot = new Item().setCreativeTab(seasoneventCreativeTab).setUnlocalizedName("eleriumIngot").setTextureName("seasonevent:elerium_ingot");
		PromethionIngot = new Item().setCreativeTab(seasoneventCreativeTab).setUnlocalizedName("promethionIngot").setTextureName("seasonevent:promethion_ingot");
		SpaceAlloyIngot = new Item().setCreativeTab(seasoneventCreativeTab).setUnlocalizedName("spaceAlloyIngot").setTextureName("seasonevent:space_alloy_ingot");
		SpaceAlloy = new Item().setCreativeTab(seasoneventCreativeTab).setUnlocalizedName("spaceAlloy").setTextureName("seasonevent:space_alloy");
		
		
		ThuliumBlock = new BlockCompressed(MapColor.ironColor).setCreativeTab(seasoneventCreativeTab).setHardness(5.0F).setResistance(2000.0F).setStepSound(Block.soundTypeMetal).setBlockName("blockThulium").setBlockTextureName("seasonevent:thulium_block");
		EleriumBlock = new BlockCompressed(MapColor.blueColor).setCreativeTab(seasoneventCreativeTab).setHardness(5.0F).setResistance(2000.0F).setStepSound(Block.soundTypeMetal).setBlockName("blockElerium").setBlockTextureName("seasonevent:elerium_block");
		PromethionBlock = new BlockCompressed(MapColor.redColor).setCreativeTab(seasoneventCreativeTab).setHardness(5.0F).setResistance(2000.0F).setStepSound(Block.soundTypeMetal).setBlockName("blockPromethion").setBlockTextureName("seasonevent:promethion_block");
		SpaceAlloyBlock = new BlockCompressed(MapColor.magentaColor).setCreativeTab(seasoneventCreativeTab).setHardness(5.0F).setResistance(2000.0F).setStepSound(Block.soundTypeMetal).setBlockName("blockSpaceAlloy").setBlockTextureName("seasonevent:space_alloy_block");
		
		RCLogoBlock = new BlockCompressed(MapColor.clayColor).setCreativeTab(seasoneventCreativeTab).setHardness(5.0F).setResistance(5.0F).setStepSound(Block.soundTypeMetal).setBlockName("blockRCLogo").setBlockTextureName("seasonevent:RC_logo");
		LampBlock = new Lamp().setBlockName("Lamp");
		AlienBlockDark = new BlockCompressed(MapColor.blackColor).setCreativeTab(seasoneventCreativeTab).setHardness(5.0F).setResistance(5.0F).setStepSound(Block.soundTypeMetal).setBlockName("blockAlienDark").setBlockTextureName("seasonevent:blockAlienDark");
		AlienBlockLight= new BlockCompressed(MapColor.limeColor).setCreativeTab(seasoneventCreativeTab).setHardness(5.0F).setResistance(5.0F).setStepSound(Block.soundTypeMetal).setBlockName("blockAlienLight").setBlockTextureName("seasonevent:blockAlienLight").setLightLevel(0.625F);
		
		register(ThuliumBlock);
		register(EleriumBlock);
		register(PromethionBlock);
		register(SpaceAlloyBlock);
		
		register(RCLogoBlock);
		GameRegistry.registerBlock(LampBlock, ItemLamp.class, "seasonevent_"+LampBlock.getUnlocalizedName().substring(5));
		register(AlienBlockDark);
		register(AlienBlockLight);
		
		//Руды (измельченные)
		CrushedThuliumOre = new Item().setCreativeTab(seasoneventCreativeTab).setUnlocalizedName("crushedThuliumOre").setTextureName("seasonevent:CrushedThuliumOre");
		CrushedEleriumOre = new Item().setCreativeTab(seasoneventCreativeTab).setUnlocalizedName("crushedEleriumOre").setTextureName("seasonevent:CrushedEleriumOre");
		CrushedPromethionOre = new Item().setCreativeTab(seasoneventCreativeTab).setUnlocalizedName("crushedPromethionOre").setTextureName("seasonevent:CrushedPromethionOre");
		
		carbonWhip = new CarbonWhip().setUnlocalizedName("carbonWhip").setMaxStackSize(1);
		
		RPPlate = new Item().setUnlocalizedName("RPPlate").setTextureName("seasonevent:RP_plate");
		RPCoin = new Item().setUnlocalizedName("RPCoin").setTextureName("seasonevent:PP_coin").setCreativeTab(seasoneventCreativeTab);
		ResearchRelic = new ResearchRelic(value).setUnlocalizedName("researchRelic"); 
		
		register(carbonWhip);
		
		register(ThuliumIngot);
		register(EleriumIngot);
		register(PromethionIngot);
		register(CrushedThuliumOre);
		register(CrushedEleriumOre);
		register(CrushedPromethionOre);
		register(SpaceAlloyIngot);
		register(SpaceAlloy);
		
		register(RPPlate);
		register(RPCoin);
		register(ResearchRelic);
		
		ItemStack white = new ItemStack(Items.dye,1,15);
		ItemStack blue = new ItemStack(Items.dye,1,4);
		ItemStack red = new ItemStack(Items.dye,1,1);
		ItemStack purple = new ItemStack(Items.dye,1,5);
		
		GameRegistry.addShapelessRecipe(new ItemStack(ResearchRelic,value,1), new Object[]{new ItemStack(ResearchRelic,1,2)});
		GameRegistry.addShapelessRecipe(new ItemStack(ResearchRelic,value), new Object[]{new ItemStack(ResearchRelic,1,1)});
		GameRegistry.addShapelessRecipe(new ItemStack(RPCoin,value), new Object[]{new ItemStack(ResearchRelic,1,0)});
		
		GameRegistry.addShapelessRecipe(new ItemStack(RPCoin) ,new Object[]{RPPlate});
//		GameRegistry.addShapelessRecipe(new ItemStack(RPPlate) ,new Object[]{RPCoin});
//		
//		GameRegistry.addShapelessRecipe(new ItemStack(RPCoin,9) ,new Object[]{RPPlate,RPPlate,RPPlate,RPPlate,RPPlate,RPPlate,RPPlate,RPPlate,RPPlate});
//		GameRegistry.addShapelessRecipe(new ItemStack(RPPlate,9) ,new Object[]{RPCoin,RPCoin,RPCoin,RPCoin,RPCoin,RPCoin,RPCoin,RPCoin,RPCoin,});
		
		ItemStack whip = new ItemStack(carbonWhip);
		whip.addEnchantment(Enchantment.knockback, 2);
		
		Recipes.advRecipes.addRecipe(whip, new Object[]{" C ","  C","GC ",'C',getIC2Item("carbonPlate"),'G',getIC2Item("insulatedGoldCableItem")});
		Recipes.advRecipes.addRecipe(new ItemStack(SpaceAlloyIngot), new Object[]{"T","E","P",'T',ThuliumIngot,'E',EleriumIngot,'P',PromethionIngot});
		
		//Recipes.advRecipes.addRecipe(new ItemStack(SpaceAlloyIngot), new Object[]{"T","E","P",'T',meteorite,'E',dash,'P',titan});
		
//		GameRegistry.addSmelting(CrushedThuliumOre, new ItemStack(ThuliumIngot), 0);
//		GameRegistry.addSmelting(CrushedEleriumOre, new ItemStack(EleriumIngot), 0);
//		GameRegistry.addSmelting(CrushedPromethionOre, new ItemStack(PromethionIngot), 0);
		
		
		GameRegistry.addRecipe(new ItemStack(ThuliumBlock), new Object[]{"XXX","XXX","XXX",'X',ThuliumIngot});
		GameRegistry.addShapelessRecipe(new ItemStack(ThuliumIngot,9) ,new Object[]{ThuliumBlock});
		
		GameRegistry.addRecipe(new ItemStack(EleriumBlock), new Object[]{"XXX","XXX","XXX",'X',EleriumIngot});
		GameRegistry.addShapelessRecipe(new ItemStack(EleriumIngot,9) ,new Object[]{EleriumBlock});
		
		GameRegistry.addRecipe(new ItemStack(PromethionBlock), new Object[]{"XXX","XXX","XXX",'X',PromethionIngot});
		GameRegistry.addShapelessRecipe(new ItemStack(PromethionIngot,9) ,new Object[]{PromethionBlock});
		
		GameRegistry.addRecipe(new ItemStack(SpaceAlloyBlock), new Object[]{"XXX","XXX","XXX",'X',SpaceAlloy});
		GameRegistry.addShapelessRecipe(new ItemStack(SpaceAlloy,9) ,new Object[]{SpaceAlloyBlock});
		
		GameRegistry.addRecipe(new ItemStack(LampBlock,1,0), new Object[]{"IRI","RTR","IRI",'I',Items.iron_ingot,'R',Items.redstone,'T',white});
		GameRegistry.addShapelessRecipe(new ItemStack(LampBlock,1,1), new Object[]{ new ItemStack(LampBlock,1,0)});
		GameRegistry.addShapelessRecipe(new ItemStack(LampBlock,1,0), new Object[]{ new ItemStack(LampBlock,1,1)});
		GameRegistry.addRecipe(new ItemStack(LampBlock,1,1), new Object[]{"IRI","RTR","IRI",'I',Items.iron_ingot,'R',Blocks.redstone_torch,'T',white});
		
//		GameRegistry.addSmelting(new ItemStack(LampBlock, 1, 0), new ItemStack(ThuliumIngot, 1), 0);
//		GameRegistry.addSmelting(new ItemStack(LampBlock, 1, 1), new ItemStack(ThuliumIngot, 1), 0);
		
		GameRegistry.addRecipe(new ItemStack(LampBlock,1,2), new Object[]{"IRI","RTR","IRI",'I',Items.iron_ingot,'R',Items.redstone,'T',blue});
		GameRegistry.addShapelessRecipe(new ItemStack(LampBlock,1,2), new Object[]{ new ItemStack(LampBlock,1,3)});
		GameRegistry.addShapelessRecipe(new ItemStack(LampBlock,1,3), new Object[]{ new ItemStack(LampBlock,1,2)});
		GameRegistry.addRecipe(new ItemStack(LampBlock,1,3), new Object[]{"IRI","RTR","IRI",'I',Items.iron_ingot,'R',Blocks.redstone_torch,'T',blue});
		
//		GameRegistry.addSmelting(new ItemStack(LampBlock, 1, 2), new ItemStack(EleriumIngot, 1), 0);
//		GameRegistry.addSmelting(new ItemStack(LampBlock, 1, 3), new ItemStack(EleriumIngot, 1), 0);
		
		GameRegistry.addRecipe(new ItemStack(LampBlock,1,4), new Object[]{"IRI","RTR","IRI",'I',Items.iron_ingot,'R',Items.redstone,'T',red});
		GameRegistry.addShapelessRecipe(new ItemStack(LampBlock,1,4), new Object[]{ new ItemStack(LampBlock,1,5)});
		GameRegistry.addShapelessRecipe(new ItemStack(LampBlock,1,5), new Object[]{ new ItemStack(LampBlock,1,4)});
		GameRegistry.addRecipe(new ItemStack(LampBlock,1,5), new Object[]{"IRI","RTR","IRI",'I',Items.iron_ingot,'R',Blocks.redstone_torch,'T',red});
		
//		GameRegistry.addSmelting(new ItemStack(LampBlock, 1, 4), new ItemStack(PromethionIngot, 1), 0);
//		GameRegistry.addSmelting(new ItemStack(LampBlock, 1, 5), new ItemStack(PromethionIngot, 1), 0);
		
		GameRegistry.addRecipe(new ItemStack(LampBlock,1,6), new Object[]{"IGI","RTR","IGI",'I',Items.iron_ingot,'R',Items.glowstone_dust,'T',purple,'G',Blocks.glass});
		GameRegistry.addShapelessRecipe(new ItemStack(LampBlock,1,6), new Object[]{ new ItemStack(LampBlock,1,7)});
		GameRegistry.addShapelessRecipe(new ItemStack(LampBlock,1,7), new Object[]{ new ItemStack(LampBlock,1,6)});
		GameRegistry.addRecipe(new ItemStack(LampBlock,1,7), new Object[]{"IRI","GTG","IRI",'I',Items.iron_ingot,'R',Items.glowstone_dust,'T',purple,'G',Blocks.glass});
		
//		GameRegistry.addSmelting(new ItemStack(LampBlock, 1, 6), new ItemStack(SpaceAlloy, 1), 0);
//		GameRegistry.addSmelting(new ItemStack(LampBlock, 1, 7), new ItemStack(SpaceAlloy, 1), 0);
		
		GameRegistry.addRecipe(new ItemStack(RCLogoBlock), new Object[]{"GTG","TFT","GTG",'G',new ItemStack(Blocks.wool,1,8),'T',white,'F',Items.feather});
//		GameRegistry.addSmelting(RCLogoBlock, new ItemStack(ThuliumIngot,4), 0);
		
//		waterAmount.setInteger("amount", 1000);
//		Recipes.oreWashing.addRecipe(new RecipeInputItemStack(new ItemStack(CrushedThuliumOre)), waterAmount, getIC2Item("purifiedCrushedIronOre",5),getIC2Item("smallIronDust",2),getIC2Item("stoneDust"));
//		Recipes.oreWashing.addRecipe(new RecipeInputItemStack(new ItemStack(CrushedEleriumOre)), waterAmount, getIC2Item("purifiedCrushedUraniumOre",5),getIC2Item("smallLeadDust",2),getIC2Item("stoneDust"));
//		Recipes.oreWashing.addRecipe(new RecipeInputItemStack(new ItemStack(CrushedPromethionOre)), waterAmount, getIC2Item("iridiumOre",5),getIC2Item("smallSilverDust",2),getIC2Item("stoneDust"));
		
		
		NBTTagCompound lowHeat = new NBTTagCompound();  lowHeat.setInteger("minHeat", 1000);
		NBTTagCompound mediumHeat = new NBTTagCompound(); mediumHeat.setInteger("minHeat", 2500);
		NBTTagCompound highHeat = new NBTTagCompound(); highHeat.setInteger("minHeat", 5000);
		
		Recipes.centrifuge.addRecipe(new RecipeInputItemStack(new ItemStack(CrushedThuliumOre)), lowHeat, getIC2Item("smallGoldDust"),new ItemStack(ThuliumIngot), getIC2Item("stoneDust"));
		Recipes.centrifuge.addRecipe(new RecipeInputItemStack(new ItemStack(CrushedEleriumOre)), mediumHeat, getIC2Item("smallUran235"),new ItemStack(EleriumIngot), getIC2Item("stoneDust"));
		Recipes.centrifuge.addRecipe(new RecipeInputItemStack(new ItemStack(CrushedPromethionOre)), highHeat,getIC2Item("smallSilverDust"), new ItemStack(PromethionIngot), getIC2Item("stoneDust"));
		
		Recipes.compressor.addRecipe(new RecipeInputItemStack(new ItemStack(SpaceAlloyIngot)), null, new ItemStack(SpaceAlloy));
		
		
		Recipes.advRecipes.addRecipe(new ItemStack(HarvesterPrimitiveBlockIdle), new Object[]{"BGB","CRC","BPB",'B',Blocks.planks,'G',Blocks.glass,'C',getIC2Item("electronicCircuit"),'R',Items.redstone,'P',getIC2Item("bronzePickaxe")});
		Recipes.advRecipes.addRecipe(new ItemStack(HarvesterPrimitiveBlockIdle), new Object[]{"BGB","CRC","BPB",'B',Blocks.planks,'G',Blocks.glass,'C',getIC2Item("electronicCircuit"),'R',Items.redstone,'P',Items.iron_pickaxe});
		
		Recipes.advRecipes.addRecipe(new ItemStack(HarvesterStableBlockIdle), new Object[]{"PGP","PIP","PDP",'P',getIC2Item("plateiron"),'G',Blocks.glass,'C',getIC2Item("electronicCircuit"),'I',getIC2Item("miner"),'D',getIC2Item("coil",1,11)});
		Recipes.advRecipes.addRecipe(new ItemStack(HarvesterStableBlockIdle), new Object[]{"PIP","PRP","PDP",'I',getIC2Item("miningPipe"),'P',getIC2Item("plateiron"),'R',HarvesterPrimitiveBlockIdle,'D',getIC2Item("coil",1,11)});
		
		Recipes.advRecipes.addRecipe(new ItemStack(HarvesterReinforcedBlockIdle),new Object[]{"PGP","CAC","TDT",'P',getIC2Item("plateadviron"),'G',Blocks.glass,'C',getIC2Item("advancedCircuit"),'A',getIC2Item("advancedMachine"),'D',getIC2Item("coil",1,12),'T',ThuliumIngot});
		//Recipes.advRecipes.addRecipe(new ItemStack(HarvesterReinforcedBlockIdle),new Object[]{"ACA","PHP","DDD",'A',getIC2Item("advancedCircuit"),'C',getIC2Item("advancedMachine"),'P',ThuliumIngot,'H',HarvesterStableBlockIdle,'D',Items.diamond});
		
		Recipes.advRecipes.addRecipe(new ItemStack(HarvesterPrototypeBlockIdle), new Object[]{"GGG","AMA","EDE",'G',getIC2Item("reinforcedGlass"),'M',new ItemStack(getIC2Item("teleporter").getItem(),1,11),'A',getIC2Item("advancedCircuit"),'D',getIC2Item("coil",1,12),'E',EleriumIngot});
		
		GameRegistry.addRecipe(new ItemStack(AlienBlockLight), new Object[]{"OGO","AEA","OGO",'O',Blocks.obsidian,'G',Items.glowstone_dust,'A',SpaceAlloy,'E',Items.ender_eye});
		
		GameRegistry.addRecipe(new ItemStack(Blocks.dragon_egg), new Object[]{" O ","AEA","OOO",'O',Blocks.obsidian,'A',AlienBlockLight,'E',Items.ender_pearl});
		
		//Loader.instance().getModList()
		
		List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
		
		Iterator<IRecipe> Leash = recipes.iterator();
				          
			while (Leash.hasNext()) {
				ItemStack is = Leash.next().getRecipeOutput();
				if (is != null && is.getItem() == getIC2Item("iridiumPlate").getItem() && is.getItemDamage() == getIC2Item("iridiumPlate").getItemDamage())
					Leash.remove();
				
				//if (is != null && is.getItem() == getIC2Item("mixedMetalIngot").getItem() && is.getItemDamage() == getIC2Item("mixedMetalIngot").getItemDamage())
				//	Leash.remove();
			}
		Recipes.advRecipes.addRecipe(getIC2Item("iridiumPlate"), new Object[]{"IAI","ASA","IAI",'I',getIC2Item("iridiumOre"),'A',getIC2Item("advancedAlloy"),'S',SpaceAlloy});		
		//Recipes.advRecipes.addRecipe(getIC2Item("mixedMetalIngot"), new Object[]{"AAA","BBB","TTT",'T',ThuliumIngot,'A',getIC2Item("plateadviron"),'B',getIC2Item("platebronze")});
		//MinecraftForge.EVENT_BUS.register(this);
		GameRegistry.addSmelting(getIC2Item("denseplatecopper"), getIC2Item("platecopper",9), 0.0F);
		GameRegistry.addSmelting(getIC2Item("denseplatetin"), getIC2Item("platetin",9), 0.0F);
		GameRegistry.addSmelting(getIC2Item("denseplateiron"), getIC2Item("plateiron",9), 0.0F);
		GameRegistry.addSmelting(getIC2Item("denseplategold"), getIC2Item("plategold",9), 0.0F);
		GameRegistry.addSmelting(getIC2Item("denseplatebronze"), getIC2Item("platebronze",9), 0.0F);
		GameRegistry.addSmelting(getIC2Item("denseplatelead"), getIC2Item("platelead",9), 0.0F);
		GameRegistry.addSmelting(getIC2Item("denseplateobsidian"), getIC2Item("plateobsidian",9), 0.0F);
		GameRegistry.addSmelting(getIC2Item("denseplateadviron"), getIC2Item("plateadviron",9), 0.0F);
		
		Recipes.macerator.addRecipe(new RecipeInputItemStack(getIC2Item("denseplatelapi")), null, getIC2Item("lapiDust",9));
		
		logger.info("Initialization finished");
	}
	
	@EventHandler
	public void postLoad(FMLPostInitializationEvent event){
		
		ItemStack meteorite = null;
		ItemStack dash = GameRegistry.findItemStack("GalacticraftMars", "ingotDesh", 1);
		ItemStack titan = GameRegistry.findItemStack("GalacticraftMars", "ingotTitanium", 1);
		
        Map<ItemStack,ItemStack> recipes = FurnaceRecipes.smelting().getSmeltingList();
				          
		for(Entry<ItemStack,ItemStack> entry : recipes.entrySet()) {
			ItemStack is = entry.getValue();
			if (is != null){
					if(is.getUnlocalizedName().equals("item.meteoricIronIngot")){
						meteorite = is.copy();
						meteorite.stackSize = 1;
					}else if(is.getUnlocalizedName().equals("item.ingotDesh")){
						dash = is.copy();
						dash.stackSize = 1;
					}else if(is.getUnlocalizedName().equals("item.ingotTitanium")){
						titan = is.copy();
						titan.stackSize = 1;
					}
			}
		}

		if(meteorite == null)
			Lambda.logger.warn("meteorite = null");

		if(dash == null)
			Lambda.logger.warn("dash = null");

		if(titan == null)
			Lambda.logger.warn("titan = null");
		
		if(titan != null && dash != null && meteorite != null)
			Recipes.advRecipes.addRecipe(new ItemStack(SpaceAlloyIngot), new Object[]{"T","E","P",'T',meteorite,'E',dash,'P',titan});
	}
	
	
	@EventHandler
	  public void serverLoad(FMLServerStartingEvent event)
	  {
	    event.registerServerCommand(new SlapCommand());
	  }
	
}
