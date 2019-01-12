package org.hiro.things;

import org.hiro.Obj_info;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


class ArmorTest {

    @Test
    void getTypes() {
        Armor armor = new Armor();
        List<Obj_info> types = armor.getTypes();
        assertEquals(types.size(), 8);
    }

}