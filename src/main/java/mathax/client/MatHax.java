package mathax.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import mathax.client.eventbus.EventBus;
import mathax.client.eventbus.EventHandler;
import mathax.client.eventbus.EventPriority;
import mathax.client.eventbus.IEventBus;
import mathax.client.events.game.OpenScreenEvent;
import mathax.client.events.mathax.KeyEvent;
import mathax.client.events.mathax.MouseButtonEvent;
import mathax.client.events.render.Render2DEvent;
import mathax.client.events.world.TickEvent;
import mathax.client.gui.GuiThemes;
import mathax.client.gui.WidgetScreen;
import mathax.client.gui.renderer.GuiRenderer;
import mathax.client.gui.tabs.Tabs;
import mathax.client.music.Music;
import mathax.client.renderer.*;
import mathax.client.renderer.text.Fonts;
import mathax.client.systems.Systems;
import mathax.client.systems.config.Config;
import mathax.client.systems.hud.HUD;
import mathax.client.systems.modules.Categories;
import mathax.client.systems.modules.Modules;
import mathax.client.systems.modules.client.CapesModule;
import mathax.client.systems.modules.client.ClientSpoof;
import mathax.client.systems.modules.client.DiscordRPC;
import mathax.client.systems.modules.client.MiddleClickFriend;
import mathax.client.systems.modules.combat.*;
import mathax.client.utils.Utils;
import mathax.client.utils.Version;
import mathax.client.utils.misc.FakeClientPlayer;
import mathax.client.utils.misc.KeyBind;
import mathax.client.utils.misc.Names;
import mathax.client.utils.misc.WindowUtils;
import mathax.client.utils.misc.input.KeyAction;
import mathax.client.utils.misc.input.KeyBinds;
import mathax.client.utils.network.MatHaxExecutor;
import mathax.client.utils.player.DamageUtils;
import mathax.client.utils.player.EChestMemory;
import mathax.client.utils.player.Rotations;
import mathax.client.utils.render.EntityShaders;
import mathax.client.utils.render.color.Color;
import mathax.client.utils.render.color.RainbowColors;
import mathax.client.utils.render.color.SettingColor;
import mathax.client.utils.world.BlockIterator;
import mathax.client.utils.world.BlockUtils;
import mathax.client.systems.modules.render.Background;
import mathax.client.systems.modules.render.Zoom;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.SplashOverlay;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.plaf.ColorUIResource;
import java.io.File;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static net.minecraft.client.gui.DrawableHelper.drawTexture;

/*/------------------------------------------------------------------/*/
/*/ THIS CLIENT IS A FORK OF METEOR CLIENT BY MINEGAME159 & SEASNAIL /*/
/*/ https://meteorclient.com                                         /*/
/*/ https://github.com/MeteorDevelopment/meteor-client               /*/
/*/------------------------------------------------------------------/*/
/*/ Music player used from Motor Tunez made by JFronny               /*/
/*/ https://github.com/JFronny/MotorTunez                            /*/
/*/------------------------------------------------------------------/*/

public class MatHax implements ClientModInitializer {
    public static MatHax INSTANCE;
    public static MinecraftClient mc;
    public static final IEventBus EVENT_BUS = new EventBus();

    public static final File GAME_FOLDER = new File(FabricLoader.getInstance().getGameDir().toString());
    public static final File FOLDER = new File(GAME_FOLDER, "Envy");
    public static final File VERSION_FOLDER = new File(FOLDER + "/" + Version.getMinecraft());
    public static final File MUSIC_FOLDER = new File(FOLDER + "/Music");

    public final Color MATHAX_COLOR = new Color(0, 104, 255, 255);
    public final int MATHAX_COLOR_INT = Color.fromRGBA(MATHAX_COLOR.r, MATHAX_COLOR.g, MATHAX_COLOR.b, MATHAX_COLOR.a);
    public final Color MATHAX_BACKGROUND_COLOR = new Color(30, 30, 45, 255);
    public final Color MATHAX_COLOR_RAINBOW = new SettingColor(0,0,0,0, true);
    public final int MATHAX_COLOR_RAINBOW_INT = MATHAX_COLOR_RAINBOW.getPacked();

    public final int MATHAX_BACKGROUND_COLOR_INT = Color.fromRGBA(MATHAX_BACKGROUND_COLOR.r, MATHAX_BACKGROUND_COLOR.g, MATHAX_BACKGROUND_COLOR.b, MATHAX_BACKGROUND_COLOR.a);

    public static final Logger LOG = LoggerFactory.getLogger("Envy");

    public static final String URL = "Coming soon";
    public static final String API_URL = "Coming soon";

