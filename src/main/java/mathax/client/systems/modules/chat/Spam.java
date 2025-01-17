package mathax.client.systems.modules.chat;

import mathax.client.events.game.GameLeftEvent;
import mathax.client.events.game.OpenScreenEvent;
import mathax.client.events.world.TickEvent;
import mathax.client.settings.*;
import mathax.client.systems.friends.Friends;
import mathax.client.systems.modules.Module;
import mathax.client.systems.modules.Categories;
import mathax.client.utils.Utils;
import mathax.client.eventbus.EventHandler;
import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.item.Items;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.List;

public class Spam extends Module {
    private int messageI, timer;

    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final SettingGroup sgAntiSpamBypass = settings.createGroup("Anti Spam Bypass");

    // General

    private final Setting<List<String>> messages = sgGeneral.add(new StringListSetting.Builder()
        .name("messages")
        .description("Messages to use for spam. Use %player% for a name of a random player.")
        .defaultValue(
            "Envy on top!",
            "Volcan on top!",
            "https://discord.gg/fG5T78aQYP"
        )
        .build()
    );

    private final Setting<Boolean> ignoreSelf = sgGeneral.add(new BoolSetting.Builder()
        .name("ignore-self")
        .description("Skips messages when you're in %player%.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> ignoreFriends = sgGeneral.add(new BoolSetting.Builder()
        .name("ignore-friends")
        .description("Skips messages when the %player% is a friend.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Integer> delay = sgGeneral.add(new IntSetting.Builder()
        .name("delay")
        .description("The delay between specified messages in ticks.")
        .defaultValue(250)
        .min(0)
        .sliderRange(0, 1000)
        .build()
    );

    private final Setting<Boolean> disableOnLeave = sgGeneral.add(new BoolSetting.Builder()
        .name("disable-on-leave")
        .description("Disables spam when you leave a server.")
        .defaultValue(true)
        .build()
    );


    private final Setting<Boolean> disableOnDisconnect = sgGeneral.add(new BoolSetting.Builder()
        .name("disable-on-disconnect")
        .description("Disables spam when you are disconnected from a server.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> random = sgGeneral.add(new BoolSetting.Builder()
        .name("randomise")
        .description("Selects a random message from your spam message list.")
        .defaultValue(true)
        .build()
    );

    // Anti Spam Bypass

    private final Setting<Boolean> randomText = sgAntiSpamBypass.add(new BoolSetting.Builder()
        .name("random-text")
        .description("Adds random text at the bottom of the text.")
        .defaultValue(false)
        .build()
    );

    private final Setting<Integer> randomTextLength = sgAntiSpamBypass.add(new IntSetting.Builder()
        .name("length")
        .description("Text length of anti spam bypass.")
        .defaultValue(16)
        .sliderRange(1, 256)
        .visible(randomText::get)
        .build()
    );

    public Spam() {
        super(Categories.Chat, Items.PAPER, "spam", "Spams specified messages in chat.");
    }

    @Override
    public boolean onActivate() {
        timer = delay.get();
        messageI = 0;
        return false;
    }

    @EventHandler
    private void onScreenOpen(OpenScreenEvent event) {
        if (disableOnDisconnect.get() && event.screen instanceof DisconnectedScreen) toggle();
    }

    @EventHandler
    private void onGameLeft(GameLeftEvent event) {
        if (disableOnLeave.get()) toggle();
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (messages.get().isEmpty()) return;

        if (timer <= 0) {
            int i;
            if (random.get()) i = Utils.random(0, messages.get().size());
            else {
                if (messageI >= messages.get().size()) messageI = 0;
                i = messageI++;
            }

            String text = messages.get().get(i);
            if (randomText.get()) text += " " + RandomStringUtils.randomAlphabetic(randomTextLength.get()).toLowerCase();

            String player;
            do {
                player = Utils.getRandomPlayer();
            } while (ignoreSelf.get() && player.equals(mc.getSession().getUsername()) || ignoreFriends.get() && Friends.get().get(player) != null);

            mc.player.sendChatMessage(text.replace("%player%", player));

            timer = delay.get();
        } else timer--;
    }
}
