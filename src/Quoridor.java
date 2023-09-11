import boardifier.model.GameException;
import control.QuoridorController;
import boardifier.control.StageFactory;
import boardifier.model.Model;
import control.QuoridorControllerMouse;
import javafx.application.Application;
import javafx.stage.Stage;
import view.QuoridorRootPane;
import view.QuoridorView;

public class Quoridor extends Application {

    private static int mode;
    public static void main(String[] args) {
        if (args.length == 1) {
            try {
                mode = Integer.parseInt(args[0]);
                if ((mode <0) || (mode>2)) mode = 0;
            }
            catch(NumberFormatException e) {
                mode = 0;
            }
        }
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        // create the global model
        Model model = new Model();
        // add some players taking mode into account
        if (mode == 0) {
            model.addHumanPlayer("player1");
            model.addHumanPlayer("player2");
        }
        else if (mode == 1) {
            model.addHumanPlayer("player");
            model.addComputerPlayer("computer");
        }
        else if (mode == 2) {
            model.addComputerPlayer("computer1");
            model.addComputerPlayer("computer2");
        }
        // register a single stage for the game, called Quoridor
        StageFactory.registerModelAndView("quoridor", "model.QuoridorStageModel", "view.QuoridorStageView");
        // create the root pane, using the subclass QuoridorRootPane
        QuoridorRootPane rootPane = new QuoridorRootPane();
        // create the global view.
        QuoridorView view = new QuoridorView(model, stage, rootPane);
        // create the controllers.
        QuoridorController control = new QuoridorController(model,view);
        // set the name of the first stage to create when the game is started
        control.setFirstStageName("quoridor");
        // set the stage title
        stage.setTitle("Quoridor : The Game");

        // show the JavaFx main stage
        stage.show();

        //view.resetView();
    }
}
