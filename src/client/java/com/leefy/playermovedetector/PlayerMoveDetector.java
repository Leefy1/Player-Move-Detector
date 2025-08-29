package com.leefy.playermovedetector;

import com.leefy.playermovedetector.config.PlayerMoveDetectorConfig;
import com.leefy.playermovedetector.config.PlayerMoveDetectorConfigManager;
import com.leefy.playermovedetector.render.MarkerRenderer;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.text.Text;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;

@Environment(EnvType.CLIENT)
public class PlayerMoveDetector {
    private static KeyBinding toggleKey;

    private static boolean enabled = false;

    private static final Map<UUID, Vec3d> startPos = new HashMap<>();

    private static final Map<UUID, Boolean> hasMoved = new HashMap<>();

    private final MinecraftClient mc = MinecraftClient.getInstance();

    public void onInitializeClient() {
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.player_move_detector.toggle", InputUtil.Type.KEYSYM, 88, "category.player_move_detector"));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKey.wasPressed()) {
                if (!enabled) {
                    beginRound();
                    continue;
                }
                endRound();
            }
            if (enabled && this.mc.world != null)
                for (PlayerEntity p : this.mc.world.getPlayers()) {
                    if (p == this.mc.player || p.isSpectator())
                        continue;
                    UUID id = p.getUuid();
                    if (startPos.containsKey(id) && !((Boolean)hasMoved.getOrDefault(id, Boolean.valueOf(false))).booleanValue()) {
                        Vec3d start = startPos.get(id);
                        Vec3d now = p.getPos();
                        double EPS = 1.0E-4D;
                        boolean moved = (Math.abs(now.x - start.x) > 1.0E-4D || Math.abs(now.y - start.y) > 1.0E-4D || Math.abs(now.z - start.z) > 1.0E-4D);
                        if (moved)
                            hasMoved.put(id, Boolean.valueOf(true));
                        continue;
                    }
                    if (!startPos.containsKey(id)) {
                        startPos.put(id, p.getPos());
                        hasMoved.put(id, Boolean.valueOf(false));
                    }
                }
        });
        WorldRenderEvents.AFTER_ENTITIES.register(context -> {
            if (!enabled || this.mc.world == null)
                return;
                
            // Get config values
            PlayerMoveDetectorConfig config = PlayerMoveDetectorConfigManager.getConfig();
            
            for (PlayerEntity p : this.mc.world.getPlayers()) {
                if (p == this.mc.player || p.isSpectator())
                    continue;
                UUID id = p.getUuid();
                boolean moved = ((Boolean)hasMoved.getOrDefault(id, Boolean.valueOf(false))).booleanValue();
                
                // Check if we should hide this marker
                if (moved && config.hideRedMarkers) continue;
                if (!moved && config.hideGreenMarkers) continue;
                
                double x = p.getX();
                double y = p.getY() + p.getHeight() + config.markerHeight;
                double z = p.getZ();
                MarkerRenderer.renderBillboardMarker(context.matrixStack(), context.consumers(), x, y, z, moved, config.getMarkerSize(), this.mc.gameRenderer.getCamera());
            }
        });
    }

    private void beginRound() {
        enabled = true;
        startPos.clear();
        hasMoved.clear();
        if (this.mc.world != null)
            for (PlayerEntity p : this.mc.world.getPlayers()) {
                if (p == this.mc.player || p.isSpectator())
                    continue;
                startPos.put(p.getUuid(), p.getPos());
                hasMoved.put(p.getUuid(), Boolean.valueOf(false));
            }
        if (this.mc.player != null)
            this.mc.player.sendMessage(Text.of("Player Move Detector: ON"), true);
    }

    private void endRound() {
        enabled = false;
        startPos.clear();
        hasMoved.clear();
        if (this.mc.player != null)
            this.mc.player.sendMessage(Text.of("Player Move Detector: OFF"), true);
    }
}