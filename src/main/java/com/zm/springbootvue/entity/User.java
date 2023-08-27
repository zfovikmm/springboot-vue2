package com.zm.springbootvue.entity;


import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_user")
@ApiModel(value="user对象",description="")
public class User implements Serializable {

  private static final long serialVersionUID =1L;

  @Alias("ID")
    @TableId(value="id",type = IdType.AUTO)
  private Integer id;
  @Alias("用户名")
  private String username;
 // @JsonIgnore    //此注解将不显示密码

  @Alias("密码")
  private String password;

  @Alias("昵称")
  private String nickname;

  @Alias("邮箱")
  private String email;

  @Alias("电话")
  private String phone;

  @Alias("地址")
  private String address;

  @Alias("创建时间")
  private LocalDateTime createTime;
}
