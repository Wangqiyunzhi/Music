<template>
  <div class="home">
    <swiper />

    <div class="notice-section">
      <div class="notice-header">
        <div class="notice-title">通知公告</div>
        <div class="notice-more" @click="goNotice">更多</div>
      </div>
      <ul class="notice-list" v-if="notices.length">
        <li class="notice-item" v-for="item in notices" :key="item.id" @click="openNotice(item)">
          <div class="notice-item-title">{{ item.title }}</div>
          <div class="notice-item-time">{{ (item.updateTime || item.createTime || '').substr(0, 10) }}</div>
        </li>
      </ul>
      <el-empty v-else description="暂无公告"></el-empty>
    </div>

    <div class="section" v-for="(item, index) in songsList" :key="index">
      <div class="section-title"><b style="font-size: 32px;"> {{ item.name }}</b></div>
      <content-list :contentList="item.list"></content-list>
    </div>
  </div>
</template>

<script>
import Swiper from "../components/Swiper";
import contentList from "../components/ContentList";
import { getAllSinger, getAllSongList, getNoticeList } from "../api/index";

export default {
  name: "home",
  components: {
    Swiper,
    contentList
  },
  data() {
    return {
      songsList: [
        { name: "歌单", list: [] },
        { name: "歌手", list: [] }
      ],
      notices: []
    };
  },
  created() {
    this.getSongList();
    this.getSinger();
    this.getNotices();
  },
  methods: {
    getNotices() {
      getNoticeList()
        .then((res) => {
          const arr = Array.isArray(res) ? res : (res && res.data ? res.data : []);
          this.notices = Array.isArray(arr) ? arr.slice(0, 6) : [];
        })
        .catch(() => {
          this.notices = [];
        });
    },
    goNotice() {
      this.$router.push({ path: "/notice" });
    },
    openNotice(item) {
      if (!item) return;
      this.$alert(item.content || "暂无内容", item.title || "公告", {
        confirmButtonText: "知道了"
      });
    },
    getSongList() {
      getAllSongList()
        .then((res) => {
          const arr = Array.isArray(res) ? res : (res && res.data ? res.data : []);
          this.songsList[0].list = arr.slice(0, 10);
        })
        .catch((e) => {
          const msg = e && e.data ? e.data : "Network Error";
          this.$message({
            showClose: true,
            message: msg,
            type: "error",
            duration: 5000
          });
        });
    },
    getSinger() {
      getAllSinger()
        .then((res) => {
          const arr = Array.isArray(res) ? res : (res && res.data ? res.data : []);
          this.songsList[1].list = arr.slice(0, 10);
        })
        .catch((e) => {
          const msg = e && e.data ? e.data : "Network Error";
          this.$message({
            showClose: true,
            message: msg,
            type: "error",
            duration: 5000
          });
        });
    }
  }
};
</script>

<style lang="scss" scoped>
@import "../assets/css/home.scss";
</style>
