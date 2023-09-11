package model;

import boardifier.model.ElementTypes;
import boardifier.model.GameElement;
import boardifier.model.GameStageModel;

/**
 * QuoridorHorizontalWall represents a horizontal wall in the Quoridor game.
 */
public class QuoridorHorizontalWall extends GameElement{

    private int color; // Represents the color of the wall
    public static int WALL_BLUE = 0; // Numeric representation for the blue color
    public static int WALL_RED = 1; // Numeric representation for the red color

    /**
     * Constructor for the QuoridorHorizontalWall
     * @param color the color of the wall
     * @param gameStageModel reference to the game stage model
     */
    public QuoridorHorizontalWall(int color, GameStageModel gameStageModel) {
        super(gameStageModel); // call the super class constructor
        ElementTypes.register("horizontalWall", 56); // Register the "horizontalWall" type
        type = ElementTypes.getType("horizontalWall"); // Set the type of the element
        this.color = color; // Set the color of the wall
    }

    /**
     * Getter for the color
     * @return color of the wall
     */
    public int getColor() {
        return color;
    }

    /**
     * Setter for the color
     * @param color new color of the wall
     */
    public void setColor(int color){
        this.color = color;
    }
}
