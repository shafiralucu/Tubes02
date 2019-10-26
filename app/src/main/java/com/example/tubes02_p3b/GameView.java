package com.example.tubes02_p3b;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

public  class GameView extends SurfaceView implements Runnable{

    Context context;
    private Thread gameThread = null;
    private SurfaceHolder ourHolder;

    //Apakah game sedang berjalan atau tidak
    private boolean playing;

    //State dimana game di pause
    private boolean paused = true;

    private Canvas canvas;
    private Paint paint;

    //Atribut frame rate
    private long fps;

    //Attribut menghitung frame rate
    private long timeThisFrame;

    //Ukuran layar
    private int screenX;
    private int screenY;

    //Pesawat pemain
    private Player playerShip;

    //Projectile pemain
    private Projectile projectile;

    //Projectile alien
    private Projectile[] invadersProjectile = new Projectile[200];
    private int nextProjectile;
    private int maxInvaderProjectile = 10;

    //Alien, max 60
    Alien[] invaders = new Alien[60];
    int numInvaders = 0;

    //Untuk sound effects
    private SoundPool soundPool;
    private int playerExplodeID = -1;
    private int invaderExplodeID = -1;
    private int shootID = -1;
    private int damageShelterID = -1;
    private int uhID = -1;
    private int ohID = -1;
    private long menaceInterval = 1000;
    private boolean uhOrOh;
    private long lastMenaceTime = System.currentTimeMillis();

    //Skor
    int score = 0;

    //Hit points
    private int lives = 3;

