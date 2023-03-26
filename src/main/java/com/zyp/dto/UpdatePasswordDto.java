package com.zyp.dto;

import lombok.Data;

@Data
public class UpdatePasswordDto {
    private String thisPassword;
    private String newPassword1;
    private String newPassword2;
    private String id;
}
