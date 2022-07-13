package com.zyp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyp.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    void updateStatusByIds(Integer status,Long[] ids);
    void removeWithDish(List<Long> ids);
}
