# springboot-vue2
springboot+vue2 后台管理系统 主要功能代码详解

## 1、前端框架 

vue2 - element-ui

下载

````
npm i element-ui -S
npm install --save element-ui
````

引入

```
import Vue from 'vue'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';

Vue.use(ElementUI,{size: "small"});
```

## 2、路由

首先在main.js引入 router包下的index.js文件

```
import router from './router'
```

分成多个页面组合

```js
//主页面
  {
    path: '/',
    name: 'Manage',
    component: () => import('../views/Manage.vue'),
    redirect: "/home",
    children: [
      {path: 'user', name: '用户管理', component: () => import('../views/User.vue')},
      {path: 'home', name: '首页', component: () => import('../views/Home.vue')}
    ]
  },
```

```js
//登录页面
  {
    path: '/login',
    name: 'Login',
    component: ()=> import('../views/Login')
  }
```

## 3、解决前端跨域问题

创建config包，创建CorsConfig类

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    // 当前跨域请求最大有效时长。这里默认1天
    private static final long MAX_AGE = 24 * 60 * 60;

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*"); // 1 设置访问源地址
        corsConfiguration.addAllowedHeader("*"); // 2 设置访问源请求头
        corsConfiguration.addAllowedMethod("*"); // 3 设置访问源请求方法
        corsConfiguration.setMaxAge(MAX_AGE);
        source.registerCorsConfiguration("/**", corsConfiguration); // 4 对接口配置跨域设置
        return new CorsFilter(source);
    }
}
```



## 4、Manage.vue

- 导入Aside和Header组件文件

```
import Aside from "@/components/Aside";
import Header from "@/components/Header";
```

- 注册组件

```vue
//定义组件需要使用components选项。components是一个对象，该对象的属性是组件的相关配置信息。
  components: {
    Aside,
    Header
  }
```

- 应用组件

```vue
 //边框
 <el-aside width="200px" style="background-color: rgb(238, 241, 246)">
    <Aside></Aside>
  </el-aside>
```

```vue
//头部
    <el-header style="font-size: 20px;position: relative;top: 20px " >
      <Header></Header>
    </el-header>
```

```vue
//主题
        <el-main>
<!--          表示当前的子路由会在 router-view里面展示-->
<!--router-view 当你的路由path 与访问的地址相符时，会将指定的组件替换该 router-view-->
          <router-view/>
        </el-main>
```

在路由中，我指定Manage.vue的子节点了

```
    children: [
      {path: 'user', name: '用户管理', component: () => import('../views/User.vue')},
      {path: 'home', name: '首页', component: () => import('../views/Home.vue')}
    ]
```

当我点击侧边的用户管理时

```
      <el-menu-item index="/user" >
        <i class="el-icon-s-custom"></i>
        <template slot="title" >用户管理</template>
      </el-menu-item>
```

会跳转到 index="/user"地址

因为 访问地址为 http://localhost:8080/user 与 在router包中的index.js设置的路由一样 就是如下

```js
      {path: 'user', name: '用户管理', component: () => import('../views/User.vue')},
```

所以User.vue组件会直接替换router-view

## 5、增加功能

按钮

```
<el-button type="primary" @click="handleAdd">新增 <i class="el-icon-circle-plus-outline"></i> </el-button>
```

handleAdd方法，这个是弹出窗口

```
      //新增弹出框
      handleAdd(){
        this.dialogFormVisible = true
        this.form = {}
      },
```

新增弹窗

```
<el-dialog title="用户信息" :visible.sync="dialogFormVisible">
      <el-form label-width="120px">
        <el-form-item label="用户名" >
          <el-input v-model="form.username" autocomplete="off" aria-required="true"></el-input>
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="form.nickname" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="电话">
          <el-input v-model="form.phone" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="form.address" autocomplete="off"></el-input>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="handleEditCancel">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
```

save方法,真正的新增方法

```
save() {
  this.request.post("/user", this.form).then(res => {   // 发送POST请求将表单数据保存到服务器端
    if (res) {   // 如果返回结果不为空
      this.$message.success("保存成功")   // 显示保存成功的消息提示
      this.dialogFormVisible = false   // 关闭表单对话框
      this.loadAll()   // 刷新页面数据，重新加载所有数据（根据实际情况可能是重新查询、重新加载列表等）
    } else {   // 如果返回结果为空
      this.$message.error("保存失败")   // 显示保存失败的消息提示
      this.dialogFormVisible = false   // 关闭表单对话框
    }
  })
},
```

```
this.request：假设request是一个封装了HTTP请求的工具类或库。通过使用this.request.post，我们可以执行一个HTTP POST请求。

