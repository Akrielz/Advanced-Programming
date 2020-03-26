package com.company;

import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Catalog implements Serializable {
    private List<Document> documents;

    public Catalog() {
        documents = new ArrayList<>();
    }

    public Catalog(List<Document> documents) {
        this.documents = documents;
    }


    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public void addDocument(Document document) {
        documents.add(document);
    }

    public void save(String path) {
        try{
            FileOutputStream file = new FileOutputStream(path);
            ObjectOutputStream out = new ObjectOutputStream(file);

            out.writeObject(this);

            out.flush();
            file.close();
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void savePlain(String path) {
        try{
            FileOutputStream file = new FileOutputStream(path);
            Writer writer = new BufferedWriter(new OutputStreamWriter(file, StandardCharsets.UTF_8));

            writer.write("Catalog_Size: ");
            writer.write(String.valueOf(documents.size()));
            writer.write("\n");

            int i = 0;
            for (Document document : documents){
                writer.write("Doocument[" + i + "]_Path: ");
                writer.write(document.getPath());
                writer.write("\n");
                i++;
            }

            writer.close();
            file.close();
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void loadPlain(String path) {
        try {
            File file = new File(path);
            Scanner scanner = new Scanner(file);

            String junk;
            String info;

            junk = scanner.next();
            info = scanner.next();

            int nrDocuments = Integer.parseInt(info);
            for (int i = 0; i < nrDocuments; i++){
                junk = scanner.next();
                info = scanner.next();
                documents.add(new Document(info));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load(String path) {
        try{
            FileInputStream file = new FileInputStream(path);
            ObjectInputStream in = new ObjectInputStream(file);

            Catalog catalog = (Catalog) in.readObject();
            this.documents = catalog.documents;

            in.close();
            file.close();
        } catch (IOException | ClassNotFoundException exception) {
            exception.printStackTrace();
        }
    }

    public void viewDocument(int index){
        File file = new File(documents.get(index).getPath());

        try {
            Desktop desktop = null;
            if (Desktop.isDesktopSupported()) {
                desktop = Desktop.getDesktop();
            }

            assert desktop != null;
            desktop.open(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void infoDocument(int index) {
        Path file = Paths.get(documents.get(index).getPath());

        try {
            BasicFileAttributes attr = Files.readAttributes(file, BasicFileAttributes.class);
            System.out.println("creationTime: " + attr.creationTime());
            System.out.println("lastAccessTime: " + attr.lastAccessTime());
            System.out.println("lastModifiedTime: " + attr.lastModifiedTime());

            System.out.println("isDirectory: " + attr.isDirectory());
            System.out.println("isOther: " + attr.isOther());
            System.out.println("isRegularFile: " + attr.isRegularFile());
            System.out.println("isSymbolicLink: " + attr.isSymbolicLink());
            System.out.println("size: " + attr.size());

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
