package com.example.tubes02_p3b;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

import java.util.Random;

public class Alien {

    RectF square; //kotak diluar alien agar bs detect alien tertembak atau tidak
    private float panjangAlien,  lebarAlien; //panjang dan lebar kumpulan alien
    private float x,y; //x membentuk alien, y koordinat top

    //kecepatan alien bergerak (pixel/s)
    private float alienSpeed;
    final int LEFT = 1;
    final int RIGHT = 2;
    private int alienMove = LEFT;
    int padding;

    //representasi alien
    private Bitmap alien;

    boolean isVisible;
    Random r = new Random();

    public Alien(Context context, int screenX, int screenY, int row, int column) {
        RectF r = new RectF();
        this.alien = BitmapFactory.decodeResource(context.getResources(), R.drawable.monster);
        this.alienSpeed = 50; //kecepatan alien bergerak (kiri-kanan)
        this.panjangAlien = screenX / 25;
        this.lebarAlien = screenY / 25;
        this.padding = screenX/25;
        this.x = column*(lebarAlien+padding);
        this.y = row*(lebarAlien+padding/4);
        this.isVisible = true;
        alien = Bitmap.createScaledBitmap(alien, (int)panjangAlien, (int)lebarAlien, false);
    }
}
