<template>
  <div class="content-list">
    <ul class="section-content">
      <li class="content-item" v-for="(item, index) in contentList" :key="item.id || index">
        <div class="kuo" @click="goAlbum(item)">
          <img class="item-img" :src="attachImageUrl(item.pic)">
          <div class="mask">
            <svg class="icon">
              <use xlink:href="#icon-bofang"></use>
            </svg>
          </div>
        </div>
        <p class="item-name" style="font-size: 22px; font-weight: 600;">
          {{ item.name || item.title }}
        </p>
      </li>
    </ul>
  </div>
</template>

<script>
import { mixin } from '../mixins'

export default {
  name: 'content-list',
  mixins: [mixin],
  props: {
    contentList: {
      type: Array,
      default: () => []
    }
  },
  methods: {
    goAlbum (item) {
      // 先缓存当前点击对象（歌手 / 歌单详情页用得到）
      this.$store.commit('setTempList', item)

      // ✅ 用字段判断：有 name => 歌手；有 title => 歌单
      if (item && item.name) {
        // 歌手详情
        this.$router.push({ path: `/singer-album/${item.id}` })
      } else {
        // 歌单详情
        this.$router.push({ path: `/song-list-album/${item.id}` })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import '../assets/css/content-list.scss';
</style>
