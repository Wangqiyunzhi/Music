<template>
    <div class="bg">
        <div class="signin">
            <div class="signUp-head">
                <span>账号登录</span>
                <p class="signUp-subtitle">欢迎回来，请登录你的管理员账号</p>
            </div>

            <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="70px">
                <el-form-item prop="username" label="用户名">
                    <el-input
                        v-model="ruleForm.username"
                        placeholder="用户名"
                        @keyup.enter.native="submitForm"
                    ></el-input>
                </el-form-item>

                <el-form-item prop="password" label="密码">
                    <el-input
                        type="password"
                        v-model="ruleForm.password"
                        placeholder="密码"
                        @keyup.enter.native="submitForm"
                    ></el-input>
                </el-form-item>

                <div class="login-btn">
                    <el-button
                        class="customer text-only-btn"
                        type="primary"
                        @click="submitForm"
                        style="font-size: 20px;"
                    >登录</el-button>
                </div>
            </el-form>
        </div>
    </div>
</template>

<script>
import { mixin } from "../mixins/index";
import { getLoginStatus } from "../api/index";

export default {
    mixins: [mixin],
    data() {
        return {
            ruleForm: {
                username: "",
                password: ""
            },
            rules: {
                username: [
                    { required: true, message: "请输入用户名", trigger: "blur" }
                ],
                password: [
                    { required: true, message: "请输入密码", trigger: "blur" }
                ]
            }
        };
    },
    methods: {
        submitForm() {
            const params = {
                name: this.ruleForm.username,
                password: this.ruleForm.password
            };
            getLoginStatus(params)
                .then((res) => {
                    if (res.code == 1) {
                        localStorage.setItem("userName", this.ruleForm.username);
                        this.$router.push("/Info");
                        this.notify("登录成功", "success");
                    } else {
                        this.notify(res.msg, "error");
                    }
                })
                .catch((e) => {
                    if (e.data == undefined) {
                        if (
                            e ==
                            "TypeError: Cannot read properties of undefined (reading 'status')"
                        ) {
                            e = "Network Error";
                        }
                        this.$message({
                            showClose: true,
                            message: e,
                            type: "error",
                            duration: 5000
                        });
                    } else {
                        this.$message({
                            showClose: true,
                            message: e.data,
                            type: "error",
                            duration: 5000
                        });
                    }
                });
        }
    }
};
</script>

<style scoped>
.bg {
    width: 100%;
    height: 100%;
    min-height: 100vh;
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 120px 20px 40px;
    background:
        radial-gradient(1200px 600px at 15% 10%, rgba(59, 130, 246, 0.18), transparent 60%),
        radial-gradient(900px 500px at 85% 20%, rgba(56, 189, 248, 0.16), transparent 60%),
        linear-gradient(180deg, #f8fafc 0%, #eef2ff 100%);
}

.signin {
    background-color: rgba(255, 255, 255, 0.92);
    border-radius: 20px;
    width: 420px;
    padding: 32px 30px 28px;
    border: 1px solid rgba(226, 232, 240, 0.9);
    box-shadow: 0 18px 45px rgba(15, 23, 42, 0.12);
}

.signUp-head {
    text-align: center;
    margin-bottom: 10px;
    font-size: 22px;
    font-weight: 600;
}

.signUp-subtitle {
    margin-top: 6px;
    font-size: 13px;
    color: #6b7280;
    font-weight: 400;
}

.login-btn {
    display: flex;
    justify-content: center;
    margin-top: 6px;
}

.login-btn .el-button {
    width: 100%;
    height: 42px;
}

::v-deep .text-only-btn i,
::v-deep .text-only-btn .iconfont,
::v-deep .text-only-btn [class*="el-icon"] {
    display: none !important;
}

::v-deep .text-only-btn span {
    margin-left: 0 !important;
}
</style>
