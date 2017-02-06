package io.github.avantgarde95.balltest.model;

import java.util.ArrayList;
import java.util.List;

import io.github.avantgarde95.balltest.renderer.MyGLRenderer;

/**
 * Created by avantgarde on 2017-01-20.
 */

public class Circle extends Model {
    private float radius;

    public Circle(MyGLRenderer renderer, float radius, int fanCount) {
        super(renderer);

        this.radius = radius;

        // gen. vertices
        List<float[]> fans = new ArrayList<>();
        float angle = (float) (2.0 * Math.PI / fanCount);

        for (int i = 0; i < fanCount; i++) {
            fans.add(new float[]{
                    radius * (float) Math.cos((i + 1) * angle),
                    radius * (float) Math.sin((i + 1) * angle),
                    0
            });

            fans.add(new float[]{0, 0, 0});

            fans.add(new float[]{
                    radius * (float) Math.cos(i * angle),
                    radius * (float) Math.sin(i * angle),
                    0
            });
        }

        // gen. normals
        List<float[]> normals = new ArrayList<>();

        for (int i = 0; i < fanCount * 3; i++) {
            normals.add(new float[]{0, 0, 1});
        }

        setVertices(unpackList(fans, 3));
        setNormals(unpackList(normals, 3));
        make();
    }

    public Circle(MyGLRenderer renderer, float radius) {
        this(renderer, radius, 30);
    }

    private float[] unpackList(List<float[]> list, int elemSize) {
        int listSize = list.size();
        float[] arr = new float[listSize * elemSize];

        for (int i = 0; i < listSize; i++) {
            for (int j = 0; j < elemSize; j++) {
                arr[i * elemSize + j] = list.get(i)[j];
            }
        }

        return arr;
    }
}
