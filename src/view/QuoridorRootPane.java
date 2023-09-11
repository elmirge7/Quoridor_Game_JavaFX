package view;

import boardifier.view.RootPane;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class QuoridorRootPane extends RootPane {

    public QuoridorRootPane() {
        super();
    }

    @Override
    public void createDefaultGroup() {
        VBox frame = new VBox();
        frame.setAlignment(Pos.CENTER);
        frame.getStyleClass().add("game_background");
        frame.setSpacing(20); // Espacement vertical entre les éléments
        frame.setPadding(new Insets(50)); // Marge autour du VBox

        // Add title
        Text title = new Text("Quoridor: The Game");
        title.setFont(Font.loadFont(getClass().getResourceAsStream("/resources/arcade_font.ttf"), 45));
        title.setFill(Color.YELLOW);
        title.setEffect(new DropShadow(10, Color.BLACK));

        // Add Buttons on main menu
        Button b2players = createButton("Player VS Player");
        Button b1player1CPU = createButton("Player VS CPU Easy");
        Button b1player1CPU2 = createButton("Player VS CPU Hard");
        Button b2CPUs = createButton("CPU VS CPU");



        // Add copyright text
        Text copyrightText = new Text("\u00A9 2023 Robin,Thomas,Sefa,Noam,Elmir. All rights reserved.");
        copyrightText.setFill(Color.WHITE);
        copyrightText.setFont(Font.loadFont(getClass().getResourceAsStream("/resources/arcade_font.ttf"), 12));
        copyrightText.setEffect(new InnerShadow(3, Color.BLACK));
        VBox.setMargin(copyrightText, new Insets(20, 0, 0, 0)); // Marge supérieure pour le texte de copyright

        frame.getChildren().addAll(title, b2players, b1player1CPU, b1player1CPU2, b2CPUs, copyrightText);

        // put shapes in the group
        group.getChildren().clear();
        group.getChildren().addAll(frame);
    }

    private Button createButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("arcade_button");
        button.setTextFill(Color.WHITE);
        button.setFont(Font.loadFont(getClass().getResourceAsStream("/resources/arcade_font.ttf"), 20));
        button.setEffect(new InnerShadow(5, Color.BLACK));
        return button;
    }
}
