package org.hiro.mapper;

import org.hiro.Obj_info;
import org.hiro.Rogue;
import org.hiro.mapper.params.TypePlayer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringJUnitConfig(Rogue.class)
class ObjectInfoMapperTest {

    @Autowired
    ObjectInfoMapper mapper;

    @Test
    @DisplayName("Armors object info initial test")
    void findByTypeByPlayer(){
        TypePlayer tp = new TypePlayer(11,1);
        List<Obj_info> typeByPlayer = mapper.findByTypeByPlayer(tp);
        assertEquals(typeByPlayer.size(),8);
        assertEquals(typeByPlayer.get(0).getName(), "leather armor");

    }

    @Configuration
    static class LocalTestContext {}

}