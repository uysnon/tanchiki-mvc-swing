package ru.rseu.gorkin.controller;

import ru.rseu.gorkin.listeners.StateChangeListener;
import ru.rseu.gorkin.model.menu.MainMenu;

import java.util.Objects;

public class MainMenuController {
    private MainMenu mainMenu;

    public MainMenuController() {
    }

    public MainMenuController(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
    }

    public MainMenu getMainMenu() {
        return mainMenu;
    }

    public void setMainMenu(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
    }

    public void subscribeOnMenuDataChange(StateChangeListener stateChangeListener) {
        mainMenu.addStateChangeListener(stateChangeListener);
    }

    public void setActiveNext() {
        mainMenu.setActive(getNextIndex(getActiveElementIndex()));
    }

    public void setActivePrevious() {
        mainMenu.setActive(getPreviousIndex(getActiveElementIndex()));
    }

    public int getActiveElementIndex() {
        for (int i = 0; i < mainMenu.getMenuElements().size(); i++) {
            if (mainMenu.getMenuElements().get(i).isActive()) {
                return i;
            }
        }
        return -1;
    }

    private int getNextIndex(int index) {
        int result;
        if (index + 1 >= mainMenu.getMenuElements().size()) {
            result = 0;
        } else {
            result = index + 1;
        }
        return result;
    }


    private int getPreviousIndex(int index) {
        int result;
        if (index - 1 < 0) {
            result = mainMenu.getMenuElements().size() - 1;
        } else {
            result = index - 1;
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MainMenuController that = (MainMenuController) o;
        return Objects.equals(mainMenu, that.mainMenu);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mainMenu);
    }

    @Override
    public String toString() {
        return "MainMenuController{" +
                "mainMenu=" + mainMenu +
                '}';
    }


}
