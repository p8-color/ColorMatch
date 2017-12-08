package com.colormatch_paris8.colormatch;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.WindowManager;

public class MainActivity extends Activity
{

    static SharedPreference saveData; //Objet pour la sauvegarde des données du jeux
    static MediaPlayer mainMusic; //Service qui tourne en arrière plan, pour la musique principale du jeux
    static MediaPlayer validShotSound, wrongShotSound, bipTimeSound, gameOverSound, buttonSound; //SoundEffect du jeux
    static Boolean muteSound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        //Création du fichier de sauvegarde
        this.saveData = new SharedPreference(getApplicationContext());

        //Chargement des effets sonore
        loadSoundEffects();
        muteSound = false;

        //Lancement de l'activité du Menu principal
        Intent myIntent = new Intent(this, MenuActivity.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(myIntent);
        MainActivity.this.finish();
    }

    public void loadSoundEffects()
    {
        this.mainMusic = MediaPlayer.create(this, R.raw.mainmusic);
        this.mainMusic.setLooping(true);
        this.mainMusic.setVolume(0.50f, 0.50f);
        this.validShotSound = MediaPlayer.create(this, R.raw.blop);
        this.wrongShotSound = MediaPlayer.create(this, R.raw.wrongbip);
        this.wrongShotSound.setVolume(0.20f,0.20f);
        this.bipTimeSound = MediaPlayer.create(this, R.raw.biptime);
        this.gameOverSound = MediaPlayer.create(this, R.raw.gameover);
        this.buttonSound = MediaPlayer.create(this, R.raw.button);
    }
}
