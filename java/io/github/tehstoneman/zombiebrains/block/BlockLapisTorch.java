package io.github.tehstoneman.zombiebrains.block;

import static net.minecraftforge.common.util.ForgeDirection.EAST;
import static net.minecraftforge.common.util.ForgeDirection.NORTH;
import static net.minecraftforge.common.util.ForgeDirection.SOUTH;
import static net.minecraftforge.common.util.ForgeDirection.WEST;
import io.github.tehstoneman.zombiebrains.ZombieBrains;
import io.github.tehstoneman.zombiebrains.client.renderer.ZombieParticleRenderer;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockLapisTorch extends Block
{
	public BlockLapisTorch()
	{
		super( Material.circuits );
		setTickRandomly( true );
		setCreativeTab( CreativeTabs.tabDecorations );
	}

	/**
	 * Returns a bounding box from the pool of bounding boxes (this means this
	 * box can change after the pool has been cleared to be reused)
	 */
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool( World world, int p_149668_2_, int p_149668_3_, int p_149668_4_ )
	{
		return null;
	}

	/**
	 * Is this block (a) opaque and (b) a full 1m cube? This determines whether
	 * or not to render the shared face of two adjacent blocks and also whether
	 * the player can attach torches, redstone wire, etc to this block.
	 */
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	/**
	 * If this block doesn't render as an ordinary block it will return False
	 * (examples: signs, buttons, stairs, etc)
	 */
	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	/**
	 * The type of render function that is called for this block
	 */
	@Override
	public int getRenderType()
	{
		return 2;
	}

	private boolean func_150107_m( World world, int p_150107_2_, int p_150107_3_, int p_150107_4_ )
	{
		if( World.doesBlockHaveSolidTopSurface( world, p_150107_2_, p_150107_3_, p_150107_4_ ) )
			return true;
		else
		{
			final Block block = world.getBlock( p_150107_2_, p_150107_3_, p_150107_4_ );
			return block.canPlaceTorchOnTop( world, p_150107_2_, p_150107_3_, p_150107_4_ );
		}
	}

	/**
	 * Checks to see if its valid to put this block at the specified
	 * coordinates. Args: world, x, y, z
	 */
	@Override
	public boolean canPlaceBlockAt( World world, int x, int y, int z )
	{
		return world.isSideSolid( x - 1, y, z, EAST, true )
				|| world.isSideSolid( x + 1, y, z, WEST, true )
				|| world.isSideSolid( x, y, z - 1, SOUTH, true )
				|| world.isSideSolid( x, y, z + 1, NORTH, true )
				|| func_150107_m( world, x, y - 1, z );
	}

	/**
	 * Called when a block is placed using its ItemBlock. Args: World, X, Y, Z,
	 * side, hitX, hitY, hitZ, block metadata
	 */
	@Override
	public int onBlockPlaced( World world, int x, int y, int z, int side, float hitX,
			float hitY, float hitZ, int meta )
	{
		int j1 = meta;

		if( side == 1 && func_150107_m( world, x, y - 1, z ) )
			j1 = 5;

		if( side == 2 && world.isSideSolid( x, y, z + 1, NORTH, true ) )
			j1 = 4;

		if( side == 3 && world.isSideSolid( x, y, z - 1, SOUTH, true ) )
			j1 = 3;

		if( side == 4 && world.isSideSolid( x + 1, y, z, WEST, true ) )
			j1 = 2;

		if( side == 5 && world.isSideSolid( x - 1, y, z, EAST, true ) )
			j1 = 1;

		return j1;
	}

	/**
	 * Ticks the block if it's been scheduled
	 */
	@Override
	public void updateTick( World world, int x, int y, int z, Random rand )
	{
		super.updateTick( world, x, y, z, rand );

		if( world.getBlockMetadata( x, y, z ) == 0 )
			onBlockAdded( world, x, y, z );
	}

	/**
	 * Called whenever the block is added into the world. Args: world, x, y, z
	 */
	@Override
	public void onBlockAdded( World world, int x, int y, int z )
	{
		if( world.getBlockMetadata( x, y, z ) == 0 )
			if( world.isSideSolid( x - 1, y, z, EAST, true ) )
				world.setBlockMetadataWithNotify( x, y, z, 1, 2 );
			else
				if( world.isSideSolid( x + 1, y, z, WEST, true ) )
					world.setBlockMetadataWithNotify( x, y, z, 2, 2 );
				else
					if( world.isSideSolid( x, y, z - 1, SOUTH, true ) )
						world.setBlockMetadataWithNotify( x, y, z, 3, 2 );
					else
						if( world.isSideSolid( x, y, z + 1, NORTH, true ) )
							world.setBlockMetadataWithNotify( x, y, z, 4, 2 );
						else
							if( func_150107_m( world, x, y - 1, z ) )
								world.setBlockMetadataWithNotify( x, y, z, 5, 2 );

		func_150109_e( world, x, y, z );
	}

	/**
	 * Lets the block know when one of its neighbor changes. Doesn't know which
	 * neighbor changed (coordinates passed are their own) Args: x, y, z,
	 * neighbor Block
	 */
	@Override
	public void onNeighborBlockChange( World world, int x, int y, int z, Block block )
	{
		func_150108_b( world, x, y, z, block );
	}

	protected boolean func_150108_b( World world, int x, int y, int z, Block block )
	{
		if( func_150109_e( world, x, y, z ) )
		{
			final int l = world.getBlockMetadata( x, y, z );
			boolean flag = false;

			if( !world.isSideSolid( x - 1, y, z, EAST, true ) && l == 1 )
				flag = true;

			if( !world.isSideSolid( x + 1, y, z, WEST, true ) && l == 2 )
				flag = true;

			if( !world.isSideSolid( x, y, z - 1, SOUTH, true ) && l == 3 )
				flag = true;

			if( !world.isSideSolid( x, y, z + 1, NORTH, true ) && l == 4 )
				flag = true;

			if( !func_150107_m( world, x, y - 1, z ) && l == 5 )
				flag = true;

			if( flag )
			{
				this.dropBlockAsItem( world, x, y, z,
						world.getBlockMetadata( x, y, z ), 0 );
				world.setBlockToAir( x, y, z );
				return true;
			}
			else
				return false;
		}
		else
			return true;
	}

	protected boolean func_150109_e( World world, int x, int y, int z )
	{
		if( !canPlaceBlockAt( world, x, y, z ) )
		{
			if( world.getBlock( x, y, z ) == this )
			{
				this.dropBlockAsItem( world, x, y, z,
						world.getBlockMetadata( x, y, z ), 0 );
				world.setBlockToAir( x, y, z );
			}

			return false;
		}
		else
			return true;
	}

	/**
	 * Ray traces through the blocks collision from start vector to end vector
	 * returning a ray trace hit. Args: world, x, y, z, startVec, endVec
	 */
	@Override
	 public MovingObjectPosition collisionRayTrace( World world, int x, int y, int z, Vec3 start,
			Vec3 end )
	{
		final int l = world.getBlockMetadata( x, y, z ) & 7;
		float f = 0.15F;

		if( l == 1 )
			 setBlockBounds( 0.0F, 0.2F, 0.5F - f, f * 2.0F, 0.8F, 0.5F + f );
		 else
			if( l == 2 )
				setBlockBounds( 1.0F - f * 2.0F, 0.2F, 0.5F - f, 1.0F, 0.8F, 0.5F + f );
			else
				if( l == 3 )
					setBlockBounds( 0.5F - f, 0.2F, 0.0F, 0.5F + f, 0.8F, f * 2.0F );
				else
					if( l == 4 )
						setBlockBounds( 0.5F - f, 0.2F, 1.0F - f * 2.0F, 0.5F + f, 0.8F, 1.0F );
					else
					{
						f = 0.1F;
						setBlockBounds( 0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.6F, 0.5F + f );
					}

		return super.collisionRayTrace( world, x, y, z, start, end );
	}

	/**
	 * A randomly called display update to be able to add particles or other
	 * items for display
	 */
	@Override
	 @SideOnly( Side.CLIENT )
	public void randomDisplayTick( World world, int x, int y, int z, Random rand )
	{
		final int l = world.getBlockMetadata( x, y, z );
		final double d0 = x + 0.5F;
		final double d1 = y + 0.7F;
		final double d2 = z + 0.5F;
		final double d3 = 0.2199999988079071D;
		final double d4 = 0.27000001072883606D;

		if( l == 1 )
		{
			world.spawnParticle( "smoke", d0 - d4, d1 + d3, d2, 0.0D, 0.0D, 0.0D );
			ZombieParticleRenderer.spawnParticle( "blueFlame", d0 - d4, d1 + d3, d2, 0.0D, 0.0D, 0.0D );
		}
		else
			if( l == 2 )
			{
				world.spawnParticle( "smoke", d0 + d4, d1 + d3, d2, 0.0D, 0.0D, 0.0D );
				ZombieParticleRenderer.spawnParticle( "blueFlame", d0 + d4, d1 + d3, d2, 0.0D, 0.0D, 0.0D );
			}
			else
				if( l == 3 )
				{
					world.spawnParticle( "smoke", d0, d1 + d3, d2 - d4, 0.0D, 0.0D, 0.0D );
					ZombieParticleRenderer.spawnParticle( "blueFlame", d0, d1 + d3, d2 - d4, 0.0D, 0.0D, 0.0D );
				}
				else
					if( l == 4 )
					{
						world.spawnParticle( "smoke", d0, d1 + d3, d2 + d4, 0.0D, 0.0D, 0.0D );
						ZombieParticleRenderer.spawnParticle( "blueFlame", d0, d1 + d3, d2 + d4, 0.0D, 0.0D, 0.0D );
					}
					else
					{
						world.spawnParticle( "smoke", d0, d1, d2, 0.0D, 0.0D, 0.0D );
						ZombieParticleRenderer.spawnParticle( "blueFlame", d0, d1, d2, 0.0D, 0.0D, 0.0D );
					}
	}
}