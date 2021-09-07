package com.hedgehog2986.autowelcome.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

@Mixin(ChatHud.class)
public abstract class chatMixin {
    @Inject(method = "addMessage(Lnet/minecraft/text/Text;)V", at = @At("HEAD"), cancellable = true)
    private void readMessage(Text message, CallbackInfo ci) {
        assert message.getString().trim().length() >= 12;
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
            assert MinecraftClient.getInstance().player != null;
            MinecraftClient.getInstance().player.sendChatMessage("/g "+welcomeMessage.replace("[user]",username));
        }
    }
}
