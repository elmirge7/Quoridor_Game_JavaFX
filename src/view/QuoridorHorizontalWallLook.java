package view;

import boardifier.model.GameElement;
import boardifier.view.ElementLook;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.QuoridorHorizontalWall;

public class QuoridorHorizontalWallLook extends ElementLook {

    public QuoridorHorizontalWallLook(GameElement element) {
        super(element, 1);
        QuoridorHorizontalWall wall = (QuoridorHorizontalWall) element;

        Rectangle rectangle = new Rectangle(50, 10);
        rectangle.setArcWidth(5);
        rectangle.setArcHeight(5);

        if (wall.getColor() == QuoridorHorizontalWall.WALL_BLUE) {
            rectangle.setFill(Color.rgb(103, 72, 40));
        } else {
            rectangle.setFill(Color.rgb(103, 72, 40));
        }

        addShape(rectangle);
    }

    @Override
    public void onChange() {
        QuoridorHorizontalWall wall = (QuoridorHorizontalWall) element;

        Rectangle rectangle = new Rectangle(10, 50);
        rectangle.setArcWidth(5);
        rectangle.setArcHeight(5);

        if (wall.getColor() == QuoridorHorizontalWall.WALL_BLUE) {
            rectangle.setFill(Color.rgb(103, 72, 40));
        } else {
            rectangle.setFill(Color.rgb(103, 72, 40));
        }

        clearShapes();
        addShape(rectangle);
    }
}
