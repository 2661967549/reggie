package com.zyp.dto;

import com.zyp.entity.Setmeal;
import com.zyp.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;

}
