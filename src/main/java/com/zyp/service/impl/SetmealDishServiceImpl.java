package com.zyp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyp.entity.Dish;
import com.zyp.entity.SetmealDish;
import com.zyp.mapper.SetmealDishMapper;
import com.zyp.service.DishService;
import com.zyp.service.SetmealDishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper, SetmealDish> implements SetmealDishService {

    @Autowired
    DishService dishService;

    @Override
    public List<Dish> getDishListBySetmealId(long id) {
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, id);
        List<SetmealDish> setmealDishes = this.list(queryWrapper);

        List<Long> dishIds = new ArrayList<>();
        for (SetmealDish setmealDish : setmealDishes) {
            dishIds.add(setmealDish.getDishId());
        }
        return dishService.listByIds(dishIds);
    }
}
