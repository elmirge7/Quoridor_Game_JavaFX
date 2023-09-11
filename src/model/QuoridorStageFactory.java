package model;

import boardifier.model.GameElement;
import boardifier.model.GameStageModel;
import boardifier.model.StageElementsFactory;
import boardifier.model.TextElement;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Background;


public class QuoridorStageFactory extends StageElementsFactory {
    private QuoridorStageModel stageModel;  // Declare a private variable of type QuoridorStageModel

    public QuoridorStageFactory(GameStageModel gameStageModel) {
        super(gameStageModel);  // Call the constructor of the superclass StageElementsFactory
        stageModel = (QuoridorStageModel) gameStageModel;  // Cast the gameStageModel to QuoridorStageModel and assign it to stageModel
    }

    @Override
    public void setup() {

        // create the board
        stageModel.setBoard(new QuoridorBoard(175, 75, stageModel));  // Create a new QuoridorBoard and set it as the board in stageModel

        // create the pots
        QuoridorWallPot bluePot = new QuoridorWallPot(155, 495, stageModel);  // Create a new QuoridorWallPot for blue walls
        stageModel.setBluePot(bluePot);  // Set bluePot as the blue pot in stageModel
        QuoridorWallPot redPot = new QuoridorWallPot(155, 5, stageModel);  // Create a new QuoridorWallPot for red walls
        stageModel.setRedPot(redPot);  // Set redPot as the red pot in stageModel

        // create and set blue and red pawns
        QuoridorPawn bluePawn = new QuoridorPawn(0, 4, 8, stageModel);  // Create a new QuoridorPawn for the blue player
        stageModel.setBluePawn(bluePawn);  // Set bluePawn as the blue pawn in stageModel
        stageModel.getBoard().putElement(bluePawn, 8, 4);  // Put the blue pawn on the board at position (8, 4)
        QuoridorPawn redPawn = new QuoridorPawn(1, 4, 0, stageModel);  // Create a new QuoridorPawn for the red player
        stageModel.setRedPawn(redPawn);  // Set redPawn as the red pawn in stageModel
        stageModel.getBoard().putElement(redPawn, 0, 4);  // Put the red pawn on the board at position (0, 4)

        // create and set blue and red walls
        Wall[] blueWalls = new Wall[10];  // Create an array of Wall objects for blue walls
        for (int i = 0; i < 10; i++) {
            blueWalls[i] = new Wall(i + 1, Wall.WALL_BLUE, stageModel);  // Create a new Wall object for each blue wall
        }
        stageModel.setBlueWalls(blueWalls);  // Set blueWalls as the blue walls in stageModel

        Wall[] redWalls = new Wall[10];  // Create an array of Wall objects for red walls
        for (int i = 0; i < 10; i++) {
            redWalls[i] = new Wall(i + 1, Wall.WALL_RED, stageModel);  // Create a new Wall object for each red wall
        }
        stageModel.setRedWalls(redWalls);  // Set redWalls as the red walls in stageModel
// assign walls to their respective pot
        for (int i = 0; i < 10; i++) {
            bluePot.putElement(blueWalls[i], 0, i);  // Put each blue wall in the blue pot at the specified position
            redPot.putElement(redWalls[i], 0, i);  // Put each red wall in the red pot at the specified position
        }

// create and set horizontal walls
        QuoridorHorizontalWallGrid horizontalWallGrid = new QuoridorHorizontalWallGrid(150+25, 75+20, stageModel);  // Create a new QuoridorHorizontalWallGrid
        stageModel.setHorizontalWallGrid(horizontalWallGrid);  // Set horizontalWallGrid as the horizontal wall grid in stageModel

        QuoridorHorizontalWall[][] horizontalWalls = new QuoridorHorizontalWall[8][9];  // Create a 2D array of QuoridorHorizontalWall objects
        for (int i = 0; i < horizontalWalls.length; i++) {
            for (int j = 0; j < horizontalWalls[0].length; j++) {
                horizontalWalls[i][j] = new QuoridorHorizontalWall(QuoridorHorizontalWall.WALL_RED, stageModel);  // Create a new QuoridorHorizontalWall object
                horizontalWalls[i][j].setVisible(false);  // Set the visibility of the wall to false
            }
        }
        stageModel.setHorizontalWalls(horizontalWalls);  // Set horizontalWalls as the horizontal walls in stageModel

        for (int i = 0; i < horizontalWalls.length; i++) {
            for (int j = 0; j < horizontalWalls[0].length; j++) {
                horizontalWallGrid.putElement(horizontalWalls[i][j], i, j);  // Put each horizontal wall in the horizontal wall grid at the specified position
            }
        }

// create and set vertical walls
        QuoridorVerticalWallGrid verticalWallGrid = new QuoridorVerticalWallGrid(172+25, 50+20, stageModel);  // Create a new QuoridorVerticalWallGrid
        stageModel.setVerticalWallGrid(verticalWallGrid);  // Set verticalWallGrid as the vertical wall grid in stageModel

        QuoridorVerticalWall[][] verticalWalls = new QuoridorVerticalWall[9][8];  // Create a 2D array of QuoridorVerticalWall objects
        for (int i = 0; i < verticalWalls.length; i++) {
            for (int j = 0; j < verticalWalls[0].length; j++) {
                verticalWalls[i][j] = new QuoridorVerticalWall(QuoridorVerticalWall.WALL_RED, stageModel);  // Create a new QuoridorVerticalWall object
                verticalWalls[i][j].setVisible(false);  // Set the visibility of the wall to false
            }
        }
        stageModel.setVerticalWalls(verticalWalls);  // Set verticalWalls as the vertical walls in stageModel

        for (int i = 0; i < verticalWalls.length; i++) {
            for (int j = 0; j < verticalWalls[0].length; j++) {
                verticalWallGrid.putElement(verticalWalls[i][j], i, j);  // Put each vertical wall in the vertical wall grid at the specified position
            }
        }

// create and set intersections
        QuoridorIntersectionGrid intersectionGrid = new QuoridorIntersectionGrid(180+25, 83+20, stageModel);  // Create a new QuoridorIntersectionGrid
        stageModel.setIntersectionGrid(intersectionGrid);  // Set intersectionGrid as the intersection grid in stageModel

        QuoridorIntersection[][] intersections = new QuoridorIntersection[8][8];  // Create a 2D array of QuoridorIntersection objects
        for (int i = 0; i < intersections.length; i++) {
            for (int j = 0; j < intersections[0].length; j++) {
                intersections[i][j] = new QuoridorIntersection(QuoridorIntersection.INTERSECTION_RED, QuoridorIntersection.horizontal, stageModel);  // Create a new QuoridorIntersection object
                intersections[i][j].setVisible(false);  // Set the visibility of the intersection to false
            }
        }
        stageModel.setIntersections(intersections);  // Set intersections as the intersections in stageModel

        for (int i = 0; i < intersections.length; i++) {
            for (int j = 0; j < intersections[0].length; j++) {
                intersectionGrid.putElement(intersections[i][j], i, j);  // Put each intersection in the intersection grid at the specified position
            }
        }
        TextElement text = new TextElement(stageModel.getCurrentPlayerName(), stageModel);
        text.setLocation(10,30);
        text.setLocationType(GameElement.LOCATION_TOPLEFT);
        stageModel.setPlayerName(text);
    }
}
