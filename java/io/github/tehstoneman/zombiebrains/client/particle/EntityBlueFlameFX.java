package io.github.tehstoneman.zombiebrains.client.particle;

import io.github.tehstoneman.zombiebrains.ModInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly( Side.CLIENT )
public class EntityBlueFlameFX extends ZombieEntityFX
{
	/** the scale of the flame FX */
	private final float	flameScale;

	public EntityBlueFlameFX( World p_i1209_1_, double p_i1209_2_, double p_i1209_4_, double p_i1209_6_, double p_i1209_8_, double p_i1209_10_,
			double p_i1209_12_ )
	{
		super( p_i1209_1_, p_i1209_2_, p_i1209_4_, p_i1209_6_, p_i1209_8_, p_i1209_10_, p_i1209_12_ );
		motionX = motionX * 0.009999999776482582D + p_i1209_8_;
		motionY = motionY * 0.009999999776482582D + p_i1209_10_;
		motionZ = motionZ * 0.009999999776482582D + p_i1209_12_;
		double d6 = p_i1209_2_ + ( rand.nextFloat() - rand.nextFloat() ) * 0.05F;
		d6 = p_i1209_4_ + ( rand.nextFloat() - rand.nextFloat() ) * 0.05F;
		d6 = p_i1209_6_ + ( rand.nextFloat() - rand.nextFloat() ) * 0.05F;
		flameScale = particleScale;
		particleRed = particleGreen = particleBlue = 1.0F;
		particleMaxAge = (int)( 8.0D / ( Math.random() * 0.8D + 0.2D ) ) + 4;
		noClip = true;
        this.particleTextureIndexX = 0;
        this.particleTextureIndexY = 0;
	}

	@Override
	public void renderParticle( Tessellator p_70539_1_, float p_70539_2_, float p_70539_3_, float p_70539_4_, float p_70539_5_, float p_70539_6_,
			float p_70539_7_ )
	{
		final float f6 = ( particleAge + p_70539_2_ ) / particleMaxAge;
		particleScale = flameScale * ( 1.0F - f6 * f6 * 0.5F );
		//Minecraft.getMinecraft().getTextureManager().bindTexture( new ResourceLocation( ModInfo.MODID + ":textures/particle/particles.png" ) );
		super.renderParticle( p_70539_1_, p_70539_2_, p_70539_3_, p_70539_4_, p_70539_5_, p_70539_6_, p_70539_7_ );
	}

	@Override
	public int getBrightnessForRender( float p_70070_1_ )
	{
		float f1 = ( particleAge + p_70070_1_ ) / particleMaxAge;

		if( f1 < 0.0F )
			f1 = 0.0F;

		if( f1 > 1.0F )
			f1 = 1.0F;

		final int i = super.getBrightnessForRender( p_70070_1_ );
		int j = i & 255;
		final int k = i >> 16 & 255;
		j += (int)( f1 * 15.0F * 16.0F );

		if( j > 240 )
			j = 240;

		return j | k << 16;
	}

	/**
	 * Gets how bright this entity is.
	 */
	@Override
	public float getBrightness( float p_70013_1_ )
	{
		float f1 = ( particleAge + p_70013_1_ ) / particleMaxAge;

		if( f1 < 0.0F )
			f1 = 0.0F;

		if( f1 > 1.0F )
			f1 = 1.0F;

		final float f2 = super.getBrightness( p_70013_1_ );
		return f2 * f1 + ( 1.0F - f1 );
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate()
	{
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;

		if( particleAge++ >= particleMaxAge )
			setDead();

		moveEntity( motionX, motionY, motionZ );
		motionX *= 0.9599999785423279D;
		motionY *= 0.9599999785423279D;
		motionZ *= 0.9599999785423279D;

		if( onGround )
		{
			motionX *= 0.699999988079071D;
			motionZ *= 0.699999988079071D;
		}
	}

	@Override
	public int getFXLayer()
	{
		return 3;
	}
}
