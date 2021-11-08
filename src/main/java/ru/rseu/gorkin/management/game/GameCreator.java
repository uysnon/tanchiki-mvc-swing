package ru.rseu.gorkin.management.game;

import ru.rseu.gorkin.model.Coordinate;
import ru.rseu.gorkin.model.game.Game;
import ru.rseu.gorkin.model.game.map.GameMapStaticView;
import ru.rseu.gorkin.model.game.objects.GameObjectTypes;
import ru.rseu.gorkin.model.game.objects.immovable.Box;
import ru.rseu.gorkin.model.game.objects.immovable.Wall;
import ru.rseu.gorkin.model.game.objects.moving.BotTank;
import ru.rseu.gorkin.model.game.objects.moving.PlayerTank;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GameCreator {
    private static final double GAP_TO_CENTER_CELL_FROM_CORNER = 0.5;

    public Game createGameFromMapFile(String pathToFieldFile) {
        List<String> gameMapLines = geListLinesOfFileContent(pathToFieldFile);
        GameMapStaticView gameMapStaticView = getGameMapFromFileView(gameMapLines);
        Game game = generateGameFromGameMapStaticView(gameMapStaticView);
        return game;
    }

    public Game initFromBackup(String pathToBackupFile) {
        Game game = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(pathToBackupFile);
            ObjectInputStream objectInputStream
                    = new ObjectInputStream(fileInputStream);
            game = (Game) objectInputStream.readObject();
            objectInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return game;
    }

    private GameMapStaticView getGameMapFromFileView(List<String> fileLines) {
        Map<Coordinate<Integer>, GameObjectTypes> gameMap = new HashMap<>();
        int size = fileLines.size();
        for (int i = 0; i < fileLines.size(); i++) {
            int y = size - i - 1;
            String line = fileLines.get(i);
            for (int j = 0; j < line.length(); j++) {
                int x = j;
                char objectSymbol = line.charAt(j);
                GameObjectTypes objectType = GameObjectTypes.parseFromCharacter(objectSymbol);
                gameMap.put(new Coordinate<>(x, y), objectType);
            }

            if (line.length() != size) {
                throw new IllegalArgumentException("Входная карта не является квадратной");
            }
        }
        return new GameMapStaticView(size, gameMap);
    }

    private List<String> geListLinesOfFileContent(String pathToFile) {
        List<String> gameMapLines = null;
        try {
            gameMapLines =
                    Files.lines(Path.of(pathToFile)).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return gameMapLines;
    }


    private Game generateGameFromGameMapStaticView(GameMapStaticView gameMapStaticView) {
        Game game = new Game(gameMapStaticView.getSize());
        Map<Coordinate<Integer>, GameObjectTypes> gameTypesMap = gameMapStaticView.getGameMap();
        for (Coordinate<Integer> coordinate : gameTypesMap.keySet()) {
            GameObjectTypes objectType = gameTypesMap.get(coordinate);
            if (objectType == GameObjectTypes.BOT_TANK) {
                BotTank botTank = (BotTank) objectType.createObject(new Coordinate<>(
                        coordinate.getX() + GAP_TO_CENTER_CELL_FROM_CORNER,
                        coordinate.getY() + GAP_TO_CENTER_CELL_FROM_CORNER
                ));
                game.addBotTank(botTank);
            } else if (objectType == GameObjectTypes.PLAYER_TANK) {
                PlayerTank playerTank = (PlayerTank) objectType.createObject(new Coordinate<>(
                        coordinate.getX() + GAP_TO_CENTER_CELL_FROM_CORNER,
                        coordinate.getY() + GAP_TO_CENTER_CELL_FROM_CORNER
                ));

                game.setPlayerTank(playerTank);
            } else if (objectType == GameObjectTypes.WALL) {
                Wall wall = (Wall) objectType.createObject(new Coordinate<>(
                        coordinate.getX() + GAP_TO_CENTER_CELL_FROM_CORNER,
                        coordinate.getY() + GAP_TO_CENTER_CELL_FROM_CORNER
                ));
                game.addWall(wall);
            } else if (objectType == GameObjectTypes.BOX) {
                Box box = (Box) objectType.createObject(new Coordinate<>(
                        coordinate.getX() + GAP_TO_CENTER_CELL_FROM_CORNER,
                        coordinate.getY() + GAP_TO_CENTER_CELL_FROM_CORNER
                ));
                game.addBox(box);
            }
        }
        return game;
    }
}
