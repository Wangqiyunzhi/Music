<template>
    <div class="table">
        <div class="container">
            <div class="handle-box">
                <el-input
                    v-model="select_word"
                    placeholder="璇疯緭鍏ユ瓕鎵嬪悕"
                    class="handle-input"
                ></el-input>
                <br /><br />
                <el-button type="danger" style="font-size: 18px" @click="delAll"
                    >
                    鎵归噺鍒犻櫎</el-button
                >
                <el-button
                    type="success"
                    style="font-size: 18px"
                    @click="centerDialogVisible = true"
                    >
                    娣诲姞姝屾墜</el-button
                >
            </div>
        </div>
        <el-table
            ref="multipleTable"
            border
            style="width: 100%"
            height="680px"
            :data="data"
            @selection-change="handleSelectionChange"
        >
            <el-table-column type="selection" width="40"></el-table-column>
            <el-table-column label="姝屾墜鍥剧墖" width="140" align="center">
                <template slot-scope="scope">
                    <div class="singer-img">
                        <img :src="getUrl(scope.row.pic)" style="width: 100%" />
                    </div>
                    <el-upload
                        :action="uploadUrl(scope.row.id)"
                        :before-upload="beforeAvatorUpload"
                        :on-success="handleAvatorSuccess"
                    >
                        <el-button type="success" style="font-size: 18px;"
                            >
                             鏇存崲</el-button
                        >
                    </el-upload>
                </template>
            </el-table-column>
            <el-table-column
                prop="name"
                label="姝屾墜"
                width="120"
                align="center"
            ></el-table-column>
            <el-table-column label="鎬у埆" width="50" align="center">
                <template slot-scope="scope">
                    {{ changeSex(scope.row.sex) }}
                </template>
            </el-table-column>
            <el-table-column label="鐢熸棩" width="120" align="center">
                <template slot-scope="scope">
                    {{ attachBirth(scope.row.birth) }}
                </template>
            </el-table-column>
            <el-table-column
                prop="location"
                label="鍦板尯"
                width="100"
                align="center"
            ></el-table-column>
            <el-table-column label="绠€浠?>
                <template slot-scope="scope">
                    <p style="height: 100px; overflow: scroll">
                        {{ scope.row.introduction }}
                    </p>
                </template>
            </el-table-column>
            <el-table-column
                label="鎿嶄綔"
                width="440"
                fixed="right"
                align="center"
            >
                <template slot-scope="scope">
                    <el-button
                        style="font-size: 18px"
                        type="warning"
                        @click="songEdit(scope.row.id, scope.row.name)"
                        >
                        姝屾洸绠＄悊</el-button
                    >
                    <el-button
                        style="font-size: 18px"
                        type="success"
                        @click="handleEdit(scope.row)"
                        >
                        缂栬緫</el-button
                    >
                    <el-button
                        style="font-size: 18px"
                        type="danger"
                        @click="handleDelete(scope.row.id)"
                        >
                        鍒犻櫎</el-button
                    >
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

        <el-dialog
            title="娣诲姞姝屾墜"
            :visible.sync="centerDialogVisible"
            width="400px"
            center
        >
            <el-form
                :model="registerForm"
                ref="registerForm"
                label-width="80px"
            >
                <el-form-item prop="name" label="姝屾墜鍚? size="mini">
                    <el-input
                        v-model="registerForm.name"
                        placeholder="姝屾墜鍚?
                    ></el-input>
                </el-form-item>
                <el-form-item label="鎬у埆" size="mini">
                    <el-radio-group v-model="registerForm.sex">
                        <el-radio :label="0">濂?/el-radio>
                        <el-radio :label="1">鐢?/el-radio>
                        <el-radio :label="2">缁勫悎</el-radio>
                        <el-radio :label="3">涓嶆槑</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item prop="birth" label="鐢熸棩" size="mini">
                    <el-date-picker
                        type="date"
                        placeholder="閫夋嫨鏃ユ湡"
                        v-model="registerForm.birth"
                        style="width: 100%"
                    ></el-date-picker>
                </el-form-item>
                <el-form-item prop="location" label="鍦板尯" size="mini">
                    <el-input
                        v-model="registerForm.location"
                        placeholder="鍦板尯"
                    ></el-input>
                </el-form-item>
                <el-form-item prop="introduction" label="绠€浠? size="mini">
                    <el-input
                        v-model="registerForm.introduction"
                        placeholder="绠€浠?
                        type="textarea"
                    ></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer">
                <el-button size="mini" @click="centerDialogVisible = false"
                    > 鍙栨秷</el-button
                >
                <el-button size="mini" @click="addSinger" type="primary"> 纭畾</el-button>
            </span>
        </el-dialog>

        <el-dialog
            title="淇敼姝屾墜"
            :visible.sync="editVisible"
            width="400px"
            center
        >
            <el-form :model="form" ref="form" label-width="80px">
                <el-form-item prop="name" label="姝屾墜鍚? size="mini">
                    <el-input
                        v-model="form.name"
                        placeholder="姝屾墜鍚?
                    ></el-input>
                </el-form-item>
                <el-form-item label="鎬у埆" size="mini">
                    <el-radio-group v-model="form.sex">
                        <el-radio :label="0">濂?/el-radio>
                        <el-radio :label="1">鐢?/el-radio>
                        <el-radio :label="2">缁勫悎</el-radio>
                        <el-radio :label="3">涓嶆槑</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="鐢熸棩" size="mini">
                    <el-date-picker
                        type="date"
                        placeholder="閫夋嫨鏃ユ湡"
                        v-model="form.birth"
                        style="width: 100%"
                    ></el-date-picker>
                </el-form-item>
                <el-form-item prop="location" label="鍦板尯" size="mini">
                    <el-input
                        v-model="form.location"
                        placeholder="鍦板尯"
                    ></el-input>
                </el-form-item>
                <el-form-item prop="introduction" label="绠€浠? size="mini">
                    <el-input
                        v-model="form.introduction"
                        placeholder="绠€浠?
                        type="textarea"
                    ></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer">
                <el-button size="mini" @click="editVisible = false"
                    > 鍙栨秷</el-button
                >
                <el-button size="mini" @click="editSave" type="primary"> 纭畾</el-button>
            </span>
        </el-dialog>

        <el-dialog
            title="鍒犻櫎姝屾墜"
            :visible.sync="delVisible"
            width="300px"
            center
        >
            <div align="center">鍒犻櫎涓嶅彲鎭㈠锛屾槸鍚︾‘瀹氬垹闄わ紵</div>
            <span slot="footer">
                <el-button size="mini" @click="delVisible = false"
                    > 鍙栨秷</el-button
                >
                <el-button size="mini" @click="deleteRow" type="primary"> 纭畾</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
