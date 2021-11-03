import java.util.Scanner;

class FixingArithmeticException {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int a = scanner.nextInt();
        int b = scanner.nextInt();
        int c = scanner.nextInt();
        int d = scanner.nextInt();

        if (d != 0) {
            int subResult = (b + c) / d;
            
            if (subResult != 0) {
                System.out.println(a / subResult);
            } else {
                System.out.println("Division by zero!");
            }
        } else {
            System.out.println("Division by zero!");
        }
    }
}