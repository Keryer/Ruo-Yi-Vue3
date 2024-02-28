import request from "@/utils/request";
// Path: ruoyi-vue3\src\api\datahouse\tablecolumn.js


export function listProject(query) {
    console.log(query);
    return request({
        url: "/datahouse/project/list",
        method: "get",
        params: query
    });
}

export function addProject(data) {
    return request({
        url: "/datahouse/project",
        method: "post",
        data: data
    })
}

export function delProject(projectId) {
    return request({
        url: '/datahouse/project/' + projectId,
        method: 'delete'
    })
}

export function getProject(projectId) {
    return request({
        url: '/datahouse/project/' + projectId,
        method: "get"
    })
}

export function updateProject(data){
    return request({
        url: '/datahouse/project',
        method: "put",
        data: data
    })
}