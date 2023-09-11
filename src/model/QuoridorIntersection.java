package model;

import boardifier.model.ElementTypes;
import boardifier.model.GameElement;
import boardifier.model.GameStageModel;

public class QuoridorIntersection extends GameElement{

    private int color;
    private int direction;
    public static int INTERSECTION_BLUE = 0;
    public static int INTERSECTION_RED = 1;
    public static int horizontal = 0;
    public static int vertical = 1;

    public QuoridorIntersection(int color, int direction, GameStageModel gameStageModel) {
        super(gameStageModel);  // Call the constructor of the superclass GameElement
        ElementTypes.register("intersection", 58);  // Register the element type "intersection" with a unique ID
        type = ElementTypes.getType("intersection");  // Set the element type of this object to "intersection"
        this.color = color;  // Set the color of the intersection
        this.direction = direction;  // Set the direction of the intersection
    }

    public int getColor() {
        return color;  // Return the color of the intersection
    }

    public int getDirection() {
        return direction;  // Return the direction of the intersection
    }

    public void setColor(int color) {
        this.color = color;  // Set the color of the intersection
    }

    public void setDirection(int direction) {
        this.direction = direction;  // Set the direction of the intersection
    }
}
