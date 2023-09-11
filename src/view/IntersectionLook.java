package view;

import boardifier.model.GameElement;
import boardifier.view.ElementLook;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.QuoridorIntersection;

/**
 * The IntersectionLook class provides a visual representation of a QuoridorIntersection.
 * It extends the ElementLook class.
 */
public class IntersectionLook extends ElementLook {

    /**
     * Constructs a new IntersectionLook.
     *
     * @param element the QuoridorIntersection element.
     */
    public IntersectionLook(GameElement element) {
        super(element, 7);
        QuoridorIntersection intersection = (QuoridorIntersection) element;
        String color = "";
        char character = (char) 0;
        if (intersection.getDirection() == QuoridorIntersection.horizontal) {
            character = (char) 9632;
            if (intersection.getColor() == QuoridorIntersection.INTERSECTION_BLUE) {
                color = "BLUE";
            } else if (intersection.getColor() == QuoridorIntersection.INTERSECTION_RED) {
                color = "RED";
            }
        } else if (intersection.getDirection() == QuoridorIntersection.vertical) {
            character = (char) 32;
            if (intersection.getColor() == QuoridorIntersection.INTERSECTION_BLUE) {
                color = "LIGHTBLUE";
            } else if (intersection.getColor() == QuoridorIntersection.INTERSECTION_RED) {
                color = "LIGHTCORAL";
            }
        }
        Rectangle rectangle = new Rectangle(10, 10);
        rectangle.setStroke(Color.TRANSPARENT);
        addShape(rectangle);
    }

    /**
     * Updates the visual representation of the QuoridorIntersection.
     * This method is called when there is a change in the intersection's look.
     */
    @Override
    public void onChange() {
        QuoridorIntersection intersection = (QuoridorIntersection) element;
        String color = "";
        char character = (char) 0;
        if (intersection.getDirection() == QuoridorIntersection.horizontal) {
            character = (char) 9632;
            if (intersection.getColor() == QuoridorIntersection.INTERSECTION_BLUE) {
                color = "BLUE";
            } else if (intersection.getColor() == QuoridorIntersection.INTERSECTION_RED) {
                color = "RED";
            }
        } else if (intersection.getDirection() == QuoridorIntersection.vertical) {
            character = (char) 32;
            if (intersection.getColor() == QuoridorIntersection.INTERSECTION_BLUE) {
                color = "LIGHTBLUE";
            } else if (intersection.getColor() == QuoridorIntersection.INTERSECTION_RED) {
                color = "LIGHTCORAL";
            }
        }
        Rectangle rectangle = new Rectangle(10, 10);
        rectangle.setStroke(Color.TRANSPARENT);
        rectangle.setFill(Color.valueOf(color));
        clearShapes();
        addShape(rectangle);
    }
}