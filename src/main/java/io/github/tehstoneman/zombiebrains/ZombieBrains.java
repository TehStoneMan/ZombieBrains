package io.github.tehstoneman.zombiebrains;

import io.github.tehstoneman.zombiebrains.block.BlockLapisTorch;
import io.github.tehstoneman.zombiebrains.block.BlockZombieStone;
import io.github.tehstoneman.zombiebrains.entity.monster.EntityTameZombie;
import io.github.tehstoneman.zombiebrains.item.ItemBrainsSpawnEgg;
import io.github.tehstoneman.zombiebrains.network.WaypointMessage;
import io.github.tehstoneman.zombiebrains.proxies.CommonProxy;
import io.github.tehstoneman.zombiebrains.tileentity.TileEntityLapisTorch;
import io.github.tehstoneman.zombiebrains.tileentity.TileEntityZombieStone;
import io.github.tehstoneman.zombiebrains.util.ConfigManager;
import io.github.tehstoneman.zombiebrains.util.EventManager;
import io.github.tehstoneman.zombiebrains.waypoints.WaypointManager;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(	modid						= ModInfo.MODID,
		name						= ModInfo.NAME,
		version						= ModInfo.VERSION,
		dependencies				= ModInfo.DEPENDENCIES,
		acceptedMinecraftVersions	= ModInfo.MINECRAFT )
public class ZombieBrains
{
	public static ModMetadata			modMetadata;

	@Instance( value = ModInfo.MODID )
	public static ZombieBrains			instance;

	// Define proxies
	@SidedProxy( clientSide = ModInfo.PROXY_LOCATION + "ClientProxy", serverSide = ModInfo.PROXY_LOCATION + "CommonProxy" )
	public static CommonProxy			proxy;

	public static WaypointManager		waypointManager	= new WaypointManager();
	public static SimpleNetworkWrapper	network;

	@EventHandler
	public void serverLoad( FMLServerStartingEvent event )
	{
		event.registerServerCommand( new DebugCommand() );
	}

	@EventHandler
	public void preInitialize( FMLPreInitializationEvent event )
	{
		proxy.preInit();

		// Initialise network messaging
		byte discriminator = 1;
		network = NetworkRegistry.INSTANCE.newSimpleChannel( ModInfo.MODID );
		network.registerMessage( WaypointMessage.Handler.class, WaypointMessage.class, discriminator++, Side.CLIENT );

		// Initialise custom renderers
		proxy.initRenderers();

		// Load configuration
		ConfigManager.init( event.getSuggestedConfigurationFile() );
	}

	@EventHandler
	public void initialize( FMLInitializationEvent event )
	{
		EntityRegistry.registerModEntity( EntityTameZombie.class, "tame_zombie", 0, this, 80, 1, true );
		final Block blockLapisTorch = new BlockLapisTorch().setHardness( 0.0F ).setLightLevel( 0.9375F ).setStepSound( Block.soundTypeWood )
				.setBlockName( "torch_lapis" ).setBlockTextureName( ModInfo.MODID + ":torch_lapis" );
		final Block blockZombieStone = new BlockZombieStone().setHardness( 1.5F ).setResistance( 10.0F ).setStepSound( Block.soundTypeStone )
				.setBlockName( "zombie_stone" ).setBlockTextureName( ModInfo.MODID + ":zombiestone" );
		final Block blockZombieStoneLit = new BlockZombieStone().setHardness( 1.5F ).setResistance( 10.0F ).setStepSound( Block.soundTypeStone )
				.setBlockName( "zombie_stone_lit" ).setBlockTextureName( ModInfo.MODID + ":zombiestone" ).setLightLevel( 0.25F );
		final Item itemSpawnEgg = new ItemBrainsSpawnEgg().setUnlocalizedName( "zombie_brains_spawn_egg" ).setTextureName( "spawn_egg" );
		GameRegistry.registerBlock( blockLapisTorch, "block_lapis_torch" );
		GameRegistry.registerBlock( blockZombieStone, "block_zombie_stone" );
		GameRegistry.registerItem( itemSpawnEgg, "spawn_egg_zombie_brains" );

		GameRegistry.addShapedRecipe( new ItemStack( blockLapisTorch ), new Object[] { "L", "T", Character.valueOf( 'L' ),
				new ItemStack( Items.dye, 1, 4 ), Character.valueOf( 'T' ), Blocks.torch } );
		GameRegistry.addShapelessRecipe( new ItemStack( blockZombieStone ),
				new Object[] {Blocks.sand, Items.rotten_flesh, new ItemStack( Items.dye, 1, 4 ) } );
		
		GameRegistry.registerTileEntity( TileEntityLapisTorch.class, "tile_entity_lapis_torch" );
		GameRegistry.registerTileEntity( TileEntityZombieStone.class, "tile_entity_zombie_stone" );
	}

	@EventHandler
	public void postInitialization( FMLPostInitializationEvent event )
	{
		// Register event handler
		MinecraftForge.EVENT_BUS.register( new EventManager() );
	}

}
