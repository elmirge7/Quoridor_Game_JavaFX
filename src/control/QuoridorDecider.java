package control;

import boardifier.control.Controller;
import boardifier.control.Decider;
import boardifier.model.Model;
import boardifier.model.action.ActionList;
import boardifier.model.action.GameAction;
import boardifier.model.action.MoveAction;
import boardifier.model.action.RemoveAction;
import model.QuoridorWallPot;
import model.QuoridorStageModel;

import java.util.ArrayDeque;
import java.util.Queue;

public class QuoridorDecider extends Decider {
    public QuoridorDecider(Model model, Controller control) {
        super(model, control);
    }

    @Override
    public ActionList decide() {
        QuoridorStageModel gameStage = (QuoridorStageModel) model.getGameStage();
        int[][] plateau = new int[9][9];
        int[][] horizontalWall = new int[8][9];
        int[][] verticalWall = new int[9][8];
        int[][] intersection = new int[8][8];
        int[] wall;
        int[][] positionJoueur = new int[2][2];
        String[] possibleMove;
        QuoridorWallPot currentPlayerWallPot;
        double[] moveValue;
        String[] goodMove;
        String finalMove = "";
        double min;
        int nbGoodMove = 0;
        Object[] temp;
        int level = 0;

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
        for(int i=0; i < intersection.length; i++){
            for(int j=0; j < intersection[0].length; j++){
                if(gameStage.getIntersectionGrid().getElement(i, j).isVisible()){
                    intersection[i][j] = 1;
                }
            }
        }
        if(model.getIdPlayer() == 0){
            currentPlayerWallPot = gameStage.getBluePot();
        }else{
            currentPlayerWallPot = gameStage.getRedPot();
        }
        wall = new int[currentPlayerWallPot.getNbCols()];
        for(int i=0; i < wall.length; i++){
            if(currentPlayerWallPot.isElementAt(0, i)){
                wall[i] = 1;
            }
        }
        positionJoueur[0][0] = gameStage.getBluePawn().getLine();
        positionJoueur[0][1] = gameStage.getBluePawn().getCol();
        positionJoueur[1][0] = gameStage.getRedPawn().getLine();
        positionJoueur[1][1] = gameStage.getRedPawn().getCol();

        possibleMove = getPossibleMove(plateau, horizontalWall, verticalWall, intersection, wall, model.getIdPlayer(), true, positionJoueur);
        moveValue = getMoveValue(plateau, horizontalWall, verticalWall, intersection, wall, model.getIdPlayer(), positionJoueur, possibleMove);
        temp = pruning(possibleMove, moveValue);
        possibleMove = (String[])temp[0];
        moveValue = (double[])temp[1];
        if(level == 1){
            temp = monteCarlo(plateau, horizontalWall, verticalWall, intersection, wall, model.getIdPlayer(), positionJoueur, 1000);
            possibleMove = (String[])temp[0];
            moveValue = (double[])temp[1];
        }else if(level == 2){
            moveValue = minMax(plateau, horizontalWall, verticalWall, intersection, wall, model.getIdPlayer(), positionJoueur, 0, 20);
        }

        min = moveValue[0];
        for(int i=0; i < moveValue.length; i++){
            if(moveValue[i] < min){
                min = moveValue[i];
            }
        }
        for(int i=0; i < moveValue.length; i++){
            if(moveValue[i] == min){
                nbGoodMove = nbGoodMove + 1;
            }
        }
        goodMove = new String[nbGoodMove];
        nbGoodMove = 0;
        for(int i=0; i < moveValue.length; i++){
            if(moveValue[i] == min){
                goodMove[nbGoodMove] = possibleMove[i];
                nbGoodMove = nbGoodMove + 1;
            }
        }
        finalMove = goodMove[(int)(Math.random()*goodMove.length)];
        return StringToActionList(finalMove, model.getIdPlayer(), positionJoueur);
    }

