package com.arilab.domain.validator;

import com.arilab.domain.CtScan;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class WetDryCombinationCtScanValidatorTest {

    WetDryCombinationCtScanValidator wetDryCombinationValidator = new WetDryCombinationCtScanValidator();



    @ParameterizedTest
    @CsvSource({
            "Yes,,EthanolConc",
            "No,DryMethod,No ethanol used"
    })
    void validateIsValid(String wetFlag, String dryFlag, String ethanolConcentrationFlag) {
        // given
        CtScan ctScan = new CtScan();
        ctScan.setWet(wetFlag);
        ctScan.setDryMethod(dryFlag);
        ctScan.setEthanolConcentration(ethanolConcentrationFlag);

        // when
        Optional<String> returnedValue = wetDryCombinationValidator.validate(ctScan);

        // then
        assertEquals(Optional.empty(),returnedValue);

    }


    @ParameterizedTest
    @CsvSource({
            "Yes,DryMethod,EthanolConc",
            "No,DryMethod,EthanolConc",
            "Yes, DryMethod,",
            "Yes,,",
            "No,,",
            "No,DryMethod,EthanolConc"
    })
    void validateIsInvalid(String wetFlag, String dryFlag, String ethanolConcentrationFlag) {
        // given
        CtScan ctScan = new CtScan();
        ctScan.setWet(wetFlag);
        ctScan.setDryMethod(dryFlag);
        ctScan.setEthanolConcentration(ethanolConcentrationFlag);

        // when
        Optional<String> returnedValue = wetDryCombinationValidator.validate(ctScan);

        // then
        assertEquals(Optional.of("Wet/dry combinations not correct."),returnedValue);
    }
}