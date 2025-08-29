package com.leefy.playermovedetector.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;

@Environment(EnvType.CLIENT)
public final class MarkerRenderer {
    // Texture identifiers for the markers
    private static final Identifier RED_MARKER_TEXTURE = Identifier.of("player-move-detector", "textures/red-marker.png");
    private static final Identifier GREEN_MARKER_TEXTURE = Identifier.of("player-move-detector", "textures/green-marker.png");
    
    public static void renderBillboardMarker(MatrixStack matrices, VertexConsumerProvider providers, double worldX, double worldY, double worldZ, boolean isMoving, float size, Camera camera) {
        matrices.push();
        // Translate to the position relative to the camera
        matrices.translate(worldX - camera.getPos().x, worldY - camera.getPos().y, worldZ - camera.getPos().z);
        
        // Apply billboard rotation to face the camera
        matrices.multiply(camera.getRotation());
        
        Matrix4f mat = matrices.peek().getPositionMatrix();
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderTexture(0, isMoving ? RED_MARKER_TEXTURE : GREEN_MARKER_TEXTURE);
        RenderSystem.disableCull();
        RenderSystem.enableBlend();
        
        BufferBuilder bb = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        
        // Draw a quad for the marker texture (corrected UV coordinates to fix upside-down issue)
        float halfSize = size / 2.0f;
        bb.vertex(mat, -halfSize, -halfSize, 0).texture(0, 1);
        bb.vertex(mat, -halfSize, halfSize, 0).texture(0, 0);
        bb.vertex(mat, halfSize, halfSize, 0).texture(1, 0);
        bb.vertex(mat, halfSize, -halfSize, 0).texture(1, 1);
        
        BufferRenderer.drawWithGlobalProgram(bb.end());
        RenderSystem.disableBlend();
        RenderSystem.enableCull();
        matrices.pop();
    }
}