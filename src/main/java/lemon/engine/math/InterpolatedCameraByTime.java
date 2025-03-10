package lemon.engine.math;

import lemon.futility.FObservable;

import java.time.Duration;
import java.time.Instant;

public class InterpolatedCameraByTime implements Camera {
    private final Camera from;
    private final Camera to;
    private final Instant startTime;
    private final Duration duration;
    private final FObservable<Boolean> done = new FObservable<>(false);

    public InterpolatedCameraByTime(Camera from, Camera to, Instant startTime, Duration duration) {
        this.from = from;
        this.to = to;
        this.startTime = startTime;
        this.duration = duration;
    }

    public float calcT() {
        var t = ((float) Duration.between(startTime, Instant.now()).toMillis()) / ((float) duration.toMillis());
        if (t >= 1f) {
            done.setValue(true);
        }
        return MathUtil.saturate(t);
    }

    @Override
    public Vector3D position() {
        if (done()) {
            return to.position();
        } else {
            var from = this.from.position();
            var to = this.to.position();
            var t = calcT();
            return to.subtract(from).multiply(t).add(from);
        }
    }

    @Override
    public Quaternion rotation() {
        if (done()) {
            return to.rotation();
        } else {
            var from = this.from.rotation();
            var to = this.to.rotation();
            var t = calcT();
            return Quaternion.slerp(from, to, t);
        }
    }

    @Override
    public Matrix projectionMatrix() {
        return to.projectionMatrix();
    }

    public boolean done() {
        return done.getValue();
    }

    public FObservable<Boolean> observableDone() {
        return done;
    }

    public Camera from() {
        return from;
    }

    public Camera to() {
        return to;
    }
}
