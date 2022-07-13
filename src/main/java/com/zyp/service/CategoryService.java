package com.zyp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyp.entity.Category;

public interface CategoryService extends IService<Category> {
    void remove(Long id);
}
