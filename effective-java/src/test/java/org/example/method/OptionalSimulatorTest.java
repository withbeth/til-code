package org.example.method;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OptionalSimulatorTest {

    @Test
    void maxOrException() {
        assertEquals(1, OptionalSimulator.maxOrException(List.of(1, 1)));
        assertEquals(10, OptionalSimulator.maxOrException(List.of(1, 5, 10)));
    }

}