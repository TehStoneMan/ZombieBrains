package io.github.tehstoneman.zombiebrains.client.renderer.entity;

import io.github.tehstoneman.zombiebrains.ModInfo;
import io.github.tehstoneman.zombiebrains.entity.monster.EntityTameZombie;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.model.ModelZombieVillager;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly( Side.CLIENT )
public class RenderTameZombie extends RenderBiped
{
	private static final ResourceLocation	zombiePigmanTextures	= new ResourceLocation( "textures/entity/zombie_pigman.png" );
	private static final ResourceLocation	zombieTextures			= new ResourceLocation( ModInfo.MODID + ":textures/entity/tamezombie.png" );
	private static final ResourceLocation	zombieVillagerTextures	= new ResourceLocation( ModInfo.MODID + ":textures/entity/tamezombie.png" );
	private final ModelBiped				zombieModel;
	private ModelZombieVillager				zombieVillagerModel;
	protected ModelBiped					field_82437_k;
	protected ModelBiped					field_82435_l;
	protected ModelBiped					field_82436_m;
	protected ModelBiped					field_82433_n;
	private int								field_82431_q			= 1;

	public RenderTameZombie()
	{
		super( new ModelZombie(), 0.5F, 1.0F );
		zombieModel = modelBipedMain;
		zombieVillagerModel = new ModelZombieVillager();
	}

	@Override
	protected void func_82421_b()
	{
		field_82423_g = new ModelZombie( 1.0F, true );
		field_82425_h = new ModelZombie( 0.5F, true );
		field_82437_k = field_82423_g;
		field_82435_l = field_82425_h;
		field_82436_m = new ModelZombieVillager( 1.0F, 0.0F, true );
		field_82433_n = new ModelZombieVillager( 0.5F, 0.0F, true );
	}

	/**
	 * Queries whether should render the specified pass or not.
	 */
	protected int shouldRenderPass( EntityTameZombie entity, int pass, float tick )
	{
		setModel( entity );
		return super.shouldRenderPass( entity, pass, tick );
	}

