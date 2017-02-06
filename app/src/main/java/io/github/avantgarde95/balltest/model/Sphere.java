package io.github.avantgarde95.balltest.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.github.avantgarde95.balltest.renderer.MyGLRenderer;

/**
 * Created by avantgarde on 2017-01-20.
 */

public class Sphere extends Model {
    private float radius;

    public Sphere(MyGLRenderer renderer, float radius, int level) {
        super(renderer);

        this.radius = radius;

        // base vertices
        float[] a = {0, radius, 0};
        float[] b = {0, 0, radius};
        float[] c = {radius, 0, 0};
        float[] d = {0, 0, -radius};
        float[] e = {-radius, 0, 0};
        float[] f = {0, -radius, 0};

        // gen. faces
        List<float[]> faces = new ArrayList<>();

        genFaces(faces, a, b, c, level);
        genFaces(faces, a, c, d, level);
        genFaces(faces, a, d, e, level);
        genFaces(faces, a, e, b, level);
        genFaces(faces, b, f, c, level);
        genFaces(faces, c, f, d, level);
        genFaces(faces, d, f, e, level);
        genFaces(faces, e, f, b, level);

        // gen. vertices / normals
        float[] arr = unpackList(faces, 3);

        setVertices(arr);
        setNormals(arr); // sphere : vertex // normal

        // shaders
        setShader("phong-vshader.glsl", "phong-fshader.glsl");

        make();
    }

    public Sphere(MyGLRenderer renderer, float radius) {
        this(renderer, radius, 3);
    }

    private void genFaces(List<float[]> faces, float[] a, float[] b, float[] c, int level) {
        if (level <= 0) {
            faces.add(a);
            faces.add(b);
            faces.add(c);
        } else {
            float[] d = resize(midpoint(b, c), radius);
            float[] e = resize(midpoint(c, a), radius);
            float[] f = resize(midpoint(a, b), radius);

            genFaces(faces, a, f, e, level - 1);
            genFaces(faces, f, b, d, level - 1);
            genFaces(faces, e, d, c, level - 1);
            genFaces(faces, e, f, d, level - 1);
        }
    }

    private float[] midpoint(float[] a, float[] b) {
        return new float[]{
                (a[0] + b[0]) / 2.0f,
                (a[1] + b[1]) / 2.0f,
                (a[2] + b[2]) / 2.0f
        };
    }

    private float[] resize(float[] a, float size) {
        float x = a[0];
        float y = a[1];
        float z = a[2];

        float factor = size / (float) Math.sqrt(x * x + y * y + z * z);

        return new float[]{x * factor, y * factor, z * factor};
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
