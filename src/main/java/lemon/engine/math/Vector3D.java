package lemon.engine.math;

import com.google.errorprone.annotations.CheckReturnValue;

import java.nio.FloatBuffer;
import java.util.function.UnaryOperator;

public interface Vector3D extends Vector<Vector3D> {
	public static final int NUM_DIMENSIONS = 3;
	public static final Vector3D ZERO = Vector3D.of(0f, 0f, 0f);
	public static final Vector3D ONE = Vector3D.of(1f, 1f, 1f);
	public static final Vector3D[] EMPTY_ARRAY = new Vector3D[] {};

	public float x();
	public float y();
	public float z();

	public static Vector3D of(float x, float y, float z) {
		return new Impl(x, y, z);
	}

	public static Vector3D ofCopy(Vector3D vector) {
		return new Impl(vector);
	}

	public static Vector3D ofParsed(String x, String y, String z) {
		return of(Float.parseFloat(x), Float.parseFloat(y), Float.parseFloat(z));
	}

	public static UnitVector3D ofRandomUnitVector() {
		return UnitVector3D.ofRandom();
	}

	record Impl(float x, float y, float z) implements Vector3D {
		public Impl(Vector3D vector) {
			this(vector.x(), vector.y(), vector.z());
		}

		@Override
		public String toString() {
			return Vector3D.toString(this);
		}
	}

	public default Vector3D crossProduct(Vector3D vector) { // Implemented in only Vector3D
		float newX = this.y() * vector.z() - vector.y() * this.z();
		float newY = this.z() * vector.x() - vector.z() * this.x();
		float newZ = this.x() * vector.y() - vector.x() * this.y();
		return Vector3D.of(newX, newY, newZ);
	}

	@Override
	public default int numDimensions() {
		return NUM_DIMENSIONS;
	}

	@Override
	public default void putInBuffer(FloatBuffer buffer) {
		buffer.put(x());
		buffer.put(y());
		buffer.put(z());
	}

	@Override
	public default void putInArray(float[] array) {
		array[0] = x();
		array[1] = y();
		array[2] = z();
	}

	@Override
	public default Vector3D operate(UnaryOperator<Float> operator) {
		return Vector3D.of(operator.apply(x()), operator.apply(y()), operator.apply(z()));
	}

	@Override
	public default Vector3D add(Vector3D vector) {
		return Vector3D.of(x() + vector.x(), y() + vector.y(), z() + vector.z());
	}

	@Override
	public default Vector3D subtract(Vector3D vector) {
		return Vector3D.of(x() - vector.x(), y() - vector.y(), z() - vector.z());
	}

	@Override
	public default Vector3D multiply(Vector3D vector) {
		return Vector3D.of(x() * vector.x(), y() * vector.y(), z() * vector.z());
	}

	@Override
	public default Vector3D multiply(float scale) {
		return Vector3D.of(x() * scale, y() * scale, z() * scale);
	}

	@Override
	public default Vector3D divide(Vector3D vector) {
		return Vector3D.of(x() / vector.x(), y() / vector.y(), z() / vector.z());
	}

	@Override
	public default Vector3D divide(float scale) {
		return Vector3D.of(x() / scale, y() / scale, z() / scale);
	}

	@Override
	public default float lengthSquared() {
		float x = x();
		float y = y();
		float z = z();
		return x * x + y * y + z * z;
	}

	@Override
	public default float dotProduct(Vector3D vector) {
		return x() * vector.x() + y() * vector.y() + z() * vector.z();
	}

	@CheckReturnValue
	@Override
	public default UnitVector3D normalize() {
		return UnitVector3D.ofNormalized(this);
	}

	public default float distanceSquared(float x, float y, float z) {
		float dx = x() - x;
		float dy = y() - y;
		float dz = z() - z;
		return dx * dx + dy * dy + dz * dz;
	}

	public default Vector2D toXYVector() {
		return Vector2D.of(x(), y());
	}

	public default Vector2D toXZVector() {
		return Vector2D.of(x(), z());
	}

	public default Vector3D withZeroX() {
		return of(0f, y(), z());
	}

	public default Vector3D withZeroY() {
		return of(x(), 0f, z());
	}

	public default Vector3D withZeroZ() {
		return of(x(), y(), 0f);
	}

	public default Vector3D withX(float x) {
		return of(x, y(), z());
	}

	public default Vector3D withY(float y) {
		return of(x(), y, z());
	}

	public default Vector3D withZ(float z) {
		return of(x(), y(), z);
	}

	public default Vector3D withOnlyX() {
		return of(x(), 0f, 0f);
	}

	public default Vector3D withOnlyY() {
		return of(0f, y(), 0f);
	}

	public default Vector3D withOnlyZ() {
		return of (0f, 0f, z());
	}

	public static String toString(Vector3D vector) {
		return String.format("Vector3D[x=%f, y=%f, z=%f]", vector.x(), vector.y(), vector.z());
	}
}
