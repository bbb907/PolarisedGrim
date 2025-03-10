package ac.grim.grimac.utils.data;

import lombok.Getter;
import org.bukkit.util.Vector;

import java.util.Objects;

public class VectorData {
    public static final class MoveVectorData extends VectorData {
        public int x;
        public int z;

        public MoveVectorData(Vector vector, VectorData lastVector, VectorType vectorType, int x, int z) {
            super(vector, lastVector, vectorType);
            this.x = x;
            this.z = z;

            if (x != 0 || z != 0) {
                addVectorType(VectorType.WithInput);
            }
        }

        public MoveVectorData(Vector vector, VectorType vectorType, int x, int z) {
            super(vector, vectorType);
            this.x = x;
            this.z = z;

            if (x != 0 || z != 0) {
                addVectorType(VectorType.WithInput);
            }
        }
    }

    public VectorType vectorType;
    public VectorData lastVector;
    public VectorData preUncertainty;
    public Vector vector;

    @Getter
    private boolean isKnockback, firstBreadKb, isExplosion, firstBreadExplosion, isTrident, isZeroPointZeroThree, isSwimHop, isFlipSneaking, isFlipItem, isJump, isAttackSlow, isWithInput;

    // For handling replacing the type of vector it is while keeping data
    public VectorData(Vector vector, VectorData lastVector, VectorType vectorType) {
        this.vector = vector;
        this.lastVector = lastVector;
        this.vectorType = vectorType;

        if (lastVector != null) {
            isKnockback = lastVector.isKnockback;
            firstBreadKb = lastVector.firstBreadKb;
            isExplosion = lastVector.isExplosion;
            firstBreadExplosion = lastVector.firstBreadExplosion;
            isTrident = lastVector.isTrident;
            isZeroPointZeroThree = lastVector.isZeroPointZeroThree;
            isSwimHop = lastVector.isSwimHop;
            isFlipSneaking = lastVector.isFlipSneaking;
            isFlipItem = lastVector.isFlipItem;
            isJump = lastVector.isJump;
            preUncertainty = lastVector.preUncertainty;
            isAttackSlow = lastVector.isAttackSlow;
            isWithInput = lastVector.isWithInput;
        }

        addVectorType(vectorType);
    }

    public VectorData(Vector vector, VectorType vectorType) {
        this.vector = vector;
        this.vectorType = vectorType;
        addVectorType(vectorType);
    }

    public VectorData returnNewModified(VectorType type) {
        return new VectorData(vector, this, type);
    }

    public VectorData returnNewModified(Vector newVec, VectorType type) {
        return new VectorData(newVec, this, type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VectorData that = (VectorData) o;
        return isKnockback == that.isKnockback && firstBreadKb == that.firstBreadKb && isExplosion == that.isExplosion && firstBreadExplosion == that.firstBreadExplosion && isTrident == that.isTrident && isZeroPointZeroThree == that.isZeroPointZeroThree && isSwimHop == that.isSwimHop && isFlipSneaking == that.isFlipSneaking && isFlipItem == that.isFlipItem && isJump == that.isJump && isAttackSlow == that.isAttackSlow && vectorType == that.vectorType && Objects.equals(lastVector, that.lastVector) && Objects.equals(preUncertainty, that.preUncertainty) && Objects.equals(vector, that.vector);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vectorType, lastVector, preUncertainty, vector, isKnockback, firstBreadKb, isExplosion, firstBreadExplosion, isTrident, isZeroPointZeroThree, isSwimHop, isFlipSneaking, isFlipItem, isJump, isAttackSlow);
    }

    protected void addVectorType(VectorType type) {
        switch (type) {
            case Knockback:
                isKnockback = true;
                break;
            case FirstBreadKnockback:
                firstBreadKb = true;
                break;
            case Explosion:
                isExplosion = true;
                break;
            case FirstBreadExplosion:
                firstBreadExplosion = true;
                break;
            case Trident:
                isTrident = true;
                break;
            case ZeroPointZeroThree:
                isZeroPointZeroThree = true;
                break;
            case Swimhop:
                isSwimHop = true;
                break;
            case Flip_Sneaking:
                isFlipSneaking = true;
                break;
            case Flip_Use_Item:
                isFlipItem = true;
                break;
            case Jump:
                isJump = true;
                break;
            case AttackSlow:
                isAttackSlow = true;
                break;
            case WithInput:
                isWithInput = true;
                break;
        }
    }

    @Override
    public String toString() {
        return "VectorData{" +
                "pointThree=" + isZeroPointZeroThree +
                ", vector=" + vector +
                '}';
    }

    // TODO: This is a stupid idea that slows everything down, remove it! There are easier ways to debug grim.
    // Would make false positives really easy to fix
    // But seriously, we could trace the code to find the mistake
    public enum VectorType {
        Normal,
        Swimhop,
        Climbable,
        Knockback,
        FirstBreadKnockback,
        HackyClimbable,
        Teleport,
        SkippedTicks,
        Explosion,
        FirstBreadExplosion,
        InputResult,
        WithInput,
        StuckMultiplier,
        Spectator,
        Dead,
        Jump,
        SurfaceSwimming,
        SwimmingSpace,
        BestVelPicked,
        Firework,
        Lenience,
        TridentJump,
        Trident,
        SlimePistonBounce,
        ZeroPointZeroThree,
        AttackSlow,
        Flip_Sneaking,
        Flip_Use_Item,
        EntityPushing
    }
}
