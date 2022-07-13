package com.zyp.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

//    @JsonSerialize(using = ToStringSerializer.class)   改用在WebMvcConfig中配置json对象映射器解决问题 com.zyp.common.JacksonObjectMapper
    private Long id;

    private String username;

    private String name;

    private String password;

    private String phone;

    private String sex;

    private String idNumber; //身份证号码

    private Integer status;

    @TableField(fill = FieldFill.INSERT)  //插入时填充字段  （mybatisPlus提供的自动填充注解）处理器类为MyMetaObjectHandler
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)  //插入和更新时填入字段
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)  //插入时填充字段
    private Long createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)  //插入和更新时填入字段
    private Long updateUser;

}
