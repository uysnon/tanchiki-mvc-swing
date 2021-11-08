package ru.rseu.gorkin;

import ru.rseu.gorkin.management.Screens;

import javax.swing.*;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Runner {
    public static void main(String[] args) {

        JFrame mf = new JFrame();
        mf.setSize(1000, 1000);
        mf.setDefaultCloseOperation(EXIT_ON_CLOSE);
        Screens.window = mf;
        Screens.MENU_SCREEN.start();
        mf.setVisible(true);

    }
}
