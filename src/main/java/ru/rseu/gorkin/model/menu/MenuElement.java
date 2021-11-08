package ru.rseu.gorkin.model.menu;

import java.util.Objects;

public abstract class MenuElement {
    private String value;
    private boolean isActive;

    public MenuElement() {
    }

    public MenuElement(String value, boolean isActive) {
        this.value = value;
        this.isActive = isActive;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public abstract void onSelect();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuElement that = (MenuElement) o;
        return isActive == that.isActive && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, isActive);
    }

    @Override
    public String toString() {
        return "MenuElement{" +
                "value='" + value + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
