<template>
    <div class="table">
        <div class="container">
            <div class="handle-box">
                <el-input v-model="select_word" placeholder="筛选关键字" class="handle-input"></el-input>
                <br><br>
                <el-button type="danger" style="font-size: 18px;" @click="delAll"> 批量删除</el-button>
                <el-button type="success" style="font-size: 18px;" @click="centerDialogVisible = true"> 添加歌单</el-button>
            </div>
        </div>
        <el-table ref="multipleTable" border style="width:100%; color:#000;" height="680px" :data="data" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="40"></el-table-column>
            <el-table-column label="歌单图片" width="130" align="center">
                <template slot-scope="scope">
                    <div class="songList-img">
                        <img :src="getUrl(scope.row.pic)" style="width:100%; color:#000;"/>
                    </div>
                    <el-upload :action="uploadUrl(scope.row.id)" :before-upload="beforeAvatorUpload" :on-success="handleAvatorSuccess">
                        <el-button style="font-size: 18px;" type="success"> 更换</el-button>
                    </el-upload>
                </template>
            </el-table-column>
            <el-table-column prop="title" label="标题" width="120" align="center"></el-table-column>
            <el-table-column label="歌单名称" width="200" align="center">
                <template slot-scope="scope">
                    {{ scope.row.title || scope.row.name || scope.row.songListTitle || scope.row.songListName || scope.row.listTitle || scope.row.listName || ("ID:" + (scope.row.id || "")) }}
                </template>
            </el-table-column>
            <el-table-column label="简介">
                <template slot-scope="scope">
                    <p style="height:100px;overflow:scroll">{{scope.row.introduction}}</p>
                </template>
            </el-table-column>
            <el-table-column prop="style" label="风格" width="120" align="center"></el-table-column>
            <el-table-column label="操作" width="520" align="center">
                <template slot-scope="scope">
                    <el-button style="font-size: 18px;" type="warning" @click="songEdit(scope.row.id)"> 歌曲管理</el-button>
                    <el-button style="font-size: 18px;" @click="getComment(data[scope.$index].id)"> 评论</el-button>
                    <el-button style="font-size: 18px;" type="success" @click="handleEdit(scope.row)"> 编辑</el-button>
                    <el-button style="font-size: 18px;" type="danger" @click="handleDelete(scope.row.id)"> 删除</el-button>
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

        <el-dialog title="添加歌单" :visible.sync="centerDialogVisible" width="400px" center>
            <el-form :model="registerForm" ref="registerForm" label-width="80px">
                <el-form-item prop="title" label="标题" size="mini">
                    <el-input v-model="registerForm.title" placeholder="标题"></el-input>
                </el-form-item>
                <el-form-item prop="introduction" label="简介" size="mini">
                    <el-input v-model="registerForm.introduction" placeholder="简介" type="textarea"></el-input>
                </el-form-item>
                <el-form-item prop="style" label="风格" size="mini">
                    <el-select v-model="registerForm.style" placeholder="风格">
                        <el-option key="华语" label="华语" value="华语"></el-option>
                        <el-option key="粤语" label="粤语" value="粤语"></el-option>
                        <el-option key="欧美" label="欧美" value="欧美"></el-option>
                        <el-option key="日韩" label="日韩" value="日韩"></el-option>
                        <el-option key="轻音乐" label="轻音乐" value="轻音乐"></el-option>
                        <el-option key="BGM" label="BGM" value="BGM"></el-option>
                        <el-option key="乐器" label="乐器" value="乐器"></el-option>
                    </el-select>
                </el-form-item>
            </el-form>
            <span slot="footer">
                <el-button size="mini" @click="centerDialogVisible = false"> 取消</el-button>
                <el-button size="mini" @click="addSongList" type="primary"> 确定</el-button>
            </span>
        </el-dialog>

        <el-dialog title="修改歌单" :visible.sync="editVisible" width="400px" center>
            <el-form :model="form" ref="form" label-width="80px">
                <el-form-item prop="title" label="标题" size="mini">
                    <el-input v-model="form.title" placeholder="标题"></el-input>
                </el-form-item>
                <el-form-item prop="introduction" label="简介" size="mini">
                    <el-input v-model="form.introduction" placeholder="简介" type="textarea"></el-input>
                </el-form-item>
                <el-form-item prop="style" label="风格" size="mini">
                    <el-select v-model="form.style" placeholder="风格">
                        <el-option key="华语" label="华语" value="华语"></el-option>
                        <el-option key="粤语" label="粤语" value="粤语"></el-option>
                        <el-option key="欧美" label="欧美" value="欧美"></el-option>
                        <el-option key="日韩" label="日韩" value="日韩"></el-option>
                        <el-option key="轻音乐" label="轻音乐" value="轻音乐"></el-option>
                        <el-option key="BGM" label="BGM" value="BGM"></el-option>
                        <el-option key="乐器" label="乐器" value="乐器"></el-option>
                    </el-select>
                </el-form-item>
            </el-form>
            <span slot="footer">
                <el-button size="mini" @click="editVisible = false"> 取消</el-button>
                <el-button size="mini" @click="editSave" type="primary"> 确定</el-button>
            </span>
        </el-dialog>

        <el-dialog title="删除歌单" :visible.sync="delVisible" width="300px" center>
            <div align="center">删除不可恢复，是否确定删除？</div>
            <span slot="footer">
                <el-button size="mini" @click="delVisible = false"> 取消</el-button>
                <el-button size="mini" @click="deleteRow" type="primary"> 确定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
