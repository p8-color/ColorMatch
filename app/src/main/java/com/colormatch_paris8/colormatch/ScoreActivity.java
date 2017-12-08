package com.colormatch_paris8.colormatch;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class ScoreActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.score);

        printScore();
        clearData();
    }

    /**
     * Cette fonction récupère le meilleur score ainsi que le dernier score du joueur, si ils éxistent et les affiche
     */
    public void printScore()
    {
        TextView resultTextScore = (TextView)findViewById(R.id.resultHighScoreText);
        int score = MainActivity.saveData.getScore();
        int bestScore = MainActivity.saveData.getHighScore();

        resultTextScore.setTextColor(Color.WHITE);
        resultTextScore.setTextSize((MenuActivity.screenHeight / MenuActivity.screenWidth) * 33);

        if(bestScore == -1)
            resultTextScore.setText("Your don't have any score yet )=");
        else
            resultTextScore.setText("Best score : " + String.valueOf(bestScore) + "\n\nLast score : " + score);
    }

    /**
     * Cette fonction réinitialise les données sauvegardées, si l'utilisateur appuie sur le bouton "clear"
     */
    public void clearData()
    {
        Button clear = (Button)findViewById(R.id.clear);
        clear.setAlpha(0.70f);

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ScoreActivity.this)

                        .setMessage("All your progress will be cleard !\n\nDo you want to continue ?")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface arg0, int arg1) {
                                if(MainActivity.muteSound == false)
                                    MainActivity.buttonSound.start();
                                MainActivity.saveData.clearData();
                                Toast.makeText(ScoreActivity.this, "Data has been cleared successfully", Toast.LENGTH_SHORT).show();
                                printScore();
                            }
                        }).create().show();
            }
        });
    }

    public void onDestroy()
    {
        super.onDestroy();
        //stopService(new Intent(ScoreActivity.this,BackgroundMusic.class));
    }

    public void onPause()
    {
        super.onPause();
        //MainActivity.mServ.pauseMusic();
        //stopService(new Intent(ScoreActivity.this,BackgroundMusic.class));

    }

    @Override
    protected void onResume() {
        super.onResume();
        //MainActivity.mServ.resumeMusic();
        //startService(new Intent(ScoreActivity.this,BackgroundMusic.class));
    }

}
