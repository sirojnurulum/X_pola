package pola.app;

import java.awt.image.BufferedImage;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Dimas
 */
public class Gambar {

    public final int width;
    public final int height;
    public int countColor;

    public final int[][] grayscale;
    public final int[][] equalized;
    public final boolean[][] binary;
    public final boolean[][] bolong;
    public final boolean[][] tulang;

    public BufferedImage biOriginal;
    public BufferedImage biGrayscale;
    public BufferedImage biEqualized;
    public BufferedImage biBinary;
    public BufferedImage biBolong;
    public BufferedImage biTulang;

    public final Histogram histogram;

    public Gambar(File file) throws IOException {
        BufferedImage input = ImageIO.read(file);
        width = input.getWidth();
        height = input.getHeight();
        BufferedImage image = new BufferedImage(width, height, TYPE_INT_RGB); // convert to RGB
        image.getGraphics().drawImage(input, 0, 0, null);

        grayscale = new int[height][width];
        equalized = new int[height][width];
        binary = new boolean[height][width];
        bolong = new boolean[height][width];
        tulang = new boolean[height][width];

        biOriginal = image;
        biGrayscale = new BufferedImage(width, height, TYPE_INT_RGB);
        biEqualized = new BufferedImage(width, height, TYPE_INT_RGB);
        biBinary = new BufferedImage(width, height, TYPE_INT_RGB);
        biBolong = new BufferedImage(width, height, TYPE_INT_RGB);
        biTulang = new BufferedImage(width, height, TYPE_INT_RGB);

        histogram = new Histogram();

        read();
        updateBufferedImage();
    }

    public final void read() {
        WritableRaster raster = biOriginal.getRaster();
        int[] pixels = raster.getPixels(0, 0, width, height, (int[]) null);

        int[][] ranges = Threading.quartering(height);
        Runnable[] runnables = new Runnable[ranges.length];
        for (int t = 0; t < runnables.length; t++) {
            int[] range = ranges[t];
            runnables[t] = () -> {
                
                int r, g, b, gray, p = range[0] * width * 3;
                for (int y = range[0]; y <= range[1]; y++) {
                    for (int x = 0; x < width; x++) {
                        r = pixels[p];
                        g = pixels[p + 1];
                        b = pixels[p + 2];

                        gray = (int) Math.round((r + g + b + 0.0) / 3);
                        grayscale[y][x] = gray;

                        // set histogram
                        histogram.r[r] += 1;
                        histogram.g[g] += 1;
                        histogram.b[b] += 1;
                        histogram.gray[gray] += 1;

                        p += 3;
                    }
                }

            };
        }
        Threading.runRunnables(runnables);
    }

    public final void equalize(int from, int to) {
        class Cdf {

            public int cumm;
            public int scaled;

            public Cdf(int _cumm, int _scaled) {
                cumm = _cumm;
                scaled = _scaled;
            }
        }

        // create LUT
        int countPixel = height * width;
        int fromCdf = histogram.gray[from];
        int cumm = 0;
        int h;

        int interval = to - from + 1;
        Cdf[] cdf = new Cdf[interval];
        for (int i = 0; i < interval; i++) {
            cumm += histogram.gray[i + from];
            h = (int) Math.round(255.0 * (cumm - fromCdf) / (countPixel - fromCdf));
            cdf[i] = new Cdf(cumm, h);
        }

        // reset
        for (int i = 0; i < 256; i++) {
            histogram.equalized[i] = 0;
        }

        // create image
        int[][] ranges = Threading.quartering(height);
        Runnable[] runnables = new Runnable[ranges.length];
        for (int r = 0; r < runnables.length; r++) {
            int[] range = ranges[r];
            runnables[r] = () -> {
                
                int colorNew;
                for (int y = range[0]; y <= range[1]; y++) {
                    for (int x = 0; x < width; x++) {
                        // convert to scale
                        if (grayscale[y][x] < from) {
                            colorNew = 0;
                        } else if (grayscale[y][x] > to) {
                            colorNew = 255;
                        } else {
                            colorNew = cdf[grayscale[y][x] - from].scaled;
                        }
                        equalized[y][x] = colorNew;

                        // get histogram
                        histogram.equalized[colorNew] += 1;
                    }
                }

            };
        }
        Threading.runRunnables(runnables);
    }

    public final void binarization() {
        int threshold = otsu();
        threshold = 128; // TODO: otsu broken, temporarily set to 128
        binarization(threshold);
    }

