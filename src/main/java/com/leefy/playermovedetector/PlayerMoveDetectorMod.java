package com.leefy.playermovedetector;

import com.leefy.playermovedetector.config.PlayerMoveDetectorConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;

public class PlayerMoveDetectorMod implements ModInitializer {
    @Override
    public void onInitialize() {
        // Register the config
        AutoConfig.register(PlayerMoveDetectorConfig.class, GsonConfigSerializer::new);
    }
}
