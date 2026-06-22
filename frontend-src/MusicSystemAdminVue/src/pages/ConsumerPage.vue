<template>
    <div class="table">
        <div class="container">
            <div class="handle-box">
                <el-input
                    v-model="select_word"
                    placeholder="筛选相关用户"
                    class="handle-input"
                ></el-input>
                <br /><br />
                <el-button type="danger" style="font-size: 18px" @click="delAll">批量删除</el-button>
                <el-button type="success" style="font-size: 18px" @click="centerDialogVisible = true">添加新用户</el-button>
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
            <el-table-column label="用户图片" width="130" align="center">
                <template slot-scope="scope">
                    <div class="consumer-img">
                        <img :src="getUrl(scope.row.avator)" style="width: 100%" />
                    </div>
                    <el-upload
                        :action="uploadUrl(scope.row.id)"
                        :before-upload="beforeAvatorUpload"
                        :on-success="handleAvatorSuccess"
                    >
                        <el-button style="font-size: 18px" type="success">更换</el-button>
                    </el-upload>
                </template>
            </el-table-column>
            <el-table-column prop="username" label="用户名" width="120" align="center"></el-table-column>
            <el-table-column label="性别" width="60" align="center">
                <template slot-scope="scope">
                    {{ changeSex(scope.row.sex) }}
                </template>
            </el-table-column>
            <el-table-column prop="phoneNum" label="手机号" width="120" align="center"></el-table-column>
            <el-table-column prop="email" label="电子邮箱" width="240" align="center"></el-table-column>
            <el-table-column label="生日" width="120" align="center">
                <template slot-scope="scope">
                    {{ attachBirth(scope.row.birth) }}
                </template>
            </el-table-column>
            <el-table-column prop="introduction" label="签名" align="center"></el-table-column>
            <el-table-column prop="location" label="地区" width="100" align="center"></el-table-column>
            <el-table-column label="操作" width="420" fixed="right" align="center">
                <template slot-scope="scope">
                    <el-button style="font-size: 18px" type="warning" @click="getCollect(data[scope.$index].id)">收藏</el-button>
                    <el-button type="success" style="font-size: 18px" @click="handleEdit(scope.row)">编辑</el-button>
                    <el-button style="font-size: 18px" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
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
            ></el-pagination>
        </div>

        <el-dialog title="添加新用户" :visible.sync="centerDialogVisible" width="400px" center>
            <el-form :model="registerForm" ref="registerForm" label-width="80px" :rules="rules">
                <el-form-item prop="username" label="用户名" size="mini">
                    <el-input v-model="registerForm.username" placeholder="用户名"></el-input>
                </el-form-item>
                <el-form-item prop="password" label="密码" size="mini">
                    <el-input type="password" v-model="registerForm.password" placeholder="密码"></el-input>
                </el-form-item>
                <el-form-item label="性别" size="mini">
                    <input type="radio" name="sex" value="0" v-model="registerForm.sex" />&nbsp;女&nbsp;&nbsp;
                    <input type="radio" name="sex" value="1" v-model="registerForm.sex" />&nbsp;男
                </el-form-item>
                <el-form-item prop="phoneNum" label="手机号" size="mini">
                    <el-input v-model="registerForm.phoneNum" placeholder="手机号"></el-input>
                </el-form-item>
                <el-form-item prop="email" label="电子邮箱" size="mini">
                    <el-input v-model="registerForm.email" placeholder="电子邮箱"></el-input>
                </el-form-item>
                <el-form-item prop="birth" label="生日" size="mini">
                    <el-date-picker type="date" placeholder="选择日期" v-model="registerForm.birth" style="width: 100%"></el-date-picker>
                </el-form-item>
                <el-form-item prop="introduction" label="签名" size="mini">
                    <el-input v-model="registerForm.introduction" placeholder="签名"></el-input>
                </el-form-item>
                <el-form-item prop="location" label="地区" size="mini">
                    <el-input v-model="registerForm.location" placeholder="地区"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer">
                <el-button size="mini" @click="centerDialogVisible = false">取消</el-button>
                <el-button size="mini" @click="addConsumer" type="primary">确定</el-button>
            </span>
        </el-dialog>

        <el-dialog title="修改用户" :visible.sync="editVisible" width="400px" center>
            <el-form :model="form" ref="form" label-width="80px" :rules="rules">
                <el-form-item prop="username" label="用户名" size="mini">
                    <el-input v-model="form.username" placeholder="用户名"></el-input>
                </el-form-item>
                <el-form-item prop="password" label="密码" size="mini">
                    <el-input type="password" v-model="form.password" placeholder="密码"></el-input>
                </el-form-item>
                <el-form-item label="性别" size="mini">
                    <input type="radio" name="sex" value="0" v-model="form.sex" />&nbsp;女&nbsp;&nbsp;
                    <input type="radio" name="sex" value="1" v-model="form.sex" />&nbsp;男
                </el-form-item>
                <el-form-item prop="phoneNum" label="手机号" size="mini">
                    <el-input v-model="form.phoneNum" placeholder="手机号"></el-input>
                </el-form-item>
                <el-form-item prop="email" label="电子邮箱" size="mini">
                    <el-input v-model="form.email" placeholder="电子邮箱"></el-input>
                </el-form-item>
                <el-form-item prop="birth" label="生日" size="mini">
                    <el-date-picker type="date" placeholder="选择日期" v-model="form.birth" style="width: 100%"></el-date-picker>
                </el-form-item>
                <el-form-item prop="introduction" label="签名" size="mini">
                    <el-input v-model="form.introduction" placeholder="签名"></el-input>
                </el-form-item>
                <el-form-item prop="location" label="地区" size="mini">
                    <el-input v-model="form.location" placeholder="地区"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer">
                <el-button size="mini" @click="editVisible = false">取消</el-button>
                <el-button size="mini" @click="editSave" type="primary">确定</el-button>
            </span>
        </el-dialog>

        <el-dialog title="删除用户" :visible.sync="delVisible" width="300px" center>
            <div align="center">删除不可恢复，是否确定删除？</div>
            <span slot="footer">
                <el-button size="mini" @click="delVisible = false">取消</el-button>
                <el-button size="mini" @click="deleteRow" type="primary">确定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
