package hii;

import java.util.concurrent.Semaphore;

class Philosopher implements Runnable {
    private final int id;
    private final Semaphore leftFork;
    private final Semaphore rightFork;

    public Philosopher(int id, Semaphore leftFork, Semaphore rightFork) {
        this.id = id;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
    }

    @Override
    public void run() {
        try {
            think();
            pickUpForks();
            eat();
            putDownForks();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void think() throws InterruptedException {
        System.out.println("Philosopher " + id + " is thinking.");
        Thread.sleep((int) (Math.random() * 100));
    }

    private void pickUpForks() throws InterruptedException {
        if (id % 2 == 0) {
            leftFork.acquire();
            System.out.println("Philosopher " + id + " picked up left fork.");
            rightFork.acquire();
            System.out.println("Philosopher " + id + " picked up right fork.");
        } else {
            rightFork.acquire();
            System.out.println("Philosopher " + id + " picked up right fork.");
            leftFork.acquire();
            System.out.println("Philosopher " + id + " picked up left fork.");
        }
    }

    private void eat() throws InterruptedException {
        System.out.println("Philosopher " + id + " is eating.");
        Thread.sleep((int) (Math.random() * 100));
    }

    private void putDownForks() {
        leftFork.release();
        System.out.println("Philosopher " + id + " put down left fork.");
        rightFork.release();
        System.out.println("Philosopher " + id + " put down right fork.");
    }
}

public class DiningPhilosophers {
    public static void main(String[] args) throws InterruptedException {
        final int NUM_PHILOSOPHERS = 5;
        Philosopher[] philosophers = new Philosopher[NUM_PHILOSOPHERS];
        Semaphore[] forks = new Semaphore[NUM_PHILOSOPHERS];
        Thread[] threads = new Thread[NUM_PHILOSOPHERS];

        for (int i = 0; i < NUM_PHILOSOPHERS; i++) {
            forks[i] = new Semaphore(1);
        }

        for (int i = 0; i < NUM_PHILOSOPHERS; i++) {
            Semaphore leftFork = forks[i];
            Semaphore rightFork = forks[(i + 1) % NUM_PHILOSOPHERS];

            philosophers[i] = new Philosopher(i, leftFork, rightFork);
            threads[i] = new Thread(philosophers[i]);
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }
    }
}
