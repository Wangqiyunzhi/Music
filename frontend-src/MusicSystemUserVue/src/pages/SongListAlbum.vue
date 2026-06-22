<template>
  <div class="song-lyric">
    <h1 class="lyric-title">歌词</h1>
    <!-- 有歌词 -->
    <ul class="has-lyric" v-if="lyr.length" key="index">
      <li v-for="(item, index) in lyr" v-bind:key="index">
        {{ item[1] }}
      </li>
    </ul>
    <!-- 没有歌词 -->
    <div v-else class="no-lyric" key="no-lyric">
      <span>暂无歌词</span>
    </div>
  </div>
</template>

<script>
import { mixin } from "../mixins";
import { mapGetters } from "vuex";
import {
  listSongDetail,
  songOfSongId,
  setRank,
  getRankOfSongListId,
  setCollect,
  getCollectOfUserIdAndType
} from "../api/index";
import AlbumContent from "../components/AlbumContent";
import Comment from "../components/Comment";

export default {
  name: "song-list-album",
  mixins: [mixin],
  components: { AlbumContent, Comment },
  data() {
    return {
      songLists: [],
      songListId: "",
      average: 0,
      rank: 0,
      isSongListActive: false
    };
  },
  computed: {
    ...mapGetters(["tempList", "loginIn", "userId"]),
    displayScore10() {
      var v = Number(this.average || 0) * 2;
      if (!isFinite(v)) v = 0;
      v = Math.round(v * 10) / 10;
      return String(v);
    }
  },
  created() {
    this.songListId = this.$route.params.id || "";
    this.loadSongs();
    this.getRank(this.songListId);
    if (this.loginIn) this.loadSongListCollectStatus();
  },
  watch: {
    "$route.params.id": function (n) {
      this.songListId = n || "";
      this.loadSongs();
      this.getRank(this.songListId);
      if (this.loginIn) this.loadSongListCollectStatus();
    },
    loginIn: function (n) {
      if (n) this.loadSongListCollectStatus();
      else this.isSongListActive = false;
    }
  },
  methods: {
    unwrap(res) {
      return res && res.data !== undefined ? res.data : res;
    },
    normalizeArray(res) {
      var r = this.unwrap(res);
      if (Array.isArray(r)) return r;
      if (r && Array.isArray(r.data)) return r.data;
      if (r && Array.isArray(r.list)) return r.list;
      if (r && r.data && Array.isArray(r.data.list)) return r.data.list;
      if (r && r.data && Array.isArray(r.data.records)) return r.data.records;
      return [];
    },
    looksLikeSong(obj) {
      if (!obj || typeof obj !== "object") return false;
      return !!(obj.name || obj.url || obj.pic || obj.lyric || obj.introduction);
    },
    extractSongId(x) {
      if (typeof x === "number" || typeof x === "string") return x;
      if (!x) return null;
      return x.songId || x.song_id || x.sid || x.songID || x.id || null;
    },

    loadSongs() {
      var self = this;
      self.songLists = [];

      if (!self.songListId) return;

      listSongDetail(self.songListId)
        .then(function (res) {
          var arr = self.normalizeArray(res);

          if (!arr.length) {
            self.songLists = [];
            return;
          }

          if (self.looksLikeSong(arr[0])) {
            self.songLists = arr;
            return;
          }

          var ids = arr
            .map(function (x) { return self.extractSongId(x); })
            .filter(function (id) { return id !== null && id !== undefined && id !== ""; });

          if (!ids.length) {
            console.log("[SongListAlbum] no songId in listSong:", arr);
            self.songLists = [];
            return;
          }

          return Promise.all(
            ids.map(function (id) {
              return songOfSongId(id)
                .then(function (song) { return song; })
                .catch(function (e) {
                  console.log("[SongListAlbum] songOfSongId failed:", id, e);
                  return null;
                });
            })
          ).then(function (songs) {
            self.songLists = (songs || []).filter(function (s) { return !!s; });
          });
        })
        .catch(function (e) {
          console.log("[SongListAlbum] listSong/detail failed:", e);
          self.songLists = [];
        });
    },

    getRank(id) {
      var self = this;
      if (!id) { self.average = 0; return; }

      getRankOfSongListId(id)
        .then(function (res) {
          var raw = self.unwrap(res);
          var score10 = parseFloat(String(raw).trim()); // 后端直接返回 "5"
          if (!isFinite(score10)) score10 = 0;
          if (score10 < 0) score10 = 0;
          if (score10 > 10) score10 = 10;
          self.average = score10 / 2;
        })
        .catch(function () {
          self.average = 0;
        });
    },

    submitRank(val) {
      if (!this.loginIn) return this.notify("请先登录", "warning");
      if (!this.songListId) return this.notify("歌单ID无效", "warning");

      var v = typeof val === "number" ? val : Number(this.rank || 0);
      if (!isFinite(v)) v = 0;

      var params = new URLSearchParams();
      params.append("songListId", this.songListId);
      params.append("consumerId", this.userId);
      params.append("score", String(Math.round(v * 2)));

      var self = this;
      setRank(params)
        .then(function (res) {
          var r = self.unwrap(res);
          if (r && (String(r.code) === "1" || r.code === 1)) {
            self.notify("评分成功", "success");
            self.getRank(self.songListId);
          } else {
            self.notify((r && r.msg) || "评分失败", "error");
          }
        })
        .catch(function () {
          self.notify("评分失败/已评价", "error");
        });
    },

    loadSongListCollectStatus() {
      var self = this;
      if (!self.loginIn) { self.isSongListActive = false; return; }

      getCollectOfUserIdAndType(self.userId, 1)
        .then(function (res) {
          var r = self.unwrap(res);
          var arr = Array.isArray(r) ? r : (r && Array.isArray(r.data) ? r.data : []);
          self.isSongListActive = arr.some(function (c) {
            return String(c.songListId) === String(self.songListId);
          });
        })
        .catch(function () {
          self.isSongListActive = false;
        });
    },

    toggleSongListCollect() {
      if (!this.loginIn) return this.notify("请先登录", "warning");

      var params = new URLSearchParams();
      params.append("userId", this.userId);
      params.append("type", 1);
      params.append("songListId", this.songListId);

      var self = this;
      setCollect(params)
        .then(function (res) {
          var r = self.unwrap(res);
          if (r && String(r.code) === "1") {
            self.isSongListActive = true;
            self.notify(r.msg || "收藏歌单成功", "success");
          } else if (r && String(r.code) === "2") {
            self.isSongListActive = false;
            self.notify(r.msg || "取消收藏歌单成功", "warning");
          } else {
            self.notify((r && r.msg) || "操作失败", "error");
          }
        })
        .catch(function () {
          self.notify("操作失败", "error");
        });
    }
  }
};
</script>

<style lang="scss" scoped>
@import "../assets/css/song-list-album.scss";

/* 防止被全局样式影响评分星星显示 */
.album-score /deep/ .el-rate {
  display: inline-block !important;
  line-height: normal !important;
}
.album-score /deep/ .el-rate__icon {
  font-size: 22px !important;
  margin-right: 4px !important;
  opacity: 1 !important;
  visibility: visible !important;
  display: inline-block !important;
}
.album-score /deep/ .el-rate__text {
  margin-left: 8px !important;
}

.icon.active { fill: #e25555; }
</style>
