package ca.skynetcloud.core_botics.client.init;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeyBindingInit {

    public static KeyBinding MODE_SWITCH = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.core_botics.toggle_ride_mode", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_U, "category.core_botics.general"));
    public static KeyBinding ascendKey  = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.core_botics.ascend", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_F, "category.core_botics.general"));
    public static KeyBinding descendKey  = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.core_botics.descend", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_R, "category.core_botics.general"));

    public static void initialize() {}
}
