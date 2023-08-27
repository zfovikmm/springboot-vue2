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
          <el-button type="primary" size="small" autocomplete="off" @click="handlEnroll">注册</el-button>
        </div>
      </el-form>

<!--      注册窗口-->
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

    </div>
  </div>
</template>

<script>
export default {
  name: "Login",
  data(){
    return{
      user: {},
      form: {},
      id: "",
      username: "",
      password: "",
      email: "",
      address: "",
      nickname: "",
      dialogFormVisible: false,
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
    },
    handlEnroll(){
      this.dialogFormVisible = true
      this.form = {}
    },
    handleEditCancel(){
      this.dialogFormVisible = false
      this.form = {}
    },
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
  }
}
</script>

<style>
  .wrapper {
    height: 100vh;
    background-image: linear-gradient(to bottom right,#FC466B,#3F5EFB);
    overflow: hidden;
  }
</style>
