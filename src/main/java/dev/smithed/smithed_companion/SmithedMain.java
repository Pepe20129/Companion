package dev.smithed.smithed_companion;

import com.google.gson.Gson;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
// getString(ctx, "string")
import java.io.File;
import java.nio.file.Path;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

// word()
// literal("foo")
// argument("bar", word())
// Import everything


public class SmithedMain implements ModInitializer {

	public static Logger logger = LogManager.getLogger();
	public static String MODID = "smithed";
	public static Gson gson = new Gson();
	private static MinecraftServer server;


	@Environment(EnvType.CLIENT)
	public static File SmithedDataPacks = (Path.of(FabricLoader.getInstance().getGameDir().toString() + "/datapacks")).toFile();


	@Override
	public void onInitialize() {

		ServerLifecycleEvents.SERVER_STARTING.register(SmithedMain::setServer);
		ServerLifecycleEvents.SERVER_STOPPED.register(SmithedMain::clearServer);


		//PacketUtils.registerServerPacketListeners();
	}


	// why didn't i put this in the smithed util class? this has to be in the main class or else it crashes the build

	@NotNull
	public static MinecraftServer getServer() {
		if (server != null) {
			return server;
		}
		throw new UnsupportedOperationException("Accessed server too early!");
	}

	public static void setServer(MinecraftServer server) {
		SmithedMain.server = server;
	}

	public static void clearServer(MinecraftServer server) {
		SmithedMain.server = null;
	}


}
