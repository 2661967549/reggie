package com.zyp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyp.dto.DishDto;
import com.zyp.entity.Dish;

public interface DishService extends IService<Dish> {
    void saveWithFlavor(DishDto dishDto);
    void updateWithFlavor(DishDto dishDto);
    void updateStatusByIds(Integer status,Long[] ids);
}
