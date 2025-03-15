package Imptelemnts;

import Exceptions.FirstCharacterNotUpperCaseException;
import Exceptions.LastCharacterMismatch;
import Exceptions.LengthException;
import Interfaces.Check;

public class CheckLength implements Check {

    @Override
    public boolean check(String str) throws Exception {
        if (!(str.length() >= 8)) {
            throw new LengthException("Karakter Uzunluğu Minimum 8 olmalı.");
        }
        return true;
    }
}
