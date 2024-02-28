<template>
    <div class="app-container">

        <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
            <el-form-item label="任务名称" prop="jobName">
                <el-input
                        v-model="queryParams.jobName"
                        placeholder="请输入任务名称"
                        clearable
                        style="width: 200px"
                        @keyup.enter="handleQuery"
                />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
                <el-button icon="Refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
                <el-button
                        type="primary"
                        plain
                        icon="Plus"
                        @click="handleAdd"
                        v-hasPermi="['datahouse:backup:add']"
                >新增</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                        type="success"
                        plain
                        icon="Edit"
                        :disabled="single"
                        @click="handleUpdate"
                        v-hasPermi="['datahouse:backup:edit']"
                >修改</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                        type="danger"
                        plain
                        icon="Delete"
                        :disabled="multiple"
                        @click="handleDelete"
                        v-hasPermi="['datahouse:backup:remove']"
                >删除</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                        type="warning"
                        plain
                        icon="Download"
                        @click="handleExport"
                        v-hasPermi="['datahouse:backup:export']"
                >导出</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                        type="info"
                        plain
                        icon="Operation"
                        @click="handleJobLog"
                        v-hasPermi="['datahouse:backup:query']"
                >日志</el-button>
            </el-col>
            <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="jobList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="任务编号" width="100" align="center" prop="jobId" />
            <el-table-column label="项目名称" align="center" prop="projectName" :show-overflow-tooltip="true" />
            <el-table-column label="创建时间" align="center" prop="createTime"/>
            <el-table-column label="冷藏地址" align="center" prop="storageAddress" :show-overflow-tooltip="true" />
            <el-table-column label="中转地址" align="center" prop="transferStation" :show-overflow-tooltip="true" />
            <el-table-column label="备注" align="center" prop="comment" :show-overflow-tooltip="true" />
            <el-table-column label="状态" align="center">

            </el-table-column>
            <el-table-column label="处理时间" align="center" prop="disposeTime" :show-overflow-tooltip="true" />
            <el-table-column label="操作" align="center" prop="state" :show-overflow-tooltip="true">
                <template #default="scope">
                    <el-button
                        ref="actionButton"
                        type="primary"
                        plain
                        @click="handleOperate(scope.row)"
                    >操作</el-button>
                </template>
            </el-table-column>
        </el-table>




        <pagination
                v-show="total > 0"
                :total="total"
                v-model:page="queryParams.pageNum"
                v-model:limit="queryParams.pageSize"
                @pagination="getList"
        />



        <!-- 添加或修改定时任务对话框 -->
        <el-dialog :title="title" v-model="open" width="800px" append-to-body>
            <el-form ref="jobRef" :model="form" :rules="rules" label-width="120px">
                <el-row>

                    <el-col :span="12">
                        <el-form-item label="项目名称" prop="projectName">
                            <el-select v-model="form.projectName" placeholder="请选择所属项目名称" >
                                <el-option
                                v-for="item in projectOption"
                                :key="item.projectName"
                                :label="item.projectName"
                                :value="item.projectName">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>


                    <el-col :span="24">
                        <el-form-item prop="transitDatabase">
                            <template #label>
                                <span>
                                   中转仓库
                                </span>
                                <el-tooltip placement="top">
                                </el-tooltip>
                            </template>
                            <el-select v-model="form.transferStation" @change-value="handleSelectedTransferValue">
                                <el-option
                                    v-for="item in transitDatabase"
                                    :key="item.databaseName"
                                    :label="item.databaseName"
                                    :value="item.databaseUrl">
                                </el-option>
                            </el-select>
                            <el-input v-model="form.transferStation"  />
                        </el-form-item>
                    </el-col>


                    <el-col :span="24">
                        <el-form-item label="冷藏仓库" prop="storageDatabase">
                            <el-select v-model="form.storageAddress" @change-value="handleSelectedStorageValue">
                                <el-option
                                    v-for="item in storageDatabase"
                                    :key="item.databaseName"
                                    :label="item.databaseName"
                                    :value="item.databaseUrl">
                                </el-option>
                            </el-select>
                            <el-input v-model="form.storageAddress" placeholder=""/>
                        </el-form-item>
                    </el-col>


                </el-row>
            </el-form>
            <template #footer>
                <div class="dialog-footer">
                    <el-button type="primary" @click="submitForm">确 定</el-button>
                    <el-button @click="cancel">取 消</el-button>
                </div>
            </template>

        </el-dialog>


        <!-- 分配任务信息对话框 -->
        <el-dialog :title="title" v-model="operate" width="800px" append-to-body>

            <el-table
                v-if="refreshTable"
                v-loading="loading"
                :data="tableList"
                row-key="tableId"
                :default-expand-all="isExpandAll"
                :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
                @selection-change="handleSelectionChange"
            >
                <el-table-column type="selection" width="55" align="center" :selectable='checkbox'/>
                <el-table-column prop="tableName" label="实体表名称" :show-overflow-tooltip="true" width="160"></el-table-column>
                <el-table-column prop="tableNameZh" label="实体表中文名称" ></el-table-column>
                <el-table-column prop="project" label="所属项目" ></el-table-column>
                <el-table-column prop="rank" label="层级" :show-overflow-tooltip="true"></el-table-column>
                <el-table-column label="操作" align="center" :show-overflow-tooltip="true">
                    <template v-slot="{row}">
                        <el-select v-if="row.masterId === 0" v-model="selectOption">
                            <el-option
                                v-for="item in operationOption"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value"
                            ></el-option>
                        </el-select>
                    </template>
                </el-table-column>
            </el-table>

            <template #footer>
                <div class="dialog-footer">
                    <el-button type="primary" @click="startOperation">开 始</el-button>
                    <el-button @click="cancelOperation">取 消</el-button>
                </div>
            </template>
        </el-dialog>


        <!-- 任务日志详细 -->
        <el-dialog title="任务详细" v-model="openView" width="700px" append-to-body>
            <el-form :model="form" label-width="120px">
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="任务编号：">{{ form.jobId }}</el-form-item>
                        <el-form-item label="任务名称：">{{ form.projectName }}</el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="创建时间：">{{ form.createTime }}</el-form-item>
                    </el-col>
                </el-row>
            </el-form>
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="openView = false">关 闭</el-button>
                </div>
            </template>
        </el-dialog>
    </div>
