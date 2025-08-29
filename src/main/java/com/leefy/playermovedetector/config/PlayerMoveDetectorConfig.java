package com.leefy.playermovedetector.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "player_move_detector")
public class PlayerMoveDetectorConfig implements ConfigData {
    
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
    public int markerSize = 40;
    
    @ConfigEntry.Gui.Tooltip
    public double markerHeight = 0.85d;
    
    @ConfigEntry.Gui.Tooltip
    public boolean hideRedMarkers = false;
    
    @ConfigEntry.Gui.Tooltip
    public boolean hideGreenMarkers = false;
    
    // Default constructor
    public PlayerMoveDetectorConfig() {
    }
    
    // Helper method to convert int percentage to float size
    public float getMarkerSize() {
        return markerSize / 100.0f;
    }
}