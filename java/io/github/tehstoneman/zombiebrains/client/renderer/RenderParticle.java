package io.github.tehstoneman.zombiebrains.client.renderer;

import io.github.tehstoneman.zombiebrains.client.particle.EntityBlueFlameFX;

import java.util.concurrent.Callable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.world.World;

public class RenderParticle
{
	private static Minecraft		mc				= Minecraft.getMinecraft();
	private static World			theWorld		= mc.theWorld;
	private static TextureManager	renderEngine	= mc.getTextureManager();

	/**
	 * Spawns a particle. Arg: particleType, x, y, z, velX, velY, velZ
	 */
	public static void spawnParticle( String particleType, double x, double y, double z, double velX, double velY, double velZ )
	{
		try
		{
			doSpawnParticle( particleType, x, y, z, velX, velY, velZ );
		}
		catch( final Throwable throwable )
		{
			final CrashReport crashreport = CrashReport.makeCrashReport( throwable, "Exception while adding particle" );
			final CrashReportCategory crashreportcategory = crashreport.makeCategory( "Particle being added" );
			crashreportcategory.addCrashSection( "Name", particleType );
			crashreportcategory.addCrashSectionCallable( "Position", new Callable()
			{
				@Override
				public String call()
				{
					return CrashReportCategory.func_85074_a( x, y, z );
				}
			} );
			throw new ReportedException( crashreport );
		}
	}

	/**
	 * Spawns a particle. Arg: particleType, x, y, z, velX, velY, velZ
	 */
	public static EntityFX doSpawnParticle( String particleType, double x, double y, double z, double velX, double velY, double velZ )
	{
		if( mc != null && mc.renderViewEntity != null && mc.effectRenderer != null )
		{
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
