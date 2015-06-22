package io.github.tehstoneman.zombiebrains.util;

import io.github.tehstoneman.zombiebrains.ZombieBrains;
import io.github.tehstoneman.zombiebrains.client.renderer.ZombieParticleRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EventManager
{
	private static Minecraft	mc	= Minecraft.getMinecraft();

	@SubscribeEvent
	public void onRenderWorldLastEvent( RenderWorldLastEvent event )
	{
		if( Settings.debug )
		{			
			EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
			double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.partialTicks;
			double y = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.partialTicks;
			double z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.partialTicks;

			ZombieBrains.waypointManager.draw( mc, x, y, z);
		}
	}

}
