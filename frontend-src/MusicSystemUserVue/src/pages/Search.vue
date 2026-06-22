<template>
  <div class="search">
    <nav class="searchList-nav">
      <span :class="{ isActive: toggle === 'Songs' }" @click="handleChangeView('Songs')">歌曲</span>
      <span :class="{ isActive: toggle === 'SongLists' }" @click="handleChangeView('SongLists')">歌单</span>
    </nav>

    <!-- 关键：切换时不丢关键词，子组件内部 watch $route.query.keywords 自动刷新 -->
    <component :is="currentView"></component>
  </div>
</template>

<script>
import SearchSongs from '../components/search/SearchSongs'
import SearchSongLists from '../components/search/SearchSongLists'

export default {
  name: 'search',
  components: {
    SearchSongs,
    SearchSongLists
  },
  data () {
    return {
      toggle: 'Songs',
      currentView: 'SearchSongs'
    }
  },
  watch: {
    // 如果你是从头部搜索框跳过来，有时会带不同 query，这里保证默认留在“歌曲”
    '$route.query.keywords': function () {
      // 不强制切换 tab，你想强制回歌曲就打开下一行
      // this.handleChangeView('Songs')
    }
  },
  methods: {
    handleChangeView (tab) {
      this.toggle = tab
      this.currentView = tab === 'Songs' ? 'SearchSongs' : 'SearchSongLists'
    }
  }
}
</script>

<style lang="scss" scoped>
@import '../assets/css/search.scss';
</style>