	/**
	 * Actually renders the given argument. This is a synthetic bridge method,
	 * always casting down its argument and then handing it off to a worker
	 * function which does the actual work. In all probabilty, the class Render
	 * is generic (Render<T extends Entity) and this method has signature public
	 * void func_76986_a(T entity, double d, double d1, double d2, float f,
	 * float f1). But JAD is pre 1.5 so doesn't do that.
	 */
	public void doRender( EntityTameZombie entity, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_ )
	{
		setModel( entity );
		super.doRender( entity, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_ );
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called
	 * unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture( EntityTameZombie entity )
	{
		//return entity instanceof EntityPigZombie ? zombiePigmanTextures : entity.isVillager() ? zombieVillagerTextures : zombieTextures;
		return entity.isVillager() ? zombieVillagerTextures : zombieTextures;
	}

	protected void renderEquippedItems( EntityTameZombie entity, float p_77029_2_ )
	{
		setModel( entity );
		super.renderEquippedItems( entity, p_77029_2_ );
	}

	private void setModel( EntityTameZombie entity )
	{
		if( entity.isVillager() )
		{
			if( field_82431_q != zombieVillagerModel.func_82897_a() )
			{
				zombieVillagerModel = new ModelZombieVillager();
				field_82431_q = zombieVillagerModel.func_82897_a();
				field_82436_m = new ModelZombieVillager( 1.0F, 0.0F, true );
				field_82433_n = new ModelZombieVillager( 0.5F, 0.0F, true );
			}

			mainModel = zombieVillagerModel;
			field_82423_g = field_82436_m;
			field_82425_h = field_82433_n;
		}
		else
		{
			mainModel = zombieModel;
			field_82423_g = field_82437_k;
			field_82425_h = field_82435_l;
		}

		modelBipedMain = (ModelBiped)mainModel;
	}

	protected void rotateCorpse( EntityTameZombie entity, float p_77043_2_, float p_77043_3_, float p_77043_4_ )
	{
		if( entity.isConverting() )
			p_77043_3_ += (float)( Math.cos( entity.ticksExisted * 3.25D ) * Math.PI * 0.25D );

		super.rotateCorpse( entity, p_77043_2_, p_77043_3_, p_77043_4_ );
	}

	@Override
	protected void renderEquippedItems( EntityLiving entity, float p_77029_2_ )
	{
		this.renderEquippedItems( (EntityTameZombie)entity, p_77029_2_ );
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called
	 * unless you call Render.bindEntityTexture.
	 */
	@Override
	 protected ResourceLocation getEntityTexture( EntityLiving entity )
	{
		return this.getEntityTexture( (EntityTameZombie)entity );
	}

	/**
	 * Actually renders the given argument. This is a synthetic bridge method,
	 * always casting down its argument and then handing it off to a worker
	 * function which does the actual work. In all probabilty, the class Render
	 * is generic (Render<T extends Entity) and this method has signature public
	 * void func_76986_a(T entity, double d, double d1, double d2, float f,
	 * float f1). But JAD is pre 1.5 so doesn't do that.
	 */
	@Override
	 public void doRender( EntityLiving entity, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_ )
	{
		this.doRender( entity, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_ );
	}

	/**
	 * Queries whether should render the specified pass or not.
	 */
	@Override
	 protected int shouldRenderPass( EntityLiving p_77032_1_, int p_77032_2_, float p_77032_3_ )
	{
		return this.shouldRenderPass( (EntityTameZombie)p_77032_1_, p_77032_2_, p_77032_3_ );
	}

	/**
	 * Queries whether should render the specified pass or not.
	 */
	@Override
	 protected int shouldRenderPass( EntityLivingBase p_77032_1_, int p_77032_2_, float p_77032_3_ )
	{
		return this.shouldRenderPass( (EntityTameZombie)p_77032_1_, p_77032_2_, p_77032_3_ );
	}

	@Override
	 protected void renderEquippedItems( EntityLivingBase p_77029_1_, float p_77029_2_ )
	{
		this.renderEquippedItems( (EntityTameZombie)p_77029_1_, p_77029_2_ );
	}

	@Override
	 protected void rotateCorpse( EntityLivingBase p_77043_1_, float p_77043_2_, float p_77043_3_, float p_77043_4_ )
	{
		this.rotateCorpse( (EntityTameZombie)p_77043_1_, p_77043_2_, p_77043_3_, p_77043_4_ );
	}

	/**
	 * Actually renders the given argument. This is a synthetic bridge method,
	 * always casting down its argument and then handing it off to a worker
	 * function which does the actual work. In all probabilty, the class Render
	 * is generic (Render<T extends Entity) and this method has signature public
	 * void func_76986_a(T entity, double d, double d1, double d2, float f,
	 * float f1). But JAD is pre 1.5 so doesn't do that.
	 */
	@Override
	 public void doRender( EntityLivingBase p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_ )
	{
		this.doRender( (EntityTameZombie)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_ );
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called
	 * unless you call Render.bindEntityTexture.
	 */
	@Override
	 protected ResourceLocation getEntityTexture( Entity entity )
	{
		return this.getEntityTexture( (EntityTameZombie)entity );
	}

	/**
	 * Actually renders the given argument. This is a synthetic bridge method,
	 * always casting down its argument and then handing it off to a worker
	 * function which does the actual work. In all probabilty, the class Render
	 * is generic (Render<T extends Entity) and this method has signature public
	 * void func_76986_a(T entity, double d, double d1, double d2, float f,
	 * float f1). But JAD is pre 1.5 so doesn't do that.
	 */
	@Override
	 public void doRender( Entity entity, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_ )
	{
		this.doRender( (EntityTameZombie)entity, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_ );
	}
}