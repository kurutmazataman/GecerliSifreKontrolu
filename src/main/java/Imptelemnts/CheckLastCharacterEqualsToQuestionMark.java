package Imptelemnts;

import Exceptions.FirstCharacterNotUpperCaseException;
import Exceptions.LastCharacterMismatch;
import Exceptions.LengthException;
import Interfaces.Check;

public class CheckLastCharacterEqualsToQuestionMark implements Check {

    private Check checkNext;

    public CheckLastCharacterEqualsToQuestionMark(Check checkNext){
        this.checkNext = checkNext;
    }

    @Override
    public boolean check(String str) throws Exception{
        //more efficient than str.endsWith();
        if (!str.endsWith("?")) {
            throw new LastCharacterMismatch("Son Karakter '?' olmalı");
        }
        return checkNext.check(str);
    }
}
