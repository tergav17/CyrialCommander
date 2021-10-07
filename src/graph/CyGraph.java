package graph;

import java.awt.*;
import java.awt.image.BufferedImage;

public class CyGraph {

    private static final int WIDTH = 450;
    private static final int HEIGHT = 450;

    private static final int RADIUS = 200;

    /**
     * Graphs data taken from an int array into a # degree semicircle
     * @param data Data set
     * @param high Highest possible value
     * @param degrees # of degrees to graph
     */
    public static BufferedImage graph(int[] data, int high, int degrees) {
        int offset = (degrees > 180) ? 90 : 0;

        BufferedImage b = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

        Graphics2D g = b.createGraphics();

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        int midX = WIDTH / 2;
        int midY = HEIGHT / 2;

        g.setColor(Color.GREEN);
        g.drawLine(midX, 0, midX, HEIGHT);
        g.drawLine(0, midY, WIDTH, midY);

        g.setColor(Color.DARK_GRAY);

        int lastX = radX(RADIUS, offset) + midX;
        int lastY = radY(RADIUS, offset) + midY;

        for (int i = 0; i < degrees; i++) {
            int newX = radX(RADIUS, i+offset) + midX;
            int newY = radY(RADIUS, i+offset) + midY;

            g.drawLine(lastX, lastY, newX, newY);

            lastX = newX;
            lastY = newY;
        }

        g.setColor(Color.RED);

        lastX = radX((int) (RADIUS * bound(data[0], high)), offset) + midX;
        lastY = radY((int) (RADIUS * bound(data[0], high)), offset) + midY;

        for (int i = 0; i < degrees; i++) {

            int di = (int) ((i / (degrees * 1.0)) * data.length);
            if (di >= data.length) di = data.length - 1;

            int newX = radX((int) (RADIUS * bound(data[di], high)), i+offset) + midX;
            int newY = radY((int) (RADIUS * bound(data[di], high)), i+offset) + midY;

            g.drawLine(lastX, lastY, newX, newY);

            lastX = newX;
            lastY = newY;
        }

        return b;
    }

    /**
     *
     * @param in Input integer
     * @param high Highest possible value
     * @return A value between 0.0 to 1.0
     */
    public static double bound(int in, int high) {
        if (in < 0) in = 0;
        if (in > high) in = high;

        return (((double) in) / high);
    }

    public static int radX(int radius, int degree) {
        return (int) (Math.cos(Math.toRadians(degree)) * radius);
    }

    public static int radY(int radius, int degree) {
        return (int) -(Math.sin(Math.toRadians(degree)) * radius);
    }
}
