package dev.smithed.smithed_companion.mixin;

import dev.smithed.smithed_companion.SmithedDataPackProvider;
import dev.smithed.smithed_companion.SmithedMain;
import net.minecraft.resource.DataPackSettings;
import net.minecraft.resource.FileResourcePackProvider;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourcePackProvider;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashSet;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

    @Inject(method = "loadDataPacks", at= @At("HEAD"))
    private static void injectLoadDatapacks(ResourcePackManager resourcePackManager, DataPackSettings dataPackSettings, boolean safeMode, CallbackInfoReturnable<DataPackSettings> cir) {


        if (safeMode) return; // exit if in safemode
        // New provider for the Smithed directory
        SmithedDataPackProvider smProvider = new SmithedDataPackProvider(SmithedMain.SmithedDataPacks); // fetch providers
        resourcePackManager.providers = new HashSet<>(resourcePackManager.providers); // set providers to existing providers

        // Check if provider exists or not if Yes: Discard, if No: continue
        for (ResourcePackProvider provider : resourcePackManager.providers) {
            if (provider instanceof FileResourcePackProvider && ((FileResourcePackProvider) provider).packsFolder.getAbsolutePath().equals(SmithedMain.SmithedDataPacks.getAbsolutePath())) {
                return;
            }
        }
        // Add and enable provider
        resourcePackManager.providers.add(smProvider);
    }

    // injecting into world reload for crucial data that i can't get just by subscribing to fabrics events
    public void InjectReload() {

    }

}
