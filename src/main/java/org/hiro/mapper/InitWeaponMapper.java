package org.hiro.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.hiro.InitWeapon;

@Mapper
public interface InitWeaponMapper {

    InitWeapon findById(int Id);
}
