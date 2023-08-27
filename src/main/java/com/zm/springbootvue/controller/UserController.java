package com.zm.springbootvue.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.zm.springbootvue.Service.UserService;
import com.zm.springbootvue.controller.UserDTO.UserDTO;
import com.zm.springbootvue.entity.User;
import com.zm.springbootvue.mapper.UserMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @PostMapping
    public Integer save(@RequestBody User user){
        //新增或者更新
        //初始化密码
        if( user.getPassword() == null){
            user.setPassword("123456");
        }
        return userService.save(user);
    }

    //查询所有数据
    @GetMapping("/")
    public List<User> findAll(){
        List<User> all = userMapper.findAll();
        return all;
    }

    //@PathVariable 表示url必须传入id
    @DeleteMapping("/{id}")
    public Integer delete(@PathVariable Integer id){
      return userMapper.deleteById(id);
    }

    /*
    @RequestParam 接收 ?pageNum=1&pageSize=10 模糊查询
     */
    @GetMapping("/page")
    public Map<String,Object> findPage(@RequestParam Integer pageNum,@RequestParam Integer pageSize,@RequestParam(required = false) Integer id,@RequestParam String username,@RequestParam String nickname){
        pageNum = (pageNum-1) * pageSize;
        List<User> data = userMapper.selectPage(pageNum, pageSize,id,username,nickname);
        Integer total = userMapper.selectTotal(id,username,nickname);
        Map<String,Object> res = new HashMap<>();
        res.put("data",data);
        res.put("total",total);
        return res;
    }

    @GetMapping("/All")
    public Map<String,Object> findAll(@RequestParam Integer pageNum,@RequestParam Integer pageSize){
        pageNum = (pageNum-1) * pageSize;
        List<User> data = userMapper.selectAll(pageNum, pageSize);
        Integer total = userMapper.selectCount();
        Map<String,Object> res = new HashMap<>();
        res.put("data",data);
        res.put("total",total);
        return res;
    }

    //批量删除
    @PostMapping ("del/batch")
    public Boolean deleteBatch(@RequestBody List<Integer> ids){
        if(!ids.isEmpty()){
            return userMapper.deleteBatch(ids);
        }else {
            return false;
        }
    }

    //导出
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception{
        //从数据库查询出所有的数据
        List<User> list = userService.list();
        //通过工具类创建writer 写出到磁盘路径

        //内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
//        writer.addHeaderAlias("username","用户名");
//        writer.addHeaderAlias("password","密码");
//        writer.addHeaderAlias("nickname","昵称");
//        writer.addHeaderAlias("email","邮箱");
//        writer.addHeaderAlias("phone","电话");
//        writer.addHeaderAlias("address","地址");
//        writer.addHeaderAlias("createTime","创建时间");
//        writer.addHeaderAlias("avatarUrl","头像");

        //一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list,true);

        //设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String filename = URLEncoder.encode("用户信息","UTF-8");
        response.setHeader("Content-Disposition","attachment;filename="+filename+".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out,true);
        out.close();
        writer.close();
    }
    //导入
    @PostMapping("/import")
    public Boolean imp(MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        List<User> list = reader.readAll(User.class);
        //System.out.println(list);
        userService.saveBatch(list);
        return true;
    }

    //登录 @RequestBody前端传的json变为java对象
    @PostMapping("/login")
    public boolean login(@RequestBody User user){
        String username = user.getUsername();
        String password = user.getPassword();
        if(StrUtil.isBlank(username) || StrUtil.isBlank(password)){
            return false;
        }
        return userService.login(user);
    }

//    @PostMapping("/login")
//    public String login(@RequestBody String username,@RequestBody String password){
//        User user = userService.LoginIn(username,password);
//        System.out.println(user);
//        if(user != null){
//            return "success";
//        }else {
//            return "error";
//        }
//    }
}
