package com.colormatch_paris8.colormatch;
import android.graphics.Color;
import android.graphics.Point;

import java.util.Random;

/**
 * Created by Mohamed on 10/11/2017.
 */

public class Case
{
    float posX;
    float posY;
    float size;
    boolean empty;
    int color;

    // Constructeur qui initialise une case à couleur aléatoire
    public Case(boolean fill, float size)
    {
        //Si création de case colorées
        if(fill == true)
        {
            int listColor[] = {Color.BLUE, Color.GREEN, Color.GRAY, Color.MAGENTA, Color.YELLOW, Color.RED, Color.BLACK};
            int numberColor = listColor.length;
            Random randomIndice = new Random();
            this.color = randomIndice.nextInt(numberColor);
            this.empty = false;
            this.size = size;
        }
        //Si création de case vide
        else
        {
            this.empty = true;
            this.color = -1;
        }

    }

    // Fonction qui retourne l'indice de la couleur de la case (int)
    public int getColor()
    {
        return this.color;
    }

    // Fonction qui affiche la couleur de la case
    public void printColor()
    {
        if(this.color == -1)     System.out.println("Case Vide");
        else if(this.color == 0) System.out.println("Couleur Bleu");
        else if(this.color== 1)  System.out.println("Couleur Verte");
        else if(this.color == 2) System.out.println("Couleur Grise");
        else if(this.color == 3) System.out.println("Couleur Magenta");
        else if(this.color == 4) System.out.println("Couleur Jaune");
        else if(this.color == 5) System.out.println("Couleur Rouge");
        else if(this.color == 6) System.out.println("Couleur Noire");
        else System.out.println("Aucune couleur reconnue !!");
    }

    //Fonction qui prend en paramètre un point cliqué et analyse si celui-ci touche la case ou non
    public boolean isClicked(Point pos)
    {
        if(pos.x >= this.posX && pos.x <= (this.posX + this.size) && pos.y >= this.posY && pos.y <= (this.posY + this.size))
            return true;
        return false;
    }

    // Fonction qui renvoie l'état de la case (vide ou remplie)
    boolean isEmpty()
    {
        return this.empty;
    }

    // Fonction qui détruit une case
    public void destroy()
    {
        this.color = -1;
        this.empty = true;
    }


}
