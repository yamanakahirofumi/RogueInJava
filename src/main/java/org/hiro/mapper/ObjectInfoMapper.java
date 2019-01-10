package org.hiro.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.hiro.Obj_info;
import org.hiro.mapper.params.TypePlayer;

import java.util.List;

@Mapper
public interface ObjectInfoMapper {

    List<Obj_info> findAll();

    List<Obj_info> findByTypeByPlayer(TypePlayer tp);

}
