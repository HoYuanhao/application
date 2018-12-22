<template>
  <div class="dashboard-editor-container">

    <github-corner style="position: absolute; top: 0px; border: 0; right: 0;"/>

    <panel-group @handleSetLineChartData="handleSetLineChartData"/>

    <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
      <line-chart :chart-data="lineChartData"/>
    </el-row>

    <el-row :gutter="20">
      <!-- <el-col :xs="24" :sm="24" :lg="8">
        <div class="chart-wrapper">
          <raddar-chart/>
        </div>
      </el-col> -->
      <el-col :xs="24" :sm="24" :lg="8">
        <div class="chart-wrapper">
          <pie-chart/>
        </div>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="8">
        <div class="chart-wrapper">
          <bar-chart/>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="8">
      <el-col :xs="{span: 50}" :sm="{span: 50}" :md="{span: 50}" :lg="{span: 12}" :xl="{span: 12}" style="padding-right:8px;margin-bottom:30px;">
        <transaction-table/>
      </el-col>
      <!-- <el-col :xs="{span: 24}" :sm="{span: 12}" :md="{span: 12}" :lg="{span: 6}" :xl="{span: 6}" style="margin-bottom:30px;">
        <todo-list/>
      </el-col>
      <el-col :xs="{span: 24}" :sm="{span: 12}" :md="{span: 12}" :lg="{span: 6}" :xl="{span: 6}" style="margin-bottom:30px;">
        <box-card/>
      </el-col> -->
    </el-row>

  </div>
</template>

<script>
import GithubCorner from '@/components/GithubCorner'
import PanelGroup from './components/PanelGroup'
import LineChart from './components/LineChart'
// import RaddarChart from './components/RaddarChart'
import PieChart from './components/PieChart'
import BarChart from './components/BarChart'
import TransactionTable from './components/TransactionTable'
// import TodoList from './components/TodoList'
// import BoxCard from './components/BoxCard'
import {showLineChartData} from '@/api/api'
import store from '@/store'
export default {
  name: 'DashboardAdmin',
  components: {
    GithubCorner,
    PanelGroup,
    LineChart,
    // RaddarChart,
    PieChart,
    BarChart,
    TransactionTable
    // TodoList,
    // BoxCard
  },
  data() {
    return {
      lineChartData:{
        music:[],
        ticket:[]
      }
    }
  },
   created() {
    this.init()

  },
  methods: {
    handleSetLineChartData(data) {
      const type=data.type;
       data=data.data;
       this.lineChartData={
            music : data.music,
            ticket : data.ticket
             }
    },

init(){
   const param={
        type:"datas",
        token:store.getters.token,
        id:store.getters.id
      }
      showLineChartData(param).then(res=>{
    this.lineChartData={
            music : res.data.music,
            ticket : res.data.ticket
             }

      })
}
    
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.dashboard-editor-container {
  padding: 32px;
  background-color: rgb(240, 242, 245);
  .chart-wrapper {
    background: #fff;
    padding: 16px 16px 0;
    margin-bottom: 32px;
  }
}
</style>
