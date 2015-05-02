package io.github.tehstoneman.zombiebrains.client.particle;

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
		// TODO Auto-generated constructor stub
	}

	public ZombieEntityFX( World world, double x, double y, double z, double velX, double velY, double velZ )
	{
		super( world, x, y, z, velX, velY, velZ );
		// TODO Auto-generated constructor stub
	}

	@Override
	public void renderParticle( Tessellator tess, float par2, float par3, float par4, float par5, float par6, float par7 )
	{
		Minecraft.getMinecraft().getTextureManager().bindTexture( new ResourceLocation( ModInfo.MODID + ":textures/particle/particles.png" ) );
		//Minecraft.getMinecraft().getTextureManager().bindTexture( new ResourceLocation( "textures/particle/particles.png" ) );
		//super.renderParticle( tess, par2, par3, par4, par5, par6, par7 );
		float f6 = (float)this.particleTextureIndexX / 16.0F;
        float f7 = f6 + 0.0624375F;
        float f8 = (float)this.particleTextureIndexY / 16.0F;
        float f9 = f8 + 0.0624375F;
        float f10 = 0.1F * this.particleScale;

        float f11 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)par2 - interpPosX);
        float f12 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)par2 - interpPosY);
        float f13 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)par2 - interpPosZ);
        tess.setColorRGBA_F(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha);
        tess.addVertexWithUV((double)(f11 - par3 * f10 - par6 * f10), (double)(f12 - par4 * f10), (double)(f13 - par5 * f10 - par7 * f10), (double)f7, (double)f9);
        tess.addVertexWithUV((double)(f11 - par3 * f10 + par6 * f10), (double)(f12 + par4 * f10), (double)(f13 - par5 * f10 + par7 * f10), (double)f7, (double)f8);
        tess.addVertexWithUV((double)(f11 + par3 * f10 + par6 * f10), (double)(f12 + par4 * f10), (double)(f13 + par5 * f10 + par7 * f10), (double)f6, (double)f8);
        tess.addVertexWithUV((double)(f11 + par3 * f10 - par6 * f10), (double)(f12 - par4 * f10), (double)(f13 + par5 * f10 - par7 * f10), (double)f6, (double)f9);
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
		return 3;
	}
}
