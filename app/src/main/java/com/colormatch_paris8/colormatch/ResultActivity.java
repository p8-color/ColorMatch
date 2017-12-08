package com.colormatch_paris8.colormatch;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends Activity
{

    static boolean isGameBlocked = false; //Boolean qui indique si la partie a été quité car le jeux était bloqué ou non

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.result);
        printResult();
        getChoice();
    }

    /**
     * Cette fonction affiche le score de la partie, et propose au joueurs de rejouer une partie, ou de retrouerner au menu principal
     */
    public void printResult()
    {
        TextView resultTextScore = (TextView)findViewById(R.id.resultScoreText);
        int score = MainActivity.saveData.getScore();
        int bestScore = MainActivity.saveData.getHighScore();

        resultTextScore.setTextColor(Color.WHITE);
        resultTextScore.setTextSize((MenuActivity.screenHeight / MenuActivity.screenWidth) * 27);

        if(isGameBlocked == true)
            resultTextScore.setText("There is no more possibilities \n\nYour score is : " + String.valueOf(score) +
                                    "\n\nDo you want to replay ?");
        else if(score < bestScore)
            resultTextScore.setText("The game is over !\n\nYour score is : " + String.valueOf(score) +
                                    "\nTry to do better )= \n\nDo you want to replay ?");
        else
            resultTextScore.setText("The game is over !\n\nYour score is : " + String.valueOf(score) +
                    "\nCongratulation, you made a new High score =) \n\n Do you want to replay ?");

        Shader textShader=new LinearGradient(0, 0, resultTextScore.getTextSize()*2, 0,
                new int[]{
                        getResources().getColor(R.color.white),
                        getResources().getColor(R.color.indigo),
                        getResources().getColor(R.color.blue),
                        getResources().getColor(R.color.green),
                        getResources().getColor(R.color.yellow),
                        getResources().getColor(R.color.orange),
                        getResources().getColor(R.color.red)},
                new float[]{0,0.2f,0.4f,0.6f,0.8f,0.9f,1}, Shader.TileMode.REPEAT);
        resultTextScore.getPaint().setShader(textShader);
    }

    /**
     * Cette fonction récupère le choix de l'utilsateur et agis en fonction de son choix : recommencer une partie ou repartir au menu
     */
    public void getChoice()
    {
        Button replay = (Button)findViewById(R.id.replay);
        Button goToMenu = (Button)findViewById(R.id.goToMenu);
        replay.setAlpha(0.70f);
        goToMenu.setAlpha(0.70f);

        replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(MainActivity.muteSound == false)
                {
                    MainActivity.buttonSound.start();
                    MainActivity.mainMusic.seekTo(0);
                }
                GameActivity.loadSave = false;
                //Toast.makeText(getApplicationContext(), " " + Boolean.toString(MainActivity.saveData.getResumeGame()) , Toast.LENGTH_SHORT).show();
                Intent myIntent = new Intent(getApplicationContext(), GameActivity.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(myIntent);
            }
        });

        goToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(MainActivity.muteSound == false)
                    MainActivity.buttonSound.start();
                GameActivity.loadSave = false;
                Intent myIntent = new Intent(getApplicationContext(), MenuActivity.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(myIntent);
            }
        });
    }

    public void onBackPressed()
    {
        Intent myIntent = new Intent(this, MenuActivity.class);
        startActivity(myIntent);
    }
}
