package club.w0sv.sl3;

import club.w0sv.util.CallSign;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CallSignTest {

    @ParameterizedTest
    @MethodSource("of_data")
    void of(String input, CallSign expected) {
        CallSign actual;
        try {
            actual = CallSign.of(input);
            if (expected == null)
                fail("should have thrown exception");
            else
                assertThat(actual).isEqualTo(expected);
        }
        catch (Exception ex) {
            if (expected != null)
                throw ex;
        }
    }

    private static Stream<Arguments> of_data() {
        return Stream.of(
                Arguments.of("W9AX", new CallSign("W9AX", null)),
                Arguments.of("KU5MC/W3", new CallSign("KU5MC","W3")),
                Arguments.of("k9mp/m", new CallSign("K9MP","m")),
                Arguments.of("1X", null),
                Arguments.of("A2 rf", null)
        );
    }
}