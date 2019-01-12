package org.hiro.map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TwoDimensionsCoordTest {

    @Test
    void random() {
    }

    @Test
    @DisplayName("reflexive(反射性)")
    void equalsReflexive() {
        TwoDimensionsCoordinate coord1 = new TwoDimensionsCoordinate(1,2);
        assertTrue(coord1.equals(coord1));
    }

    @Test
    @DisplayName("null check")
    void eqaulsNull(){
        TwoDimensionsCoordinate coord = new TwoDimensionsCoordinate(3,4);
        assertFalse(coord.equals(null));
    }
}