</template>

<script setup name="Job">
import {
    listJob,
    getJob,
    delJob,
    addJob,
    updateJob,
    listProjectName, listStorageDatabase, listTransitDatabase
} from "@/api/datahouse/backup";
import {listTable} from "@/api/datahouse/table";

const router = useRouter();
const { proxy } = getCurrentInstance();
const { sys_job_group, sys_job_status } = proxy.useDict("sys_job_group", "sys_job_status");

const isExpandAll = ref(false);
const refreshTable = ref(true);
const jobList = ref([]);
const tableList = ref([]);
const open = ref(false);
const operate = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const openView = ref(false);
const transitDatabase= ref([]);
const storageDatabase= ref([]);
const operationOption = {
    1: { label: "分库", value: 1 },
    2: { label: "其他", value: 2 },
};

const data = reactive({
    selectOption : null,
    form: {},
    projectOption: [{
        projectId: null,
        projectName: null
    }],
    transitDatabase:[],
    storageDatabase:[],
    queryParams: {
        pageNum: 1,
        pageSize: 10,
        jobName: undefined,
        status: undefined,
    },
    queryTableParams: {},
    transitDatabaseParams: {
        datasourceType: 'source',
    },
    storageDatabaseParams: {
        datasourceType: 'target',
    },
    rules: {
        jobName: [{ required: true, message: "任务名称不能为空", trigger: "blur" }],
    }
});

const { selectOption, transitDatabaseParams, storageDatabaseParams, projectOption, queryParams, form, rules, queryTableParams } = toRefs(data);

function handleSelectedTransferValue(value){
    form.value.transferStation = value;
}

function handleSelectedStorageValue(value){
    form.value.storageAddress = value;
}

/** 查询项目名称列表 */
function getProjectName() {
    listProjectName(projectOption.value).then((res)=>{
        projectOption.value = res;
    })
}

/** 查询冷藏库列表 */
function getStorageDatabaseName() {
    listStorageDatabase(storageDatabaseParams.value).then((res)=>{
        storageDatabase.value = res;
    })
}

