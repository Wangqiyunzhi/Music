<template>
  <div class="comment-wrap">
    <div class="comment-editor">
      <div class="avatar">
        <img v-if="myAvatarSrc" :src="myAvatarSrc" />
        <div v-else class="avatar-fallback">我</div>
      </div>

      <div class="editor-main">
        <el-input
          type="textarea"
          :rows="3"
          v-model="content"
          placeholder="请输入评论内容…"
          maxlength="300"
          show-word-limit
        />
        <div class="editor-actions">
          <div class="tip">共 <b>{{ total }}</b> 条评论</div>
          <el-button type="primary" size="small" @click="submitComment">评论</el-button>
        </div>
      </div>
    </div>

    <div class="comment-list" v-loading="loading">
      <el-empty v-if="!list.length && !loading" description="暂无评论" />

      <div v-for="item in list" :key="item.id" class="comment-item">
        <div class="left">
          <!-- <div class="avatar-fallback small">用户</div> -->
        </div>

        <div class="right">
          <div class="meta">
            <span class="user">
              {{ item.username ? item.username : ("用户 " + (item.userId || item.consumerId || "")) }}
            </span>
            <span class="time">{{ formatTime(item.createTime || item.create_time || item.gmtCreate) }}</span>
            <span class="op" v-if="canDelete(item)" @click="remove(item)">删除</span>
          </div>
          <div class="text">{{ item.content }}</div>
        </div>
      </div>

      <div class="pager" v-if="total > pageSize">
        <el-pagination
          background
          layout="prev, pager, next"
          :current-page="pageNo"
          :page-size="pageSize"
          :total="total"
          @current-change="changePage"
        />
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import { mixin } from "../mixins";
import { setComment, getCommentPage, deleteComment } from "../api/index";

export default {
  name: "comment",
  mixins: [mixin],
  props: {
    playId: { type: [String, Number], required: true },
    type: { type: [String, Number], required: true }
  },
  data() {
    return {
      content: "",
      list: [],
      total: 0,
      pageNo: 1,
      pageSize: 10,
      loading: false
    };
  },
  computed: {
    ...mapGetters(["loginIn", "userId"]),
    myAvatarSrc() {
      var a = localStorage.getItem("avator");
      return a ? this.attachImageUrl(a) : "";
    }
  },
  watch: {
    playId: {
      immediate: true,
      handler() {
        this.pageNo = 1;
        this.load();
      }
    }
  },
  methods: {
    unwrap(res) {
      return res && res.data ? res.data : res;
    },

    parsePage(res) {
      var r = this.unwrap(res);

      if (Array.isArray(r)) {
        return { list: r, total: r.length };
      }

      if (r && r.data && Array.isArray(r.data)) {
        return { list: r.data, total: Number(r.total || r.data.length || 0) };
      }

      if (r && Array.isArray(r.list)) {
        return { list: r.list, total: Number(r.total || r.list.length || 0) };
      }

      if (r && r.data && r.data.records && Array.isArray(r.data.records)) {
        return { list: r.data.records, total: Number(r.data.total || r.total || 0) };
      }

      if (r && r.records && Array.isArray(r.records)) {
        return { list: r.records, total: Number(r.total || 0) };
      }

      if (r && r.data && Array.isArray(r.data.list)) {
        return { list: r.data.list, total: Number(r.data.total || r.total || 0) };
      }

      return { list: [], total: 0 };
    },

    load() {
      if (!this.playId) return;
      this.loading = true;

      getCommentPage(Number(this.type), this.playId, this.pageNo, this.pageSize)
        .then((res) => {
          var parsed = this.parsePage(res);
          this.list = parsed.list || [];
          this.total = parsed.total || 0;
        })
        .catch(() => {
          this.total = 0;
          this.list = [];
        })
        .finally(() => {
          this.loading = false;
        });
    },

    changePage(p) {
      this.pageNo = p;
      this.load();
    },

    submitComment() {
      if (!this.loginIn) return this.notify("请先登录", "warning");

      var text = (this.content || "").trim();
      if (!text) return this.notify("评论内容不能为空", "warning");

      var params = new URLSearchParams();
      params.append("userId", this.userId);
      params.append("type", Number(this.type));
      params.append("content", text);
      if (Number(this.type) === 0) params.append("songId", this.playId);
      else params.append("songListId", this.playId);

      setComment(params)
        .then((res) => {
          var r = this.unwrap(res);
          var ok = r && (String(r.code) === "1" || r.code === 1 || r.success === true);
          if (ok) {
            this.notify("评论成功", "success");
            this.content = "";
            this.pageNo = 1;
            this.load();
          } else {
            this.notify((r && r.msg) || "评论失败", "error");
          }
        })
        .catch(() => this.notify("评论失败", "error"));
    },

    canDelete(item) {
      var uid = item.userId || item.consumerId;
      return this.loginIn && String(uid) === String(this.userId);
    },

    remove(item) {
      if (!this.canDelete(item)) return;

      var params = new URLSearchParams();
      params.append("id", item.id);
      params.append("userId", this.userId);

      deleteComment(params)
        .then((res) => {
          var r = this.unwrap(res);
          var ok = r && (String(r.code) === "1" || r.code === 1 || r.success === true);
          if (ok) {
            this.notify("删除成功", "success");
            if (this.list.length === 1 && this.pageNo > 1) this.pageNo -= 1;
            this.load();
          } else {
            this.notify((r && r.msg) || "删除失败", "error");
          }
        })
        .catch(() => this.notify("删除失败", "error"));
    },

    formatTime(v) {
      if (!v) return "";
      var d = new Date(v);
      if (isNaN(d.getTime())) return String(v);
      var yy = d.getFullYear();
      var mm = String(d.getMonth() + 1).padStart(2, "0");
      var dd = String(d.getDate()).padStart(2, "0");
      var hh = String(d.getHours()).padStart(2, "0");
      var mi = String(d.getMinutes()).padStart(2, "0");
      return yy + "-" + mm + "-" + dd + " " + hh + ":" + mi;
    }
  }
};
</script>

<style scoped>
.comment-wrap { margin-top: 14px; }
.comment-editor { display:flex; gap:12px; padding:12px; border:1px solid #eee; border-radius:8px; background:#fff; }
.avatar img { width:44px; height:44px; border-radius:50%; object-fit:cover; }
.avatar-fallback { width:44px; height:44px; border-radius:50%; background:#f2f3f5; color:#666; display:flex; align-items:center; justify-content:center; font-size:12px; user-select:none; }
.avatar-fallback.small { width:36px; height:36px; font-size:12px; }
.editor-main { flex:1; }
.editor-actions { margin-top:8px; display:flex; justify-content:space-between; align-items:center; }
.tip { color:#666; font-size:12px; }
.comment-list { margin-top:12px; }
.comment-item { display:flex; gap:12px; padding:12px; background:#fff; border:1px solid #f0f0f0; border-radius:8px; margin-bottom:10px; }
.comment-item .right { flex:1; }
.meta { display:flex; align-items:center; gap:10px; font-size:12px; color:#888; margin-bottom:6px; }
.meta .user { color:#333; font-weight:600; }
.meta .op { margin-left:auto; color:#f56c6c; cursor:pointer; }
.text { color:#333; font-size:14px; line-height:1.6; white-space:pre-wrap; word-break:break-word; }
.pager { display:flex; justify-content:center; padding:10px 0 20px; }
</style>
