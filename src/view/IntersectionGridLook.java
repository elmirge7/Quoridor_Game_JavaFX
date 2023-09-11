
package view;

import boardifier.model.GridElement;
import boardifier.view.GridLook;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

// Class representing the look of an intersection in a grid
public class IntersectionGridLook extends GridLook {

    public IntersectionGridLook(int cellWidth, int cellHeight, GridElement gridElement) {
        super(360,360, (400-20)/9, (400-20)/9, 2, "TRANSPARENT", gridElement);
    }
    // Creates the shape of the grid element

// ...

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


    // Method called when the look changes. Here, we do nothing.
    @Override
    public void onChange() {
        //nothing
    }
}
