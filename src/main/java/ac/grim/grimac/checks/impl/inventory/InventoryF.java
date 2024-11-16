package ac.grim.grimac.checks.impl.inventory;

import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.InventoryCheck;
import ac.grim.grimac.player.GrimPlayer;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;

@CheckData(name = "InventoryE", setback = 3, experimental = true)
public class InventoryF extends InventoryCheck {

    public InventoryF(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_9)) return;

        super.onPacketReceive(event);

        if (event.getPacketType() == PacketType.Play.Client.CLICK_WINDOW) {
            if (!player.hasInventoryOpen) {
                if (flag()) {
                    // Cancel the packet
                    if (shouldModifyPackets()) {
                        event.setCancelled(true);
                        player.onPacketCancel();
                    }
                    closeInventory();
                    alert("Sent a click window packet without a open inventory");
                }
            } else {
                reward();
            }
        }
    }
}
