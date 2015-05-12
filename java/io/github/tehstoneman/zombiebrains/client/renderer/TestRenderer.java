package io.github.tehstoneman.zombiebrains.client.renderer;

import io.github.tehstoneman.zombiebrains.ModInfo;

import java.util.List;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class TestRenderer
{
	private static final ResourceLocation	particleTextures	= new ResourceLocation( ModInfo.MODID + ":textures/particle/particles.png" );
	private final List[]					fxLayers			= new List[4];
	/** RNG. */
	private final Random					rand				= new Random();

	private static Minecraft				mc					= Minecraft.getMinecraft();
	private static World					theWorld			= mc.theWorld;
	private static TextureManager			renderEngine		= mc.getTextureManager();

	@SubscribeEvent
	public void onRenderWorldLastEvent( RenderWorldLastEvent event )
	{
		renderParticles( mc.renderViewEntity, event.partialTicks );
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
					renderEngine.bindTexture( particleTextures );
					break;
				case 1:
					renderEngine.bindTexture( TextureMap.locationBlocksTexture );
					break;
				case 2:
					renderEngine.bindTexture( TextureMap.locationItemsTexture );
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

					entityfx.renderParticle( tessellator, partialTickTime, f1, f5, f2, f3, f4 );
				}

				tessellator.draw();
				GL11.glDisable( GL11.GL_BLEND );
				GL11.glDepthMask( true );
				GL11.glAlphaFunc( GL11.GL_GREATER, 0.1F );
			}
		}
	}
}
