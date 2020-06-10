package car;

import road.Stage;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class Car implements Runnable {

    private static CountDownLatch countDownLatchReady;
    private static CountDownLatch countDownLatchFinish;
    private static CyclicBarrier startBarrier;
    private static boolean winnerFound = false;

    private static int CARS_COUNT;
    static {
        CARS_COUNT = 0;

        countDownLatchFinish = MainClass.countDownLatchFinish;
        countDownLatchReady = MainClass.countDownLatchReady;
        startBarrier = MainClass.startBarrier;
    }

    private Race race;
    private int speed;
    private String name;
    public String getName() {
        return name;
    }
    public int getSpeed() {
        return speed;
    }
    public Car(Race race, int speed) {
        this.race = race;
        this.speed = speed;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
    }
    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int)(Math.random() * 800));
            countDownLatchReady.countDown();
            System.out.println(this.name + " готов");
            startBarrier.await();


            for (int i = 0; i < race.getStages().size(); i++) {
                race.getStages().get(i).go(this);
            }
            isWinning(this);
            countDownLatchFinish.countDown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static synchronized void isWinning(Car c) {
        if (!winnerFound) {
            System.out.println(c.name + " - WIN");
            winnerFound = true;
        }
    }

}
