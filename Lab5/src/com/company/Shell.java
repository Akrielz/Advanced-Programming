package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Shell {
    List<Command> commands;
    Catalog catalog;

    public Shell() {
        commands = new ArrayList<>();
        catalog = new Catalog();
    }

    public void executeLastCommand() {
        int last = commands.size()-1;
        commands.get(last).execute();
    }

    public int readCommand() {
        System.out.print(">> ");
        String command;
        String argument;

        Scanner console = new Scanner(System.in);
        command = console.next();

        command = command.toLowerCase();

        switch (command){
            case "list":
                ListCommand listCommand = new ListCommand(catalog);
                commands.add(listCommand);
                break;

            case "load":
                argument = console.next();
                LoadCommand loadCommand = new LoadCommand(argument, catalog);
                commands.add(loadCommand);
                break;

            case "view":
                argument = console.next();
                ViewCommand viewCommand = new ViewCommand(argument, catalog);
                commands.add(viewCommand);
                break;

            case "report":
                argument = console.next();
                ReportCommand reportCommand = new ReportCommand(argument, catalog);
                commands.add(reportCommand);
                break;

            case "info":
                argument = console.next();
                InfoCommand infoCommand = new InfoCommand(argument, catalog);
                commands.add(infoCommand);
                break;

            case "exit":
                return -1;

            default:
                return 0;
        }

        return 1;
    }
}
