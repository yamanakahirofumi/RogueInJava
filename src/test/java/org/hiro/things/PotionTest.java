package org.hiro.things;

import org.hiro.Obj_info;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class PotionTest {

    @Test
    void getTypes() {
        Potion p = new Potion();
        List<Obj_info> types = p.getTypes();
        Assertions.assertEquals(types.size(), 14);

    }
}