/** 查询中转库列表 */
function getTransitDatabaseName() {
    listTransitDatabase(transitDatabaseParams.value).then((res)=>{
        transitDatabase.value = res;
    })
}


/** 查询任务列表 */
function getList() {
    loading.value = true;
    listJob(queryParams.value).then(response => {
        jobList.value = response.rows;
        total.value = response.total;
        loading.value = false;
    });
}
//const {project: tableProject} = tableList.value;
/** 查询实体表列表 */
function getTableList(project) {
    console.log(project)
    loading.value = true;
    queryTableParams.value.project = project;
    listTable(queryTableParams.value).then(response => {
        tableList.value = proxy.handleTree(response.data, "tableId", "masterId");
        console.log(tableList.value)
        console.log(tableList.value[0])
        for(let item in tableList.value){
            if(tableList.value[item].masterId === 0){
                tableList.value[item].rank = "主实体表"
                if(tableList.value[item].children !== undefined){
                    tableList.value[item].children.forEach((item1)=> {
                        item1.rank = "从实体表";
                    })
                }
            }
        }
        loading.value = false;
    });
}

/** 取消按钮 */
function cancel() {
    open.value = false;
    reset();
}
/** 表单重置 */
function reset() {
    form.value = {
        projectName: undefined,
        storageAddress: undefined,
        transferStation: undefined,
        comment: undefined,
        state: undefined,
        disposeTime: undefined,
        operation: undefined
    };
    proxy.resetForm("jobRef");
}
/** 搜索按钮操作 */
function handleQuery() {
    queryParams.value.pageNum = 1;
    getList();
}
/** 重置按钮操作 */
function resetQuery() {
    proxy.resetForm("queryRef");
    handleQuery();
}
// 多选框选中数据
function handleSelectionChange(selection) {
    ids.value = selection.map(item => item.jobId);
    single.value = selection.length != 1;
    multiple.value = !selection.length;
}



/** 任务日志列表查询 */
function handleJobLog(row) {
    const jobId = row.jobId || 0;
    router.push('/monitor/job-log/index/' + jobId)
}
/** 新增按钮操作 */
function handleAdd() {
    reset();
    open.value = true;
    title.value = "添加任务";
}
/** 修改按钮操作 */
function handleUpdate(row) {
    reset();
    const jobId = row.jobId || ids.value;
    getJob(jobId).then(response => {
        form.value = response.data;
        open.value = true;
        title.value = "修改任务";
    });
}
/** 提交按钮 */
function submitForm() {
    proxy.$refs["jobRef"].validate(valid => {
        if (valid) {
            if (form.value.jobId !== undefined) {
                console.log(111)
                updateJob(form.value).then(response => {
                    proxy.$modal.msgSuccess("修改成功");
                    open.value = false;
                    getList();
                });
            } else {
                console.log(1111)
                addJob(form.value).then(response => {
                    proxy.$modal.msgSuccess("新增成功");
                    open.value = false;
                    getList();
                });
            }
        }
    });
}
/** 删除按钮操作 */
function handleDelete(row) {
    const jobIds = row.jobId || ids.value;
    proxy.$modal.confirm('是否确认删除定时任务编号为"' + jobIds + '"的数据项?').then(function () {
        return delJob(jobIds);
    }).then(() => {
        getList();
        proxy.$modal.msgSuccess("删除成功");
    }).catch(() => {});
}
/** 导出按钮操作 */
function handleExport() {
    proxy.download("monitor/job/export", {
        ...queryParams.value,
    }, `job_${new Date().getTime()}.xlsx`);
}


/** 操作按钮操作 */
function handleOperate(row){

    reset();
    getTableList(row.projectName);
    operate.value = true;
    title.value = "添加任务";
}

/** 取消操作按钮 */
function cancelOperation(){
    operate.value = false;
    reset();
}

/** 提交操作按钮 */
function startOperation(){
    proxy.$modal.confirm('是否确认执行任务?').then(function () {
        return 0;
    }).then(() => {
        proxy.$modal.msgSuccess("开始执行任务");
    }).catch(() => {});
}

function checkbox(row){
    return row.rank === "主实体表";

}



getList();
getProjectName();
getTransitDatabaseName();
getStorageDatabaseName();
</script>
