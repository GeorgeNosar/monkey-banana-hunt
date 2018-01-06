package game;

import data.PropertiesGame;
import game.GameManager;

public class Game {
    private static Game game = new Game();

    public static Game getInstance() {
        if (game == null)
            game = new Game();
        return game;
    }

    /** Объект контролирующий взаимодействие объектов на карте */
    private GameManager gm;
    /** Значание текущего состояния игры */
    public boolean isGame;

    /** Базовый конструктор класса, в нем происходит считывание файла config.properties,
     * создается экземпляр класса GameManager и увеличивается счетчик игры на единицу.
     */
    private Game() {
        PropertiesGame.setGameProperties();
        gm = new GameManager();
    }

    /* Запускает игровой процесс, ставит состояние игры в значение true */
    public void start() {
        isGame = true;
        gm.start();
    }

    /** Останавливает процесс игры */
    public void stop() {
        isGame = false;
        gm.stopGame();
    }

    /** Запускает процесс игры заново */
    public void restart() {
        game = new Game();
    }
}

