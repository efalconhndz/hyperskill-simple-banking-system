package banking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class Account {
    private String accountNumber;
    private String cardPin;
    private final Connection connection;

    public Account(Connection connection) {
        Random random = new Random();

        this.connection = connection;
        this.accountNumber = generateAccountNumber(random);
        this.cardPin = generatePIN(random);

        createAccount(this.accountNumber, this.cardPin);
    }

    public Account(Connection connection, String accountNumber, String cardPIN) {
        this.connection = connection;
        this.accountNumber = accountNumber;
        this.cardPin = cardPIN;
    }

    public String getAccountNumber() {
        return this.accountNumber;
    }

    public String getCardPin() {
        return this.cardPin;
    }

    static boolean checkAccount(Connection connection, String accountNumber) {
        String queriedAccountNumber = null;
        String sql = "SELECT number FROM card WHERE number = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1,accountNumber);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                queriedAccountNumber = resultSet.getString(1);
            }
        }catch (SQLException e){
            System.err.println(e.getMessage());
        }
        return accountNumber.equals(queriedAccountNumber);
    }

    static boolean checkPin(Connection connection, String accountNumber, String cardPin) {
        String queriedCardPin = null;
        String sql = "SELECT pin FROM card WHERE number = ? AND pin = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1,accountNumber);
            statement.setString(2,cardPin);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                queriedCardPin = resultSet.getString(1);
            }
        }catch (SQLException e){
            System.err.println(e.getMessage());
        }
        return cardPin.equals(queriedCardPin);
    }

    public int balance() {
        int balance = 0;

        String sql = "SELECT balance FROM card WHERE number = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, this.accountNumber);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                balance = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return balance;
    }

    private void createAccount(String accountNumber, String cardPin) {

        String sql = "INSERT INTO card(number,pin) VALUES(?,?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, accountNumber);
            statement.setString(2, cardPin);
            statement.executeUpdate();

            connection.commit();

            this.accountNumber = accountNumber;
            this.cardPin = cardPin;

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            if (connection != null) {
                try {
                    System.err.print("Transaction is being rolled back");
                    connection.rollback();
                } catch (SQLException excep) {
                    excep.printStackTrace();
                }
            }
        }
    }

    private String generateAccountNumber(Random random) {
        String bankIdentificationNumber;
        String accountIdentifier;
        String checksum;
        String accountNumber;

        boolean checkLuhn = false;
        do {
            String majorIndustryIdentifier = "4";
            bankIdentificationNumber = majorIndustryIdentifier + "00000";
            accountIdentifier = String.format("%09d", random.nextInt(1000000000));
            checksum = String.valueOf(random.nextInt(10));
            accountNumber = bankIdentificationNumber + accountIdentifier + checksum;

            if (checkLuhn(accountNumber)) {
                checkLuhn = true;
            }
        } while (!checkLuhn);

        return accountNumber;
    }

    private String generatePIN(Random random) {
        return String.format("%04d",random.nextInt(10000));
    }

    static public boolean checkLuhn(String accountNumber) {
        int sum = 0;
        boolean isSecondDigit = false;

        int numberOfDigits = accountNumber.length();
        for (int i = numberOfDigits - 1; i >= 0; i--) {
            int d = accountNumber.charAt(i) - '0';

            if (isSecondDigit) d = d * 2;

            // We add two digits to handle
            // cases that make two digits
            // after doubling
            sum += d / 10;
            sum += d % 10;

            isSecondDigit = !isSecondDigit;
        }
        return (sum % 10 == 0);
    }

    public boolean addIncome(String card, int income) {
        String sql = "UPDATE card SET balance = balance + ? WHERE number = ?";

        int result = -1;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, income);
            statement.setString(2, card);
            result = statement.executeUpdate();

            connection.commit();

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            try {
                System.err.print("Transaction is being rolled back");
                connection.rollback();
            } catch (SQLException excep) {
                excep.printStackTrace();
            }
        }

        return result > 0;
    }

    public boolean transfer(String fromCard, String toCard, int amount) {
        String addBalanceSql = "UPDATE card SET balance = balance + ? WHERE number = ?";
        String subBalanceSql = "UPDATE card SET balance = balance - ? WHERE number = ?";

        int result = -1;
        try (PreparedStatement addStatement = connection.prepareStatement(addBalanceSql);
             PreparedStatement subStatement = connection.prepareStatement(subBalanceSql)) {

            addStatement.setInt(1, amount);
            addStatement.setString(2, toCard);

            subStatement.setInt(1, amount);
            subStatement.setString(2, fromCard);
            result = addStatement.executeUpdate() + subStatement.executeUpdate();

            if (result == 2) {
                connection.commit();
            } else {
                connection.rollback();
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            try {
                System.err.println(e.getMessage());
                System.err.print("Transaction is being rolled back");
                connection.rollback();
            } catch (SQLException excep) {
                excep.printStackTrace();
            }
        }

        return result > 0;
    }

    public boolean closeAccount(Account account) {
        String sql = "DELETE FROM card WHERE number = ?";

        int result = -1;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, account.getAccountNumber());
            result = statement.executeUpdate();

            connection.commit();

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            if (connection != null) {
                try {
                    System.err.print("Transaction is being rolled back");
                    connection.rollback();
                } catch (SQLException excep) {
                    excep.printStackTrace();
                }
            }
        }
        return result > 0;
    }
}
