package com.colormatch_paris8.colormatch;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;

import java.util.Random;

/**
 * Created by Mohamed on 10/11/2017.
 */

public class Grille extends AppCompatActivity {

    Case data[][];
    int height;
    int width;
    int numberEmptyCase;

    // Constructeur qui initialise une grille aux dimentions passés en paramètre, avec des cases vides et colorées
    public Grille(int w, int h)
    {
        this.height = h;
        this.width = w;
        this.numberEmptyCase = 20;
        data = new Case[this.height][this.width];
        Random random = new Random();

        //récupération de la taille d'une case en fonction de la taille de l'écran
        Display display = getWindowManager().getDefaultDisplay();
        int screenWidth = display.getWidth();
        int screenWeight = display.getHeight();
        float caseSize = screenWidth / w;

        // Initialisation des cases vides
        for(int i=0 ; i<this.numberEmptyCase ; i++)
        {
            int emptyCaseI = random.nextInt(this.height);
            int emptyCaseJ = random.nextInt(this.width);
            this.data[emptyCaseI][emptyCaseJ] = new Case(false, caseSize);
        }

        // Initialisation des cases colorées
        for(int i=0 ; i<h ; i++)
        {
            for(int j=0 ; j<w ; j++)
            {
                if(this.data[i][j] == null)
                    this.data[i][j] = new Case(true, caseSize);
            }
        }
    }

    // Fonction qui retourne la hauteur de la grille
    public int getHeight()
    {
        return this.height;
    }

    // Fonction qui retourne la longueur de la grille
    public int getWidth()
    {
        return this.width;
    }

    // Fonction qui affiche les données de la grille dans la console
    public void printData()
    {
        for(int i=0 ; i<this.height ; i++)
        {
            for(int j=0 ; j<this.width ; j++)
            {
                if(this.data[i][j].isEmpty())
                    System.out.print(" 0 ");
                else
                    System.out.print(" 1 ");
            }
            System.out.println("");
        }
    }

    //UNe fois la case cliquée repérée, on analyse ses voisins pour 
    public void destroyNeighboors()
    {

    }

    //Fonction qui parcours la grille pour trouver la case qui a été cliquée
    public int foundClickPosition(Point click)
    {
        for(int i=0 ; i<this.width ; i++)
        {
            for(int j=0 ; j<this.height ; j++)
            {
                if (this.data[i][j].isClicked(click) == true)
                {
                    if(this.data[i][j].isEmpty() == true)
                    {
                        //réduire la barre de temp
                        return -1;
                    }
                }
            }

        }
        return 0;
    }

    // Fonction qui affiche les données de la grille dans la console
    public void printColorData()
    {
        for (int i = 0; i < this.height; i++)
        {
            for (int j = 0; j < this.width; j++)
                //System.out.print(" %d ", this.data[i][j].getColor());

                System.out.println("");
        }
    }

}
