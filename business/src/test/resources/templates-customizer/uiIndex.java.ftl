<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="关键字" prop="keyWord">
        <el-input
          v-model="queryParams.keyWord"
          placeholder="请输入关键字"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
        >删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="${entity?uncap_first}List" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
    <#list table.fields as field>
        <#-- 普通字段 -->
        <#if !field.logicDeleteField>
        <el-table-column
                label="${field.comment}"
                align="center"
                prop="${field.propertyName}"
                :show-overflow-tooltip="true"
              />
        </#if>
    </#list>
      <el-table-column label="创建者" align="center" prop="createBy" width="100" />
      <el-table-column label="创建时间" align="center" prop="createAt" width="150">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createAt, '{y}-{m}-{d} {hh}:{mm}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改${table.comment!}对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="780px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <#list table.fields as field>
              <#-- 普通字段 -->
              <#if !field.logicDeleteField>
              <el-col :span="12">
                  <el-form-item label="${field.comment}" prop="${field.propertyName}">
                    <el-input v-model="form.${field.propertyName}" placeholder="请输入${field.comment}" />
                  </el-form-item>
                </el-col>
              </#if>
          </#list>
          <el-col :span="24">
            <el-form-item label="">

            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { list${entity}, get${entity}, del${entity}, add${entity}, update${entity} } from "@/api/${entity?uncap_first}";

export default {
  name: "${entity}",
  dicts: [],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // ${table.comment!}表格数据
      ${entity?uncap_first}List: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        sortName: undefined,
        sortOrder: undefined,
        keyWord: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        <#list table.fields as field>
              <#-- 普通字段 -->
              <#if !field.logicDeleteField>
              ${field.propertyName}: [
                        { required: true, message: "${field.comment}不能为空", trigger: "blur" }
                      ],
              </#if>
          </#list>
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询${table.comment!}列表 */
    getList() {
      this.loading = true;
      list${entity}(this.queryParams).then(response => {
        this.${entity?uncap_first}List = response.data.records;
        this.total = response.data.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: undefined
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!=1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加${table.comment!}";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const ${entity?uncap_first}Id = row.id || this.ids
      get${entity}(${entity?uncap_first}Id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改${table.comment!}";
      });
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != undefined) {
            update${entity}(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            add${entity}(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ${entity?uncap_first}Ids = row.id || this.ids
      this.$modal.confirm('是否确认删除${table.comment!}编号为"' + ${entity?uncap_first}Ids + '"的数据项？').then(function() {
        return del${entity}(${entity?uncap_first}Ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    }
  }
};
</script>
