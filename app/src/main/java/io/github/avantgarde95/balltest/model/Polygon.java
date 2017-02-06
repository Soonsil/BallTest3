package io.github.avantgarde95.balltest.model;

import android.opengl.GLES20;

import java.util.ArrayList;
import java.util.List;

import io.github.avantgarde95.balltest.renderer.MyGLRenderer;

/**
 * Created by avantgarde on 2017-01-20.
 */

public class Polygon extends Model {
    public Polygon(MyGLRenderer renderer, float[][] vertices) {
        super(renderer);

        // calc. edges
        List<float[]> edges = new ArrayList<>();

        for (int i = 0; i < vertices.length - 1; i++) {
            edges.add(new float[]{vertices[i][0], vertices[i][1], 0});
            edges.add(new float[]{vertices[i + 1][0], vertices[i + 1][1], 0});
        }

        // calc. normals
        List<float[]> normals = new ArrayList<>();

        for (int i = 0; i < vertices.length * 2; i++) {
            normals.add(new float[]{0, 0, 1});
        }

        setVertices(unpackList(edges, 3));
        setNormals(unpackList(normals, 3));
        setDrawType(GLES20.GL_LINES);

        make();
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
