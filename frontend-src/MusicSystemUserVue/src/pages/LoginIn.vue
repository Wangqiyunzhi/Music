<template>
  <div class="bg">
    <div class="signin">
      <div class="signUp-head">
        <span>账号登录</span>
        <p class="signUp-subtitle">欢迎回来，请登录你的账号</p>
      </div>

      <el-form :model="loginForm" ref="loginForm" label-width="70px" :rules="rules">
        <el-form-item prop="username" label="用户名">
          <el-input v-model="loginForm.username" placeholder="用户名"></el-input>
        </el-form-item>

        <el-form-item prop="password" label="密码">
          <el-input
            type="password"
            v-model="loginForm.password"
            placeholder="密码"
            @keyup.enter.native="handleLoginIn"
          />
        </el-form-item>

        <div class="login-tools">
          <el-link type="primary" :underline="false" @click="handleForgot">忘记密码？</el-link>
        </div>

        <div class="login-btn">
          <el-button @click="goSignUp" style="font-size: 20px;">注册</el-button>
          <el-button type="primary" @click="handleLoginIn" style="font-size: 20px;">登录</el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
import { mixin } from '../mixins'
import { loginIn } from '../api/index'

export default {
  name: 'login-in',
  mixins: [mixin],
  data() {
    return {
      loginForm: { username: '', password: '' },
      rules: {
        username: [{ required: true, trigger: 'blur', message: '请输入用户名' }],
        password: [{ required: true, trigger: 'blur', message: '请输入密码' }]
      }
    }
  },
  mounted() {
    this.$store.commit('setActiveName', '登录')
  },
  methods: {
    handleForgot() {
      this.$router.push({ path: '/forgot-password' })
    },
    handleLoginIn() {
      const _this = this

      if (!_this.$refs.loginForm) {
        _this.notify('表单未初始化', 'error')
        return
      }

      _this.$refs.loginForm.validate(function (ok) {
        if (!ok) return

        if (typeof loginIn !== 'function') {
          console.error('loginIn is not a function =>', loginIn)
          _this.notify('接口未正确导入：loginIn 不是函数（检查 src/api/index.js 导出）', 'error')
          return
        }

        const params = new URLSearchParams()
        params.append('username', _this.loginForm.username)
        params.append('password', _this.loginForm.password)

        loginIn(params)
          .then(function (res) {
            if (res && res.code === 1) {
              const user = res.userMsg || {}

              _this.notify('登录成功', 'success')

              _this.$store.commit('setLoginIn', true)
              _this.$store.commit('setUserId', user.id || '')
              _this.$store.commit('setUsername', user.username || _this.loginForm.username)
              _this.$store.commit('setAvator', user.avator || '/img/user.jpg')

              localStorage.setItem('userId', String(user.id || ''))
              localStorage.setItem('userName', String(user.username || _this.loginForm.username))
              localStorage.setItem('avator', String(user.avator || '/img/user.jpg'))

              const redirect =
                (_this.$route.query && _this.$route.query.redirect)
                  ? _this.$route.query.redirect
                  : '/'

              _this.$store.commit('setActiveName', '首页')
              _this.$router.replace(redirect)
            } else {
              _this.notify((res && res.msg) ? res.msg : '账号或密码错误', 'error')
            }
          })
          .catch(function (err) {
            console.error('loginIn error =>', err)
            _this.notify('Network Error', 'error')
          })
      })
    },
    goSignUp() {
      this.$store.commit('setActiveName', '注册')
      this.$router.push({ path: '/sign-up' })
    }
  }
}
</script>

<style lang="scss" scoped>
@import "../assets/css/var.scss";
@import "../assets/css/global.scss";

.bg{
  width:100%;
  height:100%;
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

  .signUp-head {
    text-align: center;
    margin-bottom: 10px;
    font-size: 22px;
    font-weight: 600;
  }
  .signUp-subtitle {
    margin-top: 6px;
    font-size: 13px;
    color: $color-grey;
    font-weight: 400;
  }

  .login-tools {
    display: flex;
    justify-content: flex-end;
    margin: 4px 0 10px;
  }

  .login-btn {
    @include layout(space-between);
    gap: 12px;
    button { display: block; width: 50%; height: 42px; }
  }
}
</style>

