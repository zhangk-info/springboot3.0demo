import request from '@/utils/request'

// 查询${table.comment!}分页列表
export function list${entity}(query) {
  return request({
    url: '/${controllerMappingHyphen}s',
    method: 'get',
    params: query
  })
}

// 查询${table.comment!}详细
export function get${entity}(${controllerMappingHyphen}Id) {
  return request({
    url: '/${controllerMappingHyphen}s/' + ${controllerMappingHyphen}Id,
    method: 'get'
  })
}

// 新增${table.comment!}
export function add${entity}(data) {
  return request({
    url: '/${controllerMappingHyphen}s',
    method: 'post',
    data: data
  })
}

// 修改${table.comment!}
export function update${entity}(data) {
  return request({
    url: '/${controllerMappingHyphen}s',
    method: 'post',
    data: data
  })
}

// 删除${table.comment!}
export function del${entity}(${controllerMappingHyphen}Ids) {
  return request({
    url: '/${controllerMappingHyphen}s/' + ${controllerMappingHyphen}Ids,
    method: 'delete'
  })
}