package Imptelemnts;

import Exceptions.FirstCharacterNotUpperCaseException;
import Interfaces.Check;

public class CheckFirstCharacterIsUpperCase implements Check {

    private Check checkNext;

    public CheckFirstCharacterIsUpperCase(Check checkNext){
        this.checkNext = checkNext;
    }

    @Override
    public boolean check(String str) throws Exception {
          // This method is more efficient than  str.matches("^[A-Z].*")
           if (!str.matches("^[A-Z].*")) {
               throw new FirstCharacterNotUpperCaseException("İlk Karakter Büyük Harfle Başlanmalı");
        }
        return checkNext.check(str);
    }
}
