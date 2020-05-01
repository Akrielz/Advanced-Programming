package com.company;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            TCPServer tcpServer = new TCPServer();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
