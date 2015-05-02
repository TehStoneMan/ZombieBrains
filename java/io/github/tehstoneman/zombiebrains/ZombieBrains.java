package io.github.tehstoneman.zombiebrains;

import io.github.tehstoneman.zombiebrains.block.BlockLapisTorch;
import io.github.tehstoneman.zombiebrains.entity.monster.EntityTameZombie;
import io.github.tehstoneman.zombiebrains.item.ItemBrainsSpawnEgg;
import io.github.tehstoneman.zombiebrains.proxies.CommonProxy;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod( modid = ModInfo.MODID, name = ModInfo.NAME, version = ModInfo.VERSION, dependencies = ModInfo.DEPENDENCIES, acceptedMinecraftVersions = ModInfo.MINECRAFT )
public class ZombieBrains
{
	public static ModMetadata	modMetadata;

	@Mod.Instance( value = ModInfo.MODID )
	public static ZombieBrains	instance;

	// Define proxies
	@SidedProxy( clientSide = ModInfo.PROXY_LOCATION + "ClientProxy", serverSide = ModInfo.PROXY_LOCATION + "CommonProxy" )
	public static CommonProxy	proxy;

	@Mod.EventHandler
	public void preInitialize( FMLPreInitializationEvent event )
	{
		// Initialise custom renderers
		proxy.initRenderers();
	}

	@Mod.EventHandler
	public void initialize( FMLInitializationEvent event )
	{
		EntityRegistry.registerModEntity( EntityTameZombie.class, "TameZombie", 0, this, 80, 1, true );
		//final Block blockLapisTorch = new BlockLapisTorch().setHardness( 0.0F ).setLightLevel( 0.9375F ).setStepSound( Block.soundTypeWood )
		//		.setBlockName( "torchLapis" ).setBlockTextureName( ModInfo.MODID + ":torch_lapis" );
		final Item itemSpawnEgg = new ItemBrainsSpawnEgg().setUnlocalizedName( "zombieBrainsSpawnEgg" ).setTextureName( "spawn_egg" );
		//GameRegistry.registerBlock( blockLapisTorch, "blockLapisTorch" );
		GameRegistry.registerItem( itemSpawnEgg, "spawnEggZombieBrains" );
	}

	@Mod.EventHandler
	public void postInitialization( FMLPostInitializationEvent event )
	{
		// Register event handler
		// MinecraftForge.EVENT_BUS.register( new SubscribedEvents() );
	}

}
