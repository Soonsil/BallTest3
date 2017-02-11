package io.github.avantgarde95.balltest.physics2d;

import java.util.Locale;

/**
 * Created by avantgarde on 2017-02-11.
 */

public class Vector2D {
    private float x;
    private float y;

    public Vector2D(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D neg() {
        return new Vector2D(-x, -y);
    }

    public Vector2D add(Vector2D other) {
        return new Vector2D(this.x + other.x, this.y + other.y);
    }

    public Vector2D sub(Vector2D other) {
        return new Vector2D(this.x - other.x, this.y - other.y);
    }

    public float inner(Vector2D other) {
        return this.x * other.x + this.y * other.y;
    }

    public Vector2D cross(float c) {
        return new Vector2D(y * c, -x * c);
    }

    public Vector2D scale(float c) {
        return new Vector2D(x * c, y * c);
    }

    public Vector2D rotate(float angle) {
        float c = (float) Math.cos((double) angle);
        float s = (float) Math.sin((double) angle);
        return new Vector2D(x * c - y * s, x * s + y * c);
    }

    public float norm() {
        return (float) Math.sqrt(x * x + y * y);
    }

    public Vector2D copy() {
        return new Vector2D(x, y);
    }

    public float angle() {
        return (float) Math.atan2((double) y, (double) x);
    }

    public Vector2D[] proj(Vector2D other) {
        Vector2D parallelVec;
        Vector2D orthogonalVec;
        float otherSize = other.norm();

        if (otherSize == 0) {
            parallelVec = new Vector2D(0, 0);
            orthogonalVec = this.copy();
        } else {
            parallelVec = other.scale(this.inner(other) / (otherSize * otherSize));
            orthogonalVec = this.sub(parallelVec);
        }

        return new Vector2D[]{parallelVec, orthogonalVec};
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "(%.4f, %.4f)", x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Vector2D other = (Vector2D) o;
        return (this.x == other.x) && (this.y == other.y);
    }
}
