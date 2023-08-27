<template>
  <div>
    <el-container style="height: 100vh; ">
      <el-aside width="200px" style="background-color: rgb(238, 241, 246)">
        <Aside></Aside>
      </el-aside>

      <el-container>
        <el-header style="font-size: 20px;position: relative;top: 20px " >
          <Header></Header>
        </el-header>

        <el-main>
<!--          表示当前的子路由会在 router-view里面展示-->
          <router-view/>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>

import Aside from "@/components/Aside";
import Header from "@/components/Header";

//export主要用于对外输出本模块变量的接口，一个文件就可以被理解为一个模块。export就是导出
export default {
//定义组件需要使用components选项。components是一个对象，该对象的属性是组件的相关配置信息。
  components: {
    Aside,
    Header
  }
};
</script>

