package org.hiro.generate;

import org.hiro.things.Thing;
import org.hiro.things.ThingFactory;

public class ItemGenerator {

    private ThingFactory factory;

    public ItemGenerator(ThingFactory factory){
        this.factory = factory;
    }

    public Thing createThing(){
        return factory.create();
    }

}
