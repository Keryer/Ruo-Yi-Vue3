<template>
   <div class="app-container">
      <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
         <el-form-item label="实体表名称" prop="tableName">
            <el-input
               v-model="queryParams.tableName"
               placeholder="请输入实体表名称"
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
               v-hasPermi="['datahouse:table:add']"
            >新增</el-button>
         </el-col>
         <el-col :span="1.5">
            <el-button 
               type="info"
               plain
               icon="Sort"
               @click="toggleExpandAll"
            >展开/折叠</el-button>
         </el-col>
         <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>


      <el-table
         v-if="refreshTable"
         v-loading="loading"
         :data="tableList"
         row-key="tableId"
         :default-expand-all="isExpandAll"
         :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
      >
         <el-table-column prop="tableName" label="实体表名称" :show-overflow-tooltip="true" width="160"></el-table-column>
         <el-table-column prop="tableNameZh" label="实体表中文名称" ></el-table-column>
         <el-table-column prop="project" label="所属项目" :show-overflow-tooltip="true"></el-table-column>
         <el-table-column prop="masterName" label="所属实体表" :show-overflow-tooltip="true"></el-table-column>
         <el-table-column prop="rank" label="层级" :show-overflow-tooltip="true"></el-table-column>
      </el-table>


   </div>
</template>

<script setup name="Table">
import { addTable, delTable, getTable, listTable, updateTable } from "@/api/datahouse/table";
import SvgIcon from "@/components/SvgIcon";
import IconSelect from "@/components/IconSelect";
import { ClickOutside as vClickOutside } from 'element-plus'

const { proxy } = getCurrentInstance();
const { sys_show_hide, sys_normal_disable } = proxy.useDict("sys_show_hide", "sys_normal_disable");

const tableList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const title = ref("");
const tableOptions = ref([]);
const isExpandAll = ref(false);
const refreshTable = ref(true);
const showChooseIcon = ref(false);
const iconSelectRef = ref(null);

const data = reactive({
  form: {},
  queryParams: {
    tableName: undefined,
  },
  rules: {
    tableName: [{ required: true, message: "菜单名称不能为空", trigger: "blur" }],
    orderNum: [{ required: true, message: "菜单顺序不能为空", trigger: "blur" }],
    path: [{ required: true, message: "路由地址不能为空", trigger: "blur" }]
  },
});

const { queryParams, form, rules } = toRefs(data);

/** 查询实体表列表 */
function getList() {
  loading.value = true;
  listTable(queryParams.value).then(response => {
     console.log(response.data)
     tableList.value = proxy.handleTree(response.data, "tableId", "masterId");
     for(let item in tableList.value){
        if(tableList.value[item].masterId === 0){
           tableList.value[item].rank = "主实体表";
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
/** 查询菜单下拉树结构 */
function getTreeselect() {
  tableOptions.value = [];
  listTable().then(response => {
    const table = { tableId: 0, tableName: "主类目", children: [] };
    table.children = proxy.handleTree(response.data, "tableId");
    tableOptions.value.push(table);
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
    tableId: undefined,
    parentId: 0,
    tableName: undefined,
    icon: undefined,
    tableType: "M",
    orderNum: undefined,
    isFrame: "1",
    isCache: "0",
    visible: "0",
    status: "0"
  };
  proxy.resetForm("tableRef");
}
/** 展示下拉图标 */
function showSelectIcon() {
  iconSelectRef.value.reset();
  showChooseIcon.value = true;
}
/** 选择图标 */
function selected(name) {
  form.value.icon = name;
  showChooseIcon.value = false;
}
/** 图标外层点击隐藏下拉列表 */
function hideSelectIcon(event) {
  var elem = event.relatedTarget || event.srcElement || event.target || event.currentTarget;
  var className = elem.className;
  if (className !== "el-input__inner") {
    showChooseIcon.value = false;
  }
}
/** 搜索按钮操作 */
function handleQuery() {
  getList();
}
/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef");
  handleQuery();
}
/** 新增按钮操作 */
function handleAdd(row) {
  reset();
  getTreeselect();
  if (row != null && row.tableId) {
    form.value.parentId = row.tableId;
  } else {
    form.value.parentId = 0;
  }
  open.value = true;
  title.value = "添加菜单";
}
/** 展开/折叠操作 */
function toggleExpandAll() {
  refreshTable.value = false;
  isExpandAll.value = !isExpandAll.value;
  nextTick(() => {
    refreshTable.value = true;
  });
}
/** 修改按钮操作 */
async function handleUpdate(row) {
  reset();
  await getTreeselect();
  getTable(row.tableId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改菜单";
  });
}
/** 提交按钮 */
function submitForm() {
  proxy.$refs["tableRef"].validate(valid => {
    if (valid) {
      if (form.value.tableId != undefined) {
        updateTable(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addTable(form.value).then(response => {
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
  proxy.$modal.confirm('是否确认删除名称为"' + row.tableName + '"的数据项?').then(function() {
    return delTable(row.tableId);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

getList();
</script>
