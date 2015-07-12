package io.github.tehstoneman.zombiebrains.block;

import io.github.tehstoneman.zombiebrains.client.renderer.ZombieParticleRenderer;
import io.github.tehstoneman.zombiebrains.tileentity.TileEntityZombieStone;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockZombieStone extends BlockContainer
{
	@SideOnly( Side.CLIENT )
	private IIcon	textureSide;
	@SideOnly( Side.CLIENT )
	private IIcon	textureTop;

	public BlockZombieStone()
	{
		super( Material.rock );
		setCreativeTab( CreativeTabs.tabBlock );
	}

	/**
	 * Gets the block's texture. Args: side, meta
	 */
	@Override
	@SideOnly( Side.CLIENT )
	public IIcon getIcon( int side, int meta )
	{
		if( side == 1 || side == 0 )
			return textureTop;
		else
			return textureSide;
	}

	@Override
	@SideOnly( Side.CLIENT )
	public void registerBlockIcons( IIconRegister iconRegister )
	{
		textureSide = iconRegister.registerIcon( getTextureName() );

		textureTop = iconRegister.registerIcon( getTextureName() + "_top" );
	}

	@Override
	public TileEntity createNewTileEntity( World world, int meta )
	{
		return new TileEntityZombieStone();
	}

	@Override
	public void onNeighborBlockChange( World world, int x, int y, int z, Block block )
	{
		final TileEntity tile = world.getTileEntity( x, y, z );
		if( tile != null && tile instanceof TileEntityZombieStone )
		{
			final TileEntityZombieStone multiBlock = (TileEntityZombieStone)tile;
			if( multiBlock.hasMaster() )
				if( multiBlock.isMaster() )
					if( !multiBlock.checkMultiBlockForm() )
						multiBlock.resetStructure();
					else
						if( !multiBlock.checkForMaster() )
							multiBlock.reset();
		}
		super.onNeighborBlockChange( world, x, y, z, block );
	}

	@Override
	@SideOnly( Side.CLIENT )
	public void randomDisplayTick( World world, int x, int y, int z, Random rand )
	{
		if(world.getBlockMetadata( x, y, z ) == 1)
			{
				final int l = world.getBlockMetadata( x, y, z );
				final double d0 = x + rand.nextFloat();
				final double d1 = y + 1.0F;
				final double d2 = z + rand.nextFloat();

				world.spawnParticle( "smoke", d0, d1, d2, 0.0D, 0.0D, 0.0D );
				ZombieParticleRenderer.spawnParticle( "blueFlame", d0, d1, d2, 0.0D, 0.0D, 0.0D );
			}
	}
	
	@Override
	public void breakBlock( World world, int x, int y, int z, Block block, int meta )
	{
		final TileEntity tile = world.getTileEntity( x, y, z );
		if( tile != null && tile instanceof TileEntityZombieStone )
		{
			final TileEntityZombieStone multiBlock = (TileEntityZombieStone)tile;
			if( multiBlock.hasMaster() )
			{
				multiBlock.resetStructure();
			}
		}
		super.breakBlock( world, x, y, z, block, meta );
	}
}
