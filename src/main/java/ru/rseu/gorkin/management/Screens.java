package ru.rseu.gorkin.management;

import ru.rseu.gorkin.configuration.AppConfiguration;
import ru.rseu.gorkin.controller.GameProcessController;
import ru.rseu.gorkin.controller.MainMenuController;
import ru.rseu.gorkin.management.game.GameCreator;
import ru.rseu.gorkin.model.game.Game;
import ru.rseu.gorkin.model.menu.MainMenu;
import ru.rseu.gorkin.view.gameprocess.GameManager;
import ru.rseu.gorkin.view.gameprocess.GameView;
import ru.rseu.gorkin.view.menu.MainMenuView;

import javax.swing.*;
import java.util.stream.Collectors;

public enum Screens {
    MENU_SCREEN {
        private MainMenu mainMenu;
        private MainMenuController mainMenuController;
        private MainMenuView mainMenuView;

        @Override
        public void start() {
            mainMenu = new MainMenu();
            mainMenu.setMenuElements(
                    MainMenuElements.getValuesWithContext()
                            .stream()
                            .map(MainMenuElements::getMenuElement)
                            .collect(Collectors.toList()));

            mainMenuController = new MainMenuController();
            mainMenuController.setMainMenu(mainMenu);
            mainMenuView = new MainMenuView(window, mainMenuController);
            mainMenuView.start();
        }

    },
    CONTINUE_GAME_SCREEN {
        private Game game;
        private GameProcessController gameProcessController;
        private GameView gameView;


        @Override
        public void start() {
            GameCreator gameCreator = new GameCreator();
            game = gameCreator.initFromBackup(AppConfiguration.INSTANCE.getPathToResourcesFolder()
                    + AppConfiguration.INSTANCE.getProperty("src.backupGameFile"));
            gameProcessController = new GameProcessController(
                    game);

            gameView = new GameView(
                    window,
                    gameProcessController,
                    new GameManager() {
                        @Override
                        public void pause() {
                            gameProcessController.pauseGame();
                            NEW_GAME_SCREEN.stop();
                            MENU_SCREEN.start();
                        }

                        @Override
                        public void end() {
                            gameProcessController.endGame();
                            NEW_GAME_SCREEN.stop();
                            MENU_SCREEN.start();
                        }
                    });
            gameView.start();
        }
    },
    NEW_GAME_SCREEN {
        private Game game;
        private GameProcessController gameProcessController;
        private GameView gameView;

        @Override
        public void start() {
            GameCreator gameCreator = new GameCreator();
            game = gameCreator.createGameFromMapFile(getClass().getClassLoader().getResource(AppConfiguration.INSTANCE.getProperty("src.mapFile")).getPath().toString());
            gameProcessController = new GameProcessController(game);
            gameView = new GameView(
                    window,
                    gameProcessController,
                    new GameManager() {
                        @Override
                        public void pause() {
                            gameProcessController.pauseGame();
                            NEW_GAME_SCREEN.stop();
                            MENU_SCREEN.start();
                        }

                        @Override
                        public void end() {
                            gameProcessController.endGame();
                            NEW_GAME_SCREEN.stop();
                            MENU_SCREEN.start();
                        }
                    });
            gameView.start();
        }
    };

    public static JFrame window;

    public static void clearWindow() {
        window.getContentPane().removeAll();
        window.revalidate();
        window.repaint();
    }

    public abstract void start();

    public void stop() {
        clearWindow();
    }
}