import { setSinger, getAllSinger, updateSinger, delSinger } from "../api/index";
import { mixin } from "../mixins/index";
export default {
    mixins: [mixin],
    data() {
        return {
            centerDialogVisible: false, //娣诲姞寮圭獥鏄惁鏄剧ず
            editVisible: false, //缂栬緫寮圭獥鏄惁鏄剧ず
            delVisible: false, //鍒犻櫎寮圭獥鏄惁鏄剧ず
            registerForm: {
                //娣诲姞妗?
                name: "",
                sex: "",
                birth: "",
                location: "",
                introduction: "",
            },
            form: {
                //缂栬緫妗?
                id: "",
                name: "",
                sex: "",
                birth: "",
                location: "",
                introduction: "",
            },
            tableData: [],
            tempData: [],
            select_word: "",
            pageSize: 5, //鍒嗛〉姣忛〉澶у皬
            currentPage: 1, //褰撳墠椤?
            idx: -1, //褰撳墠閫夋嫨椤?
            multipleSelection: [], //鍝簺椤瑰凡缁忔墦鍕?
        };
    },
    computed: {
        //璁＄畻褰撳墠鎼滅储缁撴灉琛ㄩ噷鐨勬暟鎹?
        data() {
            return this.tableData.slice(
                (this.currentPage - 1) * this.pageSize,
                this.currentPage * this.pageSize
            );
        },
    },
    watch: {
        //鎼滅储妗嗛噷闈㈢殑鍐呭鍙戠敓鍙樺寲鐨勬椂鍊欙紝鎼滅储缁撴灉table鍒楄〃鐨勫唴瀹硅窡鐫€瀹冪殑鍐呭鍙戠敓鍙樺寲
        select_word: function () {
            if (this.select_word == "") {
                this.tableData = this.tempData;
            } else {
                this.tableData = [];
                for (let item of this.tempData) {
                    if (item.name.includes(this.select_word)) {
                        this.tableData.push(item);
                    }
                }
            }
        },
    },
    created() {
        this.getData();
    },
    methods: {
        //鑾峰彇褰撳墠椤?
        handleCurrentChange(val) {
            this.currentPage = val;
        },
        //鏌ヨ鎵€鏈夋瓕鎵?
        getData() {
            this.tempData = [];
            this.tableData = [];
            getAllSinger().then((res) => {
                this.tempData = res;
                this.tableData = res;
                this.currentPage = 1;
            });
        },
        //娣诲姞姝屾墜
        addSinger() {
            let d = this.registerForm.birth;
            let datetime =
                d.getFullYear() + "-" + (d.getMonth() + 1) + "-" + d.getDate();
            let params = new URLSearchParams();
            params.append("name", this.registerForm.name);
            params.append("sex", this.registerForm.sex);
            params.append("pic", "/img/singerPic/hhh.jpg");
            params.append("birth", datetime);
            params.append("location", this.registerForm.location);
            params.append("introduction", this.registerForm.introduction);

            setSinger(params)
                .then((res) => {
                    if (res.code == 1) {
                        this.getData();
                        this.notify("娣诲姞鎴愬姛", "success");
                    } else {
                        this.notify("娣诲姞澶辫触", "error");
                    }
                })
                .catch((err) => {
                    console.log(err);
                });
            this.centerDialogVisible = false;
        },
        //寮瑰嚭缂栬緫椤甸潰
        handleEdit(row) {
            this.editVisible = true;
            this.form = {
                id: row.id,
                name: row.name,
                sex: row.sex,
                birth: row.birth,
                location: row.location,
                introduction: row.introduction,
            };
        },
        //淇濆瓨缂栬緫椤甸潰淇敼鐨勬暟鎹?
        editSave() {
            let d = new Date(this.form.birth);
            let datetime =
                d.getFullYear() + "-" + (d.getMonth() + 1) + "-" + d.getDate();
            let params = new URLSearchParams();
            params.append("id", this.form.id);
            params.append("name", this.form.name);
            params.append("sex", this.form.sex);
            params.append("birth", datetime);
            params.append("location", this.form.location);
            params.append("introduction", this.form.introduction);

            updateSinger(params)
                .then((res) => {
                    if (res.code == 1) {
                        this.getData();
                        this.notify("淇敼鎴愬姛", "success");
                    } else {
                        this.notify("淇敼澶辫触", "error");
                    }
                })
                .catch((err) => {
                    console.log(err);
                });
            this.editVisible = false;
        },
        //鏇存柊鍥剧墖
        uploadUrl(id) {
            return `${this.$store.state.HOST}/singer/updateSingerPic?id=${id}`;
        },
        //鍒犻櫎涓€鍚嶆瓕鎵?
        deleteRow() {
            delSinger(this.idx)
                .then((res) => {
                    if (res) {
                        this.getData();
                        this.notify("鍒犻櫎鎴愬姛", "success");
                    } else {
                        this.notify("鍒犻櫎澶辫触", "error");
                    }
                })
                .catch((err) => {
                    console.log(err);
                });
            this.delVisible = false;
        },
        //杞悜姝屾洸绠＄悊椤甸潰
        songEdit(id, name) {
            this.$router.push({ path: `/Song`, query: { id, name } });
        },
    },
};
</script>

<style scoped>
.handle-box {
    margin-bottom: 20px;
    padding: 16px 18px;
    background: linear-gradient(135deg, rgba(14, 165, 233, 0.08), rgba(255, 255, 255, 0.95));
    border: 1px solid rgba(148, 163, 184, 0.25);
    border-radius: 14px;
    box-shadow: 0 10px 24px rgba(15, 23, 42, 0.08);
}
.singer-img {
    width: 100%;
    height: 120px;
    border-radius: 10px;
    margin-bottom: 6px;
    overflow: hidden;
    border: 1px solid rgba(148, 163, 184, 0.25);
    background: #f8fafc;
}
.handle-input {
    width: 320px;
    display: inline-block;
}
.pagination {
    display: flex;
    justify-content: center;
    margin-top: 16px;
}
</style>
