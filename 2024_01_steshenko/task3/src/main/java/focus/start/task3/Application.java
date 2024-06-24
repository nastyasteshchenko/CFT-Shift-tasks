package focus.start.task3;

import focus.start.task3.controller.Controller;
import focus.start.task3.model.Model;
import focus.start.task3.view.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        LOGGER.info("Application started.");
        View view = new View();

        Model model = new Model();
        LOGGER.info("Model was created.");

        Controller controller = new Controller();
        LOGGER.info("Controller was created.");

        setControllerListeners(controller, model, view);

        setModelListeners(model, controller, view);
        view.getLoseWindow().setNewGameListener(e -> controller.startNewGame());
        view.getWinWindow().setNewGameListener(e -> controller.startNewGame());
        view.getSettingsWindow().setGameTypeListener(controller);
        setMainWindowListener(view, controller);
        LOGGER.info("All listeners were set.");

        controller.start();
        view.getMainWindow().setVisible(true);
        LOGGER.info("Main window was set visible.");
    }

    private static void setModelListeners(Model model, Controller controller, View view) {
        model.setOpenBombListener(view.getMainWindow());
        model.setLoseEventListener(controller);
        model.setWinEventListener(controller);
        model.setBombsAmountListener(view.getMainWindow());
        model.setMarkCellListener(view.getMainWindow());
        model.setUnmarkCellListener(view.getMainWindow());
        model.setOpenCellListener(view.getMainWindow());
        model.setStartTimerListener(controller);
    }

    private static void setControllerListeners(Controller controller, Model model, View view) {
        controller.setOnMarkCellListener(model);
        controller.setOnOpenCellListener(model);
        controller.setOnSwitchGameDifficultyListener(model);
        controller.setOnStartNewGameListener(model);
        controller.setOnOpenCellsAroundOpenedOneListener(model);
        controller.setOnCreateGameFieldListener(view.getMainWindow());
        controller.setOnSetTimerValueListener(view.getMainWindow());
        controller.setShowLoseWindowListener(view.getLoseWindow());
        controller.setShowWinWindowListener(view.getWinWindow());
        controller.setGetNewHighScoreHolderNameListener(view.getHighScoreHolderNameWindow());
    }

    private static void setMainWindowListener(View view, Controller controller) {
        MainWindow mainWindow = view.getMainWindow();
        mainWindow.setNewGameMenuAction(e -> controller.startNewGame());
        mainWindow.setHighScoresMenuAction(e -> controller.onHighScores(view.getHighScoresWindow()));
        mainWindow.setMarkCellListener(controller);
        mainWindow.setOpenCellListener(controller);
        mainWindow.setOpenCellsAroundOpenedOneListener(controller);
    }
}
