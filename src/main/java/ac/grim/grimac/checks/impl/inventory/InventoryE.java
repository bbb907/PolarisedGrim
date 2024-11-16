package ac.grim.grimac.checks.impl.inventory;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;

@CheckData(name = "InventoryE", setback = 3)
public class InventoryE extends Check implements PacketCheck {

    public InventoryE(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.HELD_ITEM_CHANGE) {
            // It is not possible to change hotbar slots with held item change while the inventory is open
            // A container click packet would be sent instead
            if (player.hasInventoryOpen) {
                if (flag()) {
                    // Cancel the packet
                    if (shouldModifyPackets()) {
                        event.setCancelled(true);
                        player.onPacketCancel();
                    }
                    player.getInventory().closeInventory();
                    alert("Sent a held item change packet while inventory is open");
                }
            } else {
                reward();
            }
        }
    }
}
