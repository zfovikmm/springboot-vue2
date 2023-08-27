<template>
  <div>
  <div style="display: inline-block">
    <el-breadcrumb separator="/" style="margin-left: 0px;font-size: 20px;display: inline-block">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>{{this.lists}}</el-breadcrumb-item>
    </el-breadcrumb>
  </div>

<!--      <el-breadcrumb-item v-for="(item,index) in lists" :key="item.path">-->
<!--        <router-link :to="item.path">{{item.name.title}}</router-link>-->
<!--      </el-breadcrumb-item>-->


    <!--          用户-->
    <el-dropdown style="width: 70px;cursor: pointer;margin-left: 1200px;font-size: 20px">
      <span>Glimpse</span>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item>个人信息</el-dropdown-item>
        <el-dropdown-item>
          <router-link to="/login" style="text-decoration: none">退出</router-link>
        </el-dropdown-item>

      </el-dropdown-menu>
    </el-dropdown>
    <!--          分割线-->
    <el-divider content-position="left"></el-divider>
  </div>
</template>

<script>
export default {
  name: "Header",
  data(){
    return{
      lists: []        //定义一个数组 用于接收路由信息
    }
  },
  methods: {
  },
  created() {
    console.log(this.$route.matched)
    this.lists = this.$route.name //获取路由信息

  },
  //这里必须使用监听，否则无法实现获取路由变动信息
  //监听后路由会实时变动，不然需要手动刷新路径才会改变
  watch:{
    $route(to,from){
      console.log(to)
      this.lists = to.name
    }
  }
  // computed: {
  //   currentPathName (){
  //     return this.$store.state.currentPathName; //需要监听的数据
  //   }
  // },
  // watch: {
  //   currentPathName (newVal, oldVal){
  //     console.log(newVal)
  //   }
  // }
}
</script>

<style scoped>

</style>
