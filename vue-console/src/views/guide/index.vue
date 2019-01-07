<template>
  <section>
      <el-form :inline="true" class="search-form"  >
          <el-row style="top:30px;">
            <el-col :span="4">
             <el-form-item>
        <div class="hits" >
          {{ hits }} {{$t('guide.record')}}
        </div>
         </el-form-item>
         </el-col>
                <el-col :span="6" >
             <el-form-item :label="$t('guide.taskID')">
        <el-select
          v-model="search.taskID"
          :disabled="showEditBtn"
          :placeholder="$t('guide.taskID')"
          @change="getExceptionType"
          >
          <el-option v-for="taskID in taskIDs" :key="taskID" :label="taskID" :value="taskID" />
        </el-select>
         </el-form-item>
              </el-col>
              <el-col :span="5" >
             <el-form-item :label="$t('guide.type')">
        <el-select
          v-model="search.type"
          :disabled="showEditBtn"
          :placeholder="$t('guide.type')">
          <el-option v-for="item in objectNames" :key="item" :label="item" :value="item" />
        </el-select>
         </el-form-item>
              </el-col>
                 <el-col :span="5" >
             <el-form-item :label="$t('guide.key')">
        <el-input
          v-model="search.keyword"
          class="search"
          :placeholder="$t('guide.key')"/>
               </el-form-item>
              </el-col>
                 <el-col :span="2" >
             <el-form-item >
        <el-button id="search-submit" type="primary" icon="search" @keyup.enter.native="exceptionSearch" @click="exceptionSearch">{{$t('guide.search')}}</el-button>
 </el-form-item>
              </el-col>
    </el-row>
      </el-form>
   

    <!--列表-->
    <template>
      <el-table
        :data="results"
        highlight-current-row
        style="width: 100%;">
        <el-table-column prop="value" :label="$t('guide.result')">
          <template scope="scope">
            <json-viewer :value="scope.row.value" :expand-depth="5" copyable boxed sort/>
          </template>
        </el-table-column>
      </el-table>
    </template>
    <!--分页-->
    <el-col :span="25" class="toolbar" style="padding-bottom:10px;">
      <el-pagination
        :current-page="table.page"
        :page-sizes="[10, 25, 50, 100]"
        :page-size="table.size"
        :total="table.total"
        layout="total, sizes, prev, pager, next, jumper"
        style="float:right"
        @current-change="currentPageChanged"
        @size-change="sizeChanged"/>
    </el-col>
  </section>
</template>

<script>
import {getTaskIDs,getExceptionTypes,exceptionSearchS} from '../../api/api'
import store from '@/store'
export default {
  filters: {},
  data() {
    return {
      taskIDs:[],
      showEditBtn: false,
      objectNames: [],
      search: {
        type: '',
        taskID: '',
        keyword: '',
      },
      pageData:{
          pageSize:10,
          pageIndex:1
      },
      table: {
        page: 1,
        size: 10,
        total: 0,
        pagination: {},
        data: []
      },
      form: {
        visible: false,
        loading: false,
        data: {
          value: ''
        }
      },
      inputVisible: false,
      inputValue: '',
      hits: 0,
      timeout: null
    }
  },
  computed: {
 
    results: function() {
      let self = this, data = self.table.data, page = self.table.page, size = self.table.size
      self.table.total = data.length
      data = data.slice((page - 1) * size, page * size)
      return data
    }

  },
  mounted() {

    this.bodyListener = (e) => {
      if (e.keyCode === 13) {
        document.getElementById('search-submit').click()
      }
    }
    document.body.addEventListener('keyup', this.bodyListener, false)
  },
  beforeDestroy() {
    document.body.removeEventListener('keyup', this.bodyListener)
  },

  mounted: function() {
     this.initTaskIDs();
  },

  methods: {
      getExceptionType(){
        this.search.type='';
        this.objectNames=[];
        const param={
          id:store.getters.id,
          token:store.getters.token,
          tid:this.search.taskID
        }
        getExceptionTypes(param).then(res=>{
           this.objectNames.push('')
           const data=res.data;
           for(var i=0;i<data.length;i++){
               this.objectNames.push(data[i])
           }
         
        })
      },
      currentPageChanged(page) {
      this.pageData.pageIndex = page
      this.hubbleSearch()
    },
    sizeChanged(size) {
      this.pageData.pageSize = size
      this.hubbleSearch()
    },
    exceptionSearch() {
      const param = { 
        key: this.search.keyword, 
        tid: this.search.taskID,
         type: this.search.type,
         token:store.getters.token,
         id:store.getters.id,
         offset:(this.pageData.pageIndex-1)*(this.pageData.pageSize),
         limit:this.pageData.pageSize 
         }
      const r = []
      exceptionSearchS(param).then((res) => {
        if (res.data) {
          this.hits = res.data['totalHits']
          for (let i = 0; i < res.data['exceptions'].length; i++) {
            const odj = {
              'value': res.data['exceptions'][i]
            }
            r.push(odj)
          }
        }
      })
      this.table.data = r
    },
    initPage() {
      this.search.objName = ''
      if (this.objectNames.length != 0) {
        this.objectNames = []
      }
      const param = { tenantId: this.search.tenantId }
      initPage(param).then((res) => {
        if (res.data) {
          for (let i = 0; i < res.data.length; i++) {
            const obj = {
              'key': res.data[i],
              'value': res.data[i]
            }
            this.objectNames.push(obj)
          }
        }
      })
    },
        initTaskIDs(){
          const parm={
            id:store.getters.id,
            token:store.getters.token,

          }
      getTaskIDs(parm).then(res=>{
        this.taskIDs=res.data;
      })
    },
    querySearchAsync(queryString, cb) {
      const param = { w: queryString }
      const result = []
      getTips(param).then((res) => {
        if (res.data) {
          for (let i = 0; i < res.data.length; i++) {
            const odj = {
              'value': res.data[i]
            }
            result.push(odj)
          }
        }
        cb(result)
      })
    },
    handleSelect(item) {
      console.log(item)
    }
  }
}

</script>

<style scoped>

.tenantId {
    width: 100%;
    padding-bottom: 10px;
    padding-left: 5%;

  }
  .search {
    width: 100%;
    padding-bottom: 10px;
    padding-left: 5%;
  }

  .hits {
    position: relative;
    left: 20px;
    font-size: 30px;
    width: 100%;
    color: #58B7FF;
  }
</style>