    Object[] pruning(String[] move, double[] value){
        String[] moveCopy = new String[move.length];
        double[] valueCopy = new double[value.length];
        double min = value[0];
        int size = 1;
        int id = 0;
        for(int i=0; i < move.length; i++){
            moveCopy[i] = move[i];
        }
        for(int i=0; i < value.length; i++){
            valueCopy[i] = value[i];
        }
        for(int i=1; i < value.length; i++){
            if(value[i] < min){
                min = value[i];
                size = 1;
            }else if(min == value[i]){
                size = size + 1;
            }
        }

        move = new String[size];
        value = new double[size];

        for(int i=0; i < moveCopy.length; i++){
            if(valueCopy[i] == min){
                move[id] = moveCopy[i];
                value[id] = valueCopy[i];
                id = id + 1;
            }
        }
        return new Object[]{move, value};
    }

    private double average(double[] tab){
        double sum = 0.0;
        for(int i=0; i < tab.length; i++){
            sum = sum + tab[i];
        }
        return sum/tab.length;
    }

    double[] minMax(int[][] plateau, int[][] horizontalWall, int[][] verticalWall, int[][] intersection, int[] wall, int player, int[][] positionJoueur, int recursionLevel, int maxRecursion){
        String[] possibleMove = getPossibleMove(plateau, horizontalWall, verticalWall, intersection, wall, player, true, positionJoueur);
        double[] moveValue = new double[possibleMove.length];
        int otherPlayer;
        int[][] savePlateau = new int[plateau.length][plateau[0].length];
        int[][] saveHorizontalWall = new int[horizontalWall.length][horizontalWall[0].length];
        int[][] saveVerticalWall = new int[verticalWall.length][verticalWall[0].length];
        int[][] savePositionJoueur = new int[positionJoueur.length][positionJoueur[0].length];
        int[][] saveIntersection = new int[intersection.length][intersection[0].length];
        int[] saveWall = new int[wall.length];
        Object[] temp;

        if(recursionLevel >= maxRecursion){

            return moveValue;
        }

        if(player == 0){
            otherPlayer = 1;
        }else{
            otherPlayer = 0;
        }
        for(int i=0; i < plateau.length; i++){
            for(int j=0; j < plateau[0].length; j++){
                savePlateau[i][j] = plateau[i][j];
            }
        }
        for(int i=0; i < horizontalWall.length; i++){
            for(int j=0; j < horizontalWall[0].length; j++){
                saveHorizontalWall[i][j] = horizontalWall[i][j];
            }
        }
        for(int i=0; i < verticalWall.length; i++){
            for(int j=0; j < verticalWall[0].length; j++){
                saveVerticalWall[i][j] = verticalWall[i][j];
            }
        }
        for(int i=0; i < positionJoueur.length; i++){
            for(int j=0; j < positionJoueur[0].length; j++){
                savePositionJoueur[i][j] = positionJoueur[i][j];
            }
        }
        for(int i=0; i < intersection.length; i++){
            for(int j=0; j < intersection[0].length; j++){
                saveIntersection[i][j] = intersection[i][j];
            }
        }
        for(int i=0; i < wall.length; i++){
            saveWall[i] = wall[i];
        }

        moveValue = getMoveValue(plateau, horizontalWall, verticalWall, intersection, wall, player, positionJoueur, possibleMove);
        temp = pruning(possibleMove, moveValue);
        possibleMove = (String[])temp[0];
        moveValue = new double[possibleMove.length];

        for(int k=0; k < possibleMove.length; k++){
            jouer(plateau, horizontalWall, verticalWall, intersection, wall, positionJoueur, player, possibleMove[k], true);
            moveValue[k] = 0;
            if(isWin(plateau, player)){
                moveValue[k] = moveValue[k] - 1;
            }else if(isWin(plateau, otherPlayer)){
                moveValue[k] = moveValue[k] + 1;
            }else{
                moveValue[k] = (distanceToPath(findShortestPath(plateau, horizontalWall, verticalWall, player, positionJoueur[player]), horizontalWall, verticalWall).length - distanceToPath(findShortestPath(plateau, horizontalWall, verticalWall, otherPlayer, positionJoueur[otherPlayer]), horizontalWall, verticalWall).length)/2.0;
            }
            moveValue[k] = moveValue[k] - average(minMax(plateau, horizontalWall, verticalWall, intersection, wall, otherPlayer, positionJoueur, recursionLevel + 1, maxRecursion))/(recursionLevel+2);
            for(int i=0; i < plateau.length; i++){
                for(int j=0; j < plateau[0].length; j++){
                    plateau[i][j] = savePlateau[i][j];
                }
            }
            for(int i=0; i < horizontalWall.length; i++){
                for(int j=0; j < horizontalWall[0].length; j++){
                    horizontalWall[i][j] = saveHorizontalWall[i][j];
                }
            }
            for(int i=0; i < verticalWall.length; i++){
                for(int j=0; j < verticalWall[0].length; j++){
                    verticalWall[i][j] = saveVerticalWall[i][j];
                }
            }
            for(int i=0; i < positionJoueur.length; i++){
                for(int j=0; j < positionJoueur[0].length; j++){
                    positionJoueur[i][j] = savePositionJoueur[i][j];
                }
            }
            for(int i=0; i < intersection.length; i++){
                for(int j=0; j < intersection[0].length; j++){
                    intersection[i][j] = saveIntersection[i][j];
                }
            }
            for(int i=0; i < wall.length; i++){
                wall[i] = saveWall[i];
            }
        }
        return moveValue;
    }

