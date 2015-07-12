package io.github.tehstoneman.zombiebrains;

import io.github.tehstoneman.zombiebrains.util.Settings;

import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class DebugCommand implements ICommand
{

	@Override
	public int compareTo( Object o )
	{
		return 0;
	}

	@Override
	public String getCommandName()
	{
		return "listpoints";
	}

	@Override
	public String getCommandUsage( ICommandSender sender )
	{
		return "listpoints";
	}

	@Override
	public List getCommandAliases()
	{
		return null;
	}

	@Override
	public void processCommand( ICommandSender sender, String[] argString )
	{
		World world = sender.getEntityWorld();
		if(!world.isRemote)
		{
			sender.addChatMessage( new ChatComponentText( "Listing waypoints" ) );
			ZombieBrains.waypointManager.listPoints( (EntityPlayer)sender );
		}
	}

	@Override
	public boolean canCommandSenderUseCommand( ICommandSender sender )
	{
		return Settings.debug;
	}

	@Override
	public List addTabCompletionOptions( ICommandSender sender, String[] argString )
	{
		return null;
	}

	@Override
	public boolean isUsernameIndex( String[] argString, int argInt )
	{
		return false;
	}
}
