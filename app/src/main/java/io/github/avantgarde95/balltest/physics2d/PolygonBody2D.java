package io.github.avantgarde95.balltest.physics2d;

/**
 * Created by avantgarde on 2017-02-11.
 */

public class PolygonBody2D extends Body2D {
    private float[][] vertices;

    public PolygonBody2D(float mass, Vector2D position, Vector2D velocity, float[][] vertices) {
        super(mass, position, velocity);
        this.vertices = vertices;
    }

    public float[][] getVertices() {
        return vertices;
    }
}
