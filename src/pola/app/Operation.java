/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pola.app;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
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
    }
    public static Operation op;

    public static Operation getOp() {
        if (op == null) {
            op = new Operation();
        }
        return op;
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
        Point current = new Point(start.x, start.y);
        boolean first = true;
        while (run) {
            try {
                if (first) {
                    first = false;
                    if (tmp.getRGB(current.x + 1, current.y) == -16777216) {
                        data.add("0");
                        current.setXY(current.x + 1, current.y);
                        tmp.setRGB(current.x, current.y, Color.WHITE.getRGB());
                    } else if (tmp.getRGB(current.x, current.y + 1) == -16777216) {
                        data.add("2");
                        current.setXY(current.x, current.y + 1);
                        tmp.setRGB(current.x, current.y, Color.WHITE.getRGB());
                    } else if (tmp.getRGB(current.x - 1, current.y) == -16777216) {
                        data.add("4");
                        current.setXY(current.x - 1, current.y);
                        tmp.setRGB(current.x, current.y, Color.WHITE.getRGB());
                    } else if (tmp.getRGB(current.x, current.y - 1) == -16777216) {
                        data.add("6");
                        current.setXY(current.x, current.y - 1);
                        tmp.setRGB(current.x, current.y, Color.WHITE.getRGB());
                    } else if (tmp.getRGB(current.x + 1, current.y + 1) == -16777216) {
                        data.add("1");
                        current.setXY(current.x + 1, current.y + 1);
                        tmp.setRGB(current.x, current.y, Color.WHITE.getRGB());
                    } else if (tmp.getRGB(current.x - 1, current.y + 1) == -16777216) {
                        data.add("3");
                        current.setXY(current.x - 1, current.y + 1);
                        tmp.setRGB(current.x, current.y, Color.WHITE.getRGB());
                    } else if (tmp.getRGB(current.x - 1, current.y - 1) == -16777216) {
                        data.add("5");
                        current.setXY(current.x - 1, current.y - 1);
                        tmp.setRGB(current.x, current.y, Color.WHITE.getRGB());
                    } else if (tmp.getRGB(current.x + 1, current.y - 1) == -16777216) {
                        data.add("7");
                        current.setXY(current.x + 1, current.y - 1);
                        tmp.setRGB(current.x, current.y, Color.WHITE.getRGB());
                    } else {
                        run = false;
                    }
                } else {
                    if (current.x == start.x && current.y == start.y) {
                        run = false;
                    } else {
                        if (tmp.getRGB(current.x + 1, current.y) == -16777216) {
                            data.add("0");
                            current.setXY(current.x + 1, current.y);
                            tmp.setRGB(current.x, current.y, Color.WHITE.getRGB());
                        } else if (tmp.getRGB(current.x, current.y + 1) == -16777216) {
                            data.add("2");
                            current.setXY(current.x, current.y + 1);
                            tmp.setRGB(current.x, current.y, Color.WHITE.getRGB());
                        } else if (tmp.getRGB(current.x - 1, current.y) == -16777216) {
                            data.add("4");
                            current.setXY(current.x - 1, current.y);
                            tmp.setRGB(current.x, current.y, Color.WHITE.getRGB());
                        } else if (tmp.getRGB(current.x, current.y - 1) == -16777216) {
                            data.add("6");
                            current.setXY(current.x, current.y - 1);
                            tmp.setRGB(current.x, current.y, Color.WHITE.getRGB());
                        } else if (tmp.getRGB(current.x + 1, current.y + 1) == -16777216) {
                            data.add("1");
                            current.setXY(current.x + 1, current.y + 1);
                            tmp.setRGB(current.x, current.y, Color.WHITE.getRGB());
                        } else if (tmp.getRGB(current.x - 1, current.y + 1) == -16777216) {
                            data.add("3");
                            current.setXY(current.x - 1, current.y + 1);
                            tmp.setRGB(current.x, current.y, Color.WHITE.getRGB());
                        } else if (tmp.getRGB(current.x - 1, current.y - 1) == -16777216) {
                            data.add("5");
                            current.setXY(current.x - 1, current.y - 1);
                            tmp.setRGB(current.x, current.y, Color.WHITE.getRGB());
                        } else if (tmp.getRGB(current.x + 1, current.y - 1) == -16777216) {
                            data.add("7");
                            current.setXY(current.x + 1, current.y - 1);
                            tmp.setRGB(current.x, current.y, Color.WHITE.getRGB());
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

    public int getCountTatangga(BufferedImage image, Point p) {
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
                    if (getCountTatangga(tulang, start) > 2) {
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
                    if (getCountTatangga(tmp, start) < 2) {
                        System.out.println("X:" + i + " Y:" + j);
                        object.add(createChainCodeTulang(start, tmp));
                    }
                }
            }
        }
        return object;
    }

    public List<String> createChainCodeTulang(Point start, BufferedImage tulang) {
        List<String> data = new ArrayList();
        Point current = new Point(start.x, start.y);
        boolean run = true;
        while (run) {
            try {
                if (tulang.getRGB(current.x + 1, current.y) == -16777216) {
                    data.add("0");
                    current.setXY(current.x + 1, current.y);
                    tulang.setRGB(current.x, current.y, Color.WHITE.getRGB());
                } else if (tulang.getRGB(current.x, current.y + 1) == -16777216) {
                    data.add("2");
                    current.setXY(current.x, current.y + 1);
                    tulang.setRGB(current.x, current.y, Color.WHITE.getRGB());
                } else if (tulang.getRGB(current.x - 1, current.y) == -16777216) {
                    data.add("4");
                    current.setXY(current.x - 1, current.y);
                    tulang.setRGB(current.x, current.y, Color.WHITE.getRGB());
                } else if (tulang.getRGB(current.x, current.y - 1) == -16777216) {
                    data.add("6");
                    current.setXY(current.x, current.y - 1);
                    tulang.setRGB(current.x, current.y, Color.WHITE.getRGB());
                } else if (tulang.getRGB(current.x + 1, current.y + 1) == -16777216) {
                    data.add("1");
                    current.setXY(current.x + 1, current.y + 1);
                    tulang.setRGB(current.x, current.y, Color.WHITE.getRGB());
                } else if (tulang.getRGB(current.x - 1, current.y + 1) == -16777216) {
                    data.add("3");
                    current.setXY(current.x - 1, current.y + 1);
                    tulang.setRGB(current.x, current.y, Color.WHITE.getRGB());
                } else if (tulang.getRGB(current.x - 1, current.y - 1) == -16777216) {
                    data.add("5");
                    current.setXY(current.x - 1, current.y - 1);
                    tulang.setRGB(current.x, current.y, Color.WHITE.getRGB());
                } else if (tulang.getRGB(current.x + 1, current.y - 1) == -16777216) {
                    data.add("7");
                    current.setXY(current.x + 1, current.y - 1);
                    tulang.setRGB(current.x, current.y, Color.WHITE.getRGB());
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

    public String createTextChainCodeKodeBelok(List<List<String>> chainData) {
        String chain = "";
        int q = 1;
        for (List<String> x : chainData) {
            if (x.size() > 0) {
                chain += "Object : " + q + "\n";
                Iterator j = x.iterator();
                while (j.hasNext()) {
                    chain += j.next() + ",";
                }
                chain += "\n";
                q++;
            }
        }
        return chain;
    }

    public String createTextCabang(List<String> cabang) {
        String tCabang = "";
        Iterator i = cabang.iterator();
        tCabang += "Cabang : \n";
        while (i.hasNext()) {
            tCabang += i.next();
        }
        return tCabang;
    }

    public String createTextChainCodeTulang(List<List<String>> data) {
        List<List<String>> bl = getKodeBelok(data);
        String chain = "";
        int q = 1;
        for (List<String> x : bl) {
            Iterator j = x.iterator();
            while (j.hasNext()) {
                chain += j.next() + ",";
            }
            q++;
        }
        return chain;
    }

    public int checkColor(int color) {
        if (color >= 255) {
            return 255;
        } else {
            return color;
        }
    }

    public BufferedImage buremin(BufferedImage image) {
        BufferedImage tmp = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        tmp.getGraphics().drawImage(image, 0, 0, null);
        WritableRaster raster = tmp.getRaster();
        int[] sample = new int[3];
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                if (y == 0) {
                    if (x == 0) {
                        int r = ((raster.getPixel(x, y, (int[]) null)[0])
                                + (raster.getPixel(x + 1, y, (int[]) null)[0] * 2)
                                + (raster.getPixel(x, y + 1, (int[]) null)[0] * 2)
                                + (raster.getPixel(x + 1, y + 1, (int[]) null)[0] * 4)) / 9;
                        int g = ((raster.getPixel(x, y, (int[]) null)[1])
                                + (raster.getPixel(x + 1, y, (int[]) null)[1] * 2)
                                + (raster.getPixel(x, y + 1, (int[]) null)[1] * 2)
                                + (raster.getPixel(x + 1, y + 1, (int[]) null)[1] * 4)) / 9;
                        int b = ((raster.getPixel(x, y, (int[]) null)[2])
                                + (raster.getPixel(x + 1, y, (int[]) null)[2] * 2)
                                + (raster.getPixel(x, y + 1, (int[]) null)[2] * 2)
                                + (raster.getPixel(x + 1, y + 1, (int[]) null)[2] * 4)) / 9;
                        sample[0] = checkColor(r);
                        sample[1] = checkColor(g);
                        sample[2] = checkColor(b);
                        raster.setPixel(x, y, sample);
                    } else if (x == tmp.getWidth() - 1) {
                        int r = ((raster.getPixel(x, y, (int[]) null)[0])
                                + (raster.getPixel(x - 1, y, (int[]) null)[0] * 2)
                                + (raster.getPixel(x, y + 1, (int[]) null)[0] * 2)
                                + (raster.getPixel(x - 1, y + 1, (int[]) null)[0] * 4)) / 9;
                        int g = ((raster.getPixel(x, y, (int[]) null)[1])
                                + (raster.getPixel(x - 1, y, (int[]) null)[1] * 2)
                                + (raster.getPixel(x, y + 1, (int[]) null)[1] * 2)
                                + (raster.getPixel(x - 1, y + 1, (int[]) null)[1] * 4)) / 9;
                        int b = ((raster.getPixel(x, y, (int[]) null)[2])
                                + (raster.getPixel(x - 1, y, (int[]) null)[2] * 2)
                                + (raster.getPixel(x, y + 1, (int[]) null)[2] * 2)
                                + (raster.getPixel(x - 1, y + 1, (int[]) null)[2] * 4)) / 9;
                        sample[0] = checkColor(r);
                        sample[1] = checkColor(g);
                        sample[2] = checkColor(b);
                        raster.setPixel(x, y, sample);
                    } else {
                        int r = ((raster.getPixel(x, y, (int[]) null)[0])
                                + (raster.getPixel(x - 1, y, (int[]) null)[0])
                                + (raster.getPixel(x + 1, y, (int[]) null)[0])
                                + (raster.getPixel(x - 1, y + 1, (int[]) null)[0] * 2)
                                + (raster.getPixel(x + 1, y + 1, (int[]) null)[0] * 2)
                                + (raster.getPixel(x, y + 1, (int[]) null)[0] * 2))
                                / 9;
                        int g = ((raster.getPixel(x, y, (int[]) null)[1])
                                + (raster.getPixel(x - 1, y, (int[]) null)[1])
                                + (raster.getPixel(x + 1, y, (int[]) null)[1])
                                + (raster.getPixel(x - 1, y + 1, (int[]) null)[1] * 2)
                                + (raster.getPixel(x + 1, y + 1, (int[]) null)[1] * 2)
                                + (raster.getPixel(x, y + 1, (int[]) null)[1] * 2))
                                / 9;
                        int b = ((raster.getPixel(x, y, (int[]) null)[2])
                                + (raster.getPixel(x - 1, y, (int[]) null)[2])
                                + (raster.getPixel(x + 1, y, (int[]) null)[2])
                                + (raster.getPixel(x - 1, y + 1, (int[]) null)[2] * 2)
                                + (raster.getPixel(x + 1, y + 1, (int[]) null)[2] * 2)
                                + (raster.getPixel(x, y + 1, (int[]) null)[2] * 2))
                                / 9;
                        sample[0] = checkColor(r);
                        sample[1] = checkColor(g);
                        sample[2] = checkColor(b);
                        raster.setPixel(x, y, sample);
                    }
                } else if (y == tmp.getHeight() - 1) {
                    if (x == 0) {
                        int r = ((raster.getPixel(x, y, (int[]) null)[0])
                                + (raster.getPixel(x + 1, y, (int[]) null)[0] * 2)
                                + (raster.getPixel(x, y - 1, (int[]) null)[0] * 2)
                                + (raster.getPixel(x + 1, y - 1, (int[]) null)[0] * 4)) / 9;
                        int g = ((raster.getPixel(x, y, (int[]) null)[1])
                                + (raster.getPixel(x + 1, y, (int[]) null)[1] * 2)
                                + (raster.getPixel(x, y - 1, (int[]) null)[1] * 2)
                                + (raster.getPixel(x + 1, y - 1, (int[]) null)[1] * 4)) / 9;
                        int b = ((raster.getPixel(x, y, (int[]) null)[2])
                                + (raster.getPixel(x + 1, y, (int[]) null)[2] * 2)
                                + (raster.getPixel(x, y - 1, (int[]) null)[2] * 2)
                                + (raster.getPixel(x + 1, y - 1, (int[]) null)[2] * 4)) / 9;
                        sample[0] = checkColor(r);
                        sample[1] = checkColor(g);
                        sample[2] = checkColor(b);
                        raster.setPixel(x, y, sample);
                    } else if (x == tmp.getWidth() - 1) {
                        int r = ((raster.getPixel(x, y, (int[]) null)[0])
                                + (raster.getPixel(x - 1, y, (int[]) null)[0] * 2)
                                + (raster.getPixel(x, y - 1, (int[]) null)[0] * 2)
                                + (raster.getPixel(x - 1, y - 1, (int[]) null)[0] * 4)) / 9;
                        int g = ((raster.getPixel(x, y, (int[]) null)[1])
                                + (raster.getPixel(x - 1, y, (int[]) null)[1] * 2)
                                + (raster.getPixel(x, y - 1, (int[]) null)[1] * 2)
                                + (raster.getPixel(x - 1, y - 1, (int[]) null)[1] * 4)) / 9;
                        int b = ((raster.getPixel(x, y, (int[]) null)[2])
                                + (raster.getPixel(x - 1, y, (int[]) null)[2] * 2)
                                + (raster.getPixel(x, y - 1, (int[]) null)[2] * 2)
                                + (raster.getPixel(x - 1, y - 1, (int[]) null)[2] * 4)) / 9;
                        sample[0] = checkColor(r);
                        sample[1] = checkColor(g);
                        sample[2] = checkColor(b);
                        raster.setPixel(x, y, sample);
                    } else {
                        int r = ((raster.getPixel(x, y, (int[]) null)[0])
                                + (raster.getPixel(x - 1, y, (int[]) null)[0])
                                + (raster.getPixel(x + 1, y, (int[]) null)[0])
                                + (raster.getPixel(x - 1, y - 1, (int[]) null)[0] * 2)
                                + (raster.getPixel(x + 1, y - 1, (int[]) null)[0] * 2)
                                + (raster.getPixel(x, y - 1, (int[]) null)[0] * 2))
                                / 9;
                        int g = ((raster.getPixel(x, y, (int[]) null)[1])
                                + (raster.getPixel(x - 1, y, (int[]) null)[1])
                                + (raster.getPixel(x + 1, y, (int[]) null)[1])
                                + (raster.getPixel(x - 1, y - 1, (int[]) null)[1] * 2)
                                + (raster.getPixel(x + 1, y - 1, (int[]) null)[1] * 2)
                                + (raster.getPixel(x, y - 1, (int[]) null)[1] * 2))
                                / 9;
                        int b = ((raster.getPixel(x, y, (int[]) null)[2])
                                + (raster.getPixel(x - 1, y, (int[]) null)[2])
                                + (raster.getPixel(x + 1, y, (int[]) null)[2])
                                + (raster.getPixel(x - 1, y - 1, (int[]) null)[2] * 2)
                                + (raster.getPixel(x + 1, y - 1, (int[]) null)[2] * 2)
                                + (raster.getPixel(x, y - 1, (int[]) null)[2] * 2))
                                / 9;
                        sample[0] = checkColor(r);
                        sample[1] = checkColor(g);
                        sample[2] = checkColor(b);
                        raster.setPixel(x, y, sample);
                    }
                } else {
                    if (x == 0) {
                        int r = ((raster.getPixel(x, y, (int[]) null)[0])
                                + (raster.getPixel(x, y - 1, (int[]) null)[0])
                                + (raster.getPixel(x, y + 1, (int[]) null)[0])
                                + (raster.getPixel(x + 1, y, (int[]) null)[0] * 2)
                                + (raster.getPixel(x + 1, y + 1, (int[]) null)[0] * 2)
                                + (raster.getPixel(x + 1, y - 1, (int[]) null)[0] * 2))
                                / 9;
                        int g = ((raster.getPixel(x, y, (int[]) null)[1])
                                + (raster.getPixel(x, y - 1, (int[]) null)[1])
                                + (raster.getPixel(x, y + 1, (int[]) null)[1])
                                + (raster.getPixel(x + 1, y, (int[]) null)[1] * 2)
                                + (raster.getPixel(x + 1, y + 1, (int[]) null)[1] * 2)
                                + (raster.getPixel(x + 1, y - 1, (int[]) null)[1] * 2))
                                / 9;
                        int b = ((raster.getPixel(x, y, (int[]) null)[2])
                                + (raster.getPixel(x, y - 1, (int[]) null)[2])
                                + (raster.getPixel(x, y + 1, (int[]) null)[2])
                                + (raster.getPixel(x + 1, y, (int[]) null)[2] * 2)
                                + (raster.getPixel(x + 1, y + 1, (int[]) null)[2] * 2)
                                + (raster.getPixel(x + 1, y - 1, (int[]) null)[2] * 2))
                                / 9;
                        sample[0] = checkColor(r);
                        sample[1] = checkColor(g);
                        sample[2] = checkColor(b);
                        raster.setPixel(x, y, sample);
                    } else if (x == tmp.getWidth() - 1) {
                        int r = ((raster.getPixel(x, y, (int[]) null)[0])
                                + (raster.getPixel(x, y - 1, (int[]) null)[0])
                                + (raster.getPixel(x, y + 1, (int[]) null)[0])
                                + (raster.getPixel(x - 1, y, (int[]) null)[0] * 2)
                                + (raster.getPixel(x - 1, y + 1, (int[]) null)[0] * 2)
                                + (raster.getPixel(x - 1, y - 1, (int[]) null)[0] * 2))
                                / 9;
                        int g = ((raster.getPixel(x, y, (int[]) null)[1])
                                + (raster.getPixel(x, y - 1, (int[]) null)[1])
                                + (raster.getPixel(x, y + 1, (int[]) null)[1])
                                + (raster.getPixel(x - 1, y, (int[]) null)[1] * 2)
                                + (raster.getPixel(x - 1, y + 1, (int[]) null)[1] * 2)
                                + (raster.getPixel(x - 1, y - 1, (int[]) null)[1] * 2))
                                / 9;
                        int b = ((raster.getPixel(x, y, (int[]) null)[2])
                                + (raster.getPixel(x, y - 1, (int[]) null)[2])
                                + (raster.getPixel(x, y + 1, (int[]) null)[2])
                                + (raster.getPixel(x - 1, y, (int[]) null)[2] * 2)
                                + (raster.getPixel(x - 1, y + 1, (int[]) null)[2] * 2)
                                + (raster.getPixel(x - 1, y - 1, (int[]) null)[2] * 2))
                                / 9;
                        sample[0] = checkColor(r);
                        sample[1] = checkColor(g);
                        sample[2] = checkColor(b);
                        raster.setPixel(x, y, sample);
                    } else {
                        int r = ((raster.getPixel(x, y, (int[]) null)[0])
                                + (raster.getPixel(x, y - 1, (int[]) null)[0])
                                + (raster.getPixel(x, y + 1, (int[]) null)[0])
                                + (raster.getPixel(x - 1, y, (int[]) null)[0])
                                + (raster.getPixel(x + 1, y, (int[]) null)[0])
                                + (raster.getPixel(x + 1, y + 1, (int[]) null)[0])
                                + (raster.getPixel(x + 1, y - 1, (int[]) null)[0])
                                + (raster.getPixel(x - 1, y - 1, (int[]) null)[0])
                                + (raster.getPixel(x - 1, y + 1, (int[]) null)[0]))
                                / 9;
                        int g = ((raster.getPixel(x, y, (int[]) null)[1])
                                + (raster.getPixel(x, y - 1, (int[]) null)[1])
                                + (raster.getPixel(x, y + 1, (int[]) null)[1])
                                + (raster.getPixel(x - 1, y, (int[]) null)[1])
                                + (raster.getPixel(x + 1, y, (int[]) null)[1])
                                + (raster.getPixel(x + 1, y + 1, (int[]) null)[1])
                                + (raster.getPixel(x + 1, y - 1, (int[]) null)[1])
                                + (raster.getPixel(x - 1, y - 1, (int[]) null)[1])
                                + (raster.getPixel(x - 1, y + 1, (int[]) null)[1]))
                                / 9;
                        int b = ((raster.getPixel(x, y, (int[]) null)[2])
                                + (raster.getPixel(x, y - 1, (int[]) null)[2])
                                + (raster.getPixel(x, y + 1, (int[]) null)[2])
                                + (raster.getPixel(x - 1, y, (int[]) null)[2])
                                + (raster.getPixel(x + 1, y, (int[]) null)[2])
                                + (raster.getPixel(x + 1, y + 1, (int[]) null)[2])
                                + (raster.getPixel(x + 1, y - 1, (int[]) null)[2])
                                + (raster.getPixel(x - 1, y - 1, (int[]) null)[2])
                                + (raster.getPixel(x - 1, y + 1, (int[]) null)[2]))
                                / 9;
                        sample[0] = checkColor(r);
                        sample[1] = checkColor(g);
                        sample[2] = checkColor(b);
                        raster.setPixel(x, y, sample);
                    }
                }
            }
            tmp.getRaster().setPixels(0, 0, tmp.getWidth(), tmp.getHeight(), raster.getPixels(0, 0, tmp.getWidth(), tmp.getHeight(), (int[]) null));
        }
        System.out.println(Integer.sum(5, 5));
        System.out.println(Arrays.toString(raster.getPixels(0, 0, tmp.getWidth(), tmp.getHeight(), (int[]) null)));
        return tmp;
    }

}
