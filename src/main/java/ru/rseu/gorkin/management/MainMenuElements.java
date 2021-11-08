package ru.rseu.gorkin.management;

import ru.rseu.gorkin.file.FileUtils;
import ru.rseu.gorkin.model.menu.MenuElement;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum MainMenuElements {
    CONTINUE_GAME {
        @Override
        MenuElement getMenuElement() {
            MenuElement startGameElement = new MenuElement() {
                @Override
                public void onSelect() {
                    Screens.MENU_SCREEN.stop();
                    Screens.CONTINUE_GAME_SCREEN.start();
                }
            };
            startGameElement.setValue("Продолжить игру");
            return startGameElement;
        }
    },
    START_GAME {
        @Override
        MenuElement getMenuElement() {
            MenuElement startGameElement = new MenuElement() {
                @Override
                public void onSelect() {
                    Screens.MENU_SCREEN.stop();
                    Screens.NEW_GAME_SCREEN.start();
                }
            };
            startGameElement.setValue("Начать игру");
            return startGameElement;
        }
    },
    SETTINGS {
        @Override
        MenuElement getMenuElement() {
            MenuElement startGameElement = new MenuElement() {
                @Override
                public void onSelect() {
                    Screens.MENU_SCREEN.stop();
                    Screens.NEW_GAME_SCREEN.start();
                }
            };
            startGameElement.setValue("Настройки");
            return startGameElement;
        }
    },
    HELP {
        @Override
        MenuElement getMenuElement() {
            MenuElement startGameElement = new MenuElement() {
                @Override
                public void onSelect() {
                    Screens.MENU_SCREEN.stop();
                    Screens.NEW_GAME_SCREEN.start();
                }
            };
            startGameElement.setValue("Помощь");
            return startGameElement;
        }
    },
    EXIT {
        @Override
        MenuElement getMenuElement() {
            MenuElement startGameElement = new MenuElement() {
                @Override
                public void onSelect() {
                    Screens.MENU_SCREEN.stop();
                    System.exit(0);
                }
            };
            startGameElement.setValue("Выйти");
            return startGameElement;
        }
    };

    public static List<MainMenuElements> getValuesWithContext() {
        List<MainMenuElements> list =
                Stream.of(
                        START_GAME,
                        EXIT
                ).collect(Collectors.toList());
        if (FileUtils.isBackupFileExists()) {
            list.add(0, CONTINUE_GAME);
        }
        return list;
    }





    abstract MenuElement getMenuElement();
}

