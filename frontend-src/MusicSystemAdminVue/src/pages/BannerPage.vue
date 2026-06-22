<template>
    <div class="table">
        <div class="container">
            <div class="handle-box">
                <el-input v-model="select_word" placeholder="请输入标题" class="handle-input"></el-input>
                <br><br>
                <el-button type="danger" style="font-size: 18px;" @click="delAll"> 批量删除</el-button>
                <el-button type="success" style="font-size: 18px;" @click="centerDialogVisible = true"> 添加轮播图</el-button>
            </div>
        </div>

        <el-table ref="multipleTable" border style="width:100%" height="680px" :data="data" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="40"></el-table-column>
            <el-table-column label="图片" width="140" align="center">
                <template slot-scope="scope">
                    <div class="banner-img">
                        <img :src="bannerImgUrl(scope.row.pic)" @error="onImgError" style="width:100%"/>
                    </div>
                    <el-upload
                        :action="uploadUrl(scope.row.id)"
                        :before-upload="beforeAvatorUpload"
                        :on-success="(res,file)=>handleBannerPicSuccess(res,file,scope.row)"
                    >
                        <el-button style="font-size: 16px;" type="success"> 上传</el-button>
                    </el-upload>
                </template>
            </el-table-column>
            <el-table-column prop="title" label="标题" width="200" align="center"></el-table-column>
            <el-table-column prop="link" label="链接" align="center"></el-table-column>
            <el-table-column label="图片路径" width="220" align="center">
                <template slot-scope="scope">
                    <span style="font-size: 12px;">{{ scope.row.pic }}</span>
                </template>
            </el-table-column>
            <!-- 排序列隐藏（如需可再打开） -->
            <el-table-column label="上下架" width="120" align="center">
                <template slot-scope="scope">
                    <el-switch
                        v-model="scope.row.status"
                        :active-value="1"
                        :inactive-value="0"
                        @change="toggleStatus(scope.row)"
                    ></el-switch>
                </template>
            </el-table-column>
            <el-table-column label="操作" width="220" align="center">
                <template slot-scope="scope">
                    <el-button style="font-size: 16px;" type="success" @click="handleEdit(scope.row)"> 编辑</el-button>
                    <el-button style="font-size: 16px;" type="danger" @click="handleDelete(scope.row.id)"> 删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <div class="pagination">
            <el-pagination
                background
                layout="total,prev,pager,next"
                :current-page="currentPage"
                :page-size="pageSize"
                :total="tableData.length"
                @current-change="handleCurrentChange"
            >
            </el-pagination>
        </div>

        <el-dialog title="添加轮播图" :visible.sync="centerDialogVisible" width="420px" center>
            <el-form :model="registerForm" ref="registerForm" label-width="90px">
                <el-form-item prop="title" label="标题" size="mini">
                    <el-input v-model="registerForm.title" placeholder="请输入标题"></el-input>
                </el-form-item>
                <!-- <el-form-item prop="link" label="链接" size="mini">
                    <el-input v-model="registerForm.link" placeholder="https://..."></el-input>
                </el-form-item> -->
                <el-form-item prop="pic" label="图片" size="mini">
                    <input type="file" ref="bannerFile" />
                </el-form-item>
                <!-- 排序字段隐藏（如需可再打开） -->
                <el-form-item prop="status" label="上下架" size="mini">
                    <el-select v-model="registerForm.status" placeholder="请选择">
                        <el-option key="1" label="上架" :value="1"></el-option>
                        <el-option key="0" label="下架" :value="0"></el-option>
                    </el-select>
                </el-form-item>
            </el-form>
            <span slot="footer">
                <el-button size="mini" @click="centerDialogVisible = false"> 取消</el-button>
                <el-button size="mini" @click="addBanner" type="primary"> 保存</el-button>
            </span>
        </el-dialog>

        <el-dialog title="编辑轮播图" :visible.sync="editVisible" width="420px" center>
            <el-form :model="form" ref="form" label-width="90px">
                <el-form-item prop="title" label="标题" size="mini">
                    <el-input v-model="form.title" placeholder="请输入标题"></el-input>
                </el-form-item>
                <el-form-item label="更换图片" size="mini">
                    <el-upload
                        :action="uploadUrl(form.id)"
                        :before-upload="beforeAvatorUpload"
                        :on-success="(res,file)=>handleBannerPicSuccess(res,file,form)"
                    >
                        <el-button style="font-size: 16px;" type="success"> 上传</el-button>
                    </el-upload>
                </el-form-item>
                <!-- 链接/图片地址字段隐藏（如需可再打开） -->
                <!-- 排序字段隐藏（如需可再打开） -->
                <el-form-item prop="status" label="上下架" size="mini">
                    <el-select v-model="form.status" placeholder="请选择">
                        <el-option key="1" label="上架" :value="1"></el-option>
                        <el-option key="0" label="下架" :value="0"></el-option>
                    </el-select>
                </el-form-item>
            </el-form>
            <span slot="footer">
                <el-button size="mini" @click="editVisible = false"> 取消</el-button>
                <el-button size="mini" @click="editSave" type="primary"> 保存</el-button>
            </span>
        </el-dialog>

        <el-dialog title="删除轮播图" :visible.sync="delVisible" width="300px" center>
            <div align="center">删除后不可恢复，确认删除？</div>
            <span slot="footer">
                <el-button size="mini" @click="delVisible = false"> 取消</el-button>
                <el-button size="mini" @click="deleteRow" type="primary"> 确认</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
