package dev.smithed.companion.packets;

import dev.smithed.companion.SmithedMain;
import dev.smithed.companion.item_groups.ItemGroupData;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

import java.util.Map;
import java.util.Objects;

public class PacketUtils {

    // everything within this map will be registered as a packet listener on the server side
    // is this the most efficient thing to do? probably not but im not making multiple classes to perform minor operations
    public static Map<Identifier, ServerPlayNetworking.PlayChannelHandler> ServerPacketMap = Map.ofEntries(
            // processes and sends itemgroup info.
            Map.entry(new Identifier(SmithedMain.MODID, "itemgroup_info_channel"), (server, player, handler, buf, responseSender) -> {
                for (ItemGroupData data : SmithedMain.unregisteredItemGroups.values()) {
                    PacketByteBuf groupBuffer = PacketByteBufs.create();
                    NbtCompound group = new NbtCompound();
                    data.toNbt(group);
                    SmithedMain.logger.info(group.toString());
                    groupBuffer.writeNbt(group);
                    SP2C(player, new Identifier(SmithedMain.MODID, "itemgroup_info_channel"), groupBuffer);
                }
            }),
            // mark down player as smithed-comp-client user. marker is removed upon logout.
            // might eventually add more subtags for enabled feature checks however idk rn.
            Map.entry(new Identifier(SmithedMain.MODID, "mark_companion_player"), (server, player, handler, buf, responseSender) -> {
                player.addScoreboardTag("smithed.client");
                SmithedMain.logger.info("marked down: " + player.getName() + " as a smithed-companion user");
            })
    );

    // everything within this map will be registered as a packet listener on the client side
    public static Map<Identifier, ClientPlayNetworking.PlayChannelHandler> ClientPacketMap = Map.ofEntries(
            // decodes itemgroup info
            Map.entry(new Identifier(SmithedMain.MODID, "itemgroup_info_channel"), (client, handler, buf, responseSender) -> {

                ItemGroupData itemGroupData = ItemGroupData.fromNBT(Objects.requireNonNull(buf.readUnlimitedNbt()));
                if(!SmithedMain.registeredItemGroups.containsKey(itemGroupData.getName()))
                    SmithedMain.registeredItemGroups.put(itemGroupData.getName(), itemGroupData.toItemGroup());
                else {
                    DefaultedList<ItemStack> stacks = DefaultedList.of();
                    stacks.addAll(itemGroupData.getItemStacks());
                    SmithedMain.registeredItemGroups.get(itemGroupData.getName()).appendStacks(stacks);
                }
            })
    );

    // generic method for sending packet to client
    public static void SP2C(ServerPlayerEntity playerEntity, Identifier channelName, PacketByteBuf buf) {
        ServerPlayNetworking.send(playerEntity, channelName, buf);
    }

    // generic method for sending packet to server
    public static void SP2S(Identifier identifier, PacketByteBuf buf) {
        ClientPlayNetworking.send(identifier, buf);
    }

    public static void registerServerPacketListeners() {
        for (Identifier identifier: ServerPacketMap.keySet())
            ServerPlayNetworking.registerGlobalReceiver(identifier, ServerPacketMap.get(identifier));
    }

    public static void registerClientPacketListeners() {
        for (Identifier identifier: ClientPacketMap.keySet())
            ClientPlayNetworking.registerGlobalReceiver(identifier, ClientPacketMap.get(identifier));
    }

}
