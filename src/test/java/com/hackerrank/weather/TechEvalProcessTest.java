package com.hackerrank.weather;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class TechEvalProcessTest {

    TechEvalProcess techEvalProcess = new TechEvalProcess();

    @Test
    public void givenNonRepeatingCharacters_expectTheIndex() {
        String s = "loveoldnavy";

        int actual = techEvalProcess.checkForNonRepeatingCharacter(s);

        Assertions.assertThat(3).isEqualTo(actual);
    }

    @Test
    public void givenStringoldnavy_expectTheIndex() {
        String s = "oldnavy";

        int actual = techEvalProcess.checkForNonRepeatingCharacter(s);

        Assertions.assertThat(0).isEqualTo(actual);
    }

    @Test
    public void givenNoNonRepeatingCharacters_expectTheIndex() {
        String s = "olddlo";

        int actual = techEvalProcess.checkForNonRepeatingCharacter(s);

        Assertions.assertThat(-1).isEqualTo(actual);
    }

    @Test
    public void givenNullOrEmptyString_expectDefaultValue() {
        String s = "";

        int actual = techEvalProcess.checkForNonRepeatingCharacter(s);
        Assertions.assertThat(-1).isEqualTo(actual);

        String sn = null;

        int actual1 = techEvalProcess.checkForNonRepeatingCharacter(sn);
        Assertions.assertThat(-1).isEqualTo(actual1);
    }

    @Test
    public void test(){
        techEvalProcess.print();
    }
}