    public final void binarization(int threshold) {
        int[][] ranges = Threading.quartering(height);
        Runnable[] runnables = new Runnable[ranges.length];
        for (int r = 0; r < runnables.length; r++) {
            int[] range = ranges[r];
            runnables[r] = () -> {
                
                for (int y = range[0]; y <= range[1]; y++) {
                    for (int x = 0; x < width; x++) {
                        binary[y][x] = grayscale[y][x] < threshold;
                    }
                }

            };
        }
        Threading.runRunnables(runnables);
    }

    public final void bolongin() {
        int[][] ranges = Threading.quartering(height);
        Runnable[] runnables = new Runnable[ranges.length];
        for (int r = 0; r < runnables.length; r++) {
            int[] range = ranges[r];
            runnables[r] = () -> {
                
                for (int y = range[0]; y <= range[1]; y++) {
                    for (int x = 0; x < width; x++) {
                        bolong[y][x] = binary[y][x];
                        if (binary[y][x] && // if the pixel is black
                                y > 0 && y < height - 1 && x > 0 && x < width - 1) {  // and not in boundary
                            if (binary[y - 1][x] && binary[y + 1][x] && binary[y][x - 1] && binary[y][x + 1]) { // if all 4 neighbors is black
                                bolong[y][x] = false;
                            }
                        }
                    }
                }

            };
        }
        Threading.runRunnables(runnables);
    }

