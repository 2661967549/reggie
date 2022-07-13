package com.zyp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyp.entity.AddressBook;
import com.zyp.mapper.AddressBookMapper;
import com.zyp.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
