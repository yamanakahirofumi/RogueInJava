package org.hiro;

import org.hiro.things.ObjectType;
import org.hiro.things.ThingImp;

/*
 * describe a place on the level map
 */
public class Place {
    public ObjectType p_ch;
    int p_flags;  // charかも
    public ThingImp p_monst;
}
