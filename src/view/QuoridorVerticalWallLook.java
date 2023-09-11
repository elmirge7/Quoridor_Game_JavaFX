package view;

import boardifier.model.GameElement;
import boardifier.view.ElementLook;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.QuoridorVerticalWall;

public class QuoridorVerticalWallLook extends ElementLook {

    public QuoridorVerticalWallLook(GameElement element) {
        super(element, 10);
        QuoridorVerticalWall wall = (QuoridorVerticalWall) element;

        Rectangle rectangle = new Rectangle(10, 50);
        rectangle.setArcWidth(5);
        rectangle.setArcHeight(5);

        if (wall.getColor() == QuoridorVerticalWall.WALL_BLUE) {
            rectangle.setFill(Color.rgb(103, 72, 40));
        } else {
            rectangle.setFill(Color.rgb(103, 72, 40));
        }

        addShape(rectangle);
    }

    @Override
    public void onChange() {
        QuoridorVerticalWall wall = (QuoridorVerticalWall) element;

        Rectangle rectangle = new Rectangle(10, 50);
        rectangle.setArcWidth(5);
        rectangle.setArcHeight(5);

        if (wall.getColor() == QuoridorVerticalWall.WALL_BLUE) {
            rectangle.setFill(Color.rgb(103, 72, 40));
        } else {
            rectangle.setFill(Color.rgb(103, 72, 40));
        }

        clearShapes();
        addShape(rectangle);
    }
}
