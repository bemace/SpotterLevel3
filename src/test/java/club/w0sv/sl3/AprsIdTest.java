package club.w0sv.sl3;

import club.w0sv.util.AprsId;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class AprsIdTest {

    @ParameterizedTest
    @MethodSource("of_data")
    void of(String input, AprsId expected) {
        assertThat(AprsId.of(input)).isEqualTo(expected);
    }
    
    private static Stream<Arguments> of_data() {
        return Stream.of(
                Arguments.of("W9AX-9", new AprsId("W9AX",9)),
                Arguments.of("KU5MC-9", new AprsId("KU5MC",9)),
                Arguments.of("k9mp-9", new AprsId("K9MP",9)),
                Arguments.of("KD0NNi-tour", new AprsId("KD0NNI", "tour")),
                Arguments.of("KC0TAF-1-i", new AprsId("KC0TAF", "1-i")),
                Arguments.of("KC0UEA-i", new AprsId("KC0UEA", "i"))
        );
    }
}