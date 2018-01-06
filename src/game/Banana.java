package game;

import data.StaticResource;

/** Класс представляет собой корм разбрасываемый во время игры */
public class Banana extends GameObject {

    /** В конструкторе случайным образом задается размер получаемой энергии агентом
    * Генерируется случаный размер объекта
    */
    public Banana() {
        energy = StaticResource.bananaEnergy;
        min = 1;
        max = StaticResource.mapSize - min;
        fixPosition(x, y);
    }


    /** Коректирует данные координат, чтобы объект не выходил за пределы карты
     * @see #fixPosition(double, double)
     */
    @Override
    public void fixPosition(double x, double y) {
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
}