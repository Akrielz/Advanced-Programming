package com.company;

import java.io.File;

public class LoadCommand implements Command{
    private String argument;
    private Catalog catalog;

    public LoadCommand(String argument, Catalog catalog) {
        this.argument = argument;
        this.catalog = catalog;
    }

    @Override
    public void execute() {
        File file = new File(argument);

        if (!file.exists()) {
            System.out.println("File doesn't exists");
            return;
        }

        for (Document document : catalog.getDocuments()){
            if (document.getPath().equals(argument)){
                System.out.println("File already added");
                return;
            }
        }

        System.out.println("Load successful");
        Document document = new Document(argument);
        catalog.addDocument(document);
    }
}
