package view;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class RulesLook extends VBox {

    public RulesLook() {
        setSpacing(10);

        Label titleLabel = new Label("Règles du jeu :");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        Label commandLabel = new Label("Les commandes du jeu sont à entrer dans le terminal de la console");

        Label moveLabel = new Label("Pour faire avancer un pion, entrez \"p + direction\"");
        Label moveDescLabel = new Label("ph = pion en haut\npg = pion à gauche\npd = pion à droite\npb = pion en bas");

        Label wallLabel = new Label("Pour placer les murs, entrez \"type de mur + position\"");
        Label wallDescLabel = new Label("Entrez \"mh\" si mur horizontal\nEntrez \"mv\" si mur vertical\nSuivi de la position dans le plateau (en majuscule)\nExemple : \"mhE2\"");

        getChildren().addAll(titleLabel, commandLabel, moveLabel, moveDescLabel, wallLabel, wallDescLabel);
    }
}
