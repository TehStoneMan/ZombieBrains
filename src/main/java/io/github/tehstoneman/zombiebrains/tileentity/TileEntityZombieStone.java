package io.github.tehstoneman.zombiebrains.tileentity;

import io.github.tehstoneman.zombiebrains.ModInfo;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import org.apache.logging.log4j.LogManager;

public class TileEntityZombieStone extends TileEntity
{
	private boolean	hasMaster, isMaster, isTower;
	private int		masterX, masterY, masterZ;

	@Override
	public void updateEntity()
	{
		super.updateEntity();
		if( !worldObj.isRemote )
			if( hasMaster() )
			{
				if( isMaster() )
				{
					// Put stuff you want the multiblock structure to do here!
				}
			}
			else
				// Constantly check is the structure is formed until it is.
				if( checkMultiBlockForm() )
					setupStructure();
	}

	@Override
	public void writeToNBT( NBTTagCompound compound )
	{
		super.writeToNBT( compound );
		compound.setInteger( "masterX", masterX );
		compound.setInteger( "masterY", masterY );
		compound.setInteger( "masterZ", masterZ );
		compound.setBoolean( "hasMaster", hasMaster );
		compound.setBoolean( "isMaster", isMaster );
		compound.setBoolean( "isTower", isTower );
		if( hasMaster && isMaster )
		{
			// Any other value should only be saved to the master
		}
	}

	@Override
	public void readFromNBT( NBTTagCompound compound )
	{
		super.readFromNBT( compound );
		masterX = compound.getInteger( "masterX" );
		masterY = compound.getInteger( "masterY" );
		masterZ = compound.getInteger( "masterZ" );
		hasMaster = compound.getBoolean( "hasMaster" );
		isMaster = compound.getBoolean( "isMaster" );
		isTower = compound.getBoolean( "isTower" );
		if( hasMaster && isMaster )
		{
			// Any other value should only be read by the master
		}
	}

	public boolean hasMaster()
	{
		return hasMaster;
	}

	public boolean isMaster()
	{
		return isMaster;
	}

	public boolean isTower()
	{
		return isTower;
	}

	public int getMasterX()
	{
		return masterX;
	}

	public int getMasterY()
	{
		return masterY;
	}

	public int getMasterZ()
	{
		return masterZ;
	}

	public void setHasMaster( boolean bool )
	{
		hasMaster = bool;
	}

	public void setIsMaster( boolean bool )
	{
		isMaster = bool;
		if( bool )
			worldObj.setBlockMetadataWithNotify( xCoord, yCoord, zCoord, 1, 7 );
		else
			worldObj.setBlockMetadataWithNotify( xCoord, yCoord, zCoord, 0, 7 );
	}

	public void setIsTower( boolean bool )
	{
		isTower = bool;
		if( bool )
			worldObj.setBlockMetadataWithNotify( xCoord, yCoord, zCoord, 2, 7 );
		else
			worldObj.setBlockMetadataWithNotify( xCoord, yCoord, zCoord, 0, 7 );
	}

	public void setMasterCoords( int x, int y, int z )
	{
		masterX = x;
		masterY = y;
		masterZ = z;
	}

	public boolean checkMultiBlockForm()
	{
		int offset = 0;
		TileEntity tile;

		for( offset = 0; offset < 4; offset++ )
		{
			// Check for square surround
			tile = worldObj.getTileEntity( xCoord - 2 + offset, yCoord, zCoord - 2 );
			if( !( tile != null && tile instanceof TileEntityZombieStone ) )
				return false;
			tile = worldObj.getTileEntity( xCoord + 2, yCoord, zCoord - 2 + offset );
			if( !( tile != null && tile instanceof TileEntityZombieStone ) )
				return false;
			tile = worldObj.getTileEntity( xCoord + 2 - offset, yCoord, zCoord + 2 );
			if( !( tile != null && tile instanceof TileEntityZombieStone ) )
				return false;
			tile = worldObj.getTileEntity( xCoord - 2, yCoord, zCoord + 2 - offset );
			if( !( tile != null && tile instanceof TileEntityZombieStone ) )
				return false;

			// Check for pillars
			if( offset > 0 )
			{
				tile = worldObj.getTileEntity( xCoord - 2, yCoord + offset, zCoord - 2 );
				if( !( tile != null && tile instanceof TileEntityZombieStone ) )
					return false;
				tile = worldObj.getTileEntity( xCoord + 2, yCoord + offset, zCoord - 2 );
				if( !( tile != null && tile instanceof TileEntityZombieStone ) )
					return false;
				tile = worldObj.getTileEntity( xCoord + 2, yCoord + offset, zCoord + 2 );
				if( !( tile != null && tile instanceof TileEntityZombieStone ) )
					return false;
				tile = worldObj.getTileEntity( xCoord - 2, yCoord + offset, zCoord + 2 );
				if( !( tile != null && tile instanceof TileEntityZombieStone ) )
					return false;
			}
		}

		LogManager.getLogger( ModInfo.MODID ).info( "Success!" );
		return true;

		// Scan 3x3x3 area, starting with the bottom left corner
		/*
		 * for( int x = xCoord - 1; x < xCoord + 2; x++ ) for( int y = yCoord; y
		 * < yCoord + 3; y++ ) for( int z = zCoord - 1; z < zCoord + 2; z++ ) {
		 * TileEntity tile = worldObj.getTileEntity( x, y, z ); // Make sure
		 * tile isn't null, is an instance of the same tile. and isn't already a
		 * part of a multiblock if( tile != null && (tile instanceof
		 * TileEntityZombieStone)) { if( this.isMaster()) { if(
		 * ((TileEntityZombieStone)tile).hasMaster()) i++; }else
		 * if(!((TileEntityZombieStone)tile).hasMaster()) i++; } } // Check if
		 * there are 26 blocks present ((3x3x3)-1) and check that center block
		 * is empty return i > 25 && worldObj.isAirBlock( xCoord, yCoord + 1,
		 * zCoord );
		 */
	}

