<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input :placeholder="$t('singer.singerName')" v-model="listQuery.singerName" style="width: 200px;" class="filter-item" @keyup.enter.native="search"/>
    <el-input :placeholder="$t('singer.singerDesc')" v-model="listQuery.singerDesc" style="width: 200px;" class="filter-item" @keyup.enter.native="search"/>
 <el-input :placeholder="$t('singer.singerId')" v-model="listQuery.singerId" style="width: 200px;" class="filter-item" @keyup.enter.native="search"/>
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="search">{{ $t('table.search') }}</el-button>
    </div>
    <el-table
      v-loading="listLoading"
      :key="tableKey"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;">
      <el-table-column :label="$t('singer.singerId')" prop="id" sortable="custom" align="center" width="100">
        <template slot-scope="scope">
          <span>{{ scope.row.singerId }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('singer.singerName')" width="100px" align="center">
        <template slot-scope="scope">
          <span> {{ scope.row.singerName}}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('singer.singerDesc')" min-width="400px" align="center">
          <template slot-scope="scope">
            <span class="link-type">{{ scope.row.singerDesc}}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('singer.singerHref')" width="200px" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.singerHref}}</span>
        </template>
      </el-table-column>
      <el-table-column  :label="$t('singer.getTime')" width="100px" align="center">
        <template slot-scope="scope">
             <span>{{ scope.row.getTime|dateFormat}}</span>
        </template>
      </el-table-column>
    </el-table>
<el-pagination v-show="total>0" :total="total" :page-sizes="[20, 30, 50, 100]" :current-page="listQuery.page" :page-size="listQuery.limit" 
   @current-change="handleCurrentChange" 
   @size-change="handleSizeChange"
   layout="total, sizes, prev, pager, next, jumper"/>
  </div>
  
</template>

<script>
import { fetchList, fetchPv, createArticle, updateArticle } from '@/api/article'
import waves from '@/directive/waves' // Waves directive
import { formatTime } from '@/utils'
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination
import{searchSinger,getSingerLimit} from '@/api/api'
import store from '@/store'
import JsonEditor from '@/components/JsonEditor'


// arr to obj ,such as { CN : "China", US : "USA" }

export default {
  name: 'ComplexTable',
  type:undefined,
  components: { Pagination ,JsonEditor},
  directives: { waves },

  filters: {
      dateFormat(stamp) {
      return formatTime(stamp)
    },
     alarmFilter(status) {
      const statusMap = {
        0: '关闭',
        1: '启用',
      }
      return statusMap[status]
    },
     alarmStatusFilter(status) {
      const statusMap = {
        0: 'error',
        1: 'success',
      }
      return statusMap[status]
    },
    statusFilter(status) {
      const statusMap = {
        0: 'info',
        1: 'success',
        2: 'danger',
        3: 'warning',
        4: 'error'
      }
      return statusMap[status]
    },
      statusNameFilter(status) {
      const statusMap = {
        0: 'Stop',
        1: 'Running',
        2: 'Exception',
        3: 'Error',
        4: 'Finish'
      }
      return statusMap[status]
    },
     typeStatusFilter(status) {
      const statusMap = {
        TICKET: 'success',
        MUSIC: 'error',
      }
      return statusMap[status]
    },
    typeFilter(type) {
      return calendarTypeKeyValue[type]
    }
  },

  data() {
    return {
      isSearch:false,
      loading:false,
      alarm:false,
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: true,
      listQuery: {
        id:store.getters.id,
        page: 1,
        limit: 20,
        singerDesc: undefined,
        singerName: undefined,
        singerId: undefined,
        token:store.getters.token,
      },
      showrun:false,
      importanceOptions: [1, 2, 3],

      statusSelect: [
        { label: 'Running', key: '1' },
         { label: 'Error', key: '3' },
         {label:'Stop',key:'0'}, 
         { label: 'Exception', key: '2' },
           { label: 'Finish', key: '4' }],
         
      statusOptions: ['published', 'draft', 'deleted'],
      showReviewer: false,
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: 'Edit',
        create: '新建任务'
      },
      dialogPvVisible: false,
      pvData: [],
      rules: {
        type: [{ required: true, message: '必须选择类型', trigger: 'change' }],
        name: [{ required: true, message: '必须输入名称', trigger: 'change' }],
        processNum: [{ required: true,type:'number', message: '必须输入进程数,切必须为数字',trigger: 'change' }]
      },
      downloadLoading: false
    }
  },
  created() {
    this.getList()
  },
  methods: {
  handleClick(){
  this.num = this.num.replace('/[^\w]/g', '');
},

    getList() {
      this.listLoading = true
      if(this.isSearch){
        this.listQuery.page=1
        this.search()
      }else{
        const param={
          id:store.getters.id,
          token:store.getters.token,
          limit:this.listQuery.limit,
          page:this.listQuery.page,
        }
          getSingerLimit(param).then(res=>{
           this.list=res.data.singers
           this.total=res.data.count
           this.listLoading = false
          })
          }
    
    },
    search() {
      this.isSearch=true
      const param=this.listQuery
     searchSinger(param).then(res=>{
        this.list=res.data.singers
        this.total=res.data.count
         this.listLoading = false
     })
    },
  
   
    handleModifyStatus(row, status) {
      this.$message({
        message: '操作成功',
        type: 'success'
      })
      row.status = status
    }, 
    handleUpdate(row) {
      this.temp = Object.assign({}, row) // copy obj
      this.temp.timestamp = new Date(this.temp.timestamp)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
 handleSizeChange(val) {
        this.listQuery.limit=val
        this.getList()
      },
      handleCurrentChange(val) {
        this.listQuery.page=val
        this.getList()
  
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.temp)
          tempData.timestamp = +new Date(tempData.timestamp) // change Thu Nov 30 2017 16:41:05 GMT+0800 (CST) to 1512031311464
          updateArticle(tempData).then(() => {
            for (const v of this.list) {
              if (v.id === this.temp.id) {
                const index = this.list.indexOf(v)
                this.list.splice(index, 1, this.temp)
                break
              }
            }
            this.dialogFormVisible = false
            this.$notify({
              title: '成功',
              message: '更新成功',
              type: 'success',
              duration: 2000
            })
          })
        }
      })
    },
    handleDelete(row) {
      this.$notify({
        title: '成功',
        message: '删除成功',
        type: 'success',
        duration: 2000
      })
      const index = this.list.indexOf(row)
      this.list.splice(index, 1)
    },

    formatJson(filterVal, jsonData) {
      return jsonData.map(v => filterVal.map(j => {
        if (j === 'timestamp') {
          return parseTime(v[j])
        } else {
          return v[j]
        }
      }))
    }
  }
}
</script>
<style scoped>
.editor-container{
  position: relative;
  height: 100%;
  
}
</style>