import { getAllConsumer, setConsumer, updateConsumer, delConsumer } from "../api/index";
import { mixin } from "../mixins/index";

export default {
    mixins: [mixin],
    data() {
        return {
            centerDialogVisible: false,
            editVisible: false,
            delVisible: false,
            registerForm: {
                username: "",
                password: "",
                sex: "1",
                phoneNum: "",
                email: "",
                birth: "",
                introduction: "",
                location: ""
            },
            form: {
                id: "",
                username: "",
                password: "",
                sex: "",
                phoneNum: "",
                email: "",
                birth: "",
                introduction: "",
                location: ""
            },
            tableData: [],
            tempData: [],
            select_word: "",
            pageSize: 5,
            currentPage: 1,
            idx: -1,
            multipleSelection: [],
            rules: {
                username: [{ required: true, message: "请输入用户名", trigger: "blur" }],
                password: [{ required: true, message: "请输入密码", trigger: "blur" }],
                email: [
                    { type: "email", message: "请输入正确的电子邮箱地址", trigger: ["blur", "change"] }
                ]
            }
        };
    },
    computed: {
        data() {
            return this.tableData.slice((this.currentPage - 1) * this.pageSize, this.currentPage * this.pageSize);
        }
    },
    watch: {
        select_word() {
            if (this.select_word === "") {
                this.tableData = this.tempData;
            } else {
                this.tableData = this.tempData.filter((item) => item.username.includes(this.select_word));
            }
        }
    },
    created() {
        this.getData();
    },
    methods: {
        handleCurrentChange(val) {
            this.currentPage = val;
        },
        getData() {
            this.tempData = [];
            this.tableData = [];
            getAllConsumer().then((res) => {
                this.tempData = res;
                this.tableData = res;
                this.currentPage = 1;
            });
        },
        addConsumer() {
            this.registerForm.username = (this.registerForm.username || "").trim();
            this.registerForm.password = (this.registerForm.password || "").trim();
            this.$refs["registerForm"].validate((valid) => {
                if (!valid) return;

                const params = {
                    username: this.registerForm.username,
                    password: this.registerForm.password,
                    sex: this.registerForm.sex,
                    phoneNum: this.registerForm.phoneNum || "",
                    email: this.registerForm.email || "",
                    introduction: this.registerForm.introduction || "",
                    location: this.registerForm.location || "",
                    avator: "/img/user.jpg"
                };
                if (this.registerForm.birth) {
                    let d = new Date(this.registerForm.birth);
                    let datetime = d.getFullYear() + "-" + (d.getMonth() + 1) + "-" + d.getDate();
                    params.birth = datetime;
                }

                setConsumer(params)
                    .then((res) => {
                        if (res.code == 1) {
                            this.getData();
                            this.notify("添加成功", "success");
                            this.centerDialogVisible = false;
                        } else {
                            const msgMap = {
                                "username exists": "用户名已存在",
                                "username required": "请输入用户名",
                                "password required": "请输入密码",
                                "add failed": "添加失败"
                            };
                            this.notify(msgMap[res.msg] || res.msg || "添加失败", "error");
                        }
                    })
                    .catch(() => {
                        this.notify("网络异常，添加失败", "error");
                    });
            });
        },
        handleEdit(row) {
            this.editVisible = true;
            this.form = {
                id: row.id,
                username: row.username,
                password: row.password,
                sex: row.sex,
                phoneNum: row.phoneNum,
                email: row.email,
                birth: row.birth,
                introduction: row.introduction,
                location: row.location
            };
        },
        editSave() {
            this.$refs["form"].validate((valid) => {
                if (!valid) return;

                let d = this.form.birth ? new Date(this.form.birth) : new Date();
                let datetime = d.getFullYear() + "-" + (d.getMonth() + 1) + "-" + d.getDate();
                const params = {
                    id: this.form.id,
                    username: (this.form.username || "").trim(),
                    password: (this.form.password || "").trim(),
                    sex: this.form.sex,
                    phoneNum: this.form.phoneNum || "",
                    email: this.form.email || "",
                    birth: datetime,
                    introduction: this.form.introduction || "",
                    location: this.form.location || ""
                };

                updateConsumer(params)
                    .then((res) => {
                        if (res.code == 1) {
                            this.getData();
                            this.notify("修改成功", "success");
                        } else {
                            this.notify("修改失败", "error");
                        }
                    })
                    .catch(() => {
                        this.notify("网络异常，修改失败", "error");
                    });
                this.editVisible = false;
            });
        },
        uploadUrl(id) {
            return `${this.$store.state.HOST}/consumer/updateConsumerPic?id=${id}`;
        },
        deleteRow() {
            delConsumer(this.idx)
                .then((res) => {
                    if (res) {
                        this.getData();
                        this.notify("删除成功", "success");
                    } else {
                        this.notify("删除失败", "error");
                    }
                })
                .catch(() => {
                    this.notify("网络异常，删除失败", "error");
                });
            this.delVisible = false;
        },
        getCollect(id) {
            this.$router.push({ path: "/collect", query: { id } });
        }
    }
};
</script>

<style scoped>
.handle-box {
    margin-bottom: 20px;
}
.consumer-img {
    width: 100%;
    height: 110px;
    border-radius: 5px;
    margin-bottom: 5px;
    overflow: hidden;
}
.handle-input {
    width: 300px;
    display: inline-block;
}
.pagination {
    display: flex;
    justify-content: center;
}
</style>
