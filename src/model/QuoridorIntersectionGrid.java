package model;

import boardifier.model.GameStageModel;
import boardifier.model.GridElement;

public class QuoridorIntersectionGrid extends GridElement {
    public QuoridorIntersectionGrid(int x, int y, GameStageModel gameStageModel) {
        super("intersectiongrid", x, y, 8, 8, gameStageModel);
    }
}
