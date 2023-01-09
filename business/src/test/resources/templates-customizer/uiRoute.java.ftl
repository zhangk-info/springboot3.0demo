import request from '@/utils/request'

// 查询${table.comment!}分页列表
export function list${entity}(query) {
  return request({
    url: '/${entity?uncap_first}s',
    method: 'get',
    params: query
  })
}

// 查询${table.comment!}详细
export function get${entity}(${entity?uncap_first}Id) {
  return request({
    url: '/${entity?uncap_first}s/' + ${entity?uncap_first}Id,
    method: 'get'
  })
}

// 新增${table.comment!}
export function add${entity}(data) {
  return request({
    url: '/${entity?uncap_first}s',
    method: 'post',
    data: data
  })
}

// 修改${table.comment!}
export function update${entity}(data) {
  return request({
    url: '/${entity?uncap_first}s',
    method: 'post',
    data: data
  })
}

// 删除${table.comment!}
export function del${entity}(${entity?uncap_first}Ids) {
  return request({
    url: '/${entity?uncap_first}s/' + ${entity?uncap_first}Ids,
    method: 'delete'
  })
}