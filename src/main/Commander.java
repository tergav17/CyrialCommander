package main;

import com.CommunicationAdapter;
import com.TerminalCom;
import dash.Dashboard;
import dash.JBTextButton;
import dash.JBTextInput;
import panel.PanelCommandHandler;

import java.util.ArrayList;
import java.util.HashMap;

public class Commander {

    private static final int PANEL_WIDTH_DEFAULT = 60;
    private static final int PANEL_HEIGHT_DEFAULT = 30;

    private Dashboard console;
    private Dashboard scanner;
    private Dashboard panel = null;

    private HashMap<String, Object> elements = new HashMap<>();
    private HashMap<String, Boolean> buttonStates = new HashMap<>();

    CommunicationAdapter com;

    public Commander() {
        console = new Dashboard(Dashboard.CONSOLE, "Terminal", 80, 24);
        console.dashboardExitOnClose();

        console.println("[SYS] CyRIAL Commander V1.0\n[SYS] Written by Gavin Tersteeg, 2021");

        com = new TerminalCom(this);
        com.startLineHandler();


    }

    public void issueCommand(String line) {
        if (line.length() == 0) return;

        String[] args = line.split(",");


        if (line.charAt(0) == 'I') {
            // INIT command, clear dashboards and prepare special structures
            console.println("[SYS] INIT Detected, Resetting...");


            if (panel == null) {
                console.println("[SYS] Creating Control Panel...");

                int width = PANEL_WIDTH_DEFAULT;
                int height = PANEL_HEIGHT_DEFAULT;

                if (args.length == 3) {
                    width = toInt(args[1]);
                    height = toInt(args[2]);

                    console.println("[SYS] Custom Panel Size = " + width + "x" + height);
                }

                panel = new Dashboard(Dashboard.GENERIC, "Control Panel", width, height);

            } else {
                console.println("[SYS] Cleaning Control Panel...");

                // Remove everything from dashboard, then clear element table
                for (Object e : elements.values()) {
                    panel.remove(e);
                }

                elements = new HashMap<>();
                buttonStates = new HashMap<>();
            }
        } else if (line.charAt(0) == 'P') {
            // PUTS command, puts a number of chars on the terminal
            if (line.length() == 1) return;

            String chars = line.substring(1);
            console.print(chars);

        } else if (line.charAt(0) == 'N') {
            // NEWLIN command, puts a '\n" char on the terminals
            console.print("\n");

        } else if (line.charAt(0) == 'G') {
            // GETS command, gets a string from the terminal
            com.sendCommand(console.scanString());

        } else if (line.charAt(0) == 'R') {
            // GETC command, returns the integer value of a char in terminal buffer, 0 if none
            com.sendCommand("" + (int) console.getChar());
        } else if (line.charAt(0) == 'C') {
            // PUTC command, puts a char from an integer value onto the terminal
            if (line.length() == 1) return;

            String number = line.substring(1);
            console.print("" + (char) toInt(number));
        } else if (line.charAt(0) == 'A') {
            // ADD command, adds an element to the dashboard
            if (panel == null) return;
            if (args.length < 3) return;

            if (elements.containsKey(args[2])) return;

            PanelCommandHandler.handleAdditionCommand(args, this);
        } else if (line.charAt(0) == 'D') {
            // DELETE command, remove an element from the dashboard

            if (args.length < 2) return;

            if (elements.containsKey(args[1])) {
                panel.remove(elements.get(args[1]));
                elements.remove(args[1]);
            }

            if (buttonStates.containsKey(args[1])) {
                buttonStates.remove(args[1]);
            }
        } else if (line.charAt(0) == 'E') {
            // EXAMINE command, returns a value from a specific element

            if (args.length < 2) return;

            if (elements.containsKey(args[1])) {
                Object el = elements.get(args[1]);

                if (el instanceof JBTextButton) {
                    if (buttonStates.containsKey(args[1]) && buttonStates.get(args[1])) {
                        com.sendCommand("1");
                        buttonStates.replace(args[1], false);
                    } else {
                        com.sendCommand("0");
                    }
                } else if (el instanceof JBTextInput) {
                    com.sendCommand(((JBTextInput) el).getContent());
                } else {
                    com.sendCommand("?");
                }
            } else {
                com.sendCommand("?");
            }
        } else if (line.charAt(0) == 'U') {
            // UPDATE command, updates an element on the dashboard
            if (panel == null) return;

            PanelCommandHandler.handleUpdateCommand(args, this);
        }
    }

    public Dashboard getConsole() {
        return console;
    }

    public void setConsole(Dashboard console) {
        this.console = console;
    }

    public Dashboard getScanner() {
        return scanner;
    }

    public void setScanner(Dashboard scanner) {
        this.scanner = scanner;
    }

    public Dashboard getControl() {
        return panel;
    }

    public void setPanel(Dashboard panel) {
        this.panel = panel;
    }

    public HashMap<String, Boolean> getButtonStates() {
        return buttonStates;
    }

    public void setButtonStates(HashMap<String, Boolean> buttonStates) {
        this.buttonStates = buttonStates;
    }

    private int toInt(String in) {
        try {
            return Integer.valueOf(in);
        } catch (Exception e) {
            return 0;
        }
    }

    public HashMap<String, Object> getElements() {
        return elements;
    }

    public void setElements(HashMap<String, Object> elements) {
        this.elements = elements;
    }
}
