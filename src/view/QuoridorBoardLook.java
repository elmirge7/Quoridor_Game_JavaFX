package view;

import boardifier.model.GameElement;
import boardifier.view.GridLook;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import model.QuoridorBoard;

public class QuoridorBoardLook extends GridLook {
    private Rectangle[][] cells;

    public QuoridorBoardLook(int size, GameElement element) {
        super(size, size, (size - 20) / 9, (size - 20) / 9, 10, "0X000000", element);
        cells = new Rectangle[9][9];
        // create the rectangles
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                Color c;
                if ((i + j) % 2 == 0) {
                    c = Color.rgb(14, 14, 14); // Couleur rose vif
                } else {
                    c = Color.rgb(206, 206, 206); // Couleur pêche
                }
                cells[i][j] = new Rectangle(cellWidth, cellHeight, c);
                cells[i][j].setX(j * cellWidth + borderWidth);
                cells[i][j].setY(i * cellHeight + borderWidth);
                cells[i][j].setArcWidth(10); // Bord arrondi
                cells[i][j].setArcHeight(10); // Bord arrondi
                addShape(cells[i][j]);
            }
        }
    }

    @Override
    public void onChange() {
        QuoridorBoard board = (QuoridorBoard) element;
        boolean[][] reach = board.getReachableCells();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (reach[i][j]) {
                    cells[i][j].setStrokeWidth(3);
                    cells[i][j].setStrokeMiterLimit(10);
                    cells[i][j].setStrokeType(StrokeType.CENTERED);
                    cells[i][j].setStroke(Color.rgb(51, 51, 51)); // Couleur grise
                } else {
                    cells[i][j].setStrokeWidth(0);
                }
            }
        }
    }
}
