package model;

import boardifier.model.ElementTypes;
import boardifier.model.GameElement;
import boardifier.model.GameStageModel;
import boardifier.model.animation.AnimationStep;
import boardifier.view.GridGeometry;

/**
 * The QuoridorPawn class represents a pawn in the game.
 * It is a type of GameElement and has an ID, column, and line.
 */
public class QuoridorPawn extends GameElement{
    private int playerID;
    private int col;
    private int line;
    public static int PAWN_BLACK = 0;
    public static int PAWN_RED = 1;

    /**
     * Constructs a new QuoridorPawn.
     * @param playerID the ID of the player.
     * @param col the column.
     * @param line the line.
     * @param gameStageModel the game stage model.
     */
    public QuoridorPawn(int playerID, int col, int line, GameStageModel gameStageModel) {
        super(gameStageModel, playerID);
        this.playerID = playerID;
        this.col = col;
        this.line = line;
        ElementTypes.register("pawn", 55);
        type = ElementTypes.getType("pawn");
    }

    public int getPlayerID(){
        return this.playerID;
    }
    public int getCol(){
        return this.col;
    }
    public int getLine(){
        return this.line;
    }
    public void setPlayerID(int playerID){
        this.playerID = playerID;
    }
    public void setCol(int col){
        this.col = col;
    }
    public void setLine(int line){
        this.line = line;
    }
    public void update(double width, double height, GridGeometry gridGeometry) {
        // if must be animated, move the pawn
        if (animation != null) {
            AnimationStep step = animation.next();
            if (step != null) {
                setLocation(step.getInt(0), step.getInt(1));
            } else {
                animation = null;
            }
        }
    }

}
