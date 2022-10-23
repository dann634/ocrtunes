package com.example.ocrtune;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Account {
    private String name, password, dateOfBirth, favArtist, favGenre;
    private final String ACCOUNT_DETAILS_DIR = "src/main/Account Details/";
    private static String currentUser;

    public void saveAccountData(String usernameInput, String passwordInput, String dateOfBirthInput, String favArtistInput, String favGenreInput) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(ACCOUNT_DETAILS_DIR + "/" + usernameInput + ".txt"));
            writer.write(usernameInput
                    + "\n" + passwordInput
                    + "\n" + dateOfBirthInput
                    + "\n" + favArtistInput
                    + "\n" + favGenreInput);
            writer.close();

        } catch (IOException e) {
            System.err.print("Error: Couldn't save account details to file");
        }
    }
    public boolean login(String usernameInput, String passwordInput) {
        AccountDetails.saveFileNames();
        String firstLine = "";
        String secondLine = "";
        boolean detailsMatch = false;
        try {
            File[] listOfFiles = AccountDetails.saveFileNames();
            for (int i = 0; i < listOfFiles.length; i++) {

            BufferedReader reader = new BufferedReader(new FileReader("src/main/Account Details/" + listOfFiles[i].getName()));
            firstLine = reader.readLine();
            secondLine = reader.readLine();

                if(firstLine != null && secondLine != null) {
                    if(firstLine.equals(usernameInput) && secondLine.equals(passwordInput)) {
                        detailsMatch = true;
                        break;
                    }
                }
            }

            if(detailsMatch) {
                return true;
            }
        } catch (FileNotFoundException e) {
            System.err.print("Error: File Not Found");
        } catch (IOException e) {
            System.err.print("Error: IO Exception whilst logging in");
            System.out.println(e);
        }
        return false;
    }

    public void register(String usernameInput, String passwordInput, String dateOfBirthInput, String favArtistInput, String favGenreInput) {
        AccountDetails.saveFileNames();
        boolean fileDuplicate = false;
        File[] listOfFiles = AccountDetails.saveFileNames();

        for (int i = 0; i < listOfFiles.length; i++) {
            if(usernameInput.equalsIgnoreCase(listOfFiles[i].getName())) {
                fileDuplicate = true;
            }
        }
        //Creates new account details file
        if(!fileDuplicate) {
            File newAccountDetails = new File(ACCOUNT_DETAILS_DIR + "/" + usernameInput + ".txt");
            try {
                newAccountDetails.createNewFile();
            } catch (IOException e) {
                System.err.println("Error: Account Details File Not Created");
            }

        }
        saveAccountData(usernameInput, passwordInput, dateOfBirthInput, favArtistInput, favGenreInput);
    }

    public static List<String> getAllUsers() {
        List<String> userList = new ArrayList<>();
        File[] listOfFiles = AccountDetails.saveFileNames();

        for(File file : listOfFiles) {
            userList.add(file.getName());
        }
        return userList;
    }

    public static String getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(String currentUser) {
        Account.currentUser = currentUser;
    }

    public static boolean doesUsernameExist(String newUsername) {
        List<String> listOfUsers = getAllUsers();
        for(String fileName : listOfUsers) {
            String username = fileName.substring(0, fileName.length() - 4);
            if (username.equalsIgnoreCase(newUsername)) {
                return true;
            }
        }
        return false;
    }

}
