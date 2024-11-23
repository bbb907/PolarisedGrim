package ac.grim.grimac.checks.impl.packetorder;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.type.PostPredictionCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.anticheat.update.PredictionComplete;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.world.BlockFace;
import com.github.retrooper.packetevents.wrapper.play.client.*;
import lombok.Getter;
import org.jetbrains.annotations.Contract;

@Getter
public final class PacketOrderProcessor extends Check implements PostPredictionCheck {
    public PacketOrderProcessor(final GrimPlayer player) {
        super(player);
    }

    private boolean openingInventory; // only pre 1.9 clients on pre 1.9 servers
    private boolean swapping;
    private boolean dropping;
    private boolean interacting;
    private boolean attacking;
    private boolean releasing;
    private boolean digging;
    private boolean sprinting;
    private boolean sneaking;
    private boolean placing;
    private boolean using;
    private boolean picking;
    private boolean clickingInInventory;
    private boolean closingInventory;
    private boolean quickMoveClicking;
    private boolean pickUpClicking;

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        final PacketTypeCommon packetType = event.getPacketType();

        if (packetType == PacketType.Play.Client.CLIENT_STATUS) {
            if (new WrapperPlayClientClientStatus(event).getAction() == WrapperPlayClientClientStatus.Action.OPEN_INVENTORY_ACHIEVEMENT) {
                openingInventory = true;
            }
        }

        if (packetType == PacketType.Play.Client.INTERACT_ENTITY) {
            if (new WrapperPlayClientInteractEntity(event).getAction() == WrapperPlayClientInteractEntity.InteractAction.ATTACK) {
                attacking = true;
            } else {
                interacting = true;
            }
        }

        if (packetType == PacketType.Play.Client.PLAYER_DIGGING) {
            switch (new WrapperPlayClientPlayerDigging(event).getAction()) {
                case SWAP_ITEM_WITH_OFFHAND -> swapping = true;
                case DROP_ITEM, DROP_ITEM_STACK -> dropping = true;
                case RELEASE_USE_ITEM -> releasing = true;
                case FINISHED_DIGGING, CANCELLED_DIGGING, START_DIGGING -> digging = true;
            }
        }

        if (packetType == PacketType.Play.Client.ENTITY_ACTION) {
            switch (new WrapperPlayClientEntityAction(event).getAction()) {
                case START_SPRINTING, STOP_SPRINTING -> sprinting = true;
                case STOP_SNEAKING, START_SNEAKING -> sneaking = true;
            }
        }

        if (packetType == PacketType.Play.Client.USE_ITEM) {
            using = true;
        }

        if (packetType == PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT) {
            if (new WrapperPlayClientPlayerBlockPlacement(event).getFace() == BlockFace.OTHER) {
                using = true;
            } else {
                placing = true;
            }
        }

        if (packetType == PacketType.Play.Client.PICK_ITEM) {
            picking = true;
        }

        if (packetType == PacketType.Play.Client.CLICK_WINDOW) {
            clickingInInventory = true;

            switch (new WrapperPlayClientClickWindow(event).getWindowClickType()) {
                case QUICK_MOVE -> quickMoveClicking = true;
                case PICKUP, PICKUP_ALL -> pickUpClicking = true;
            }
        }

        if (packetType == PacketType.Play.Client.CLOSE_WINDOW) {
            closingInventory = true;
        }

        if (WrapperPlayClientPlayerFlying.isFlying(event.getPacketType()) && player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_8) && !player.packetStateData.lastPacketWasTeleport) {
            onTick();
        }
    }

    @Override
    public void onPredictionComplete(PredictionComplete predictionComplete) {
        if (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_9)) {
            onTick();
        }
    }

    private void onTick() {
        openingInventory = false;
        swapping = false;
        dropping = false;
        attacking = false;
        interacting = false;
        releasing = false;
        digging = false;
        placing = false;
        using = false;
        picking = false;
        sprinting = false;
        sneaking = false;
        clickingInInventory = false;
        closingInventory = false;
        quickMoveClicking = false;
        pickUpClicking = false;
    }

    @Contract(pure = true)
    public boolean isRightClicking() {
        return placing || using || interacting;
    }
}