    public static List<String> getDeveloperUUIDs() {
        return Arrays.asList(
            //Volcan
            "0e986848824f4215b346018190cf923d",
            "6186d473ea704c36a3472e2b3c3ef5e6",
            "850b366c0bed4d71a731760e30167b72",
            "1466c42c86d44c71883f2156029b638c",
            "b1e727a1472349e482db7bb74c21375a",
            "781eaca0ff3649b490490d980d6fe10c",
            "5dd703bb78a44285bff91be63e32b485",
            //HardlineMouse16
            "6904b1a0a8a246fab09fd66f10a8136e",
            "8a89cdaa3cc049e0bae40cfc88e23e0c"
        );
    }

    public static List<String> getSplashes() {
        return Arrays.asList(

            // SPLASHES
            Formatting.RED + "Envy Client on top!",
            Formatting.GRAY + "Volcan" + Formatting.RED + " based god",
            Formatting.RED + Version.getStylized(),
            Formatting.RED + Version.getMinecraft(),

            // MEME SPLASHES
            Formatting.YELLOW + "cope",
            Formatting.YELLOW + "I am funny -HiIAmFunny",
            Formatting.YELLOW + "IntelliJ IDEa",
            Formatting.YELLOW + "I <3 nns",
            Formatting.YELLOW + "haha 69",
            Formatting.YELLOW + "420 XDDDDDD",
            Formatting.YELLOW + "QUICK STAB THE WHITE GUY",
            Formatting.YELLOW + "ayy",
            Formatting.YELLOW + "too ez",
            Formatting.YELLOW + "owned",
            Formatting.YELLOW + "your mom :joy:",
            Formatting.YELLOW + "BOOM BOOM BOOM!",
            Formatting.YELLOW + "I <3 forks",
            Formatting.YELLOW + "based",
            Formatting.YELLOW + "Pog",
            Formatting.YELLOW + "Big Rat on top!",
            Formatting.YELLOW + "bigrat.monster",
            Formatting.YELLOW + "Hack on go.mineberry.org",
            Formatting.YELLOW + "Hack on 2b2t.org",
            Formatting.YELLOW + "Hack on 2b2t.au",
            Formatting.YELLOW + "Hack on 6b6t.org",
            Formatting.YELLOW + "Compatible with JexClient",
            Formatting.YELLOW + "Better Than Wurst",
            Formatting.YELLOW + "Better Than Impact",
            Formatting.YELLOW + "Better Than Future",
            Formatting.YELLOW + "Better Than Salhack",
            Formatting.YELLOW + "Better Than Sigma",
            Formatting.YELLOW + "Better Than KAMI",
            Formatting.YELLOW + "Better Than Phobos",
            Formatting.YELLOW + "Better Than LiquidBounce",
            Formatting.YELLOW + "Better Than Badlion",
            Formatting.YELLOW + "Better Than Baritone",
            Formatting.YELLOW + "Better Than Lunar",
            Formatting.YELLOW + "Better Than Rise",
            Formatting.YELLOW + "Better Than Hypixel",
            Formatting.YELLOW + "Better Than Mineplex",
            Formatting.YELLOW + "Better Than Meteor",
            Formatting.YELLOW + "Better Than ForgeHax",
            Formatting.YELLOW + "Better Than Novoline",
            Formatting.YELLOW + "Better Than Vape",
            Formatting.YELLOW + "Better Than BleachHack",
            Formatting.YELLOW + "Better Than Mathax",
            Formatting.YELLOW + "Better Than Feather",
            Formatting.YELLOW + "Better Than OptiFine",
            Formatting.YELLOW + "Better Than Sodium",
            Formatting.YELLOW + "Better Than Horion",
            Formatting.YELLOW + "Better Than Zypher",
            Formatting.YELLOW + "Better Than ToolBox",
            Formatting.YELLOW + "Better Than Google",
            Formatting.YELLOW + "Better Than Bing",
            Formatting.YELLOW + "Better Than DuckDuckGo",
            Formatting.YELLOW + "Better Than Yahoo",
            Formatting.YELLOW + "Better Than Tor",
            Formatting.YELLOW + "Better Than Firefox",
            Formatting.YELLOW + "Better Than Chrome",
            Formatting.YELLOW + "Better Than Safari",
            Formatting.YELLOW + "Better Than Opera",
            Formatting.YELLOW + "Better Than Edge",
            Formatting.YELLOW + "Better Than Internet Explorer",
            Formatting.YELLOW + "Better Than Brave",
            Formatting.YELLOW + "Better Than MineBot",
            Formatting.YELLOW + "Better Than MineFlyer",
            Formatting.YELLOW + "Better Than CubeCraft",
            Formatting.YELLOW + "Better Than Dream",
            Formatting.YELLOW + "Wish was better than Technoblade",
            Formatting.YELLOW + "Get Cosmetica or you die in 5 days",
            Formatting.YELLOW + "Chad Water",
            Formatting.YELLOW + "Better than PornHub",
            Formatting.YELLOW + "Better than Xvideos",
            Formatting.YELLOW + "Better than Xhamster",
            Formatting.YELLOW + "Better than RedTube",
            Formatting.YELLOW + "Better than YouPorn",
            Formatting.YELLOW + "Better than Hentai",
            Formatting.YELLOW + "Better than NoComm",
            Formatting.YELLOW + "Better than SSG",
            Formatting.YELLOW + "Better than 1.8",
            Formatting.YELLOW + "Better than 1.12.2",
            Formatting.YELLOW + "Better than 1.16.5",
            Formatting.YELLOW + "Better than 1.17.1",
            Formatting.YELLOW + "Better than 1.18",
            Formatting.YELLOW + "Better than 1.18.1",
            Formatting.YELLOW + "Better than 1.18.2",
            Formatting.YELLOW + "Better than Jeb_",
            Formatting.YELLOW + "Better than Notch",
            Formatting.YELLOW + "Better than Dinnerbone",
            Formatting.YELLOW + "Better than Grumm",
            Formatting.YELLOW + "Better than Minecon",
            Formatting.YELLOW + "L Bozo",
            Formatting.YELLOW + "MEDIC!",
            Formatting.YELLOW + "Femboy's On Top!",
            Formatting.YELLOW + "https://thisfootdoesnotexist.com/",
            Formatting.YELLOW + "https://thispersondoesnotexist.com/",
            Formatting.YELLOW + "MUST....TELEPORT....BREAD....",
            Formatting.YELLOW + "Better than lmaobox",
            Formatting.YELLOW + "The Oldest Anarchy Server in minecraft",
            Formatting.YELLOW + "Clown Down",
            Formatting.YELLOW + "Hippity Hoppity Your Anarchy Server is now my property",
            Formatting.YELLOW + "\"is this mathax?\"",
            Formatting.YELLOW + "please help me",
            Formatting.YELLOW + "I'm hungry - China Man",
            Formatting.YELLOW + "big dum",
            Formatting.YELLOW + "we need all 5 of these, trust me",
            Formatting.BLUE + "操你妈",
            Formatting.BLUE + "Chyna man says hai",
            Formatting.LIGHT_PURPLE + "UwU",
            Formatting.YELLOW + "These orphans are getting destroyed",
            Formatting.YELLOW + "Do you guys like my content? Just kidding, I dont care",
            Formatting.YELLOW + "Officer, I drop kicked that child in self defence",
            Formatting.YELLOW + "Murder is cringe",
            Formatting.YELLOW + "If you see a murder being committed, immediately call them a boomer",
            Formatting.YELLOW + "It's my 360 Ghost client",
            Formatting.YELLOW + "If you have a problem, the answer is slavery",
            Formatting.YELLOW + "alts.bigrat.monster",
            Formatting.YELLOW + "Technoblade Never Dies!",
            Formatting.YELLOW + "please help me volcan trapped me in his basement",
            Formatting.YELLOW + "I was bored so i made this",


            // PERSONALIZED
            Formatting.YELLOW + "You're cool, " + Formatting.GRAY + MinecraftClient.getInstance().getSession().getUsername(),
            Formatting.YELLOW + "Owning with " + Formatting.GRAY + MinecraftClient.getInstance().getSession().getUsername(),
            Formatting.YELLOW + "Who is " + Formatting.GRAY + MinecraftClient.getInstance().getSession().getUsername() + Formatting.YELLOW + "?",
            Formatting.YELLOW + "Watching Hentai with " + Formatting.GRAY + MinecraftClient.getInstance().getSession().getUsername(),
            Formatting.YELLOW + "Getting rekt by" + Formatting.GRAY + MinecraftClient.getInstance().getSession().getUsername()

        );
    }

