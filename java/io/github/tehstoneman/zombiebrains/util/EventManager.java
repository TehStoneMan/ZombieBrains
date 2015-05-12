package io.github.tehstoneman.zombiebrains.util;

import io.github.tehstoneman.zombiebrains.ZombieBrains;
import io.github.tehstoneman.zombiebrains.client.renderer.ZombieParticleRenderer;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EventManager
{
	private static Minecraft	mc	= Minecraft.getMinecraft();

	@SubscribeEvent
	public void onRenderWorldLastEvent( RenderWorldLastEvent event )
	{
		//ZombieBrains.zombieParticleRenderer.renderParticles( mc.renderViewEntity, event.partialTicks );
	}

}
