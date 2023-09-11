package model;

import boardifier.control.Controller;
import boardifier.model.*;
import boardifier.view.View;
import control.QuoridorController;
import view.QuoridorView;

public class QuoridorStageModel extends GameStageModel{

    public final static int STATE_SELECTPAWN = 1; // the player must select a pawn
    public final static int STATE_SELECTDEST = 2; // the player must select a destination

    private QuoridorBoard board;  // Declare a private variable of type QuoridorBoard
    private QuoridorWallPot bluePot;  // Declare a private variable of type QuoridorWallPot
    private QuoridorWallPot redPot;  // Declare a private variable of type QuoridorWallPot
    private QuoridorHorizontalWallGrid horizontalWallGrid;  // Declare a private variable of type QuoridorHorizontalWallGrid
    private QuoridorVerticalWallGrid verticalWallGrid;  // Declare a private variable of type QuoridorVerticalWallGrid
    private QuoridorIntersectionGrid intersectionGrid;  // Declare a private variable of type QuoridorIntersectionGrid
    private Wall[] blueWalls;  // Declare a private array variable of type Wall
    private Wall[] redWalls;  // Declare a private array variable of type Wall
    private int blueWallsToPlay;  // Declare a private integer variable named blueWallsToPlay
    private int redWallsToPlay;  // Declare a private integer variable named redWallsToPlay
    private QuoridorPawn bluePawn;  // Declare a private variable of type QuoridorPawn
    private QuoridorPawn redPawn;  // Declare a private variable of type QuoridorPawn
    private QuoridorHorizontalWall horizontalWall;  // Declare a private variable of type QuoridorHorizontalWall
    private QuoridorHorizontalWall[][] horizontalWalls;  // Declare a private 2D array variable of type QuoridorHorizontalWall
    private QuoridorVerticalWall verticalWall;  // Declare a private variable of type QuoridorVerticalWall
    private QuoridorVerticalWall[][] verticalWalls;  // Declare a private 2D array variable of type QuoridorVerticalWall
    private QuoridorIntersection[][] intersections;  // Declare a private 2D array variable of type QuoridorIntersection
    private TextElement playerName;

    public QuoridorStageModel(String name, Model model) {
        super(name, model);  // Call the constructor of the superclass GameStageModel
        state = STATE_SELECTPAWN;
        blueWallsToPlay = 10;  // Set the value of blueWallsToPlay to 10
        redWallsToPlay = 10;  // Set the value of redWallsToPlay to 10
        setupCallbacks();  // Call the setupCallbacks() method
    }

    public QuoridorBoard getBoard() {
        return board;  // Return the value of board
    }
    public void setBoard(QuoridorBoard board) {
        this.board = board;  // Set the value of board
        addGrid(board);  // Add the board to the grid
    }

    public TextElement getPlayerName() {
        return playerName;
    }
    public void setPlayerName(TextElement playerName) {
        this.playerName = playerName;
        addElement(playerName);
    }
    public QuoridorPawn getBluePawn() {
        return bluePawn;
    }
    public void setBluePawn(QuoridorPawn bluePawn) {
        this.bluePawn = bluePawn;
        addElement(bluePawn);
    }

    public QuoridorPawn getRedPawn() {
        return redPawn;
    }
    public void setRedPawn(QuoridorPawn redPawn) {
        this.redPawn = redPawn;
        addElement(redPawn);
    }

    public QuoridorHorizontalWall getHorizontalWall() {
        return horizontalWall;
    }
    public void setHorizontalWall(QuoridorHorizontalWall horizontalWall) {
        this.horizontalWall = horizontalWall;
        addElement(horizontalWall);
    }

    public QuoridorVerticalWall getVerticalWall() {
        return verticalWall;
    }
    public void setVerticalWall(QuoridorVerticalWall verticalWall) {
        this.verticalWall = verticalWall;
        addElement(verticalWall);
    }

    public QuoridorWallPot getBluePot() {
        return bluePot;
    }
    public void setBluePot(QuoridorWallPot bluePot) {
        this.bluePot = bluePot;
        addGrid(bluePot);
    }

    public QuoridorWallPot getRedPot() {
        return redPot;
    }
    public void setRedPot(QuoridorWallPot redPot) {
        this.redPot = redPot;
        addGrid(redPot);
    }

    public QuoridorHorizontalWallGrid getHorizontalWallGrid() {
        return horizontalWallGrid;
    }
    public void setHorizontalWallGrid(QuoridorHorizontalWallGrid horizontalWallGrid) {
        this.horizontalWallGrid = horizontalWallGrid;
        addGrid(horizontalWallGrid);
    }

    public QuoridorVerticalWallGrid getVerticalWallGrid() {
        return verticalWallGrid;
    }
    public void setVerticalWallGrid(QuoridorVerticalWallGrid verticalWallGrid) {
        this.verticalWallGrid = verticalWallGrid;
        addGrid(verticalWallGrid);
    }

