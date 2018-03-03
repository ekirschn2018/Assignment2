package com.evelynkirschner.assignment_2;

import java.util.Map;
import java.util.TreeMap;
import java.util.Scanner;
import java.util.Set;
import java.util.Iterator;

class Task {
    // instance fields
    protected String title, description;
    protected int priority;     // can be from 0 to 5 where 5 is the highest

    // Constructor
    public Task(String userTitle, String desc, int prty){
        this.title = userTitle;
        this.description = desc;
        this.priority = prty;
    }
}

class TaskList {
    // instance fields
    private Map<String,Task> _tasks = new TreeMap<>();

    // class field
    private static Scanner scanner = new Scanner(System.in);

    // Constructor - not needed

    // methods
    // check if title is valid
    private String getTaskTitle(String donePrompt, String doneString ){
        // userPrompt: How we ask for the string
        // doneString: if the input == the doneString then exit
        System.out.println("Enter the Title of the task " + donePrompt);
        String userInput = scanner.nextLine();

        String userString = "";
        boolean isString = false;
        while (!isString) {
            // if the string is the doneString, then return
            if ( userInput.toLowerCase().contentEquals(doneString.toLowerCase()) )
            {
                isString = true;
                userString = doneString;
            }
            else if ( userInput.equals("") )
                // if doneString is "" then captured above otherwise we want a string
                // so ask for another
            {
                System.out.println("You must enter a non-empty string.");
                System.out.println("Enter the Title of the task " + donePrompt);
                userInput = scanner.nextLine();
            }
            else
            {
                userString = userInput;
                isString = true;
            }
        }
        return userString;
    }

    // check if description is valid
    private String getTaskDescription(){
        // userPrompt: How we ask for the string
        System.out.println("Enter the Description of the task:");
        String userInput = scanner.nextLine();

        String userString = "";
        boolean isString = false;
        while (!isString) {
            // if the string is null, then ask for another
            if ( userInput.equals("") )    // if doneString is "" then captured above otherwise we want a string
            {
                System.out.println("You must enter a non-empty string.");
                System.out.println("Enter the Description of the task:");
                userInput = scanner.nextLine();
            }
            else
            {
                userString = userInput;
                isString = true;
            }
        }
        return userString;
    }

