import request from '@/utils/request'

// 查询备份任务调度列表
export function listJob(query) {
    return request({
        url: '/datahouse/backup/list',
        method: 'get',
        params: query
    })
}

export function listProjectName(query){
    return request({
        url: '/datahouse/project/list',
        method: 'get',
        params: query
    })
}

export function listStorageDatabase(query){
    return request({
        url: '/datahouse/datasource/list',
        method: 'get',
        params: query
    })
}

export function listTransitDatabase(query){
    return request({
        url: '/datahouse/datasource/list',
        method: 'get',
        params: query
    })
}

// 查询备份任务调度详细
export function getJob(jobId) {
    return request({
        url: '/datahouse/backup/' + jobId,
        method: 'get'
    })
}

// 新增定时任务调度
export function addJob(data) {
    return request({
        url: '/datahouse/backup',
        method: 'post',
        data: data
    })
}

// 修改备份任务调度
export function updateJob(data) {
    return request({
        url: '/datahouse/backup/',
        method: 'put',
        data: data
    })
}

// 删除定时任务调度
export function delJob(jobId) {
    return request({
        url: '/datahouse/backup/' + jobId,
        method: 'delete'
    })
}

// 任务状态修改
export function changeJobStatus(jobId, status) {
    const data = {
        jobId,
        status
    }
    return request({
        url: '/datahouse/backup/changeStatus',
        method: 'put',
        data: data
    })
}

