package com.company;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        //compulsory();
        optional();
    }

    public static void compulsory() {
        Document myText = new Document("file1.txt");
        Document myImage = new Document("file2.png");
        Document myExe = new Document("file3.exe");

        Catalog catalog1 = new Catalog();
        catalog1.addDocument(myText);
        catalog1.addDocument(myImage);
        catalog1.addDocument(myExe);

        System.out.println("Catalog1: ");
        for (var document : catalog1.getDocuments()){
            System.out.println(document);
        }
        catalog1.save("catalog.ser");

        Catalog catalog2 = new Catalog();
        catalog2.load("catalog.ser");

        System.out.println("Catalog2: ");
        for (var document : catalog2.getDocuments()){
            System.out.println(document);
        }

        catalog1.viewDocument(0);

        catalog1.savePlain("catalog.plain");

        Catalog catalog3 = new Catalog();
        catalog3.loadPlain("catalog.plain");


        System.out.println("Catalog3: ");
        for (var document : catalog3.getDocuments()){
            System.out.println(document);
        }
    }

    public static void optional() {
        Shell shell = new Shell();

        while(true) {
            int result = shell.readCommand();

            if (result == -1){
                return;
            }
            if (result == 0){
                System.out.println("Unrecognized Command");
            }
            if (result == 1){
                shell.executeLastCommand();
            }
        }
    }
}