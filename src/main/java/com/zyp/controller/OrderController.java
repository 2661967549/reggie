package com.zyp.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyp.common.BaseContext;
import com.zyp.common.R;
import com.zyp.dto.OrdersDto;
import com.zyp.entity.OrderDetail;
import com.zyp.entity.Orders;
import com.zyp.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * 订单
 */
@Slf4j
@RestController
@RequestMapping("/aip/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 用户下单
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        log.info("订单数据：{}",orders);
        orderService.submit(orders);
        return R.success("下单成功");
    }

    @GetMapping("/userPage")
    public R<Page<OrdersDto>> UserPage(@RequestParam int page, int pageSize){
        Page<OrdersDto> ordersDtoPage = orderService.ordersUserPage(page, pageSize);
        return R.success(ordersDtoPage);
    }

    @GetMapping("/page")
    public R<Page<Orders>> ordersPage(@RequestParam int page, int pageSize,
                                      Long number,
                                      String beginTime,
                                      String endTime){

        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(number != null,Orders::getNumber,number);
        queryWrapper.ge(beginTime != null,Orders::getOrderTime,beginTime);
        queryWrapper.le(endTime != null,Orders::getOrderTime,endTime);
        queryWrapper.orderByDesc(Orders::getOrderTime);
        Page<Orders> page1 = new Page<>(page,pageSize);
        orderService.page(page1,queryWrapper);
        return R.success(page1);
    }


    @PutMapping()
    public R<String> status(@RequestBody Orders orders){
        orderService.updateById(orders);
        return R.success("cg");
    }
}