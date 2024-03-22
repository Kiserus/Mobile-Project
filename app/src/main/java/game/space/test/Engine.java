package game.space.test;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Rect;
import android.icu.text.Edits;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class Engine {

    SurfaceHolder.Callback callback;
    SurfaceHolder surfaceHolder;
    Context context;
    ImageView spaceshipImageView;

    boolean stopped = false;
    long time = 0;
    long lastTime = 0;
    double asteroidsPerTime = 100;

    ArrayList<Asteroid> asteroidArrayList = new ArrayList<>();
    ArrayList<Asteroid> bulletArrayList = new ArrayList<>();
    Thread DrawThread = new Thread() {
        @Override
        public void run() {
            while (!stopped) {
                Canvas canvas;
                if (surfaceHolder == null || (canvas = surfaceHolder.lockCanvas()) == null) {
                    synchronized (Engine.this) {
                        try {
                            Engine.this.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    update(canvas);

                    time = System.nanoTime();
                    if (time - lastTime >= 1e7 * asteroidsPerTime) { // раз в n создаём новый астероид
                        lastTime = time;
                        String TAG = "TIME";
                        Log.i(TAG, lastTime + " " + time);
                        int diametr = new Random().nextInt(800) + 50;
                        asteroidArrayList.add(new Asteroid(diametr, new Random().nextInt(canvas.getWidth() + diametr) - diametr, context));
                    }
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    };

    void update(Canvas canvas) {
        Paint paint = new Paint();
        Bitmap background = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.space), canvas.getWidth(), canvas.getHeight(), true);
        canvas.drawBitmap(background, 0, 0, paint);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);

        /*
        Iterator <Asteroid> iterator = asteroidArrayList.iterator();
        while (iterator.hasNext()) {
            Asteroid asteroid = iterator.next();
            if (asteroid.y >= canvas.getHeight()) {
                iterator.remove();
            } else {
                canvas.drawBitmap(asteroid.bitmap, asteroid.x, asteroid.y, paint);
                asteroid.y += asteroid.speed;
            }
        }
        */

        for (int i = 0; i < asteroidArrayList.size(); ++i) {
            if (asteroidArrayList.get(i).y >= canvas.getHeight() - 200) {
                asteroidArrayList.remove(i);
                i--;
            } else {
                canvas.drawBitmap(asteroidArrayList.get(i).bitmap, asteroidArrayList.get(i).x, asteroidArrayList.get(i).y, paint);
                asteroidArrayList.get(i).y += asteroidArrayList.get(i).speed;
            }
        }
    }

    Engine(SurfaceView surfaceView, Context context, ImageView spaceshipImageView) {
        this.context = context;
        this.spaceshipImageView = spaceshipImageView;

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        RelativeLayout.LayoutParams startingPos = (RelativeLayout.LayoutParams) spaceshipImageView.getLayoutParams();
        startingPos.topMargin = (int) (height * 0.8);
        startingPos.leftMargin = width / 2 - 70;
        spaceshipImageView.setLayoutParams(startingPos);

        DrawThread.start();
        callback = new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                Engine.this.surfaceHolder = holder;
                synchronized (Engine.this) {
                    Engine.this.notifyAll();
                }
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
                Engine.this.surfaceHolder = holder;
            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                Engine.this.surfaceHolder = null;
            }
        };

        surfaceView.getHolder().addCallback(callback);
    }

    void stop() {
        this.stopped = true;
    }
}
