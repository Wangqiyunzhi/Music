<template>
  <div class="my-music">
    <div class="album-slide">
      <div class="album-img">
        <img :src="attachImageUrl(avator)" />
      </div>
      <ul class="album-info">
        <li>昵称：{{ username }}</li>
        <li>性别：{{ userSex }}</li>
        <li>生日：{{ birth }}</li>
        <li>地区：{{ location }}</li>
      </ul>
    </div>

    <div class="album-content">
      <div class="album-title">个性签名：{{ introduction }}</div>

      <div class="songs-body">
        <album-content :songList="collectSongList">
          <template slot="title">我收藏的歌曲</template>
        </album-content>
        <el-empty v-if="!collectSongList.length" description="暂无收藏歌曲"></el-empty>
      </div>

      <div class="songs-body" style="margin-top: 18px;">
        <content-list :contentList="collectSongLists">
          <template slot="title">我收藏的歌单</template>
        </content-list>
        <el-empty v-if="!collectSongLists.length" description="暂无收藏歌单"></el-empty>
      </div>

      <div class="songs-body" style="margin-top: 18px;">
        <div style="display:flex; align-items:center; justify-content:space-between;">
          <div style="font-size: 18px; font-weight: 600; padding: 6px 0;">最近播放</div>
          <el-button
            size="mini"
            type="danger"
            plain
            @click="clearRecentPlays"
            :disabled="!recentPlayList.length"
          >
            清空最近播放
          </el-button>
        </div>

        <album-content v-if="recentPlayList.length" :songList="recentPlayList" />
        <el-empty v-else description="暂无最近播放"></el-empty>
      </div>
    </div>
  </div>
</template>

<script>
import { mixin } from "../mixins";
import { mapGetters } from "vuex";
import {
  getUserOfId,
  getCollectOfUserIdAndType,
  songOfSongId,
  songListOfSongListId,
  getPlayHistory,
  clearPlayHistory
} from "../api/index";

import AlbumContent from "../components/AlbumContent";
import ContentList from "../components/ContentList";

export default {
  name: "my-music",
  mixins: [mixin],
  components: { AlbumContent, ContentList },
  data() {
    return {
      avator: "",
      username: "",
      userSex: "",
      birth: "",
      location: "",
      introduction: "",
      collectSongList: [],
      collectSongLists: [],
      playHistoryList: []
    };
  },
  computed: {
    ...mapGetters(["userId", "recentPlays"]),
    recentPlayList() {
      if (Array.isArray(this.playHistoryList) && this.playHistoryList.length) {
        return this.playHistoryList;
      }
      return Array.isArray(this.recentPlays) ? this.recentPlays : [];
    }
  },
  mounted() {
    const uid = this.userId || window.sessionStorage.getItem("userId") || window.localStorage.getItem("userId");
    if (uid) {
      this.getMsg(uid);
      this.loadCollectSongs(uid);
      this.loadCollectSongLists(uid);
      this.loadPlayHistory(uid);
    }
  },
  methods: {
    unwrap(res) {
      if (res && Object.prototype.hasOwnProperty.call(res, "data")) {
        return res.data;
      }
      return res;
    },
    getMsg(userId) {
      getUserOfId(userId).then((res) => {
        const r = this.unwrap(res) || {};
        this.avator = r.avator || "";
        this.username = r.username || "";
        this.userSex = r.sex === 0 ? "女" : r.sex === 1 ? "男" : "";
        this.birth = this.attachBirth(r.birth);
        this.location = r.location || "";
        this.introduction = r.introduction || "";
      });
    },

    loadCollectSongs(userId) {
      this.collectSongList = [];
      getCollectOfUserIdAndType(userId, 0).then((res) => {
        const arr = Array.isArray(res) ? res : [];
        arr.forEach((c) => {
          if (c && c.songId) {
            songOfSongId(c.songId).then((song) => {
              if (song) this.collectSongList.push(song);
            });
          }
        });
      });
    },

    loadCollectSongLists(userId) {
      this.collectSongLists = [];
      getCollectOfUserIdAndType(userId, 1).then((res) => {
        const arr = Array.isArray(res) ? res : [];
        arr.forEach((c) => {
          if (c && c.songListId) {
            songListOfSongListId(c.songListId).then((sl) => {
              if (sl) this.collectSongLists.push(sl);
            });
          }
        });
      });
    },

    loadPlayHistory(userId) {
      getPlayHistory(userId, 50).then((res) => {
        const arr = Array.isArray(res) ? res : (res && res.data ? res.data : []);
        this.playHistoryList = Array.isArray(arr) ? arr : [];
      });
    },

    clearRecentPlays() {
      const uid = this.userId || window.sessionStorage.getItem("userId") || window.localStorage.getItem("userId");
      if (uid) {
        clearPlayHistory(uid).then(() => {
          this.playHistoryList = [];
          this.$store.commit("clearRecentPlays");
          this.notify("已清空最近播放", "success");
        });
      } else {
        this.$store.commit("clearRecentPlays");
        this.notify("已清空最近播放", "success");
      }
    }
  }
};
</script>

<style lang="scss" scoped>
@import "../assets/css/my-music.scss";
</style>
