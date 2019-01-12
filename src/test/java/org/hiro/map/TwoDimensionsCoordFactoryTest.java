package org.hiro.map;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TwoDimensionsCoordFactoryTest {

    @Test
    @DisplayName("Classの確認")
    void create() {
        TwoDimensionsCoordinateFactory factory = new TwoDimensionsCoordinateFactory();
        AbstractCoordinate coord = factory.create();
        assertTrue(coord instanceof TwoDimensionsCoordinate);
    }

    @Test
    @Disabled
    void getMaxCoord() {
    }
}