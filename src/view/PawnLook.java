package view;

import boardifier.model.GameElement;
import boardifier.view.ElementLook;
import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import model.QuoridorPawn;

public class PawnLook extends ElementLook {
    private Circle circle;
    public PawnLook(int radius, GameElement element) {
        super(element);
        QuoridorPawn pawn = (QuoridorPawn)element;
        circle = new Circle();
        circle.setRadius(radius);
        if (pawn.getPlayerID() == 0) {
            circle.setFill(Color.BLUE);
        }
        else {
            circle.setFill(Color.RED);
        }

        circle.setCenterX(radius);
        circle.setCenterY(radius);
        addShape(circle);
        // NB: text won't change so no need to put it as an attribute
        Text text = new Text(String.valueOf(pawn.getPlayerID()));
        text.setFont(new Font(24));
        if (pawn.getPlayerID() == 0) {
            text.setFill(Color.valueOf("0xFFFFFF"));
        }
        else {
            text.setFill(Color.valueOf("0x000000"));
        }
        Bounds bt = text.getBoundsInLocal();
        text.setX(radius - bt.getWidth()/2);
        text.setY(text.getBaselineOffset()+ bt.getHeight()/2-3);
        addShape(text);
    }

    @Override
    public void onSelectionChange() {
        QuoridorPawn pawn = (QuoridorPawn)getElement();
        if (pawn.isSelected()) {
            circle.setStrokeWidth(3);
            circle.setStrokeMiterLimit(10);
            circle.setStrokeType(StrokeType.CENTERED);
            circle.setStroke(Color.valueOf("0x333333"));
        }
        else {
            circle.setStrokeWidth(0);
        }
    }

    @Override
    public void onChange() {
    }
}