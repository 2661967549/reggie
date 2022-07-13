package com.zyp.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zyp.dto.OrdersDto;
import com.zyp.entity.Orders;

public interface OrderService extends IService<Orders> {

    /**
     * 用户下单
     * @param orders
     */
    public void submit(Orders orders);

    /**
     * 用户分页查询订单数据
     */
    Page<OrdersDto> ordersUserPage(int page, int pageSize);
}
