package dev.smithed.companion.pack_provider;

import net.minecraft.resource.ResourcePackSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public interface SmithedDataPackSource extends ResourcePackSource {

    ResourcePackSource PACK_SOURCE_SMITHED = smithedText();

    static ResourcePackSource smithedText() {
        return (text2) -> (Text.translatable("pack.nameAndSource", text2, Text.literal("smithed"))).formatted(Formatting.AQUA);
    }

}