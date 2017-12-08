package com.colormatch_paris8.colormatch;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import java.util.Random;

/**
 * Classe Grille : Cette classe permet de créer une grille de jeux et implémente les fonctions
 * nécéssaires pour jouer : Parcours de voisinage, destruction des bloques identiques, affichage de la grille  ...
 */
public class Grille
{

    Cell data[][]; //Streucture de donnée à deux dimensions qui contiendras els case de la grille
    float leftTopX, leftTopY, rightBottomX, rightBottomY; //Position de la grille sur l'écran
    int height; //Nombre de case dans la grille en hauteur
    int width; //Nombre de case dans la grille en longueur
    int numberEmptyCase; //Nombre de cases vide
    int opacity = 255; //Opacité de l'arrière plan de la grille

    /**
     * Constructeur qui initialise une grille aux dimentions passés en paramètres, avec des cases vides et colorées
     * @param w : Nombre de cases en longueur
     * @param h : Nombre de cases en hauteur
     * @param cellSize : Dimensions d'une case
     * @param topX : Position supérieur gauche de la grille en X
     * @param topY : Position supérieur gauche de la grille en Y
     */
    public Grille(Boolean loadData, int w, int h, float cellSize, float topX, float topY)
    {
        //Récupération des informations de la grille
        this.height = h;
        this.width = w;
        this.numberEmptyCase = 20;
        this.leftTopX = topX;
        this.leftTopY = topY;
        this.rightBottomX = this.leftTopX + (w * cellSize);
        this.rightBottomY = this.leftTopY + (h * cellSize);
        Random random = new Random();

        if(loadData == true)
        {
             data = MainActivity.saveData.loadArray(cellSize, leftTopX, leftTopY);
        }
        else
        {
            this.data = new Cell[this.height][this.width];

            // Initialisation des cases vides, par défault on initialise 20 case vide
            for (int i = 0; i < this.numberEmptyCase; i++)
            {
                int emptyCaseI = random.nextInt(this.height);
                int emptyCaseJ = random.nextInt(this.width);
                this.data[emptyCaseI][emptyCaseJ] = new Cell(false, cellSize);
            }

            // Initialisation des cases restantes avec une couleur aléatoire
            float posX = this.leftTopX;
            float posY = this.leftTopY;
            for (int i = 0; i < h; i++) {
                for (int j = 0; j < w; j++) {
                    //Création de la case si celle-ci n'existe pas
                    if (this.data[i][j] == null)
                        this.data[i][j] = new Cell(true, cellSize);

                    //Initialisation des positions de chaque cases
                    this.data[i][j].posX = posX;
                    this.data[i][j].posY = posY;
                    posX += cellSize;
                }

                posX = this.leftTopX;
                posY += cellSize;
            }
        }
    }

    /**
     * Fonction qui parcoure la grille afin de trouver sur quelle case le clique a été éfféctué
     * @param clickX : Position en X du clique capturé
     * @param clickY : Position en Y du clique capturés
     * @return : Coordonnées de la case cliquée {i ; j}
     */
    public int[] foundClickPosition(int clickX, int clickY)
    {
        int[] res = {-1, -1};

        for(int i=0 ; i<this.height; i++)
        {
            for(int j=0 ; j<this.width ; j++)
            {
                if (this.data[i][j].isClicked(clickX, clickY) == true)
                {
                    res[0] = i;
                    res[1] = j;
                    return res;
                }
            }
        }
        return res;
    }

    /**
     * Cette fonction récupère les voisins de la case passé en paramètre, si ils éxistent,  et stocks leurs positions
     * et leurs couleurs dans un tableau 2D.
     * @param caseI : Position en abscisse de la case dans la grille
     * @param caseJ : Position en ordonnées de la case dans la grille
     * @return : Tableau de voisins {couleur ; I ; J}
     */
    public int[][] getNeighbors(int caseI, int caseJ)
    {
        //couleur ; caseI ; caseJ
        int[][] neighbors = {  {-1, -1, -1},
                                {-1, -1, -1},
                                {-1, -1, -1},
                                {-1, -1, -1} };
        int numberNeighbors = 0;


        //Récupération du voisin de droite
        for(int j=caseJ+1 ; j<this.width ; j++)
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

        //Récupération du voisin de gauche
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

        //Récupération du voisin du haut
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

        //Récupération du voisin du bas
        for(int i=caseI+1 ; i<this.height ; i++)
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

        return neighbors;
    }

