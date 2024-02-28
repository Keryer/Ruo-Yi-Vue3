import request from "@/utils/request";
// Path: ruoyi-vue3\src\api\datahouse\tablecolumn.js


export function listTable(query) {
    console.log(query);
  return request({
    url: "/datahouse/table/list",
    method: "get",
    params: query
  });
}

export function getTable(tableId) {
  return request({
    url: "/datahouse/table/" + tableId,
    method: "get"
  });
}

export function addTable(data) {
    return request({
        url: "/datahouse/table",
        method: "post",
        data: data
    })
}

export function updateTable(data) {
    return request({
        url: "/datahouse/table",
        method: "put",
        data: data
    })
}

export function delTable(tableId) {
    return request({
        url: "/datahouse/table/" + tableId,
        method: "delete"
    })
}

export function treeselect() {
    return request({
        url: "/datahouse/table/treeselect",
        method: "get"
    })
}

export function roleTableTreeselect(roleId) {
    return request({
        url: "/datahouse/table/roleTableTreeselect/" + roleId,
        method: "get"
    })
}