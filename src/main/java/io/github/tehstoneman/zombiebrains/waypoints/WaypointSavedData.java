package io.github.tehstoneman.zombiebrains.waypoints;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldSavedData;

public class WaypointSavedData extends WorldSavedData
{
	private NBTTagCompound	waypointData	= new NBTTagCompound();

	public WaypointSavedData( String tagName )
	{
		super( tagName );
	}

	@Override
	public void readFromNBT( NBTTagCompound compound )
	{
		waypointData = compound.getCompoundTag( "Waypoints" );
	}

	public void addWaypointTag( NBTTagCompound compound, int index )
	{
		compound.setInteger( "Index", index );
		waypointData.setTag( "Wp [" + index + "]", compound );
	}

	public void removeWaypointTag( int index )
	{
		waypointData.removeTag( "Wp [" + index + "]" );
	}

	@Override
	public void writeToNBT( NBTTagCompound compound )
	{
		compound.setTag( "Waypoints", waypointData );
	}

	public NBTTagCompound getWaypointData()
	{
		return waypointData;
	}

}
