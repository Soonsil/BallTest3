package io.github.avantgarde95.balltest.physics2d;

/**
 * Created by avantgarde on 2017-02-11.
 */

public class CircleBody2D extends Body2D {
    private float radius;

    public CircleBody2D(float mass, Vector2D position, Vector2D velocity, float radius) {
        super(mass, position, velocity);
        this.radius = radius;
    }

    public float getRadius() {
        return radius;
    }
}
