<template>
  <div>
<!--          <div style="margin-bottom: 30px">-->
<!--            <el-breadcrumb separator="/">-->
<!--              <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>-->
<!--              <el-breadcrumb-item>用户管理</el-breadcrumb-item>-->
<!--            </el-breadcrumb>-->
<!--          </div>-->
    <!--          搜索框-->
    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="请输入ID" suffix-icon="el-icon-s-custom" class="ml-5" v-model="id"></el-input>
      <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="username" class="ml-5"></el-input>

      <el-input style="width: 200px" placeholder="请输入昵称" suffix-icon="el-icon-search" class="ml-5" v-model="nickname"></el-input>
      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
    </div>
    <!--          操作-->
    <div style="margin: 10px 0">
      <el-button type="primary" @click="handleAdd">新增 <i class="el-icon-circle-plus-outline"></i> </el-button>
      <el-button type="danger" @click="delBatch">批量删除 <i class="el-icon-remove-outline"></i> </el-button>
      <el-upload action="http://localhost:9090/user/import" :show-file-list="false" accept="xlsx" :on-success="handelExcelImportSuccess" :on-error="handelExcelImportError" style="display: inline-block">
        <el-button type="primary" class="ml-5">导入 <i class="el-icon-bottom"></i> </el-button>
      </el-upload>
      <el-button type="primary" @click="exp" class="ml-5">导出 <i class="el-icon-top"></i> </el-button>
    </div>

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
    <!--          分页查询-->
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


    <!-- Form -->
    <!--          新增,编辑窗口-->
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
  </div>
</template>

<script>

export default {
  name: "User",
  data(){
    return{
      tableData:[],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      id: "",
      username: "",
      email: "",
      address: "",
      nickname: "",
      dialogFormVisible: false,
      form: {},
      multipleSelection: []
    }
  },
  created() {
    this.loadAll()
  },
  methods: {
      //请求分页查询数据,模糊查询
      load(){
        this.request.get("/user/page",{
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
            id: this.id,
            username: this.username,
            nickname: this.nickname
          }
        }).then(res =>{
          console.log(res)

          this.tableData= res.data
          this.total=res.total
        })
      },
      loadAll(){
        this.request.get("/user/All",{
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
          }
        }).then(res =>{
          console.log(res)

          this.tableData= res.data
          this.total=res.total
        })
      },
      handleSizeChange(pageSize){
        this.pageSize = pageSize
        this.load()
      },
      handleCurrentChange(pageNum){
        this.pageNum=pageNum
        this.load()
      },
      //新增弹出框
      handleAdd(){
        this.dialogFormVisible = true
        this.form = {}
      },
      //更改
      handleEdit(row){
        this.form = row
        this.dialogFormVisible=true
      },
      //取消更新
      handleEditCancel(){
        this.form = {}
        this.dialogFormVisible = false
        this.loadAll()
      },
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
      //新增方法
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
      //批量删除
      handleSelectionChange(val){
        console.log(val)
        this.multipleSelection = val
      },
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
      //导出
      exp(){
        window.open("http://localhost:9090/user/export")
      },
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

  }
}
</script>

<style scoped>

</style>
