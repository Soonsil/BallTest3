package io.github.avantgarde95.balltest.physics;

/**
 * Created by avantgarde on 2017-01-20.
 */

public class PolygonBody2D extends Body2D {
    private Vector2D[] vertices;

    public PolygonBody2D(double mass, Vector2D position, Vector2D velocity, double dt, Vector2D[] vertices) {
        super(mass, position, velocity, dt);
        this.vertices = vertices;
    }

    public Vector2D[] getVertices() {
        return vertices;
    }
}
