package view;

import boardifier.model.GameElement;
import boardifier.view.ElementLook;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.Wall;

/**
 * The WallLook class provides a visual representation of a Wall.
 * It extends the ElementLook class.
 */
public class WallLook extends ElementLook {

    /**
     * Constructs a new WallLook.
     * @param element the Wall element.
     */
    public WallLook(GameElement element) {
        super(element, 10);
        Wall wall = (Wall) element;
        Rectangle rectangle = new Rectangle(10, 50);
        rectangle.setArcWidth(5); // Bord arrondi
        rectangle.setArcHeight(5); // Bord arrondi
        rectangle.setEffect(new DropShadow(5, Color.BLACK)); // Ombre

        if (wall.getColor() == Wall.WALL_BLUE) {
            rectangle.setFill(Color.rgb(103, 72, 40)); // Couleur bleue
        } else {
            rectangle.setFill(Color.rgb(103, 72, 40)); // Couleur rouge
        }
        addShape(rectangle);
    }

    /**
     * Updates the visual representation of the Wall.
     * This method is called when there is a change in the wall's look.
     */
    @Override
    public void onChange() {
        // do nothing since a wall never changes its appearance
    }
}
