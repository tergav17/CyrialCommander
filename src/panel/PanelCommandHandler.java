package panel;

import dash.JBLineDivider;
import dash.JBStatusIcon;
import dash.JBTextLabel;
import main.Commander;

import java.util.HashMap;

public class PanelCommandHandler {

    public static void handleAdditionCommand(String[] args, Commander cmd) {
        HashMap<String, Object> el = cmd.getElements();

        if (args.length < 2) return;

        if (args[1].equals("L")) {
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
        }
    }

    private static int toInt(String in) {
        try {
            return Integer.valueOf(in);
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
}
