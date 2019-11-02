package com.example.tubes02_p3b;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import androidx.core.content.res.ResourcesCompat;


//Kelas untuk menu utama, belum berhasil diimplementasikan
public class MainMenu {
    private GameView gameView;
    private Paint welcomeMsg = new Paint();
    private Paint detailsMsg = new Paint();
    private Typeface t = Typeface.create("Helvetica",  Typeface.BOLD);
    private int x,y;

    public MainMenu (GameView view) {
        this.gameView = gameView;
        welcomeMsg.setTypeface(t);
        welcomeMsg.setColor(Color.MAGENTA);
        welcomeMsg.setTextSize(40);
        welcomeMsg.setTextAlign(Paint.Align.CENTER);
        x = gameView.getWidth()/2;
        y = gameView.getHeight()/6;
        detailsMsg.setColor(Color.BLUE);
        detailsMsg.setTextAlign(Paint.Align.CENTER);
        detailsMsg.setTextSize(20);
    }

    public void draw(Canvas canvas) {
        canvas.drawText("Galaga",x,y,welcomeMsg);
        canvas.drawText("Tap to start",x,(int)(5.5*y),welcomeMsg);
        canvas.drawText("Tugas Besar P3B",x,gameView.getHeight()/2,detailsMsg);
    }
}