    // check if priority is valid
    private int getPriority(int lowValue, int hiValue){
        // valid range of Priorities lowValue to hiValue

        // build the prompt for the priority
        String userPrompt = "Enter the Priority of the task (between " + lowValue + " (low) and " + hiValue + " (high)).";
        System.out.println(userPrompt);
        String userInput = scanner.nextLine();

        int userInt = 0;
        boolean isInt = false;
        while (!isInt) {
            try {
                userInt = Integer.parseInt(userInput);
                if ((userInt >= lowValue) && (userInt <= hiValue)) {
                    isInt = true;
                }
                else {
                    System.out.println("Priority must be between " + lowValue + " (low) and " + hiValue + " (high).");
                    System.out.println(userInput + " is not a valid priority. ");
                    System.out.println(userPrompt);
                    userInput = scanner.nextLine();
                }
            }
            catch (NumberFormatException e) {
                System.out.println(userInput + " is not a valid integer. ");
                System.out.println(userPrompt);
                userInput = scanner.nextLine();
            }
        }
        return userInt;
    }
    // add a task
    public void addTask(){
        String newTitle, newDesc;
        int newPriority;
        String cancelFlag = "Cancel";

        // get the Title
        newTitle = getTaskTitle("(type Cancel to Cancel adding a new task):",
                cancelFlag);
        if (newTitle.equals(cancelFlag))
            return;     // user is cancelling the add

        // get the Description
        newDesc = getTaskDescription();
        // get the Priority
        newPriority = getPriority( 0, 5);
        // add it
        _tasks.put(newTitle, new Task(newTitle, newDesc, newPriority));
    }
    // remove a task
    public void removeTask(){
        String cancelFlag = "Cancel";
        String removeIt;

        // get the title of the task to remove
        String removeTitle = getTaskTitle("(type Cancel to Cancel removing a task):",
                cancelFlag);
        if (removeTitle.equals(cancelFlag)) {
            System.out.println("Remove canceled");
            return;     // user is cancelling the remove
        }
        // make sure the user wants to remove the task
        System.out.print("Are you sure? (Y/N)");
        removeIt = scanner.nextLine();
        if (removeIt.toUpperCase().contentEquals("Y")) {
            // agreeing to remove
            if ( _tasks.containsKey(removeTitle) ) { // if it is a valid task
                // remove the task
                _tasks.remove(removeTitle);
            }
            else
                System.out.println("You do not have a task with the title '" + removeTitle + "' on your list");
        }
        else
            System.out.println("Remove canceled");
    }
    // edit a task
    public void editTask(){
        String cancelFlag = "Cancel";
        String editDescription, changeIt;
        int editPriority;

        // get the title of the task to edit
        String editTitle = getTaskTitle("(type Cancel to Cancel editing a task):",
                cancelFlag);
        if (editTitle.equals(cancelFlag)) {
            System.out.println("Edit canceled");
            return;     // user is cancelling the edit
        }
        // make sure it is a valid task
        if ( _tasks.containsKey(editTitle) ) {
            Task currentTask = _tasks.get(editTitle);
            // ask for new description
            System.out.println("Change the description? (Y/N)");
            changeIt = scanner.nextLine();
            if (changeIt.toUpperCase().contentEquals("Y"))
                editDescription = getTaskDescription();
            else
                // keep it the same
                editDescription = _tasks.get(editTitle).description;

            // ask for a new priority
            System.out.println("Change the priority? (Y/N)");
            changeIt = scanner.nextLine();
            if (changeIt.toUpperCase().contentEquals("Y"))
                editPriority = getPriority(0, 5);
            else
                // keep it the same
                editPriority = _tasks.get(editTitle).priority;

            // now update it
            currentTask.priority = editPriority;
            currentTask.description = editDescription;
        }
        else
            System.out.println("You do not have a task with the title '" + editTitle + "' on your list");
   }
    // List all tasks
    public void listTasks(){
        boolean isTask = false;
        // Get a set of the keys
        Set<String> keys = _tasks.keySet();

        for (String task: keys){    // list for all keys
            if (! isTask ){
                isTask = true;
                System.out.println("Your Current Tasks");
            }
            System.out.println(task + ": " + _tasks.get(task).description);
            System.out.println("Priority: "+ _tasks.get(task).priority);
        }
        if (! isTask ){
            System.out.println("You do not have any Tasks on your list.");
        }
    }
    // List tasks of a certain priority
    public void listPriorityTasks(){
        // get the priority
        int listPriority = getPriority(0,5);
        boolean haveTasks = false;

        // Get a set of the keys
        Set<String> keys = _tasks.keySet();

        // list all tasks in the tree with that priority
        for (String task: keys){
            if ( _tasks.get(task).priority == listPriority ) {
                // list it
                if (!haveTasks) { // first time, give a title
                    System.out.println("Tasks with priority " + listPriority);
                    haveTasks = true;
                }
                System.out.println(task + ": " + _tasks.get(task).description);
            }
        }
        if (!haveTasks){
            // There were not any of this priority
            System.out.println("You do not have any Tasks with priority " + listPriority);
        }
    }
}

public class Main {

    public static void listMenuOptions(){
        // method to list the Menu Options

        System.out.println("Please choose an option:");
        System.out.println("(1) Add a task.");
        System.out.println("(2) Remove a task.");
        System.out.println("(3) Edit a task's description and/or priority.");
        System.out.println("(4) List all tasks.");
        System.out.println("(5) List tasks of a certain priority");
        System.out.println("(0) Exit");
    }

    public static int getUserOption(int loOpt, int hiOpt){
        // method to get user input

        String numberAsString;
        int userOption = loOpt;
        Scanner scanner = new java.util.Scanner(System.in);

        listMenuOptions();
        numberAsString = scanner.nextLine();
        boolean goodOption = false;
        while (!goodOption)
        {
            try{
                userOption = Integer.parseInt(numberAsString);     // Convert string to a integer
                if ( ( userOption >= loOpt ) && (userOption <= hiOpt) )  // check to see if range of options
                    goodOption = true;
                else {
                    System.out.println(userOption + " is not a valid option. ");
                    listMenuOptions();
                    numberAsString = scanner.nextLine();
                }
            }
            catch (NumberFormatException e) {
                System.out.println(numberAsString + " is not a valid option. ");
                listMenuOptions();
                numberAsString = scanner.nextLine();
            }
        }
        return userOption;
    }

    public static void main(String[] args) {
	    TaskList toDo = new TaskList();
	    int userChoice;     // user input from the menu

        userChoice = getUserOption(0,5);
        while (userChoice != 0){
            switch (userChoice) {
                case 1:         // Add a Task
                    toDo.addTask();
                    break;
                case 2:         // Remove a Task
                    toDo.removeTask();
                    break;
                case 3:         // Edit a Task
                    toDo.editTask();
                    break;
                case 4:         // List all Tasks
                    toDo.listTasks();
                    break;
                case 5:         // List Tasks of a certain priority
                    toDo.listPriorityTasks();
            }
            userChoice = getUserOption(0,5);
        }


    }
}
