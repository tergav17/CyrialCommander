package com;

import main.Commander;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class NetworkCom extends CommunicationAdapter{


    private String hostname;
    private int port;

    private Socket sock;
    private BufferedReader sockIn;
    private PrintWriter sockOut;

    public NetworkCom(Commander cmd) {
        super(cmd);

    }

    @Override
    public boolean makeConnection() {
        hostname = "192.168.1.1";
        port = 288;

        cmd.getConsole().println("[COM] Attempting connection to " + hostname + " on port " + port + "...");

        try {
            sock = new Socket("192.168.1.1", 288);
            sockIn = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            sockOut = new PrintWriter(sock.getOutputStream(), true);
        } catch (IOException e) {
            cmd.getConsole().println("[COM] General IO failure, cannot connect!");
            return false;
        }

        return true;
    }

    @Override
    public String readCommand() {
        while (true) {
            try {
                cmd.issueCommand(sockIn.readLine());
            } catch (IOException e) {
                cmd.getConsole().println("Failed read!");
            }
        }

        //return null;
    }

    @Override
    public void sendCommand(String in) {
        sockOut.println(in);
    }

    @Override
    public void reset() {
        sockOut.flush();
    }
}
