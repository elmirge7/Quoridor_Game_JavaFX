package control;

import boardifier.control.ActionPlayer;
import boardifier.control.Controller;
import boardifier.control.ControllerMouse;
import boardifier.model.*;
import boardifier.model.action.ActionList;
import boardifier.model.action.GameAction;
import boardifier.model.action.MoveAction;
import boardifier.model.animation.AnimationTypes;
import boardifier.view.GridLook;
import boardifier.view.View;
import javafx.event.*;
import javafx.geometry.Point2D;
import javafx.scene.input.*;
import model.QuoridorBoard;
import model.QuoridorWallPot;
import model.QuoridorStageModel;
import model.QuoridorPawn;

import java.util.List;

/**
 * A basic mouse controller that just grabs the mouse clicks and prints out some informations.
 * It gets the elements of the scene that are at the clicked position and prints them.
 */
public class QuoridorControllerMouse extends ControllerMouse implements EventHandler<MouseEvent> {
    public QuoridorControllerMouse(Model model, View view, Controller control) {
        super(model, view, control);
    }

    public void handle(MouseEvent event) {
        // if mouse event capture is disabled in the model, just return
        if (!model.isCaptureMouseEvent()) return;

        // get the clic x,y in the Quoridor scene (this includes the menu bar if it exists)
        Coord2D clic = new Coord2D(event.getSceneX(), event.getSceneY());
        // get elements at that position
        List<GameElement> list = control.elementsAt(clic);
        // for debug, uncomment next instructions to display x,y and elements at that postion
        /*
        System.out.println("click in " + event.getSceneX() + "," + event.getSceneY());
        for (GameElement element : list) {
            System.out.println(element);
        }
         */
        QuoridorStageModel stageModel = (QuoridorStageModel) model.getGameStage();

        if (stageModel.getState() == QuoridorStageModel.STATE_SELECTPAWN) {
            for (GameElement element : list) {
                if (element.getType() == ElementTypes.getType("pawn")) {
                    QuoridorPawn pawn = (QuoridorPawn) element;
                    // check if color of the pawn corresponds to the current player id
                    if (pawn.getPlayerID() == model.getIdPlayer()) {
                        element.toggleSelected();
                        stageModel.setState(QuoridorStageModel.STATE_SELECTDEST);
                        return; // do not allow another element to be selected
                    }
                }
            }
        } else if (stageModel.getState() == QuoridorStageModel.STATE_SELECTDEST) {
            // first check if the click is on the currently selected pawn. In this case, unselect it
            for (GameElement element : list) {
                if (element.isSelected()) {
                    element.toggleSelected();
                    stageModel.setState(QuoridorStageModel.STATE_SELECTPAWN);
                    return;
                }
            }
            // secondly, check if the board has been clicked. If not, just return
            boolean boardClicked = false;
            for (GameElement element : list) {
                if (element.equals(stageModel.getBoard())) {
                    boardClicked = true;
                    break;
                }
            }
            if (!boardClicked) return;
            // get the board, pot, and the selected pawn to simplify code in the following
            QuoridorBoard board = stageModel.getBoard();
            // by default, get the black pot
            QuoridorWallPot pot = stageModel.getBluePot();
            // but if it's player2's turn, get the red pot
            if (model.getIdPlayer() == 1) {
                pot = stageModel.getRedPot();
            }
            GameElement pawn = model.getSelected().get(0);

            // thirdly, get the clicked cell in the 3x3 board
            GridLook lookBoard = (GridLook) control.getElementLook(board);
            int[] dest = lookBoard.getCellFromSceneLocation(clic);
            // get the cell in the pot that owns the selected pawn
            int[] from = pawn.getGrid().getElementCell(pawn);
            System.out.println("try to move pawn from pot " + from[0] + "," + from[1] + " to board " + dest[0] + "," + dest[1]);
            // if the destination cell is valid for the selected pawn

                // build the list of actions to do, and pass to the next player when done
                ActionList actions = new ActionList(true);
                // determine the destination point in the root pane
                Coord2D center = lookBoard.getRootPaneLocationForCellCenter(dest[0], dest[1]);
                // create an action with a linear move animation, with 10 pixel/frame
                GameAction move = new MoveAction(model, pawn, "quoridorboard", dest[0], dest[1], AnimationTypes.MOVE_LINEARPROP, center.getX(), center.getY(), 10);
                // add the action to the action list.
                actions.addSingleAction(move);
                stageModel.unselectAll();
                stageModel.setState(QuoridorStageModel.STATE_SELECTPAWN);
                ActionPlayer play = new ActionPlayer(model, control, actions);
                play.start();

        }
    }
}

