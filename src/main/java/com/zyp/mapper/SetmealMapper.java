package com.zyp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zyp.entity.Setmeal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface SetmealMapper extends BaseMapper<Setmeal> {

    @Update("<script>"
            +"update setmeal set status = #{status} where id in "
            +"<foreach item='item' index='index' collection='id' open='(' separator=',' close=')'>"
                +"#{item}"
            +"</foreach>"
        +"</script>"
    )
    void updateStatusByIds(@Param("status") Integer status, @Param("id") Long[] id);
}
