package pola.app;

import java.awt.image.BufferedImage;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Dimas
 */
public class Gambar {

    public final int width;
    public final int height;
    public final int countColor;

    public final int[][][] original;
    public final int[][] grayscale;
    public final int[][] equalized;
    public final boolean[][] binary;

    public final Histogram histogram;

    public Gambar(File file) throws IOException {
        BufferedImage input = ImageIO.read(file);
        width = input.getWidth();
        height = input.getHeight();
        BufferedImage image = new BufferedImage(width, height, TYPE_INT_RGB); // convert to RGB
        image.getGraphics().drawImage(input, 0, 0, null);

        original = new int[height][width][3];
        grayscale = new int[height][width];
        equalized = new int[height][width];
        binary = new boolean[height][width];
        histogram = new Histogram();

        int colors = 0;
        boolean[][][] flagColors = new boolean[256][256][256];

        // scanline
        WritableRaster raster = image.getRaster();
        int[] pixels = raster.getPixels(0, 0, width, height, (int[]) null);
        int x = 0, y = 0;
        int r, g, b, gray;
        for (int i = 0; i < pixels.length; i += 3) {
            r = pixels[i];
            g = pixels[i + 1];
            b = pixels[i + 2];
            gray = (int) Math.round((r + g + b + 0.0) / 3);

            // set pixels
            original[y][x][0] = r;
            original[y][x][1] = g;
            original[y][x][2] = b;
            grayscale[y][x] = gray;

            // set countColor
            if (!flagColors[r][g][b]) {
                flagColors[r][g][b] = true;
                colors += 1;
            }

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

        // set color count
        countColor = colors;
    }

    public void equalize(int from, int to) {
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

    private float otsu() {
        int total = width * height;
        float sum = 0, sumB = 0;
        float wB = 0, wF;
        float varMax = 0, threshold = 0;

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

    public void binarization() {
        double threshold = otsu();
        binarization(threshold);
    }

    public void binarization(double threshold) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                binary[y][x] = grayscale[y][x] < threshold;
            }
        }
    }

    public static BufferedImage toBufferedImage(int[][][] image) {
        // from RGB
        int width = image[0].length, height = image.length;
        int[] pixels = new int[width * height * 3];
        int x = 0, y = 0;
        for (int i = 0; i < pixels.length; i += 3) {
            pixels[i] = image[y][x][0];
            pixels[i + 1] = image[y][x][1];
            pixels[i + 2] = image[y][x][2];

            x += 1;
            if (x == width) {
                x = 0;
                y += 1;
            }
        }

        BufferedImage result = new BufferedImage(width, height, TYPE_INT_RGB);
        WritableRaster raster = result.getRaster();
        raster.setPixels(0, 0, width, height, pixels);
        return result;
    }

    public static BufferedImage toBufferedImage(int[][] image) {
        // from Grayscale
        int width = image[0].length, height = image.length;
        int[] pixels = new int[width * height * 3];
        int x = 0, y = 0;
        for (int i = 0; i < pixels.length; i += 3) {
            pixels[i] = image[y][x];
            pixels[i + 1] = image[y][x];
            pixels[i + 2] = image[y][x];

            x += 1;
            if (x == width) {
                x = 0;
                y += 1;
            }
        }

        BufferedImage result = new BufferedImage(width, height, TYPE_INT_RGB);
        WritableRaster raster = result.getRaster();
        raster.setPixels(0, 0, width, height, pixels);
        return result;
    }

    public static BufferedImage toBufferedImage(boolean[][] image) {
        // from BW
        int width = image[0].length, height = image.length;
        int[] pixels = new int[width * height * 3];
        int x = 0, y = 0;
        for (int i = 0; i < pixels.length; i += 3) {
            pixels[i] = image[y][x] ? 0 : 255;
            pixels[i + 1] = image[y][x] ? 0 : 255;
            pixels[i + 2] = image[y][x] ? 0 : 255;

            x += 1;
            if (x == width) {
                x = 0;
                y += 1;
            }
        }

        BufferedImage result = new BufferedImage(width, height, TYPE_INT_RGB);
        WritableRaster raster = result.getRaster();
        raster.setPixels(0, 0, width, height, pixels);
        return result;
    }
}
