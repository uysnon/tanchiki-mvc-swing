package ru.rseu.gorkin.view.menu;

import ru.rseu.gorkin.model.menu.MenuElement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MenuElementButton extends JButton {
    private static Font baseFont;
    private MenuElement menuElement;

    public MenuElementButton(MenuElement menuElement) {
        super(menuElement.getValue());
        this.menuElement = menuElement;
    }

    public MenuElement getMenuElement() {
        return menuElement;
    }

    public void setMenuElement(MenuElement menuElement) {
        this.menuElement = menuElement;
    }

    @Override
    public void paint(Graphics g) {
        if (baseFont == null){
            baseFont = getFont();
        }
        if (menuElement.isActive()){
            Font font = new Font(baseFont.getName(), Font.BOLD, baseFont.getSize());
            setFont(font);
        } else {
           setFont(baseFont);
        }
        super.paint(g);
    }
}
