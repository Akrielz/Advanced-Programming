package com.company;

public class InfoCommand implements Command {
    private int argument;
    private Catalog catalog;

    public InfoCommand(String argument, Catalog catalog) {
        this.argument = Integer.parseInt(argument);
        this.catalog = catalog;
    }

    @Override
    public void execute() {
        if (catalog.getDocuments().size() <= argument){
            System.out.println("Cannot access file with nr: " + argument);
            return;
        }
        catalog.infoDocument(argument);
    }
}