    public GameView(Context context, int x, int y) {
        super(context);
        this.context = context;
        this.ourHolder = getHolder();
        this.paint = new Paint();
        this.screenX = x;
        this.screenY = y;
        this.soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC,0);
        try {
            AssetManager assetManager = context.getAssets();
            //Load sound effects
            shootID = soundPool.load(assetManager.openFd("shoot.ogg"), 0);
            invaderExplodeID = soundPool.load(assetManager.openFd("invaderexplode.ogg"), 0);
            damageShelterID = soundPool.load(assetManager.openFd("damageshelter.ogg"), 0);
            playerExplodeID = soundPool.load(assetManager.openFd("playerexplode.ogg"), 0);
            damageShelterID = soundPool.load(assetManager.openFd("damageshelter.ogg"), 0);
            uhID = soundPool.load(assetManager.openFd("uh.ogg"), 0);
            ohID = soundPool.load(assetManager.openFd("oh.ogg"), 0);
        } catch(IOException e){
            Log.e("error", "Failed to load sound files");
        }
        prepareLevel();
    }

    //Membuat state awal level
    private void prepareLevel(){
        this.playerShip = new Player(this.context, this.screenX, this.screenY);
        this.projectile = new Projectile(this.context, this.screenY);
        for(int i = 0; i < this.invadersProjectile.length; i++){
            this.invadersProjectile[i] = new Projectile(this.context, this.screenY);
        }
        this.numInvaders = 0;
        for(int column = 0; column < 6; column++){
            for(int row = 0; row < 5; row++){
                this.invaders[numInvaders] = new Alien(this.context, this.screenX, this.screenY, row, column);
                this.numInvaders++;
            }
        }
    }

    @Override
    public void run() {
        while (this.playing) {
            long startFrameTime = System.currentTimeMillis();
            if (!this.paused) {
                update();
            }
            draw();
            this.timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (this.timeThisFrame >= 1) {
                this.fps = 1000 / this.timeThisFrame;
            }
            if(!this.paused) {
                if ((startFrameTime - this.lastMenaceTime) > this.menaceInterval) {
                    if(this.uhOrOh) {
                        this.soundPool.play(uhID, 1, 1, 0, 0, 1);

                    } else {
                        this.soundPool.play(ohID, 1, 1, 0, 0, 1);
                    }
                    this.lastMenaceTime = System.currentTimeMillis();
                    this.uhOrOh = !this.uhOrOh;
                }
            }
        }
    }

    private void update(){
        boolean collision = false;
        boolean lost = false;
        this.playerShip.update(this.fps);
        for(int i = 0; i < this.numInvaders; i++){
            if(this.invaders[i].getVisible()) {
                this.invaders[i].update(this.fps);
                if(this.invaders[i].takeAim(this.playerShip.getKiri(), this.playerShip.getPanjang())){
                    if(invadersProjectile[this.nextProjectile].shoot(this.invaders[i].getX() + this.invaders[i].getPanjangAlien() / 2, invaders[i].getY(), this.projectile.DOWN)) {
                        this.nextProjectile++;
                        if (this.nextProjectile == this.maxInvaderProjectile) {
                            this.nextProjectile = 0;
                        }
                    }
                }
                if (invaders[i].getX() > screenX - invaders[i].getPanjangAlien() || invaders[i].getX() < 0){
                    collision = true;
                }
            }
        }
        for(int i = 0; i < this.invadersProjectile.length; i++){
            if(this.invadersProjectile[i].getStatus()) {
                this.invadersProjectile[i].update(fps);
            }
        }
        if(this.projectile.getStatus()){
            this.projectile.update(fps);
        }
        if(this.projectile.getImpactPoint() < 0){
            this.projectile.setInactive();
        }
        for(int i = 0; i < this.invadersProjectile.length; i++){
            if(this.invadersProjectile[i].getImpactPoint() > screenY){
                this.invadersProjectile[i].setInactive();
            }
        }
        if(this.projectile.getStatus()) {
            for (int i = 0; i < this.numInvaders; i++) {
                if (this.invaders[i].getVisible()) {
                    if (RectF.intersects(this.projectile.getRect(), this.invaders[i].getRect())) {
                        this.invaders[i].setVisible();
                        this.soundPool.play(invaderExplodeID, 1, 1, 0, 0, 1);
                        this.projectile.setInactive();
                        this.score = this.score + 10;
                        if (this.score == this.numInvaders * 10) {
                            this.paused = true;
                            this.score = 0;
                            this.lives = 3;
                            prepareLevel();
                        }
                    }
                }
            }
        }
        for(int i = 0; i < this.invadersProjectile.length; i++) {
            if (this.invadersProjectile[i].getStatus()) {
                if (RectF.intersects(this.playerShip.getRect(), this.invadersProjectile[i].getRect())) {
                    this.invadersProjectile[i].setInactive();
                    this.lives--;
                    this.soundPool.play(playerExplodeID, 1, 1, 0, 0, 1);
                    if (this.lives == 0) {
                        this.paused = true;
                        this.lives = 3;
                        this.score = 0;
                        prepareLevel();

                    }
                }
            }
        }
        if(collision){
            for(int i = 0; i < this.numInvaders; i++){
                this.invaders[i].dropDownAndReverse();
                if(this.invaders[i].getY() + this.invaders[i].getTinggiAlien() >= this.screenY - this.screenY / 10){
                    lost = true;
                }
            }
            this.menaceInterval = this.menaceInterval - 80;
        }
        if(lost){
            prepareLevel();
        }
    }

    private void draw(){
        if (this.ourHolder.getSurface().isValid()) {
            this.canvas = this.ourHolder.lockCanvas();
            this.canvas.drawColor(Color.argb(255, 26, 128, 182));
            this.paint.setColor(Color.argb(255,  255, 255, 255));
            this.canvas.drawBitmap(this.playerShip.getBitmap(), this.playerShip.getKiri(), this.screenY - this.playerShip.getTinggi(), this.paint);
            for(int i = 0; i < this.numInvaders; i++){
                if(this.invaders[i].getVisible()) {
                    if(this.uhOrOh) {
                        this.canvas.drawBitmap(this.invaders[i].getAlien(), this.invaders[i].getX(), this.invaders[i].getY(), this.paint);
                    }else{
                        this.canvas.drawBitmap(this.invaders[i].getAlien2(), this.invaders[i].getX(), this.invaders[i].getY(), this.paint);
                    }
                }
            }
            if(this.projectile.getStatus()){
                this.canvas.drawBitmap(this.projectile.getBitmap(), this.projectile.getX(), this.projectile.getY(), this.paint);
            }
            for(int i = 0; i < this.invadersProjectile.length; i++){
                if(this.invadersProjectile[i].getStatus()) {
                    this.canvas.drawBitmap(this.invadersProjectile[i].getBitmap2(), this.invadersProjectile[i].getX(), this.invadersProjectile[i].getY(), this.paint);
                }
            }
            this.paint.setColor(Color.argb(255,  249, 129, 0));
            this.paint.setTextSize(40);
            this.canvas.drawText("Score: " + this.score + "   Lives: " + this.lives, 10,50, this.paint);
            this.ourHolder.unlockCanvasAndPost(this.canvas);
        }
    }
    public void pause() {
        this.playing = false;
        try {
            this.gameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }

    }

    // If SpaceInvadersActivity is started then
    // start our thread.
    public void resume() {
        this.playing = true;
        this.gameThread = new Thread(this);
        this.gameThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                this.paused = false;
                if(motionEvent.getY() > this.screenY - this.screenY / 8) {
                    if (motionEvent.getX() > this.screenX / 2) {
                        this.playerShip.setState(this.playerShip.RIGHT);
                    } else {
                        playerShip.setState(this.playerShip.LEFT);
                    }
                }
                if(motionEvent.getY() < screenY - screenY / 8) {
                    this.projectile.shoot(playerShip.getKiri() + playerShip.getPanjang()/2, this.screenY, this.projectile.UP);
                    this.soundPool.play(this.shootID, 1, 1, 0, 0, 1);
                    this.canvas.drawBitmap(this.projectile.getBitmap(), this.projectile.getX(), this.projectile.getY(), this.paint);
                }
                break;
            case MotionEvent.ACTION_UP:
                if(motionEvent.getY() > this.screenY - this.screenY / 10) {
                    this.playerShip.setState(this.playerShip.STOP);
                }
                break;
        }
        return true;
    }
}