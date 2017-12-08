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

/**
 * Classe CreditActivity : Cette classe permet d'afficher les crédits de l'application sous forme de textView
 */
public class CreditActivity extends Activity
{
    TextView creditText; // Texte affichant les crédit de l'application

    /**
     * Fonction onCreate : Appel les fonction d'affichage des credits
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.credit);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        printCredit();
        printBackButton();
    }


    /**
    * Cette fonction affiche les crédits sous forme d'un TextView
    */
    public void printCredit()
    {
        creditText = (TextView)findViewById(R.id.creditText);
        creditText.setTextColor(Color.WHITE);
        creditText.setTextSize((MenuActivity.screenHeight / MenuActivity.screenWidth) * 27);

        creditText.setText("CREDITS\n\n" +"" +
                           "Application developped by :\n\n" +
                           "Yanis Mokeddem\n" +
                           "&\n" +
                           "Mohamed Nehari");

        Shader textShader=new LinearGradient(0, 0, creditText.getTextSize()*2, 0,
                new int[]{
                        getResources().getColor(R.color.white),
                        getResources().getColor(R.color.indigo),
                        getResources().getColor(R.color.blue),
                        getResources().getColor(R.color.green),
                        getResources().getColor(R.color.yellow),
                        getResources().getColor(R.color.orange),
                        getResources().getColor(R.color.red)},
                new float[]{0,0.2f,0.4f,0.6f,0.8f,0.9f,1}, Shader.TileMode.REPEAT);
        creditText.getPaint().setShader(textShader);
    }

    /**
     * Cette fonction affiche le bouton de retour au menu
     */
    public void printBackButton()
    {
        Button back = (Button)findViewById(R.id.back);
        back.setAlpha(0.70f);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(MainActivity.muteSound == false)
                    MainActivity.buttonSound.start();
                Intent myIntent = new Intent(v.getContext(), MenuActivity.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(myIntent);
            }
        });
    }
}
