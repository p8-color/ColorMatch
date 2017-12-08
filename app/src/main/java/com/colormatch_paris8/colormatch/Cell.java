package com.colormatch_paris8.colormatch;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import java.util.Random;

/**
 * Classe Cell : Cette classe permet d'instancier une case ainsi que tous ses attributs :
 * Sa couleur, sa position, son état ...
 */
public class Cell
{
    //Liste de 7 couleur pour les cases
    int listColor[] = { Color.rgb(0,78,205), Color.GREEN, Color.rgb(214, 175, 80), Color.MAGENTA, Color.YELLOW, Color.RED, Color.CYAN};
    float posX; //Position de la case sur l'axe des abscisse
    float posY; //Position de la case sur l'axe des ordonnées
    float size; //Dimensions de la case
    boolean empty; //Boolean qui indique si la case est vide ou non
    int color; // ID de la coueleur de la case (l'ID représente l'indice de la couleur correspondante dans le tabealeau "listColor")
    int opacity = 180; //Opacité de la couleur de la case

    /**
     * Constructeur qui initialise une case vide, ou à couleur aléatoire
     * @param fill : Case vide si définit à "False", et case coloré si définit à "True"
     * @param size : Dimension de la case
     */
     public Cell(boolean fill, float size)
    {
        //Si création de case colorées
        if(fill == true)
        {
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
            this.size = size;
        }
    }

    /**
     * Constructeur pour case définie (utilisé pour charger les données du tbaleau lors du chargement de la sauvegarde)
     */
    public Cell(int color, float size)
    {
        //Si case vide
        if(color == -1)
        {
            this.empty = true;
            this.color = -1;
            this.size = size;
        }

        //Si case colorée
        else
        {
            this.color = color;
            this.empty = false;
            this.size = size;
        }
    }

    /**
     * Fonction qui retourne l'indice de la couleur de la case (int)
     * @return : Id de la couleur de la case
     */
    public int getColor()
    {
        return this.color;
    }


    /**
     * Fonction qui prend en paramètre un point cliqué et analyse si celui-ci touche la case ou non
     * @param clickX : Position du clique en X
     * @param clickY : Position du clique en Y
     * @return : True si la case est touché, False si la case n'est pas touchée
     */
    public boolean isClicked(int clickX, int clickY)
    {
        if(clickX >= this.posX && clickX <= (this.posX + this.size) && clickY >= this.posY && clickY <= (this.posY + this.size))
            return true;
        return false;
    }

    /**
     * Fonction qui dessine une case à sa position déterminée, avec sa couleur réspéctive
     * @param canvas
     */
    public void draw(Canvas canvas)
    {
        if(this.empty == false)
        {
            //Scale représente l'éloignement du rectangle colorré par rapport aux bordures de la grille
            int scale = 7;
            Paint paint = new Paint();

            paint.setColor(listColor[this.color]);
            paint.setAlpha(this.opacity);
            paint.setStrokeWidth(3);
            RectF rect = new RectF(this.posX + scale, this.posY + scale, this.posX + this.size - scale, this.posY + this.size - scale);
            canvas.drawRoundRect(rect, 9, 9, paint);
        }
    }

    /**
     * Fonction qui renvoie l'état de la case (vide ou remplie)
     * @return : True si la case est vide, et False si la case est colorée
     */
    boolean isEmpty()
    {
        return this.empty;
    }

    /**
     * Fonction qui détruit une case
     */
    public void destroy()
    {
        this.color = -1;
        this.empty = true;

    }
}
