package org.hiro.mapper;

import org.hiro.InitWeapon;
import org.hiro.Rogue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringJUnitConfig(Rogue.class)
class InitWeaponMapperTest {

    @Autowired
    private InitWeaponMapper mapper;

    @Test
    @DisplayName("InitWeapon initial test")
    void findById(){
        InitWeapon initWeapon = mapper.findById(0);
        assertEquals(initWeapon.iw_dam, "2x4");

    }

    @Configuration
    static class LocalTestContext {}



}