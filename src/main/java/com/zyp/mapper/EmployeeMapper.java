package com.zyp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zyp.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
