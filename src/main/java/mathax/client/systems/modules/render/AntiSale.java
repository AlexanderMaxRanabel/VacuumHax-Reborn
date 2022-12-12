package mathax.client.systems.modules.render;

import mathax.client.eventbus.EventHandler;
import mathax.client.systems.modules.Categories;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import mathax.client.systems.modules.Modules;
import mathax.client.systems.modules.Module;
import mathax.client.events.packets.PacketEvent;

public class AntiSale extends Module {

    private TitleS2CPacket packet;

    @EventHandler
    public void onPacketSend(PacketEvent.Receive event) {
        if (event.packet instanceof TitleS2CPacket) {
            packet = (TitleS2CPacket) event.packet;
                if (packet.getTitle().getString().contains("SALE") || packet.getTitle().getString().contains("sale") || packet.getTitle().getString().contains("Sale")) {
                    event.cancel();
                }
            }
        }
    public AntiSale() {
        super(Categories.Misc, Items.BARRIER, "Anti-Sale", "AD blocker For BlockGame");
    }
}