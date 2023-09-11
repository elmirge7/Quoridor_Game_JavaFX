package model;

import boardifier.model.ElementTypes;
import boardifier.model.GameElement;
import boardifier.model.GameStageModel;

/**
 * The QuoridorVerticalWall class represents a vertical wall in the game.
 * It is a type of GameElement.
 */
public class QuoridorVerticalWall extends GameElement{
    private int color;
    public static int WALL_BLUE = 0;
    public static int WALL_RED = 1;

    /**
     * Constructs a new QuoridorVerticalWall.
     * @param color the color of the wall.
     * @param gameStageModel the game stage model.
     */
    public QuoridorVerticalWall(int color, GameStageModel gameStageModel) {
        super(gameStageModel);
        ElementTypes.register("verticalWall", 57);
        type = ElementTypes.getType("verticalWall");
        this.color = color;
    }

    /**
     * Returns the color of the wall.
     * @return the color of the wall.
     */
    public int getColor() {
        return color;
    }

    /**
     * Sets the color of the wall.
     * @param color the new color.
     */
    public void setColor(int color){
        this.color = color;
    }
}
