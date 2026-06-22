<template>
  <div class="bg">
    <div class="signUp">
      <div class="signUp-head"><span>用户注册</span></div>

      <el-form :model="registerForm" ref="registerForm" label-width="70px" :rules="rules">
        <el-form-item prop="username" label="用户名">
          <el-input v-model="registerForm.username" placeholder="用户名"></el-input>
        </el-form-item>

        <el-form-item prop="password" label="密码">
          <el-input type="password" v-model="registerForm.password" placeholder="密码"></el-input>
        </el-form-item>

        <el-form-item prop="sex" label="性别">
          <el-radio-group v-model="registerForm.sex">
            <el-radio :label="0">女</el-radio>
            <el-radio :label="1">男</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item prop="phoneNum" label="手机">
          <el-input v-model="registerForm.phoneNum" placeholder="手机"></el-input>
        </el-form-item>

        <el-form-item prop="email" label="邮箱">
          <el-input v-model="registerForm.email" placeholder="邮箱"></el-input>
        </el-form-item>

        <el-form-item prop="birth" label="生日">
          <el-date-picker
            v-model="registerForm.birth"
            type="date"
            placeholder="选择日期"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
            style="width: 100%;"
          />
        </el-form-item>

        <el-form-item prop="introduction" label="签名">
          <el-input v-model="registerForm.introduction" placeholder="签名"></el-input>
        </el-form-item>

        <el-form-item prop="location" label="地区">
          <el-select v-model="registerForm.location" placeholder="地区" style="width: 100%;">
            <el-option v-for="item in cities" :key="item.value" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>

        <div class="login-btn">
          <el-button @click="goback(-1)" style="font-size: 20px;">取消</el-button>
          <el-button type="primary" @click="handleSignUp" style="font-size: 20px;">确定</el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
import { rules, cities } from '../assets/data/form'
import { mixin } from '../mixins'
import { SignUp as apiSignUp } from '../api/index'

export default {
  name: 'sign-up',
  mixins: [mixin],
  data() {
    return {
      registerForm: {
        username: '',
        password: '',
        sex: '',
        phoneNum: '',
        email: '',
        birth: '',         // ✅ 这里就是 'yyyy-MM-dd' 字符串
        introduction: '',
        location: ''
      },
      cities: cities,
      rules: rules
    }
  },
  methods: {
    handleSignUp() {
      var _this = this
      _this.$refs.registerForm.validate(function(ok){
        if (!ok) return

        var params = new URLSearchParams()
        params.append('username', _this.registerForm.username)
        params.append('password', _this.registerForm.password)
        params.append('sex', _this.registerForm.sex)
        params.append('phoneNum', _this.registerForm.phoneNum)
        params.append('email', _this.registerForm.email)
        params.append('birth', _this.registerForm.birth || '') // ✅ 直接发字符串
        params.append('introduction', _this.registerForm.introduction)
        params.append('location', _this.registerForm.location)
        params.append('avator', '/img/user.jpg')

        apiSignUp(params).then(function(res){
          if (res && res.code === 1) {
            _this.notify('注册成功', 'success')
            _this.$router.push({ path: '/login-in' })
          } else {
            _this.notify((res && res.msg) ? res.msg : '注册失败', 'error')
          }
        }).catch(function(){
          _this.notify('注册失败', 'error')
        })
      })
    },
    goback(n) {
      this.$router.go(n)
    }
  }
}
</script>

<style lang="scss" scoped>
@import "../assets/css/var.scss";
@import "../assets/css/global.scss";
.bg{
  background:url("../../static/bg.jpg") no-repeat 5px 5px;
  width:100%;
  height:100%;
  background-size: cover;
  background-position: center 0;
  background-color: rgba(245, 249, 250,.75);
}
.signUp {
  background-color: rgba(245, 249, 250,.75);
  border-radius: 10px;
  width: 350px;
  margin: 110px auto 45px;
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
