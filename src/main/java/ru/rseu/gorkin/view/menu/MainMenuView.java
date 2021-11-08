package ru.rseu.gorkin.view.menu;

import ru.rseu.gorkin.controller.MainMenuController;
import ru.rseu.gorkin.model.menu.MenuElement;
import ru.rseu.gorkin.view.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class MainMenuView extends View {
    private MainMenuController mainMenuController;
    private List<MenuElementButton> menuElementButtons;
    private JPanel innerPanel;

    public MainMenuView(JFrame window, MainMenuController mainMenuController) {
        super(window);
        this.mainMenuController = mainMenuController;
        menuElementButtons = new ArrayList<>();
        mainMenuController.subscribeOnMenuDataChange(this);
        innerPanel = new JPanel();
        innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));

        JPanel panel = new JPanel();
        panel.add(innerPanel);
        setViewPanel(panel);
        setOnKeyPressedListener();
    }

    @Override
    public void dataChanged() {
        render();
    }

    @Override
    public void start() {
        List<MenuElement> listMenuElements = mainMenuController.getMainMenu().getMenuElements();
        listMenuElements.forEach(element -> menuElementButtons.add(new MenuElementButton(element)));
        menuElementButtons.forEach(button -> button.addActionListener(getButtonPressedActionListener(button)));
        menuElementButtons.forEach(button -> innerPanel.add(button));
        getWindow().add(getViewPanel());
        getWindow().revalidate();
    }

    @Override
    public void render() {
        for (MenuElementButton button : menuElementButtons) {
            button.repaint();
        }
    }

    private void setOnKeyPressedListener(){
        JPanel panel = getViewPanel();
        panel.setFocusable(true);
        panel.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()){
                    case (KeyEvent.VK_DOWN):{
                        mainMenuController.setActiveNext();
                        break;
                    }
                    case (KeyEvent.VK_UP):{
                        mainMenuController.setActivePrevious();
                        break;
                    }
                    case (KeyEvent.VK_ENTER):{
                        MenuElement menuElement = mainMenuController
                                .getMainMenu()
                                .getMenuElements()
                                .get(mainMenuController.getActiveElementIndex());
                        MenuElementButton activeButton = menuElementButtons.stream()
                                .filter(button->button.getMenuElement().equals(menuElement))
                                .findFirst().get();
                        menuElement.onSelect();
                        break;
                    }
                }
            }
        });
    }

    private ActionListener getButtonPressedActionListener(MenuElementButton menuElementButton){
        return e -> {
            mainMenuController.getMainMenu().setActive(menuElementButton.getMenuElement());
            menuElementButton.getMenuElement().onSelect();
        };
    }
}
