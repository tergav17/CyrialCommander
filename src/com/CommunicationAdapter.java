package com;

import main.Commander;

public abstract class CommunicationAdapter {

    private boolean isActive;

    protected Commander cmd;

    public CommunicationAdapter(Commander cmd) {
        this.cmd = cmd;

        cmd.getConsole().println("[COM] Obtaining connection...");

        if (makeConnection()) {
            cmd.getConsole().println("[COM] Connection established, awaiting INIT");

            isActive = true;
        } else {
            isActive = false;

            cmd.getConsole().println("[COM] Cannot establish connection!");
            return;
        }
    }

    public void startLineHandler() {
        while (isActive) {
            cmd.issueCommand(readCommand());
        }

        cmd.getConsole().println("[COM] Connection handler terminated!");
    }

    public abstract boolean makeConnection();

    public abstract String readCommand();
    public abstract void sendCommand(String in);
    public abstract  void reset();
}
