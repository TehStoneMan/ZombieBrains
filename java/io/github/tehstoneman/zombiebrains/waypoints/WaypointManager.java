package io.github.tehstoneman.zombiebrains.waypoints;

import io.github.tehstoneman.zombiebrains.ModInfo;
import io.github.tehstoneman.zombiebrains.ZombieBrains;
import io.github.tehstoneman.zombiebrains.network.WaypointMessage;
import io.github.tehstoneman.zombiebrains.util.Help;
import io.github.tehstoneman.zombiebrains.util.Settings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class WaypointManager extends WorldSavedData
{
	public HashMap< Integer, Waypoint >	points;

	private int							nextKey;
	private NBTTagCompound				data	= new NBTTagCompound();

	public WaypointManager()
	{
		super( "WaypointManager" );
		points = new HashMap< Integer, Waypoint >();
		nextKey = 0;
	}

	public int addWaypoint( int x, int y, int z )
	{
		// Find valid index
		int index = 0;
		if( nextKey == points.size() )
			index = nextKey;
		else
			while( points.containsKey( index ) )
				index++;

		// Add waypoint to list
		addIndex( index, x, y, z );

		if( index >= nextKey )
			nextKey = index + 1;

		return index;
	}

	public void addIndex( int index, int x, int y, int z )
	{
		final Waypoint wp = new Waypoint( x, y, z );
		points.put( index, wp );
		markDirty();

		// Send debug message to clients
		if( Settings.debug )
			ZombieBrains.network.sendToAll( new WaypointMessage( WaypointMessage.ADD, index, wp ) );
	}

	public void removeWaypoint( int index )
	{
		points.remove( index );
		markDirty();

		// Send debug message to clients
		if( Settings.debug )
			ZombieBrains.network.sendToAll( new WaypointMessage( WaypointMessage.DEL, index, new Waypoint( 0, 0, 0 ) ) );

	}

	public void connect( Waypoint w0, Waypoint w1, float dist )
	{
		w0.addNeighbor( w1, dist );
		w1.addNeighbor( w0, dist );
	}

	public int getWaypointAt( int x, int y, int z )
	{
		for( final Map.Entry< Integer, Waypoint > p : points.entrySet() )
		{
			if( p.getValue().posX == x && p.getValue().posY == y && p.getValue().posZ == z )
				return p.getKey();
		}
		return -1;
	}

	public int getClosestWaypoint( int x, int y, int z )
	{
		for( final Map.Entry< Integer, Waypoint > p : points.entrySet() )
		{
			// if(p.getValue().pos.distance(mp)<10){
			// return p.getValue();
			// }
		}
		return -1;
	}

	public void connectAuto( float mdist )
	{
		final ArrayList< Waypoint > graph = new ArrayList< Waypoint >();
		for( final Map.Entry< Integer, Waypoint > p : points.entrySet() )
			graph.add( p.getValue() );

		for( int i = 0; i < graph.size() - 1; i++ )
			for( int j = i + 1; j < graph.size(); j++ )
			{
				// float dist = graph.get(i).pos.distance(graph.get(j).pos);
				// if(dist<mdist){
				// connect(graph.get(i),graph.get(j),dist);
				// }
			}
	}

	public ArrayList< Waypoint > getPath( Waypoint start, Waypoint end )
	{
		final ArrayList< Integer > visited = new ArrayList< Integer >();
		final ArrayList< Waypoint > path = new ArrayList< Waypoint >();
		final ArrayList< Waypoint > graph = new ArrayList< Waypoint >();

		for( final Map.Entry< Integer, Waypoint > p : points.entrySet() )
		{
			graph.add( p.getValue() );
			p.getValue().cdist = Float.POSITIVE_INFINITY;
			p.getValue().cpre = null;
		}

		start.cdist = 0;

		Waypoint c, u = null;
		while( graph.size() > 0 )
		{
			float a = Float.POSITIVE_INFINITY;
			for( final Waypoint p : graph )
				if( p.cdist < a )
				{
					u = p;
					a = p.cdist;
				}
			graph.remove( u );
			for( int j = 0; j < u.neighbors.size(); j++ )
			{
				final Waypoint v = u.neighbors.get( j );
				if( graph.contains( v ) )
				{
					final float alt = u.cdist + u.distances.get( j );
					if( alt < u.neighbors.get( j ).cdist )
					{
						v.cdist = alt;
						v.cpre = u;
					}
				}
			}
			u = null;
		}

		c = end;
		path.add( c );
		while( c.cpre != null )
		{
			path.add( 0, c.cpre );
			c = c.cpre;
		}

		if( path.size() == 0 )
		{
			Help.p( "no path found from:" + start.id + " to " + end.id + " !" );
			return null;
		}
		/*
		 * ArrayList<Waypoint> rpath = new ArrayList<Waypoint>();
		 * while(path.size()>0){ rpath.add(path.get(path.size()-1));
		 * path.remove(path.size()-1); }
		 */
		return path;
	}

	public void listPoints( EntityPlayer player )
	{
		if( points.size() == 0 )
			player.addChatMessage( new ChatComponentText( "No waypoints to list." ) );
		else
			for( int i = 0; i < points.size(); i++ )
				player.addChatMessage( new ChatComponentText( "Point " + points.keySet().toArray()[i] + ":" + points.entrySet().toArray()[i] ) );
	}

	/**
	 * Draw the available waypoints and edges
	 *
	 * @param mc
	 * @param x
	 *            = viewer x location
	 * @param y
	 *            = viewer y location
	 * @param z
	 *            = viewer z location
	 */
	@SideOnly( Side.CLIENT )
	public void draw( Minecraft mc, double x, double y, double z )
	{
		Waypoint waypoint;
		for( final Map.Entry< Integer, Waypoint > point : points.entrySet() )
		{
			waypoint = point.getValue();
			if( waypoint != null )
				waypoint.draw( mc, x, y, z );
		}
	}

	protected void loadOrCreateData( World world )
	{
		if( points == null )
			points = world.perWorldStorage.loadData( WaypointManager.class, getTagName() );
		
		if( points == null )
		{
			
		}
	}
	
	@Override
	public void readFromNBT( NBTTagCompound compound )
	{
		data = compound.getCompoundTag( "Waypoints" );
	}

	@Override
	public void writeToNBT( NBTTagCompound compound )
	{
		for( final Map.Entry< Integer, Waypoint > point : points.entrySet() )
		{
			compound.setInteger( "Index", point.getKey() );
			compound.setInteger( "x", point.getValue().posX );
			compound.setInteger( "y", point.getValue().posY );
			compound.setInteger( "z", point.getValue().posZ );
		}
		//compound.setTag( "Waypoints", data );
	}
}