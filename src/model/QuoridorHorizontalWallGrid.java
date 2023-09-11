package model;

import boardifier.model.GameStageModel;
import boardifier.model.GridElement;

public class QuoridorHorizontalWallGrid extends GridElement {

    public QuoridorHorizontalWallGrid(int x, int y, GameStageModel gameStageModel) {
        super("horizontalwallgrid", x, y, 8, 9, gameStageModel);
        // Call the constructor of the superclass GridElement
        // Set the element type of this grid element to "horizontalwallgrid"
        // Set the position (x, y) of the grid element
        // Set the number of rows and columns in the grid to 8 and 9 respectively
        // Set the game stage model for the grid element
    }
}
