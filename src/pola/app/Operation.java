/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pola.app;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Siroj Nur Ulum
 */
public class Operation {

    public class Point {

        public int x;
        public int y;

        public Point() {
        }

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void setXY(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }
    }
    public static Operation op;

    public static Operation getOp() {
        if (op == null) {
            op = new Operation();
        }
        return op;
    }

    public BufferedImage convertGray(BufferedImage buffOri) {
        BufferedImage tmp = new BufferedImage(buffOri.getColorModel(), buffOri.copyData(null), buffOri.isAlphaPremultiplied(), null);
        for (int i = 0; i < tmp.getHeight(); i++) {
            for (int j = 0; j < tmp.getWidth(); j++) {
                Color c = new Color(tmp.getRGB(j, i));
                int red = (int) (c.getRed() * 0.299);
                int green = (int) (c.getGreen() * 0.587);
                int blue = (int) (c.getBlue() * 0.114);
                Color newColor = new Color(red + green + blue,
                        red + green + blue, red + green + blue);
                tmp.setRGB(j, i, newColor.getRGB());
            }
        }
        return tmp;
    }

    public BufferedImage convertBw(BufferedImage buffGray) {
        BufferedImage tmp = new BufferedImage(buffGray.getColorModel(), buffGray.copyData(null), buffGray.isAlphaPremultiplied(), null);
        for (int i = 0; i < tmp.getHeight(); i++) {
            for (int j = 0; j < tmp.getWidth(); j++) {
                if (((new Color(tmp.getRGB(j, i)).getRed() + new Color(tmp.getRGB(j, i)).getGreen() + new Color(tmp.getRGB(j, i)).getBlue()) / 3) < 150) {
                    tmp.setRGB(j, i, new Color(0, 0, 0).getRGB());
                } else {
                    tmp.setRGB(j, i, new Color(255, 255, 255).getRGB());
                }
            }
        }
        return tmp;
    }

