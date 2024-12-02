package ac.grim.grimac.checks.impl.scaffolding;

import ac.grim.grimac.api.config.ConfigManager;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.BlockPlaceCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.anticheat.update.BlockPlace;
import ac.grim.grimac.utils.nmsutil.Materials;
import com.github.retrooper.packetevents.protocol.player.GameMode;
import com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import com.github.retrooper.packetevents.util.Vector3i;

@CheckData(name = "RotatingPlace",configName = "RotatingPlace", decay = 0.1)
public class RotatingPlace extends BlockPlaceCheck {
    public RotatingPlace(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onBlockPlace(final BlockPlace place) {
        if (player.gamemode == GameMode.CREATIVE) return;
        Vector3i blockPos = place.getPlacedBlockPos();

        double deltaYaw = Math.abs(player.xRot-player.lastXRot);
        double deltaPitch = Math.abs(player.yRot-player.lastYRot);
        StateType placedUnder = player.compensatedWorld.getStateTypeAt(blockPos.getX(), blockPos.getY()-1, blockPos.getZ());

        if (blockPos.y < player.y && !Materials.isAir(placedUnder)) {
            if (deltaYaw >= 15) {
                if (flagAndAlert() && shouldModifyPackets() && shouldCancel()) {
                    place.resync();
                }
            } else if (deltaPitch >= 5) {
                if (flagAndAlert() && shouldModifyPackets() && shouldCancel()) {
                    place.resync();
                }
            }
        }
    }

    @Override
    public void onReload(ConfigManager config) {
        this.cancelVL = config.getIntElse(getConfigName() + ".cancelVL", 0);
    }
}
