package com.colormatch_paris8.colormatch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * Classe GameView : Cette classe est la vue principale du jeux.
 * Elle instancie une grille de jeux, affiche une barre de temp ainsi que le score de la partie.
 */
public class GameView extends View
{
    Grille grille; //Grille du jeux
    int touchX; //clique de l'utilisateur en X
    int touchY; //clique de l'utilisateur en Y
    int screenWidth; //Dimension en lageur de l'écran
    int screenHeight; //Dimension en hauteur de l'écran
    boolean playGame, threadAlive, resumeGame; // La partie est elle toujour en cour, bloqué, ou terminé ?
    int score; //Score ...
    float barProgressionX; //Niveau de la barre de temps en X
    float timerBarSize; //Taille totale de la barre de progression (nécéssaire pour calculer la quantité a enlever lors de la décrémentation du timer)
    float gameTime = 60000; //Durée de la partie
    float elapsedTime = gameTime; // Décrémentation du temp, var nécéssaire car gameTime doit rester comme référence pour diminuer l'affichage de la barre
    Thread myThread ; // Thread qui gère le temp et l'animation

    /**
     * Constructeur
     */
    public GameView(Context context) {
        super(context);
        init();
    }

    /**
     * Constructeur
     */
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * Constructeur
     */
    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /**
     * Fonction qui initialise une grille aux dimensions 10x14, et la positionne sur l'écran
     */
    private void init()
    {
        //Initialisation du thread et des variables du jeux
        initThread();
        this.playGame = true;
        this.threadAlive = true;
        this.resumeGame = true;
        this.score=0;
        myThread.start();

        //Récupération des dimensions de l'écran
        getScreenSize();

        //Initialisation de la grille aux dimensions 10 x 14
        float caseSize = (this.screenWidth / 10) / (float)1.1;
        float grilleX = this.screenWidth/2 - (10/2 * caseSize);
        float grilleY = this.screenHeight/(float)2.2 - (14/2 * caseSize);

        //Création de la grille, en fonction de "loadSave"
        grille = new Grille(GameActivity.loadSave, 10, 14, caseSize, grilleX, grilleY);

        //Première analyse de la grille pour voir si la grille est jouable ou non, si non on arrête la partie
        if(isGameBlocked() == true)
        {
            this.playGame = false;
            this.threadAlive = false;
            this.resumeGame = false;
            if(MainActivity.muteSound == false)
                MainActivity.gameOverSound.start();
            ResultActivity.isGameBlocked = true;
            switchToResultActivity();
        }

        //Taille totale en longueur de la barre de temp (nécéssaire pour calculer la quantité a enlever lors de la décrémentation du timer)
        this.timerBarSize = this.grille.rightBottomX - this.grille.leftTopX;
        //Position du niveau maximum de la barre de temps
        this.barProgressionX = this.grille.rightBottomX;

    }

    /**
     * Fonction appelé lors de la capture d'un clique sur la grille.
     * Les coordonnées du clique sont analysé afin de trouver sur quel case le jour a cliqué.
     * Analyse des voisins de même couleur, puis destructions si ils éxistent, enfin récupération
     * du score, en fonction du nombre de point éfféctués.
     */
    public void play()
    {
        //Si le clique se trouve dans la grille
        if( this.touchY > this.grille.leftTopY && this.touchY < this.grille.rightBottomY)
        {
            int[] caseClicked = this.grille.foundClickPosition(this.touchX, this.touchY);

            //Si on clique entre les cases, on ne fait rien ..
            if(caseClicked[0] == -1)
                return ;

            //Si la case jouée cliquée, est occupée, on ne fait rien
            if (this.grille.data[caseClicked[0]][caseClicked[1]].isEmpty() == false)
                return ;

            //Sinon, on commence les recherches
            else
            {
                int[][] neighbors = this.grille.getNeighbors(caseClicked[0], caseClicked[1]);
                int nbCaseDestoyed = this.grille.destroySameColors(neighbors, neighbors.length);

                //Si mauvais coups
                if(nbCaseDestoyed == 0)
                {
                    Log.e("PlAY", "Coups non permis !!");
                    timePenality(2000);
                    if(MainActivity.muteSound == false)
                        MainActivity.wrongShotSound.start();
                    return ;
                }

                //Si bon coups
                if(MainActivity.muteSound == false)
                    MainActivity.validShotSound.start();
                getScore(nbCaseDestoyed);

                //Après dectruction des cases, nouvelles analyse de la grille pour analyser si la partie est encore jouable
                if(isGameBlocked() == true)
                {
                    this.playGame = false;
                    this.threadAlive = false;
                    this.resumeGame = false;
                    if(MainActivity.muteSound == false)
                        MainActivity.gameOverSound.start();
                    ResultActivity.isGameBlocked = true;
                    switchToResultActivity();
                }
            }
        }
    }

