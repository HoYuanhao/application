<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input :placeholder="$t('taskconsole.name')" v-model="listQuery.title" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter"/>
    <el-input :placeholder="$t('taskconsole.description')" v-model="listQuery.description" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter"/>
      <el-select v-model="listQuery.type" :placeholder="$t('table.type')" clearable class="filter-item" style="width: 130px">
        <el-option v-for="item in calendarTypeOptions" :key="item.key" :label="item.display_name" :value="item.key"/>
      </el-select>
      <el-select v-model="listQuery.status" style="width: 140px" class="filter-item" @change="handleFilter">
        <el-option v-for="item in statusSelect" :key="item.key" :label="item.label" :value="item.key"/>
      </el-select>
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">{{ $t('table.search') }}</el-button>
      <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="handleCreate">{{ $t('taskconsole.add') }}</el-button>
      <!-- <el-button v-waves :loading="downloadLoading" class="filter-item" type="primary" icon="el-icon-download" @click="handleDownload">{{ $t('table.export') }}</el-button> -->
      <el-checkbox v-model="showReviewer" class="filter-item" style="margin-left:15px;" @change="tableKey=tableKey+1">{{ $t('taskconsole.running') }}</el-checkbox>
    </div>

    <el-table
      v-loading="listLoading"
      :key="tableKey"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;"
      @sort-change="sortChange">
      <el-table-column :label="$t('taskconsole.id')" prop="id" sortable="custom" align="center" width="100">
        <template slot-scope="scope">
          <span>{{ scope.row.tid }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('taskconsole.name')" width="100px" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.name}}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('taskconsole.description')" min-width="150px">
          <template slot-scope="scope">
          <span>{{ scope.row.describe}}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('taskconsole.processNum')" width="70px" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.processNum }}</span>
        </template>
      </el-table-column>
      <el-table-column  :label="$t('taskconsole.createTime')" width="110px" align="center">
        <template slot-scope="scope">
             <span>{{ scope.row.createTime|dateFormat}}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('taskconsole.startTime')" width="80px">
        <template slot-scope="scope">
          <span>{{ scope.row.startTime|dateFormat}}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('taskconsole.endTime')" align="center" width="95">
        <template slot-scope="scope">
          <span>{{ scope.row.endTime|dateFormat}}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('taskconsole.status')" class-name="status-col" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status | statusFilter">{{ scope.row.status|statusNameFilter}}</el-tag>
        </template>
      </el-table-column>
      
      <el-table-column :label="$t('taskconsole.type')" class-name="status-col" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.type|typeStatusFilter">{{ scope.row.type}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('taskconsole.action')" align="center" width="150" class-name="small-padding fixed-width">
        <template slot-scope="scope">
           <el-button v-if="scope.row.status==0||scope.row.status==3||scope.row.status==4" size="mini" type="success" @click="handleModifyStatus(scope.row,'published')">{{ $t('taskconsole.run') }}</el-button>
          <el-button v-if="scope.row.status==1||scope.row.status==2" size="mini" type="info" @click="handleModifyStatus(scope.row,'published')">{{ $t('taskconsole.stop') }}</el-button>
          <el-button type="danger" size="mini" @click="handleUpdate(scope.row)">{{ $t('taskconsole.delete') }}</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="right" label-width="100px" style="width: 400px; margin-left:0px;">
        <el-form-item :label="$t('taskconsole.type')" prop="type">
          <el-select v-model="temp.type" class="filter-item" placeholder="任务类型">
            <el-option v-for="item in calendarTypeOptions" :key="item.key" :label="item.display_name" :value="item.key"/>
          </el-select>
        </el-form-item>

        <el-form-item :label="$t('taskconsole.name')" prop="name">
          <el-input v-model="temp.name" placeholder="输入任务名称"/>
        </el-form-item>

        <el-form-item :label="$t('taskconsole.processNum')" prop="processNum">
      <el-input-number v-model="temp.processNum" :min="1" :max="32" label="进程数"></el-input-number>
        </el-form-item>
 <el-form-item :label="$t('taskconsole.alarm')">  
                        <el-switch
                            on-text ="是"
                            off-text = "否"
                            on-color="#5B7BFA"
                            off-color="#dadde5"
                            v-model="temp.check" 
                            @change="alarmChange"
                            >
                        </el-switch>
                  
     </el-form-item>    
        <el-form-item :label="$t('taskconsole.description')">
          <el-input :autosize="{ minRows: 2, maxRows: 4}" v-model="temp.remark" type="textarea" placeholder="Please input"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">{{ $t('table.cancel') }}</el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">{{ $t('table.confirm') }}</el-button>
      </div>
    </el-dialog>

    <el-dialog :visible.sync="dialogPvVisible" title="Reading statistics">
      <el-table :data="pvData" border fit highlight-current-row style="width: 100%">
        <el-table-column prop="key" label="Channel"/>
        <el-table-column prop="pv" label="Pv"/>
      </el-table>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="dialogPvVisible = false">{{ $t('table.confirm') }}</el-button>
      </span>
    </el-dialog>

  </div>
</template>

<script>
import { fetchList, fetchPv, createArticle, updateArticle } from '@/api/article'
import waves from '@/directive/waves' // Waves directive
import { formatTime } from '@/utils'
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination
import{showTaskData} from '@/api/api'
import store from '@/store'
const calendarTypeOptions = [
  { key: 'MS', display_name: 'MUSIC' },
  { key: 'TK', display_name: 'TICKET' },
]

// arr to obj ,such as { CN : "China", US : "USA" }
const calendarTypeKeyValue = calendarTypeOptions.reduce((acc, cur) => {
  acc[cur.key] = cur.display_name
  return acc
}, {})

export default {
  name: 'ComplexTable',
  components: { Pagination },
  directives: { waves },
  filters: {
      dateFormat(stamp) {
        if(stamp==null||stamp==''){
          return '未开始'
        }
      return formatTime(stamp)
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
      alarm:false,
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        importance: undefined,
        title: undefined,
        type: undefined,
        status: ''
      },
      importanceOptions: [1, 2, 3],
      calendarTypeOptions,
      statusSelect: [
        { label: 'Running', key: 'run' },
         { label: 'Error', key: 'error' },
         {label:'Stop',key:'stop'}, 
         { label: 'Exception', key: 'exception' },
           { label: 'Finish', key: 'finish' }],
         
      statusOptions: ['published', 'draft', 'deleted'],
      showReviewer: false,
      temp: {
        id: undefined,
        importance: 1,
        remark: '',
        timestamp: new Date(),
        title: '',
        type: '',
        status: 'published'
      },
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
        const param={
          id:store.getters.id,
          token:store.getters.token,
          limit:this.listQuery.limit,
          page:this.listQuery.page
        }
          showTaskData(param).then(res=>{
           this.list=res.data.tasks
           this.total=res.data.total
             this.listLoading = false
          })
    
    },
    alarmChange(){
        if(this.alarm==false){
        this.alarm=true;
         this.$message({
              type: 'success',
              message: `邮件告警已启用！若未设置邮箱地址，设置将失效。`
            });
        }else{
          this.alarm=false;
           this.$message({
              type: 'success',
              message: `邮件告警已关闭！`
            });
        }
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleModifyStatus(row, status) {
      this.$message({
        message: '操作成功',
        type: 'success'
      })
      row.status = status
    },
    sortChange(data) {
      const { prop, order } = data
      if (prop === 'id') {
        this.sortByID(order)
      }
    },
    sortByID(order) {
      if (order === 'ascending') {
        this.listQuery.sort = '+id'
      } else {
        this.listQuery.sort = '-id'
      }
      this.handleFilter()
    },
    resetTemp() {
      this.temp = {
        id: undefined,
        importance: 1,
        remark: '',
        timestamp: new Date(),
        title: '',
        status: 'published',
        type: ''
      }
    },
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.temp.id = parseInt(Math.random() * 100) + 1024 // mock a id
          this.temp.author = 'vue-element-admin'
          createArticle(this.temp).then(() => {
            this.list.unshift(this.temp)
            this.dialogFormVisible = false
            this.$notify({
              title: '成功',
              message: '创建成功',
              type: 'success',
              duration: 2000
            })
          })
        }
      })
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
    handleFetchPv(pv) {
      fetchPv(pv).then(response => {
        this.pvData = response.data.pvData
        this.dialogPvVisible = true
      })
    },
    handleDownload() {
      this.downloadLoading = true
      import('@/vendor/Export2Excel').then(excel => {
        const tHeader = ['timestamp', 'title', 'type', 'importance', 'status']
        const filterVal = ['timestamp', 'title', 'type', 'importance', 'status']
        const data = this.formatJson(filterVal, this.list)
        excel.export_json_to_excel({
          header: tHeader,
          data,
          filename: 'table-list'
        })
        this.downloadLoading = false
      })
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
