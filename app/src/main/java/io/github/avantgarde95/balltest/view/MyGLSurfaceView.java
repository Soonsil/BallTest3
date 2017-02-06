package io.github.avantgarde95.balltest.view;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.AttributeSet;
import android.view.MotionEvent;

import io.github.avantgarde95.balltest.renderer.MyGLRenderer;

/**
 * Created by avantgarde on 2017-01-20.
 */

public class MyGLSurfaceView extends GLSurfaceView {
    private MyGLRenderer renderer;

    private float prevX;
    private float prevY;

    public MyGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(2);

        renderer = new MyGLRenderer(context);
        setRenderer(renderer);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int count = event.getPointerCount();
        int action = event.getAction();

        float x = event.getX();
        float y = event.getY();

        float dx = Math.max(Math.min(x - prevX, 10.0f), -10.0f);
        float dy = Math.max(Math.min(y - prevY, 10.0f), -10.0f);

        if (action == MotionEvent.ACTION_MOVE && count == 1) {
            float[] tempMatrix = new float[16];
            float[] temp2Matrix = new float[16];

            Matrix.setIdentityM(tempMatrix, 0);
            Matrix.rotateM(tempMatrix, 0, dx, 0, 1, 0);
            Matrix.rotateM(tempMatrix, 0, dy, 1, 0, 0);
            Matrix.multiplyMM(temp2Matrix, 0, tempMatrix, 0, renderer.viewRotationMatrix, 0);
            System.arraycopy(temp2Matrix, 0, renderer.viewRotationMatrix, 0, 16);

            requestRender();
        }

        prevX = x;
        prevY = y;

        return true;
    }
}
