package io.github.tehstoneman.zombiebrains.waypoints;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldSavedData;

public class WaypointSavedData extends WorldSavedData
{
	private NBTTagCompound waypointData = new NBTTagCompound();

	public WaypointSavedData( String tagName )
	{
		super( tagName );
	}

	@Override
	public void readFromNBT( NBTTagCompound compound )
	{
		waypointData = compound.getCompoundTag( "Waypoints" );
	}

	@Override
	public void writeToNBT( NBTTagCompound compound )
	{
		compound.setTag( "Waypoints", waypointData );
	}
}
