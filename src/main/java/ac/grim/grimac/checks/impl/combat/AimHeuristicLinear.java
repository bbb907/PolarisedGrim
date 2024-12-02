package ac.grim.grimac.checks.impl.combat;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PostPredictionCheck;
import ac.grim.grimac.player.GrimPlayer;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;


@CheckData(name = "AimHeuristicLinear", configName = "AimHeuristicLinear", decay = 0.2)
public class AimHeuristicLinear extends Check implements PostPredictionCheck {
    public AimHeuristicLinear(final GrimPlayer player) {
        super(player);
    }

    double lDeltaYaw=0;
    double lDeltaPitch=0;

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {

        if (event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY) {

            WrapperPlayClientInteractEntity packet = new WrapperPlayClientInteractEntity(event);

            if (packet.getAction() == WrapperPlayClientInteractEntity.InteractAction.ATTACK) {

                double deltaYaw = Math.abs(player.xRot-player.lastXRot);
                double deltaPitch = Math.abs(player.yRot-player.lastYRot);

                if ((deltaPitch == lDeltaPitch && deltaPitch > 0.8) || (deltaYaw == lDeltaYaw && deltaYaw > 0.8)) {
                    flagAndAlert();
                }

                if (deltaYaw % deltaPitch == 0 && deltaYaw >= 0.3) {
                    flagAndAlert();
                }

                lDeltaPitch = deltaPitch;
                lDeltaYaw = deltaYaw;

            }

        }

    }
}
