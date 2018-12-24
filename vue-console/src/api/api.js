import request from '@/utils/request'

export function showLineChartData(query) {
  return request({
    url: '/data/showLineChartData',
    method: 'get',
    params: query
  })
}
export function showPanelGroupData(query) {
  return request({
    url: '/data/showPanelGroupData',
    method: 'get',
    params: query
  })
}

  export function showPieChartData(query) {
    return request({
      url: '/data/showPieChartData',
      method: 'get',
      params: query
    })
}

export function showBarChartData(query) {
  return request({
    url: '/data/showBarChartData',
    method: 'get',
    params: query
  })
}

export function showTransactionTableData(query) {
  return request({
    url: '/data/showTransactionTableData',
    method: 'get',
    params: query
  })
}

export function showTaskData(query) {
  return request({
    url: '/task/showTaskData',
    method: 'get',
    params: query
  })
}