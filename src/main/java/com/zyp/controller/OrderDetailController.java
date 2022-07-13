package com.zyp.controller;

import com.zyp.service.OrderDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 订单明细
 */
@Slf4j
@RestController
@RequestMapping("/aip/orderDetail")
public class OrderDetailController {

    @Autowired
    private OrderDetailService orderDetailService;

}