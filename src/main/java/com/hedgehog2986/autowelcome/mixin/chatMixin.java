package com.hedgehog2986.autowelcome.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

@Mixin(ChatHud.class)
public abstract class chatMixin {
    long lastWelcome = 0;
    Vec3d lastPos;
    @Inject(method = "addMessage(Lnet/minecraft/text/Text;)V", at = @At("HEAD"))
    private void readMessage(Text message, CallbackInfo ci) {
        if (message.getString().trim().length() >= 12){
            if (System.currentTimeMillis() >= lastWelcome + 30000) {
                assert MinecraftClient.getInstance().player != null;
                if (MinecraftClient.getInstance().player.getPos() != lastPos) {
                    var messageString = message.getString().trim();
                    File messageFile = new File("welcomemsg.txt");
                    var welcomeMessage = "";
                    try {
                        welcomeMessage = new Scanner(messageFile).nextLine();
                    } catch (FileNotFoundException ignored) {
                        welcomeMessage = "welcome";
                    }
                    if (messageString.startsWith("[ WELCOME ]")) {
                        var username = messageString.split(" ")[4];
                        lastWelcome = System.currentTimeMillis();
                        lastPos = MinecraftClient.getInstance().player.getPos();
                        MinecraftClient.getInstance().player.sendCommand("g " + welcomeMessage.replace("[user]", username));
                    }
                }
            }
        }
    }
}
