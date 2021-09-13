package mathax.legacy.client.systems.modules.render;

import mathax.legacy.client.settings.DoubleSetting;
import mathax.legacy.client.settings.EnumSetting;
import mathax.legacy.client.settings.Setting;
import mathax.legacy.client.settings.SettingGroup;
import mathax.legacy.client.systems.modules.Categories;
import mathax.legacy.client.systems.modules.Module;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.Quaternion;

public class HandView extends Module {
    private final SettingGroup sgScale = settings.createGroup("Scale");
    private final SettingGroup sgPosition = settings.createGroup("Position");
    private final SettingGroup sgRotation = settings.createGroup("Rotation");
    private final SettingGroup sgSwing = settings.createGroup("Swing");

    // Scale

    private final Setting<Double> scaleX = sgScale.add(new DoubleSetting.Builder()
        .name("x")
        .description("The X scale of your hands.")
        .defaultValue(0.250)
        .sliderMin(0)
        .sliderMax(5)
        .build()
    );

    private final Setting<Double> scaleY = sgScale.add(new DoubleSetting.Builder()
        .name("y")
        .description("The Y scale of your hands.")
        .defaultValue(0.250)
        .sliderMin(0)
        .sliderMax(5)
        .build()
    );

    private final Setting<Double> scaleZ = sgScale.add(new DoubleSetting.Builder()
        .name("z")
        .description("The Z scale of your hands.")
        .defaultValue(0.250)
        .sliderMin(0)
        .sliderMax(5)
        .build()
    );

    // Position

    private final Setting<Double> posX = sgPosition.add(new DoubleSetting.Builder()
        .name("x")
        .description("The X position offset of your hands.")
        .defaultValue(0.1)
        .sliderMin(-3)
        .sliderMax(3)
        .build()
    );

    private final Setting<Double> posY = sgPosition.add(new DoubleSetting.Builder()
        .name("y")
        .description("The Y position offset of your hands.")
        .defaultValue(1.250)
        .sliderMin(-3)
        .sliderMax(3)
        .build()
    );

    private final Setting<Double> posZ = sgPosition.add(new DoubleSetting.Builder()
        .name("z")
        .description("The Z position offset of your hands.")
        .defaultValue(-0.1)
        .sliderMin(-3)
        .sliderMax(3)
        .build()
    );

    // Rotation

    private final Setting<Double> rotationX = sgRotation.add(new DoubleSetting.Builder()
        .name("x")
        .description("The X orientation of your hands.")
        .defaultValue(0)
        .sliderMin(-1)
        .sliderMax(1)
        .build()
    );

    private final Setting<Double> rotationY = sgRotation.add(new DoubleSetting.Builder()
        .name("y")
        .description("The Y orientation of your hands.")
        .defaultValue(0)
        .sliderMin(-1)
        .sliderMax(1)
        .build()
    );

    private final Setting<Double> rotationZ = sgRotation.add(new DoubleSetting.Builder()
        .name("z")
        .description("The Z orientation of your hands.")
        .defaultValue(0)
        .sliderMin(-1)
        .sliderMax(1)
        .build()
    );

    // Swing

    public final Setting<SwingMode> swingMode = sgSwing.add(new EnumSetting.Builder<SwingMode>()
        .name("mode")
        .description("Modifies your client & server hand swinging.")
        .defaultValue(SwingMode.None)
        .build()
    );

    public final Setting<Double> mainSwing = sgSwing.add(new DoubleSetting.Builder()
        .name("main-progress")
        .description("The swing progress of your main hand.")
        .defaultValue(0)
        .min(0)
        .max(1)
        .sliderMin(0)
        .sliderMax(1)
        .build()
    );

    public final Setting<Double> offSwing = sgSwing.add(new DoubleSetting.Builder()
        .name("offhand-progress")
        .description("The swing progress of your offhand.")
        .defaultValue(0)
        .min(0)
        .max(1)
        .sliderMin(0)
        .sliderMax(1)
        .build()
    );

    public HandView() {
        super(Categories.Render, Items.LIME_STAINED_GLASS_PANE, "hand-view", "Alters the way items are rendered in your hands.");
    }

    public void transform(MatrixStack matrices) {
        if (!isActive()) return;

        matrices.scale(scaleX.get().floatValue(), scaleY.get().floatValue(), scaleZ.get().floatValue());
        matrices.translate(posX.get(), posY.get(), posZ.get());
        matrices.multiply(Quaternion.method_35825(rotationX.get().floatValue(), rotationY.get().floatValue(), rotationZ.get().floatValue()));
    }

    public enum SwingMode {
        Offhand,
        Mainhand,
        None
    }
}
