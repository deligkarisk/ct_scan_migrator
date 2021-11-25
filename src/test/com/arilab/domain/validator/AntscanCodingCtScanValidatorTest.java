package com.arilab.domain.validator;

import com.arilab.domain.CtScan;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AntscanCodingCtScanValidatorTest {

    AntscanCodingCtScanValidator antscanCodingValidator = new AntscanCodingCtScanValidator();

    @ParameterizedTest
    @CsvSource({
            "Yes, ANTSCANCODE",
            "No,"
    })
    void validateIsValid(String antscanFlag, String antscanCode) {
        // given
        CtScan ctScan = new CtScan();
        ctScan.setAntscan(antscanFlag);
        ctScan.setAntscanCode(antscanCode);

        // when
        Optional<String> returnedResult = antscanCodingValidator.validate(ctScan);

        // then
        assertEquals(Optional.empty(), returnedResult);
    }


    @ParameterizedTest
    @CsvSource({
            "No, ANTSCANCODE",
            "Yes,"
    })
    void validateIsiInvalid(String antscanFlag, String antscanCode) {
        // given
        CtScan ctScan = new CtScan();
        ctScan.setAntscan(antscanFlag);
        ctScan.setAntscanCode(antscanCode);

        // when
        Optional<String> returnedResult = antscanCodingValidator.validate(ctScan);

        // then
        assertEquals(Optional.of("Antscan coding is not correct."), returnedResult);
    }
}