package com.colormatch_paris8.colormatch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Test Débugage
        Grille grille = new Grille(10,14);
        grille.printDataColor();
    }
}
