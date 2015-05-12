package io.github.tehstoneman.zombiebrains.client.renderer;

import io.github.tehstoneman.zombiebrains.ModInfo;
import io.github.tehstoneman.zombiebrains.client.particle.EntityBlueFlameFX;
import io.github.tehstoneman.zombiebrains.client.particle.ZombieEntityFX;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

public class ZombieParticleRenderer
{
	private static Minecraft				mc					= Minecraft.getMinecraft();

	/**
	 * Spawns a particle. Arg: particleType, x, y, z, velX, velY, velZ
	 */
	public static EntityFX spawnParticle( String particleType, double x, double y, double z, double velX, double velY, double velZ )
	{
		if( mc != null && mc.renderViewEntity != null )
		{
			final World theWorld = mc.theWorld;
			int particleLevel = mc.gameSettings.particleSetting;

			if( particleLevel == 1 && theWorld.rand.nextInt( 3 ) == 0 )
				particleLevel = 2;

			final double viewX = mc.renderViewEntity.posX - x;
			final double viewY = mc.renderViewEntity.posY - y;
			final double viewZ = mc.renderViewEntity.posZ - z;
			EntityFX entityfx = null;

			final double distance = 16.0D;

			if( viewX * viewX + viewY * viewY + viewZ * viewZ > distance * distance )
				return null;
			else
				if( particleLevel > 1 )
					return null;
				else
				{
					if( particleType.equals( "blueFlame" ) )
						entityfx = new EntityBlueFlameFX( theWorld, x, y, z, velX, velY, velZ );

					if( entityfx != null )
						mc.effectRenderer.addEffect( entityfx );

					return entityfx;
				}
		}
		else
			return null;
	}
}
