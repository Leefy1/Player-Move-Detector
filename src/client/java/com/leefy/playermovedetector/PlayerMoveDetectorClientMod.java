package com.leefy.playermovedetector;

import com.leefy.playermovedetector.config.PlayerMoveDetectorConfigManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class PlayerMoveDetectorClientMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Initialize the config manager
        PlayerMoveDetectorConfigManager.init();
        
        PlayerMoveDetector playerMoveDetector = new PlayerMoveDetector();
        playerMoveDetector.onInitializeClient();
    }
}
