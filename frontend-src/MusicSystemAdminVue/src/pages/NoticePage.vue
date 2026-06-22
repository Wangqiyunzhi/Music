<template>
    <div class="table">
        <div class="container">
            <div class="handle-box">
                <el-input v-model="select_word" placeholder="璇疯緭鍏ユ爣棰? class="handle-input"></el-input>
                <br><br>
                <el-button type="danger" style="font-size: 18px;" @click="delAll"> 鎵归噺鍒犻櫎</el-button>
                <el-button type="success" style="font-size: 18px;" @click="centerDialogVisible = true"> 娣诲姞鍏憡</el-button>
            </div>
        </div>

        <el-table ref="multipleTable" border style="width:100%" height="680px" :data="data" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="40"></el-table-column>
            <el-table-column prop="title" label="鏍囬" width="220" align="center"></el-table-column>
            <el-table-column label="鍐呭" align="left">
                <template slot-scope="scope">
                    <div style="line-height: 1.6; white-space: pre-line;">
                        {{ scope.row.content }}
                    </div>
                </template>
            </el-table-column>
            <el-table-column label="涓婁笅鏋? width="120" align="center">
                <template slot-scope="scope">
                    <el-switch
                        v-model="scope.row.status"
                        :active-value="1"
                        :inactive-value="0"
                        @change="toggleStatus(scope.row)"
                    ></el-switch>
                </template>
            </el-table-column>
            <el-table-column label="鎿嶄綔" width="220" align="center">
                <template slot-scope="scope">
                    <el-button style="font-size: 16px;" type="success" @click="handleEdit(scope.row)"> 缂栬緫</el-button>
                    <el-button style="font-size: 16px;" type="danger" @click="handleDelete(scope.row.id)"> 鍒犻櫎</el-button>
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

        <el-dialog title="娣诲姞鍏憡" :visible.sync="centerDialogVisible" width="520px" center>
            <el-form :model="registerForm" ref="registerForm" label-width="90px">
                <el-form-item prop="title" label="鏍囬" size="mini">
                    <el-input v-model="registerForm.title" placeholder="璇疯緭鍏ユ爣棰?></el-input>
                </el-form-item>
                <el-form-item prop="content" label="鍐呭" size="mini">
                    <el-input type="textarea" :rows="5" v-model="registerForm.content" placeholder="璇疯緭鍏ュ叕鍛婂唴瀹?></el-input>
                </el-form-item>
                <el-form-item prop="status" label="涓婁笅鏋? size="mini">
                    <el-select v-model="registerForm.status" placeholder="璇烽€夋嫨">
                        <el-option key="1" label="涓婃灦" :value="1"></el-option>
                        <el-option key="0" label="涓嬫灦" :value="0"></el-option>
                    </el-select>
                </el-form-item>
            </el-form>
            <span slot="footer">
                <el-button size="mini" @click="centerDialogVisible = false"> 鍙栨秷</el-button>
                <el-button size="mini" @click="addNotice" type="primary"> 淇濆瓨</el-button>
            </span>
        </el-dialog>

        <el-dialog title="缂栬緫鍏憡" :visible.sync="editVisible" width="520px" center>
            <el-form :model="form" ref="form" label-width="90px">
                <el-form-item prop="title" label="鏍囬" size="mini">
                    <el-input v-model="form.title" placeholder="璇疯緭鍏ユ爣棰?></el-input>
                </el-form-item>
                <el-form-item prop="content" label="鍐呭" size="mini">
                    <el-input type="textarea" :rows="5" v-model="form.content" placeholder="璇疯緭鍏ュ叕鍛婂唴瀹?></el-input>
                </el-form-item>
                <el-form-item prop="status" label="涓婁笅鏋? size="mini">
                    <el-select v-model="form.status" placeholder="璇烽€夋嫨">
                        <el-option key="1" label="涓婃灦" :value="1"></el-option>
                        <el-option key="0" label="涓嬫灦" :value="0"></el-option>
                    </el-select>
                </el-form-item>
            </el-form>
            <span slot="footer">
                <el-button size="mini" @click="editVisible = false"> 鍙栨秷</el-button>
                <el-button size="mini" @click="editSave" type="primary"> 淇濆瓨</el-button>
            </span>
        </el-dialog>

        <el-dialog title="鍒犻櫎鍏憡" :visible.sync="delVisible" width="300px" center>
            <div align="center">鍒犻櫎鍚庝笉鍙仮澶嶏紝纭鍒犻櫎锛?/div>
            <span slot="footer">
                <el-button size="mini" @click="delVisible = false"> 鍙栨秷</el-button>
                <el-button size="mini" @click="deleteRow" type="primary"> 纭</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
