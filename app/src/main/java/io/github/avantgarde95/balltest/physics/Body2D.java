package io.github.avantgarde95.balltest.physics;

/**
 * Created by avantgarde on 2017-01-20.
 */

public class Body2D {
    private double dt;
    private double mass;
    private Vector2D position;
    private Vector2D velocity;
    private Vector2D acceleration;

    public Body2D(double mass, Vector2D position, Vector2D velocity, double dt) {
        this.mass = mass;
        this.position = position;
        this.velocity = velocity;
        this.dt = dt;

        this.acceleration = new Vector2D(0, 0);
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

    public void setVelocity(Vector2D velocity) {
        this.velocity = velocity;
    }

    public void setAcceleration(Vector2D acceleration) {
        this.acceleration = acceleration;
    }

    public void addForce(Vector2D force) {
        acceleration.add(force);
    }

    public void integrateForces() {
        velocity.add(Vector2D.scale(acceleration, dt));
        position.add(Vector2D.scale(velocity, dt));
        acceleration.clear();
    }
}
