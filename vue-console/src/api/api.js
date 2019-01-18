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

export function newTask(data) {
  return request({
    url: '/task/newTask',
    method: 'post',
    data
  })
}

export function searchTask(query) {
  return request({
    url: '/task/searchTask',
    method: 'get',
    params: query
  })
}
export function getTaskIDs(query) {
  return request({
    url: '/task/getTaskIDs',
    method: 'get',
    params: query
  })
}

export function getExceptionTypes(query) {
  return request({
    url: '/task/q',
    method: 'get',
    params: query
  })
}


export function exceptionSearchS(query) {
  return request({
    url: '/task/s',
    method: 'get',
    params: query
  })
}

export function spiderOperation(query) {
  return request({
    url: '/spider/operation',
    method: 'get',
    params: query
  })
}

export function getSingerLimit(query) {
  return request({
    url: '/singer/getSingerLimit',
    method: 'get',
    params: query
  })
}

export function searchSinger(query) {
  return request({
    url: '/singer/searchSinger',
    method: 'get',
    params: query
  })
}