    public ArrayList<int[]> getHistogramData(BufferedImage image) {
        ArrayList<int[]> data = new ArrayList<>();
        int red[] = new int[256];
        int green[] = new int[256];
        int blue[] = new int[256];
        Arrays.fill(red, 0);
        Arrays.fill(green, 0);
        Arrays.fill(blue, 0);
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                Color color = new Color(image.getRGB(j, i));
                red[color.getRed()]++;
                green[color.getGreen()]++;
                blue[color.getBlue()]++;
            }
        }
        data.add(red);
        data.add(green);
        data.add(blue);
        return data;
    }

    public BufferedImage equalizeImage(BufferedImage buffGray) {
        BufferedImage tmp = new BufferedImage(buffGray.getColorModel(), buffGray.copyData(null), buffGray.isAlphaPremultiplied(), null);
        ArrayList<int[]> dataHisto = getHistogramData(tmp);
        int[] rhistogram = new int[256];
        int[] ghistogram = new int[256];
        int[] bhistogram = new int[256];
        Arrays.fill(rhistogram, 0);
        Arrays.fill(ghistogram, 0);
        Arrays.fill(bhistogram, 0);
        long sumr = 0;
        long sumg = 0;
        long sumb = 0;
        float skala = (float) (255.0 / (tmp.getWidth() * tmp.getHeight()));
        for (int i = 0; i < rhistogram.length; i++) {
            sumr += dataHisto.get(0)[i];
            int valr = (int) (sumr * skala);
            if (valr > 255) {
                rhistogram[i] = 255;
            } else {
                rhistogram[i] = valr;
            }
            sumg += dataHisto.get(1)[i];
            int valg = (int) (sumg * skala);
            if (valg > 255) {
                ghistogram[i] = 255;
            } else {
                ghistogram[i] = valg;
            }
            sumb += dataHisto.get(2)[i];
            int valb = (int) (sumb * skala);
            if (valb > 255) {
                bhistogram[i] = 255;
            } else {
                bhistogram[i] = valb;
            }
        }
        for (int i = 0; i < tmp.getHeight(); i++) {
            for (int j = 0; j < tmp.getWidth(); j++) {
                Color newColor = new Color(rhistogram[new Color(tmp.getRGB(j, i)).getRed()], ghistogram[new Color(tmp.getRGB(j, i)).getGreen()], bhistogram[new Color(tmp.getRGB(j, i)).getBlue()]);
                tmp.setRGB(j, i, newColor.getRGB());
            }
        }
        return tmp;
    }

    public int[][] rollMatrix(int roll, int[][] data) {
        int[][] hasil = new int[data.length][data[0].length];
        int tmp[];
        int rl;
        switch (roll) {
            case 1:
                tmp = data[data.length - 1];
                hasil[0] = tmp;
                for (int i = 1; i < data.length; i++) {
                    hasil[i] = data[i - 1];
                }
                break;
            case 2:
                tmp = data[0];
                for (int i = 0; i < data.length - 1; i++) {
                    hasil[i] = data[i + 1];
                }
                hasil[data.length - 1] = tmp;
                break;
            case 3:
                for (int i = 0; i < data.length; i++) {
                    rl = data[i][data[i].length - 1];
                    hasil[i][0] = rl;
                    for (int j = 1; j < data[i].length; j++) {
                        hasil[i][j] = data[i][j - 1];
                    }
                }
                break;
            case 4:
                for (int i = 0; i < data.length; i++) {
                    rl = data[i][0];
                    for (int j = 0; j < data[i].length - 1; j++) {
                        hasil[i][j] = data[i][j + 1];
                    }
                    hasil[i][data[i].length - 1] = rl;
                }
                break;
        }
        return hasil;
    }

    public int[][] convertBinaryImge(BufferedImage bwImage) {
        int[][] hasil = new int[bwImage.getHeight()][bwImage.getWidth()];
        for (int i = 0; i < bwImage.getHeight(); i++) {
            for (int j = 0; j < bwImage.getWidth(); j++) {
                if (new Color(bwImage.getRGB(j, i)).getRed() == 0) {
                    hasil[i][j] = 1;
                } else {
                    hasil[i][j] = 0;
                }
            }
        }
        return hasil;
    }

    public BufferedImage bolongin(BufferedImage bwImage) {
        BufferedImage tmp = new BufferedImage(bwImage.getColorModel(), bwImage.copyData(null), bwImage.isAlphaPremultiplied(), null);
        int[][] binaryImage = convertBinaryImge(tmp);
        int[][] up = rollMatrix(1, binaryImage);
        int[][] down = rollMatrix(2, binaryImage);
        int[][] right = rollMatrix(3, binaryImage);
        int[][] left = rollMatrix(4, binaryImage);
        int x[][] = new int[tmp.getHeight()][tmp.getWidth()];
        for (int i = 0; i < tmp.getHeight(); i++) {
            for (int j = 0; j < tmp.getWidth(); j++) {
                int res = binaryImage[i][j] - (up[i][j] * down[i][j] * right[i][j] * left[i][j]);
                x[i][j] = res;
                if (res == 1) {
                    tmp.setRGB(j, i, new Color(0, 0, 0).getRGB());
                } else {
                    tmp.setRGB(j, i, new Color(255, 255, 255).getRGB());
                }
            }
        }
        return tmp;
    }

    public int getA(int[][] binaryImage, int y, int x) {
        int count = 0;
        // p2 p3
        if (y - 1 >= 0 && x + 1 < binaryImage[y].length
                && binaryImage[y - 1][x] == 0 && binaryImage[y - 1][x + 1] == 1) {
            count++;
        }
        // p3 p4
        if (y - 1 >= 0 && x + 1 < binaryImage[y].length
                && binaryImage[y - 1][x + 1] == 0 && binaryImage[y][x + 1] == 1) {
            count++;
        }
        // p4 p5
        if (y + 1 < binaryImage.length && x + 1 < binaryImage[y].length
                && binaryImage[y][x + 1] == 0 && binaryImage[y + 1][x + 1] == 1) {
            count++;
        }
        // p5 p6
        if (y + 1 < binaryImage.length && x + 1 < binaryImage[y].length
                && binaryImage[y + 1][x + 1] == 0 && binaryImage[y + 1][x] == 1) {
            count++;
        }
        // p6 p7
        if (y + 1 < binaryImage.length && x - 1 >= 0
                && binaryImage[y + 1][x] == 0 && binaryImage[y + 1][x - 1] == 1) {
            count++;
        }
        // p7 p8
        if (y + 1 < binaryImage.length && x - 1 >= 0
                && binaryImage[y + 1][x - 1] == 0 && binaryImage[y][x - 1] == 1) {
            count++;
        }
        // p8 p9
        if (y - 1 >= 0 && x - 1 >= 0 && binaryImage[y][x - 1] == 0
                && binaryImage[y - 1][x - 1] == 1) {
            count++;
        }
        // p9 p2
        if (y - 1 >= 0 && x - 1 >= 0 && binaryImage[y - 1][x - 1] == 0
                && binaryImage[y - 1][x] == 1) {
            count++;
        }
        return count;
    }

    public int getB(int[][] binaryImage, int y, int x) {
        return binaryImage[y - 1][x] + binaryImage[y - 1][x + 1]
                + binaryImage[y][x + 1] + binaryImage[y + 1][x + 1]
                + binaryImage[y + 1][x] + binaryImage[y + 1][x - 1]
                + binaryImage[y][x - 1] + binaryImage[y - 1][x - 1];
    }

    public BufferedImage tulangin(BufferedImage bwImage) {
        BufferedImage tmp = new BufferedImage(bwImage.getColorModel(), bwImage.copyData(null), bwImage.isAlphaPremultiplied(), null);
        int[][] binaryImage = convertBinaryImge(tmp);
        int a, b;
        List<Point> pointsToChange = new ArrayList<>();
        boolean hasChange;
        do {
            hasChange = false;
            for (int y = 1; y + 1 < binaryImage.length; y++) {
                for (int x = 1; x + 1 < binaryImage[y].length; x++) {
                    a = getA(binaryImage, y, x);
                    b = getB(binaryImage, y, x);
                    if (binaryImage[y][x] == 1
                            && 2 <= b
                            && b <= 6
                            && a == 1
                            && (binaryImage[y - 1][x] * binaryImage[y][x + 1]
                            * binaryImage[y + 1][x] == 0)
                            && (binaryImage[y][x + 1] * binaryImage[y + 1][x]
                            * binaryImage[y][x - 1] == 0)) {
                        pointsToChange.add(new Point(x, y));
                        hasChange = true;
                    }
                }
            }
            pointsToChange.stream().forEach((point) -> {
                binaryImage[point.y][point.x] = 0;
            });
            pointsToChange.clear();
            for (int y = 1; y + 1 < binaryImage.length; y++) {
                for (int x = 1; x + 1 < binaryImage[y].length; x++) {
                    a = getA(binaryImage, y, x);
                    b = getB(binaryImage, y, x);
                    if (binaryImage[y][x] == 1
                            && 2 <= b
                            && b <= 6
                            && a == 1
                            && (binaryImage[y - 1][x] * binaryImage[y][x + 1]
                            * binaryImage[y][x - 1] == 0)
                            && (binaryImage[y - 1][x] * binaryImage[y + 1][x]
                            * binaryImage[y][x - 1] == 0)) {
                        pointsToChange.add(new Point(x, y));
                        hasChange = true;
                    }
                }
            }
            pointsToChange.stream().forEach((point) -> {
                binaryImage[point.y][point.x] = 0;
            });
            pointsToChange.clear();
        } while (hasChange);
        for (int i = 0; i < binaryImage.length; i++) {
            for (int j = 0; j < binaryImage[i].length; j++) {
                int newPix = binaryImage[i][j];
                if (newPix == 1) {
                    newPix = 0;
                } else {
                    newPix = 255;
                }
                Color newColor = new Color(newPix, newPix, newPix);
                tmp.setRGB(j, i, newColor.getRGB());
            }
        }
        return tmp;
    }

    public List<List<String>> getChainCode(BufferedImage bolongImage) {
        BufferedImage tmp = new BufferedImage(bolongImage.getColorModel(), bolongImage.copyData(null), bolongImage.isAlphaPremultiplied(), null);
        List<List<String>> object = new ArrayList();
        Point start = new Point();
        for (int i = 0; i < tmp.getHeight(); i++) {
            for (int j = 0; j < tmp.getWidth(); j++) {
                if (tmp.getRGB(j, i) == -16777216) {
                    start.setXY(j, i);
                    object.add(createChainCode(start, tmp));
                }
            }
        }
        return object;
    }

    private List<String> createChainCode(Point start, BufferedImage tmp) {
        List<String> data = new ArrayList();
        boolean run = true;
        Point current = new Point(start.getX(), start.getY());
        boolean first = true;
        while (run) {
            try {
                if (first) {
                    first = false;
                    if (tmp.getRGB(current.getX() + 1, current.getY()) == -16777216) {
                        data.add("0");
                        current.setXY(current.getX() + 1, current.getY());
                        tmp.setRGB(current.getX(), current.getY(), Color.WHITE.getRGB());
                    } else if (tmp.getRGB(current.getX() + 1, current.getY() - 1) == -16777216) {
                        data.add("7");
                        current.setXY(current.getX() + 1, current.getY() - 1);
                        tmp.setRGB(current.getX(), current.getY(), Color.WHITE.getRGB());
                    } else if (tmp.getRGB(current.getX() + 1, current.getY() + 1) == -16777216) {
                        data.add("1");
                        current.setXY(current.getX() + 1, current.getY() + 1);
                        tmp.setRGB(current.getX(), current.getY(), Color.WHITE.getRGB());
                    } else if (tmp.getRGB(current.getX(), current.getY() - 1) == -16777216) {
                        data.add("6");
                        current.setXY(current.getX(), current.getY() - 1);
                        tmp.setRGB(current.getX(), current.getY(), Color.WHITE.getRGB());
                    } else if (tmp.getRGB(current.getX(), current.getY() + 1) == -16777216) {
                        data.add("2");
                        current.setXY(current.getX(), current.getY() + 1);
                        tmp.setRGB(current.getX(), current.getY(), Color.WHITE.getRGB());
                    } else if (tmp.getRGB(current.getX() - 1, current.getY() - 1) == -16777216) {
                        data.add("5");
                        current.setXY(current.getX() - 1, current.getY() - 1);
                        tmp.setRGB(current.getX(), current.getY(), Color.WHITE.getRGB());
                    } else if (tmp.getRGB(current.getX() - 1, current.getY() + 1) == -16777216) {
                        data.add("3");
                        current.setXY(current.getX() - 1, current.getY() + 1);
                        tmp.setRGB(current.getX(), current.getY(), Color.WHITE.getRGB());
                    } else if (tmp.getRGB(current.getX() - 1, current.getY()) == -16777216) {
                        data.add("4");
                        current.setXY(current.getX() - 1, current.getY());
                        tmp.setRGB(current.getX(), current.getY(), Color.WHITE.getRGB());
                    } else {
                        run = false;
                    }
                } else {
                    if (current.getX() == start.getX() && current.getY() == start.getY()) {
                        run = false;
                    } else {
                        if (tmp.getRGB(current.getX() + 1, current.getY()) == -16777216) {
                            data.add("0");
                            current.setXY(current.getX() + 1, current.getY());
                            tmp.setRGB(current.getX(), current.getY(), Color.WHITE.getRGB());
                        } else if (tmp.getRGB(current.getX() + 1, current.getY() - 1) == -16777216) {
                            data.add("7");
                            current.setXY(current.getX() + 1, current.getY() - 1);
                            tmp.setRGB(current.getX(), current.getY(), Color.WHITE.getRGB());
                        } else if (tmp.getRGB(current.getX() + 1, current.getY() + 1) == -16777216) {
                            data.add("1");
                            current.setXY(current.getX() + 1, current.getY() + 1);
                            tmp.setRGB(current.getX(), current.getY(), Color.WHITE.getRGB());
                        } else if (tmp.getRGB(current.getX(), current.getY() - 1) == -16777216) {
                            data.add("6");
                            current.setXY(current.getX(), current.getY() - 1);
                            tmp.setRGB(current.getX(), current.getY(), Color.WHITE.getRGB());
                        } else if (tmp.getRGB(current.getX(), current.getY() + 1) == -16777216) {
                            data.add("2");
                            current.setXY(current.getX(), current.getY() + 1);
                            tmp.setRGB(current.getX(), current.getY(), Color.WHITE.getRGB());
                        } else if (tmp.getRGB(current.getX() - 1, current.getY() - 1) == -16777216) {
                            data.add("5");
                            current.setXY(current.getX() - 1, current.getY() - 1);
                            tmp.setRGB(current.getX(), current.getY(), Color.WHITE.getRGB());
                        } else if (tmp.getRGB(current.getX() - 1, current.getY() + 1) == -16777216) {
                            data.add("3");
                            current.setXY(current.getX() - 1, current.getY() + 1);
                            tmp.setRGB(current.getX(), current.getY(), Color.WHITE.getRGB());
                        } else if (tmp.getRGB(current.getX() - 1, current.getY()) == -16777216) {
                            data.add("4");
                            current.setXY(current.getX() - 1, current.getY());
                            tmp.setRGB(current.getX(), current.getY(), Color.WHITE.getRGB());
                        } else {
                            run = false;
                        }
                    }
                }
            } catch (Exception e) {
                run = false;
            }
        }
        return data;
    }

    public List<List<String>> getKodeBelok(List<List<String>> chainData) {
        List<List<String>> data = new ArrayList();
        List<String> tmpData;
        for (List<String> tmp : chainData) {
            tmpData = new ArrayList();
            if (tmp.size() > 0) {
                for (int i = 0; i < tmp.size() - 1; i++) {
                    tmpData.add(tmp.get(i));
                    switch (tmp.get(i)) {
                        case "0":
                            switch (tmp.get(i + 1)) {
                                case "1":
                                case "2":
                                case "3":
                                    tmpData.add("+");
                                    break;
                                case "7":
                                case "6":
                                case "5":
                                    tmpData.add("-");
                                    break;
                            }
                            break;
                        case "1":
                            switch (tmp.get(i + 1)) {
                                case "2":
                                case "3":
                                case "4":
                                    tmpData.add("+");
                                    break;
                                case "0":
                                case "7":
                                case "6":
                                    tmpData.add("-");
                                    break;
                            }
                            break;
                        case "2":
                            switch (tmp.get(i + 1)) {
                                case "3":
                                case "4":
                                case "5":
                                    tmpData.add("+");
                                    break;
                                case "1":
                                case "0":
                                case "7":
                                    tmpData.add("-");
                                    break;
                            }
                            break;
                        case "3":
                            switch (tmp.get(i + 1)) {
                                case "4":
                                case "5":
                                case "6":
                                    tmpData.add("+");
                                    break;
                                case "2":
                                case "1":
                                case "0":
                                    tmpData.add("-");
                                    break;
                            }
                            break;
                        case "4":
                            switch (tmp.get(i + 1)) {
                                case "5":
                                case "6":
                                case "7":
                                    tmpData.add("+");
                                    break;
                                case "3":
                                case "2":
                                case "1":
                                    tmpData.add("-");
                                    break;
                            }
                            break;
                        case "5":
                            switch (tmp.get(i + 1)) {
                                case "6":
                                case "7":
                                case "0":
                                    tmpData.add("+");
                                    break;
                                case "4":
                                case "3":
                                case "2":
                                    tmpData.add("-");
                                    break;
                            }
                            break;
                        case "6":
                            switch (tmp.get(i + 1)) {
                                case "7":
                                case "0":
                                case "1":
                                    tmpData.add("+");
                                    break;
                                case "5":
                                case "4":
                                case "3":
                                    tmpData.add("-");
                                    break;
                            }
                            break;
                        case "7":
                            switch (tmp.get(i + 1)) {
                                case "0":
                                case "1":
                                case "2":
                                    tmpData.add("+");
                                    break;
                                case "6":
                                case "5":
                                case "4":
                                    tmpData.add("-");
                                    break;
                            }
                            break;
                    }
                }
            }
            data.add(tmpData);
        }
        return data;
    }

    public String deteksi() {
        String hasil = null;

        return hasil;
    }

    public int getTatangga(BufferedImage image, Point p) {
        int count = 0;
        try {
            if (p.x == 0 && p.y == 0) {
                if (image.getRGB(p.x + 1, p.y) == -16777216) {
                    count++;
                }
                if (image.getRGB(p.x + 1, p.y + 1) == -16777216) {
                    count++;
                }
                if (image.getRGB(p.x, p.y + 1) == -16777216) {
                    count++;
                }
            } else if (p.x == 0 && p.y == image.getHeight()) {
                if (image.getRGB(p.x + 1, p.y) == -16777216) {
                    count++;
                }
                if (image.getRGB(p.x + 1, p.y - 1) == -16777216) {
                    count++;
                }
                if (image.getRGB(p.x, p.y - 1) == -16777216) {
                    count++;
                }
            } else if (p.x == image.getWidth() && p.y == 0) {
                if (image.getRGB(p.x, p.y + 1) == -16777216) {
                    count++;
                }
                if (image.getRGB(p.x - 1, p.y + 1) == -16777216) {
                    count++;
                }
                if (image.getRGB(p.x - 1, p.y) == -16777216) {
                    count++;
                }
            } else if (p.x == image.getWidth() && p.y == image.getHeight()) {
                if (image.getRGB(p.x, p.y - 1) == -16777216) {
                    count++;
                }
                if (image.getRGB(p.x - 1, p.y - 1) == -16777216) {
                    count++;
                }
                if (image.getRGB(p.x - 1, p.y) == -16777216) {
                    count++;
                }
            } else if (p.x == 0) {
                if (image.getRGB(p.x + 1, p.y) == -16777216) {
                    count++;
                }
                if (image.getRGB(p.x + 1, p.y - 1) == -16777216) {
                    count++;
                }
                if (image.getRGB(p.x + 1, p.y + 1) == -16777216) {
                    count++;
                }
                if (image.getRGB(p.x, p.y - 1) == -16777216) {
                    count++;
                }
                if (image.getRGB(p.x, p.y + 1) == -16777216) {
                    count++;
                }
            } else if (p.x == image.getWidth()) {
                if (image.getRGB(p.x, p.y - 1) == -16777216) {
                    count++;
                }
                if (image.getRGB(p.x, p.y + 1) == -16777216) {
                    count++;
                }
                if (image.getRGB(p.x - 1, p.y - 1) == -16777216) {
                    count++;
                }
                if (image.getRGB(p.x - 1, p.y + 1) == -16777216) {
                    count++;
                }
                if (image.getRGB(p.x - 1, p.y) == -16777216) {
                    count++;
                }
            } else if (p.y == 0) {
                if (image.getRGB(p.x + 1, p.y) == -16777216) {
                    count++;
                }
                if (image.getRGB(p.x + 1, p.y + 1) == -16777216) {
                    count++;
                }
                if (image.getRGB(p.x, p.y + 1) == -16777216) {
                    count++;
                }
                if (image.getRGB(p.x - 1, p.y + 1) == -16777216) {
                    count++;
                }
                if (image.getRGB(p.x - 1, p.y) == -16777216) {
                    count++;
                }
            } else if (p.y == image.getHeight()) {
                if (image.getRGB(p.x + 1, p.y) == -16777216) {
                    count++;
                }
                if (image.getRGB(p.x + 1, p.y - 1) == -16777216) {
                    count++;
                }
                if (image.getRGB(p.x, p.y - 1) == -16777216) {
                    count++;
                }
                if (image.getRGB(p.x - 1, p.y - 1) == -16777216) {
                    count++;
                }
                if (image.getRGB(p.x - 1, p.y) == -16777216) {
                    count++;
                }
            } else {
                if (image.getRGB(p.x + 1, p.y) == -16777216) {
                    count++;
                }
                if (image.getRGB(p.x + 1, p.y - 1) == -16777216) {
                    count++;
                }
                if (image.getRGB(p.x + 1, p.y + 1) == -16777216) {
                    count++;
                }
                if (image.getRGB(p.x, p.y - 1) == -16777216) {
                    count++;
                }
                if (image.getRGB(p.x, p.y + 1) == -16777216) {
                    count++;
                }
                if (image.getRGB(p.x - 1, p.y - 1) == -16777216) {
                    count++;
                }
                if (image.getRGB(p.x - 1, p.y + 1) == -16777216) {
                    count++;
                }
                if (image.getRGB(p.x - 1, p.y) == -16777216) {
                    count++;
                }
            }
        } catch (Exception e) {
            System.out.println("pinggir teuing");
            count = 2;
        }
        return count;
    }

    public List<String> getCabang(BufferedImage tulang) {
        List<String> data = new ArrayList();
        Point start = new Point();
        for (int i = 0; i < tulang.getWidth(); i++) {
            for (int j = 0; j < tulang.getHeight(); j++) {
                if (tulang.getRGB(i, j) == -16777216) {
                    start.setXY(i, j);
                    if (getTatangga(tulang, start) > 2) {
                        data.add("X : " + i + " Y : " + j + " / ");
                    }
                }
            }
        }
        return data;
    }

    public List<List<String>> getChainCodeTulang(BufferedImage tulang) {
        BufferedImage tmp = new BufferedImage(tulang.getColorModel(), tulang.copyData(null), tulang.isAlphaPremultiplied(), null);
        List<List<String>> object = new ArrayList();
        object.add(getCabang(tulang));
        Point start = new Point();
        for (int i = 0; i < tmp.getWidth(); i++) {
            for (int j = 0; j < tmp.getHeight(); j++) {
                if (tmp.getRGB(i, j) == -16777216) {
                    start.setXY(i, j);
//                    System.out.println("->x" + i + "->h" + j);
                    object.add(createChainCodeTulang(start, tmp));
                }
            }
        }
        return object;
    }

    public String getChainBelokHuruf(List<List<String>> data) {
        List<List<String>> bl = getKodeBelok(data);
        String chain = "";
        int q = 1;
        for (List<String> x : bl) {
//            chain += "Object : " + q + "\n";
            Iterator j = x.iterator();
            while (j.hasNext()) {
                chain += j.next() + ",";
            }
//            chain += "\n";
            q++;
        }
        System.out.println("Op : " + chain);
        return chain;
    }

    public List<String> createChainCodeTulang(Point start, BufferedImage tulang) {
        List<String> data = new ArrayList();
        Point current = new Point(start.x, start.y);
        boolean run = true;
        while (run) {
            try {
                if (tulang.getRGB(current.x + 1, current.getY()) == -16777216) {
                    data.add("0");
                    current.setXY(current.x + 1, current.getY());
                    tulang.setRGB(current.x, current.getY(), Color.WHITE.getRGB());
                } else if (tulang.getRGB(current.x + 1, current.getY() - 1) == -16777216) {
                    data.add("7");
                    current.setXY(current.getX() + 1, current.getY() - 1);
                    tulang.setRGB(current.getX(), current.getY(), Color.WHITE.getRGB());
                } else if (tulang.getRGB(current.getX() + 1, current.getY() + 1) == -16777216) {
                    data.add("1");
                    current.setXY(current.getX() + 1, current.getY() + 1);
                    tulang.setRGB(current.getX(), current.getY(), Color.WHITE.getRGB());
                } else if (tulang.getRGB(current.getX(), current.getY() - 1) == -16777216) {
                    data.add("6");
                    current.setXY(current.getX(), current.getY() - 1);
                    tulang.setRGB(current.getX(), current.getY(), Color.WHITE.getRGB());
                } else if (tulang.getRGB(current.getX(), current.getY() + 1) == -16777216) {
                    data.add("2");
                    current.setXY(current.getX(), current.getY() + 1);
                    tulang.setRGB(current.getX(), current.getY(), Color.WHITE.getRGB());
                } else if (tulang.getRGB(current.getX() - 1, current.getY() - 1) == -16777216) {
                    data.add("5");
                    current.setXY(current.getX() - 1, current.getY() - 1);
                    tulang.setRGB(current.getX(), current.getY(), Color.WHITE.getRGB());
                } else if (tulang.getRGB(current.getX() - 1, current.getY() + 1) == -16777216) {
                    data.add("3");
                    current.setXY(current.getX() - 1, current.getY() + 1);
                    tulang.setRGB(current.getX(), current.getY(), Color.WHITE.getRGB());
                } else if (tulang.getRGB(current.getX() - 1, current.getY()) == -16777216) {
                    data.add("4");
                    current.setXY(current.getX() - 1, current.getY());
                    tulang.setRGB(current.getX(), current.getY(), Color.WHITE.getRGB());
                } else {
                    run = false;
                }
            } catch (Exception e) {
                System.out.println("out out");
                run = false;
            }
        }
        return data;
    }

    public BufferedImage benerinTulang(BufferedImage tulang) {
        BufferedImage tmp = new BufferedImage(tulang.getColorModel(), tulang.copyData(null), tulang.isAlphaPremultiplied(), null);
        Point p = new Point();
        for (int i = 0; i < tmp.getHeight(); i++) {
            for (int j = 0; j < tmp.getWidth(); j++) {
                if (tmp.getRGB(i, j) == -16777216) {
                    p.setXY(j, i);
                    if (getTatangga(tmp, p) > 2) {

                    }
                }
            }
        }
        return tmp;
    }
}