"/user"：这是向服务器发送POST请求的URL。该请求将被发送到/user路径下，可能是一个API接口或某个特定的服务器端点。

this.form：这是将作为请求的主体发送给服务器的表单数据。它是通过this.form属性来访问的，具体内容可能包括用户在表单中输入的字段信息。(就是save方法中的form表单)

.then(res => {})：这是Promise机制的语法，表示当POST请求成功响应后，执行相应的操作。res是服务器响应的结果。
```

```
在给定的代码中，res是一个表示服务器响应的变量。

在这段代码中，通过使用this.request.post方法发送POST请求，并在响应中使用.then方法处理结果。在Promise的then回调函数中，res代表服务器返回的响应结果。具体的响应内容取决于服务器端的实现。

通常情况下，服务器端会返回一些数据，比如保存成功的提示、保存失败的提示或其他相关信息。根据实际的后端实现，res可能是一个包含响应数据的对象，也可能是其他类型的数据或错误对象。

在代码的逻辑中，根据res的存在与否，分别执行了保存成功和保存失败的代码块。

你可以根据实际情况查看后端接口的定义和文档，以了解服务器返回的具体结构和内容。
```

经过this.request.post("/user", this.form)后，访问Springboot中controller层的save方法

```java
    @PostMapping
    public Integer save(@RequestBody User user){
        //新增或者更新
        return userService.save(user);
    }
```

service层的save方法

```
    public int save(User user){
        if(user.getId() == null){
           return userMapper.insert(user);
        }else{
           return userMapper.update(user);
        }
    }
```

UserMapper的SQL语句

```
    @Insert("INSERT INTO sys_user(username,password,nickname,email,phone,address) values (#{username},#{password},#{nickname},#{email},#{phone},#{address})")
    int insert(User user);
```

## 6、删除功能

删除按钮

```
        <template v-slot="scope">
          <el-button type="success" @click="handleEdit(scope.row)">编辑 <i class="el-icon-edit"></i></el-button>
          <el-button type="danger" @click="open(scope.row.id)">删除 <i class="el-icon-remove-outline"></i></el-button>
        </template>
```

v-slot="scope",它用于在 Vue 组件中定义插槽，并将数据传递给插槽内容。 `scope.row.id`，其表示传递给插槽的 `row` 对象中的 `id` 属性。

@click="open(scope.row.id)"，这是按钮的点击事件绑定，即当按钮被点击时，触发名为 `open` 的方法，并传递 `scope.row.id` 作为参数

row：在 Vue.js 中，`row` 是一个常见的命名，用于表示表格中的一行数据。它通常用于循环渲染表格的每一行，并将每一行的数据绑定到 `row` 变量上。在 `<el-button>` 组件的点击事件处理函数中，可以访问 `row` 对象的属性，如 `row.id`，来获取当前行的唯一标识符（ID）。根据具体的业务需求，你可以使用该 ID 或其他属性来执行相关的操作，比如编辑或删除该行数据。

open方法,删除弹窗

```
 //删除弹窗
open(id) {
this.$confirm('此操作将永久删除该用户, 是否继续?', '提示', {
  confirmButtonText: '确定',
  cancelButtonText: '取消',
  type: 'warning'
}).then(() => {
  this.del(id)
}).catch(() => {
  this.$message({
    type: 'info',
    message: '已取消删除'
  });
});
},
```

```
首先，通过 this.$confirm 方法弹出一个确认对话框，询问用户是否要继续删除操作。对话框中显示的文本内容如下：

提示信息为：‘此操作将永久删除该用户，是否继续?’
确认按钮的文本为：‘确定’
取消按钮的文本为：‘取消’
对话框的类型为警告 (‘warning’)
如果用户点击确认按钮 (then 分支)，则会调用 this.del(id) 方法执行删除操作，并将 id 参数传递给 del 方法进行删除。

