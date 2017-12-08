package com.colormatch_paris8.colormatch;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

/**
 * Activitée MenuActivity : cette activité est liée à la view "MenuView" et permet de créer l'activité du Menu
 */
public class MenuActivity extends Activity
{
    ImageButton play; //Bouton pour lancer la partie
    ImageButton score; //Boutton pour afficher les scores
    ImageButton resume; //Texte pour continuer la partie en cours, s'affiche lorsque une partie a ét énon terminée
    ImageButton yes; //Bouton Yes (pour continuer la aprtie en cours)
    ImageButton no; // Bouton No (pour recommerncer une partie clean)
    ImageButton credit; // Bouton pour accéder au credit du jeux
    Button sound; // Boutton pour activer ou désactiver le son
    Button exit; // Boutton pour quitter l'application
    boolean resumeTextAffich = false; //Boolean pour savoir si le texte d'affichage de la reprise de la partie est affiché ou non
    static int screenWidth; //Largeur de l'écran
    static int screenHeight; //Hauteur de l'écran


    /**
     * Fonction onCreate : Appel les fonction d'affichage des boutons et du logo
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getScreenSize();
        printLogo();
        printPlayButton();
        printScoreButton();
        printCreditButton();
        printMuteSoundButton();
        printExitButton();
    }

    /**
     * Fonction qui récupère les dimensions de l'écran
     */
    public void getScreenSize()
    {
        this.screenWidth = getResources().getDisplayMetrics().widthPixels;
        this.screenHeight = getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * Fonction qui affiche le logo du jeux
     */
    public void printLogo()
    {
        ImageView logo = (ImageView)findViewById(R.id.logoImage);
        Bitmap image = BitmapFactory.decodeResource(this.getResources(),R.drawable.logo);
        logo.setImageBitmap(image);
        logo.setY(-image.getHeight()/4);
    }

    /**
     * Fonction qui affiche le bouton "Play"
     */
    public void printPlayButton()
    {

        //Récupération de l'image du bouton, et gestion de sa taille et de sa position sur l'écran
        play = (ImageButton) findViewById(R.id.playButton);
        Bitmap image = BitmapFactory.decodeResource(this.getResources(),R.drawable.playbutton);
        image = Bitmap.createScaledBitmap(image, this.screenWidth /2, this.screenHeight/12, true);
        play.setImageBitmap(image);


        (findViewById(R.id.playButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(MainActivity.muteSound == false)
                    MainActivity.buttonSound.start();

                //Si on clique sur "Jouer" et qu'une partie était non terminée, on propose de la continuer ou non
                if(MainActivity.saveData.getResumeGame() == true)
                {
                    printResume();
                    resumeTextAffich = true;
                    play.setVisibility(View.INVISIBLE);
                    score.setVisibility(View.INVISIBLE);
                    credit.setVisibility(View.INVISIBLE);
                }
                //Si aucune partie n'était en cour, on lance une partie clean
                else
                {
                    if(MainActivity.muteSound == false)
                    {
                        MainActivity.buttonSound.start();
                        MainActivity.mainMusic.seekTo(0);
                    }
                    GameActivity.loadSave = false;
                    Intent myIntent = new Intent(v.getContext(), GameActivity.class);
                    startActivity(myIntent);
                }
            }
        });
    }

    /**
     * Fonction qui affiche le bouton "Play"
     */
    public void printResume()
    {
        //Récupération de l'image
        resume = (ImageButton) findViewById(R.id.resume);
        Bitmap image = BitmapFactory.decodeResource(this.getResources(),R.drawable.resume);
        image = Bitmap.createScaledBitmap(image, this.screenWidth , this.screenHeight/4, true);
        resume.setImageBitmap(image);

        //Récupération de l'image "Yes"
        yes = (ImageButton) findViewById(R.id.yes);
        Bitmap image2 = BitmapFactory.decodeResource(this.getResources(),R.drawable.yes);
        image2 = Bitmap.createScaledBitmap(image2, this.screenWidth / 4, this.screenHeight/9, true);
        yes.setX(screenWidth /2 + (screenWidth /8));
        yes.setY(screenHeight - (screenHeight/3));
        yes.setImageBitmap(image2);

        //Récupération de l'image "No"
        no = (ImageButton) findViewById(R.id.no);
        Bitmap image3 = BitmapFactory.decodeResource(this.getResources(),R.drawable.no);
        image3 = Bitmap.createScaledBitmap(image3, this.screenWidth / 4, this.screenHeight/9, true);
        no.setX(screenWidth /2 - image3.getWidth() - (screenWidth /8));
        no.setY(screenHeight - (screenHeight/3));
        no.setImageBitmap(image3);

        //On rend invisible tous les éléments du menu
        resume.setVisibility(View.VISIBLE);
        yes.setVisibility(View.VISIBLE);
        no.setVisibility(View.VISIBLE);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.muteSound == false)
                    MainActivity.buttonSound.start();