    Object[] monteCarlo(int[][] plateau, int[][] horizontalWall, int[][] verticalWall, int[][] intersection, int[] wall, int player, int[][] positionJoueur, int nbGame){
        String[] possibleMove = getPossibleMove(plateau, horizontalWall, verticalWall, intersection, wall, player, true, positionJoueur);
        double[] moveValue;
        double[] currentMoveValue;
        int savePlayer = player;
        int[][] savePlateau = new int[plateau.length][plateau[0].length];
        int[][] saveHorizontalWall = new int[horizontalWall.length][horizontalWall[0].length];
        int[][] saveVerticalWall = new int[verticalWall.length][verticalWall[0].length];
        int[][] savePositionJoueur = new int[positionJoueur.length][positionJoueur[0].length];
        int[][] saveIntersection = new int[intersection.length][intersection[0].length];
        int[] saveWall = new int[wall.length];
        String[] currentMove;
        Object[] temp;

        moveValue = getMoveValue(plateau, horizontalWall, verticalWall, intersection, wall, player, positionJoueur, possibleMove);
        temp = pruning(possibleMove, moveValue);
        possibleMove = (String[])temp[0];
        moveValue = new double[possibleMove.length];
        for(int i=0; i < plateau.length; i++){
            for(int j=0; j < plateau[0].length; j++){
                savePlateau[i][j] = plateau[i][j];
            }
        }
        for(int i=0; i < horizontalWall.length; i++){
            for(int j=0; j < horizontalWall[0].length; j++){
                saveHorizontalWall[i][j] = horizontalWall[i][j];
            }
        }
        for(int i=0; i < verticalWall.length; i++){
            for(int j=0; j < verticalWall[0].length; j++){
                saveVerticalWall[i][j] = verticalWall[i][j];
            }
        }
        for(int i=0; i < positionJoueur.length; i++){
            for(int j=0; j < positionJoueur[0].length; j++){
                savePositionJoueur[i][j] = positionJoueur[i][j];
            }
        }
        for(int i=0; i < intersection.length; i++){
            for(int j=0; j < intersection[0].length; j++){
                saveIntersection[i][j] = intersection[i][j];
            }
        }
        for(int i=0; i < wall.length; i++){
            saveWall[i] = wall[i];
        }

        if(possibleMove.length > 1){
            for(int i=0; i < possibleMove.length; i++){
                for(int j=0; j < nbGame; j++){
                    while(!isWin(plateau, player)){
                        currentMove = getPossibleMove(plateau, horizontalWall, verticalWall, intersection, wall, player, true, positionJoueur);
                        currentMoveValue = getMoveValue(plateau, horizontalWall, verticalWall, intersection, wall, player, positionJoueur, currentMove);
                        temp = pruning(currentMove, currentMoveValue);
                        currentMove = (String[])temp[0];
                        currentMoveValue = (double[])temp[1];
                        jouer(plateau, horizontalWall, verticalWall, intersection, wall, positionJoueur, player, currentMove[(int)(Math.random()*currentMove.length)], true);
                        if(player == 0){
                            player = 1;
                        }else{
                            player = 0;
                        }
                    }
                    if(player == savePlayer){
                        moveValue[i] = moveValue[i] - 1;
                    }else{
                        moveValue[i] = moveValue[i] + 1;
                    }
                    for(int k=0; k < plateau.length; k++){
                        for(int l=0; l < plateau[0].length; l++){
                            plateau[k][l] = savePlateau[k][l];
                        }
                    }
                    for(int k=0; k < horizontalWall.length; k++){
                        for(int l=0; l < horizontalWall[0].length; l++){
                            horizontalWall[k][l] = saveHorizontalWall[k][l];
                        }
                    }
                    for(int k=0; k < verticalWall.length; k++){
                        for(int l=0; l < verticalWall[0].length; l++){
                            verticalWall[k][l] = saveVerticalWall[k][l];
                        }
                    }
                    for(int k=0; k < positionJoueur.length; k++){
                        for(int l=0; l < positionJoueur[0].length; l++){
                            positionJoueur[k][l] = savePositionJoueur[k][l];
                        }
                    }
                    for(int k=0; k < intersection.length; k++){
                        for(int l=0; l < intersection[0].length; l++){
                            intersection[k][l] = saveIntersection[k][l];
                        }
                    }
                    for(int k=0; k < wall.length; k++){
                        wall[k] = saveWall[k];
                    }
                    player = savePlayer;
                }
            }
        }
        return new Object[]{possibleMove, moveValue};
    }

