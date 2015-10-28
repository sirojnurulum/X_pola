package pola.app;

/**
 *
 * @author Dimas
 */
public class Konvolusi {

    public static int[][] samarkan(int[][] grayscale) {
        int height = grayscale.length;
        int width = grayscale[0].length;

        int[][] result = new int[height][width];

        int[][] ranges = Threading.quartering(height);
        Runnable[] runnables = new Runnable[ranges.length];
        for (int r = 0; r < runnables.length; r++) {
            int[] range = ranges[r];
            runnables[r] = () -> {

                int ymin, ymax, xmin, xmax, total, count;
                for (int y = range[0]; y <= range[1]; y++) {
                    for (int x = 0; x < width; x++) {
                        ymin = y == 0 ? y : y - 1;
                        ymax = y == height - 1 ? y : y + 1;
                        xmin = x == 0 ? x : x - 1;
                        xmax = x == width - 1 ? x : x + 1;

                        total = 0;
                        count = 0;
                        for (int yc = ymin; yc <= ymax; yc++) {
                            for (int xc = xmin; xc <= xmax; xc++) {
                                total += grayscale[yc][xc];
                                count += 1;
                            }
                        }
                        result[y][x] = (int) Math.round((total + 0.0) / count);
                    }
                }

            };
        }
        Threading.runRunnables(runnables);

        return result;
    }

    public static int[][] sobel(int[][] grayscale) {
        int[][] operandBaris = new int[][]{
            new int[]{1, 2, 1},
            new int[]{0, 0, 0},
            new int[]{-1, -2, -1}
        };
        int[][] operandKolom = new int[][]{
            new int[]{1, 0, -1},
            new int[]{2, 0, -2},
            new int[]{1, 0, -1}
        };
        return operate(grayscale, operandBaris, operandKolom);
    }

    private static int[][] operate(int[][] grayscale, int[][] operandBaris, int[][] operandKolom) {
        int height = grayscale.length;
        int width = grayscale[0].length;

        int[][] result = new int[height][width];
        int[] intervalMinMax = new int[2];

        int[][] ranges = Threading.quartering(height);
        ranges[0][0] = 1;
        ranges[3][1] = height - 2;
        Runnable[] runnables = new Runnable[ranges.length];
        for (int r = 0; r < runnables.length; r++) {
            int[] range = ranges[r];
            runnables[r] = () -> {

                int kali, value;
                for (int y = range[0]; y <= range[1]; y++) {
                    for (int x = 1; x < width - 1; x++) {
                        kali = 0;
                        for (int ym = -1; ym <= 1; ym++) {
                            for (int xm = -1; xm <= 1; xm++) {
                                value = grayscale[y + ym][x + xm];
                                kali += value * operandBaris[ym + 1][xm + 1];
                                kali += value * operandKolom[ym + 1][xm + 1];
                            }
                        }

                        result[y][x] = kali;
                        intervalMinMax[0] = kali < intervalMinMax[0] ? kali : intervalMinMax[0];
                        intervalMinMax[1] = kali > intervalMinMax[1] ? kali : intervalMinMax[1];
                    }
                }

            };
        }
        Threading.runRunnables(runnables);

        int interval = intervalMinMax[1] - intervalMinMax[0];

        runnables = new Runnable[ranges.length];
        for (int r = 0; r < runnables.length; r++) {
            int[] range = ranges[r];
            runnables[r] = () -> {

                for (int y = range[0]; y <= range[1]; y++) {
                    for (int x = 1; x < width - 1; x++) {
                        result[y][x] = (int) Math.round(255 * (result[y][x] - intervalMinMax[0] + 0.0) / interval);
                    }
                }

            };
        }
        Threading.runRunnables(runnables);

        return result;
    }
}
