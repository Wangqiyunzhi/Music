<template>
  <div class="bg">
    <div class="signin">
      <div class="signUp-head"><span>重置密码</span></div>

      <el-form :model="form" ref="form" label-width="70px" :rules="rules">
        <el-form-item prop="username" label="用户名">
          <el-input v-model="form.username" placeholder="请输入用户名"></el-input>
        </el-form-item>

        <div class=".signUp-mimachushi"><span>密码初始为123</span> </div>
        <div class="login-btn">
          <el-button @click="goBack" style="font-size: 20px;">返回登录</el-button>
          <el-button type="primary" @click="handleReset" style="font-size: 20px;">重置密码</el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
import { mixin } from '../mixins'
import { resetUserPassword } from '../api/index'

export default {
  name: 'forgot-password',
  mixins: [mixin],
  data() {
    return {
      form: { username: '' },
      rules: {
        username: [{ required: true, trigger: 'blur', message: '请输入用户名' }]
      }
    }
  },
  methods: {
    goBack() {
      this.$router.push({ path: '/login-in' })
    },
    handleReset() {
      const _this = this
      if (!_this.$refs.form) return
      _this.$refs.form.validate(function (ok) {
        if (!ok) return
        const params = new URLSearchParams()
        params.append('username', _this.form.username)
        resetUserPassword(params)
          .then(function (res) {
            if (res && res.code === 1) {
              _this.notify('密码已重置为123', 'success')
              _this.$router.replace('/login-in')
            } else {
              _this.notify((res && res.msg) ? res.msg : '重置失败', 'error')
            }
          })
          .catch(function () {
            _this.notify('Network Error', 'error')
          })
      })
    }
  }
}
</script>

<style lang="scss" scoped>
@import "../assets/css/var.scss";
@import "../assets/css/global.scss";

.bg{
  background:url("../../src/assets/img/bg.jpg") no-repeat 5px 5px;
  width:100%;
  height:100%;
  background-size: cover;
  background-position: center 0;
}
.signin {
  background-color: rgba(245, 249, 250,.75);
  border-radius: 10px;
  width: 350px;
  margin: 265px auto;
  padding: 30px 30px;

  .signUp-head {
    text-align: center;
    margin-bottom: 10px;
    font-size: 20px;
    font-weight: 600;
  }
  .login-btn {
    @include layout(space-between);
    button { display: block; width: 50%; }
  }



}
</style>
