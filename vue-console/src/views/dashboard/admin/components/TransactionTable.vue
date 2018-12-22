<template>
  <el-table v-loading="loading"  :data="list" border=true style="width: 100%;padding-top: 15px;">
    <el-table-column label="Number" width="280">
      <template slot-scope="scope">
        {{ scope.row.id | orderNoFilter }}
      </template>
    </el-table-column>
    <el-table-column label="Type" width="100" align="center">
      <template slot-scope="scope">
        {{ scope.row.type  }}
      </template>
    </el-table-column>
        <el-table-column label="Name" width="100" align="center">
      <template slot-scope="scope">
        {{ scope.row.name  }}
      </template>
    </el-table-column>
            <el-table-column label="Describe" width="200" align="center">
      <template slot-scope="scope">
        {{ scope.row.describe  }}
      </template>
    </el-table-column>
            <el-table-column label="CreateTime" min-width="200" align="center" >
      <template slot-scope="scope">
        {{scope.row.createTime | dateFormat}}
      </template>
    </el-table-column>
    <el-table-column label="Status" width="100" align="center">
      <template slot-scope="scope">
        <el-tag :type="scope.row.status | statusFilter"> {{ scope.row.status }}</el-tag>
      </template>
    </el-table-column>
  </el-table>
</template>

<script>
import store from '@/store'
import {showTransactionTableData} from '@/api/api'
import { formatTime } from '@/utils';

export default {
  filters: {
    dateFormat(stamp) {
      return formatTime(stamp, '{y}-{m}-{d} {h}:{i}:{s}')
    },
    statusFilter(status) {
      const statusMap = {
        success: 'success',
        pending: 'info',
        error: 'warning',
        exception:'danger'
      }
      return statusMap[status]
    },
    orderNoFilter(str) {
      return str.substring(0, 32)
    }
  },
  data() {
    return {
      list: null
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      const param={
      id:store.getters.id,
      token:store.getters.token
      }
    showTransactionTableData(param).then(res=>{
      this.list=res.data
    });

    },
     
  }
}
</script>
