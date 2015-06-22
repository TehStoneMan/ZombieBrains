package io.github.tehstoneman.zombiebrains.tileentity;

import io.github.tehstoneman.zombiebrains.ZombieBrains;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntityLapisTorch extends TileEntity
{
	public int	waypoint;

	public TileEntityLapisTorch( World world )
	{
		super();
		if( !world.isRemote )
		{
			//waypoint = ZombieBrains.waypointManager.addWaypoint( xCoord, yCoord, zCoord );
			//markDirty();
		}
	}

	@Override
	public void readFromNBT( NBTTagCompound compound )
	{
		super.readFromNBT( compound );
		waypoint = compound.getInteger( "waypoint" );
	}

	@Override
	public void writeToNBT( NBTTagCompound compound )
	{
		super.writeToNBT( compound );
		compound.setInteger( "waypoint", waypoint );
	}
}
