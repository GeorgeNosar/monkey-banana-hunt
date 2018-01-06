package game;

import game.Game;
import game.GameObject;
import game.Banana;
import game.Monkey;
import data.DataBase;
import data.PropertiesGame;
import data.StaticResource;
import socket.WebSocket;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Класс отвечающий за поведение и взаимодействие объектов на поле
 */
public class GameManager implements Runnable {
    /**
     * Поток в котором работает класс
     */
    private Thread t;
    /**
     * Контейнер Агентов
     */
    private ArrayList<Monkey> monkeys;
    /**
     * Контейнер бананов
     */
    private ArrayList<Banana> bananas;

    /**
     * Внутренний класс, отвечающий за производство бананов
     */
    private BananaSpawner bananaSpawner;

    /**
     * Базовый конструктор
     */
    public GameManager() {
    	bananaSpawner = new BananaSpawner();
        monkeys = new ArrayList<>();
    }

    /**
     * Создание и запуск Агентов по данным из файла ресурсов
     * Старт контролера корма
     * Старт основного потока
     */
    public void start() {
    	
    	monkeys.add(Monkey.getAgent("Abu"));
    	monkeys.add(Monkey.getAgent("Bobo"));
    	monkeys.add(Monkey.getAgent("Jumbo"));
    	monkeys.add(Monkey.getAgent("Makaken"));
    	monkeys.add(Monkey.getAgent("Kongo"));
    	monkeys.add(Monkey.getAgent("Bong"));
        startAgents();

        bananaSpawner.start();

        t = new Thread(this);
        t.start();
    }

    /**
     * Запуск основного цикла игры
     */
    @Override
    public void run() {
        while (Game.getInstance().isGame) {
            try {
            for (int i = 0; i < monkeys.size(); i++) {
                if (!monkeys.get(i).isAlive())
                	monkeys.remove(monkeys.get(i));
                else {
                    for (int j = 0; j < bananas.size(); j++)
                        interactionBanana(monkeys.get(i), bananas.get(j));

                    for (int m = 0; m < monkeys.size(); m++)
                        if (!monkeys.get(i).equals(monkeys.get(m)))
                            interactionAgent(monkeys.get(i), monkeys.get(m));
                }
            }
                WebSocket.updateGame(monkeys, bananas);
            if(monkeys.size() == 1) {
                stopGame();
            }
                Thread.sleep(StaticResource.timeToMove);
            } catch (InterruptedException | IOException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * Остановка игры
     * Запись выживших Агентов в базу данных
     */
    public void stopGame() {
        stopAgents();
        DataBase.addAgents(monkeys);
        bananaSpawner.stop();
        Game.getInstance().isGame = false;
    }

    /**
     * Проверка на соприкоснование двух Агентов
     * Взаимодействие двух Агентов, после получения урона Агенты меняют траекторию
     * @param radiant первый Агент
     * @param dire второй Агент
     */
    private void interactionAgent(Monkey radiant, Monkey dire) {
        if (distance(radiant, dire) <= 50) {
            radiant.giveDmg(dire.getDmg());
            dire.giveDmg(radiant.getDmg());
            radiant.changeDir();
            dire.changeDir();
        }
    }

    /**
     * Проверка на соприкоснование Агента и банана
     * Взаимодейстиве Агента с бананом, пополнение энергии от банана
     * @param agent Агент участвующий во взаимодействии
     * @param banana банан столкнувшийся с Агентом
     */
    private void interactionBanana(Monkey agent, Banana banana) {
        if (distance(agent, banana) <= 50) {
            agent.eat(banana);
            bananas.remove(banana);
        }
    }


    /**
     * Метод возвращает расстояние между двумя объектами
     * @param object1 первый объект
     * @param object2 второй объект
     * @return расстояния
     */
    private double distance(GameObject object1, GameObject object2) {
        return Math.sqrt(Math.pow(object2.getX() - object1.getX(), 2) + Math.pow(object2.getY() - object1.getY(), 2));
    }

    /**
     * Метод останавливает жизненный цикл всех агентов
     */
    private void stopAgents() {
        for (int i = 0; i < monkeys.size(); i++) {
            monkeys.get(i).stop();
        }
    }

    /**
     * Метод запускает жизненный цикл всех агентов
     */
    private void startAgents() {
        for (int i = 0; i < monkeys.size(); i++) {
            monkeys.get(i).start();
        }
    }

    public void setAgents(ArrayList<Monkey> agents) {
        this.monkeys = agents;
    }
    public void setPies(ArrayList<Banana> bananas) {
        this.bananas = bananas;
    }
    public ArrayList<Monkey> getAgents() {
        return monkeys;
    }
    public ArrayList<Banana> getBananas() {
        return bananas;
    }

    /**
     * Внутриний класс отвечающий за распределение пирогов на поле
     */
    private class BananaSpawner implements Runnable {
        /**
         * Поток в котором работает котролер
         */
        Thread pc;
        /**
         * Состояние процесса выпечки
         *  true - продолжать выпечку
         *  false - прекратить выпечку
         */
        private boolean cook;

        /**
         * Базовый конструктов создающий массив пирогов
         */
        public BananaSpawner() {
            bananas = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                bananas.add(new Banana());
            }
        }

        /**
         * Метод запускает жизненный цикл контроллера
         */
        private void start() {
            cook = true;
            pc = new Thread(this);
            pc.start();
        }

        /**
         * Метод останавливает жизненный цикл контроллера
         */
        private void stop() {
            cook = false;
            deleteBananas();
        }
        
        private void deleteBananas() {
            for (int i = 0; i < bananas.size(); i++) {
                bananas.remove(bananas.get(i));
            }
        }

        /**
         * Метод отвечающий за распространение пирогов
         */
        @Override
        public void run() {
            while (cook) {
            	bananas.add(new Banana());
                try {
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}