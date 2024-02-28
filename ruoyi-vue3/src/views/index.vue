<template>
  <div class="app-container home">
    <el-row :gutter="20">
      <el-col :span="6" :xs="25">
          <el-card class="box-card">
              <template v-slot:header>
                  <div class="clearfix">
                      <span>个人卡片</span>
                  </div>
              </template>
              <div>
                  <div class="text-center">
                      <userAvatar :user="state.user" />
                  </div>
                  <ul class="list-group list-group-striped">
                      <li class="list-group-item">
                          <svg-icon icon-class="user" />用户名称
                          <div class="pull-right">{{ state.user.userName }}</div>
                      </li>
                      <li class="list-group-item">
                          <svg-icon icon-class="phone" />手机号码
                          <div class="pull-right">{{ state.user.phonenumber }}</div>
                      </li>
                      <li class="list-group-item">
                          <svg-icon icon-class="email" />用户邮箱
                          <div class="pull-right">{{ state.user.email }}</div>
                      </li>
                      <li class="list-group-item">
                          <svg-icon icon-class="tree" />所属部门
                          <div class="pull-right" v-if="state.user.dept">{{ state.user.dept.deptName }} / {{ state.postGroup }}</div>
                      </li>
                      <li class="list-group-item">
                          <svg-icon icon-class="peoples" />所属角色
                          <div class="pull-right">{{ state.roleGroup }}</div>
                      </li>
                      <li class="list-group-item">
                          <svg-icon icon-class="date" />创建日期
                          <div class="pull-right">{{ state.user.createTime }}</div>
                      </li>
                  </ul>
              </div>
          </el-card>
      </el-col>
      <el-col :span="18" :xs="50" style="">
          <el-card >
              <template v-slot:header>
                  <div class="clearfix">
                      <span>公告信息</span>
                  </div>
              </template>
              <el-table v-loading="loading" :data="noticeList">
<!--                <el-table-column type="selection" width="55" align="center" />-->
                <el-table-column
                        label="公告标题"
                        align="center"
                        prop="noticeTitle"
                        :show-overflow-tooltip="true"
                />
                <el-table-column label="公告类型" align="center" prop="noticeType" width="100">
                    <template #default="scope">
                        <dict-tag :options="sys_notice_type" :value="scope.row.noticeType" />
                    </template>
                </el-table-column>
                <el-table-column label="发布时间" align="center" prop="createTime" width="100">
                    <template #default="scope">
                        <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
                    </template>
                </el-table-column>
                <el-table-column label="内容详情" align="center" prop="details" width="100" >
                    <template #default="scope">
                        <el-button link type="primary"  @click="details(scope.row)" >详情</el-button>
                    </template>
                </el-table-column>
            </el-table>

<!--            <pagination-->
<!--                    v-show="total > 0"-->
<!--                    :total="total"-->
<!--                    v-model:page="queryParams.pageNum"-->
<!--                    v-model:limit="queryParams.pageSize"-->
<!--                    @pagination="getList"-->
<!--            />-->
            <!--公告详情对话框-->
            <el-dialog :title="title" v-model="detail" width="780px" append-to-body>
                <el-form ref="noticeRef" :model="form" :rules="rules" label-width="80px" :hide-required-asterisk="true">
                    <el-row >
                        <el-col :span="12">
                            <el-form-item label="公告标题" prop="noticeTitle">
                                <div>
                                    {{form.noticeTitle}}
                                </div>
                            </el-form-item>
                        </el-col>
                        <el-col :span="12">
                            <el-form-item label="公告类型" prop="noticeType">
                                <div v-if="form.noticeType === '1' ">
                                    通知
                                </div>
                                <div v-if="form.noticeType === '2' ">
                                    公告
                                </div>
                            </el-form-item>
                        </el-col>
                        <el-col :span="24" v-if="form.noticeContent">
                            <el-form-item label="内容">

                                <div> {{ form.noticeContent.replace(/<[^>]+>/g,"") }} </div>

                            </el-form-item>
                        </el-col>
                    </el-row>
                </el-form>
                <template #footer>
                    <div class="dialog-footer">
                        <el-button type="primary" @click="closeDetail">确 定</el-button>
                    </div>
                </template>
            </el-dialog>
          </el-card>
      </el-col>
    </el-row>
      <el-row :gutter="20" class="el-row">
          <el-col :span="6" :xs="25">
              <el-card class="box-card">
                  <el-calendar v-model="value"></el-calendar>
              </el-card>
          </el-col>
      </el-row>

  </div>
