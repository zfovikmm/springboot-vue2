<template>
  <el-menu :default-openeds="['1', '3']" style="min-height: 100vh;overflow-x: hidden"
           background-color="rgb(48,64,86)"
           text-color="#fff"
           router
  >
    <div style="height: 60px;line-height: 60px;text-align: center">
      <img src="../assets/logo.png" alt="" style="width: 20px;position: relative;top: 5px;margin-right: 5px">
      <b style="color: white">后台管理系统</b>
    </div>
    <el-menu-item index="/">
      <template slot="title">
        <i class="el-icon-house"></i>
        <span slot="title">主页</span>
      </template>
    </el-menu-item>
    <el-submenu index="/" >
      <template slot="title"><i class="el-icon-menu"></i>系统管理</template>
      <el-menu-item index="/user" >
        <i class="el-icon-s-custom"></i>
        <template slot="title" >用户管理</template>
      </el-menu-item>
    </el-submenu>
  </el-menu>
</template>

<script>
export default {
  name: "Aside",
  props: {
    msg: String
  }
}
</script>

<style scoped>

</style>
