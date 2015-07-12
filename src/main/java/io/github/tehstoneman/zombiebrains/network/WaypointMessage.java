package io.github.tehstoneman.zombiebrains.network;

import io.github.tehstoneman.zombiebrains.ModInfo;
import io.github.tehstoneman.zombiebrains.ZombieBrains;
import io.github.tehstoneman.zombiebrains.waypoints.Waypoint;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;

import org.apache.logging.log4j.LogManager;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;

public class WaypointMessage implements IMessage
{
	private byte				type;
	private int					index;
	private Waypoint			point;

	public final static byte	ADD	= 0;
	public final static byte	DEL	= 1;

	public WaypointMessage()
	{}

	public WaypointMessage( byte mess, int index, Waypoint wp )
	{
		type = mess;
		this.index = index;
		point = wp;
	}

	@Override
	public void fromBytes( ByteBuf buf )
	{
		type = buf.readByte();
		index = buf.readInt();
		final int dimID = buf.readInt();
		final int x = buf.readInt();
		final int y = buf.readInt();
		final int z = buf.readInt();
		point = new Waypoint( x, y, z, dimID );
	}

	@Override
	public void toBytes( ByteBuf buf )
	{
		buf.writeByte( type );
		buf.writeInt( index );
		buf.writeInt( point.dimensionId );
		buf.writeInt( point.posX );
		buf.writeInt( point.posY );
		buf.writeInt( point.posZ );
	}

	public static class Handler implements IMessageHandler< WaypointMessage, IMessage >
	{
		@Override
		public IMessage onMessage( WaypointMessage message, MessageContext ctx )
		{
			/*
			switch( message.type )
			{
			case ADD:
				ZombieBrains.waypointManager.points.put( message.index, new Waypoint( message.point.posX, message.point.posY, message.point.posZ ) );
			case DEL:
				ZombieBrains.waypointManager.points.get( message.index );
			}
			*/
			if( ctx.side == Side.CLIENT)
				ProccessMessage( message );
			else
				LogManager.getLogger( ModInfo.MODID ).warn( "WaypointMessage recieved on wrong side" );
			return null;
		}

		void ProccessMessage( WaypointMessage message )
		{
			/*
			switch( message.type )
			{
			case ADD:
				ZombieBrains.waypointManager.points.put( message.index, new Waypoint( message.point.posX, message.point.posY, message.point.posZ ) );
				break;
			case DEL:
				ZombieBrains.waypointManager.points.remove( message.index );
				break;
			}
			*/
		}
	}
}
