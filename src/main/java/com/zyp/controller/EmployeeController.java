package com.zyp.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyp.common.BaseContext;
import com.zyp.common.R;
import com.zyp.dto.UpdatePasswordDto;
import com.zyp.entity.Employee;
import com.zyp.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 后台员工管理系统
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 登录
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        //1.将页面提交的用户名username进行md5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //2.将页面提交的拥护名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        //3.如果没有查询到则返回登录失败结果
        if (emp == null){
            return R.error("用户名不存在，登录失败");
        }

        //4.密码对比，不一致返回登录失败结果
        if (!emp.getPassword().equals(password)){
            return R.error("密码错误,登录失败");
        }
        //5.查看员工状态，如果已经为禁用状态，则返回员已经禁用结果
        if (emp.getStatus() == 0){
            return R.error("该账号已被禁用");
        }
        //6.登录成功，将员工id存入Session并返回成功结果
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }

    /**
     * 退出登录
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        //1.清理Session中的用户id
        request.getSession().removeAttribute("employee");
        return R.success("退出登录");
    }

    /**
     * 新增员工
     * @param employee
     * @return
     */
    @PostMapping()
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee){

        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        employeeService.save(employee);
        return R.success("添加成功");
    }

    /**
     * 修改密码
     * @param dto
     * @return
     */
    @PostMapping("/updatePassword")
    public R<String> updatePassword(HttpServletRequest request, @RequestBody UpdatePasswordDto dto){
        Employee employee = employeeService.getById(Long.valueOf(dto.getId()));
        if (employee == null){
            return R.error("用户名不存在，修改失败");
        }

        if (BaseContext.getCurrentId() == 1L){ //admin用户直接修改密码
            if (dto.getNewPassword1() != null && dto.getNewPassword1().length() > 5) {
                String password = DigestUtils.md5DigestAsHex(dto.getNewPassword1().getBytes());
                employee.setPassword(password);
                employeeService.updateById(employee);
                return R.success("修改成功");
            }else {
                return R.error("请输入至少6位数的密码");
            }
        }

        if (dto.getNewPassword1() != null && dto.getNewPassword1().equals(dto.getNewPassword2())){
            if (dto.getNewPassword1().length() > 5) {
                String password = DigestUtils.md5DigestAsHex(dto.getNewPassword1().getBytes());
                if (!employee.getPassword().equals(password)){
                    return R.error("旧密码错误,修改失败");
                }
                employee.setPassword(password);
                employeeService.updateById(employee);
                return R.error("修改成功");
            }else {
                return R.error("密码至少6位");
            }
        }else {
            return R.error("两次输入的新密码不相同");
        }
    }

    /**
     * 分页展示员工数据
     */
    @GetMapping("/page")
    public R<Page<Employee>> listEmp(HttpServletRequest request){
        String page = request.getParameter("page");
        String pageSize = request.getParameter("pageSize");
        String name = request.getParameter("name");

        Page<Employee> employeePage = new Page<>();
        employeePage.setCurrent(Long.parseLong(page));
        employeePage.setSize(Long.parseLong(pageSize));

        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<Employee>();
        //queryWrapper.like(name != null && !"".equals(name),Employee::getName,name);
        queryWrapper.like(StringUtils.isNotEmpty(name) ,Employee::getName,name);
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        employeeService.page(employeePage, queryWrapper);

        return R.success(employeePage);
    }

    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable String id){
        Employee emp = employeeService.getById(Long.parseLong(id));
        return R.success(emp);
    }

    @PutMapping()
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee){
//        Long uid = (Long) request.getSession().getAttribute("employee");
//        employee.setUpdateUser(uid);
//        employee.setUpdateTime(LocalDateTime.now());
        employeeService.updateById(employee);

        return R.success("修改成功");
    }

}

























