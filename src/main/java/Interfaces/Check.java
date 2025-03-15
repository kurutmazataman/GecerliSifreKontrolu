package Interfaces;

import Exceptions.FirstCharacterNotUpperCaseException;
import Exceptions.LastCharacterMismatch;
import Exceptions.LengthException;

public interface  Check {
    boolean check(String str) throws Exception;
}
