package io.github.tehstoneman.zombiebrains.waypoints;

import io.github.tehstoneman.zombiebrains.ModInfo;
import io.github.tehstoneman.zombiebrains.ZombieBrains;
import io.github.tehstoneman.zombiebrains.network.WaypointMessage;
import io.github.tehstoneman.zombiebrains.util.Help;
import io.github.tehstoneman.zombiebrains.util.Settings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.logging.log4j.LogManager;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class WaypointManager
{
	/** Saved and loaded world data containing previously recorded waypooints */
	private WaypointSavedData				waypointData;

	protected HashMap< Integer, Waypoint >	points	= new HashMap< Integer, Waypoint >();

	/** This world object. */
	protected World							worldObj;

	private int								nextKey;
	private NBTTagCompound					data	= new NBTTagCompound();

	public WaypointManager()
	{
		// super( "WaypointManager" );
		points = new HashMap< Integer, Waypoint >();
		nextKey = 0;
	}

	/** The name of the NBTTagCompound stored in the world data */
	public String getTagName()
	{
		return "zWaypoints";
	}

	/**
	 * Reads appropriate data from NBT compound and places it in the structure
	 * map; this allows for different storage formats (NBTTagCompound,
	 * NBTTagList, etc) in each MapGen
	 */
	protected void translateNbtIntoMap( NBTTagCompound compound )
	{}

	/**
	 * If waypointData is null, it is loaded from world storage if available or a
	 * new one is created
	 */
	public final void loadOrCreateData( World world )
	{
		if( waypointData == null )
		{
			waypointData = (WaypointSavedData)world.perWorldStorage.loadData( WaypointSavedData.class, getTagName() );

			if( waypointData == null )
			{
				// Create empty storage
				waypointData = new WaypointSavedData( getTagName() );
				world.perWorldStorage.setData( getTagName(), waypointData );
			}
			else
			{
				// Load from file
				final NBTTagCompound compound = waypointData.getWaypointData();
				// func_150296_c is getKeySet()
				final Iterator< String > iterator = compound.func_150296_c().iterator();
				while( iterator.hasNext() )
				{
					final String s = iterator.next();
					final NBTTagCompound wpCompound = (NBTTagCompound)compound.getTag( s );
					final Waypoint wp = new Waypoint( 0, 0, 0, 0 );
					wp.readFromNBT( wpCompound );
					points.put( wpCompound.getInteger( "Index" ), wp );
					if( !world.isRemote )
					{
						// Send debug message to clients
						if( Settings.debug )
							ZombieBrains.network.sendToAll( new WaypointMessage( WaypointMessage.ADD, wpCompound.getInteger( "Index" ), wp ) );
					}
				}
			}
		}
	}

	/**
	 * Wrapper method to add compound to waypointData
	 */
	protected final void addWaypointTag( NBTTagCompound compound, int index )
	{
		waypointData.addWaypointTag( compound, index );
		waypointData.markDirty();
	}

	/**
	 * Wrapper method to add compound to waypointData
	 */
	protected final void removeWaypointTag( int index )
	{
		waypointData.removeWaypointTag( index );
		waypointData.markDirty();
	}

	public int addWaypoint( World world, int x, int y, int z )
	{
		// Find valid index
		int index = 0;
		if( nextKey == points.size() )
			index = nextKey;
		else
			while( points.containsKey( index ) )
				index++;

		// Add waypoint to list
		addIndex( world, index, x, y, z );

		if( index >= nextKey )
			nextKey = index + 1;

		return index;
	}

	public void addIndex( World world, int index, int x, int y, int z )
	{
		final Waypoint wp = new Waypoint( x, y, z, world.provider.dimensionId );
		points.put( index, wp );
		if( !world.isRemote )
		{
			loadOrCreateData( world );
			NBTTagCompound compound = new NBTTagCompound();
			wp.writeToNBT( compound );
			addWaypointTag( compound, index );

			// Send debug message to clients
			if( Settings.debug )
				ZombieBrains.network.sendToAll( new WaypointMessage( WaypointMessage.ADD, index, wp ) );
		}
	}

	public void removeIndex( World world, int index )
	{
		points.remove( index );
		if( !world.isRemote )
		{
			loadOrCreateData( world );

			if( Settings.debug )
				ZombieBrains.network.sendToAll( new WaypointMessage( WaypointMessage.DEL, index, new Waypoint( 0, 0, 0, 0 ) ) );
		}

	}

	public void connect( Waypoint w0, Waypoint w1, float dist )
	{
		w0.addNeighbor( w1, dist );
		w1.addNeighbor( w0, dist );
	}

	public int getWaypointAt( int x, int y, int z, int dimId )
	{
		for( final Map.Entry< Integer, Waypoint > p : points.entrySet() )
			if( p.getValue().dimensionId == dimId && p.getValue().posX == x && p.getValue().posY == y && p.getValue().posZ == z )
				return p.getKey();
		return -1;
	}

	public int getClosestWaypoint( int x, int y, int z, int dimId )
	{
		double dist = 0.0;
		int index = -1;
		for( final Map.Entry< Integer, Waypoint > p : points.entrySet() )
		{
			if( p.getValue().dimensionId == dimId )
			{
				double d = p.getValue().distanceTo( x, y, z );
				if( index < 0 || d < dist )
				{
					dist = d;
					index = p.getKey();
				}
			}
		}
		return index;
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
		World world = mc.theWorld;
		for( final Map.Entry< Integer, Waypoint > point : points.entrySet() )
		{
			waypoint = point.getValue();
			if( waypoint != null && waypoint.dimensionId == world.provider.dimensionId )
				waypoint.draw( mc, x, y, z );
		}
	}

	public Waypoint getIndex( int wpIndex )
	{
		return points.get( wpIndex );
	}
}