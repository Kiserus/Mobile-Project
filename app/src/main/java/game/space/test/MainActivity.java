package game.space.test;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    SurfaceView surfaceView;
    ViewGroup mainLayout;
    Engine engine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Window w = getWindow();
        w.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY); // фиксируем ориентацию экрана
        mainLayout = findViewById(R.id.main);

        surfaceView = findViewById(R.id.surface_view);
        ImageView spaceshipImageView = findViewById(R.id.spaceship);
        spaceshipImageView.setOnTouchListener(onTouchListener());

        engine = new Engine(surfaceView, this, spaceshipImageView);

    }

    private View.OnTouchListener onTouchListener() {
        return new View.OnTouchListener() {

            int xDelta;
            int yDelta;
            int xyz;

            @Override
            public boolean onTouch(View view, MotionEvent event) {

                final int x = (int) event.getRawX();
                final int y = (int) event.getRawY();

                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                        xDelta = x - lParams.leftMargin;
                        yDelta = y - lParams.topMargin;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (x - xDelta + view.getWidth() <= mainLayout.getWidth() // исключаем случай выхода за границы экрана изображения
                                && y - yDelta + view.getHeight() <= mainLayout.getHeight()
                                && x - xDelta >= 0
                                && y - yDelta >= 0) {
                            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                            layoutParams.leftMargin = x - xDelta;
                            layoutParams.topMargin = y - yDelta;
                            layoutParams.rightMargin = 0;
                            layoutParams.bottomMargin = 0;
                            view.setLayoutParams(layoutParams);
                        }
                        break;
                }
                mainLayout.invalidate();
                return true;
            }
        };
    }

    @Override
    protected void onDestroy() {
        engine.stop();
        super.onDestroy();
    }
}