如果用户点击取消按钮 (catch 分支)，则会显示一个信息提示框 (this.$message)，提示用户已取消删除操作，提示框的类型为 ‘info’，消息内容为 ‘已取消删除’。
```

del，删除功能

```
//删除功能
del(id){
this.request.delete("/user/"+id).then(res =>{
  if(res){
    this.$message.success("删除成功")
    this.loadAll()
  }else {
    this.$message.error("删除失败")
    this.loadAll()
  }
})
},
```

经过this.request.delete("/user/"+id)会访问到Springboot的delete方法

```java
@DeleteMapping("/{id}")
public Integer delete(@PathVariable Integer id){
  return userMapper.deleteById(id);
}
```

UserMapper的SQL语句

```
@Delete("delete from sys_user where id = #{id}")
Integer deleteById(@Param("id") Integer id);
```

## 7、分页查询

定义变量

```
data(){
    return{
      tableData:[],        // 通过后台查询到的数据，保存到tableData数组中
      total: 0,			   // 存储数据的总条数
      pageNum: 1,		   //当前页码
      pageSize: 10,		   //表示每页显示的数据条数
      username: "",
      email: "",
      address: "",
      nickname: "",
      dialogFormVisible: false,
      form: {},
      multipleSelection: []
    }
  },
```

数据显示页面

```
    <el-table :data="tableData" border stripe @selection-change="handleSelectionChange">
      <!--            批量删除复选框，配合@selection-change="handleSelectionChange"事件删除-->
      <el-table-column
          type="selection"
          width="55">
      </el-table-column>
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="username" label="用户名" width="140"></el-table-column>
      <el-table-column prop="nickname" label="昵称" width="120"></el-table-column>
      <el-table-column prop="email" label="邮箱"></el-table-column>
      <el-table-column prop="phone" label="电话"></el-table-column>
      <el-table-column prop="address" label="地址"></el-table-column>
      <el-table-column label="操作">
        <template v-slot="scope">
          <el-button type="success" @click="handleEdit(scope.row)">编辑 <i class="el-icon-edit"></i></el-button>
          <el-button type="danger" @click="open(scope.row.id)">删除 <i class="el-icon-remove-outline"></i></el-button>
        </template>
      </el-table-column>
    </el-table>
```

- `:data="tableData"` 将表格的数据源绑定到 `tableData` 变量，这个变量可能是从后端获取的数据。
- `border` 属性用于添加表格边框。
- `stripe` 属性用于为表格的每一行添加斑马纹样式。
- `@selection-change="handleSelectionChange"` 是一个事件监听器，当选择项发生变化时会触发 `handleSelectionChange` 方法。
- `<el-table-column type="selection" width="55"></el-table-column>` 创建了一个包含复选框的列，用于进行批量操作。
- `prop` 属性用于指定表格列要渲染的数据字段。它与表格数据源中的字段名相对应，将数据源中的数据绑定到对应的列上。
- `<el-table-column prop="id" label="ID" width="80"></el-table-column>` 定义了一个属性为 “id” 的列，用于显示 ID。
- `<el-table-column prop="username" label="用户名" width="140"></el-table-column>` 定义了一个属性为 “username” 的列，用于显示用户名。
- `<el-table-column prop="nickname" label="昵称" width="120"></el-table-column>` 定义了一个属性为 “nickname” 的列，用于显示昵称。
- `<el-table-column prop="email" label="邮箱"></el-table-column>` 定义了一个属性为 “email” 的列，用于显示邮箱。
- `<el-table-column prop="phone" label="电话"></el-table-column>` 定义了一个属性为 “phone” 的列，用于显示电话号码。
- `<el-table-column prop="address" label="地址"></el-table-column>` 定义了一个属性为 “address” 的列，用于显示地址。

底下分页查询栏

```
<div style="padding: 10px 0">
  <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="pageNum"
      :page-sizes="[2,5,10,20]"
      :page-size="pageSize"
      layout="total,sizes,prev, pager, next, jumper"
      :total="total">
  </el-pagination>
