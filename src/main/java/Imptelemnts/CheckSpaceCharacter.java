package Imptelemnts;

import Exceptions.NotContainSpaceCharacter;
import Interfaces.Check;

public class CheckSpaceCharacter implements Check {

    Check checkNext;

    public CheckSpaceCharacter(Check check){
        this.checkNext = check;
    }

    @Override
    public boolean check(String str) throws Exception {
        if (!str.contains(" ")) {
        throw new NotContainSpaceCharacter("Boşluk Harfi İçermeli");
        }
        return checkNext.check(str);
    }
}
