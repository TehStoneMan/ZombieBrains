package io.github.tehstoneman.zombiebrains.client.particle;

import org.lwjgl.opengl.GL11;

import io.github.tehstoneman.zombiebrains.ModInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ZombieEntityFX extends EntityFX
{
	protected ZombieEntityFX( World world, double x, double y, double z )
	{
		super( world, x, y, z );
	}

	public ZombieEntityFX( World world, double x, double y, double z, double velX, double velY, double velZ )
	{
		super( world, x, y, z, velX, velY, velZ );
	}

	@Override
	public void renderParticle( Tessellator tessellator, float partialTickTime, float par3, float par4, float par5, float par6, float par7 )
	{
		// Call tessellator to draw previously rendered particles
        tessellator.draw();
        
        // Change to custom particle texture
		Minecraft.getMinecraft().getTextureManager().bindTexture( new ResourceLocation( ModInfo.MODID + ":textures/particle/particles.png" ) );

		// Prepare tessellator for this particle
        tessellator.startDrawingQuads();
        
        // Set particle brightness for this render pass
        tessellator.setBrightness(getBrightnessForRender(partialTickTime));

        // Call original function to do the actual rendering
        super.renderParticle( tessellator, partialTickTime, par3, par4, par5, par6, par7 );

        // Draw our rendered particle
        tessellator.draw();

        // Prepare tessellator for next particle in queue
        tessellator.startDrawingQuads();
        
        // Restore default texture
		Minecraft.getMinecraft().getTextureManager().bindTexture( new ResourceLocation( "textures/particle/particles.png" ) );
	}

	/**
	 * Public method to set private field particleTextureIndex.
	 */
	@Override
	public void setParticleTextureIndex( int index )
	{
		particleTextureIndexX = index % 16;
		particleTextureIndexY = index / 16;
	}

	@Override
	public int getFXLayer()
	{
		return 0;
	}
}
