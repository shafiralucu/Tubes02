package com.example.tubes02_p3b;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

public class Projectile {
    //Koordinat projectile
    private float x;
    private float y;

    //Hitbox projectile
    private RectF rect;

    //Bitmap untuk projectile
    private Bitmap bitmap;
    private Bitmap bitmap2;

    //Kode arah projectile
    public final int UP = 0;
    public final int DOWN = 1;

    //State arah projectile
    int heading = -1;

    //Kecepatan projectile
    float kecepatan =  500;

    //Panjang dan lebar projectile
    private int panjang;
    private int lebar;

    //Apakah projectile masih aktif di layar
    private boolean isActive;

    public Projectile(Context context, int screenX) {
        this.panjang = screenX / 20;
        this.lebar = this.panjang;
        this.isActive = false;
        this.rect = new RectF();
        this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.projectile);
        this.bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.flame);
        this.bitmap = Bitmap.createScaledBitmap(this.bitmap,
                (int) (this.panjang),
                (int) (this.lebar),
                false);
        this.bitmap2 = Bitmap.createScaledBitmap(this.bitmap2,
                (int) (this.panjang),
                (int) (this.lebar),
                false);
    }

    public RectF getRect(){
        return this.rect;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public Bitmap getBitmap2() {
        return bitmap2;
    }

    public boolean getStatus(){
        return this.isActive;
    }

    public void setInactive(){
        this.isActive = false;
    }

    //Mengetahui koordinat y projectile
    public float getImpactPoint(){
        if (this.heading == this.DOWN){
            return this.y + this.lebar;
        } else {
            return this.y;
        }
    }

    //Method menembak projectile
    public boolean shoot(float startX, float startY, int direction) {
        if (!isActive) {
            this.x = startX;
            this.y = startY;
            this.heading = direction;
            this.isActive = true;
            return true;
        }else {
            return false;
        }
    }

    //Mengupdate koordinat projectile
    public void update(long fps){
        if(heading == UP){
            this.y = this.y - this.kecepatan / fps;
        }else{
            this.y = this.y + this.kecepatan / fps;
        }
        rect.left = x;
        rect.right = x + this.panjang;
        rect.top = y;
        rect.bottom = y + this.lebar;
    }
}
