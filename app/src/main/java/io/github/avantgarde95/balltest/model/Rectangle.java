package io.github.avantgarde95.balltest.model;

import io.github.avantgarde95.balltest.renderer.MyGLRenderer;

/**
 * Created by avantgarde on 2017-01-20.
 */

public class Rectangle extends Model {
    private float width;
    private float height;

    public Rectangle(MyGLRenderer renderer, float width, float height) {
        super(renderer);

        this.width = width;
        this.height = height;

        float u = width / 2.0f;
        float v = height / 2.0f;

        setVertices(new float[]{
                u, v, 0,
                -u, v, 0,
                u, -v, 0,
                -u, -v, 0,
                u, -v, 0,
                -u, v, 0
        });

        setNormals(new float[]{
                0, 0, 1,
                0, 0, 1,
                0, 0, 1,
                0, 0, 1,
                0, 0, 1,
                0, 0, 1
        });

        make();
    }
}