import { getAllNotice, setNotice, updateNotice, delNotice, updateNoticeStatus } from "../api/index";
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
                content: "",
                link: "",
                sort: 0,
                status: 1,
            },
            form:{
                id: "",
                title: "",
                content: "",
                link: "",
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
        },
        centerDialogVisible: function(val){
            if (val) {
                this.resetRegisterForm();
            }
        }
    },
    created(){
        this.getData();
    },
    methods:{
        resetRegisterForm(){
            if (this.$refs && this.$refs.registerForm && this.$refs.registerForm.resetFields) {
                this.$refs.registerForm.resetFields();
            }
            this.registerForm = {
                title: "",
                content: "",
                link: "",
                sort: 0,
                status: 1,
            };
        },
        handleCurrentChange(val){
            this.currentPage = val;
        },
        getData(){
            this.tempData = [];
            this.tableData = [];
            getAllNotice().then(res => {
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
                        title: item.title || "",
                        content: item.content || "",
                        sort: item.sort || 0,
                        status: item.status !== undefined ? item.status : (item.enabled ? 1 : 0),
                    };
                });

                this.tempData = normalized;
                this.tableData = normalized;
                this.currentPage = 1;
            })
        },
        addNotice(){
            const params = {
                title: this.registerForm.title,
                content: this.registerForm.content,
                link: this.registerForm.link,
                sort: this.registerForm.sort || 0,
                status: this.registerForm.status !== undefined ? this.registerForm.status : 1
            };
            setNotice(params)
            .then(res => {
                if(res.code == 1 || res.success){
                    this.getData();
                    this.notify("娣诲姞鎴愬姛", "success");
                    this.resetRegisterForm();
                }else{
                    this.notify(res.msg || "娣诲姞澶辫触", "error");
                }
            })
            .catch(err => {
                console.log(err);
            });
            this.centerDialogVisible = false;
        },
        handleEdit(row){
            this.editVisible = true;
            this.form = {
                id: row.id,
                title: row.title,
                content: row.content,
                link: row.link || "",
                sort: row.sort || 0,
                status: row.status
            }
        },
        editSave(){
            const params = {
                id: this.form.id,
                title: this.form.title,
                content: this.form.content,
                link: this.form.link,
                sort: this.form.sort,
                status: this.form.status
            };
            updateNotice(params)
            .then(res => {
                if(res.code == 1 || res.success){
                    this.getData();
                    this.notify("鏇存柊鎴愬姛", "success");
                }else{
                    this.notify(res.msg || "鏇存柊澶辫触", "error");
                }
            })
            .catch(err => {
                console.log(err);
            });
            this.editVisible = false;
        },
        toggleStatus(row){
            if (!row || row.id === undefined) return;
            const params = { id: row.id, status: row.status };
            updateNoticeStatus(params)
            .then(res => {
                if(res.code == 1 || res.success){
                    this.notify("鐘舵€佸凡鏇存柊", "success");
                }else{
                    this.notify("鐘舵€佹洿鏂板け璐?, "error");
                }
            })
            .catch(err => {
                console.log(err);
            });
        },
        deleteRow(){
            delNotice(this.idx)
            .then(res => {
                if(res){
                    this.getData();
                    this.notify("鍒犻櫎鎴愬姛", "success");
                }else{
                    this.notify("鍒犻櫎澶辫触", "error");
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
        padding: 16px 18px;
        background: linear-gradient(135deg, rgba(59, 130, 246, 0.08), rgba(255, 255, 255, 0.95));
        border: 1px solid rgba(148, 163, 184, 0.25);
        border-radius: 14px;
        box-shadow: 0 10px 24px rgba(15, 23, 42, 0.08);
    }
    .handle-input{
        width: 320px;
        display: inline-block;
    }
    .pagination{
        display: flex;
        justify-content: center;
        margin-top: 16px;
    }
</style>