    double[] getMoveValue(int[][] plateau, int[][] horizontalWall, int[][] verticalWall, int[][] intersection, int[] wall, int player, int[][] positionJoueur, String[] move){
        double[] moveValue = new double[move.length];
        int[][] savePlateau = new int[plateau.length][plateau[0].length];
        int[][] saveHorizontalWall = new int[horizontalWall.length][horizontalWall[0].length];
        int[][] saveVerticalWall = new int[verticalWall.length][verticalWall[0].length];
        int[][] savePositionJoueur = new int[positionJoueur.length][positionJoueur[0].length];
        int[][] saveIntersection = new int[intersection.length][intersection[0].length];
        int[] saveWall = new int[wall.length];
        int otherPlayer;

        if(player == 0){
            otherPlayer = 1;
        }else{
            otherPlayer = 0;
        }
        for(int i=0; i < plateau.length; i++){
            for(int j=0; j < plateau[0].length; j++){
                savePlateau[i][j] = plateau[i][j];
            }
        }
        for(int i=0; i < horizontalWall.length; i++){
            for(int j=0; j < horizontalWall[0].length; j++){
                saveHorizontalWall[i][j] = horizontalWall[i][j];
            }
        }
        for(int i=0; i < verticalWall.length; i++){
            for(int j=0; j < verticalWall[0].length; j++){
                saveVerticalWall[i][j] = verticalWall[i][j];
            }
        }
        for(int i=0; i < positionJoueur.length; i++){
            for(int j=0; j < positionJoueur[0].length; j++){
                savePositionJoueur[i][j] = positionJoueur[i][j];
            }
        }
        for(int i=0; i < intersection.length; i++){
            for(int j=0; j < intersection[0].length; j++){
                saveIntersection[i][j] = intersection[i][j];
            }
        }
        for(int i=0; i < wall.length; i++){
            saveWall[i] = wall[i];
        }

        for(int i=0; i < move.length; i++){
            jouer(plateau, horizontalWall, verticalWall, intersection, wall, positionJoueur, player, move[i], true);
            moveValue[i] = distanceToPath(findShortestPath(plateau, horizontalWall, verticalWall, player, positionJoueur[player]), horizontalWall, verticalWall).length;
            moveValue[i] = moveValue[i] - distanceToPath(findShortestPath(plateau, horizontalWall, verticalWall, otherPlayer, positionJoueur[otherPlayer]), horizontalWall, verticalWall).length;

            for(int k=0; k < plateau.length; k++){
                for(int l=0; l < plateau[0].length; l++){
                    plateau[k][l] = savePlateau[k][l];
                }
            }
            for(int k=0; k < horizontalWall.length; k++){
                for(int l=0; l < horizontalWall[0].length; l++){
                    horizontalWall[k][l] = saveHorizontalWall[k][l];
                }
            }
            for(int k=0; k < verticalWall.length; k++){
                for(int l=0; l < verticalWall[0].length; l++){
                    verticalWall[k][l] = saveVerticalWall[k][l];
                }
            }
            for(int k=0; k < positionJoueur.length; k++){
                for(int l=0; l < positionJoueur[0].length; l++){
                    positionJoueur[k][l] = savePositionJoueur[k][l];
                }
            }
            for(int k=0; k < intersection.length; k++){
                for(int l=0; l < intersection[0].length; l++){
                    intersection[k][l] = saveIntersection[k][l];
                }
            }
            for(int k=0; k < wall.length; k++){
                wall[k] = saveWall[k];
            }
        }
        return moveValue;
    }