</template>

<script setup name="Index">
import { getUserProfile } from "@/api/system/user";
import UserAvatar from "@/views/system/user/profile/userAvatar.vue";
import {reactive, ref} from "vue";
const value = ref(new Date())
const activeTab = ref("userinfo");
const state = reactive({
    user: {},
    roleGroup: {},
    postGroup: {}
});

function getUser() {
    getUserProfile().then(response => {
        state.user = response.data;
        state.roleGroup = response.roleGroup;
        state.postGroup = response.postGroup;
    });
};
getUser();
const version = ref('3.8.5')

function goTarget(url) {
  window.open(url, '__blank')
}


//公告板方法
import {listNotice, getNotice, delNotice, addNotice, updateNotice, listIndexNotice} from "@/api/system/notice";

const { proxy } = getCurrentInstance();
const { sys_notice_status, sys_notice_type } = proxy.useDict("sys_notice_status", "sys_notice_type");

const noticeList = ref([]);
const open = ref(false);
const detail = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");

const data = reactive({
    form: {},
    queryParams: {
        pageNum: 1,
        pageSize: 10,
        noticeTitle: undefined,
        createBy: undefined,
        status: undefined
    },
    rules: {
        noticeTitle: [{ required: true, message: "公告标题不能为空", trigger: "blur" }],
        noticeType: [{ required: true, message: "公告类型不能为空", trigger: "change" }]
    },
});

const { queryParams, form, rules } = toRefs(data);

/** 查询公告列表 */
function getList() {
    loading.value = true;
    listIndexNotice(queryParams.value).then(response => {
        noticeList.value = response.rows;
        total.value = response.total;
        loading.value = false;
    });
}

/** 表单重置 */
function reset() {
    form.value = {
        noticeId: undefined,
        noticeTitle: undefined,
        noticeType: undefined,
        noticeContent: undefined,
        status: "0"
    };
    proxy.resetForm("noticeRef");
}



/** 获取公告内容*/
function details(row) {
    reset();
    const noticeId = row.noticeId || ids.value;
    getNotice(noticeId).then(response => {
        form.value = response.data;
        detail.value = true;
        title.value = "详情";
    });
}
/** 关闭通知内容页面 */
function closeDetail() {
    detail.value = false;
}

getList();
</script>

<style scoped lang="scss">
.home {
  blockquote {
    padding: 10px 20px;
    margin: 0 0 20px;
    font-size: 17.5px;
    border-left: 5px solid #eee;
  }
  hr {
    margin-top: 20px;
    margin-bottom: 20px;
    border: 0;
    border-top: 1px solid #eee;
  }
  .col-item {
    margin-bottom: 20px;
  }

  ul {
    padding: 0;
    margin: 0;
  }

  font-family: "open sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
  font-size: 13px;
  color: #676a6c;
  overflow-x: hidden;

  ul {
    list-style-type: none;
  }

  h4 {
    margin-top: 0px;
  }

  h2 {
    margin-top: 10px;
    font-size: 26px;
    font-weight: 100;
  }

  p {
    margin-top: 10px;

    b {
      font-weight: 700;
    }
  }

  .update-log {
    ol {
      display: block;
      list-style-type: decimal;
      margin-block-start: 1em;
      margin-block-end: 1em;
      margin-inline-start: 0;
      margin-inline-end: 0;
      padding-inline-start: 40px;
    }
  }
}

.el-row {
    margin-bottom: 20px;
    display: flex;
    flex-wrap: wrap
}

.el-card {
    min-width: 100%;
    height: 100%; //高度要设置百分比才可以
    margin-right: 20px;
    transition: all .5s;
}
.el-calendar-table__row td {
  .el-calendar-day {
    height: 30px;
    line-height: 30px;
    text-align: center;
  }
}
</style>

