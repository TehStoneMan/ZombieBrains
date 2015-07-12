package io.github.tehstoneman.zombiebrains.item;

import io.github.tehstoneman.zombiebrains.entity.EntityBrainsList;
import io.github.tehstoneman.zombiebrains.entity.monster.EntityTameZombie;

import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBrainsSpawnEgg extends Item
{
	@SideOnly( Side.CLIENT )
	private IIcon	theIcon;

	public ItemBrainsSpawnEgg()
	{
		setHasSubtypes( true );
		setCreativeTab( CreativeTabs.tabMisc );
	}

	@Override
	public String getItemStackDisplayName( ItemStack itemStack )
	{
		String s = ( "" + StatCollector.translateToLocal( this.getUnlocalizedName() + ".name" ) ).trim();
		final String s1 = EntityBrainsList.getStringFromID( itemStack.getItemDamage() );

		if( s1 != null )
			s = s + " " + StatCollector.translateToLocal( "entity." + s1 + ".name" );

		return s;
	}

	@Override
	@SideOnly( Side.CLIENT )
	public int getColorFromItemStack( ItemStack itemStack, int index )
	{
		final EntityBrainsList.EntityEggInfo entityegginfo = (EntityBrainsList.EntityEggInfo)EntityBrainsList.entityEggs.get( Integer.valueOf( itemStack
				.getItemDamage() ) );
		return entityegginfo != null ? index == 0 ? entityegginfo.primaryColor : entityegginfo.secondaryColor : 16777215;
	}

	/**
	 * Callback for item usage. If the item does something special on right
	 * clicking, he will have one of those. Return True if something happen and
	 * false if it don't. This is for ITEMS, not BLOCKS
	 */
	@Override
	public boolean onItemUse( ItemStack itemStack, EntityPlayer player, World world, int p_77648_4_, int p_77648_5_, int p_77648_6_,
			int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_ )
	{
		if( world.isRemote )
			return true;
		else
		{
			final Block block = world.getBlock( p_77648_4_, p_77648_5_, p_77648_6_ );
			p_77648_4_ += Facing.offsetsXForSide[p_77648_7_];
			p_77648_5_ += Facing.offsetsYForSide[p_77648_7_];
			p_77648_6_ += Facing.offsetsZForSide[p_77648_7_];
			double d0 = 0.0D;

			if( p_77648_7_ == 1 && block.getRenderType() == 11 )
				d0 = 0.5D;

			final Entity entity = spawnCreature( world, itemStack.getItemDamage(), p_77648_4_ + 0.5D, p_77648_5_ + d0, p_77648_6_ + 0.5D );

			if( entity != null )
			{
				if( entity instanceof EntityLivingBase && itemStack.hasDisplayName() )
					( (EntityLiving)entity ).setCustomNameTag( itemStack.getDisplayName() );

				if( !player.capabilities.isCreativeMode )
					--itemStack.stackSize;
			}

			return true;
		}
	}

	/**
	 * Called whenever this item is equipped and the right mouse button is
	 * pressed. Args: itemStack, world, entityPlayer
	 */
	@Override
	public ItemStack onItemRightClick( ItemStack itemStack, World world, EntityPlayer player )
	{
		if( world.isRemote )
			return itemStack;
		else
		{
			final MovingObjectPosition movingobjectposition = getMovingObjectPositionFromPlayer( world, player, true );

			if( movingobjectposition == null )
				return itemStack;
			else
			{
				if( movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK )
				{
					final int i = movingobjectposition.blockX;
					final int j = movingobjectposition.blockY;
					final int k = movingobjectposition.blockZ;

					if( !world.canMineBlock( player, i, j, k ) )
						return itemStack;

					if( !player.canPlayerEdit( i, j, k, movingobjectposition.sideHit, itemStack ) )
						return itemStack;

					if( world.getBlock( i, j, k ) instanceof BlockLiquid )
					{
						final Entity entity = spawnCreature( world, itemStack.getItemDamage(), i, j, k );

						if( entity != null )
						{
							if( entity instanceof EntityLivingBase && itemStack.hasDisplayName() )
								( (EntityLiving)entity ).setCustomNameTag( itemStack.getDisplayName() );

							if( !player.capabilities.isCreativeMode )
								--itemStack.stackSize;
						}
					}
				}

				return itemStack;
			}
		}
	}

	/**
	 * Spawns the creature specified by the egg's type in the location specified
	 * by the last three parameters. Parameters: world, entityID, x, y, z.
	 */
	public static Entity spawnCreature( World world, int id, double x, double y, double z )
	{
		if( !EntityBrainsList.entityEggs.containsKey( Integer.valueOf( id ) ) )
			return null;
		else
		{
			Entity entity = null;

			for( int j = 0; j < 1; ++j )
			{
				entity = EntityBrainsList.createEntityByID( id, world );

				if( entity != null && entity instanceof EntityLivingBase )
				{
					final EntityLiving entityliving = (EntityLiving)entity;
					entity.setLocationAndAngles( x, y, z,
							MathHelper.wrapAngleTo180_float( world.rand.nextFloat() * 360.0F ), 0.0F );
					entityliving.rotationYawHead = entityliving.rotationYaw;
					entityliving.renderYawOffset = entityliving.rotationYaw;
					entityliving.onSpawnWithEgg( (IEntityLivingData)null );
					world.spawnEntityInWorld( entity );
					entityliving.playLivingSound();
				}
			}

			return entity;
		}
	}

	@Override
	@SideOnly( Side.CLIENT )
	public boolean requiresMultipleRenderPasses()
	{
		return true;
	}

	/**
	 * Gets an icon index based on an item's damage value and the given render
	 * pass
	 */
	@Override
	@SideOnly( Side.CLIENT )
	public IIcon getIconFromDamageForRenderPass( int damage, int pass )
	{
		return pass > 0 ? theIcon : super.getIconFromDamageForRenderPass( damage, pass );
	}

	/**
	 * returns a list of items with the same ID, but different meta (eg: dye
	 * returns 16 items)
	 */
	@Override
	@SideOnly( Side.CLIENT )
	public void getSubItems( Item item, CreativeTabs tab, List list )
	{
		final Iterator iterator = EntityBrainsList.entityEggs.values().iterator();

		while( iterator.hasNext() )
		{
			final EntityBrainsList.EntityEggInfo entityegginfo = (EntityBrainsList.EntityEggInfo)iterator.next();
			list.add( new ItemStack( item, 1, entityegginfo.spawnedID ) );
		}
	}

	@Override
	@SideOnly( Side.CLIENT )
	public void registerIcons( IIconRegister icon )
	{
		super.registerIcons( icon );
		theIcon = icon.registerIcon( getIconString() + "_overlay" );
	}
}