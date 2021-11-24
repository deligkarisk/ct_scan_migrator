package com.arilab.flowcontroller;

import com.arilab.utils.ArgumentChecker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ArgumentCheckerTest {

    ArgumentChecker argumentChecker = new ArgumentChecker();

    @BeforeEach
    void setUp() {

    }


    @Test
    void checkMinimumTwoArguments() {
        String[] arguments = {""};
        Assertions.assertThrows(RuntimeException.class, () -> { argumentChecker.check(arguments); } );
    }

    @Test
    void wrongThirdArgument() {
        String[] arguments = {"", "", "randomArgument"};
        Assertions.assertThrows(RuntimeException.class, () -> {argumentChecker.check(arguments);});
    }

    @Test
    void correctThirdArgument() {
        String[] arguments = {"", "", "--do-migration"};
        assertTrue(argumentChecker.check(arguments));
    }
}