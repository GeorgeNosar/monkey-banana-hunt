package game;

import data.StaticResource;

import java.util.Random;

/**
 * Абстрактный класс содержащий в себе базовые свойства объекта игры
 */
public abstract class GameObject {

    /** максимальное возможное значение координат*/
    protected double max;
    /** минимальное возможное значение координат*/
    protected double min;
    /** энергия игрового объекта */
    protected double energy;
    /** координата Х объекта на карте */
    protected double x;
    /** координата Y объекта на карте */
    protected double y;
    /** вспомогательный объект для генирации случайного чисал */
    protected Random random;

    /** Базовый конструктор класса */
    public GameObject() {
        random = new Random();
        x = random.nextInt(StaticResource.mapSize);
        y = random.nextInt(StaticResource.mapSize);
    }

    /** Коректирует данные координат, чтобы объект не выходил за пределы карты
     * @param x координата X для проверки
     * @param y координата Y для проверки
     */
    public abstract void fixPosition(double x, double y);

    public double getX() {
        return x;
    }
    public void setX(double x) {
        this.x = x;
    }
    public double getY() {
        return y;
    }
    public void setY(double y) {
        this.y = y;
    }
    public double getEnergy() {
        return energy;
    }
    public void setEnergy(double energy) {
        this.energy = energy;
    }

    /** Задает новую позицию объекта
    * @param x новая координата Х объекта
    * @param y новая координата Y объекта
    */
    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }
}

