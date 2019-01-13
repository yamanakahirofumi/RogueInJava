package org.hiro.map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TwoDimensionsCoordinateFactoryTest {

    @Test
    @DisplayName("Classの確認")
    void create() {
        TwoDimensionsCoordinateFactory factory = new TwoDimensionsCoordinateFactory();
        AbstractCoordinate coordinate = factory.create();
        assertTrue(coordinate instanceof TwoDimensionsCoordinate);
    }

    @Test
    void getMaxCoordinate() {
        int cols = 80;
        int lines = 24;

        TwoDimensionsCoordinateFactory factory = new TwoDimensionsCoordinateFactory();
        TwoDimensionsCoordinate coordinate = (TwoDimensionsCoordinate) factory.create();
        coordinate.setDimensions(cols/3, lines/3);
        AbstractCoordinate maxCoordinate = factory.getMaxCoordinate();
        assertTrue(maxCoordinate.equals(coordinate));

    }
}