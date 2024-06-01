package game.space.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

public class Bullet {

    Bitmap bitmap;

    int diametr = 50;
    int x;
    int y;
    int speed = 30;

    Bullet(int x, int y, Context context) {
        this.diametr = diametr;
        this.x = x;
        this.y = y;
        bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.bullet), diametr, diametr, true);
    }
}
