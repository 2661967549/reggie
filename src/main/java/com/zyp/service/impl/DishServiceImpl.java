package com.zyp.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyp.dto.DishDto;
import com.zyp.entity.Dish;
import com.zyp.entity.DishFlavor;
import com.zyp.mapper.DishMapper;
import com.zyp.service.DishFlavorService;
import com.zyp.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private DishMapper dishMapper;

    @Override
    public void saveWithFlavor(DishDto dishDto) {

        dishService.save(dishDto);

        Long id = dishDto.getId(); //此时id已经产生了，不再为空

        List<DishFlavor> flavors = dishDto.getFlavors();

        //为flavors里的DishFlavor加DishId属性
        //方式一：
//        for (DishFlavor dishFlavor : flavors){
//            dishFlavor.setDishId(id);
//        }
        //方式二：
//        flavors = flavors.stream().map((item)->{
//            item.setDishId(id);
//            return item;
//        }).collect(Collectors.toList());
        //方式三：
        flavors = flavors.stream().peek((item)-> item.setDishId(id)).collect(Collectors.toList());


        dishFlavorService.saveBatch(flavors);
    }

    @Override
    public void updateWithFlavor(DishDto dishDto) {
        this.updateById(dishDto);

        Long dishId = dishDto.getId();

        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().peek((item)-> item.setDishId(dishId)).collect(Collectors.toList());

        //删除之前的口味数据
        log.info("---------------dishFlavor开始删除-----------------");
        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId,dishId);
        dishFlavorService.remove(dishFlavorLambdaQueryWrapper);
        log.info("---------------dishFlavor删除完毕-----------------");

        //添加现在的口味数据
        dishFlavorService.saveBatch(flavors);
    }

    @Override
    public void updateStatusByIds(Integer status, Long[] ids) {
        for (int i=0; i<ids.length; i++) {
            dishMapper.updateStatusById(status,ids[i]);
        }
    }
}
