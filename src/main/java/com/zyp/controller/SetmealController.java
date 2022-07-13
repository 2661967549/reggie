package com.zyp.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyp.common.R;
import com.zyp.dto.SetmealDto;
import com.zyp.entity.Category;
import com.zyp.entity.Setmeal;
import com.zyp.entity.SetmealDish;
import com.zyp.service.CategoryService;
import com.zyp.service.SetmealDishService;
import com.zyp.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 套餐管理
 */
@RestController
@RequestMapping("/setmeal")
@Slf4j          // http://localhost:8080/setmeal/list?categoryId=1413386191767674881&status=1
public class SetmealController {

    @Autowired
    SetmealService setmealService;

    @Autowired
    SetmealDishService setmealDishService;

    @Autowired
    CategoryService categoryService;

    @GetMapping("/list")
    @Cacheable(value = "userCategorySetmeal",key = "#categoryId")
    public R<List<Setmeal>> getSetmealListById(@RequestParam Long categoryId, Integer status){
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Setmeal::getCategoryId, categoryId);
        lambdaQueryWrapper.eq(Setmeal::getStatus,status);
        List<Setmeal> list = setmealService.list(lambdaQueryWrapper);
        return R.success(list);
    }

    @GetMapping("/page")
    public R<Page<SetmealDto>> setemealList(int page,int pageSize,String name){
        Page<Setmeal> setmealPage = new Page<>(page,pageSize);
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.like(StringUtils.isNotEmpty(name),Setmeal::getName,name);
        setmealLambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);

        setmealService.page(setmealPage,setmealLambdaQueryWrapper);

        Page<SetmealDto> setmealDtoPage = new Page<>();
        BeanUtils.copyProperties(setmealPage,setmealDtoPage,"records");

        ArrayList<SetmealDto> setmealDtos = new ArrayList<>();

        List<Setmeal> records = setmealPage.getRecords();
        for (Setmeal setmeal : records){
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(setmeal,setmealDto);

            Category category = categoryService.getById(setmeal.getCategoryId());
            setmealDto.setCategoryName(category.getName());
            setmealDtos.add(setmealDto);
        }

        setmealDtoPage.setRecords(setmealDtos);
        return R.success(setmealDtoPage);
    }

    @PostMapping
    @Transactional
    @CacheEvict(value = "userCategorySetmeal",allEntries = true)
    public R<String> save(@RequestBody SetmealDto setmealDto){
        setmealService.save(setmealDto);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes){
            setmealDish.setSetmealId(setmealDto.getId());
        }
        setmealDishService.saveBatch(setmealDishes);
        return R.success("cg");
    }

    @PostMapping("/status/{status}")
    @CacheEvict(value = "userCategorySetmeal",allEntries = true)
    public R<String> updateStatus(Long[] ids, @PathVariable Integer status){
        setmealService.updateStatusByIds(status,ids);
        return R.success("cg");
    }

    @GetMapping("/{id}")
    public R<SetmealDto> getSetmealDtoById(@PathVariable Long id){
        Setmeal setmeal = setmealService.getById(id);

        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.eq(SetmealDish::getSetmealId,id);
        List<SetmealDish> list = setmealDishService.list(setmealDishLambdaQueryWrapper);

        Category category = categoryService.getById(setmeal.getCategoryId());

        SetmealDto setmealDto = new SetmealDto();
        setmealDto.setCategoryName(category.getName());
        setmealDto.setSetmealDishes(list);
        BeanUtils.copyProperties(setmeal,setmealDto);
        return R.success(setmealDto);
    }

    @PutMapping
    @Transactional
    @CacheEvict(value = "userCategorySetmeal",allEntries = true)
    public R<String> update(@RequestBody SetmealDto setmealDto){
        setmealService.updateById(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes){
            setmealDish.setSetmealId(setmealDto.getId());
        }

        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,setmealDto.getId());
        setmealDishService.remove(queryWrapper);
        setmealDishService.saveBatch(setmealDishes);

        return R.success("cg");
    }

    @DeleteMapping
    @CacheEvict(value = "userCategorySetmeal",allEntries = true)
    public R<String> deleteByIds(@RequestParam List<Long> ids){
        setmealService.removeWithDish(ids);
        return R.success("cg");
    }

}
