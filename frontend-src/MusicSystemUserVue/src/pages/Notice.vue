<template>
  <div class="notice-page">
    <div class="notice-header">
      <div class="notice-title">通知公告</div>
    </div>

    <div class="notice-body">
      <div
        class="notice-card"
        v-for="item in notices"
        :key="item.id"
        @click="openNotice(item)"
      >
        <div class="notice-card-title">{{ item.title }}</div>
        <div class="notice-card-time">{{ (item.updateTime || item.createTime || '').substr(0, 10) }}</div>
        <div class="notice-card-content">{{ item.content }}</div>
      </div>

      <el-empty v-if="!notices.length" description="暂无公告"></el-empty>
    </div>
  </div>
</template>

<script>
import { getNoticeList } from "../api/index";

export default {
  name: "notice",
  data() {
    return {
      notices: []
    };
  },
  created() {
    this.getNotices();
  },
  methods: {
    getNotices() {
      getNoticeList()
        .then((res) => {
          const arr = Array.isArray(res) ? res : (res && res.data ? res.data : []);
          this.notices = Array.isArray(arr) ? arr : [];
        })
        .catch(() => {
          this.notices = [];
        });
    },
    openNotice(item) {
      if (!item) return;
      this.$alert(item.content || "暂无内容", item.title || "公告", {
        confirmButtonText: "知道了"
      });
    }
  }
};
</script>

<style lang="scss" scoped>
@import "../assets/css/notice.scss";
</style>
