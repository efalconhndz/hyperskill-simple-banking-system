package banking;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Menu {
    Scanner scanner = new Scanner(System.in);

    Account account;
    boolean loggedIn;
    boolean askForAnother = true;
    Connection connection;

    public Menu(Connection connection) {
        this.connection = connection;
    }

    private void createAccount() throws SQLException {
        account = new Account(connection);

        String accountNumber = account.getAccountNumber();
        String cardPin = account.getCardPin();

        System.out.println("Your card has been created");
        System.out.println("Your card number:");
        System.out.println(accountNumber);
        System.out.println("Your card PIN:");
        System.out.println(cardPin);

        printMenu(-1);
    }

    private void accountLogIn() throws SQLException {
        System.out.println("Enter your card number:");
        String inputCard = scanner.nextLine();
        System.out.println("Enter your PIN:");
        String inputPIN = scanner.nextLine();

        if (Account.checkAccount(connection, inputCard) && Account.checkPin(connection, inputCard, inputPIN)) {
            this.account = new Account(connection, inputCard, inputPIN);
            setLoggedIn(true);
            System.out.println("You have successfully logged in!");
            accountMenu(-1);
        } else {
            System.out.println("Wrong card number or PIN!");
            printMenu(-1);
        }
    }

    private int getOptionFromUser() {
        return Integer.parseInt(scanner.nextLine());
    }

    private void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    private void accountMenu(int menuEntry) throws SQLException {
        while (askForAnother) {
            switch (menuEntry) {
                case 0:
                    setLoggedIn(false);
                    askForAnother = false;
                    return;
                case 1:
                    System.out.println("Balance: " + account.balance());
                    accountMenu(-1);
                    break;
                case 2:
                    System.out.println("Enter income: ");

                    if (account.addIncome(account.getAccountNumber(), getOptionFromUser())) {
                        System.out.println("Income was added!");
                    } else {
                        System.out.println("Couldn't add income, try again later.");
                    }

                    accountMenu(-1);
                    break;
                case 3:
                    System.out.println("Transfer\n" + "Enter card number:");
                    String inputCard = scanner.nextLine();

                    if (Account.checkLuhn(inputCard)) {

                        if (Account.checkAccount(this.connection, inputCard)) {

                            if (!account.getAccountNumber().equals(inputCard)) {

                                int amount = getOptionFromUser();
                                if (amount <= account.balance()) {

                                    if (account.transfer(account.getAccountNumber(), inputCard, amount)) {

                                        System.out.println("Success!");

                                    } else {
                                        System.out.println("Couldn't transfer the money, try again later.");
                                    }

                                } else {
                                    System.out.println("Not enough money!");
                                }

                            } else {
                                System.out.println("You can't transfer money to the same account!");
                            }

                        } else {
                            System.out.println("Such a card does not exist.");
                        }

                    } else {
                        System.out.println("Probably you made a mistake in the card number. Please try again!");
                    }

                    accountMenu(-1);
                    break;
                case 4:
                    if (account.closeAccount(account)) {
                        System.out.println("The account has been closed!");
                        printMenu(-1);
                    } else {
                        System.out.println("Couldn't close the account, try again later.");
                    }
                    break;
                case 5:
                    setLoggedIn(false);
                    System.out.println("You have successfully logged out!");
                    printMenu(-1);
                    break;
                default:
                    System.out.println("1. Balance");
                    System.out.println("2. Add income");
                    System.out.println("3. Do transfer");
                    System.out.println("4. Close Account");
                    System.out.println("5. Log out");
                    System.out.println("0. Exit");

                    accountMenu(getOptionFromUser());
            }
        }
    }

    public void printMenu(int menuEntry) throws SQLException {
        while (askForAnother) {
            switch (menuEntry) {
                case 0:
                    setLoggedIn(false);
                    askForAnother = false;
                    return;
                case 1:
                    createAccount();
                    printMenu(-1);
                    break;
                case 2:
                    accountLogIn();
                    break;
                default:
                    System.out.println("1. Create an account");
                    System.out.println("2. Log into account");
                    System.out.println("0. Exit");

                    printMenu(getOptionFromUser());
                    break;
            }
        }
    }
}
