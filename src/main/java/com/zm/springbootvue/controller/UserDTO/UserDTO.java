package com.zm.springbootvue.controller.UserDTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

/*
接收前端接受请求的注释
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String Username;
    private String password;
}
