package com.leefy.playermovedetector.config;

import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class PlayerMoveDetectorConfigManager {
    private static PlayerMoveDetectorConfig config;
    
    public static void init() {
        config = AutoConfig.getConfigHolder(PlayerMoveDetectorConfig.class).getConfig();
    }
    
    public static PlayerMoveDetectorConfig getConfig() {
        return config;
    }
}