package ru.leoltron.lambda.eventhandler;

import net.minecraft.util.MathHelper;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import ru.leoltron.lambda.blocks.JumpBlock;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class LivingUpdateEventHandler {
	
	@SubscribeEvent
	public void onLivingUpdate(LivingUpdateEvent event){
		if(event.entity.worldObj.getBlock(MathHelper.floor_double(event.entity.posX), 
		          MathHelper.floor_double(event.entity.posY - 0.20000000298023224D - (double)event.entity.yOffset), 
		          MathHelper.floor_double(event.entity.posZ)) instanceof JumpBlock){
			
			int meta = event.entity.worldObj.getBlockMetadata(MathHelper.floor_double(event.entity.posX), 
			          MathHelper.floor_double(event.entity.posY - 0.20000000298023224D - (double)event.entity.yOffset), 
			          MathHelper.floor_double(event.entity.posZ));
			if(meta < JumpBlock.jumpVelocity.length)
				event.entity.motionY = JumpBlock.jumpVelocity[meta];
		}
	}

}
