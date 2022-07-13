package com.zyp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyp.entity.ShoppingCart;
import com.zyp.mapper.ShoppingCartMapper;
import com.zyp.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
