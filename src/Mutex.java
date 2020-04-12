import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Random;
import java.io.IOException;
import java.util.concurrent.Semaphore;

public class Mutex {

    public static void showMenu() {
        System.out.println("Введите: ");
        System.out.println("1. Чтобы продолжить работу");
        System.out.println("2. Чтобы завершить работу");
    }

    public static void main(String[] args) throws InterruptedException, IOException{
        System.out.println("Main thread started");
        BufferedReader buff = new BufferedReader(new InputStreamReader(System.in));
        int choice;
        // Создание потоков
        oneThread[] threads = new oneThread[]{
                new oneThread("thread1"),
                new oneThread("thread2"),
                new oneThread("thread3"),
                new oneThread("thread4"),
                new oneThread("thread5")
        };

        // Запуск потоков
        for (oneThread item : threads) {
            item.start();
        }
        do {
            for(oneThread thread : threads) {
                while (thread.getflowThread().getState() != Thread.State.WAITING) {
                    Thread.sleep(1);
                }
            }
            showMenu();
            choice = Integer.parseInt(buff.readLine());

            switch (choice) {
                case 1:
                    for (oneThread item : threads) {
                        synchronized (item.getLock()) {
                            item.notWait();
                        }
                    }
                    break;
                case 2:
                    for (oneThread item : threads) {
                        synchronized (item.getLock()) {
                            item.stop();
                            item.notWait();
                        }
                    }
            }
        } while (choice != 2);
        for (oneThread item : threads) {
            item.getflowThread().join();
        }
        System.out.println("Main thread finished");
    }
}

class oneThread implements Runnable {
    // Подлючение Mutex (В Java - Semaphore)
    private final Semaphore lock;

    Random rand = new Random();
    int randomNum = rand.nextInt((1000 - 100) + 1) + 10;

    public Thread thread;
    private String threadName;
    private boolean isActive;

    // Конструктор потока
    oneThread(String name) {
        threadName = name;
        isActive = true;
        lock = new Semaphore(1); // Mutex - это Semaphore с кол-вом разрешений 1
    }

    @Override
    public void run() {
        while (isActive) {
            try {
                lock.acquire();
                int randomNum = rand.nextInt((1000 - 100) + 1) + 10;
                try {
                    Thread.sleep(randomNum);
                    System.out.println(threadName + " woke up after " + randomNum + " ms");
                }
                catch (InterruptedException e) {
                    System.out.println("Thread has been interrupted");
                }
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        int randomNum = rand.nextInt((1000 - 100) + 1) + 10;
        try {
            Thread.sleep(randomNum);
        }
        catch (InterruptedException e) {
            System.out.println("Thread has been interrupted");
        }
        System.out.println("Thread " + threadName + " is stopped and worked " + randomNum + " ms");
    }


    public void start() {
        System.out.println("Thread " + threadName + " is running");
        if (thread == null) {
            thread = new Thread(this, threadName);
            thread.start();
        }
    }

    // Остановка потока
    public void stop() {
        this.isActive = false;
    }

    public Thread getflowThread() {
        return thread;
    }

    public Object getLock() {
        return lock;
    }

    public void notWait() {
        this.lock.release();
    }
}