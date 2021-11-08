package ru.rseu.gorkin.model.menu;

import ru.rseu.gorkin.model.ModelElement;

import java.util.List;
import java.util.Objects;

public class MainMenu extends ModelElement{
    private List<MenuElement> menuElements;

    public MainMenu() {
    }

    public MainMenu(List<MenuElement> menuElements) {
        this.menuElements = menuElements;
    }

    public List<MenuElement> getMenuElements() {
        return menuElements;
    }

    public void setMenuElements(List<MenuElement> menuElements) {
        this.menuElements = menuElements;
    }

    public void setActive(int index){
        for (int i = 0; i < menuElements.size(); i++){
            if (menuElements.get(i).isActive()){
                menuElements.get(i).setActive(false);
            }
        }
        menuElements.get(index).setActive(true);
        notifyStateChanged();
    }

    public void setActive(MenuElement menuElement){
        for(int i = 0; i < menuElements.size(); i++){
            if (menuElements.get(i).equals(menuElement)){
                setActive(i);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MainMenu mainMenu = (MainMenu) o;
        return Objects.equals(menuElements, mainMenu.menuElements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(menuElements);
    }

    @Override
    public String toString() {
        return "MainMenu{" +
                "menuElements=" + menuElements +
                '}';
    }
}
