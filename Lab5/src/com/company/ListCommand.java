package com.company;

public class ListCommand implements Command{
    private Catalog catalog;

    public ListCommand(Catalog catalog) {
        this.catalog = catalog;
    }

    @Override
    public void execute() {
        if (catalog.getDocuments().size() == 0){
            System.out.println("No documents available.");
            return;
        }

        int id = 0;
        for (Document document : catalog.getDocuments()){
            System.out.println(document.getPath() + " --|-- Accessed with id: " + id);
            id++;
        }
    }
}