</div>
```

- `<div style="padding: 10px 0">` 创建一个包含样式为上下 10 像素间距的 `<div>` 元素。
- `<el-pagination>` 是 Element UI 提供的分页组件，用于展示分页导航栏。
- `@size-change="handleSizeChange"` 是一个事件监听器，当页面大小改变时会触发 `handleSizeChange` 方法。
- `@current-change="handleCurrentChange"` 是一个事件监听器，当当前页码改变时会触发 `handleCurrentChange` 方法。
- `:current-page="pageNum"` 绑定了 `pageNum` 变量作为当前页码的值。
- `:page-sizes="[2,5,10,20]"` 定义了可选的页面大小选项，可以选择每页显示的数据数量。
- `:page-size="pageSize"` 绑定了 `pageSize` 变量作为每页显示数据的数量。
- `layout="total,sizes,prev, pager, next, jumper"` 定义了分页导航栏的布局，包含了总条数、页面大小选择、上一页、页码、下一页和快速跳转输入框。
- `:total="total"` 绑定了 `total` 变量作为总条数的值，用于显示总共有多少条数据。

handleSizeChange，handleCurrentChange，当页码更改时，更新页面

```
handleSizeChange(pageSize){
this.pageSize = pageSize
this.loadAll()
},
handleCurrentChange(pageNum){
this.pageNum=pageNum
this.loadAll()
},
```

loadAll()方法

```
loadAll(){
this.request.get("/user/All",{
  params: {
    pageNum: this.pageNum,
    pageSize: this.pageSize,
    username: this.username,
    email: this.email,
    address: this.address
  }
}).then(res =>{
  console.log(res)

  this.tableData= res.data
  this.total=res.total
})
},
```

- `params: { ... }` 是请求的参数对象。
- `pageNum`：表示当前页码。
- `pageSize`：表示每页显示的数据条数。
- `username`：表示用户名。
- `email`：表示邮箱。
- `address`：表示地址。

- 在请求成功后，使用 `then` 方法来处理返回的响应数据（在这里命名为 `res`）。
- `console.log(res)` 用于将返回的响应数据输出到控制台，以便开发者进行调试和查看数据。
- `this.tableData = res.data` 将返回的数据中的 `data` 字段赋值给 `tableData` 变量，通常用于存储表格组件要显示的数据。
- `this.total = res.total` 将返回的数据中的 `total` 字段赋值给 `total` 变量，通常用于存储数据的总条数，便于分页等相关操作。

经过

```vue
this.request.get("/user/All",{
  params: {
    pageNum: this.pageNum,
    pageSize: this.pageSize,
    username: this.username,
    email: this.email,
    address: this.address
  }
})
```

后。会访问 springboot中user/all接口

```java
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
```

- `@GetMapping("/All")` 注解表示该方法将处理来自 “/All” 路径的 GET 请求。
- `public Map<String,Object> findAll(@RequestParam Integer pageNum,@RequestParam Integer pageSize)` 是该方法的签名。它接受两个请求参数，`pageNum` 和 `pageSize`，用于分页查询。
- `@RequestParam`：将请求参数绑定到你控制器的方法参数上（是springmvc中接收普通参数的注解）
- `pageNum = (pageNum-1) * pageSize` 是为了将传递过来的页码转换为数据库查询所需要的偏移量。
  - 例如，如果 `pageNum` 为 1，`pageSize` 为 10，则计算结果为 0（表示查询结果从数据库中的第一条记录开始），如果 `pageNum` 为 2，则计算结果为 10（表示查询结果从数据库中的第十一条记录开始），依此类推。

- `List<User> data = userMapper.selectAll(pageNum, pageSize)` 通过调用 `userMapper` 的 `selectAll` 方法，传递页码和每页大小参数进行数据库查询，获取用户数据。
- `Integer total = userMapper.selectCount()` 通过调用 `userMapper` 的 `selectCount` 方法获取用户总数。
- `Map<String,Object> res = new HashMap<>()` 创建一个新的 Map 对象用于存储响应数据。
- `res.put("data",data)` 将获取到的用户数据存储到 Map 对象中的 “data” 键下。
- `res.put("total",total)` 将用户总数存储到 Map 对象中的 “total” 键下。
- `return res` 返回组装好的包含用户数据和总数的 Map 对象作为响应。

对应的SQL语句

```java
    @Select("select * from sys_user limit #{pageNum},#{pageSize}")
    List<User> selectAll(@Param("pageNum") Integer pageNum,@Param("pageSize") Integer pageSize);
    
    @Select("select count(*) from sys_user")
    Integer selectCount();
```

返回数据，前端获取数据

```vue
.then(res =>{
          console.log(res)

          this.tableData= res.data
          this.total=res.total
        })
