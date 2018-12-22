<template>
  <el-row :gutter="40" class="panel-group">
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel" @click="handleSetLineChartData('datas')">
        <div class="card-panel-icon-wrapper icon-datas">
          <svg-icon icon-class="documentation" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">Datas</div>
          <count-to :start-val="0" :end-val="datas.end" :duration="datas.duration" class="card-panel-num"/>
        </div>
      </div>
    </el-col>
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel" @click="handleSetLineChartData('exceptions')">
        <div class="card-panel-icon-wrapper icon-exceptions">
          <svg-icon icon-class="bug" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">Exceptions</div>
          <count-to :start-val="0" :end-val="exceptions.end" :duration="exceptions.duration" class="card-panel-num"/>
        </div>
      </div>
    </el-col>
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel" @click="handleSetLineChartData('processes')">
        <div class="card-panel-icon-wrapper icon-processes">
          <svg-icon icon-class="tree" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">Processes</div>
          <count-to :start-val="0" :end-val="processes.end" :duration="processes.duration" class="card-panel-num"/>
        </div>
      </div>
    </el-col>
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel" @click="handleSetLineChartData('tasks')">
        <div class="card-panel-icon-wrapper icon-tasks">
          <svg-icon icon-class="example" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">Tasks
          </div>
          <count-to :start-val="0" :end-val="tasks.end" :duration="tasks.duration" class="card-panel-num"/>
        </div>
      </div>
    </el-col>
  </el-row>
</template>

<script>
import CountTo from 'vue-count-to'
import {showLineChartData,showPanelGroupData} from '@/api/api'
import store from '@/store'
export default {
  components: {
    CountTo
  },
   data() {
    return {
      datas:{
        end:0,
        duration:4000,
      },
       exceptions:{
        end:0,
        duration:2600,
      },
       processes:{
        end:0,
        duration:2100,
      },
       tasks:{
        end:0,
        duration:3000,
      }
  
    }
  },
  created(){
    this.init();
  },
  methods: {
    handleSetLineChartData(type) {
      const param={
        type:type,
        token:store.getters.token,
        id:store.getters.id
      }
      showLineChartData(param).then(res=>{
        const data={
          type:type,
          data:res.data
        }
    this.$emit('handleSetLineChartData',data)
      })
      
    },
    init(){ 
    const param={
        token:store.getters.token,
        id:store.getters.id
      }
showPanelGroupData(param).then(res=>{
  this.datas=res.data.datas
   this.exceptions=res.data.exceptions
    this.processes=res.data.processes
     this.tasks=res.data.tasks
    
})
    }

  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.panel-group {
  margin-top: 18px;
  .card-panel-col{
    margin-bottom: 32px;
  }
  .card-panel {
    height: 108px;
    cursor: pointer;
    font-size: 12px;
    position: relative;
    overflow: hidden;
    color: #666;
    background: #fff;
    box-shadow: 4px 4px 40px rgba(0, 0, 0, .05);
    border-color: rgba(0, 0, 0, .05);
    &:hover {
      .card-panel-icon-wrapper {
        color: #fff;
      }
      .icon-people {
         background: #40c9c6;
      }
      .icon-exceptions {
        background: #36a3f7;
      }
      .icon-processes {
        background: #f4516c;
      }
      .icon-tasks {
        background: #34bfa3
      }
    }
    .icon-datas {
      color: #40c9c6;
    }
    .icon-exceptions {
      color: #f73636;
    }
    .icon-processes {
      color: #92f451;
    }
    .icon-tasks {
      color: #4234bf
    }
    .card-panel-icon-wrapper {
      float: left;
      margin: 14px 0 0 14px;
      padding: 16px;
      transition: all 0.38s ease-out;
      border-radius: 6px;
    }
    .card-panel-icon {
      float: left;
      font-size: 48px;
    }
    .card-panel-description {
      float: right;
      font-weight: bold;
      margin: 26px;
      margin-left: 0px;
      .card-panel-text {
        line-height: 18px;
        color: rgba(0, 0, 0, 0.45);
        font-size: 16px;
        margin-bottom: 12px;
      }
      .card-panel-num {
        font-size: 20px;
      }
    }
  }
}
</style>