    ActionList StringToActionList(String line, int currentPlayer, int[][] positionJoueur){
        QuoridorStageModel gameStage = (QuoridorStageModel) model.getGameStage();
        ActionList actions = new ActionList(true);
        GameAction move;
        int x = 0;
        int y = 0;
        int x2 = 0;
        int y2 = 0;
        int col = 0;
        int ligne = 0;
        int count = 0;
        int otherPlayer;
        QuoridorWallPot currentPlayerWallPot;
        if(model.getIdPlayer() == 0){
            otherPlayer = 1;
            currentPlayerWallPot = gameStage.getBluePot();
        }else{
            otherPlayer = 0;
            currentPlayerWallPot = gameStage.getRedPot();
        }
        if(line.charAt(0) == 'm'){
            col = (int)line.charAt(2) - 65;
            count = 3;
            while(count < line.length() && line.charAt(count) >= 48 && line.charAt(count) <= 57){
                ligne = 10*ligne + ((int)line.charAt(count) - 48);
                count = count + 1;
            }
            if(line.charAt(1) == 'h'){
                move = removeWall(currentPlayerWallPot);
                actions.addSingleAction(move);
                gameStage.getHorizontalWalls()[ligne-1][col].setColor(currentPlayer);
                gameStage.getHorizontalWallGrid().getElement(ligne-1, col).setVisible(true);
                gameStage.getHorizontalWalls()[ligne-1][col+1].setColor(currentPlayer);
                gameStage.getHorizontalWallGrid().getElement(ligne-1, col+1).setVisible(true);
                gameStage.getIntersections()[ligne-1][col].setColor(currentPlayer);
                gameStage.getIntersections()[ligne-1][col].setDirection(0);
                gameStage.getIntersectionGrid().getElement(ligne-1, col).setVisible(true);
            }else if(line.charAt(1) == 'v'){
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
        }else if(line.charAt(0) == 'p'){
            if(line.charAt(1) == 'd'){
                x = 1;
            }else if(line.charAt(1) == 'g'){
                x = -1;
            }else if(line.charAt(1) == 'h'){
                y = -1;
            }else if(line.charAt(1) == 'b'){
                y = 1;
            }
            if(line.length() > 2){
                if(line.charAt(2) == 'd'){
                    x2 = 1;
                }else if(line.charAt(2) == 'g'){
                    x2 = -1;
                }else if(line.charAt(2) == 'h'){
                    y2 = -1;
                }else if(line.charAt(2) == 'b'){
                    y2 = 1;
                }
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
            }else if(positionJoueur[currentPlayer][0] + y == positionJoueur[otherPlayer][0] && positionJoueur[currentPlayer][1] + x == positionJoueur[otherPlayer][1]){
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
            }else{
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
        }
        return actions;
    }

    String[] getPossibleMove(int[][] plateau, int[][] horizontalWall, int[][] verticalWall, int[][] intersection, int[] wall, int player, boolean noNull, int[][] positionJoueur){
        String[] lettre = new String[]{"d", "g", "h", "b"};
        String[] possibleMove = new String[3*lettre.length + horizontalWall.length*(horizontalWall[0].length-1) + (verticalWall.length-1)*verticalWall[0].length];
        int id = 0;
        int nbCoupPossible = 0;

        for(int i=0; i < lettre.length; i++){
            if(jouer(plateau, horizontalWall, verticalWall, intersection, wall, positionJoueur, player, "p" + lettre[i], false)){
                possibleMove[id] = "p" + lettre[i];
            }
            id = id + 1;
        }
        for(int i=0; i < lettre.length; i++){
            for(int j=0; j < lettre.length; j++){
                if(i < 2 && j > 1 || i > 1 && j < 2){
                    if(jouer(plateau, horizontalWall, verticalWall, intersection, wall, positionJoueur, player, "p" + lettre[i] + lettre[j], false)){
                        possibleMove[id] = "p" + lettre[i] + lettre[j];
                    }
                    id = id + 1;
                }
            }
        }
        for(int i=0; i < horizontalWall[0].length - 1; i++){
            for(int j=1; j < horizontalWall.length + 1; j++){
                if(jouer(plateau, horizontalWall, verticalWall, intersection, wall, positionJoueur, player, "mh" + (char)(65+i) + String.valueOf(j), false)){
                    possibleMove[id] = "mh" + (char)(65+i) + String.valueOf(j);
                }
                id = id + 1;
            }
        }
        for(int i=0; i < verticalWall[0].length; i++){
            for(int j=1; j < verticalWall.length; j++){
                if(jouer(plateau, horizontalWall, verticalWall, intersection, wall, positionJoueur, player, "mv" + (char)(65+i) + String.valueOf(j), false)){
                    possibleMove[id] = "mv" + (char)(65+i) + String.valueOf(j);
                }
                id = id + 1;
            }
        }
        if(noNull){
            for(int i=0; i < possibleMove.length; i++){
                if(possibleMove[i] != null){
                    nbCoupPossible = nbCoupPossible + 1;
                }
            }
            String[] possibleMoveNoNull = new String[nbCoupPossible];
            id = 0;
            for(int i=0; i < possibleMove.length; i++){
                if(possibleMove[i] != null){
                    possibleMoveNoNull[id] = possibleMove[i];
                    id = id + 1;
                }
            }
            return possibleMoveNoNull;
        }
        return possibleMove;
    }

    int[][] findShortestPath(int[][] plateau, int[][] horizontalWall, int[][] verticalWall, int player, int[] positionJoueur) {
        int[][] newPlateau = new int[plateau.length][plateau[0].length];
        int[][] distance = new int[plateau.length][plateau[0].length];

        for (int i = 0; i < plateau.length; i++) {
            for (int j = 0; j < plateau[0].length; j++) {
                newPlateau[i][j] = 0;
                distance[i][j] = -1;
            }
        }

        newPlateau[positionJoueur[0]][positionJoueur[1]] = player + 1;
        distance[positionJoueur[0]][positionJoueur[1]] = 0;
        distance = findShortestPath(newPlateau, horizontalWall, verticalWall, player, positionJoueur, distance);

        return distance;
    }

    int[][] findShortestPath(int[][] plateau, int[][] horizontalWall, int[][] verticalWall, int player, int[] positionJoueur, int[][] distance) {
        if (isWin(plateau, player)) {
            return distance;
        }

        Queue<int[]> queue = new ArrayDeque<>();
        queue.offer(positionJoueur);

        while (!queue.isEmpty()) {
            int[] currentPosition = queue.poll();
            int x = currentPosition[1];
            int y = currentPosition[0];

            for (int i = 0; i < 4; i++) {
                int newX = x;
                int newY = y;

                if (i == 0) {
                    newX = x + 1;
                } else if (i == 1) {
                    newX = x - 1;
                } else if (i == 2) {
                    newY = y + 1;
                } else if (i == 3) {
                    newY = y - 1;
                }

                if (newY >= 0 && newY < plateau.length && newX >= 0 && newX < plateau[0].length) {
                    if (plateau[newY][newX] == 0) {
                        if ((i == 0 && verticalWall[y][x] == 0) ||
                                (i == 1 && verticalWall[y][x - 1] == 0) ||
                                (i == 2 && horizontalWall[y][x] == 0) ||
                                (i == 3 && horizontalWall[y - 1][x] == 0)) {

                            plateau[newY][newX] = player + 1;
                            distance[newY][newX] = distance[y][x] + 1;
                            queue.offer(new int[]{newY, newX});
                            if (isWin(plateau, player)) {
                                distance[newY][newX] = distance[y][x] + 2;
                                return distance;
                            }
                        }
                    }
                }
            }
        }
        return distance;
    }

    String[] distanceToPath(int[][] distance, int[][] horizontalWall, int[][] verticalWall){
        int max = distance[0][0];
        int x = 0;
        int y = 0;
        String[] path;
        for(int i=0; i < distance.length; i++){
            for(int j=0; j < distance[0].length; j++){
                if(max < distance[i][j]){
                    max = distance[i][j];
                    y = i;
                    x = j;
                }
            }
        }
        max = max - 1;
        if(max < 0){
            max = 0;
        }
        path = new String[max];
        for(int i=path.length - 1; i >= 0; i--){
            if(x > 0 && distance[y][x-1] == max - 1 && verticalWall[y][x-1] == 0){
                x = x - 1;
                max = max - 1;
                path[i] = "pd";
            }else if(x < distance[0].length - 1 && distance[y][x+1] == max - 1  && verticalWall[y][x] == 0){
                x = x + 1;
                max = max - 1;
                path[i] = "pg";
            }else if(y > 0 && distance[y-1][x] == max - 1 && horizontalWall[y-1][x] == 0){
                y = y - 1;
                max = max - 1;
                path[i] = "pb";
            }else if(y < distance.length - 1 && distance[y+1][x] == max - 1 && horizontalWall[y][x] == 0){
                y = y + 1;
                max = max - 1;
                path[i] = "ph";
            }
        }
        return path;
    }

    boolean isWin(int[][] plateau, int player){
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

    private boolean isWallEmpty(int[] wall){
        for(int i=0; i < wall.length; i++){
            if(wall[i] == 1){
                return false;
            }
        }
        return true;
    }

    private void removeWall(int[] wall){
        for(int i=0; i < wall.length; i++){
            if(wall[i] == 1){
                wall[i] = 0;
            }
        }
    }

    private GameAction removeWall(QuoridorWallPot wall){
        GameAction move = null;
        for(int i=wall.getNbCols()-1; i >= 0; i--){
            if(wall.isElementAt(0, i)){
                move = new RemoveAction(model, wall.getElement(0,i));
                return move;
            }
        }
        return move;
    }

    boolean isBlocked(int[][] plateau, int[][] horizontalWall, int[][] verticalWall, int[][] positionJoueur){
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
    boolean isBlocked(int[][] plateau, int[][] horizontalWall, int[][] verticalWall, int player, int[] positionJoueur){
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

    boolean jouer(int[][] plateau, int[][] horizontalWall, int[][] verticalWall, int[][] intersection, int[] wallCurrentPlayer, int[][] positionJoueur, int currentPlayer, String line, boolean applyMove){
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

        if(line.length() < 2){
            return false;
        }
        if(line.charAt(0) == 'M' || line.charAt(0) == 'm'){
            if(isWallEmpty(wallCurrentPlayer)){
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
            }else {
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
                                removeWall(wallCurrentPlayer);
                                horizontalWall[ligne-1][col] = currentPlayer + 1;
                                horizontalWall[ligne-1][col+1] = currentPlayer + 1;
                                intersection[ligne-1][col] = currentPlayer + 1;
                                intersection[ligne-1][col] = 1;
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
                                removeWall(wallCurrentPlayer);
                                verticalWall[ligne-1][col] = currentPlayer + 1;
                                verticalWall[ligne][col] = currentPlayer + 1;
                                intersection[ligne-1][col] = currentPlayer + 1;
                                intersection[ligne-1][col] = 2;
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
                                    plateau[positionJoueur[currentPlayer][0]][positionJoueur[currentPlayer][1]] = 0;
                                    positionJoueur[currentPlayer][1] = positionJoueur[currentPlayer][1] + x + x2;
                                    positionJoueur[currentPlayer][0] = positionJoueur[currentPlayer][0] + y + y2;
                                    plateau[positionJoueur[currentPlayer][0]][positionJoueur[currentPlayer][1]] = currentPlayer + 1;
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
                            plateau[positionJoueur[currentPlayer][0]][positionJoueur[currentPlayer][1]] = 0;
                            positionJoueur[currentPlayer][1] = positionJoueur[currentPlayer][1] + x;
                            positionJoueur[currentPlayer][0] = positionJoueur[currentPlayer][0] + y;
                            plateau[positionJoueur[currentPlayer][0]][positionJoueur[currentPlayer][1]] = currentPlayer + 1;
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
                                        plateau[positionJoueur[currentPlayer][0]][positionJoueur[currentPlayer][1]] = 0;
                                        positionJoueur[currentPlayer][1] = positionJoueur[currentPlayer][1] + 2*x;
                                        positionJoueur[currentPlayer][0] = positionJoueur[currentPlayer][0] + 2*y;
                                        plateau[positionJoueur[currentPlayer][0]][positionJoueur[currentPlayer][1]] = currentPlayer + 1;
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
        return true;
    }
}