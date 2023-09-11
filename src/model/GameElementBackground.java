package model;

import boardifier.model.GameElement;
import boardifier.model.GameStageModel;

public class GameElementBackground extends GameElement {
    public GameElementBackground(GameStageModel gameStageModel) {
        super(gameStageModel);
    }

    public GameElementBackground(GameStageModel gameStageModel, int type) {
        super(gameStageModel, type);
    }

    public GameElementBackground(double x, double y, GameStageModel gameStageModel) {
        super(x, y, gameStageModel);
    }

    public GameElementBackground(double x, double y, GameStageModel gameStageModel, int type) {
        super(x, y, gameStageModel, type);
    }
}
