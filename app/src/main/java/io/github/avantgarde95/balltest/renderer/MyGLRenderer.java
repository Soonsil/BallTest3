package io.github.avantgarde95.balltest.renderer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import io.github.avantgarde95.balltest.model.Circle;
import io.github.avantgarde95.balltest.model.Polygon;
import io.github.avantgarde95.balltest.physics.CircleBody2D;
import io.github.avantgarde95.balltest.physics.PolygonBody2D;
import io.github.avantgarde95.balltest.physics.Vector2D;

/**
 * Created by avantgarde on 2017-01-20.
 */

public class MyGLRenderer implements GLSurfaceView.Renderer {
    private Context context; // for using Activity.getAssets()

    private float minDist = 1.0f;
    private float maxDist = 30.0f;

    private float[] eyePos = {0.0f, 0.0f, 4.0f};
    private float[] lookPos = {0.0f, 0.0f, -1.0f};
    private float[] upVec = {0.0f, 1.0f, 0.0f};
    private float[] viewOffset = {0.0f, 0.0f, -1.001f};

    private float[] bgColor = {1.0f, 1.0f, 1.0f, 1.0f};
    private float[] lightPos = {2.0f, 3.0f, 14.0f};

    public float[] viewMatrix = new float[16];
    public float[] viewRotationMatrix = new float[16];
    public float[] viewTranslationMatrix = new float[16];

    private float[] projMatrix = new float[16];

    private Circle ball;
    private CircleBody2D ballBody;

    private Polygon rock1;
    private Polygon rock2;
    private Polygon rock3;

    private PolygonBody2D rockBody1;
    private PolygonBody2D rockBody2;
    private PolygonBody2D rockBody3;

    public MyGLRenderer(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // background color
        GLES20.glClearColor(bgColor[0], bgColor[1], bgColor[2], bgColor[3]);

        // init. gl
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        // init. view
        Matrix.setLookAtM(
                viewMatrix, 0,
                eyePos[0], eyePos[1], eyePos[2],
                lookPos[0], lookPos[1], lookPos[2],
                upVec[0], upVec[1], upVec[2]
        );

        // init. ball
        float ballRadius = 0.13f;
        ball = new Circle(this, ballRadius);

        ballBody = new CircleBody2D(
                1.0,
                new Vector2D(-0.3, 0.4),
                new Vector2D(0, 0),
                1.0,
                (double) ballRadius
        );

        // init. rock1
        float[][] rock1Vertices = {
                {0.0f, -0.4f},
                {-0.1f, -0.5f},
                {-0.2f, -0.6f},
                {-0.5f, -0.6f},
                {-0.1f, -0.7f},
                {0.0f, -0.4f}
        };

        rock1 = new Polygon(this, rock1Vertices);
        rockBody1 = new PolygonBody2D(1.0, new Vector2D(0, 0), new Vector2D(0, 0), 1.0, floatsToVectors(rock1Vertices));

        // init. rock2
        float[][] rock2Vertices = {
                {-0.2f, 0.4f},
                {-0.1f, 0.4f},
                {0.2f, 0.2f},
                {0.3f, 0.0f},
                {0.1f, 0.1f},
                {-0.2f, 0.4f}
        };

        rock2 = new Polygon(this, rock2Vertices);
        rockBody2 = new PolygonBody2D(1.0, new Vector2D(0, 0), new Vector2D(0, 0), 1.0, floatsToVectors(rock2Vertices));

        // calc. view matrix
        Matrix.setIdentityM(viewRotationMatrix, 0);
        Matrix.setIdentityM(viewTranslationMatrix, 0);
        Matrix.translateM(viewTranslationMatrix, 0, viewOffset[0], viewOffset[1], viewOffset[2]);
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        // draw background
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // calc. view
        float[] tempMatrix = new float[16];
        Matrix.setIdentityM(viewMatrix, 0);
        Matrix.multiplyMM(tempMatrix, 0, viewRotationMatrix, 0, viewMatrix, 0);
        System.arraycopy(tempMatrix, 0, viewMatrix, 0, 16);
        Matrix.multiplyMM(tempMatrix, 0, viewTranslationMatrix, 0, viewMatrix, 0);
        System.arraycopy(tempMatrix, 0, viewMatrix, 0, 16);

        // physics
        Vector2D gravity = new Vector2D(0, -0.001);
        ballBody.addForce(gravity);
        ballBody.integrateForces();

        ballBody.collidePolygon(rockBody1, 0.3);
        ballBody.collidePolygon(rockBody2, 0.3);

        // move models
        float[] ballMatrix = new float[16];
        float ballX = (float) ballBody.getPosition().x;
        float ballY = (float) ballBody.getPosition().y;
        Matrix.setIdentityM(ballMatrix, 0);
        Matrix.translateM(ballMatrix, 0, ballX, ballY, 0);
        ball.setMatrix(ballMatrix);

        // draw models
        ball.draw(projMatrix, viewMatrix, lightPos);
        rock1.draw(projMatrix, viewMatrix, lightPos);
        rock2.draw(projMatrix, viewMatrix, lightPos);
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        Matrix.frustumM(
                projMatrix, 0,
                -1, 1,
                -(float) height / width, (float) height / width,
                minDist, maxDist
        );
    }

    public int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);

        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    public int loadShader(int type, InputStream shaderFile) {
        String shaderCode = null;

        try {
            shaderCode = IOUtils.toString(shaderFile, Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return loadShader(type, shaderCode);
    }

    public int loadShaderFromFile(int type, String fileName) {
        try {
            return loadShader(type, context.getAssets().open(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public Bitmap loadImage(String fileName) {
        try {
            Bitmap tmp = BitmapFactory.decodeStream(context.getAssets().open(fileName));
            android.graphics.Matrix matrix = new android.graphics.Matrix();
            matrix.preScale(1.0f, -1.0f);
            Bitmap image = Bitmap.createBitmap(tmp, 0, 0, tmp.getWidth(), tmp.getHeight(), matrix, true);
            tmp.recycle();
            return image;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Vector2D[] floatsToVectors(float[][] arr) {
        Vector2D[] vectors = new Vector2D[arr.length];

        for (int i = 0; i < vectors.length; i++) {
            vectors[i] = new Vector2D((double) arr[i][0], (double) arr[i][1]);
        }

        return vectors;
    }
}