```

## 8、模糊查询

查询框

```
    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="username"></el-input>
      <el-input style="width: 200px" placeholder="请输入邮箱" suffix-icon="el-icon-message" class="ml-5" v-model="email"></el-input>
      <el-input style="width: 200px" placeholder="请输入地址" suffix-icon="el-icon-position" class="ml-5" v-model="address"></el-input>
      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
    </div>
```

load方法

```
	load(){
        this.request.get("/user/page",{
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
            username: this.username,
            email: this.email,
            address: this.address
          }
        }).then(res =>{
          console.log(res)

          this.tableData= res.data
          this.total=res.total
        })
      },
```

 Springboot的page接口

```
    @GetMapping("/page")
    public Map<String,Object> findPage(@RequestParam Integer pageNum,@RequestParam Integer pageSize,@RequestParam String username){
        pageNum = (pageNum-1) * pageSize;
        List<User> data = userMapper.selectPage(pageNum, pageSize,username);
        Integer total = userMapper.selectTotal(username);
        Map<String,Object> res = new HashMap<>();
        res.put("data",data);
        res.put("total",total);
        return res;
    }
```

对应的SQL语句

```
    @Select("select * from sys_user where username like concat('%',#{username},'%') limit #{pageNum},#{pageSize}")
    List<User> selectPage(@Param("pageNum") Integer pageNum,@Param("pageSize") Integer pageSize,@Param("username") String username);

    @Select("select count(*) from sys_user where username like concat('%',#{username},'%')")
    Integer selectTotal(String username);
```

## 9、更改功能

编辑按钮

```
        <template v-slot="scope">
          <el-button type="success" @click="handleEdit(scope.row)">编辑 <i class="el-icon-edit"></i></el-button>
          <el-button type="danger" @click="open(scope.row.id)">删除 <i class="el-icon-remove-outline"></i></el-button>
        </template>
```

handleEdit方法

```
//更改
handleEdit(row){
  this.form = row
  this.dialogFormVisible=true
},
```

编辑弹窗，实际也是新增弹窗

```
    <el-dialog title="用户信息" :visible.sync="dialogFormVisible">
      <el-form label-width="120px">
        <el-form-item label="用户名" >
          <el-input v-model="form.username" autocomplete="off" aria-required="true"></el-input>
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="form.nickname" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="电话">
          <el-input v-model="form.phone" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="form.address" autocomplete="off"></el-input>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="handleEditCancel">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
```

save方法

```
      save(){
        this.request.post("/user",this.form).then(res => { // 发送POST请求将表单数据保存到服务器端
          if(res){
            this.$message.success("保存成功")
            this.dialogFormVisible = false
            this.loadAll()    //刷新 不加也会刷新
          }else {
            this.$message.error("保存失败")
            this.dialogFormVisible = false
          }
        })
      },
```

springboot的controller层

```
@PostMapping
public Integer save(@RequestBody User user){
    //新增或者更新
    return userService.save(user);
}
```

service层save方法

```
public int save(User user){
    if(user.getId() == null){
       return userMapper.insert(user);
    }else{
       return userMapper.update(user);
    }
}
```

SQL语句

```xml
<update id="update">
    update sys_user
    <set>
        <if test="username != null">
            username=#{username},
        </if>
<!--            <if test="password != null">-->
<!--                password=#{password},-->
<!--            </if>-->
        <if test="nickname  != null">
            nickname=#{nickname},
        </if>
        <if test="email != null">
            email=#{email},
        </if>
        <if test="phone != null">
            phone=#{phone},
        </if>
        <if test="address != null">
            address=#{address}
        </if>
    </set>
    <where>
        id=#{id}
    </where>
</update>
```

## 10、批量删除

按钮

```
 <el-button type="danger" @click="delBatch">批量删除 <i class="el-icon-remove-outline"></i> </el-button>
```

复选框

```
<el-table-column
  type="selection"
  width="55">
</el-table-column>
```

在table表格中添加监听事件

```
 <el-table :data="tableData" border stripe @selection-change="handleSelectionChange">
      <!--            批量删除复选框，配合@selection-change="handleSelectionChange"事件删除-->
