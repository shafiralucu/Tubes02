package com.example.tubes02_p3b;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

import java.util.Random;

public class Alien {

    RectF square; //kotak diluar alien agar bs detect alien tertembak atau tidak
    private float panjangAlien,  tinggiAlien; //panjang dan lebar kumpulan alien
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
        this.tinggiAlien = screenY / 25;
        this.padding = screenX/25;
        this.x = column*(panjangAlien+padding);
        this.y = row*(panjangAlien+padding/4);
        this.isVisible = true;
        alien = Bitmap.createScaledBitmap(alien, (int)panjangAlien, (int)tinggiAlien, false);
    }

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }

    public float getPanjangAlien(){
        return panjangAlien;
    }

    public Bitmap getAlien() {
        return alien;
    }

    public void setVisible() {
        isVisible = false;
    }

    public boolean getVisible() {
        return isVisible;
    }

    public RectF getRect(){
        return square;
    }

    public void dropDownAndReverse() {
        if (alienMove == RIGHT ) {
            alienMove = LEFT;
        } else {
            alienMove = RIGHT;
        }

        y = y + tinggiAlien;
        alienSpeed = alienSpeed * 2.18f;
    }

    public void update(long fps){
        if(alienMove == LEFT){
            x = x - alienSpeed / fps;
        }

        if(alienMove == RIGHT){
            x = x + alienSpeed / fps;
        }
        square.top = y;
        square.bottom = y + tinggiAlien;
        square.left = x;
        square.right = x + panjangAlien;
    }

    public boolean takeAim(float playerShipX, float playerShipLength){

        int randomNumber = -1;
        if((playerShipX + playerShipLength > x &&
                playerShipX + playerShipLength < x + panjangAlien) || (playerShipX > x && playerShipX < x + panjangAlien)) {
            randomNumber = r.nextInt(150);
            if(randomNumber == 0) {
                return true;
            }
        }
        randomNumber = r.nextInt(2000);
        if(randomNumber == 0){
            return true;
        }
        return false;
    }
}
