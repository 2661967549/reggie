package com.zyp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zyp.entity.Dish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {

    @Update("update dish set status = #{status} where id = #{id}")
    void updateStatusById(@Param("status") Integer status, @Param("id") Long id);
}
