package io.github.avantgarde95.balltest.physics;

import java.util.Locale;

/**
 * Created by avantgarde on 2017-01-20.
 */

public class Vector2D {
    public double x; // instead of float, we use double for better precision...
    public double y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // ------------------------------------------------------
    // instance-wise

    public void neg() {
        x = -x;
        y = -y;
    }

    public void add(Vector2D other) {
        x += other.x;
        y += other.y;
    }

    public void sub(Vector2D other) {
        x -= other.x;
        y -= other.y;
    }

    public void scale(double s) {
        x *= s;
        y *= s;
    }

    public double norm() {
        return Math.sqrt(x * x + y * y);
    }

    public double inner(Vector2D other) {
        return x * other.x + y * other.y;
    }

    public double angle() {
        return Math.atan2(y, x);
    }

    public Vector2D[] projection(Vector2D other) {
        double d1 = norm();
        double d2 = other.norm();

        if (d1 == 0) {
            return new Vector2D[]{new Vector2D(0, 0), new Vector2D(0, 0)};
        }

        if (d2 == 0) {
            return new Vector2D[]{copy(), new Vector2D(0, 0)};
        }

        double c = Vector2D.inner(this, other) / (d1 * d2);

        if (c > 1.0) {
            c = 1.0;
        }

        if (c < -1.0) {
            c = -1.0;
        }

        double f = d1 / d2 * Math.sqrt(1 - c * c);
        Vector2D para = Vector2D.scale(other, d1 / d2 * c);
        Vector2D ortho = new Vector2D(-other.y * f, other.x * f);

        return new Vector2D[]{para, ortho};
    }

    public Vector2D copy() {
        return new Vector2D(x, y);
    }

    public void clear() {
        x = 0;
        y = 0;
    }

    public String toString() {
        return String.format(Locale.US, "(%.4f, %.4f)", x, y);
    }

    // ------------------------------------------------------
    // class-wise

    public static Vector2D add(Vector2D a, Vector2D b) {
        return new Vector2D(a.x + b.x, a.y + b.y);
    }

    public static Vector2D sub(Vector2D a, Vector2D b) {
        return new Vector2D(a.x - b.x, a.y - b.y);
    }

    public static Vector2D scale(Vector2D a, double s) {
        return new Vector2D(a.x * s, a.y * s);
    }

    public static double inner(Vector2D a, Vector2D b) {
        return a.x * b.x + a.y * b.y;
    }
}