                GameActivity.loadSave = true;
                Intent myIntent = new Intent(v.getContext(), GameActivity.class);
                startActivity(myIntent);
                yes.setVisibility(View.INVISIBLE);
                no.setVisibility(View.INVISIBLE);
                resume.setVisibility(View.INVISIBLE);
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.muteSound == false)
                {
                    MainActivity.buttonSound.start();
                    MainActivity.mainMusic.seekTo(0);
                }

                GameActivity.loadSave = false;
                Intent myIntent = new Intent(v.getContext(), GameActivity.class);
                startActivity(myIntent);
                yes.setVisibility(View.INVISIBLE);
                no.setVisibility(View.INVISIBLE);
                resume.setVisibility(View.INVISIBLE);
            }
        });
    }

    /**
     * Fonction qui affiche le bouton "Score"
     */
    public void printScoreButton()
    {

        score = (ImageButton) findViewById(R.id.scoreButton);
        Bitmap image = BitmapFactory.decodeResource(this.getResources(),R.drawable.scorebutton);
        image = Bitmap.createScaledBitmap(image, this.screenWidth /2, this.screenHeight/12, true);

        score.setX(this.screenWidth/2 - image.getWidth()/2);
        score.setY(this.screenHeight/2 + image.getHeight());
        score.setImageBitmap(image);

        this.score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.muteSound == false)
                    MainActivity.buttonSound.start();
                Intent myIntent = new Intent(v.getContext(), ScoreActivity.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(myIntent);
            }
        });
    }

    /**
     * Cette fonction sert à afficher le bouton "crédits"
     */
    public void printCreditButton()
    {

        credit = (ImageButton) findViewById(R.id.credit);
        Bitmap image = BitmapFactory.decodeResource(this.getResources(),R.drawable.creditsbutton);
        image = Bitmap.createScaledBitmap(image, this.screenWidth /2, this.screenHeight/12, true);

        credit.setX(this.screenWidth/2 - image.getWidth()/2);
        credit.setY(this.screenHeight/2 + (image.getHeight()*2) + image.getHeight()/2);
        credit.setImageBitmap(image);

        this.credit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.muteSound == false)
                    MainActivity.buttonSound.start();
                Intent myIntent = new Intent(v.getContext(), CreditActivity.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(myIntent);
            }
        });
    }

    /**
     * Cette fonction sert à afficher le bouton d'activation et désactivation du son
     */
    public void printMuteSoundButton()
    {
        sound = (Button) findViewById(R.id.sound);

        //ON définit l'image de départ
        if(MainActivity.muteSound == false)
            sound.setBackgroundResource(R.drawable.ic_volume_up);
        else
            sound.setBackgroundResource(R.drawable.ic_volume_off);


        //Puis on définit ce qui se passe si on clique sur le boutton
        this.sound.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(MainActivity.muteSound == false)
            {
                MainActivity.muteSound = true;
                sound.setBackgroundResource(R.drawable.ic_volume_off);
            }
            else
            {
                MainActivity.buttonSound.start();
                MainActivity.muteSound = false;
                sound.setBackgroundResource(R.drawable.ic_volume_up);
            }
        }
        });

    }

    /**
     * Fonction qui affiche le bouton de sortie du jeux
     */
    public void printExitButton()
    {
        exit = (Button) findViewById(R.id.exit);

        this.exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(MenuActivity.this)

                        .setMessage("Do you really want to quit ?")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface arg0, int arg1) {
                                if(MainActivity.muteSound == false)
                                    MainActivity.buttonSound.start();
                                finish();
                                System.exit(0);
                            }
                        }).create().show();
            }

        });

    }

    public void onDestroy()
    {
        super.onDestroy();
    }

    public void onPause()
    {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void onBackPressed()
    {
        //Si le texte de propostion de reprise de la partie est affiché, on réaffiche els éléments du menu
        if(resumeTextAffich == true)
        {
            this.resume.setVisibility(View.INVISIBLE);
            this.yes.setVisibility(View.INVISIBLE);
            this.no.setVisibility(View.INVISIBLE);
            this.play.setVisibility(View.VISIBLE);
            this.score.setVisibility(View.VISIBLE);
            this.credit.setVisibility(View.VISIBLE);
            this.resumeTextAffich = false;
        }
        //Si on se trouve sur le menu principal, alors on quitte l'application
        else
        {
            new AlertDialog.Builder(MenuActivity.this)

                    .setMessage("Do you really want to quit ?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface arg0, int arg1) {
                            if(MainActivity.muteSound == false)
                                MainActivity.buttonSound.start();
                            finish();
                            System.exit(0);
                        }
                    }).create().show();
        }
    }


}
