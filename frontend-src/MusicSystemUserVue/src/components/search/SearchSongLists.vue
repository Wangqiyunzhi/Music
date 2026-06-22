<template>
  <div class="search-song-lists">
    <content-list v-if="pagedLists.length" :contentList="pagedLists" />
    <el-empty v-else description="没有找到相关歌单"></el-empty>

    <div class="pagination" v-if="lists.length">
      <el-pagination
        @current-change="handleCurrentChange"
        background
        layout="total, prev, pager, next"
        :current-page="currentPage"
        :page-size="pageSize"
        :total="lists.length"
      />
    </div>
  </div>
</template>

<script>
import ContentList from "../ContentList";
import { getSongListOfLikeTitle, getAllSongList } from "../../api/index";
import { mixin } from "../../mixins";

export default {
  name: "search-song-lists",
  components: { ContentList },
  mixins: [mixin],
  data() {
    return {
      lists: [],
      pageSize: 12,
      currentPage: 1,
      loading: false,
    };
  },
  computed: {
    keywords() {
      return (this.$route.query.keywords || "").trim();
    },
    pagedLists() {
      const start = (this.currentPage - 1) * this.pageSize;
      return this.lists.slice(start, start + this.pageSize);
    },
  },
  watch: {
    keywords: {
      immediate: true,
      handler() {
        this.currentPage = 1;
        this.fetchLists();
      },
    },
  },
  methods: {
    handleCurrentChange(val) {
      this.currentPage = val;
    },

    fetchLists() {
      if (!this.keywords) {
        this.lists = [];
        this.notify("请输入搜索内容", "warning");
        return;
      }

      this.loading = true;

      getSongListOfLikeTitle(this.keywords)
        .then(async (res) => {
          const arr = Array.isArray(res) ? res : [];
          if (arr.length) {
            this.lists = arr;
            return;
          }

          // ✅ 搜不到：回退展示全部歌单
          this.notify("没有找到相关歌单，已为你展示全部歌单", "warning");
          const all = await getAllSongList();
          this.lists = Array.isArray(all) ? all : [];
        })
        .catch(() => {
          this.lists = [];
          this.notify("歌单搜索失败", "error");
        })
        .finally(() => {
          this.loading = false;
        });
    },
  },
};
</script>

<style lang="scss" scoped>
@import "../../assets/css/search-song-lists.scss";
.pagination {
  display: flex;
  justify-content: center;
  margin-top: 16px;
}
</style>