```

handleSelectionChange方法

```
handleSelectionChange(val){
  console.log(val)
  this.multipleSelection = val
},
```

- 将选中复选框的数据，添加到multipleSelection数组中，此时是[{}]，还需要将id取出

delBatch方法

```
delBatch(){
let ids = this.multipleSelection.map(v => v.id) //[1,2] 将对象数组变成纯id数组
console.log("ids:"+ids)
this.request.post("/user/del/batch",ids).then(res =>{
  if(res){
    this.$message.success("批量删除成功")
    this.loadAll()
  }else {
    this.$message.error("批量删除失败")
    this.loadAll()
  }
})
},
```

- 调用del/batch接口，传入ids数组

```
this.request.post("/user/del/batch",ids).
```

springboot的del/batch接口

```
//批量删除
@PostMapping ("del/batch")
public Boolean deleteBatch(@RequestBody List<Integer> ids){
    if(!ids.isEmpty()){
        return userMapper.deleteBatch(ids);
    }else {
        return false;
    }
}
```

mysql

```
<!--  批量删除-->
<delete id="deleteBatch">
    delete from sys_user where id in
    <foreach item="ids" collection="list" index="index" open="("
             separator="," close=")">
        #{ids}
    </foreach>
</delete>
```

## 11、导出 spring+hutool = excel

按钮

```
      <el-button type="primary" @click="exp" class="ml-5">导出 <i class="el-icon-top"></i> </el-button>
```

exp方法

```
  exp(){
    window.open("http://localhost:9090/user/export")
  },
```

- 调用 `window.open()` 方法，在新窗口中打开http://localhost:9090/user/export

springboot的export接口

```java
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
    
这段代码是一个用于导出用户信息到Excel文件的方法。下面是对代码的解释：

- `@GetMapping("/export")`: 这是一个标注在方法上的Spring MVC注解，表示该方法可以处理HTTP GET请求，并处理对应的"/export"路径。
- `public void export(HttpServletResponse response) throws Exception`: 这是导出方法的定义，它接受一个`HttpServletResponse`对象作为参数，用于向浏览器发送响应。
- `List<User> list = userService.list()`: 从数据库查询出所有的`User`对象的列表。
- `ExcelWriter writer = ExcelUtil.getWriter(true)`: 使用工具类创建一个ExcelWriter对象，用于操作Excel文件。这里设置为内存操作，将数据写出到浏览器。
- 注释掉的代码部分是对Excel表头的自定义别名。如果需要自定义表头别名，可以取消注释并设置相应的别名。
- `writer.write(list, true)`: 将`list`中的对象一次性写出到Excel文件中，使用默认样式，并强制输出标题部分。
- `response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8")`: 设置浏览器响应的内容类型为Excel文件。
- `String filename = URLEncoder.encode("用户信息", "UTF-8")`: 设置导出的Excel文件名，并进行URL编码，确保文件名中的中文字符不会出现乱码。
- `response.setHeader("Content-Disposition", "attachment;filename=" + filename + ".xlsx")`: 设置响应头信息，告诉浏览器以附件形式下载文件，并指定文件名。
- `ServletOutputStream out = response.getOutputStream()`: 获取响应的输出流。
- `writer.flush(out, true)`: 将Excel文件内容写出到响应的输出流中，进行刷新。
- `out.close()`: 关闭输出流。
- `writer.close()`: 关闭ExcelWriter对象，释放资源。
```

完成上面接口需要的工作

- 下载依赖

```xml
<!--hutool-->
<dependency>
    <groupId>cn.hutool</groupId>
    <artifactId>hutool-all</artifactId>
    <version>5.7.20</version>
</dependency>
<!-- https://mvnrepository.com/artifact/org.apache.poi/poi-ooxml -->
<dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi-ooxml</artifactId>
    <version>4.1.2</version>
</dependency>

<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>3.2.0</version>
</dependency>
<dependency>
    <groupId>com.spring4all</groupId>
    <artifactId>spring-boot-starter-swagger</artifactId>
    <version>1.5.1.RELEASE</version>
</dependency>
```

实体类

```java
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
```

- `@Alias` 注解指定了属性名对应的别名。导出导入时，不是数据库中的名
- `@TableName("sys_user")`: 这是 MyBatis-Plus 注解，用于指定实体类对应的数据库表名为 “sys_user”。
- `@ApiModel(value="user对象",description="")`: 这是 Swagger 注解，用于为实体类提供文档和描述信息。

## 12、导入

```
      <el-upload action="http://localhost:9090/user/import" :show-file-list="false" accept="xlsx" :on-success="handelExcelImportSuccess" :on-error="handelExcelImportError" style="display: inline-block">
        <el-button type="primary" class="ml-5">导入 <i class="el-icon-bottom"></i> </el-button>
      </el-upload>