import {getAllSongList,setSongList,updateSongList,delSongList} from '../api/index';
import { mixin } from '../mixins/index';
export default {
    mixins: [mixin],
    data(){
        return{
            centerDialogVisible: false,
            editVisible: false,
            delVisible: false,
            registerForm:{
                title: '',
                introduction: '',
                style: ''
            },
            form:{
                id: '',
                title: '',
                introduction: '',
                style: ''
            },
            tableData: [],
            tempData: [],
            select_word: '',
            pageSize: 5,
            currentPage: 1,
            idx: -1,
            multipleSelection: []
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
                    if(item.title.includes(this.select_word)){
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
        getData(){
            this.tempData = [];
            this.tableData = [];
            getAllSongList().then(res => {
                let arr = [];
                if (Array.isArray(res)) arr = res;
                else if (res && Array.isArray(res.data)) arr = res.data;
                else if (res && res.data && Array.isArray(res.data.data)) arr = res.data.data;
                else if (res && res.data && Array.isArray(res.data.list)) arr = res.data.list;
                else if (res && res.data && Array.isArray(res.data.records)) arr = res.data.records;
                else if (res && res.data && Array.isArray(res.data.rows)) arr = res.data.rows;
                else if (res && Array.isArray(res.list)) arr = res.list;
                else if (res && Array.isArray(res.records)) arr = res.records;
                else if (res && Array.isArray(res.rows)) arr = res.rows;

                if (typeof console !== "undefined" && console.info) {
                    console.info("SongListPage getData raw:", res);
                    console.info("SongListPage getData parsed length:", arr.length);
                }

                const normalized = (arr || []).map((item) => {
                    const it = item || {};
                    const title =
                        it.title ||
                        it.name ||
                        it.songListTitle ||
                        it.songListName ||
                        it.listTitle ||
                        it.listName ||
                        (it.id !== undefined && it.id !== null ? `ID:${it.id}` : "");
                    const introduction =
                        it.introduction ||
                        it.intro ||
                        it.desc ||
                        it.description ||
                        it.detail ||
                        it.content ||
                        "";
                    const style =
                        it.style ||
                        it.type ||
                        it.genre ||
                        it.tag ||
                        it.category ||
                        "";
                    const pic =
                        it.pic ||
                        it.cover ||
                        it.image ||
                        it.img ||
                        it.picUrl ||
                        it.coverUrl ||
                        it.avatar ||
                        "";
                    return {
                        ...it,
                        title,
                        introduction,
                        style,
                        pic,
                    };
                });

                this.tempData = normalized;
                this.tableData = normalized;
                this.currentPage = 1;

                if (!normalized.length && res && res.msg) {
                    this.notify && this.notify(res.msg, "error");
                }
            })
        },
        addSongList(){
            let params = new URLSearchParams();
            params.append('title',this.registerForm.title);
            params.append('pic','/img/songListPic/123.jpg');
            params.append('introduction',this.registerForm.introduction);
            params.append('style',this.registerForm.style);

            setSongList(params)
            .then(res => {
                if(res.code == 1){
                    this.getData();
                    this.notify("添加成功","success");
                }else{
                    this.notify("添加失败","error");
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
                introduction: row.introduction,
                style: row.style
            }
        },
        editSave(){
            let params = new URLSearchParams();
            params.append('id',this.form.id);
            params.append('title',this.form.title);
            params.append('introduction',this.form.introduction);
            params.append('style',this.form.style);

            updateSongList(params)
            .then(res => {
                if(res.code == 1){
                    this.getData();
                    this.notify("修改成功","success");
                }else{
                    this.notify("修改失败","error");
                }
            })
            .catch(err => {
                console.log(err);
            });
            this.editVisible = false;
        },
        uploadUrl(id){
            return `${this.$store.state.HOST}/songList/updateSongListPic?id=${id}`
        },
        deleteRow(){
            delSongList(this.idx)
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
        songEdit(id){
            this.$router.push({path:`/ListSong`,query:{id}});
        },
        getComment(id){
            this.$router.push({path: '/Comment',query:{id, type: 1}})
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
    .songList-img{
        width: 100%;
        height: 100px;
        border-radius: 10px;
        margin-bottom: 6px;
        overflow: hidden;
        border: 1px solid rgba(148, 163, 184, 0.25);
        background: #f8fafc;
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
