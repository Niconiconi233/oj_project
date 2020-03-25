package com.yw.ojproject.bo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum AdminType {
    REGULAR_USER(0, "Regular User"),
    ADMIN(1, "Admin"),
    SUPER_ADMIN(2, "Super Admin");

    private int code;
    private String desc;

    public static AdminType getAdminTypeEnumByCode(Integer code){
        for(AdminType type : AdminType.values()){
            if(code.equals(type.getCode())){
                return type;
            }
        }
        return null;
    }
}