import { getAllBanner, setBanner, updateBanner, delBanner, updateBannerStatus } from "../api/index";
import { mixin } from "../mixins/index";

export default {
    mixins: [mixin],
    data(){
        return{
            centerDialogVisible: false,
            editVisible: false,
            delVisible: false,
            registerForm:{
                title: "",
                link: "",
                pic: "",
                sort: 0,
                status: 1,
            },
            form:{
                id: "",
                title: "",
                link: "",
                pic: "",
                sort: 0,
                status: 1,
            },
            tableData: [],
            tempData: [],
            select_word: "",
            pageSize: 5,
            currentPage: 1,
            idx: -1,
            multipleSelection: [],
            placeholderImg: require("../assets/img/user.jpg"),
        }
    },
    computed:{
        data(){
            return this.tableData.slice((this.currentPage - 1) * this.pageSize,this.currentPage * this.pageSize)
        }
    },
    watch:{
        select_word: function(){
            if(this.select_word == ''){
                this.tableData = this.tempData;
            }else{
                this.tableData = [];
                for(let item of this.tempData){
                    if((item.title || "").includes(this.select_word)){
                        this.tableData.push(item);
                    }
                }
            }
        }
    },
    created(){
        this.getData();
    },
    methods:{
        handleCurrentChange(val){
            this.currentPage = val;
        },
        bannerImgUrl(pic){
            if (!pic) return this.placeholderImg;
            if (typeof pic === "string") {
                if (!pic.includes("/") && !pic.startsWith("http")) {
                    return this.getUrl(encodeURI(`/img/banner/${pic}`));
                }
            }
            if (typeof pic === "string" && (pic.startsWith("http://") || pic.startsWith("https://") || pic.startsWith("//"))) {
                return encodeURI(pic);
            }
            return this.getUrl(typeof pic === "string" ? encodeURI(pic) : pic);
        },
        onImgError(e){
            if (e && e.target) {
                e.target.onerror = null;
                e.target.src = this.placeholderImg;
            }
        },
        getData(){
            this.tempData = [];
            this.tableData = [];
            getAllBanner().then(res => {
                let arr = [];
                if (Array.isArray(res)) arr = res;
                else if (res && Array.isArray(res.data)) arr = res.data;
                else if (res && res.data && Array.isArray(res.data.data)) arr = res.data.data;
                else if (res && res.data && Array.isArray(res.data.list)) arr = res.data.list;
                else if (res && Array.isArray(res.list)) arr = res.list;

                const normalized = (arr || []).map((it) => {
                    const item = it || {};
                    return {
                        ...item,
                        title: item.title || item.name || "",
                        link: item.link || item.url || item.href || "",
                        pic: item.pic || item.image || item.img || item.cover || "",
                        sort: item.sort || item.order || 0,
                        status: item.status !== undefined ? item.status : (item.enabled ? 1 : 0),
                    };
                });

                this.tempData = normalized;
                this.tableData = normalized;
                this.currentPage = 1;
            })
        },
        addBanner(){
            const form = new FormData();
            form.append("title", this.registerForm.title || "");
            form.append("link", this.registerForm.link || "");
            form.append("sort", this.registerForm.sort || 0);
            form.append("status", this.registerForm.status !== undefined ? this.registerForm.status : 1);
            const fileInput = this.$refs.bannerFile;
            if (fileInput && fileInput.files && fileInput.files[0]) {
                form.append("file", fileInput.files[0]);
            }

            const req = new XMLHttpRequest();
            const _this = this;
            req.onreadystatechange = function () {
                if (req.readyState == 4) {
                    if (req.status == 200) {
                        let res = {};
                        try {
                            res = JSON.parse(req.response);
                        } catch (e) {
                            res = {};
                        }
                        if (res.code == 1 || res.success) {
                            if (res.pic) _this.registerForm.pic = res.pic;
                            if (res.data && res.data.pic) _this.registerForm.pic = res.data.pic;
                            _this.getData();
                            _this.notify("添加成功", "success");
                        } else {
                            _this.notify(res.msg || "添加失败", "error");
                        }
                    } else {
                        _this.notify("上传失败", "error");
                    }
                }
            };
            req.open("post", `${this.$store.state.HOST}/banner/add`, true);
            req.send(form);
            this.centerDialogVisible = false;
        },
        handleEdit(row){
            this.editVisible = true;
            this.form = {
                id: row.id,
                title: row.title,
                link: row.link,
                pic: row.pic,
                sort: row.sort,
                status: row.status
            }
        },
        editSave(){
            const params = {
                id: this.form.id,
                title: this.form.title,
                link: this.form.link,
                pic: this.form.pic,
                sort: this.form.sort,
                status: this.form.status
            };
            updateBanner(params)
            .then(res => {
                if(res.code == 1 || res.success){
                    this.getData();
                    this.notify("更新成功", "success");
                }else{
                    this.notify("更新失败", "error");
                }
            })
            .catch(err => {
                console.log(err);
            });
            this.editVisible = false;
        },
        uploadUrl(id){
            return `${this.$store.state.HOST}/banner/updateBannerPic?id=${id}`
        },
        handleBannerPicSuccess(res, file, row){
            const pic =
                (res && res.pic) ||
                (res && res.data && res.data.pic) ||
                (res && res.data && res.data.url) ||
                (res && res.data && res.data.path) ||
                "";
            if (pic && row) {
                row.pic = pic;
            }
            this.handleAvatorSuccess(res);
        },
        toggleStatus(row){
            if (!row || row.id === undefined) return;
            const params = { id: row.id, status: row.status };
            updateBannerStatus(params)
            .then(res => {
                if(res.code == 1 || res.success){
                    this.notify("状态已更新", "success");
                }else{
                    this.notify("状态更新失败", "error");
                }
            })
            .catch(err => {
                console.log(err);
            });
        },
        deleteRow(){
            delBanner(this.idx)
            .then(res => {
                if(res){
                    this.getData();
                    this.notify("删除成功", "success");
                }else{
                    this.notify("删除失败", "error");
                }
            })
            .catch(err => {
                console.log(err);
            });
            this.delVisible = false;
        }
    }
}
</script>

<style scoped>
    .handle-box{
        margin-bottom: 20px;
    }
    .banner-img{
        width: 100%;
        height: 100px;
        border-radius: 5px;
        margin-bottom: 5px;
        overflow: hidden;
    }
    .handle-input{
        width: 300px;
        display: inline-block;
    }
    .pagination{
        display: flex;
        justify-content: center;
    }
</style>
