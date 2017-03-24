package ru.leoltron.lambda;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;

public class SlapCommand implements ICommand {

	private List aliases;
	 
	public SlapCommand() {
		this.aliases = new ArrayList();
	    this.aliases.add("slap");
	}

	@Override
	public int compareTo(Object arg0) {
		return 0;
	}

	@Override
	public String getCommandName() {
		return "slap";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/slap <nickname>";
	}

	@Override
	public List getCommandAliases() {
		return this.aliases;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		if (args.length < 1){
			sender.addChatMessage(new ChatComponentText("\u00A7c"+"Usage: /"+this.getCommandUsage(sender)));
			return;
		}else{
			//EntityPlayer senderPlayer = sender instanceof EntityPlayer ? (EntityPlayer) sender : null;
			List players = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
			Iterator<EntityPlayer> iterator= players.iterator();
			
			while (iterator.hasNext()) {
				EntityPlayer player = iterator.next();
				if(player.getDisplayName().equals(args[0])){
//					if (senderPlayer != null){
//						player.attackEntityFrom(DamageSource.outOfWorld, 2);
//					} else{
					player.motionY += 1;
					player.worldObj.playSoundAtEntity(player, "seasonevent:whip_crack", 1.0F, player.worldObj.rand.nextFloat() * 0.1F + 0.9F);
					player.attackEntityFrom(DamageSource.outOfWorld, 2);
//					}
					sender.addChatMessage(new ChatComponentText(args[0]+" slapped!"));
					return;
				}
			};
			sender.addChatMessage(new ChatComponentText("\u00A7c"+"Have not found player "+args[0]));
		}
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender) {
		if (sender instanceof EntityPlayer){
			EntityPlayer player = (EntityPlayer) sender;
			return MinecraftServer.getServer().getConfigurationManager().func_152596_g(player.getGameProfile());
		}else{
			return true;
		}
	}

	@Override
	public List addTabCompletionOptions(ICommandSender p_71516_1_, String[] p_71516_2_)
    {
        return p_71516_2_.length >= 1 ? CommandBase.getListOfStringsMatchingLastWord(p_71516_2_, MinecraftServer.getServer().getAllUsernames()) : null;
    }

	@Override
	public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
		// TODO Auto-generated method stub
		return p_82358_2_ == 0;
	}

}
