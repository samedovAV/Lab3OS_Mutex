import java.util.Scanner;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Mutex {

    public static void main(String[] args) {
        // userChoice();

        createThreads();
        userChoice();



        /*
        oneThread thread1 = new oneThread();
        oneThread thread2 = new oneThread();
        oneThread thread3 = new oneThread();
        oneThread thread4 = new oneThread();
        oneThread thread5 = new oneThread();
        */

        /*
        thread1.place = place;
        thread2.place = place;
        thread3.place = place;
        thread4.place = place;
        thread5.place = place;
        */
        /*
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();
        */

    }

    // Выбор дальнейших действий
    public static void userChoice() {
        Scanner input = new Scanner(System.in);
        System.out.println("Повторить - 0; Выйти - 1");
        int choice = input.nextInt();
        if (choice == 0) {
            createThreads();
            userChoice();
        }
        else if (choice == 1) {
            System.out.println("Работа с потоками закончена.");
        }
        else
        {
            System.out.println("Неккоректный ввод. Повторите.\n");
            userChoice();
        }
    }

    public static void createThreads() {

        oneThread thread1 = new oneThread("Thread 1");
        thread1.start();
        oneThread thread2 = new oneThread("Thread 2");
        thread2.start();
        oneThread thread3 = new oneThread("Thread 3");
        thread3.start();
        oneThread thread4 = new oneThread("Thread 4");
        thread4.start();
        oneThread thread5 = new oneThread("Thread 5");
        thread5.start();

        try {
            thread1.join();
            thread2.join();
            thread3.join();
            thread4.join();
            thread5.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static Semaphore place = new Semaphore(1);

    static class oneThread extends Thread {
        String threadName;

        oneThread(String threadName) {
            this.threadName = threadName;
        }

        @Override
        public void run() {
            Random rand = new Random();
            int randomNum = rand.nextInt((1000 - 100) + 1) + 10;
            System.out.println(this.getName() + " waiting to run");

            try {
                place.acquire();
                try {

                    this.sleep(randomNum);
                } finally {
                    place.release();

                }
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(this.getName() + " is running");
            System.out.println(this.getName() + " worked " + randomNum);
        }
    }
}


