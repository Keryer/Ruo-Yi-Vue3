<template>
    <div class="app-container">

        <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
            <el-form-item label="项目名称" prop="jobName">
                <el-input
                        v-model="queryParams.projectName"
                        placeholder="请输入项目名称"
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
                        v-hasPermi="['datahouse:project:add']"
                >新增</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                        type="success"
                        plain
                        icon="Edit"
                        :disabled="single"
                        @click="handleUpdate"
                        v-hasPermi="['datahouse:project:edit']"
                >修改</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                        type="danger"
                        plain
                        icon="Delete"
                        :disabled="multiple"
                        @click="handleDelete"
                        v-hasPermi="['datahouse:project:remove']"
                >删除</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                        type="warning"
                        plain
                        icon="Download"
                        @click="handleExport"
                        v-hasPermi="['datahouse:project:export']"
                >导出</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                        type="info"
                        plain
                        icon="Operation"
                        @click="handleJobLog"
                        v-hasPermi="['datahouse:project:query']"
                >日志</el-button>
            </el-col>
            <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="projectList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="项目ID" align="center" prop="projectId" width="80" :show-overflow-tooltip="true" />
            <el-table-column label="项目名称" align="center" prop="projectName" :show-overflow-tooltip="true" />
            <el-table-column label="创建时间" align="center" prop="createTime" :show-overflow-tooltip="true" >
            </el-table-column>
            <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip="true" />
        </el-table>

        <pagination
                v-show="total > 0"
                :total="total"
                v-model:page="queryParams.pageNum"
                v-model:limit="queryParams.pageSize"
                @pagination="getList"
        />

        <!-- 添加或修改项目对话框 -->
        <el-dialog :title="title" v-model="open" width="800px" append-to-body>
            <el-form ref="jobRef" :model="form" :rules="rules" label-width="120px">
                <el-row>

                    <el-col :span="12">
                        <el-form-item label="项目名称" prop="projectName">
                            <el-input v-model="form.projectName" placeholder="请输入项目名称" ></el-input>
                        </el-form-item>
                    </el-col>


                    <el-col :span="20">
                        <el-form-item label="项目备注">
                            <el-input v-model="form.remark" size="large"></el-input>
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

<script setup name="Project">
import {
    listProject,
    addProject, delProject, getProject, updateProject,
} from "@/api/datahouse/project";
import Crontab from '@/components/Crontab'
const router = useRouter();
const { proxy } = getCurrentInstance();
const { sys_job_group, sys_job_status } = proxy.useDict("sys_job_group", "sys_job_status");

const projectList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const openView = ref(false);
const data = reactive({
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
        status: undefined
    },
    rules: {
        jobName: [{ required: true, message: "任务名称不能为空", trigger: "blur" }],
    }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询项目列表 */
function getList() {
    loading.value = true;
    listProject(queryParams.value).then(response => {
        projectList.value = response;
        total.value = response.total;
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
        jobId: undefined,
        projectName: undefined,
        createTime: undefined,
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
    ids.value = selection.map(item => item.projectId);
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
    const projectId = row.projectId || ids.value;
    console.log(1)
    getProject(projectId).then(response => {
        console.log(11)
        form.value = response.data;
        open.value = true;
        title.value = "修改任务";
    });
}
/** 提交按钮 */
function submitForm() {
    proxy.$refs["jobRef"].validate(valid => {
        if (valid) {
            if (form.value.projectId != undefined) {
                updateProject(form.value).then(response => {
                    proxy.$modal.msgSuccess("修改成功");
                    open.value = false;
                    getList();
                });
            } else {
                addProject(form.value).then(response => {
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
    const projectIds = row.projectId || ids.value;
    proxy.$modal.confirm('是否确认删除定时任务编号为"' + projectIds + '"的数据项?').then(function () {
        return delProject(projectIds);
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

getList();

</script>
