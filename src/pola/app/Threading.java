package pola.app;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dimas
 */
public class Threading {

    public static int[][] quartering(int height) {
        int quarter = height / 4;
        return new int[][]{
            new int[]{0, quarter},
            new int[]{quarter + 1, quarter * 2},
            new int[]{(quarter * 2) + 1, quarter * 3},
            new int[]{(quarter * 3) + 1, height - 1}
        };
    }

    public static void runRunnables(Runnable[] runnables) {
        Thread[] threads = new Thread[runnables.length];
        for (int t = 0; t < threads.length; t++) {
            threads[t] = new Thread(runnables[t]);
        }
        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(Gambar.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
