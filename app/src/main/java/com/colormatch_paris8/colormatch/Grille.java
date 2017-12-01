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
        /*
        Display display = getWindowManager().getDefaultDisplay();
        int screenWidth = display.getWidth();
        int screenWeight = display.getHeight();
        float caseSize = screenWidth / w;
        */
        float caseSize = 0;

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
                    if(this.data[i][j].isEmpty() == false)
                    {
                        //réduire la barre de temp
                        return -1;
                    }
                }
            }
        }
        return 0;
    }

    /*
        Cette fonction récupère les voisins de la case passé en paramètre, si ils éxistent,  et stocks leurs positions
        et leurs couleurs dans un tableau 2D
    */
    public void searchNeighbors(int caseI, int caseJ)
    {
        // couleur ; caseI ; caseJ
        int[][] neighbors = {  {-1, -1, -1},
                                {-1, -1, -1},
                                {-1, -1, -1},
                                {-1, -1, -1} };
        int numberNeighbors = 0;


        //Récupération du voisin de droite
        for(int i=caseI+1 ; i<this.width ; i++)
        {
            if(this.data[i][caseJ].isEmpty() == false)
            {
                neighbors[numberNeighbors][0] = this.data[i][caseJ].getColor();
                neighbors[numberNeighbors][1] = i;
                neighbors[numberNeighbors][2] = caseJ;
                numberNeighbors++;
                break;
            }
        }

        //Récupération du voisin de gauche
        for(int i=caseI-1 ; i>=0 ; i--)
        {
            if(this.data[i][caseJ].isEmpty() == false)
            {
                neighbors[numberNeighbors][0] = this.data[i][caseJ].getColor();
                neighbors[numberNeighbors][1] = i;
                neighbors[numberNeighbors][2] = caseJ;
                numberNeighbors++;
                break;
            }
        }

        //Récupération du voisin du haut
        for(int j=caseJ-1 ; j>=0 ; j--)
        {
            if(this.data[caseI][j].isEmpty() == false)
            {
                neighbors[numberNeighbors][0] = this.data[caseI][j].getColor();
                neighbors[numberNeighbors][1] = caseI;
                neighbors[numberNeighbors][2] = j;
                numberNeighbors++;
                break;
            }
        }

        //Récupération du voisin du bas
        for(int j=caseJ+1 ; j<this.height ; j++)
        {
            if(this.data[caseI][j].isEmpty() == false)
            {
                neighbors[numberNeighbors][0] = this.data[caseI][j].getColor();
                neighbors[numberNeighbors][1] = caseI;
                neighbors[numberNeighbors][2] = j;
                numberNeighbors++;
                break;
            }
        }

        System.out.println("case = " + caseI + " " + caseJ);
        for(int i=0 ; i<numberNeighbors ; i++)
            System.out.println(neighbors[i][0] + " " + neighbors[i][1] + " " + neighbors[i][2]);

    }

    //Fonction qui prend en paramètre le tableau de voisins, et analyse si des couleurs identiques s'y trouvent, si oui, il les détruits
    void destroySameColors(int[][] neighbors, int numberNeighbors)
    {
        for(int i=0 ; i<numberNeighbors ; i++)
        {

        }
    }

    // Fonction qui affiche les données de la grille dans la console
    public void printDataColor()
    {
        for (int i = 0; i < this.height; i++)
        {
            for (int j = 0; j < this.width; j++)
                System.out.print(" " + this.data[i][j].getColor() + " ");
            System.out.println("");
        }

        searchNeighbors(3,3);
    }

}