    @Override
    public void onInitializeClient() {
        // Instance
        if (INSTANCE == null) {
            INSTANCE = this;
            return;
        }

        // Log
        LOG.info("Initializing Envy " + Version.getStylized() + " for Minecraft " + Version.getMinecraft() + "...");

        // Global Minecraft client accessor
        mc = MinecraftClient.getInstance();

        // Icon & Title
        WindowUtils.MatHax.setIcon();
        WindowUtils.MatHax.setTitleLoading();

        // Register event handlers
        EVENT_BUS.registerLambdaFactory("mathax.client", (lookupInMethod, klass) -> (MethodHandles.Lookup) lookupInMethod.invoke(null, klass, MethodHandles.lookup()));

        // Pre-load
        Systems.addPreLoadTask(() -> {
            if (!FOLDER.exists()) {
                FOLDER.getParentFile().mkdirs();
                FOLDER.mkdir();

                // ACTIVATE
                Modules.get().get(CapesModule.class).forceToggle(true); // CAPES
                Modules.get().get(DiscordRPC.class).forceToggle(true); // DISCORD RPC
                Modules.get().get(Background.class).forceToggle(true); // BACKGROUND
                Modules.get().get(MiddleClickFriend.class).forceToggle(true); // MIDDLE CLICK FRIEND

                // VISIBILITY
                Modules.get().get(ClientSpoof.class).setVisible(false); // CLIENT SPOOF
                Modules.get().get(CapesModule.class).setVisible(false); // CAPES
                Modules.get().get(DiscordRPC.class).setVisible(false); // DISCORD RPC
                Modules.get().get(Background.class).setVisible(false); // BACKGROUND
                Modules.get().get(MiddleClickFriend.class).setVisible(false); // MIDDLE CLICK FRIEND
                Modules.get().get(Zoom.class).setVisible(false); // ZOOM

                // KEYBINDS
                Modules.get().get(Zoom.class).keybind.set(KeyBind.fromKey(GLFW.GLFW_KEY_C)); // ZOOM

                // KEYBIND OPTIONS
                Modules.get().get(Zoom.class).toggleOnBindRelease = true; // ZOOM

                // TOASTS
                Modules.get().get(AnchorAura.class).setToggleToast(true); // ANCHOR AURA
                Modules.get().get(BedAura.class).setToggleToast(true); // BED AURA
                Modules.get().get(CEVBreaker.class).setToggleToast(true); // CEV BREAKER
                Modules.get().get(CrystalAura.class).setToggleToast(true); // CRYSTAL AURA
                Modules.get().get(KillAura.class).setToggleToast(true); // KILL AURA

                // MESSAGES
                Modules.get().get(Zoom.class).setToggleMessage(false); // ZOOM
            }

            // RESET HUD LOCATIONS
            if (!Systems.get(HUD.class).getFile().exists()) Systems.get(HUD.class).reset.run(); // HUD
        });

        // Pre init
        Utils.init();
        Version.init();
        GL.init();
        Shaders.init();
        Renderer2D.init();
        EntityShaders.initOutlines();
        MatHaxExecutor.init();
        BlockIterator.init();
        EChestMemory.init();
        Rotations.init();
        Names.init();
        FakeClientPlayer.init();
        PostProcessRenderer.init();
        Tabs.init();
        GuiThemes.init();
        Fonts.init();
        DamageUtils.init();
        BlockUtils.init();
        Music.init();

        // Register module categories
        Categories.init();

        // Load systems
        Systems.init();

        // Event bus
        EVENT_BUS.subscribe(this);

        // Sorting modules
        Modules.get().sortModules();

        // Load saves
        Systems.load();

        // Post init
        Fonts.load();
        GuiRenderer.init();
        GuiThemes.postInit();
        RainbowColors.init();

        // Title
        WindowUtils.MatHax.setTitleLoaded();

        // Shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            DiscordRPC.disable();
            Systems.save();
            GuiThemes.save();
        }));

        // Icon & Title
        ClientSpoof cs = Modules.get().get(ClientSpoof.class);
        if (cs.isActive() && cs.changeWindowIcon()) WindowUtils.Meteor.setIcon();
        else WindowUtils.MatHax.setIcon();
        if (cs.isActive() && cs.changeWindowTitle()) WindowUtils.Meteor.setTitle();
        else WindowUtils.MatHax.setTitle();

        // Log
        LOG.info("Envy " + Version.getStylized() + " initialized for Minecraft " + Version.getMinecraft() + "!");
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (mc.currentScreen == null && mc.getOverlay() == null && KeyBinds.OPEN_COMMANDS.wasPressed())
            mc.setScreen(new ChatScreen(Config.get().prefix.get()));

        if (Music.player == null) return;
        if (Music.player.getVolume() != Config.get().musicVolume.get())
            Music.player.setVolume(Config.get().musicVolume.get());
    }

    @EventHandler
    private void onKey(KeyEvent event) {
        if (mc.getOverlay() instanceof SplashOverlay) return;
        if (event.action == KeyAction.Press && KeyBinds.OPEN_CLICK_GUI.matchesKey(event.key, 0)) openClickGUI();
    }

    private void onMouseButton(MouseButtonEvent event) {
        if (mc.getOverlay() instanceof SplashOverlay) return;
        if (event.action == KeyAction.Press && KeyBinds.OPEN_CLICK_GUI.matchesMouse(event.button)) openClickGUI();
    }

    private void openClickGUI() {
        if (Utils.canOpenClickGUI()) Tabs.get().get(0).openScreen(GuiThemes.get());
    }

    private boolean wasWidgetScreen, wasHudHiddenRoot;
    }
