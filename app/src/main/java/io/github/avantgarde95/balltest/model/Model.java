package io.github.avantgarde95.balltest.model;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Arrays;

import io.github.avantgarde95.balltest.renderer.MyGLRenderer;

/**
 * Created by avantgarde on 2017-01-20.
 */

public class Model {
    private MyGLRenderer renderer;
    private int program;

    private FloatBuffer vertexBuffer;
    private FloatBuffer normalBuffer;
    private FloatBuffer textureBuffer;

    private int coordsPerVertex = 3;
    private int vertexStride = coordsPerVertex * 4;

    private float[] color = {0.0f, 0.0f, 1.0f};

    private float[] vertices = new float[]{};
    private float[] normals = new float[]{};
    private float[] textures = new float[]{};

    private float[] modelMatrix = new float[16];
    private float[] normalMatrix = new float[16];

    private String vertexShader = "basic-vshader.glsl";
    private String fragmentShader = "basic-fshader.glsl";

    private int drawType = GLES20.GL_TRIANGLES;

    public Model(MyGLRenderer renderer) {
        this.renderer = renderer;

        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.setIdentityM(normalMatrix, 0);
    }

    public void setVertices(float[] vertices) {
        this.vertices = new float[vertices.length];
        System.arraycopy(vertices, 0, this.vertices, 0, vertices.length);
    }

    public void setNormals(float[] normals) {
        this.normals = new float[normals.length];
        System.arraycopy(normals, 0, this.normals, 0, normals.length);
    }

    public void setMatrix(float[] mat) {
        System.arraycopy(mat, 0, modelMatrix, 0, 16);
    }

    public void setMatrix(float dx, float dy, float dz) {
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.translateM(modelMatrix, 0, dx, dy, dz);
    }

    public void setShader(String vertexShader, String fragmentShader) {
        this.vertexShader = vertexShader;
        this.fragmentShader = fragmentShader;
    }

    public void setDrawType(int drawType) {
        this.drawType = drawType;
    }

    public void setColor(float[] color) {
        System.arraycopy(color, 0, this.color, 0, 3);
    }

    public void make() {
        makeBuffer();
        makeShader();
    }

    public void draw(float[] projMatrix, float[] viewMatrix, float[] light) {
        GLES20.glUseProgram(program);

        // calc. modelview matrix
        float[] modelViewMatrix = new float[16];
        Matrix.multiplyMM(modelViewMatrix, 0, viewMatrix, 0, modelMatrix, 0);

        // calc. normal matrix
        normalMatrix(normalMatrix, 0, modelViewMatrix, 0);

        // uniform handles
        int projMatrixHandle = GLES20.glGetUniformLocation(program, "uProjMatrix");
        int modelViewMatrixHandle = GLES20.glGetUniformLocation(program, "uModelViewMatrix");
        int normalMatrixHandle = GLES20.glGetUniformLocation(program, "uNormalMatrix");
        int colorHandle = GLES20.glGetUniformLocation(program, "uColor");
        int lightHandle = GLES20.glGetUniformLocation(program, "uLight");

        GLES20.glUniformMatrix4fv(projMatrixHandle, 1, false, projMatrix, 0);
        GLES20.glUniformMatrix4fv(modelViewMatrixHandle, 1, false, modelViewMatrix, 0);
        GLES20.glUniformMatrix4fv(normalMatrixHandle, 1, false, normalMatrix, 0);

        GLES20.glUniform3fv(colorHandle, 1, color, 0);
        GLES20.glUniform3fv(lightHandle, 1, light, 0);

        // attr. handles
        int positionHandle = GLES20.glGetAttribLocation(program, "aPosition");
        int normalHandle = GLES20.glGetAttribLocation(program, "aNormal");

        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glEnableVertexAttribArray(normalHandle);

        GLES20.glVertexAttribPointer(positionHandle, coordsPerVertex, GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);
        GLES20.glVertexAttribPointer(normalHandle, coordsPerVertex, GLES20.GL_FLOAT, false,
                vertexStride, normalBuffer);

        // draw vertices
        GLES20.glDrawArrays(drawType, 0, vertices.length / 3);

        // disable handles
        GLES20.glDisableVertexAttribArray(positionHandle);
        GLES20.glDisableVertexAttribArray(normalHandle);
    }

    private void normalMatrix(float[] dst, int dstOffset, float[] src, int srcOffset) {
        Matrix.invertM(dst, dstOffset, src, srcOffset);

        dst[12] = 0;
        dst[13] = 0;
        dst[14] = 0;

        float[] tempMatrix = Arrays.copyOf(dst, 16);
        Matrix.transposeM(dst, dstOffset, tempMatrix, 0);
    }


    private void makeBuffer() {
        vertexBuffer = genBuffer(vertices);
        normalBuffer = genBuffer(normals);
        textureBuffer = genBuffer(textures);
    }

    private void makeShader() {
        int vs = renderer.loadShaderFromFile(GLES20.GL_VERTEX_SHADER, vertexShader);
        int fs = renderer.loadShaderFromFile(GLES20.GL_FRAGMENT_SHADER, fragmentShader);

        program = GLES20.glCreateProgram();

        GLES20.glAttachShader(program, vs);
        GLES20.glAttachShader(program, fs);
        GLES20.glLinkProgram(program);
    }

    private FloatBuffer genBuffer(float[] arr) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(arr.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());

        FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
        floatBuffer.put(arr);
        floatBuffer.position(0);

        return floatBuffer;
    }
}
