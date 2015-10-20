/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pola.app;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

/**
 *
 * @author Siroj Nur Ulum
 */
public class Operat {

    private class Point {

        public int x;
        public int y;

        public Point() {
        }

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public BufferedImage convertGray(BufferedImage buffOri) {
        BufferedImage tmp = new BufferedImage(buffOri.getColorModel(), buffOri.copyData(null), buffOri.isAlphaPremultiplied(), null);
        WritableRaster raster = tmp.getRaster();
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
}
