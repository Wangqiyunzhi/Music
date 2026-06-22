<template>
  <div class="search-songs">
    <album-content v-if="pagedSongs.length" :songList="pagedSongs" />
    <el-empty v-else description="没有找到相关歌曲"></el-empty>

    <div class="pagination" v-if="songs.length">
      <el-pagination
        @current-change="handleCurrentChange"
        background
        layout="total, prev, pager, next"
        :current-page="currentPage"
        :page-size="pageSize"
        :total="songs.length"
      />
    </div>
  </div>
</template>

<script>
import AlbumContent from "../AlbumContent";
import { mixin } from "../../mixins";
import { likeSongOfName } from "../../api/index";

export default {
  name: "search-songs",
  components: { AlbumContent },
  mixins: [mixin],
  data() {
    return {
      songs: [],
      pageSize: 12,
      currentPage: 1,
      loading: false,
    };
  },
  computed: {
    keywords() {
      return (this.$route.query.keywords || "").trim();
    },
    pagedSongs() {
      const start = (this.currentPage - 1) * this.pageSize;
      return this.songs.slice(start, start + this.pageSize);
    },
  },
  watch: {
    keywords: {
      immediate: true,
      handler() {
        this.currentPage = 1;
        this.fetchSongs();
      },
    },
  },
  methods: {
    handleCurrentChange(val) {
      this.currentPage = val;
    },

    fetchSongs() {
      if (!this.keywords) {
        this.songs = [];
        this.$store.commit("setListOfSongs", []);
        this.notify("请输入搜索内容", "warning");
        return;
      }

      this.loading = true;
      likeSongOfName(this.keywords)
        .then((res) => {
          this.songs = Array.isArray(res) ? res : [];
          // ✅ 同步 store：给播放列表、上一首下一首用
          this.$store.commit("setListOfSongs", this.songs);

          if (!this.songs.length) {
            this.notify("没有找到相关歌曲", "warning");
          }
        })
        .catch(() => {
          this.songs = [];
          this.$store.commit("setListOfSongs", []);
          this.notify("歌曲搜索失败", "error");
        })
        .finally(() => {
          this.loading = false;
        });
    },
  },
};
</script>

<style lang="scss" scoped>
@import "../../assets/css/search-songs.scss";
.pagination {
  display: flex;
  justify-content: center;
  margin-top: 16px;
}
</style>
