<template>
    <div class="table">
        <div class="crumbs" style="padding: 20px 0 5px 20px;">
            <i class="iconfont icon-r-list" style="font-size: 24px;"> 评论信息</i>
        </div>
        <div class="container">
            <div class="handle-box">
                <el-input v-model="select_word" placeholder="筛选关键词" class="handle-input"></el-input>
                <br><br>
                <el-button type="danger" style="font-size: 18px;" @click="delAll"> 批量删除</el-button>
            </div>
        </div>
        <el-table ref="multipleTable" border style="width:100%" height="680px" :data="tableData" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="40"></el-table-column>
            <el-table-column prop="name" label="用户" align="center"></el-table-column>
            <el-table-column prop="content" label="评论内容" align="center"></el-table-column>
            <el-table-column label="操作" width="150" align="center">
                <template slot-scope="scope">
                    <el-button style="font-size: 18px;" type="danger" @click="handleDelete(scope.row)"> 删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <el-dialog title="删除评论" :visible.sync="delVisible" width="300px" center>
            <div align="center">删除不可恢复，是否确定删除？</div>
            <span slot="footer">
                <el-button size="mini" @click="delVisible = false"> 取消</el-button>
                <el-button size="mini" @click="deleteRow" type="primary"> 确定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
import { mixin } from '../mixins/index';
import {getUserOfId,getCommentPage,deleteComment} from '../api/index';

export default {
    mixins: [mixin],
    props: ['id'],
    data(){
        return{
            delVisible: false,
            tableData: [],
            tempData: [],
            select_word: '',
            idx: -1,
            multipleSelection: [],
        }
    },
    watch:{
        select_word: function(){
            if(this.select_word == ''){
                this.tableData = this.tempData;
            }else{
                this.tableData = [];
                for(let item of this.tempData){
                    if(item.name.includes(this.select_word)){
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
        getData(){
            this.tempData = [];
            this.tableData = [];
            const id = this.$route.query.id;
            const typeParam = this.$route.query.type;
            const type = (typeParam === 0 || typeParam === '0' || typeParam === 'song') ? 0 : 1;
            const params = { type, pageNo: 1, pageSize: 1000 };
            if (type === 0) {
                params.songId = id;
            } else {
                params.songListId = id;
            }
            getCommentPage(params).then(res => {
                let list = [];
                if (Array.isArray(res)) list = res;
                else if (res && Array.isArray(res.data)) list = res.data;
                else if (res && res.data && Array.isArray(res.data.data)) list = res.data.data;
                else if (res && Array.isArray(res.list)) list = res.list;
                else if (res && res.data && Array.isArray(res.data.list)) list = res.data.list;
                for(let item of list){
                    this.getUsers(item.userId,item);
                }
            })
        },
        getUsers(id,item){
            getUserOfId(id)
            .then(res => {
                let o = item;
                o.name = res.username;
                this.tempData.push(o);
                this.tableData.push(o);
            })
            .catch(err => {
                console.log(err);
            });
        },
        deleteRow(){
            if (!this.idx || !this.idx.id || !this.idx.userId) {
                this.notify("缺少用户信息，无法删除","error");
                this.delVisible = false;
                return;
            }
            const params = {
                id: this.idx.id,
                userId: this.idx.userId
            };
            deleteComment(params)
            .then(res => {
                if(res){
                    this.getData();
                    this.notify("删除成功","success");
                }else{
                    this.notify("删除失败","error");
                }
            })
            .catch(err => {
                console.log(err);
            });
            this.delVisible = false;
        },
        delAll(){
            for(let item of this.multipleSelection){
                this.handleDelete(item);
                this.deleteRow();
            }
            this.multipleSelection = [];
        }
    }
}
</script>

<style scoped>
    .handle-box{
        margin-bottom: 20px;
    }
    .handle-input{
        width: 300px;
        display: inline-block;
    }
</style>
