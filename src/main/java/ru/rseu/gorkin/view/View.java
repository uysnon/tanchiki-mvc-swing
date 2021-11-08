package ru.rseu.gorkin.view;

import ru.rseu.gorkin.listeners.StateChangeListener;

import javax.swing.*;
import java.util.Objects;
import java.util.UUID;

public abstract class View implements StateChangeListener {
    private UUID id;
    private JFrame window;
    private JPanel viewPanel;

    public View() {
        id = UUID.randomUUID();
    }

    public View(JFrame window) {
        this.window = window;
    }

    public JFrame getWindow() {
        return window;
    }

    public void setWindow(JFrame window) {
        this.window = window;
    }

    public JPanel getViewPanel() {
        return viewPanel;
    }

    public void setViewPanel(JPanel viewPanel) {
        this.viewPanel = viewPanel;
    }

    public abstract void start();
    public abstract void render();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        View view = (View) o;
        return Objects.equals(id, view.id) && Objects.equals(window, view.window) && Objects.equals(viewPanel, view.viewPanel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, window, viewPanel);
    }

    @Override
    public String toString() {
        return "View{" +
                "id=" + id +
                ", window=" + window +
                ", viewPanel=" + viewPanel +
                '}';
    }
}
