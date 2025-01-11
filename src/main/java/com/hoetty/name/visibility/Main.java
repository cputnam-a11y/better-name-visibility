package com.hoetty.name.visibility;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BooleanSupplier;

public class Main implements ModInitializer, ClientTickEvents.EndTick {

    private static KeyBinding TOGGLE_NAMES_KEY;
    public static boolean NAMES_TOGGLED = true;

    @Override
    public void onInitialize() {
        TOGGLE_NAMES_KEY = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        "key.name_visibility.toggleNames",
                        InputUtil.Type.KEYSYM,
                        InputUtil.UNKNOWN_KEY.getCode(),
                        "category.name_visibility.keybindcategory"
                )
        );
        ClientTickEvents.END_CLIENT_TICK.register(this);
    }

    @Override
    public void onEndTick(MinecraftClient client) {
        if (!TOGGLE_NAMES_KEY.wasPressed()) {
            return;
        }
        NAMES_TOGGLED = !NAMES_TOGGLED;
        if (client.player == null)
            return;
        Text message = NAMES_TOGGLED
                       ? Text.translatable("message.name_visibility.enabled")
                       : Text.translatable("message.name_visibility.disabled");
        client.player.sendMessage(message, true);
        runWhileTrue(TOGGLE_NAMES_KEY::wasPressed);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    private static void runWhileTrue(BooleanSupplier supplier) {
        while (supplier.getAsBoolean());
    }
}
