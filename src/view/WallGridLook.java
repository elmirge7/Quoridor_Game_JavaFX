package view;

import boardifier.model.GridElement;
import boardifier.view.GridLook;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

/**
 * The WallGridLook class provides a visual representation of a grid containing walls.
 * It extends the GridLook class.
 */
public class WallGridLook extends GridLook {

    /**
     * Constructs a new WallGridLook.
     *
     * @param cellWidth    the width of each cell.
     * @param cellHeight   the height of each cell.
     * @param gridElement  the GridElement representing the grid.
     */
    public WallGridLook(int cellWidth, int cellHeight, GridElement gridElement) {
        super(100, 100, cellWidth, cellHeight, 2, "BLACK", gridElement);
        createShape();
    }

    /**
     * Creates the shape of the wall grid.
     */
    protected void createShape() {
        // Create a rectangle representing the wall grid
        Rectangle rectangle = new Rectangle(10 * cellWidth, 10 * cellHeight);
        rectangle.setStroke(Color.BLACK);
        rectangle.setFill(Color.TRANSPARENT);
        addShape(rectangle);

        // Create the vertical lines of the wall grid
        for (int i = 1; i < 5; i++) {
            Line line = new Line(i * cellWidth, 0, i * cellWidth, 5 * cellHeight);
            line.setStroke(Color.BLACK);
            addShape(line);
        }

        // Create the horizontal lines of the wall grid
        for (int i = 1; i < 5; i++) {
            Line line = new Line(0, i * cellHeight, 5 * cellWidth, i * cellHeight);
            line.setStroke(Color.BLACK);
            addShape(line);
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