```

- `action="http://localhost:9090/user/import"`: 设置上传文件的目标URL，即文件上传的接口地址。
- `:show-file-list="false"`: 设置是否显示已上传的文件列表。这里设置为`false`，表示不显示文件列表。
- `accept="xlsx"`: 设置接受的文件类型。这里设置为`xlsx`，表示只接受后缀名为`.xlsx`的文件。
- `:on-success="handelExcelImportSuccess"`: 设置上传成功时的回调函数，即文件上传成功后执行的操作。`handelExcelImportSuccess`是回调函数的名称，需要在Vue的组件中定义该函数来处理上传成功的逻辑。
- `:on-error="handelExcelImportError"`: 设置上传失败时的回调函数，即文件上传失败后执行的操作。`handelExcelImportError`是回调函数的名称，需要在Vue的组件中定义该函数来处理上传失败的逻辑。
- `style="display: inline-block"`: 设置上传组件的显示样式，这里设置为`inline-block`，表示以行内块元素的方式显示上传组件。
- `<el-button type="primary" class="ml-5">导入 <i class="el-icon-bottom"></i> </el-button>`: 这是一个Element-UI的el-button组件，用于触发文件上传操作。`type="primary"`表示按钮的主题样式为主要按钮，`class="ml-5"`表示按钮的左外边距为5个单位，`导入`是按钮的文本内容，`<i class="el-icon-bottom"></i>`是一个包含下箭头图标的元素，用于在按钮中显示一个下箭头图标。

import接口

```java
@PostMapping("/import")
public Boolean imp(MultipartFile file) throws IOException {
    InputStream inputStream = file.getInputStream();
    ExcelReader reader = ExcelUtil.getReader(inputStream);
    List<User> list = reader.readAll(User.class);
    //System.out.println(list);
    userService.saveBatch(list);
    return true;
}

@PostMapping("/import"): 这是一个使用Spring的@PostMapping注解标记的接口，表示这是一个处理POST请求的接口，路径为"/import"。当客户端发送一个POST请求到该路径时，将会执行imp方法进行处理。

public Boolean imp(MultipartFile file) throws IOException: 这是一个名为imp的方法。它接受一个类型为MultipartFile的参数file，该参数用于接收上传的文件数据。MultipartFile是Spring框架提供的用于处理文件上传的类。

InputStream inputStream = file.getInputStream(): 通过getInputStream()方法获取上传文件的输入流。

ExcelReader reader = ExcelUtil.getReader(inputStream): 使用一个名为ExcelUtil的工具类，通过输入流创建一个ExcelReader对象。这个工具类可能是自定义的或第三方库，它提供了一些用于处理Excel文件的方法。

List<User> list = reader.readAll(User.class): 通过ExcelReader对象的readAll()方法，将Excel文件数据读取为一个名为User的类的对象列表。这里假设User是一个自定义的类，表示导入的用户数据。

userService.saveBatch(list): 调用一个名为userService的服务类的saveBatch()方法，将读取的用户数据批量保存到数据库中。这里假设userService是一个自定义的服务类，用于处理与用户相关的业务逻辑。

return true: 返回一个布尔值true，表示文件导入操作成功。
```

导入成功失败弹窗

```js
  //提示导入成功弹窗
  handelExcelImportSuccess(){
    this.$message.success("上传成功")
    this.loadAll()
  },
  //提示导入失败窗口
  handelExcelImportError(){
    this.$message.success("上传失败")
    this.loadAll()
  }
```

## 13、面包屑导航

导航栏

```
  <div style="display: inline-block">
    <el-breadcrumb separator="/" style="margin-left: 0px;font-size: 20px;display: inline-block">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>{{this.lists}}</el-breadcrumb-item>
    </el-breadcrumb>
  </div>
```

变量

```
  data(){
    return{
      lists: []        //定义一个数组 用于接收路由信息
    }
  },
```

获取路由信息

```
  created() {
    console.log(this.$route.matched)
    this.lists = this.$route.name //获取路由信息

  },