    public final void tulangin() {
        for (int y = 0; y < height; y++) {
            tulang[y] = Arrays.copyOf(binary[y], width);
        }

        ArrayList<int[]> target;
        boolean changed = true;
        while (changed) {
            changed = false;
            target = new ArrayList<>();

            for (int step = 1; step <= 2; step++) {
                for (int y = 1; y < height - 1; y++) {
                    for (int x = 1; x < width - 1; x++) {
                        if (tulang[y][x]) {
                            int countBlackNeighbor = getCountBlackNeighbor(x, y);
                            int wbTrans = getWBTrans(x, y);
                            int pA = step == 1 ? 6 : 8;
                            int pB = step == 1 ? 4 : 2;

                            if ((countBlackNeighbor >= 2 && countBlackNeighbor <= 6)
                                    && wbTrans == 1
                                    && !(getP(x, y, 2) && getP(x, y, 4) && getP(x, y, pA))
                                    && !(getP(x, y, pB) && getP(x, y, 6) && getP(x, y, 8))) {
                                target.add(new int[]{y, x});
                                changed = true;
                            }
                        }
                    }
                }

                if (changed) {
                    for (int[] point : target) {
                        tulang[point[0]][point[1]] = false;
                    }
                }
            }
        }

        // remove 'tangga'
        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                if (tulang[y][x]) {
                    if ((getP(x, y, 2) && getP(x, y, 3) && getP(x, y, 8))
                            || (getP(x, y, 3) && getP(x, y, 4) && getP(x, y, 6))
                            || (getP(x, y, 4) && getP(x, y, 5) && getP(x, y, 2))
                            || (getP(x, y, 5) && getP(x, y, 6) && getP(x, y, 8))
                            || (getP(x, y, 6) && getP(x, y, 7) && getP(x, y, 4))
                            || (getP(x, y, 7) && getP(x, y, 8) && getP(x, y, 2))
                            || (getP(x, y, 8) && getP(x, y, 9) && getP(x, y, 6))
                            || (getP(x, y, 9) && getP(x, y, 2) && getP(x, y, 4))) {
                        tulang[y][x] = false;
                    }
                }
            }
        }

        // remove 'siku'
        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                if (tulang[y][x]) {
                    int countBlackNeighbor = getCountBlackNeighbor(x, y);
                    if (countBlackNeighbor == 2) {
                        if ((getP(x, y, 2) && (getP(x, y, 4)))
                                || (getP(x, y, 4) && (getP(x, y, 6)))
                                || (getP(x, y, 6) && (getP(x, y, 8)))
                                || (getP(x, y, 8) && (getP(x, y, 2)))) {
                            tulang[y][x] = false;
                        }
                    }
                }
            }
        }
    }

    public final void updateBufferedImage() {
        int[] pixelsGrayscale = new int[width * height * 3];
        int[] pixelsEqualized = new int[width * height * 3];
        int[] pixelsBinary = new int[width * height * 3];
        int[] pixelsBolong = new int[width * height * 3];
        int[] pixelsTulang = new int[width * height * 3];

        int[][] ranges = Threading.quartering(height);
        Runnable[] runnables = new Runnable[ranges.length];
        for (int r = 0; r < runnables.length; r++) {
            int[] range = ranges[r];
            runnables[r] = () -> {
                
                int p = range[0] * width * 3;
                for (int y = range[0]; y <= range[1]; y++) {
                    for (int x = 0; x < width; x++) {
                        for (int j = 0; j < 3; j++) {
                            pixelsGrayscale[p + j] = grayscale[y][x];
                            pixelsEqualized[p + j] = equalized[y][x];
                            pixelsBinary[p + j] = binary[y][x] ? 0 : 255;
                            pixelsBolong[p + j] = bolong[y][x] ? 0 : 255;
                            pixelsTulang[p + j] = tulang[y][x] ? 0 : 255;
                        }
                        
                        p += 3;
                    }
                }

            };
        }
        Threading.runRunnables(runnables);

        biGrayscale.getRaster().setPixels(0, 0, width, height, pixelsGrayscale);
        biEqualized.getRaster().setPixels(0, 0, width, height, pixelsEqualized);
        biBinary.getRaster().setPixels(0, 0, width, height, pixelsBinary);
        biBolong.getRaster().setPixels(0, 0, width, height, pixelsBolong);
        biTulang.getRaster().setPixels(0, 0, width, height, pixelsTulang);
    }

    public static BufferedImage toBufferedImage(int[][] grayscale) {
        int height = grayscale.length;
        int width = grayscale[0].length;
        
        int[] pixels = new int[width * height * 3];
        
        int[][] ranges = Threading.quartering(height);
        Runnable[] runnables = new Runnable[ranges.length];
        for (int r = 0; r < runnables.length; r++) {
            int[] range = ranges[r];
            runnables[r] = () -> {
                
                int p = range[0] * width * 3;
                for (int y = range[0]; y <= range[1]; y++) {
                    for (int x = 0; x < width; x++) {
                        for (int j = 0; j < 3; j++) {
                            pixels[p + j] = grayscale[y][x];
                        }
                        
                        p += 3;
                    }
                }

            };
        }
        Threading.runRunnables(runnables);

        BufferedImage result = new BufferedImage(width, height, TYPE_INT_RGB);
        result.getRaster().setPixels(0, 0, width, height, pixels);
        return result;
    }

    // <editor-fold defaultstate="collapsed" desc="Non-threading methods">
    
    private void readNonThreading() {
        countColor = 0;
        boolean[][][] flagColors = new boolean[256][256][256];

        // scanline
        WritableRaster raster = biOriginal.getRaster();
        int[] pixels = raster.getPixels(0, 0, width, height, (int[]) null);
        int x = 0, y = 0;
        int r, g, b, gray;
        for (int i = 0; i < pixels.length; i += 3) {
            r = pixels[i];
            g = pixels[i + 1];
            b = pixels[i + 2];

            // set countColor
            if (!flagColors[r][g][b]) {
                flagColors[r][g][b] = true;
                countColor += 1;
            }

            gray = (int) Math.round((r + g + b + 0.0) / 3);
            grayscale[y][x] = gray;

            // set histogram
            histogram.r[r] += 1;
            histogram.g[g] += 1;
            histogram.b[b] += 1;
            histogram.gray[gray] += 1;

            x += 1;
            if (x == width) {
                x = 0;
                y += 1;
            }
        }
    }

    public final void equalizeNonThreading(int from, int to) {
        class Cdf {

            public int cumm;
            public int scaled;

            public Cdf(int _cumm, int _scaled) {
                cumm = _cumm;
                scaled = _scaled;
            }
        }

        // create LUT
        int countPixel = height * width;
        int fromCdf = histogram.gray[from];
        int cumm = 0;
        int h;

        int range = to - from + 1;
        Cdf[] cdf = new Cdf[range];
        for (int i = 0; i < range; i++) {
            cumm += histogram.gray[i + from];
            h = (int) Math.round(255.0 * (cumm - fromCdf) / (countPixel - fromCdf));
            cdf[i] = new Cdf(cumm, h);
        }

        // reset
        for (int i = 0; i < 256; i++) {
            histogram.equalized[i] = 0;
        }

        // create image
        int colorNew;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // convert to scale
                if (grayscale[y][x] < from) {
                    colorNew = 0;
                } else if (grayscale[y][x] > to) {
                    colorNew = 255;
                } else {
                    colorNew = cdf[grayscale[y][x] - from].scaled;
                }
                equalized[y][x] = colorNew;

                // get histogram
                histogram.equalized[colorNew] += 1;
            }
        }
    }

    public void binarizationNonThreading(int threshold) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                binary[y][x] = grayscale[y][x] < threshold;
            }
        }
    }

    public final void bolonginNonThreading() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                bolong[y][x] = binary[y][x];
                if (binary[y][x] && // if the pixel is black
                        y > 0 && y < height - 1 && x > 0 && x < width - 1) {  // and not in boundary
                    if (binary[y - 1][x] && binary[y + 1][x] && binary[y][x - 1] && binary[y][x + 1]) { // if all 4 neighbors is black
                        bolong[y][x] = false;
                    }
                }
            }
        }
    }

    public final void updateBufferedImageNonThreading() {
        int[] pixelsGrayscale = new int[width * height * 3];
        int[] pixelsEqualized = new int[width * height * 3];
        int[] pixelsBinary = new int[width * height * 3];
        int[] pixelsBolong = new int[width * height * 3];
        int[] pixelsTulang = new int[width * height * 3];

        int x = 0, y = 0;
        for (int i = 0; i < pixelsGrayscale.length; i += 3) {
            for (int j = 0; j < 3; j++) {
                pixelsGrayscale[i + j] = grayscale[y][x];
                pixelsEqualized[i + j] = equalized[y][x];
                pixelsBinary[i + j] = binary[y][x] ? 0 : 255;
                pixelsBolong[i + j] = bolong[y][x] ? 0 : 255;
                pixelsTulang[i + j] = tulang[y][x] ? 0 : 255;
            }

            x += 1;
            if (x == width) {
                x = 0;
                y += 1;
            }
        }

        biGrayscale.getRaster().setPixels(0, 0, width, height, pixelsGrayscale);
        biEqualized.getRaster().setPixels(0, 0, width, height, pixelsEqualized);
        biBinary.getRaster().setPixels(0, 0, width, height, pixelsBinary);
        biBolong.getRaster().setPixels(0, 0, width, height, pixelsBolong);
        biTulang.getRaster().setPixels(0, 0, width, height, pixelsTulang);
    }

    public static BufferedImage toBufferedImageNonThreading(int[][] grayscale) {
        int height = grayscale.length;
        int width = grayscale[0].length;

        int[] pixels = new int[width * height * 3];
        int x = 0, y = 0;
        for (int i = 0; i < pixels.length; i += 3) {
            for (int j = 0; j < 3; j++) {
                pixels[i + j] = grayscale[y][x];
            }

            x += 1;
            if (x == width) {
                x = 0;
                y += 1;
            }
        }

        BufferedImage result = new BufferedImage(width, height, TYPE_INT_RGB);
        result.getRaster().setPixels(0, 0, width, height, pixels);
        return result;
    }
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Helpers">
    
    private int otsu() {
        int total = width * height;
        float sum = 0, sumB = 0;
        float wB = 0, wF;
        float varMax = 0;
        int threshold = 0;

        for (int i = 0; i < 256; i++) {
            sum += i * histogram.gray[i];
        }
        for (int i = 0; i < 256; i++) {
            wB += histogram.gray[i];
            if (wB == 0) {
                continue;
            }

            wF = total - wB;
            if (wF == 0) {
                break;
            }

            sumB += i * histogram.gray[i];
            float mB = sumB / wB;
            float mF = (sum - sumB) / wF;
            float varBetween = wB * wF * (mB - mF) * (mB - mF);
            if (varBetween > varMax) {
                varMax = varBetween;
                threshold = i;
            }
        }

        return threshold;
    }

    private boolean getP(int x, int y, int p) {
        int newX = p >= 3 && p <= 5 ? x + 1 : (p >= 7 && p <= 9 ? x - 1 : x);
        int newY = p >= 5 && p <= 7 ? y + 1 : (p == 4 || p == 8 ? y : y - 1);
        return tulang[newY][newX];
    }

    private int getCountBlackNeighbor(int x, int y) {
        int result = 0;
        for (int p = 2; p <= 9; p++) {
            result += getP(x, y, p) ? 1 : 0;
        }
        return result;
    }

    private int getWBTrans(int x, int y) {
        int result = 0;
        for (int p = 2; p <= 9; p++) {
            if (p == 9) {
                result += !getP(x, y, 9) && getP(x, y, 2) ? 1 : 0;
            } else {
                result += !getP(x, y, p) && getP(x, y, p + 1) ? 1 : 0;
            }
        }
        return result;
    }

    // </editor-fold>
}
