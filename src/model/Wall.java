package model;

import boardifier.model.GameElement;
import boardifier.model.GameStageModel;

public class Wall extends GameElement {

    private int number;
    private int color;
    public static int WALL_BLUE = 0;
    public static int WALL_RED = 1;

    /**
     * Constructs a new Wall.
     * @param number the number of the wall.
     * @param color the color of the wall.
     * @param gameStageModel the game stage model.
     */
    public Wall(int number, int color, GameStageModel gameStageModel) {
        super(gameStageModel);
        this.number = number;
        this.color = color;
    }

    /**
     * Returns the number of the wall.
     * @return the number of the wall.
     */
    public int getNumber() {
        return number;
    }

    /**
     * Returns the color of the wall.
     * @return the color of the wall.
     */
    public int getColor() {
        return color;
    }
}