```

监听

```
  //这里必须使用监听，否则无法实现获取路由变动信息
  //监听后路由会实时变动，不然需要手动刷新路径才会改变
  watch:{
    $route(to,from){
      console.log(to)
      this.lists = to.name
    }
  }
```

## 14、实现登录

登录界面

```javascript
<template>
  <div class="wrapper">
    <div style="margin: 200px auto;background-color: #fff;width: 350px;height: 300px;padding: 20px;border-radius: 10px">
      <div style="margin: 20px 0;text-align: center;font-size: 24px"><b>登录</b></div>
      <el-form :model="user" :rules="rules" ref="userForm">
        <el-form-item prop="username">
          <el-input  size="medium" style="margin: 10px 0" prefix-icon="el-icon-user" v-model="user.username"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input  size="medium" style="margin: 10px 0" prefix-icon="el-icon-lock" show-password v-model="user.password"></el-input>
        </el-form-item>

        <div style="margin: 10px 0;text-align: right">
          <el-button type="primary" size="small" autocomplete="off" @click="login">登录</el-button>
          <el-button type="primary" size="small" autocomplete="off">注册</el-button>
        </div>
      </el-form>

    </div>
  </div>
</template>

<style>
  .wrapper {
    height: 100vh;
    background-image: linear-gradient(to bottom right,#FC466B,#3F5EFB);
    overflow: hidden;
  }
</style>
```

添加路由

```js
  {
    path: '/login',
    name: 'Login',
    component: ()=> import('../views/Login')
  }
```

登录方法 login

```javascript
methods: {
    login(){
      this.$refs['userForm'].validate((valid) => {
        if (valid) {  //表单校验合法，如果不符合输入规则，不会上传到后端校验
          this.request.post("/user/login",this.user).then(res => {
            if(!res){
              this.$message.error("用户名或者密码错误")
            }else {
              this.$router.push("/")
            }
          })
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    }
  }
```

data

```javascript
  name: "Login",
  data(){
    return{
      user:{},
      rules: {
          username:[  //trigger: 'blur'失焦时会触发
            {required: true,message:"请输入用户名",trigger: 'blur'},
            {min:1,max:15,message: '长度在1到15个字符串',trigger: 'blur'}
          ],
          password:[  //trigger: 'blur'失焦时会触发
          {required: true,message:"请输入密码",trigger: 'blur'},
          {min:1,max:15,message: '长度在1到15个字符串',trigger: 'blur'}
          ]
      }
    }
  },
```

后台实现

controller层

```java
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
```

service层

```java
    public boolean login(User user) {
        User userload = userMapper.getInfo(user.getUsername(),user.getPassword());
        return userload != null;
    }
```

mapper层

```java
    //登录
    @Select("select * from sys_user WHERE username = #{username} and password = #{password}")
    User getInfo(@Param("username") String username,@Param("password") String password);
```

## 15、注册

按钮

```
 <el-button type="primary" size="small" autocomplete="off" @click="handlEnroll">注册</el-button>
```

handlEnroll方法

```
handlEnroll(){
  this.dialogFormVisible = true
  this.form = {}
},
```

注册接口

```
	<el-dialog title="用户信息" :visible.sync="dialogFormVisible">
        <el-form label-width="120px">
          <el-form-item label="用户名" >
            <el-input v-model="form.username" autocomplete="off" aria-required="true"></el-input>
          </el-form-item>
          <el-form-item label="密码">
            <el-input v-model="form.password" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="昵称">
            <el-input v-model="form.nickname" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="邮箱">
            <el-input v-model="form.email" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="电话">
            <el-input v-model="form.phone" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="地址">
            <el-input v-model="form.address" autocomplete="off"></el-input>
          </el-form-item>

        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="handleEditCancel">取 消</el-button>
          <el-button type="primary" @click="save">确 定</el-button>
        </div>
      </el-dialog>
```

handleEditCancel方法

```
    handleEditCancel(){
      this.dialogFormVisible = false
      this.form = {}
    },
```

save方法

```
	save(){
      this.request.post("/user",this.form).then(res => { // 发送POST请求将表单数据保存到服务器端
        if(res){
          this.$message.success("注册成功")
          this.dialogFormVisible = false
        }else {
          this.$message.error("注册失败")
          this.dialogFormVisible = false
        }
      })
    },
```





功能介绍



