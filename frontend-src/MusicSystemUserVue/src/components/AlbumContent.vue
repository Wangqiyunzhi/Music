<template>
  <div class="content">
    <h1 class="title">
      <slot name="title"></slot>
      <hr />
    </h1>

    <ul>
      <li>
        <div class="song-item">
          <span class="item-index"></span>
          <span class="item-title">歌曲名</span>
          <span class="item-name">歌手</span>
          <span class="item-intro">专辑</span>
        </div>
      </li>

      <li v-for="(item, index) in songList" :key="`${item.id || 'item'}-${index}`">
        <div class="song-item" @click="onClick(item, index)">
          <span class="item-index">{{ index + 1 }}</span>
          <span class="item-title">{{ replaceFName(item.name) }}</span>
          <span class="item-name">{{ replaceLName(item.name) }}</span>
          <span class="item-intro">{{ item.introduction }}</span>
        </div>
      </li>
    </ul>
  </div>
</template>

<script>
import { mixin } from "../mixins";

export default {
  name: "album-content",
  mixins: [mixin],
  props: {
    songList: { type: Array, default: function () { return []; } }
  },
  methods: {
    onClick: function (item, index) {
      // ✅ 关键：每次点击播放，都把当前列表写入播放队列
      if (this.$store && this.$store.commit) {
        this.$store.commit("setListOfSongs", this.songList || []);
      }

      // ✅ 点击就播放
      this.toplay(item.id, item.url, item.pic, index, item.name, item.lyric);
    }
  }
};
</script>

<style lang="scss" scoped>
@import "../assets/css/album-content.scss";
</style>
