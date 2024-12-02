package ac.grim.grimac.checks.impl.combat;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.impl.aim.processor.Collection;
import ac.grim.grimac.checks.type.PostPredictionCheck;
import ac.grim.grimac.player.GrimPlayer;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;


@CheckData(name = "AimHeuristic", configName = "AimHeuristic")
public class AimHeuristic extends Check implements PostPredictionCheck {
    public AimHeuristic(final GrimPlayer player) {
        super(player);
    }

    double lDeltaYaw=0;
    double lDeltaPitch=0;
    Collection yawAccels = new Collection(15,true);
    Collection pitchAccels = new Collection(15,true);

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {

        if (event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY) {

            WrapperPlayClientInteractEntity packet = new WrapperPlayClientInteractEntity(event);

            if (packet.getAction() == WrapperPlayClientInteractEntity.InteractAction.ATTACK) {

                double deltaYaw = Math.abs(player.xRot-player.lastXRot);
                double deltaPitch = Math.abs(player.yRot-player.lastYRot);

                if ((deltaPitch == lDeltaPitch && deltaPitch > 0.1) || (deltaYaw == lDeltaYaw && deltaYaw > 0.1)) {
                    //flagAndAlert("Repeated aiming");
                    event.setCancelled(true);
                }

                if (deltaYaw % deltaPitch == 0 && deltaYaw != 0) {
                    flagAndAlert("Linear aim! DY: "+deltaYaw+" DP: "+deltaPitch);
                    event.setCancelled(true);
                }

                double yawAccel=deltaYaw-lDeltaYaw;
                double pitchAccel=deltaPitch-lDeltaPitch;

                if (yawAccels.getCollectedList().size() >= 8) {

                    double differences = Math.abs(yawAccels.getAverage() - pitchAccels.getAverage());

                    if (differences >= 2.2) {
                        player.bukkitPlayer.sendMessage("D: "+differences);
                        flagAndAlert();
                    }

                    yawAccels.clear();
                    pitchAccels.clear();
                }

                yawAccels.addItem(yawAccel);
                pitchAccels.addItem(pitchAccel);

                lDeltaPitch = deltaPitch;
                lDeltaYaw = deltaYaw;

            }

        }

    }
}
