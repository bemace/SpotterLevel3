package club.w0sv.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import systems.uom.common.USCustomary;
import tech.units.indriya.quantity.Quantities;
import tech.units.indriya.unit.Units;

import javax.measure.Quantity;
import javax.measure.Unit;
import javax.measure.quantity.Speed;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class QuantityUtilTest {
    @ParameterizedTest
    @MethodSource("toDecimal_data")
    <Q extends Quantity<Q>> void toDecimal(Quantity<Q> input, Unit<Q> unit, BigDecimal expected) {
        assertThat(QuantityUtil.toDecimal(input, unit)).isEqualTo(expected);
    }

    private static Stream<Arguments> toDecimal_data() {
        return Stream.of(
                Arguments.of(Quantities.getQuantity(10, Units.KILOMETRE_PER_HOUR), USCustomary.MILE_PER_HOUR, new BigDecimal("6.213711922373339696174341843633182"))
        );
    }

    @ParameterizedTest
    @MethodSource("setScale_data")
    <Q extends Quantity<Q>> void setScale(Quantity<Q> input, int scale, RoundingMode roundingMode, Quantity<Q> expected) {
        assertThat(QuantityUtil.setScale(input, scale, roundingMode)).isEqualTo(expected);
    }

    private static Stream<Arguments> setScale_data() {
        Quantity<Speed> quantity = Quantities.getQuantity(new BigDecimal("6.213711922373339696174341843633182"), USCustomary.MILE_PER_HOUR);
        return Stream.of(
                Arguments.of(quantity, 5, RoundingMode.HALF_UP, Quantities.getQuantity( new BigDecimal("6.21371"),USCustomary.MILE_PER_HOUR )),
                Arguments.of(quantity, 1, RoundingMode.HALF_UP, Quantities.getQuantity( new BigDecimal("6.2"),USCustomary.MILE_PER_HOUR ))
        );
    }


}