package com;

import main.Commander;

import java.util.Scanner;

public class TerminalCom extends CommunicationAdapter{

    Scanner sc;

    public TerminalCom(Commander cmd) {
        super(cmd);
    }

    @Override
    public boolean makeConnection() {
        cmd.getConsole().println("[COM] Opening System.in...");
        sc = new Scanner(System.in);

        return true;
    }

    @Override
    public String readCommand() {
        System.out.print(">");
        return sc.nextLine();
    }

    @Override
    public void sendCommand(String in) {
        System.out.println("'" + in + "'");
    }
}
