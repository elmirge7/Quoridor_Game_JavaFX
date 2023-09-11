
package model;

import boardifier.model.GameStageModel;
import boardifier.model.GridElement;

// Class representing a vertical wall grid in the game of Quoridor
public class QuoridorVerticalWallGrid extends GridElement {

    // Constructor
    // Instantiates a vertical wall grid element at the given x, y coordinates in the game stage
    public QuoridorVerticalWallGrid(int x, int y, GameStageModel gameStageModel) {
        // Calls the superclass constructor with the grid type as "horizontalwallgrid"
        // and the grid dimensions as 9 rows and 8 columns
        super("horizontalwallgrid", x, y, 9, 8, gameStageModel);
    }
}

