package view;

import boardifier.model.GameStageModel;
import boardifier.view.*;
import model.GameElementBackground;
import model.QuoridorHorizontalWall;
import model.QuoridorStageModel;

// This class is responsible for creating the visual representation of the Quoridor game
public class QuoridorStageView extends GameStageView {
    // Constructor takes a name and a GameStageModel
    public QuoridorStageView(String name, GameStageModel gameStageModel) {
        super(name, gameStageModel);  // Call the parent constructor
        width = 1000;
        height = 550;
    }

    // This method is responsible for creating the different looks of the game elements
    @Override
    public void createLooks() {

        QuoridorStageModel model = (QuoridorStageModel)gameStageModel;  // Cast the GameStageModel to QuoridorStageModel
        GameElementBackground pd = new GameElementBackground(model);
        addLook(new BackgroundLook(1000, 550, 1, "/resources/background_game_hd.png",pd));
        // Create looks for horizontal and vertical walls, intersections, board, wall pots, and pawns
        addLook(new HorizontalWallGridLook(20, 500, model.getHorizontalWallGrid()));
        addLook(new VerticalWallGridLook(20, 10, model.getVerticalWallGrid()));
        addLook(new IntersectionGridLook(20, 20, model.getIntersectionGrid()));
        //addLook(new GridLook(8, 4, 50, 50, 0, "#000000", model.getBoard()));
        addLook(new QuoridorBoardLook(400, model.getBoard()));
        addLook(new WallPotLook(40,10, model.getBluePot()));
        addLook(new WallPotLook(40, 10, model.getRedPot()));
        addLook(new PawnLook(10,model.getBluePawn()));
        addLook(new PawnLook(10,model.getRedPawn()));

        // Create looks for individual walls
        for(int i=0;i<10;i++) {
            addLook(new WallLook(model.getBlueWalls()[i]));
            addLook(new WallLook(model.getRedWalls()[i]));
        }

        // Create looks for horizontal walls
        for(int i=0;i<model.getHorizontalWalls().length;i++) {
            for(int j=0;j<model.getHorizontalWalls()[0].length;j++) {
                addLook(new QuoridorHorizontalWallLook(model.getHorizontalWalls()[i][j]));
            }
        }

        // Create looks for vertical walls
        for(int i=0;i<model.getVerticalWalls().length;i++) {
            for(int j=0;j<model.getVerticalWalls()[0].length;j++) {
                addLook(new QuoridorVerticalWallLook(model.getVerticalWalls()[i][j]));
            }
        }

        // Create looks for intersections
        for(int i=0;i<model.getIntersections().length;i++) {
            for(int j=0;j<model.getIntersections()[0].length;j++) {
                addLook(new IntersectionLook(model.getIntersections()[i][j]));
            }
        }
        addLook(new TextLook(24, "WHITE", model.getPlayerName()));
    }

    // This method sets the look of a horizontal wall
    public void setHorizontalWallLook(QuoridorHorizontalWall horizontalWall){
        addLook(new QuoridorHorizontalWallLook(horizontalWall));
    }
}
