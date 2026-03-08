package net.iljorus.logstrippermod;

import com.mojang.logging.LogUtils;
import net.iljorus.logstrippermod.block.ModBlocks;
import net.iljorus.logstrippermod.block.entity.ModBlockEntity;
import net.iljorus.logstrippermod.config.BaseConfig;
import net.iljorus.logstrippermod.gui.LogStripperScreen;
import net.iljorus.logstrippermod.gui.ModMenuTypes;
import net.iljorus.logstrippermod.item.ModItems;
import net.iljorus.logstrippermod.recipe.ModRecipes;
import net.iljorus.logstrippermod.util.Utils;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(LogStripperMod.MOD_ID)
public class LogStripperMod {
    public static final String MOD_ID = "logstrippermod";

    public static IEventBus modEventBus;
    public static ModContainer modContainer;
    private final Logger LOGGER = LogUtils.getLogger();

    public LogStripperMod(FMLJavaModLoadingContext context) {
        //LogStripperMod.modEventBus = modEventBus;         IEventBus modEventBus, ModContainer modContainer
        //LogStripperMod.modContainer = modContainer;

        modEventBus = context.getModEventBus();
        modContainer = context.getContainer();

        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);
        ModBlockEntity.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModRecipes.register(modEventBus);

        //Register Config
        context.registerConfig(ModConfig.Type.COMMON, BaseConfig.COMMON_SPEC);

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }


    private void addCreative(BuildCreativeModeTabContentsEvent event) {

    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            MenuScreens.register(ModMenuTypes.LOG_STRIPPER_MENU.get(), LogStripperScreen::new);
        }
    }

    @Mod.EventBusSubscriber(modid = MOD_ID)
    public static class CoreEvents {
        @SubscribeEvent
        public static void serverTick(TickEvent.ServerTickEvent event) {
            if (event.phase == TickEvent.Phase.START) {
                Utils.tickTime();
            }
        }
    }
}
