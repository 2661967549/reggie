package com.zyp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyp.entity.Dish;
import com.zyp.entity.SetmealDish;

import java.util.List;

public interface SetmealDishService extends IService<SetmealDish> {
    List<Dish> getDishListBySetmealId(long ids);
}