    public QuoridorIntersectionGrid getIntersectionGrid() {
        return intersectionGrid;
    }
    public void setIntersectionGrid(QuoridorIntersectionGrid intersectionGrid) {
        this.intersectionGrid = intersectionGrid;
        addGrid(intersectionGrid);
    }

    public Wall[] getBlueWalls() {
        return blueWalls;
    }
    public void setBlueWalls(Wall[] blueWalls) {
        this.blueWalls = blueWalls;
        for(int i=0;i<blueWalls.length;i++) {
            addElement(blueWalls[i]);
        }
    }

    public Wall[] getRedWalls() {
        return redWalls;
    }
    public void setRedWalls(Wall[] redWalls) {
        this.redWalls = redWalls;
        for(int i=0;i<redWalls.length;i++) {
            addElement(redWalls[i]);
        }
    }

    public QuoridorHorizontalWall[][] getHorizontalWalls() {
        return horizontalWalls;
    }
    public void setHorizontalWalls(QuoridorHorizontalWall[][] horizontalWalls) {
        this.horizontalWalls = horizontalWalls;
        for(int i=0;i<horizontalWalls.length;i++) {
            for(int j=0;j<horizontalWalls[0].length;j++) {
                addElement(horizontalWalls[i][j]);
            }
        }
    }

    public QuoridorVerticalWall[][] getVerticalWalls() {
        return verticalWalls;
    }
    public void setVerticalWalls(QuoridorVerticalWall[][] verticalWalls) {
        this.verticalWalls = verticalWalls;
        for(int i=0;i<verticalWalls.length;i++) {
            for(int j=0;j<verticalWalls[0].length;j++) {
                addElement(verticalWalls[i][j]);
            }
        }
    }

    public QuoridorIntersection[][] getIntersections() {
        return intersections;
    }
    public void setIntersections(QuoridorIntersection[][] intersections) {
        this.intersections = intersections;
        for(int i=0;i<intersections.length;i++) {
            for(int j=0;j<intersections[0].length;j++) {
                addElement(intersections[i][j]);
            }
        }
    }

    // Callback setup code commented out
    private void setupCallbacks() {
        /*onPutInGrid( (element, gridDest, rowDest, colDest) -> {
            // just check when walls are put in 3x3 board
            if (gridDest != board) return;
            Wall p = (Wall) element;
            if (p.getColor() == 0) {
                blueWallsToPlay--;
            }
            else {
                redWallsToPlay--;
            }
            if ((blueWallsToPlay == 0) && (redWallsToPlay == 0)) {
                computePartyResult();
            }
        });*/
    }

    // Method implementation commented out
    private void computePartyResult() {
        int idWinner = -1;
        // get the empty cell, which should be in 2D at [0,0], [0,2], [1,1], [2,0] or [2,2]
        // i.e. or in 1D at index 0, 2, 4, 6 or 8
        int i = 0;
        int nbBlue = 0;
        int nbRed = 0;
        int countBlue = 0;
        int countRed = 0;
        Wall p = null;
        int row, col;
        for (i = 0; i < 9; i+=2) {
            if (board.isEmptyAt(i / 3, i % 3)) break;
        }
        // get the 4 adjacent cells (if they exist) starting by the upper one
        row = (i / 3) - 1;
        col = i % 3;
        for (int j = 0; j < 4; j++) {
            // skip invalid cells
            if ((row >= 0) && (row <= 2) && (col >= 0) && (col <= 2)) {
                p = (Wall) (board.getElement(row, col));
                if (p.getColor() == Wall.WALL_BLUE) {
                    nbBlue++;
                    countBlue += p.getNumber();
                } else {
                    nbRed++;
                    countRed += p.getNumber();
                }
            }
            // change row & col to set them at the correct value for the next iteration
            if ((j==0) || (j==2)) {
                row++;
                col--;
            }
            else if (j==1) {
                col += 2;
            }
        }

        System.out.println("nb blue: "+nbBlue+", nb red: "+nbRed+", count blue: "+countBlue+", count red: "+countRed);
        // decide whose winning
        if (nbBlue < nbRed) {
            idWinner = 0;
        }
        else if (nbBlue > nbRed) {
            idWinner = 1;
        }
        else {
            if (countBlue < countRed) {
                idWinner = 0;
            }
            else if (countBlue > countRed) {
                idWinner = 1;
            }
        }
        // set the winner
        model.setIdWinner(idWinner);
        // stop de the game
        model.stopStage();

    }

    @Override
    public StageElementsFactory getDefaultElementFactory() {
        return new QuoridorStageFactory(this); // Return a new instance of QuoridorStageFactory
    }
}
