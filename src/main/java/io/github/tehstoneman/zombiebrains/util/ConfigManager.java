package io.github.tehstoneman.zombiebrains.util;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class ConfigManager
{
	public static void init( File configFile )
	{
		final Configuration config = new Configuration( configFile );

		config.load();

		// General settings
		Settings.debug = config.get(config.CATEGORY_GENERAL, "debug", false).getBoolean(false);

		config.save();
	}
}
