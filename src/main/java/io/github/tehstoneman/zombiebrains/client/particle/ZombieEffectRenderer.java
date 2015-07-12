package io.github.tehstoneman.zombiebrains.client.particle;

import io.github.tehstoneman.zombiebrains.ModInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly( Side.CLIENT )
public class ZombieEffectRenderer
{
	private static final ResourceLocation	particleTextures	= new ResourceLocation( ModInfo.MODID + ":textures/particle/particles.png" );
	/** Reference to the World object. */
	protected World							worldObj;
	private final List[]					fxLayers			= new List[4];
	private final TextureManager			renderer;
	/** RNG. */
	//private final Random					rand				= new Random();

	public ZombieEffectRenderer( World world, TextureManager manager )
	{
		if( world != null )
			worldObj = world;

		renderer = manager;

		for( int i = 0; i < 4; ++i )
			fxLayers[i] = new ArrayList();
	}

	public void addEffect( EntityFX entity )
	{
		final int i = entity.getFXLayer();

		if( fxLayers[i].size() >= 4000 )
			fxLayers[i].remove( 0 );

		fxLayers[i].add( entity );
	}

	public void updateEffects()
	{
		for( int k = 0; k < 4; ++k )
		{
			final int i = k;

			for( int j = 0; j < fxLayers[i].size(); ++j )
			{
				final EntityFX entityfx = (EntityFX)fxLayers[i].get( j );

				try
				{
					if( entityfx != null )
						entityfx.onUpdate();
				}
				catch( final Throwable throwable )
				{
					final CrashReport crashreport = CrashReport.makeCrashReport( throwable, "Ticking Particle" );
					final CrashReportCategory crashreportcategory = crashreport.makeCategory( "Particle being ticked" );
					crashreportcategory.addCrashSectionCallable( "Particle", new Callable()
					{
						@Override
						public String call()
						{
							return entityfx.toString();
						}
					} );
					crashreportcategory.addCrashSectionCallable( "Particle Type", new Callable()
					{
						@Override
						public String call()
						{
							return i == 0 ? "MISC_TEXTURE" : i == 1 ? "TERRAIN_TEXTURE" : i == 2 ? "ITEM_TEXTURE"
									: i == 3 ? "ENTITY_PARTICLE_TEXTURE" : "Unknown - " + i;
						}
					} );
					throw new ReportedException( crashreport );
				}

				if( entityfx == null || entityfx.isDead )
					fxLayers[i].remove( j-- );
			}
		}
	}

	/**
	 * Renders all current particles. Args player, partialTickTime
	 */
	public void renderParticles( Entity player, float partialTickTime )
	{
		final float f1 = ActiveRenderInfo.rotationX;
		final float f2 = ActiveRenderInfo.rotationZ;
		final float f3 = ActiveRenderInfo.rotationYZ;
		final float f4 = ActiveRenderInfo.rotationXY;
		final float f5 = ActiveRenderInfo.rotationXZ;
		EntityFX.interpPosX = player.lastTickPosX + ( player.posX - player.lastTickPosX ) * partialTickTime;
		EntityFX.interpPosY = player.lastTickPosY + ( player.posY - player.lastTickPosY ) * partialTickTime;
		EntityFX.interpPosZ = player.lastTickPosZ + ( player.posZ - player.lastTickPosZ ) * partialTickTime;

		for( int k = 0; k < 3; ++k )
		{
			final int i = k;

			if( !fxLayers[i].isEmpty() )
			{
				switch( i )
				{
				case 0:
				default:
					renderer.bindTexture( particleTextures );
					break;
				case 1:
					renderer.bindTexture( TextureMap.locationBlocksTexture );
					break;
				case 2:
					renderer.bindTexture( TextureMap.locationItemsTexture );
				}

				GL11.glColor4f( 1.0F, 1.0F, 1.0F, 1.0F );
				GL11.glDepthMask( false );
				GL11.glEnable( GL11.GL_BLEND );
				GL11.glBlendFunc( GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA );
				GL11.glAlphaFunc( GL11.GL_GREATER, 0.003921569F );
				final Tessellator tessellator = Tessellator.instance;
				tessellator.startDrawingQuads();

				for( int j = 0; j < fxLayers[i].size(); ++j )
				{
					final EntityFX entityfx = (EntityFX)fxLayers[i].get( j );
					if( entityfx == null )
						continue;
					tessellator.setBrightness( entityfx.getBrightnessForRender( partialTickTime ) );

					try
					{
						entityfx.renderParticle( tessellator, partialTickTime, f1, f5, f2, f3, f4 );
					}
					catch( final Throwable throwable )
					{
						final CrashReport crashreport = CrashReport.makeCrashReport( throwable, "Rendering Particle" );
						final CrashReportCategory crashreportcategory = crashreport.makeCategory( "Particle being rendered" );
						crashreportcategory.addCrashSectionCallable( "Particle", new Callable()
						{
							private static final String	__OBFID	= "CL_00000918";

							@Override
							public String call()
							{
								return entityfx.toString();
							}
						} );
						crashreportcategory.addCrashSectionCallable( "Particle Type", new Callable()
						{
							private static final String	__OBFID	= "CL_00000919";

							@Override
							public String call()
							{
								return i == 0 ? "MISC_TEXTURE" : i == 1 ? "TERRAIN_TEXTURE" : i == 2 ? "ITEM_TEXTURE"
										: i == 3 ? "ENTITY_PARTICLE_TEXTURE" : "Unknown - " + i;
							}
						} );
						throw new ReportedException( crashreport );
					}
				}

				tessellator.draw();
				GL11.glDisable( GL11.GL_BLEND );
				GL11.glDepthMask( true );
				GL11.glAlphaFunc( GL11.GL_GREATER, 0.1F );
			}
		}
	}

	public void renderLitParticles( Entity player, float partialTickTime )
	{
		final float f1 = 0.017453292F;
		final float f2 = MathHelper.cos( player.rotationYaw * 0.017453292F );
		final float f3 = MathHelper.sin( player.rotationYaw * 0.017453292F );
		final float f4 = -f3 * MathHelper.sin( player.rotationPitch * 0.017453292F );
		final float f5 = f2 * MathHelper.sin( player.rotationPitch * 0.017453292F );
		final float f6 = MathHelper.cos( player.rotationPitch * 0.017453292F );
		final byte b0 = 3;
		final List list = fxLayers[b0];

		if( !list.isEmpty() )
		{
			final Tessellator tessellator = Tessellator.instance;

			for( int i = 0; i < list.size(); ++i )
			{
				final EntityFX entityfx = (EntityFX)list.get( i );
				if( entityfx == null )
					continue;
				tessellator.setBrightness( entityfx.getBrightnessForRender( partialTickTime ) );
				entityfx.renderParticle( tessellator, partialTickTime, f2, f6, f3, f4, f5 );
			}
		}
	}

	public void clearEffects( World world )
	{
		worldObj = world;

		for( int i = 0; i < 4; ++i )
			fxLayers[i].clear();
	}

	public String getStatistics()
	{
		return "" + ( fxLayers[0].size() + fxLayers[1].size() + fxLayers[2].size() );
	}
}