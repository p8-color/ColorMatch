package com.colormatch_paris8.colormatch;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Classe SharedPreference : Cette classe sert à sauvegarder les données du jeux via la méthode "Sharedpreference"
 */
public class SharedPreference
{

    Context context;
    android.content.SharedPreferences prefs;
    android.content.SharedPreferences.Editor editor;

    /**
     * Constructeur
     *
     */
    public SharedPreference(Context context)
    {
        this.context = context;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        editor = prefs.edit();
    }

    /**
     * Fonction qui inclu un booléen dans le fichier de sauvegarde
     * Si True alors la partie est en cours
     * Si false, le joueur a terminé sa partie
     * nécéssaire dans le cas ou le joueur souhaite continuer sa partie, si la variable est a true alors cela signifie que le joueur avait quitté une partie en cours
     * au préalable, et si false, le joueur peut commencer une partie clean ...
     * @return
     */
    public void setResumeGame(boolean b) {
        editor.putBoolean("ResumeGame", b).commit();
    }

    public boolean getResumeGame() {
        return prefs.getBoolean("ResumeGame", false);
    }

    /**
     * Fonction qui retourne le meilleur score si il existe, sinon -1
     * @return
     */
    public int getHighScore() {
        return prefs.getInt("High Score", -1);
    }

    /**
     * Fonction qui sauvegarde le meilleur score
     * @return
     */
    public void setHighScore(int i) {
        editor.putInt("High Score", i).commit();
    }

    /**
     * Fonction qui retourne le score de la partie si il existe, sinon -1
     * @return
     */
    public int getScore() {
        return prefs.getInt("Score", 0);
    }

    /**
     * Fonction qui sauvegarde le score
     * @return
     */
    public void setScore(int i) {
        editor.putInt("Score", i).commit();
    }

    /**
     * Fonction qui retourne le temp de la derniere partie si il existe, sinon -1
     * @return
     */
    public float getTime() {
        return prefs.getFloat("Time", 60000);
    }

    /**
     * Fonction qui sauvegarde le score
     * @return
     */
    public void setTime(float i)
    {
        editor.putFloat("Time", i).commit();
    }

    /**
     * Fonction qui sauvegarde les couleurs de chaque case de la grille.
     * @param sizeX Nombre de cases en longueur
     * @param sizeY Nombre de cases en hauteur
     * @param data Structure de donnée (la grille)
     */
    public void saveArray(int sizeX, int sizeY, Cell data[][])
    {
        editor.putInt("Array_SizeX", sizeX).commit();
        editor.putInt("Array_SizeY", sizeY).commit();

        for(int i=0;i<sizeY;i++)
        {
            for(int j=0;j<sizeX;j++)
            {
                editor.remove("Color" + i + j);
                editor.putInt("Color" + i + j, data[i][j].color).commit();
            }
        }
    }

    /**
     * Fonction qui charge le tableau de donné sauvegarder, en créant au fur et amseure chaque case avec sa couleur respective
     * @param cellSize Taille d'une case
     * @param posGrilleX Position de la premiere case en X
     * @param posGrilleY position de la premiere case en Y
     * @return Retourne un tableau de cases identiques à celui sur lequel la partie s'est arrêté
     */
    public Cell[][] loadArray(float cellSize, float posGrilleX, float posGrilleY)
    {
        int sizeX = prefs.getInt("Array_SizeX", 0);
        int sizeY = prefs.getInt("Array_SizeY", 0);
        Cell[][] loadedArray = new Cell[sizeY][sizeX];
        float posCellX = posGrilleX;
        float posCellY = posGrilleY;

        for(int i=0 ; i<sizeY ; i++)
        {
            for (int j=0; j<sizeX; j++)
            {
                int color = prefs.getInt("Color" + i + j, 0);
                loadedArray[i][j] = new Cell(color, cellSize);

                loadedArray[i][j].posX = posCellX;
                loadedArray[i][j].posY = posCellY;
                posCellX += cellSize;
            }

            posCellX = posGrilleX;
            posCellY += cellSize;
        }

        return loadedArray;
    }

    /**
     * Fonction qui supprime les données sauvegardés (
     * @return
     */
    public void clearData()
    {
        editor.clear().commit();
    }
}