    /**
     * Fonction qui prend en paramètre le tableau de voisins, et analyse si des couleurs identiques s'y trouvent, si oui, on les détruits
     * @param neighbors : Tableau de voisins
     * @param numberNeighbors : Nombre de voisins trouvés
     * @return : Nombre de cases détruite
     */
    int destroySameColors(int[][] neighbors, int numberNeighbors)
    {
        int sameColor = 1;
        int colorRef;
        int point = 0;
        int caseI, caseJ;

        // Si pas assez de voisins à comparer, on arrête la fonction
        if(numberNeighbors <= 1)
            return 0;

        //Sinon, on compare chaque voisin avec les autres pour voir si il y a des couleurs identiques
        for(int i=0 ; i<numberNeighbors-1 ; i++)
        {
            //On stock la couleur du voisin qu'on analyse
            colorRef = neighbors[i][0];

            for(int j=i+1 ; j<numberNeighbors ; j++)
            {
                //Si la case a déja été détruite, on saute le tour
                if (neighbors[j][0] == -1) continue ;

                //Si deux voisins A et B ont la même couleur, ont détruit le voisin B (le voisin A sera détruit une fois que toute les autres occurence
                //de sa couleur auront été détruites)
                if(colorRef == neighbors[j][0])
                {
                    //On supprime le voisin de la liste des voisins
                    neighbors[j][0] = -1;

                    //Puis on supprime la case dans la structure de donnée de la grille
                    caseI = neighbors[j][1];
                    caseJ = neighbors[j][2];
                    this.data[caseI][caseJ].destroy();

                    // On incrémente le nombre de couleur identiques trouvés
                    sameColor++;

                    System.out.println("\n couleur = " + colorRef + " " + caseI + " " + caseJ);
                }
            }

            //Si au moins deux couleurs identiques ont été trouvées
            if(sameColor > 1)
            {
                point += sameColor;
                sameColor = 1;

                //On détruit le voisin A, qui est le voisin de référence
                caseI = neighbors[i][1];
                caseJ = neighbors[i][2];
                this.data[caseI][caseJ].destroy();
            }
        }

        return point ;
    }

    /**
     * Fonction qui parcour les quatre voisins passé en paramètres pour savoir si un coup est possible, si au moins un coup est possible,
     * la fonction renvoie true, et false dans le cas contraire
     * @param neighbors : Tableau de voisins
     * @return : True si deux couleurs idéntiques ont été trouvés
     * @return : False si aucunes couleurs idéntiques ont été trouvés
     */
    boolean sameColorFound(int[][] neighbors)
    {
        int colorRef;

        for(int i=0 ; i<4 ; i++)
        {
            colorRef =  neighbors[i][0];
            if(colorRef == -1)
                continue;

            for(int j=i+1 ; j<4 ; j++)
            {
                if(colorRef == neighbors[j][0])
                    return true;
            }
        }
        return false;
    }

    /**
     * Fonction qui dessine chaque case de la grille
     * @param canvas
     */
    public void draw(Canvas canvas)
    {
        Paint paint = new Paint();
        Paint paint1 = new Paint();

        for(int i=0 ; i<this.height ; i++)
        {
            for(int j=0 ; j<this.width ; j++)
            {
                //Dessin de l'arrière plan de la case en blanc
                paint1.setColor(Color.rgb(255,255,255));
                paint1.setAlpha(this.opacity);
                RectF rect = new RectF(this.data[i][j].posX, this.data[i][j].posY, this.data[i][j].posX + this.data[i][j].size, this.data[i][j].posY + this.data[i][j].size);
                canvas.drawRoundRect(rect,10,10,paint1);

                //Dessin du contour de la case en noir
                paint.setColor(Color.BLACK);
                paint.setStrokeWidth(2);
                paint.setStyle(Paint.Style.STROKE);
                RectF rect2 = new RectF(this.data[i][j].posX, this.data[i][j].posY, this.data[i][j].posX + this.data[i][j].size, this.data[i][j].posY + this.data[i][j].size);
                canvas.drawRoundRect(rect2,10,10,paint);

                //Dessin de la couleur de la case, avec sa couleur réspéctive
                this.data[i][j].draw(canvas);
            }
        }
    }

}
