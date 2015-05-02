package io.github.tehstoneman.zombiebrains.entity;

import io.github.tehstoneman.zombiebrains.entity.monster.EntityTameZombie;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.FMLLog;

public class EntityBrainsList
{
	private static final Logger	logger					= LogManager.getLogger();
	/** Provides a mapping between entity classes and a string */
	public static Map			stringToClassMapping	= new HashMap();
	/** Provides a mapping between a string and an entity classes */
	public static Map			classToStringMapping	= new HashMap();
	/** provides a mapping between an entityID and an Entity Class */
	public static Map			IDtoClassMapping		= new HashMap();
	/** provides a mapping between an Entity Class and an entity ID */
	private static Map			classToIDMapping		= new HashMap();
	/** Maps entity names to their numeric identifiers */
	private static Map			stringToIDMapping		= new HashMap();
	/** This is a HashMap of the Creative Entity Eggs/Spawners. */
	public static HashMap		entityEggs				= new LinkedHashMap();

	/**
	 * adds a mapping between Entity classes and both a string representation
	 * and an ID
	 */
	public static void addMapping( Class entityClass, String name, int id )
	{
		if( stringToClassMapping.containsKey( name ) )
			throw new IllegalArgumentException( "ID is already registered: " + name );
		else
			if( IDtoClassMapping.containsKey( Integer.valueOf( id ) ) )
				throw new IllegalArgumentException( "ID is already registered: " + id );
			else
			{
				stringToClassMapping.put( name, entityClass );
				classToStringMapping.put( entityClass, name );
				IDtoClassMapping.put( Integer.valueOf( id ), entityClass );
				classToIDMapping.put( entityClass, Integer.valueOf( id ) );
				stringToIDMapping.put( name, Integer.valueOf( id ) );
			}
	}

	/**
	 * Adds a entity mapping with egg info.
	 */
	public static void addMapping( Class entityClass, String name, int id, int primaryColor, int secondaryColor )
	{
		addMapping( entityClass, name, id );
		entityEggs.put( Integer.valueOf( id ), new EntityBrainsList.EntityEggInfo( id, primaryColor, secondaryColor ) );
	}

	/**
	 * Create a new instance of an entity in the world by using the entity name.
	 */
	public static Entity createEntityByName( String name, World world )
	{
		Entity entity = null;

		try
		{
			final Class oclass = (Class)stringToClassMapping.get( name );

			if( oclass != null )
				entity = (Entity)oclass.getConstructor( new Class[] { World.class } ).newInstance( new Object[] { world } );
		}
		catch( final Exception exception )
		{
			exception.printStackTrace();
		}

		return entity;
	}

	/**
	 * create a new instance of an entity from NBT store
	 */
	public static Entity createEntityFromNBT( NBTTagCompound tagNBT, World world )
	{
		Entity entity = null;

		Class oclass = null;
		try
		{
			oclass = (Class)stringToClassMapping.get( tagNBT.getString( "id" ) );

			if( oclass != null )
				entity = (Entity)oclass.getConstructor( new Class[] { World.class } ).newInstance( new Object[] { world } );
		}
		catch( final Exception exception )
		{
			exception.printStackTrace();
		}

		if( entity != null )
			try
			{
				entity.readFromNBT( tagNBT );
			}
			catch( final Exception e )
			{
				FMLLog.log( Level.ERROR, e,
						"An Entity %s(%s) has thrown an exception during loading, its state cannot be restored. Report this to the mod author",
						tagNBT.getString( "id" ), oclass.getName() );
				entity = null;
			}
		else
			logger.warn( "Skipping Entity with id " + tagNBT.getString( "id" ) );

		return entity;
	}

	/**
	 * Create a new instance of an entity in the world by using an entity ID.
	 */
	public static Entity createEntityByID( int id, World world )
	{
		Entity entity = null;

		try
		{
			final Class oclass = getClassFromID( id );

			if( oclass != null )
				entity = (Entity)oclass.getConstructor( new Class[] { World.class } ).newInstance( new Object[] { world } );
		}
		catch( final Exception exception )
		{
			exception.printStackTrace();
		}

		if( entity == null )
			logger.warn( "Skipping Entity with id " + id );

		return entity;
	}

	/**
	 * gets the entityID of a specific entity
	 */
	public static int getEntityID( Entity entity )
	{
		final Class oclass = entity.getClass();
		return classToIDMapping.containsKey( oclass ) ? ( (Integer)classToIDMapping.get( oclass ) ).intValue() : 0;
	}

	/**
	 * Return the class assigned to this entity ID.
	 */
	public static Class getClassFromID( int id )
	{
		return (Class)IDtoClassMapping.get( Integer.valueOf( id ) );
	}

	/**
	 * Gets the string representation of a specific entity.
	 */
	public static String getEntityString( Entity entity )
	{
		return (String)classToStringMapping.get( entity.getClass() );
	}

	/**
	 * Finds the class using IDtoClassMapping and classToStringMapping
	 */
	public static String getStringFromID( int id )
	{
		final Class oclass = getClassFromID( id );
		return oclass != null ? (String)classToStringMapping.get( oclass ) : null;
	}

	public static void func_151514_a()
	{}

	public static Set func_151515_b()
	{
		return Collections.unmodifiableSet( stringToIDMapping.keySet() );
	}

	static
	{
		addMapping( EntityTameZombie.class, "TameZombie", 1, 0x88AFAF, 0x799C65 );
	}

	public static class EntityEggInfo
	{
		/** The entityID of the spawned mob */
		public final int			spawnedID;
		/** Base colour of the egg */
		public final int			primaryColor;
		/** Colour of the egg spots */
		public final int			secondaryColor;
		//public final StatBase		field_151512_d;
		//public final StatBase		field_151513_e;

		public EntityEggInfo( int id, int colour1, int colour2 )
		{
			spawnedID = id;
			primaryColor = colour1;
			secondaryColor = colour2;
			//this.field_151512_d = StatList.func_151182_a(this);
			//this.field_151513_e = StatList.func_151176_b(this);
		}
	}
}