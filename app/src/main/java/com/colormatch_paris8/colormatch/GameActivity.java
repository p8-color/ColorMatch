package com.colormatch_paris8.colormatch;

import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

/**
 * Activitée du jeux : cette activité est liée à la view "GameView" et permet de créer l'activité du jeux
 */
public class GameActivity extends AppCompatActivity {

    GameView game; // Variable instanciant la View du jeux
    static boolean loadSave = false; // Boolean indiquant si la partie doit être chargée (si partie en cours) ou non
    Boolean dialogAlertExitGame = false; // Boolean indiquat si une fenêtre d'alerte est affichée (lorsque le joueur souhaite quitter la partie)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Initialisation du jeux
        game = (GameView)findViewById(R.id.gameView);
        if(loadSave == true)
            game.resumeGame();

        //Animation de l'arriere plan
        backgroundAnimation();

        game.setVisibility(View.VISIBLE);

    }

    /**
     * Cette fonction gère l'animation de l'arrière plan.
     * Deux images sont récupérée dans game.xml, et on enchaine les deux images une derière l'autre, les images se répétent, cela créé une animation
     */
    public void backgroundAnimation()
    {
        final ImageView backgroundOne = (ImageView) findViewById(R.id.firstImageAnimation);
        final ImageView backgroundTwo = (ImageView) findViewById(R.id.secondImageAnimation);

        ValueAnimator animator = ValueAnimator.ofFloat(0.0f, 1.0f);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(10000L);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final float progress = (float) animation.getAnimatedValue();
                final float width = backgroundOne.getWidth();
                final float translationX = width * progress;
                backgroundOne.setTranslationX(translationX);
                backgroundTwo.setTranslationX(translationX - width);
            }
        });
        animator.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("InfoActivity", "Déstruction de l'activité");

        if(MainActivity.muteSound == false)
        {
            MainActivity.mainMusic.pause();
        }
        this.game.threadAlive = false;
    }


    @Override
    protected void onPause()
    {
        super.onPause();

        if(MainActivity.muteSound == false)
        {
            MainActivity.mainMusic.pause();
        }
        game.saveGame();
        Log.d("InfoActivity", "Mise en pause de l'activité");
        this.game.playGame = false;
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Log.d("InfoActivity", "Reprise de l'activité");

        if(MainActivity.muteSound == false)
        {
            MainActivity.mainMusic.start();
        }

        if(dialogAlertExitGame == false)
            this.game.playGame = true;
    }

    /**
     * On specifie qu'il faut revenir au menu dans le cas ou l'activité du resultat s'affiche et que le joueur rejoue, et qu'il appuie sur back,
     * il ne retombera pas sur l'écran d'activité du résultat mais directement sur le menu.
     */
    public void onBackPressed()
    {
        dialogAlertExitGame = true;
        game.playGame = false;

        new AlertDialog.Builder(this)

                .setMessage("Are you sure you want to leave ?\n\nYour progression will be saved")
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener()
                {

                    public void onClick(DialogInterface arg0, int arg1)
                    {
                        game.playGame = true;
                        dialogAlertExitGame = false;
                    }
                })

                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent myIntent = new Intent(getApplicationContext(), MenuActivity.class);
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        dialogAlertExitGame = false;
                        game.saveGame();
                        game.playGame = false;
                        startActivity(myIntent);
                        GameActivity.super.onBackPressed();
                    }
                }).create().show();
    }
}
