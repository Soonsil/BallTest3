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
import io.github.avantgarde95.balltest.physics2d.CircleBody2D;
import io.github.avantgarde95.balltest.physics2d.CirclePolygonCollision2D;
import io.github.avantgarde95.balltest.physics2d.PolygonBody2D;
import io.github.avantgarde95.balltest.physics2d.Vector2D;

/**
 * Created by avantgarde on 2017-01-20.
 */

public class MyGLRenderer implements GLSurfaceView.Renderer {
    private Context context; // for using Activity.getAssets()

    private int delay = 0;

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
    private CirclePolygonCollision2D ballCollision;

    private final Vector2D initBallVelocity = new Vector2D(0, 0);
    private final Vector2D initBallPosition = new Vector2D(-0.25f, 1.0f);

    int rockCount;
    private Circle[] rocks;
    private PolygonBody2D[] rockBodies;

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
        float ballRadius = 0.1f;
        ball = new Circle(this, ballRadius);

        ballBody = new CircleBody2D(
                1,
                initBallPosition,
                initBallVelocity,
                ballRadius
        );

        // init. rocks
        float[] rockRadiuses = {0.2f, 0.3f, 0.2f};
        int[] rockFanCounts = {20, 10, 15};

        Vector2D[] rockPositions = {
                new Vector2D(-0.2f, 0.2f),
                new Vector2D(-0.1f, -0.3f),
                new Vector2D(0.4f, -0.5f)
        };

        rockCount = rockRadiuses.length;

        rocks = new Circle[rockCount];
        rockBodies = new PolygonBody2D[rockCount];

        for (int i = 0; i < rockCount; i++) {
            rocks[i] = new Circle(this, rockRadiuses[i], rockFanCounts[i]);
            rocks[i].setColor(new float[]{0.0f, 1.0f, 0.0f});

            rockBodies[i] = new PolygonBody2D(
                    1.0f,
                    rockPositions[i],
                    new Vector2D(0, 0),
                    rocks[i].getVertices2D()
            );
        }

        // init. collision
        ballCollision = new CirclePolygonCollision2D(ballBody);

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
        Vector2D gravity = new Vector2D(0.0f, -0.0005f);
        float preserve = 0.0f;

        ballBody.addForce(gravity);
        ballBody.integrateForce(1.0f);

        for (PolygonBody2D rockBody : rockBodies) {
            if (ballCollision.collidePolygon(rockBody, preserve)) {
                break;
            }
        }

        // set matrix from physics
        ball.setMatrix(ballBody.evalMatrix());

        for (int i = 0; i < rockCount; i++) {
            rocks[i].setMatrix(rockBodies[i].evalMatrix());
        }

        // draw models
        ball.draw(projMatrix, viewMatrix, lightPos);

        for (Circle rock : rocks) {
            rock.draw(projMatrix, viewMatrix, lightPos);
        }

        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            // do nothing
        }
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

    public void setDelay(int delay) {
        if (delay < 0) {
            delay = 0;
        }

        this.delay = delay;
    }

    public void initBall() {
        ballBody.setVelocity(initBallVelocity);
        ballBody.setPosition(initBallPosition);
    }
}
