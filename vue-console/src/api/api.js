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