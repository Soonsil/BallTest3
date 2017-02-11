package io.github.avantgarde95.balltest.physics2d;

import android.opengl.Matrix;

/**
 * Created by avantgarde on 2017-02-11.
 */

public class Body2D {
    private float mass;
    private Vector2D position;
    private Vector2D velocity;
    private Vector2D acceleration;

    public Body2D(float mass, Vector2D position, Vector2D velocity) {
        this.mass = mass;
        this.position = position;
        this.velocity = velocity;

        this.acceleration = new Vector2D(0, 0);
    }

    public void addForce(Vector2D force) {
        acceleration = acceleration.add(force.scale(1.0f / mass));
    }

    public void integrateForce(float dt) {
        velocity = velocity.add(acceleration.scale(dt));
        position = position.add(velocity.scale(dt));
        acceleration = new Vector2D(0, 0);
    }

    public float getMass() {
        return mass;
    }

    public float[] evalMatrix() {
        float[] matrix = new float[16];
        float x = position.getX();
        float y = position.getY();
        Matrix.setIdentityM(matrix, 0);
        Matrix.translateM(matrix, 0, x, y, 0);
        return matrix;
    }

    public Vector2D getPosition() {
        return position;
    }

    public Vector2D getVelocity() {
        return velocity;
    }

    public Vector2D getAcceleration() {
        return acceleration;
    }

    public void setPosition(Vector2D position) {
        this.position = position;
    }

    public void setVelocity(Vector2D velocity) {
        this.velocity = velocity;
    }
}
