package lemon.engine.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuaternionTest {
    private static final float TOLERANCE = 0.001f;

    @Test
    public void testFromToEulerAnglesX() {
        var angle = MathUtil.toRadians(90f);
        var v = Vector3D.of(angle, 0f, 0f);
        var a = new EulerAngles(v, EulerAnglesConvention.YAW_PITCH_ROLL);
        Quaternion q = Quaternion.fromEulerAngles(a);
        var b = q.toEulerAngles();
        assertTrue(EulerAngles.isEqual(a, b, TOLERANCE));
    }

    @Test
    public void testFromToEulerAnglesY() {
        var angle = MathUtil.toRadians(90f);
        var v = Vector3D.of(0f, angle, 0f);
        var a = new EulerAngles(v, EulerAnglesConvention.YAW_PITCH_ROLL);
        Quaternion q = Quaternion.fromEulerAngles(a);
        var b = q.toEulerAngles();
        assertTrue(EulerAngles.isEqual(a, b, TOLERANCE));
    }

    @Test
    public void testFromToEulerAnglesZ() {
        var angle = MathUtil.toRadians(90f);
        var v = Vector3D.of(0f, 0f, angle);
        var a = new EulerAngles(v, EulerAnglesConvention.YAW_PITCH_ROLL);
        Quaternion q = Quaternion.fromEulerAngles(a);
        var b = q.toEulerAngles();
        assertTrue(EulerAngles.isEqual(a, b, TOLERANCE));
    }

    @Test
    public void testFromToEulerAnglesRandomXY() {
        MathUtilTest.assertAgreementEulerAngles(1000,
                () -> MathUtil.randomYawPitchRoll().withZeroRoll(),
                v -> Quaternion.fromEulerAngles(v).toEulerAngles());
    }

    @Test
    public void testFromToEulerAnglesRandomXZ() {
        MathUtilTest.assertAgreementEulerAngles(1000,
                () -> MathUtil.randomYawPitchRoll().withZeroYaw(),
                v -> Quaternion.fromEulerAngles(v).toEulerAngles());
    }

    @Test
    public void testFromToEulerAnglesRandomYZ() {
        MathUtilTest.assertAgreementEulerAngles(1000,
                () -> MathUtil.randomYawPitchRoll().withZeroPitch(),
                v -> Quaternion.fromEulerAngles(v).toEulerAngles());
    }

    @Test
    public void testFromToEulerAnglesRandom() {
        MathUtilTest.assertAgreementEulerAngles(1000,
                MathUtil::randomYawPitchRoll,
                v -> Quaternion.fromEulerAngles(v).toEulerAngles());
    }

    @Test
    public void testRotationMatrixX() {
        var angle = MathUtil.toRadians(90f);
        var angles = new EulerAngles(Vector3D.of(angle, 0f, 0f), EulerAnglesConvention.YAW_PITCH_ROLL);
        Quaternion q = Quaternion.fromEulerAngles(angles);
        Matrix a = q.toRotationMatrix();
        Matrix b = MathUtil.getRotationX(angle);
        assertTrue(Matrix.isEqual(a, b, TOLERANCE));
    }

    @Test
    public void testRotationMatrixY() {
        var angle = MathUtil.toRadians(90f);
        var angles = new EulerAngles(Vector3D.of(0f, angle, 0f), EulerAnglesConvention.YAW_PITCH_ROLL);
        Quaternion q = Quaternion.fromEulerAngles(angles);
        Matrix a = q.toRotationMatrix();
        Matrix b = MathUtil.getRotationY(angle);
        assertTrue(Matrix.isEqual(a, b, TOLERANCE));
    }

    @Test
    public void testRotationMatrixZ() {
        var angle = MathUtil.toRadians(90f);
        var angles = new EulerAngles(Vector3D.of(0f, 0f, angle), EulerAnglesConvention.YAW_PITCH_ROLL);
        Quaternion q = Quaternion.fromEulerAngles(angles);
        Matrix a = q.toRotationMatrix();
        Matrix b = MathUtil.getRotationZ(angle);
        assertTrue(Matrix.isEqual(a, b, TOLERANCE));
    }

    @Test
    public void testRotationMatrix() {
        MathUtilTest.assertAgreementMatrix(1000,
                MathUtil::randomYawPitchRoll,
                MathUtil::getRotation,
                angles -> Quaternion.fromEulerAngles(angles).toRotationMatrix());
    }

    @Test
    public void testYawPitchRollConvention() {
        for (int i = 0; i < 1000; i++) {
            var angle = MathUtil.randomYawPitchRoll();
            var a = Quaternion.fromEulerAngles(angle);

            var yaw = Quaternion.fromEulerAngles(angle.withOnlyYaw());
            var pitch = Quaternion.fromEulerAngles(angle.withOnlyPitch());
            var roll = Quaternion.fromEulerAngles(angle.withOnlyRoll());
            var b = yaw.multiply(pitch.multiply(roll));
            Quaternion.assertEquals(a, b, TOLERANCE);
        }
    }

    @Test
    public void testFromEulerAnglesNoNaN() {
        var pi_over_2 = MathUtil.PI / 2f;
        for (int i = 0; i < 1000; i++) {
            var a = new EulerAngles(Vector3D.of(-pi_over_2, MathUtil.randomAngle(), MathUtil.randomAngle()), EulerAnglesConvention.YAW_PITCH_ROLL);
            var b = new EulerAngles(Vector3D.of(pi_over_2, MathUtil.randomAngle(), MathUtil.randomAngle()), EulerAnglesConvention.YAW_PITCH_ROLL);
            var v_a = Quaternion.fromEulerAngles(a).toEulerAngles().vector();
            var v_b = Quaternion.fromEulerAngles(b).toEulerAngles().vector();
            assertFalse(v_a.hasNaN(), v_a::toString);
            assertFalse(v_b.hasNaN(), v_b::toString);
        }
    }

    @Test
    public void testSlerpFromEqualsTo() {
        for (int i = 0; i < 1000; i++) {
            var random = Quaternion.fromEulerAngles(MathUtil.randomYawPitchRoll());
            var slerp = Quaternion.slerp(random, random, (float) (Math.random() * 2.0 - 0.5));
            var x = random.conjugate().multiply(random);
            var y = x.pow(0.5f);
            var z = random.multiply(y); // Equation 6.4
            assertFalse(slerp.hasNaN());
            Quaternion.assertEquals(random, slerp, TOLERANCE);
        }
    }
}