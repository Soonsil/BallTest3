package io.github.avantgarde95.balltest.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import io.github.avantgarde95.balltest.R;
import io.github.avantgarde95.balltest.view.MyGLSurfaceView;

public class MainActivity extends AppCompatActivity {
    private MyGLSurfaceView boardView;
    private Button resetButton;
    private Button quitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boardView = (MyGLSurfaceView) findViewById(R.id.boardView);
        resetButton = (Button) findViewById(R.id.resetButton);
        quitButton = (Button) findViewById(R.id.quitButton);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onResetButtonPressed();
            }
        });

        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onQuitButtonPressed();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        boardView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        boardView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void onResetButtonPressed() {

    }

    private void onQuitButtonPressed() {
        this.finishAffinity();
    }
}
