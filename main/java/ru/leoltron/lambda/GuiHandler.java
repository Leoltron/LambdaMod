package ru.leoltron.lambda;

 
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import ru.leoltron.lambda.container.ContainerHarvester;
import ru.leoltron.lambda.gui.GuiHarvester;
import ru.leoltron.lambda.tileentity.TileEntityHarvester;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler{

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		TileEntity entity = world.getTileEntity(x, y, z);
		
		if(entity != null){
			switch(ID){
			case Lambda.guiIDHarvester:
				if(entity instanceof TileEntityHarvester){
					return new ContainerHarvester(player.inventory,(TileEntityHarvester) entity);
				}
				return null;
			}
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		TileEntity entity = world.getTileEntity(x, y, z);
		
		if(entity != null){
			switch(ID){
			case Lambda.guiIDHarvester:
				if(entity instanceof TileEntityHarvester){
					return new GuiHarvester(player.inventory,(TileEntityHarvester) entity);
				}
				return null;
			}
		}
		return null;
	}

}