	public void setupStructure()
	{
		int offset = 0;
		TileEntity tile;

		for( offset = 0; offset < 4; offset++ )
		{
			// Check for square surround
			tile = worldObj.getTileEntity( xCoord - 2 + offset, yCoord, zCoord - 2 );
			if( !( tile != null && tile instanceof TileEntityZombieStone ) )
				setBlock((TileEntityZombieStone)tile,xCoord - 2 + offset, yCoord, zCoord - 2);
			tile = worldObj.getTileEntity( xCoord + 2, yCoord, zCoord - 2 + offset );
			if( !( tile != null && tile instanceof TileEntityZombieStone ) )
				setBlock((TileEntityZombieStone)tile,xCoord - 2 + offset, yCoord, zCoord - 2);
			tile = worldObj.getTileEntity( xCoord + 2 - offset, yCoord, zCoord + 2 );
			if( !( tile != null && tile instanceof TileEntityZombieStone ) )
				setBlock((TileEntityZombieStone)tile,xCoord - 2 + offset, yCoord, zCoord - 2);
			tile = worldObj.getTileEntity( xCoord - 2, yCoord, zCoord + 2 - offset );
			if( !( tile != null && tile instanceof TileEntityZombieStone ) )
				setBlock((TileEntityZombieStone)tile,xCoord - 2 + offset, yCoord, zCoord - 2);

			// Check for pillars
			if( offset > 0 )
			{
				tile = worldObj.getTileEntity( xCoord - 2, yCoord + offset, zCoord - 2 );
				if( !( tile != null && tile instanceof TileEntityZombieStone ) )
					setBlock((TileEntityZombieStone)tile,xCoord - 2 + offset, yCoord, zCoord - 2);
				tile = worldObj.getTileEntity( xCoord + 2, yCoord + offset, zCoord - 2 );
				if( !( tile != null && tile instanceof TileEntityZombieStone ) )
					setBlock((TileEntityZombieStone)tile,xCoord - 2 + offset, yCoord, zCoord - 2);
				tile = worldObj.getTileEntity( xCoord + 2, yCoord + offset, zCoord + 2 );
				if( !( tile != null && tile instanceof TileEntityZombieStone ) )
					setBlock((TileEntityZombieStone)tile,xCoord - 2 + offset, yCoord, zCoord - 2);
				tile = worldObj.getTileEntity( xCoord - 2, yCoord + offset, zCoord + 2 );
				if( !( tile != null && tile instanceof TileEntityZombieStone ) )
					setBlock((TileEntityZombieStone)tile,xCoord - 2 + offset, yCoord, zCoord - 2);
			}
		}

		/*
		for( int x = xCoord - 1; x < xCoord + 2; x++ )
			for( int y = yCoord; y < yCoord + 3; y++ )
				for( int z = zCoord - 1; z < zCoord + 2; z++ )
				{
					final TileEntity tile = worldObj.getTileEntity( x, y, z );
					// Check if block is bottom center block
					final boolean master = x == xCoord && y == yCoord && z == zCoord;
					if( tile != null && tile instanceof TileEntityZombieStone )
					{
						( (TileEntityZombieStone)tile ).setMasterCoords( xCoord, yCoord, zCoord );
						( (TileEntityZombieStone)tile ).setHasMaster( true );
						( (TileEntityZombieStone)tile ).setIsMaster( master );
					}
				}
				*/
	}
	
	public void setBlock( TileEntityZombieStone tile, int x, int y, int z )
	{
		final boolean master = x == xCoord && y == yCoord && z == zCoord;
		tile.setMasterCoords( xCoord, yCoord, zCoord );
		tile.setHasMaster( true );
		tile.setIsMaster( master );
	}

	public void resetStructure()
	{
		for( int x = xCoord - 1; x < xCoord + 2; x++ )
			for( int y = yCoord; y < yCoord + 3; y++ )
				for( int z = zCoord - 1; z < zCoord + 2; z++ )
				{
					final TileEntity tile = worldObj.getTileEntity( x, y, z );
					if( tile != null && tile instanceof TileEntityZombieStone )
						( (TileEntityZombieStone)tile ).reset();
				}
	}

	// Reset method to be run when the master is gone or tells them to
	public void reset()
	{
		masterX = 0;
		masterY = 0;
		masterZ = 0;
		hasMaster = false;
		isMaster = false;
		isTower = false;
		worldObj.setBlockMetadataWithNotify( xCoord, yCoord, zCoord, 0, 7 );
	}

	public boolean checkForMaster()
	{
		final TileEntity tile = worldObj.getTileEntity( masterZ, masterY, masterZ );
		return tile != null && tile instanceof TileEntityZombieStone;
	}
}
