package control;

import boardifier.control.ActionPlayer;
import boardifier.control.Controller;
import boardifier.model.*;
import boardifier.model.action.ActionList;
import boardifier.model.action.GameAction;
import boardifier.model.action.MoveAction;
import boardifier.model.action.RemoveAction;
import boardifier.model.animation.AnimationTypes;
import boardifier.view.View;
import javafx.scene.control.Alert;
import model.*;
import view.WallGridLook;
import boardifier.view.GameStageView;
import view.QuoridorStageView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class QuoridorController extends Controller {

    BufferedReader consoleIn;
    boolean firstPlayer;

    // Scanner scanner = new Scanner(System.in);

    public QuoridorController(Model model, View view) {
        super(model, view);
        setControlKey(new QuoridorControllerKey(model, view, this));
        // setControlMouse(new QuoridorControllerMouse(model, view, this));
        setControlAction (new QuoridorControllerAction(model, view, this));
        firstPlayer = true;
    }

    @Override
    protected void startStage(String stageName) throws GameException {
        super.startStage(stageName);
        consoleIn = new BufferedReader(new InputStreamReader(System.in));
        nextPlayer();
    }


    /**
     * Defines what to do within the single stage of the single party
     * It is pretty straight forward to write :
     */

    public void nextPlayer() {
        update();
        // use the default method to compute next player
        model.setNextPlayer();
        // get the new player
        Player p = model.getCurrentPlayer();
        // change the text of the TextElement
        QuoridorStageModel stageModel = (QuoridorStageModel) model.getGameStage();
        stageModel.getPlayerName().setText(p.getName());
        if (p.getType() == Player.COMPUTER) {
            update();
            System.out.println("COMPUTER PLAYS");
            QuoridorDecider decider = new QuoridorDecider(model, this);
            ActionPlayer play = new ActionPlayer(model, this, decider, null);
            play.start();
            update();
        } else {
            boolean ok = false;
            while (!ok) {
                System.out.print(p.getName() + " > ");
                try {
                    String line = consoleIn.readLine();
                    ok = analyseAndPlay(line, true);
                    if (!ok) {
                        System.out.println("incorrect instruction. retry !");
                    }
                } catch (IOException e) {
                }
            }
        }
        update();
        isWin();
    }
    private boolean analyseAndPlay(String line, boolean applyMove) {
        QuoridorStageModel gameStage = (QuoridorStageModel) model.getGameStage();
        int[][] plateau = new int[9][9];
        int[][] horizontalWall = new int[8][9];
        int[][] verticalWall = new int[9][8];
        int[][] intersection = new int[8][8];
        int[][] positionJoueur = new int[2][2];
        boolean horizontal = false;
        int[][] newHorizontalWall = new int[horizontalWall.length][horizontalWall[0].length];
        int[][] newVerticalWall = new int[verticalWall.length][verticalWall[0].length];
        int x = 0;
        int y = 0;
        int x2 = 0;
        int y2 = 0;
        int col = 0;
        int ligne = 0;
        int count = 0;
        int currentPlayer = model.getIdPlayer();
        QuoridorWallPot currentPlayerWallPot;
        ActionList actions = new ActionList(true);
        ActionPlayer play;
        GameAction move;

        plateau[gameStage.getRedPawn().getLine()][gameStage.getRedPawn().getCol()] = gameStage.getRedPawn().getPlayerID() + 1;
        plateau[gameStage.getBluePawn().getLine()][gameStage.getBluePawn().getCol()] = gameStage.getBluePawn().getPlayerID() + 1;
        for(int i=0; i < horizontalWall.length; i++){
            for(int j=0; j < horizontalWall[0].length; j++){
                if(gameStage.getHorizontalWallGrid().getElement(i, j).isVisible()){
                    horizontalWall[i][j] = 1;
                }
            }
        }
        for(int i=0; i < verticalWall.length; i++){
            for(int j=0; j < verticalWall[0].length; j++){
                if(gameStage.getVerticalWallGrid().getElement(i, j).isVisible()){
                    verticalWall[i][j] = 1;
                }
            }
        }
        for(int j=0; j < newHorizontalWall.length; j++){
            for(int k=0; k < newHorizontalWall[0].length; k++){
                newHorizontalWall[j][k] = horizontalWall[j][k];
            }
        }
        for(int j=0; j < newVerticalWall.length; j++){
            for(int k=0; k < newVerticalWall[0].length; k++){
                newVerticalWall[j][k] = verticalWall[j][k];
            }
        }
        for(int i=0; i < intersection.length; i++){
            for(int j=0; j < intersection[0].length; j++){
                if(gameStage.getIntersectionGrid().getElement(i, j).isVisible()){
                    intersection[i][j] = 1;
                }
            }
        }
        positionJoueur[0][0] = gameStage.getBluePawn().getLine();
        positionJoueur[0][1] = gameStage.getBluePawn().getCol();
        positionJoueur[1][0] = gameStage.getRedPawn().getLine();
        positionJoueur[1][1] = gameStage.getRedPawn().getCol();
        if(currentPlayer == 0){
            currentPlayerWallPot = gameStage.getBluePot();
        }else{
            currentPlayerWallPot = gameStage.getRedPot();
        }

        if(line.length() < 2){
            return false;
        }
        if(line.charAt(0) == 'M' || line.charAt(0) == 'm'){
            if(isWallEmpty(currentPlayerWallPot)){
                return false;
            }
            if(line.charAt(1) == 'H' || line.charAt(1) == 'h'){
                horizontal = true;

            }else if(line.charAt(1) == 'V' || line.charAt(1) == 'v'){
                horizontal = false;
            }else{
                return false;
            }
            if(line.length() > 2){
                col = (int)line.charAt(2) - 65;
            }else{
                return false;
            }
            if(line.length() > 3){
                count = 3;
                while(count < line.length() && line.charAt(count) >= 48 && line.charAt(count) <= 57){
                    ligne = 10*ligne + ((int)line.charAt(count) - 48);
                    count = count + 1;
                }
            }else{
                return false;
            }
            if(horizontal && ligne >= 1 && ligne <= horizontalWall.length && col >= 0 && col < horizontalWall[0].length - 1){
                if(horizontalWall[ligne-1][col] == 0 && horizontalWall[ligne-1][col+1] == 0){
                    if(intersection[ligne-1][col] == 0){
                        newHorizontalWall[ligne-1][col] = currentPlayer + 1;
                        newHorizontalWall[ligne-1][col+1] = currentPlayer + 1;
                        if(!isBlocked(plateau, newHorizontalWall, newVerticalWall, positionJoueur)){
                            if(applyMove){
                                move = removeWall(currentPlayerWallPot);
                                actions.addSingleAction(move);
                                gameStage.getHorizontalWalls()[ligne-1][col].setColor(currentPlayer);
                                gameStage.getHorizontalWallGrid().getElement(ligne-1, col).setVisible(true);
                                gameStage.getHorizontalWalls()[ligne-1][col+1].setColor(currentPlayer);
                                gameStage.getHorizontalWallGrid().getElement(ligne-1, col+1).setVisible(true);
                                gameStage.getIntersections()[ligne-1][col].setColor(currentPlayer);
                                gameStage.getIntersections()[ligne-1][col].setDirection(0);
                                gameStage.getIntersectionGrid().getElement(ligne-1, col).setVisible(true);
                            }
                        }else{
                            return false;
                        }
                    }else{
                        return false;
                    }
                }else{
                    return false;
                }
            }else if(!horizontal && ligne >= 1 && ligne <= verticalWall.length - 1 && col >= 0 && col < verticalWall[0].length){
                if(verticalWall[ligne-1][col] == 0 && verticalWall[ligne][col] == 0){
                    if(intersection[ligne-1][col] == 0){
                        newVerticalWall[ligne-1][col] = currentPlayer + 1;
                        newVerticalWall[ligne][col] = currentPlayer + 1;
                        if(!isBlocked(plateau, newHorizontalWall, newVerticalWall, positionJoueur)){
                            if(applyMove){
                                move = removeWall(currentPlayerWallPot);
                                actions.addSingleAction(move);
                                gameStage.getVerticalWalls()[ligne-1][col].setColor(currentPlayer);
                                gameStage.getVerticalWallGrid().getElement(ligne-1, col).setVisible(true);
                                gameStage.getVerticalWalls()[ligne][col].setColor(currentPlayer);
                                gameStage.getVerticalWallGrid().getElement(ligne, col).setVisible(true);
                                gameStage.getIntersections()[ligne-1][col].setColor(currentPlayer);
                                gameStage.getIntersections()[ligne-1][col].setDirection(1);
                                gameStage.getIntersectionGrid().getElement(ligne-1, col).setVisible(true);
                            }
                        }else{
                            return false;
                        }
                    }else{
                        return false;
                    }
                }else{
                    return false;
                }
            }else{
                return false;
            }
        }else if(line.charAt(0) == 'P' || line.charAt(0) == 'p'){
            if(line.charAt(1) == 'D' || line.charAt(1) == 'd'){
                x = 1;
            }else if(line.charAt(1) == 'G' || line.charAt(1) == 'g'){
                x = -1;
            }else if(line.charAt(1) == 'H' || line.charAt(1) == 'h'){
                y = -1;
            }else if(line.charAt(1) == 'B' || line.charAt(1) == 'b'){
                y = 1;
            }else{
                return false;
            }
            if(positionJoueur[currentPlayer][1] + x < plateau[0].length && positionJoueur[currentPlayer][1] + x >= 0 && positionJoueur[currentPlayer][0] + y < plateau.length && positionJoueur[currentPlayer][0] + y >= 0){
                if(line.length() > 2 && (line.charAt(2) == 'd' || line.charAt(2) == 'g' || line.charAt(2) == 'h' || line.charAt(2) == 'b' || line.charAt(2) == 'D' || line.charAt(2) == 'G' || line.charAt(2) == 'H' || line.charAt(2) == 'B')){
                    if(plateau[positionJoueur[currentPlayer][0] + y][positionJoueur[currentPlayer][1] + x] != 0){
                        if(line.charAt(2) == 'D' || line.charAt(2) == 'd'){
                            x2 = 1;
                        }else if(line.charAt(2) == 'G' || line.charAt(2) == 'g'){
                            x2 = -1;
                        }else if(line.charAt(2) == 'H' || line.charAt(2) == 'h'){
                            y2 = -1;
                        }else if(line.charAt(2) == 'B' || line.charAt(2) == 'b'){
                            y2 = 1;
                        }else{
                            return false;
                        }
                        if(positionJoueur[currentPlayer][1] + x + x2 < plateau[0].length && positionJoueur[currentPlayer][1] + x + x2 >= 0 && positionJoueur[currentPlayer][0] + y + y2 < plateau.length && positionJoueur[currentPlayer][0] + y + y2 >= 0){
                            if(x2 == 1 && verticalWall[positionJoueur[currentPlayer][0] + y][positionJoueur[currentPlayer][1] + x] == 0 || x2 == -1 && verticalWall[positionJoueur[currentPlayer][0] + y][positionJoueur[currentPlayer][1] + x - 1] == 0 || y2 == 1 && horizontalWall[positionJoueur[currentPlayer][0] + y][positionJoueur[currentPlayer][1] + x] == 0 || y2 == -1 && horizontalWall[positionJoueur[currentPlayer][0] + y - 1][positionJoueur[currentPlayer][1] + x] == 0){
                                if(applyMove){
                                    if(currentPlayer == 0){
                                        move = new MoveAction(model, gameStage.getBluePawn(), "quoridorboard", gameStage.getBluePawn().getLine() + y + y2, gameStage.getBluePawn().getCol() + x + x2);
                                        gameStage.getBluePawn().setLine(gameStage.getBluePawn().getLine() + y + y2);
                                        gameStage.getBluePawn().setCol(gameStage.getBluePawn().getCol() + x + x2);
                                    }else{
                                        move = new MoveAction(model, gameStage.getRedPawn(), "quoridorboard", gameStage.getRedPawn().getLine() + y + y2, gameStage.getRedPawn().getCol() + x + x2);
                                        gameStage.getRedPawn().setLine(gameStage.getRedPawn().getLine() + y + y2);
                                        gameStage.getRedPawn().setCol(gameStage.getRedPawn().getCol() + x + x2);
                                    }
                                    actions.addSingleAction(move);
                                }
                            }else{
                                return false;
                            }
                        }else{
                            return false;
                        }
                    }else{
                        return false;
                    }
                }else if(plateau[positionJoueur[currentPlayer][0] + y][positionJoueur[currentPlayer][1] + x] == 0){
                    if(x == 1 && verticalWall[positionJoueur[currentPlayer][0]][positionJoueur[currentPlayer][1]] == 0 || x == -1 && verticalWall[positionJoueur[currentPlayer][0]][positionJoueur[currentPlayer][1]-1] == 0 || y == 1 && horizontalWall[positionJoueur[currentPlayer][0]][positionJoueur[currentPlayer][1]] == 0 || y == -1 && horizontalWall[positionJoueur[currentPlayer][0]-1][positionJoueur[currentPlayer][1]] == 0){
                        if(applyMove){
                            if(currentPlayer == 0){
                                move = new MoveAction(model, gameStage.getBluePawn(), "quoridorboard", gameStage.getBluePawn().getLine() + y, gameStage.getBluePawn().getCol() + x);
                                gameStage.getBluePawn().setLine(gameStage.getBluePawn().getLine() + y);
                                gameStage.getBluePawn().setCol(gameStage.getBluePawn().getCol() + x);
                            }else{
                                move = new MoveAction(model, gameStage.getRedPawn(), "quoridorboard", gameStage.getRedPawn().getLine() + y, gameStage.getRedPawn().getCol() + x);
                                gameStage.getRedPawn().setLine(gameStage.getRedPawn().getLine() + y);
                                gameStage.getRedPawn().setCol(gameStage.getRedPawn().getCol() + x);
                            }
                            actions.addSingleAction(move);
                        }
                    }else{
                        return false;
                    }
                }else{
                    if(positionJoueur[currentPlayer][1] + 2*x < plateau[0].length && positionJoueur[currentPlayer][1] + 2*x >= 0 && positionJoueur[currentPlayer][0] + 2*y < plateau.length && positionJoueur[currentPlayer][0] + 2*y >= 0){
                        if(plateau[positionJoueur[currentPlayer][0] + 2*y][positionJoueur[currentPlayer][1] + 2*x] == 0){
                            if(x == 1 && verticalWall[positionJoueur[currentPlayer][0]][positionJoueur[currentPlayer][1]] == 0 || x == -1 && verticalWall[positionJoueur[currentPlayer][0]][positionJoueur[currentPlayer][1]-1] == 0 || y == 1 && horizontalWall[positionJoueur[currentPlayer][0]][positionJoueur[currentPlayer][1]] == 0 || y == -1 && horizontalWall[positionJoueur[currentPlayer][0]-1][positionJoueur[currentPlayer][1]] == 0){
                                if(x == 1 && verticalWall[positionJoueur[currentPlayer][0]][positionJoueur[currentPlayer][1]+1] == 0 || x == -1 && verticalWall[positionJoueur[currentPlayer][0]][positionJoueur[currentPlayer][1]-2] == 0 || y == 1 && horizontalWall[positionJoueur[currentPlayer][0]+1][positionJoueur[currentPlayer][1]] == 0 || y == -1 && horizontalWall[positionJoueur[currentPlayer][0]-2][positionJoueur[currentPlayer][1]] == 0){
                                    if(applyMove){
                                        if(currentPlayer == 0){
                                            move = new MoveAction(model, gameStage.getBluePawn(), "quoridorboard", gameStage.getBluePawn().getLine() + 2*y, gameStage.getBluePawn().getCol() + 2*x);
                                            gameStage.getBluePawn().setLine(gameStage.getBluePawn().getLine() + 2*y);
                                            gameStage.getBluePawn().setCol(gameStage.getBluePawn().getCol() + 2*x);
                                        }else{
                                            move = new MoveAction(model, gameStage.getRedPawn(), "quoridorboard", gameStage.getRedPawn().getLine() + 2*y, gameStage.getRedPawn().getCol() + 2*x);
                                            gameStage.getRedPawn().setLine(gameStage.getRedPawn().getLine() + 2*y);
                                            gameStage.getRedPawn().setCol(gameStage.getRedPawn().getCol() + 2*x);
                                        }
                                        actions.addSingleAction(move);
                                    }
                                }else{
                                    return false;
                                }
                            }else{
                                return false;
                            }
                        }else{
                            return false;
                        }
                    }else{
                        return false;
                    }
                }
            }else{
                return false;
            }
        }else{
            return false;
        }
        play = new ActionPlayer(model, this, actions);
        play.start();
        return true;
    }

    public GameAction removeWall(QuoridorWallPot wall){
        GameAction move = null;
        for(int i=wall.getNbCols()-1; i >= 0; i--){
            if(wall.isElementAt(0, i)){
                move = new RemoveAction(model, wall.getElement(0,i));
                return move;
            }
        }
        return move;
    }

    private boolean isWallEmpty(QuoridorWallPot wall){
        for(int i=0; i < wall.getNbCols(); i++){
            if(wall.isElementAt(0, i)){
                return false;
            }
        }
        return true;
    }

    static boolean isBlocked(int[][] plateau, int[][] horizontalWall, int[][] verticalWall, int[][] positionJoueur){
        int[][] newPlateau = new int[plateau.length][plateau[0].length];
        for(int i=0; i < positionJoueur.length; i++){
            for(int j=0; j < plateau.length; j++){
                for(int k=0; k < plateau[0].length; k++){
                    newPlateau[j][k] = 0;
                }
            }
            newPlateau[positionJoueur[i][0]][positionJoueur[i][1]] = i;
            if(isBlocked(newPlateau, horizontalWall, verticalWall, i, positionJoueur[i])){
                return true;
            }
        }
        return false;
    }
    static boolean isBlocked(int[][] plateau, int[][] horizontalWall, int[][] verticalWall, int player, int[] positionJoueur){
        if(isWin(plateau, player)){
            return false;
        }
        int x=0;
        int y=0;
        int[] savePositionJoueur = new int[2];
        savePositionJoueur[0] = positionJoueur[0];
        savePositionJoueur[1] = positionJoueur[1];
        for(int i=0; i < 4; i++){
            x = 0;
            y = 0;
            if(i==0){
                x = 1;
            }else if(i==1){
                x = -1;
            }else if(i==2){
                y = 1;
            }else if(i==3){
                y = -1;
            }
            if(positionJoueur[0] + y >= 0 && positionJoueur[0] + y < plateau.length && positionJoueur[1] + x >= 0 && positionJoueur[1] + x < plateau[0].length){
                if(plateau[positionJoueur[0] + y][positionJoueur[1] + x] == 0){
                    if(x == 1 && verticalWall[positionJoueur[0]][positionJoueur[1]] == 0 || x == -1 && verticalWall[positionJoueur[0]][positionJoueur[1]-1] == 0 || y == 1 && horizontalWall[positionJoueur[0]][positionJoueur[1]] == 0 || y == -1 && horizontalWall[positionJoueur[0]-1][positionJoueur[1]] == 0){
                        plateau[positionJoueur[0] + y][positionJoueur[1] + x] = player + 1;
                        if(!isBlocked(plateau, horizontalWall, verticalWall, player, new int[]{positionJoueur[0] + y, positionJoueur[1] + x})){
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    static boolean isWin(int[][] plateau, int player){
        if(player == 0){
            for(int i=0; i < plateau[0].length; i++){
                if(plateau[0][i] == 1){
                    return true;
                }
            }
        }
        if(player == 1){
            for(int i=0; i < plateau[0].length; i++){
                if(plateau[plateau.length-1][i] == 2){
                    return true;
                }
            }
        }
        return false;
    }

    void isWin(){
        QuoridorStageModel gameStage = (QuoridorStageModel) model.getGameStage();
        if(gameStage.getBluePawn().getLine() == 0){
            model.setIdWinner(0);
            model.stopStage();
            endGame();
        }else if(gameStage.getRedPawn().getLine() == 8){
            model.setIdWinner(1);
            model.stopStage();
            endGame();
        }
    }
}
