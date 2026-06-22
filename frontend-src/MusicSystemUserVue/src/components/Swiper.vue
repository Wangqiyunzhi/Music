<!--
 * @Description: 
 * @Author: Rabbiter
 * @Date: 2023-03-16 01:26:34
-->
<template>
    <div class="swiper">
        <el-carousel :interval="200000" type="card" height="280px">
            <el-carousel-item v-for="(item,index) in swiperList" :key="index">
                <img :src="item.picImg" />
            </el-carousel-item>
        </el-carousel>
    </div>
</template>

<script>
import { swiperList } from '../assets/data/swiper'
import { getBannerHome } from '../api/index'
export default {
  name: 'swiper',
  data () {
    return {
      swiperList: []
    }
  },
  created () {
    const _this = this
    getBannerHome()
      .then(function (res) {
        const arr = Array.isArray(res) ? res : (res && Array.isArray(res.data) ? res.data : [])
        if (arr && arr.length) {
          _this.swiperList = arr.map(function (b) {
            const pic = b.pic || b.picImg || b.image || b.img || b.cover || ''
            const url = pic ? _this.$store.state.configure.HOST + pic : ''
            return { picImg: url ? encodeURI(url) : '' }
          })
        } else {
          _this.swiperList = swiperList
        }
      })
      .catch(function () {
        _this.swiperList = swiperList
      })
  }
}
</script>

<style lang="scss" scoped>
@import '../assets/css/swiper.scss';
</style>
