package io.github.avantgarde95.balltest.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import io.github.avantgarde95.balltest.R;
import io.github.avantgarde95.balltest.view.MyGLSurfaceView;

public class MainActivity extends AppCompatActivity {
    private MyGLSurfaceView boardView;
    private SeekBar delayBar;
    private Button resetButton;
    private Button quitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boardView = (MyGLSurfaceView) findViewById(R.id.boardView);
        delayBar = (SeekBar) findViewById(R.id.delayBar);
        resetButton = (Button) findViewById(R.id.resetButton);
        quitButton = (Button) findViewById(R.id.quitButton);

        delayBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                onDelayChanged();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // do nothing
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // do nothing
            }
        });

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

    private void onDelayChanged() {
        boardView.delayRenderer(delayBar.getProgress());
    }

    private void onResetButtonPressed() {
        boardView.resetRenderer();
    }

    private void onQuitButtonPressed() {
        this.finishAffinity();
    }
}
