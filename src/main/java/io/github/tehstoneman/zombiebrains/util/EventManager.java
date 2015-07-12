package io.github.tehstoneman.zombiebrains.util;

import io.github.tehstoneman.zombiebrains.ZombieBrains;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.ChunkEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EventManager
{
	private static Minecraft	mc	= Minecraft.getMinecraft();

	@SubscribeEvent
	public void onRenderWorldLastEvent( RenderWorldLastEvent event )
	{
		if( Settings.debug )
		{
			final EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
			final double x = player.lastTickPosX + ( player.posX - player.lastTickPosX ) * event.partialTicks;
			final double y = player.lastTickPosY + ( player.posY - player.lastTickPosY ) * event.partialTicks;
			final double z = player.lastTickPosZ + ( player.posZ - player.lastTickPosZ ) * event.partialTicks;

			ZombieBrains.waypointManager.draw( mc, x, y, z );
		}
	}

	@SubscribeEvent
	public void onChunkLoad( ChunkEvent.Load event )
	{
		final World world = event.world;
		ZombieBrains.waypointManager.loadOrCreateData( world );
	}
}
