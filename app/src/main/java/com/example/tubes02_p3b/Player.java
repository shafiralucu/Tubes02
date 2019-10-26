package com.example.tubes02_p3b;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

public class Player {
    //Hitbox player
    RectF rect;

    //Bitmap untuk player ship
    private Bitmap bitmap;

    //Panjang dan tinggi pesawat
    private float panjang;
    private float tinggi;

    //Koordinat sisi kiri pesawat
    private float kiri;

    //Koordinat sisi atas pesawat
    private float atas;

    //Kecepatan gerak pesawat
    private float kecepatan;

    //Kode gerak pesawat
    public final int STOP = 0;
    public final int LEFT = 1;
    public final int RIGHT = 2;

    //State pergerakan pesawat
    private int shipMoving = STOP;

    public Player(Context context, int screenX, int screenY){
        this.rect = new RectF();
        this.panjang = screenX/10;
        this.tinggi = screenY/10;
        this.kiri = screenX / 2;
        this.atas = screenY - 20;
        this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.spaceship);
        this.bitmap = Bitmap.createScaledBitmap(this.bitmap,
                (int) (this.panjang),
                (int) (this.tinggi),
                false);
        this.kecepatan = 350;
    }

    public RectF getRect(){
        return rect;
    }

    public Bitmap getBitmap(){
        return bitmap;
    }

    public float getKiri(){
        return this.kiri;
    }

    public float getPanjang(){
        return this.panjang;
    }

    public float getAtas() {
        return this.atas;
    }

    public float getTinggi() {
        return this.tinggi;
    }

    public float getKecepatan() {
        return this.kecepatan;
    }

    //Mengubah state pergerakan pesawat
    public void setState(int state){
        this.shipMoving = state;
    }

    //Mengupdate koordinat pesawat
    public void update(long fps){
        if(this.shipMoving == this.LEFT){
            this.kiri = this.kiri - this.kecepatan / fps;
            if(this.kiri < 0) {
                this.kiri = 0;
            }
        }

        if(shipMoving == RIGHT){
            this.kiri = this.kiri + this.kecepatan / fps;
            if(this.kiri + this.panjang > this.panjang * 10) {
                this.kiri = this.panjang * 9;
            }
        }
        rect.left = this.kiri;
        rect.right = this.kiri + this.panjang;
    }

}