    /**
     * Fonction qui baisse le temp. Est appelé lorsque qu'un mauvais coup a été joué.
     * @param millisPenality : Temp de pénalitée en milliseconde
     */
    public void timePenality(int millisPenality)
    {
        this.elapsedTime -= millisPenality;
        barProgressionX -= ((timerBarSize / (gameTime /100)) * (millisPenality/100));
        invalidate();

    }

    /**
     * Fonction qui calcule le nombre de point obtenue, en fonction du nombre de bloc détruit
     * @param nbCaseDestroyed : Nombre de case détruite dans un coup joué
     */
    public void getScore(int nbCaseDestroyed)
    {
        if(nbCaseDestroyed == 2)
            this.score += 20;

        if(nbCaseDestroyed == 3)
            this.score += 60;

        if(nbCaseDestroyed == 4)
            this.score += 120;
    }

    /**
     * Fonction qui récupère un clique sur l'écran, puis fait appel a la fonction "play()", afin de jouer.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        this.touchX = (int) event.getX();
        this.touchY = (int) event.getY();

        if(this.playGame == true)
            play();

        return false;
    }

    /**
     * Fonction qui récupère les dimensions de l'écran deu device.
     * Les dimensions seront utilisés afin d'adapter la taille et la position de chaque objet en fonction de la taille de l'écran
     */
    public void getScreenSize()
    {
        this.screenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        this.screenHeight = getContext().getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * Fonction qui affiche le score via un TextView
     * @param canvas
     */
    public void printScore(Canvas canvas)
    {
        //Récupération et initialisation du layout
        TextView textScore = (TextView)this.getRootView().findViewById(R.id.textScore);
        textScore.setTextColor(Color.GREEN);
        textScore.setTextSize((this.screenHeight / this.screenWidth) * 33);
        textScore.setText("Score : " + String.valueOf(this.score));
        textScore.setX(this.grille.leftTopX);
        textScore.setY((this.screenHeight - ((this.screenHeight - this.grille.rightBottomY) / 2)) - textScore.getTextSize());

        //Affuchage d'un contraste avec plusieurs couleurs pour l'affchage du texte
        Shader textShader=new LinearGradient(0, 0, textScore.getTextSize()*2, 0,
                new int[]{
                        getResources().getColor(R.color.white),
                        getResources().getColor(R.color.indigo),
                        getResources().getColor(R.color.blue),
                        getResources().getColor(R.color.green),
                        getResources().getColor(R.color.yellow),
                        getResources().getColor(R.color.orange),
                        getResources().getColor(R.color.red)},
                new float[]{0,0.2f,0.4f,0.6f,0.8f,0.9f,1}, Shader.TileMode.REPEAT);
        textScore.getPaint().setShader(textShader);
    }

    /**
     * Fonction qui analyse si le jeux est bloqué ou non (si des coups sont encore possibles)
     * @return : True si le jeux est bloqué et False si la grille est encore jouable
     */
    boolean isGameBlocked()
    {
        int[][] neighbors;

        for(int i=0 ; i<this.grille.height ; i++)
        {
            for(int j=0 ; j<this.grille.width ; j++)
            {
                if(this.grille.data[i][j].isEmpty() == true)
                {
                    //Récupération des 4 voisins de la case courante
                    neighbors = this.grille.getNeighbors(i,j);

                    //Si on trouve au moins deux couleurs identiques parmis les voisins, alors le jeux peux continuer
                    if(this.grille.sameColorFound(neighbors) == true)
                        return false;
                }
            }
        }
        //Si aucune combinaison trouvée, alors le jeux est bloqué
        return true;
    }

    /**
     * Focntion qui initialise le thread principale
     */
    public void initThread()
    {
        myThread = new Thread(new Runnable() {
            @Override
            public void run()
            {
                //Tant que le Thread est en vie
                while (threadAlive == true)
                {
                    //Tant que la partie n'est pas terminée
                    while (playGame)
                        updateTimerBar(100);
                }
            }
        });
    }


    /**
     * Fonction qui actualise la barre de temp en fonction du timer
     * @param delayUpdateMilis : Intervalle de raffraichissement de la barre en Miliseconde
     */
    public void updateTimerBar(int delayUpdateMilis)
    {
        try
        {
            myThread.sleep(delayUpdateMilis);
        }
        catch(Exception e) {}

        //Actualisation de la progression de la barre de temp
        barProgressionX -= (timerBarSize / (gameTime / delayUpdateMilis));
        elapsedTime -= delayUpdateMilis;
        postInvalidate();

        //Si le temp est inférieur a 8 seconde, on lance l'effet sonore du bip à chaque seconde
        if(MainActivity.muteSound == false)
        {
            if (elapsedTime <= 8000) {
                if (elapsedTime % 1000 == 0)
                    MainActivity.bipTimeSound.start();
            }
        }

        //Si le temps est terminé, on arrête la partie + le thread, on sauvegarde, puis on passe à l'affichage des résultats
        if (elapsedTime <= 0)
        {
            this.playGame = false;
            this.threadAlive = false;
            this.resumeGame = false;
            saveGame();
            if(MainActivity.muteSound == false)
                MainActivity.gameOverSound.start();
            switchToResultActivity();
        }
    }

    public void switchToResultActivity()
    {
        Intent myIntent = new Intent(getContext(), ResultActivity.class);
        ((Activity)getContext()).startActivity(myIntent);
    }

    /**
     * Fonction qui affiche la barre de temps
     * @param canvas
     */
    public void printTimerBar(final Canvas canvas)
    {
        Paint paint = new Paint();
        Paint paint1 = new Paint();
        int sc = 0;

        //Calcul de la position de la barre pour s'adapter à la taille de l'écran
        float temp = (this.screenHeight - (this.screenHeight - this.grille.leftTopY) ) / 3;
        float timerBarTopPosY = temp;
        float timerBarBottomPosY = this.grille.leftTopY - temp;

        //Contour de la barre en gris foncé
        paint.setColor(Color.DKGRAY);
        paint.setStrokeWidth(6);
        paint.setStyle(Paint.Style.STROKE);
        RectF rect = new RectF(this.grille.leftTopX, timerBarTopPosY, this.grille.rightBottomX, timerBarBottomPosY);
        canvas.drawRoundRect(rect,10,10,paint);

        //Interieur de la barre en blanc
        paint1.setColor(Color.WHITE);
        paint1.setStyle(Paint.Style.FILL);
        paint1.setAlpha(200);;
        canvas.drawRoundRect(rect,10,10,paint1);

        //Couleur de la barre de progression en verte, si le temp est inférieur à 8 seconde, on passe la couleur en rouge
        if(elapsedTime <= 8000)
            paint.setColor(Color.RED);
        else
            paint.setColor(Color.rgb(137,204,91));
        paint.setStyle(Paint.Style.FILL);
        RectF rect3 = new RectF(this.grille.leftTopX+sc, timerBarTopPosY, this.barProgressionX, timerBarBottomPosY);
        canvas.drawRoundRect(rect3,10,10,paint);

        //Barre de reflet en blanc opaque, juste pour faire jolie ^^
        paint1.setColor(Color.WHITE);
        paint1.setStyle(Paint.Style.FILL);
        paint1.setAlpha(80);
        float refletBarTopY = timerBarTopPosY + ((timerBarBottomPosY - timerBarTopPosY) / 5);
        float refletBarBottomY = refletBarTopY + ((timerBarBottomPosY - timerBarTopPosY) / 4);
        RectF rect2 = new RectF(this.grille.leftTopX, refletBarTopY , this.grille.rightBottomX, refletBarBottomY);
        canvas.drawRoundRect(rect2,10,10,paint1);
    }

    /**
     * Fonction qui sauvegarde les données de la partie en cours
     */
    public void saveGame()
    {
        int temp;
        //Sauvegarde du meilleur score si il est supérieur au meilleur score précédent
        if((temp = MainActivity.saveData.getHighScore()) != -1)
        {
            if(this.score > temp)
                MainActivity.saveData.setHighScore(this.score);
        }
        else
            MainActivity.saveData.setHighScore(this.score);

        //Sauvegarde du dernier score
        MainActivity.saveData.setScore(this.score);
        //Sauvegarde du temp
        MainActivity.saveData.setTime(this.elapsedTime);

        //Sauvegarde indiquant que la partie n'est pas terminées uniquement si il reste plus d'une seconde, sinon ce n'est pas
        //la peine de considérer la partie comme encore en cours
        if(this.elapsedTime < 1000)
            MainActivity.saveData.setResumeGame(false);
        else
            MainActivity.saveData.setResumeGame(this.resumeGame);

        //Sauvegarde du tableau, si le jeu est toujours en cours
        if(this.resumeGame == true)
            MainActivity.saveData.saveArray(this.grille.width, this.grille.height, this.grille.data);
    }

    /**
     * Fonction qui charge les données de la partie si le joueur souhaite continuer une partie précédemment lancée
     */
    public void resumeGame()
    {
        //Récupération du score et du temp
        this.score =  MainActivity.saveData.getScore();
        this.elapsedTime = MainActivity.saveData.getTime();

        //Actualisation de la barre de temp en fonction du temp qui a été écoulé
        float timeRemoved = this.gameTime - this.elapsedTime;
        barProgressionX -= ((timerBarSize / (gameTime / 100)) * (timeRemoved / 100));

        //Rafraichissement de l'affichage de l'activité
        invalidate();
    }

    /**
     * Fonction qui dessine.
     * Appel à la fonction qui affiche la grille.
     * Appel à la fonction qui affiche la barre de temps.
     * Appel à la fonction qui affiche le score.
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        this.grille.draw(canvas);
        printTimerBar(canvas);
        printScore(canvas);
    }
}
