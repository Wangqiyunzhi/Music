<template>
  <transition name="slide-fade">
    <div class="the-aside" v-if="showAside" @click.stop>
      <div class="aside-head">
        <h2 class="title">{{ activeTab === 'queue' ? '播放列表' : '最近播放' }}</h2>
        <div class="tabs">
          <span :class="{ on: activeTab==='queue' }" @click="activeTab='queue'">播放列表</span>
          <span :class="{ on: activeTab==='recent' }" @click="activeTab='recent'">最近播放</span>
        </div>

        <div class="actions" v-if="activeTab==='queue'">
          <span class="btn" @click="clearQueue">清空队列</span>
        </div>
        <div class="actions" v-else>
          <span class="btn" @click="clearRecent">清空最近</span>
        </div>
      </div>

      <ul class="menus" v-if="activeTab==='queue'">
        <li
          v-for="(item, index) in listOfSongs"
          :key="item.id || index"
          :class="{ 'is-play': String(id) === String(item.id) }"
          @click="playFromQueue(item, index)"
        >
          {{ replaceFName(item.name) }}
        </li>
      </ul>

      <ul class="menus" v-else>
        <li
          v-for="(item, index) in recentPlays"
          :key="item.id || index"
          :class="{ 'is-play': String(id) === String(item.id) }"
          @click="playFromRecent(item)"
        >
          {{ replaceFName(item.name) }}
        </li>
      </ul>
    </div>
  </transition>
</template>

<script>
import { mapGetters } from "vuex";
import { getCollectOfUserId } from "../api/index";

export default {
  name: "the-aside",
  data() {
    return {
      activeTab: "queue", // queue / recent
    };
  },
  computed: {
    ...mapGetters([
      "showAside",
      "listOfSongs",
      "recentPlays",
      "id",
      "loginIn",
      "userId",
    ]),
  },
  mounted() {
    document.addEventListener(
      "click",
      () => {
        this.$store.commit("setShowAside", false);
      },
      true
    );
  },
  methods: {
    replaceLName(str) {
      let arr = (str || "").split("-");
      return arr[0] || "";
    },
    replaceFName(str) {
      let arr = (str || "").split("-");
      return arr[1] || str || "";
    },

    clearQueue() {
      this.$store.commit("clearQueue");
      this.$notify({ title: "已清空播放队列", type: "success" });
    },
    clearRecent() {
      this.$store.commit("clearRecent");
      this.$notify({ title: "已清空最近播放", type: "success" });
    },

    // ✅ 从队列播放（用 index）
    playFromQueue(song, index) {
      this.applySong(song, index);
    },

    // ✅ 从最近播放（不改变队列，只切到当前歌；index 用当前队列的 index（找不到就 0）
    playFromRecent(song) {
      const idx = Array.isArray(this.listOfSongs)
        ? this.listOfSongs.findIndex(s => String(s.id) === String(song.id))
        : -1;
      this.applySong(song, idx >= 0 ? idx : 0);
    },

    applySong(song, index) {
      if (!song) return;

      this.$store.commit("setId", song.id);
      this.$store.commit("setUrl", this.$store.state.configure.HOST + song.url);
      this.$store.commit("setPicUrl", this.$store.state.configure.HOST + song.pic);
      this.$store.commit("setListIndex", index);
      this.$store.commit("setTitle", this.replaceFName(song.name));
      this.$store.commit("setArtist", this.replaceLName(song.name));
      this.$store.commit("setLyric", this.parseLyric(song.lyric || ""));

      // ✅ 最近播放写入
      this.$store.commit("pushRecent", song);

      // ✅ 直接播放
      this.$store.commit("setIsPlay", true);

      // 同步收藏状态（如果你仍用全局 isActive，这块你可以继续在 PlayBar 里做）
      if (this.loginIn) {
        getCollectOfUserId(this.userId).catch(() => {});
      }
    },

    parseLyric(text) {
      if (!text) return [[0, ""]];
      let lines = text.split("\n");
      let pattern = /\[\d{2}:\d{2}.(\d{3}|\d{2})\]/g;
      let result = [];
      if (!/\[.+\]/.test(text)) return [[0, text]];
      while (lines.length && !pattern.test(lines[0])) lines = lines.slice(1);
      for (let item of lines) {
        let time = item.match(pattern);
        let value = item.replace(pattern, "");
        if (!time || value === undefined) continue;
        for (let titem of time) {
          let t = titem.slice(1, -1).split(":");
          if (value !== "") {
            result.push([parseInt(t[0], 10) * 60 + parseFloat(t[1]), value]);
          }
        }
      }
      result.sort((a, b) => a[0] - b[0]);
      return result;
    },
  },
};
</script>

<style lang="scss" scoped>
@import "../assets/css/the-aside.scss";

/* 轻量补一点（不破坏你原样式） */
.aside-head {
  padding: 10px 12px 0;
}
.tabs {
  display: flex;
  gap: 10px;
  margin-top: 6px;
}
.tabs span {
  cursor: pointer;
  font-size: 12px;
  opacity: 0.7;
}
.tabs span.on {
  opacity: 1;
  font-weight: 700;
}
.actions {
  margin-top: 6px;
}
.actions .btn {
  font-size: 12px;
  cursor: pointer;
  opacity: 0.8;
}
</style>
