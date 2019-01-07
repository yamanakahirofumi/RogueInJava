package org.hiro.map;

public class TwoDimensionsCoordFactory implements AbstractCoordFactory {
    @Override
    public AbstractCoord create(){
        return new TwoDimensionsCoord();
    }
}
