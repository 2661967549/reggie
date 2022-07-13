package com.zyp.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyp.common.R;
import com.zyp.dto.DishDto;
import com.zyp.entity.Category;
import com.zyp.entity.Dish;
import com.zyp.entity.DishFlavor;
import com.zyp.service.CategoryService;
import com.zyp.service.DishFlavorService;
import com.zyp.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {

    @Autowired
    DishService dishService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    DishFlavorService dishFlavorService;

    @Autowired
    RedisTemplate<Object,Object> redisTemplate;


    @PostMapping
    @CacheEvict(value = "userCategoryDish", key = "#dishDto.getCategoryId()")
    public R<String> save(@RequestBody DishDto dishDto){
        dishService.saveWithFlavor(dishDto);

//        //构造redis的key
//        String key = "dish_"+dishDto.getCategoryId()+"_1";
//        //清理redis缓存，保证redis缓存数据和mysql数据库中的数据一致性
//        redisTemplate.delete(key);

        return R.success("保存成功");
    }

    @GetMapping("/page")
    public R<Page<DishDto>> Dishlsit(Integer page, Integer pageSize,String name){
        //设置查询条件
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.like(StringUtils.isNotEmpty(name),Dish::getName,name);
        dishLambdaQueryWrapper.orderByDesc(Dish::getUpdateTime);

        //查询菜品消息（查到的数据中没有分类名称，但有分类id）
        Page<Dish> dishPage = new Page<>(page, pageSize);
        dishService.page(dishPage,dishLambdaQueryWrapper);

        //把Page<Dish>转为Page<DishDto>，方便在响应数据中设置分类名称。（不拷贝records，因为需要重新设置数据）
        Page<DishDto> dishDtoPage = new Page<>();
        BeanUtils.copyProperties(dishPage,dishDtoPage,"records");

        //取出Page<Dish>的records 和 new出Page<DishDto>的records（dtoRecords）
        List<Dish> records = dishPage.getRecords();
        List<DishDto> dtoRecords = new ArrayList<>();

        for (Dish dish : records){
            //new出要往dtoRecords中添加的对象
            DishDto dishDto = new DishDto();
            //把records 中的 dish对象的数据 拷贝到 dtoRecords 中的 dishDto对象
            BeanUtils.copyProperties(dish,dishDto);
            //根据dish对象的CategoryId（分类id）属性 查询到具体的 category（类别）对象
            Category category = categoryService.getById(dish.getCategoryId());
            //再根据 category（类别）对象的name值设置dishDto中CategoryName的值
            dishDto.setCategoryName(category.getName());
            //保存到new出的records（dtoRecords）
            dtoRecords.add(dishDto);
        }

        dishDtoPage.setRecords(dtoRecords);

        return R.success(dishDtoPage);
    }

    @GetMapping("/{id}")
    public R<DishDto> getDishOne(@PathVariable Long id){
        Dish dish = dishService.getById(id);

        Category category = categoryService.getById(dish.getCategoryId());

        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> flavorList = dishFlavorService.list(dishFlavorLambdaQueryWrapper);

        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);

        dishDto.setCategoryName(category.getName());
        dishDto.setFlavors(flavorList);

        return R.success(dishDto);
    }

    @DeleteMapping
    @CacheEvict(value = "userCategoryDish", allEntries = true)
    public R<String> delete(Long[] ids){
        dishService.removeByIds(Arrays.asList(ids));

//        //构造redis的key
//        Set<Object> keys = redisTemplate.keys("dish_*");
//        //清理redis缓存，保证redis缓存数据和mysql数据库中的数据一致性
//        redisTemplate.delete(keys);

        return R.success("cg");
    }

    @PostMapping("status/{status}")
    @CacheEvict(value = "userCategoryDish", allEntries = true)
    public R<String> updateStatus(Long[] ids,@PathVariable int status){
        dishService.updateStatusByIds(status,ids);

//        //构造redis的key
//        Set<Object> keys = redisTemplate.keys("dish_*");
//        //清理redis缓存，保证redis缓存数据和mysql数据库中的数据一致性
//        redisTemplate.delete(keys);

        return R.success("cg");
    }

    @PutMapping
    @CacheEvict(value = "userCategoryDish", key = "#dishDto.getCategoryId()")
    public R<String> update(@RequestBody DishDto dishDto){
        dishService.updateWithFlavor(dishDto);

//        //构造redis的key
//        String key = "dish_"+dishDto.getCategoryId()+"_1";
//        //清理redis缓存，保证redis缓存数据和mysql数据库中的数据一致性
//        redisTemplate.delete(key);

        return R.success("保存成功");
    }

    @GetMapping("/list")
    @Cacheable(value = "userCategoryDish",key = "#dish.getCategoryId()")
    public R<List<DishDto>> getDishList(Dish dish){
        List<DishDto> dishDtoList = null;

//        //构造redis的key
//        String key = "dish_"+dish.getCategoryId()+"_"+dish.getStatus();

        //先从redis中查询数据，如果有直接返回数据
//        dishDtoList = (List<DishDto>) redisTemplate.opsForValue().get(key);
//
//        if (dishDtoList != null){
//            //存在，直接返回，不需要查询数据库
//            return R.success(dishDtoList);
//        }

        //构造查询条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null ,Dish::getCategoryId,dish.getCategoryId());
        //添加条件，查询状态为1（起售状态）的菜品
        queryWrapper.eq(Dish::getStatus,1);

        //添加排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(queryWrapper);

        dishDtoList = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item,dishDto);

            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);

            if(category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }

            //当前菜品的id
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DishFlavor::getDishId,dishId);
            //SQL:select * from dish_flavor where dish_id = ?
            List<DishFlavor> dishFlavorList = dishFlavorService.list(lambdaQueryWrapper);
            dishDto.setFlavors(dishFlavorList);
            return dishDto;
        }).collect(Collectors.toList());

//        //不存在时要把查询到的数据存到redis中
//        redisTemplate.opsForValue().set(key,dishDtoList,30, TimeUnit.MINUTES);

        return R.success(dishDtoList);
    }

}

