package mathax.client.systems.modules.movement;

import mathax.client.eventbus.EventHandler;
import mathax.client.events.world.TickEvent;
import mathax.client.systems.modules.Categories;
import mathax.client.systems.modules.Module;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;

public class AntiJebus extends Module {

    public AntiJebus() {
        super(Categories.Movement, Items.IRON_BOOTS, "AntiJebus", "Allows you to not walk on liquids and powder snow like Jebus.");
    }
    @EventHandler
    private void onTick(TickEvent.Post event) {

    Block antijebus = mc.world.getBlockState(mc.player.getBlockPos().down()).getBlock();

    if (antijebus == Blocks.WATER) {
        mc.player.setVelocity(mc.player.getVelocity().x, -2, mc.player.getVelocity().z);
    }



}

}