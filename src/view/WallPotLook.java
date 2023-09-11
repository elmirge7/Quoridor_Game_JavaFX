package view;

import boardifier.model.GridElement;
import boardifier.view.GridLook;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.QuoridorBoard;

public class WallPotLook extends GridLook {

    public WallPotLook(int cellWidth, int cellHeight, GridElement gridElement) {
        super(400,50,cellWidth, cellHeight,20, "TRANSPARENT", gridElement);
    }

    protected void createShape() {
        // draw cells
        GridElement gridElement = (GridElement) element;
        int nbRows = gridElement.getNbRows();
        int nbCols = gridElement.getNbCols();
        for(int i = 0; i < nbRows; i++){
            for(int j = 0; j < cellWidth/2; j++){
                createRectangle(j, i, cellWidth, cellHeight);
            }
        }
    }

    private void createRectangle(int column, int row, int cellWidth, int cellHeight) {
        Rectangle rectangle = new Rectangle(column * cellWidth, row * cellHeight, cellWidth, cellHeight);
        rectangle.setFill(Color.TRANSPARENT);
        rectangle.setStroke(Color.BLACK);
        addShape(rectangle);
    }

    @Override
    public void onChange() {
        // Do nothing
    }
}
