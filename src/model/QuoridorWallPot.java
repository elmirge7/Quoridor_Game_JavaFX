package model;

import boardifier.model.GameStageModel;
import boardifier.model.GridElement;

/**
 * The QuoridorWallPot class represents a pot of walls in the game.
 * It is a type of GridElement.
 */
public class QuoridorWallPot extends GridElement {
    /**
     * Constructs a new QuoridorWallPot.
     * @param x the x-coordinate.
     * @param y the y-coordinate.
     * @param gameStageModel the game stage model.
     */
    public QuoridorWallPot(int x, int y, GameStageModel gameStageModel) {
        super("wallpot", x, y, 1, 10, gameStageModel);
    }
}