import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Main {

    public static void main(String[] args) {
        List<String> userInput = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();

            if ("0".equals(input)) {
                break;
            } else {
                userInput.add(input);
            }
        }

        for (String input : userInput) {
            try {
                System.out.println(Integer.parseInt(input) * 10);
            } catch (NumberFormatException e) {
                System.out.println("Invalid user input: " + input);
            }
        }
    }
}