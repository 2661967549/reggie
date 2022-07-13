package com.zyp.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyp.common.R;
import com.zyp.entity.Category;
import com.zyp.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @GetMapping("/page")
    public R<Page<Category>> listCategory(HttpServletRequest request){
        String page = request.getParameter("page");
        String pageSize = request.getParameter("pageSize");

        Page<Category> categoryPage = new Page<>();
        categoryPage.setSize(Long.parseLong(pageSize));
        categoryPage.setCurrent(Long.parseLong(page));

        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Category::getSort);
        categoryService.page(categoryPage,queryWrapper);
        return R.success(categoryPage);
    }

    @GetMapping("/list")
    public R<List<Category>> listType(Integer type){
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(type != null,Category::getType,type);
        List<Category> list = categoryService.list(queryWrapper);

        return R.success(list);
    }

    @PostMapping()
    public R<String> save( @RequestBody Category category){
        categoryService.save(category);
        return R.success("添加成功");
    }

    @PutMapping()
    public R<String> update(@RequestBody Category category){
        categoryService.updateById(category);

        return R.success("修改成功");
    }

    @DeleteMapping()
    public R<String> delete(Long id){
        categoryService.remove(id);
        return R.success("删除成功");
    }
}
