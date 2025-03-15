import Exceptions.FirstCharacterNotUpperCaseException;
import Exceptions.LastCharacterMismatch;
import Exceptions.LengthException;
import Imptelemnts.*;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        while (true) {
            System.out.print("Şifre Girin: ");
            try {
                new CheckFirstCharacterIsUpperCase(new CheckLastCharacterEqualsToQuestionMark(new CheckSpaceCharacter(new CheckLength()))).check(new Scanner(System.in).nextLine());
                return;
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            }
        }
    }
}