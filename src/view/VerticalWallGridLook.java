package view;

import boardifier.model.GridElement;
import boardifier.view.GridLook;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

/**
 * The VerticalWallGridLook class represents the graphical appearance of a vertical wall grid.
 */
public class VerticalWallGridLook extends GridLook {

    /**
     * Constructs a new VerticalWallGridLook.
     * @param cellWidth the width of a cell.
     * @param cellHeight the height of a cell.
     * @param gridElement the grid element.
     */
    public VerticalWallGridLook(int cellWidth, int cellHeight, GridElement gridElement) {
        super(400, 360, (400-20)/9, (400-20)/9, 10, "TRANSPARENT", gridElement);
        createShape();
    }

    /**
     * Creates the visual shape of the grid.

    protected void createShape() {
        // Create a rectangle representing the vertical wall grid
        Rectangle rectangle = new Rectangle(5 * cellWidth, 5 * cellHeight);
        rectangle.setStroke(Color.BLACK);
        rectangle.setFill(Color.TRANSPARENT);
        addShape(rectangle);
    }

     */
    // Je ne sais pas quel est le bon create Shape ?

    protected void createShape() {
        // Gets the grid element
        GridElement gridElement = (GridElement) element;
        int nbRows = gridElement.getNbRows();
        int nbCols = gridElement.getNbCols();

        // Clear the existing shapes
        clearShapes();

        // Create a new shape for each cell
        for (int i = 0; i < nbRows; i++) {
            for (int j = 0; j < nbCols; j++) {
                // Create a rectangle shape for each cell
                Shape shape = new Rectangle(cellWidth, cellHeight);
                shape.setFill(Color.TRANSPARENT); // Set the shape color to transparent

                // Compute the position of the shape based on cellWidth, cellHeight, and grid position
                double x = j * cellWidth;
                double y = i * cellHeight;
                shape.setLayoutX(x);
                shape.setLayoutY(y);

                // Add the shape to the list of shapes
                addShape(shape);
            }
        }
    }


    /**
     * An empty method as the visual appearance of the grid does not change.
     */
    @Override
    public void onChange() {
        // Do nothing
    }
}
