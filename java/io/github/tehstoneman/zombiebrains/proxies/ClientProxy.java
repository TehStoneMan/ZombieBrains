package io.github.tehstoneman.zombiebrains.proxies;

import net.minecraft.client.renderer.entity.RenderZombie;
import io.github.tehstoneman.zombiebrains.client.renderer.entity.RenderTameZombie;
import io.github.tehstoneman.zombiebrains.entity.monster.EntityTameZombie;
import cpw.mods.fml.client.registry.RenderingRegistry;


public class ClientProxy extends CommonProxy
{
	@Override
	public void initRenderers()
	{
		RenderingRegistry.registerEntityRenderingHandler( EntityTameZombie.class, new RenderTameZombie() );
	}
}
