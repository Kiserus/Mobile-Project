package game.space.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

public class Bullet {

    Bitmap bitmap;

    int diametr;
    int x;
    int y;
    int speed = new Random().nextInt(7) + 2;

    Bullet(int diametr, int x, Context context) {
        this.diametr = diametr;
        this.x = x;
        this.y = -diametr;
        bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.asteroid_common), diametr, diametr, true);
    }
}
