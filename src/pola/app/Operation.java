/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pola.app;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
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
        int[] sample = new int[3];
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                if (y == 0) {
                    if (x == 0) {
                        int r = ((tmp.getRaster().getPixel(x, y, (int[]) null)[0])
                                + (tmp.getRaster().getPixel(x + 1, y, (int[]) null)[0] * 2)
                                + (tmp.getRaster().getPixel(x, y + 1, (int[]) null)[0] * 2)
                                + (tmp.getRaster().getPixel(x + 1, y + 1, (int[]) null)[0] * 4)) / 9;
                        int g = ((tmp.getRaster().getPixel(x, y, (int[]) null)[1])
                                + (tmp.getRaster().getPixel(x + 1, y, (int[]) null)[1] * 2)
                                + (tmp.getRaster().getPixel(x, y + 1, (int[]) null)[1] * 2)
                                + (tmp.getRaster().getPixel(x + 1, y + 1, (int[]) null)[1] * 4)) / 9;
                        int b = ((tmp.getRaster().getPixel(x, y, (int[]) null)[2])
                                + (tmp.getRaster().getPixel(x + 1, y, (int[]) null)[2] * 2)
                                + (tmp.getRaster().getPixel(x, y + 1, (int[]) null)[2] * 2)
                                + (tmp.getRaster().getPixel(x + 1, y + 1, (int[]) null)[2] * 4)) / 9;
                        sample[0] = checkColor(r);
                        sample[1] = checkColor(g);
                        sample[2] = checkColor(b);
                        tmp.getRaster().setPixel(x, y, sample);
                    } else if (x == tmp.getWidth() - 1) {
                        int r = ((tmp.getRaster().getPixel(x, y, (int[]) null)[0])
                                + (tmp.getRaster().getPixel(x - 1, y, (int[]) null)[0] * 2)
                                + (tmp.getRaster().getPixel(x, y + 1, (int[]) null)[0] * 2)
                                + (tmp.getRaster().getPixel(x - 1, y + 1, (int[]) null)[0] * 4)) / 9;
                        int g = ((tmp.getRaster().getPixel(x, y, (int[]) null)[1])
                                + (tmp.getRaster().getPixel(x - 1, y, (int[]) null)[1] * 2)
                                + (tmp.getRaster().getPixel(x, y + 1, (int[]) null)[1] * 2)
                                + (tmp.getRaster().getPixel(x - 1, y + 1, (int[]) null)[1] * 4)) / 9;
                        int b = ((tmp.getRaster().getPixel(x, y, (int[]) null)[2])
                                + (tmp.getRaster().getPixel(x - 1, y, (int[]) null)[2] * 2)
                                + (tmp.getRaster().getPixel(x, y + 1, (int[]) null)[2] * 2)
                                + (tmp.getRaster().getPixel(x - 1, y + 1, (int[]) null)[2] * 4)) / 9;
                        sample[0] = checkColor(r);
                        sample[1] = checkColor(g);
                        sample[2] = checkColor(b);
                        tmp.getRaster().setPixel(x, y, sample);
                    } else {
                        int r = ((tmp.getRaster().getPixel(x, y, (int[]) null)[0])
                                + (tmp.getRaster().getPixel(x - 1, y, (int[]) null)[0])
                                + (tmp.getRaster().getPixel(x + 1, y, (int[]) null)[0])
                                + (tmp.getRaster().getPixel(x - 1, y + 1, (int[]) null)[0] * 2)
                                + (tmp.getRaster().getPixel(x + 1, y + 1, (int[]) null)[0] * 2)
                                + (tmp.getRaster().getPixel(x, y + 1, (int[]) null)[0] * 2))
                                / 9;
                        int g = ((tmp.getRaster().getPixel(x, y, (int[]) null)[1])
                                + (tmp.getRaster().getPixel(x - 1, y, (int[]) null)[1])
                                + (tmp.getRaster().getPixel(x + 1, y, (int[]) null)[1])
                                + (tmp.getRaster().getPixel(x - 1, y + 1, (int[]) null)[1] * 2)
                                + (tmp.getRaster().getPixel(x + 1, y + 1, (int[]) null)[1] * 2)
                                + (tmp.getRaster().getPixel(x, y + 1, (int[]) null)[1] * 2))
                                / 9;
                        int b = ((tmp.getRaster().getPixel(x, y, (int[]) null)[2])
                                + (tmp.getRaster().getPixel(x - 1, y, (int[]) null)[2])
                                + (tmp.getRaster().getPixel(x + 1, y, (int[]) null)[2])
                                + (tmp.getRaster().getPixel(x - 1, y + 1, (int[]) null)[2] * 2)
                                + (tmp.getRaster().getPixel(x + 1, y + 1, (int[]) null)[2] * 2)
                                + (tmp.getRaster().getPixel(x, y + 1, (int[]) null)[2] * 2))
                                / 9;
                        sample[0] = checkColor(r);
                        sample[1] = checkColor(g);
                        sample[2] = checkColor(b);
                        tmp.getRaster().setPixel(x, y, sample);
                    }
                } else if (y == tmp.getHeight() - 1) {
                    if (x == 0) {
                        int r = ((tmp.getRaster().getPixel(x, y, (int[]) null)[0])
                                + (tmp.getRaster().getPixel(x + 1, y, (int[]) null)[0] * 2)
                                + (tmp.getRaster().getPixel(x, y - 1, (int[]) null)[0] * 2)
                                + (tmp.getRaster().getPixel(x + 1, y - 1, (int[]) null)[0] * 4)) / 9;
                        int g = ((tmp.getRaster().getPixel(x, y, (int[]) null)[1])
                                + (tmp.getRaster().getPixel(x + 1, y, (int[]) null)[1] * 2)
                                + (tmp.getRaster().getPixel(x, y - 1, (int[]) null)[1] * 2)
                                + (tmp.getRaster().getPixel(x + 1, y - 1, (int[]) null)[1] * 4)) / 9;
                        int b = ((tmp.getRaster().getPixel(x, y, (int[]) null)[2])
                                + (tmp.getRaster().getPixel(x + 1, y, (int[]) null)[2] * 2)
                                + (tmp.getRaster().getPixel(x, y - 1, (int[]) null)[2] * 2)
                                + (tmp.getRaster().getPixel(x + 1, y - 1, (int[]) null)[2] * 4)) / 9;
                        sample[0] = checkColor(r);
                        sample[1] = checkColor(g);
                        sample[2] = checkColor(b);
                        tmp.getRaster().setPixel(x, y, sample);
                    } else if (x == tmp.getWidth() - 1) {
                        int r = ((tmp.getRaster().getPixel(x, y, (int[]) null)[0])
                                + (tmp.getRaster().getPixel(x - 1, y, (int[]) null)[0] * 2)
                                + (tmp.getRaster().getPixel(x, y - 1, (int[]) null)[0] * 2)
                                + (tmp.getRaster().getPixel(x - 1, y - 1, (int[]) null)[0] * 4)) / 9;
                        int g = ((tmp.getRaster().getPixel(x, y, (int[]) null)[1])
                                + (tmp.getRaster().getPixel(x - 1, y, (int[]) null)[1] * 2)
                                + (tmp.getRaster().getPixel(x, y - 1, (int[]) null)[1] * 2)
                                + (tmp.getRaster().getPixel(x - 1, y - 1, (int[]) null)[1] * 4)) / 9;
                        int b = ((tmp.getRaster().getPixel(x, y, (int[]) null)[2])
                                + (tmp.getRaster().getPixel(x - 1, y, (int[]) null)[2] * 2)
                                + (tmp.getRaster().getPixel(x, y - 1, (int[]) null)[2] * 2)
                                + (tmp.getRaster().getPixel(x - 1, y - 1, (int[]) null)[2] * 4)) / 9;
                        sample[0] = checkColor(r);
                        sample[1] = checkColor(g);
                        sample[2] = checkColor(b);
                        tmp.getRaster().setPixel(x, y, sample);
                    } else {
                        int r = ((tmp.getRaster().getPixel(x, y, (int[]) null)[0])
                                + (tmp.getRaster().getPixel(x - 1, y, (int[]) null)[0])
                                + (tmp.getRaster().getPixel(x + 1, y, (int[]) null)[0])
                                + (tmp.getRaster().getPixel(x - 1, y - 1, (int[]) null)[0] * 2)
                                + (tmp.getRaster().getPixel(x + 1, y - 1, (int[]) null)[0] * 2)
                                + (tmp.getRaster().getPixel(x, y - 1, (int[]) null)[0] * 2))
                                / 9;
                        int g = ((tmp.getRaster().getPixel(x, y, (int[]) null)[1])
                                + (tmp.getRaster().getPixel(x - 1, y, (int[]) null)[1])
                                + (tmp.getRaster().getPixel(x + 1, y, (int[]) null)[1])
                                + (tmp.getRaster().getPixel(x - 1, y - 1, (int[]) null)[1] * 2)
                                + (tmp.getRaster().getPixel(x + 1, y - 1, (int[]) null)[1] * 2)
                                + (tmp.getRaster().getPixel(x, y - 1, (int[]) null)[1] * 2))
                                / 9;
                        int b = ((tmp.getRaster().getPixel(x, y, (int[]) null)[2])
                                + (tmp.getRaster().getPixel(x - 1, y, (int[]) null)[2])
                                + (tmp.getRaster().getPixel(x + 1, y, (int[]) null)[2])
                                + (tmp.getRaster().getPixel(x - 1, y - 1, (int[]) null)[2] * 2)
                                + (tmp.getRaster().getPixel(x + 1, y - 1, (int[]) null)[2] * 2)
                                + (tmp.getRaster().getPixel(x, y - 1, (int[]) null)[2] * 2))
                                / 9;
                        sample[0] = checkColor(r);
                        sample[1] = checkColor(g);
                        sample[2] = checkColor(b);
                        tmp.getRaster().setPixel(x, y, sample);
                    }
                } else {
                    if (x == 0) {
                        int r = ((tmp.getRaster().getPixel(x, y, (int[]) null)[0])
                                + (tmp.getRaster().getPixel(x, y - 1, (int[]) null)[0])
                                + (tmp.getRaster().getPixel(x, y + 1, (int[]) null)[0])
                                + (tmp.getRaster().getPixel(x + 1, y, (int[]) null)[0] * 2)
                                + (tmp.getRaster().getPixel(x + 1, y + 1, (int[]) null)[0] * 2)
                                + (tmp.getRaster().getPixel(x + 1, y - 1, (int[]) null)[0] * 2))
                                / 9;
                        int g = ((tmp.getRaster().getPixel(x, y, (int[]) null)[1])
                                + (tmp.getRaster().getPixel(x, y - 1, (int[]) null)[1])
                                + (tmp.getRaster().getPixel(x, y + 1, (int[]) null)[1])
                                + (tmp.getRaster().getPixel(x + 1, y, (int[]) null)[1] * 2)
                                + (tmp.getRaster().getPixel(x + 1, y + 1, (int[]) null)[1] * 2)
                                + (tmp.getRaster().getPixel(x + 1, y - 1, (int[]) null)[1] * 2))
                                / 9;
                        int b = ((tmp.getRaster().getPixel(x, y, (int[]) null)[2])
                                + (tmp.getRaster().getPixel(x, y - 1, (int[]) null)[2])
                                + (tmp.getRaster().getPixel(x, y + 1, (int[]) null)[2])
                                + (tmp.getRaster().getPixel(x + 1, y, (int[]) null)[2] * 2)
                                + (tmp.getRaster().getPixel(x + 1, y + 1, (int[]) null)[2] * 2)
                                + (tmp.getRaster().getPixel(x + 1, y - 1, (int[]) null)[2] * 2))
                                / 9;
                        sample[0] = checkColor(r);
                        sample[1] = checkColor(g);
                        sample[2] = checkColor(b);
                        tmp.getRaster().setPixel(x, y, sample);
                    } else if (x == tmp.getWidth() - 1) {
                        int r = ((tmp.getRaster().getPixel(x, y, (int[]) null)[0])
                                + (tmp.getRaster().getPixel(x, y - 1, (int[]) null)[0])
                                + (tmp.getRaster().getPixel(x, y + 1, (int[]) null)[0])
                                + (tmp.getRaster().getPixel(x - 1, y, (int[]) null)[0] * 2)
                                + (tmp.getRaster().getPixel(x - 1, y + 1, (int[]) null)[0] * 2)
                                + (tmp.getRaster().getPixel(x - 1, y - 1, (int[]) null)[0] * 2))
                                / 9;
                        int g = ((tmp.getRaster().getPixel(x, y, (int[]) null)[1])
                                + (tmp.getRaster().getPixel(x, y - 1, (int[]) null)[1])
                                + (tmp.getRaster().getPixel(x, y + 1, (int[]) null)[1])
                                + (tmp.getRaster().getPixel(x - 1, y, (int[]) null)[1] * 2)
                                + (tmp.getRaster().getPixel(x - 1, y + 1, (int[]) null)[1] * 2)
                                + (tmp.getRaster().getPixel(x - 1, y - 1, (int[]) null)[1] * 2))
                                / 9;
                        int b = ((tmp.getRaster().getPixel(x, y, (int[]) null)[2])
                                + (tmp.getRaster().getPixel(x, y - 1, (int[]) null)[2])
                                + (tmp.getRaster().getPixel(x, y + 1, (int[]) null)[2])
                                + (tmp.getRaster().getPixel(x - 1, y, (int[]) null)[2] * 2)
                                + (tmp.getRaster().getPixel(x - 1, y + 1, (int[]) null)[2] * 2)
                                + (tmp.getRaster().getPixel(x - 1, y - 1, (int[]) null)[2] * 2))
                                / 9;
                        sample[0] = checkColor(r);
                        sample[1] = checkColor(g);
                        sample[2] = checkColor(b);
                        tmp.getRaster().setPixel(x, y, sample);
                    } else {
                        int r = ((tmp.getRaster().getPixel(x, y, (int[]) null)[0])
                                + (tmp.getRaster().getPixel(x, y - 1, (int[]) null)[0])
                                + (tmp.getRaster().getPixel(x, y + 1, (int[]) null)[0])
                                + (tmp.getRaster().getPixel(x - 1, y, (int[]) null)[0])
                                + (tmp.getRaster().getPixel(x + 1, y, (int[]) null)[0])
                                + (tmp.getRaster().getPixel(x + 1, y + 1, (int[]) null)[0])
                                + (tmp.getRaster().getPixel(x + 1, y - 1, (int[]) null)[0])
                                + (tmp.getRaster().getPixel(x - 1, y - 1, (int[]) null)[0])
                                + (tmp.getRaster().getPixel(x - 1, y + 1, (int[]) null)[0]))
                                / 9;
                        int g = ((tmp.getRaster().getPixel(x, y, (int[]) null)[1])
                                + (tmp.getRaster().getPixel(x, y - 1, (int[]) null)[1])
                                + (tmp.getRaster().getPixel(x, y + 1, (int[]) null)[1])
                                + (tmp.getRaster().getPixel(x - 1, y, (int[]) null)[1])
                                + (tmp.getRaster().getPixel(x + 1, y, (int[]) null)[1])
                                + (tmp.getRaster().getPixel(x + 1, y + 1, (int[]) null)[1])
                                + (tmp.getRaster().getPixel(x + 1, y - 1, (int[]) null)[1])
                                + (tmp.getRaster().getPixel(x - 1, y - 1, (int[]) null)[1])
                                + (tmp.getRaster().getPixel(x - 1, y + 1, (int[]) null)[1]))
                                / 9;
                        int b = ((tmp.getRaster().getPixel(x, y, (int[]) null)[2])
                                + (tmp.getRaster().getPixel(x, y - 1, (int[]) null)[2])
                                + (tmp.getRaster().getPixel(x, y + 1, (int[]) null)[2])
                                + (tmp.getRaster().getPixel(x - 1, y, (int[]) null)[2])
                                + (tmp.getRaster().getPixel(x + 1, y, (int[]) null)[2])
                                + (tmp.getRaster().getPixel(x + 1, y + 1, (int[]) null)[2])
                                + (tmp.getRaster().getPixel(x + 1, y - 1, (int[]) null)[2])
                                + (tmp.getRaster().getPixel(x - 1, y - 1, (int[]) null)[2])
                                + (tmp.getRaster().getPixel(x - 1, y + 1, (int[]) null)[2]))
                                / 9;
                        sample[0] = checkColor(r);
                        sample[1] = checkColor(g);
                        sample[2] = checkColor(b);
                        tmp.getRaster().setPixel(x, y, sample);
                    }
                }
            }
            tmp.getRaster().setPixels(0, 0, tmp.getWidth(), tmp.getHeight(), tmp.getRaster().getPixels(0, 0, tmp.getWidth(), tmp.getHeight(), (int[]) null));
        }
        return tmp;
    }

    public BufferedImage konvolusi(BufferedImage image, String action) {
        BufferedImage tmp = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        tmp.getGraphics().drawImage(image, 0, 0, null);
        int[] rasterData = tmp.getRaster().getPixels(0, 0, tmp.getWidth(), tmp.getHeight(), (int[]) null);
        int a[];
        int b[] = new int[9];
        int result, resultMax = 0, resultMin = 0;
        for (int y = 0; y < tmp.getHeight(); y++) {
            for (int x = 0; x < tmp.getWidth(); x++) {
                if (y == 0) {
                    if (x == 0) {
                        a = tmp.getRaster().getPixels(x, y, 2, 2, (int[]) null);
                        b[0] = a[3];
                        b[1] = a[2];
                        b[2] = a[3];
                        b[3] = a[1];
                        b[4] = a[0];
                        b[5] = a[1];
                        b[6] = a[3];
                        b[7] = a[2];
                        b[8] = a[3];
                    } else if (x == tmp.getWidth() - 1) {
                        a = tmp.getRaster().getPixels(x - 1, y, 2, 2, (int[]) null);
                        b[0] = a[2];
                        b[1] = a[3];
                        b[2] = a[2];
                        b[3] = a[0];
                        b[4] = a[1];
                        b[5] = a[0];
                        b[6] = a[2];
                        b[7] = a[3];
                        b[8] = a[2];
                    } else {
                        a = tmp.getRaster().getPixels(x - 1, y, 3, 2, (int[]) null);
                        b[0] = a[3];
                        b[1] = a[4];
                        b[2] = a[5];
                        b[3] = a[0];
                        b[4] = a[1];
                        b[5] = a[2];
                        b[6] = a[3];
                        b[7] = a[4];
                        b[8] = a[5];
                    }
                } else if (y == tmp.getHeight() - 1) {
                    if (x == 0) {
                        a = tmp.getRaster().getPixels(x, y - 1, 2, 2, (int[]) null);
                        b[0] = a[2];
                        b[1] = a[3];
                        b[2] = a[2];
                        b[3] = a[0];
                        b[4] = a[1];
                        b[5] = a[0];
                        b[6] = a[2];
                        b[7] = a[3];
                        b[8] = a[2];
                    } else if (x == tmp.getWidth() - 1) {
                        a = tmp.getRaster().getPixels(x - 1, y - 1, 2, 2, (int[]) null);
                        b[0] = a[3];
                        b[1] = a[2];
                        b[2] = a[3];
                        b[3] = a[1];
                        b[4] = a[0];
                        b[5] = a[1];
                        b[6] = a[3];
                        b[7] = a[2];
                        b[8] = a[3];
                    } else {
                        a = tmp.getRaster().getPixels(x - 1, y - 1, 3, 2, (int[]) null);
                        b[0] = a[0];
                        b[1] = a[1];
                        b[2] = a[2];
                        b[3] = a[3];
                        b[4] = a[4];
                        b[5] = a[5];
                        b[6] = a[1];
                        b[7] = a[2];
                        b[8] = a[3];
                    }
                } else {
                    if (x == 0) {
                        a = tmp.getRaster().getPixels(x, y - 1, 2, 3, (int[]) null);
                        b[0] = a[1];
                        b[1] = a[2];
                        b[2] = a[1];
                        b[3] = a[3];
                        b[4] = a[2];
                        b[5] = a[3];
                        b[6] = a[5];
                        b[7] = a[4];
                        b[8] = a[5];
                    } else if (x == tmp.getWidth() - 1) {
                        a = tmp.getRaster().getPixels(x - 1, y - 1, 2, 3, (int[]) null);
                        b[0] = a[0];
                        b[1] = a[1];
                        b[2] = a[0];
                        b[3] = a[2];
                        b[4] = a[3];
                        b[5] = a[2];
                        b[6] = a[4];
                        b[7] = a[5];
                        b[8] = a[4];
                    } else {
                        b = tmp.getRaster().getPixels(x - 1, y - 1, 3, 3, (int[]) null);
                    }
                }
                switch (action) {
                    case "homogen8":
                        tmp.getRaster().setPixel(x, y, new int[]{calculateHomogen8(b)});
                        break;
                    case "sobel":
                        result = calculateSobel(b);
                        if (result > resultMax) {
                            resultMax = result;
                        }
                        if (result < resultMin) {
                            resultMin = result;
                        }
                        rasterData[((tmp.getWidth() * y) + x)] = calculateSobel(b);
                        break;
                }
            }
        }
        if (action.equals("sobel")) {
            rasterData = scalePixelValue(rasterData, resultMin, resultMax);
            tmp.getRaster().setPixels(0, 0, tmp.getWidth(), tmp.getHeight(), rasterData);
        }
        return tmp;
    }

    public int calculateHomogen8(int[] a) {
        int c = 0;
        for (int i = 0; i < a.length; i++) {
            int b = a[i] - a[4];
            if (Math.abs(b) > 0) {
                c = Math.abs(b);
            }
        }
        return Math.abs(c);
    }

    public int calculateSobel(int[] a) {
        int x = Math.abs((a[0] * -1) + (a[1] * 0) + (a[2] * 1) + (a[3] * -2) + (a[4] * 0) + (a[5] * 2) + (a[6] * -1) + (a[7] * 0) + (a[8] * -1));
        int y = Math.abs((a[0] * -1) + (a[1] * -2) + (a[2] * -1) + (a[3] * 0) + (a[4] * 0) + (a[5] * 0) + (a[6] * 1) + (a[7] * 2) + (a[8] * 1));
        return (x + y);
    }

    public int[] scalePixelValue(int[] data, int min, int max) {
        for (int i = 0; i < data.length; i++) {
            data[i] = (255 * ((data[i]) - (min))) / ((max) - (min));
        }
        return data;
    }
}
