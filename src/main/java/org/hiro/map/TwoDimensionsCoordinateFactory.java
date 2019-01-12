package org.hiro.map;

public class TwoDimensionsCoordinateFactory implements AbstractCoordinateFactory {
    private final int cols = 80;
    private final int lines = 24;

    @Override
    public AbstractCoordinate create(){
        return new TwoDimensionsCoordinate();
    }
    @Override
    public AbstractCoordinate getMaxCoordinate(){
        return new TwoDimensionsCoordinate(cols / 3, lines/3 );
    }
}
