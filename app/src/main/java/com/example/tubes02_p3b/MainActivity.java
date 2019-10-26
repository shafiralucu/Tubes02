package com.example.tubes02_p3b;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;

// SpaceInvadersActivity is the entry point to the game.
// It will handle the lifecycle of the game by calling
// methods of spaceInvadersView when prompted to so by the OS.
public class MainActivity extends Activity {

    // spaceInvadersView will be the view of the game
    // It will also hold the logic of the game
    // and respond to screen touches as well
    GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        this.gameView = new GameView(this, size.x, size.y);
        setContentView(this.gameView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.gameView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.gameView.pause();
    }
}