package game;

import game.GameObject;
import game.Banana;

import java.util.Random;

import data.StaticResource;

/**
 * Класс Обезьяны - действующего объекта игры, реализующий поведение Runnable
*/
public class Monkey extends GameObject implements Runnable {
    /**
     * Поток агента
     */
    private Thread t;

    /**
     * Состояние агента:
     *  true - агент жив
     *  false - агент мертв
     */
    private boolean isAlive;
    /**
     * имя агента
     */
    private String name;
    /**
     * Значение скорости агента
     */
    private double speed;
    /**
     * Направление движения агента
     */
    private int direction;

    /**
     * Урон наносимый агентом другим агентам
     */
    private double damage;

    /**
     * Базовый конструктор класса
     * @param speed скорость агента
     * @param damage урон наносимый агентом
     */
    public Monkey(String name, double speed, double damage) {
    	random = new Random();
        energy = random.nextInt(100);
        setDirection();
        this.name = name;
        this.speed = speed;
        this.damage = damage;
        min = 5;
        max = StaticResource.mapSize - min;
    }

    /**
     * Метод запускаеющий жизненный цикл Агента
     */
    public void start() {
        isAlive = true;
        t = new Thread(this);
        t.start();
    }

    /**
     * Останавливает жизненный цикл Агента
     */
    public void stop() {
        isAlive = false;
    }


    /**
     * Запускается процесс перемещения объекта
     */
    @Override
    public void run() {
        while (isAlive) {
            oneStep();
            try {
                Thread.sleep(StaticResource.timeToMove);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * В зависимости от начальных координат задается направление Агента
     */
    private void setDirection() {
        if (x > 350)
            if (y < 350)
                direction = 2;
            else
                direction = 1;
        if (x < 350)
            if (y < 350)
                direction = 3;
            else
                direction = 0;
    }

    /**
     * Делает один шаг в зависимости от направления Агента
     * Проверяет не ушел ли Агент за границы поля
     * Теряется заданное количество энергии агента за шаг, проверяет не закончилась ли энергия Агента
     */
    public void oneStep() {
        int x = 0, y = 0;
        switch (direction) {
            case 0:
                x += speed;
                y -= speed; // rightup
                break;
            case 1:
                x -= speed;
                y -= speed; // leftup
                break;
            case 2:
                x -= speed;
                y += speed; // leftdown
                break;
            case 3:
                x += speed;
                y += speed; // rightdown
                break;
        }

        double newX = this.x + x;
        double newY = this.y + y;

        fixPosition(newX, newY);

        energy -= StaticResource.energyLost;
        if (energy < 0)
            isAlive = false;
    }

    /**
     * Меняет направление движения Агента
     */
    protected void changeDir() {
        direction++;
        if (direction > 3)
            direction = 0;
    }

    /**
     * Метод отвечающий за питание Агента
     * Энергия Агента не может быть больше 100
     * @param b банан, который съедает Агент
     */
    public void eat(Banana b) {
        this.energy += b.getEnergy();
        if (this.energy > 100) {
            this.energy = 100;
        }
    }

    public String getName() {
    	return this.name;
    }
    
    /**
     * Метод фиксирующий получение урона
     * @param dmg урон, который получает Агент
     */
    public void giveDmg(double dmg) {
        energy -= dmg;
    }

    public boolean isAlive() {
        return isAlive;
    }
  
    public double getDmg() {
        return damage;
    }

    /** Коректирует данные координат, чтобы объект не выходил за пределы карты
     * @param x координата X для проверки
     * @param y координата Y для проверки
     */
    @Override
    public void fixPosition(double x, double y) {
        if (x > max || y > max || x < min || y < min)
            changeDir();

        if (x > max)
            x = max;
        else if (x < min)
            x = min;

        if (y > max)
            y = max;
        else if (y < min)
            y = min;

        setPosition(x, y);
    }
    
    /** Создает объект Monkey
     */
    public static Monkey getAgent(String name) {
        double speed = StaticResource.agentSpeed;
        double damage = StaticResource.agentDamage;

        return new Monkey(name, speed, damage);
    }
}