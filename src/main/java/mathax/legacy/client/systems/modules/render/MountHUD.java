package mathax.legacy.client.systems.modules.render;

import mathax.legacy.client.systems.modules.Categories;
import mathax.legacy.client.systems.modules.Module;
import net.minecraft.item.Items;

public class MountHUD extends Module {
    public MountHUD() {
        super(Categories.Render, Items.DIAMOND_CHESTPLATE, "mount-hud", "Display xp bar and hunger when riding.");
    }
}
