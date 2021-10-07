package panel;

import dash.*;
import main.Commander;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class PanelCommandHandler {

    public static void handleUpdateCommand(String[] args, Commander cmd) {
        HashMap<String, Object> el = cmd.getElements();

        if (args.length < 2) return;

        if (el.containsKey(args[1])) {
            Object e = el.get(args[1]);

            if (e instanceof JBTextLabel) {
                if (args.length < 3) return;

                ((JBTextLabel) e).setContent(args[2]);
            } else if (e instanceof JBStatusIcon) {
                if (args.length < 3) return;

                ((JBStatusIcon) e).setState(toState(args[2]));
            } else if (e instanceof JBNumericLogger) {
                if (args.length < 4) return;

                JBNumericLogger log = (JBNumericLogger) e;

                if (args[2].equals("B")) {
                    if (args.length < 5) return;

                    log.getGraph().setYMin(toDouble(args[3]));
                    log.getGraph().setYMax(toDouble(args[4])) ;
                } else log.addTracker(toDouble(args[2]), toColor(args[3]));

            } else if (e instanceof JBPlotter) {
                if (args.length < 3) return;

                JBPlotter plot = (JBPlotter) e;

                if (args[2].equals("E")) {
                    plot.clearPlot();
                    plot.update();
                } else if (args[2].equals("L")) {
                    plot.setLinear(true);
                } else if (args[2].equals("N")) {
                    plot.setLinear(false);
                } else if (args[2].equals("C")) {
                    if (args.length < 4) return;

                    plot.setCurrentColor(toColor(args[3]));
                } else if (args[2].equals("B")) {
                    if (args.length < 7) return;

                    plot.setXMin(toDouble(args[3]));
                    plot.setXMax(toDouble(args[4]));
                    plot.setYMin(toDouble(args[5]));
                    plot.setYMax(toDouble(args[6]));

                } else {
                    if (args.length < 4) return;
                    plot.plot(toDouble(args[2]), toDouble(args[3]));
                }

            }
        }

    }

    public static void handleAdditionCommand(String[] args, Commander cmd) {
        HashMap<String, Object> el = cmd.getElements();

        if (args.length < 2) return;

        if (args[1].equals("T")) {
            // Add label to dashboard
            if (args.length < 8) return;

            boolean centered = false;
            boolean boxed = false;

            if (args.length > 9) {
                centered = !args[8].equals("0");
                boxed = !args[9].equals("0");
            }

            JBTextLabel label = new JBTextLabel(args[3], toInt(args[4]), toInt(args[5]),toInt(args[6]), toInt(args[7]), centered);
            label.setBoxed(boxed);

            el.put(args[2], label);
            cmd.getControl().add(label);
        } else if (args[1].equals("D")) {
            // Adds divider to dashboard
            if (args.length < 9) return;

            JBLineDivider div = new JBLineDivider(toInt(args[3]), toInt(args[4]), toInt(args[5]), toInt(args[6]), toInt(args[7]), toInt(args[8]));

            el.put(args[2], div);
            cmd.getControl().add(div);
        } else if (args[1].equals("S")) {
            // Adds status indicator to dashboard
            if (args.length < 6) return;

            JBStatusIcon icon = new JBStatusIcon(toState(args[3]), toInt(args[4]), toInt(args[5]));

            el.put(args[2], icon);
            cmd.getControl().add(icon);
        } else if (args[1].equals("B")) {
            // Adds button to dashboard
            if (args.length < 8) return;

            JBTextButton button = new JBTextButton(new ActionListener() {
                String buttonID = args[2];

                @Override
                public void actionPerformed(ActionEvent e) {
                    cmd.getButtonStates().replace(buttonID, true);
                }

            }, args[3], toInt(args[4]), toInt(args[5]), toInt(args[6]), toInt(args[7]));

            el.put(args[2], button);
            cmd.getControl().add(button);

            cmd.getButtonStates().put(args[2], false);
        } else if (args[1].equals("I")) {
            // Adds text input to dashboard
            if (args.length < 8) return;

            JBTextInput input = new JBTextInput(null, args[3], toInt(args[4]), toInt(args[5]), toInt(args[6]), toInt(args[7]));

            el.put(args[2], input);
            cmd.getControl().add(input);
        } else if (args[1].equals("L")) {
            // Adds logger to dashboard
            if (args.length < 10) return;

            JBNumericLogger log = new JBNumericLogger(args[3], toInt(args[4]), toInt(args[5]), toInt(args[6]), toInt(args[7]), args[8], toDouble(args[9]));

            el.put(args[2], log);
            cmd.getControl().add(log);

        } else if (args[1].equals("P")) {
            // Adds plot to dashboard
            if (args.length < 10) return;

            JBPlotter plot = new JBPlotter(args[3], toInt(args[4]), toInt(args[5]), toInt(args[6]), toInt(args[7]), args[8], args[9]);
            plot.setLinear(false);
            plot.setCurrentColor(Color.LIGHT_GRAY);

            el.put(args[2], plot);
            cmd.getControl().add(plot);
        }
    }

    private static int toInt(String in) {
        try {
            return Integer.valueOf(in);
        } catch (Exception e) {
            return 0;
        }
    }

    private static double toDouble(String in) {
        try {
            return Double.valueOf(in);
        } catch (Exception e) {
            return 0;
        }
    }

    private static JBStatusIcon.State toState(String in) {
        if (in.startsWith("I")) return JBStatusIcon.State.INACTIVE;
        if (in.startsWith("NOM")) return JBStatusIcon.State.NOMINAL;
        if (in.startsWith("NOT")) return JBStatusIcon.State.NOTICE;
        if (in.startsWith("W")) return JBStatusIcon.State.WARNING;
        return JBStatusIcon.State.ERROR;
    }

    private static Color toColor(String in) {
        if (in.startsWith("BLA")) return Color.BLACK;
        if (in.startsWith("BLU")) return Color.BLUE;
        if (in.startsWith("GRA")) return Color.GRAY;
        if (in.startsWith("GRE")) return Color.GREEN;
        if (in.startsWith("DA")) return Color.DARK_GRAY;
        if (in.startsWith("LI")) return Color.LIGHT_GRAY;
        if (in.startsWith("YE")) return Color.YELLOW;
        if (in.startsWith("MA")) return Color.MAGENTA;
        if (in.startsWith("RE")) return Color.RED;
        if (in.startsWith("CY")) return Color.CYAN;
        if (in.startsWith("PI")) return Color.PINK;
        if (in.startsWith("OR")) return Color.ORANGE;

        return Color.WHITE;
    }
}
