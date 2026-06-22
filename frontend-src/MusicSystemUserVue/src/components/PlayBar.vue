<template>
  <div class="play-bar" :class="{ show: !toggle }">
    <div @click="toggle = !toggle" class="item-up" :class="{ turn: toggle }">
      <svg class="icon"><use xlink:href="#icon-jiantou-shang-cuxiantiao"></use></svg>
    </div>

    <div class="kongjian">
      <!-- 上一首 -->
      <div class="item" @click="prev">
        <svg class="icon"><use xlink:href="#icon-ziyuanldpi"></use></svg>
      </div>

      <!-- 播放 -->
      <div class="item" @click="togglePlay">
        <svg class="icon"><use :xlink:href="playButtonUrl"></use></svg>
      </div>

      <!-- 下一首 -->
      <div class="item" @click="next">
        <svg class="icon"><use xlink:href="#icon-ziyuanldpi1"></use></svg>
      </div>

      <!-- 播放模式（现代图标：列表循环/单曲循环/随机） -->
      <div class="item" @click="toggleMode" title="播放模式（列表循环/单曲循环/随机）">
  <!-- 列表循环 -->
  <svg v-if="playMode==='order'" class="icon" viewBox="0 0 24 24">
    <path d="M17 1l4 4-4 4V6H7a4 4 0 0 0-4 4v1H1v-1a6 6 0 0 1 6-6h10V1zM7 23l-4-4 4-4v3h10a4 4 0 0 0 4-4v-1h2v1a6 6 0 0 1-6 6H7v3z"/>
  </svg>

  <!-- 单曲循环（带 1） -->
  <svg v-else-if="playMode==='one'" class="icon" viewBox="0 0 24 24">
    <path d="M17 1l4 4-4 4V6H7a4 4 0 0 0-4 4v1H1v-1a6 6 0 0 1 6-6h10V1zM7 23l-4-4 4-4v3h10a4 4 0 0 0 4-4v-1h2v1a6 6 0 0 1-6 6H7v3z"/>
    <path d="M12 8h-1l-2 1v1l2-1v7h1V8z"/>
  </svg>

  <!-- 随机 -->
  <svg v-else class="icon" viewBox="0 0 24 24">
    <path d="M17 3h4v4l-1.5-1.5-3.2 3.2a4 4 0 0 1-2.8 1.1H12v-2h1.5c.5 0 1-.2 1.4-.6l3.2-3.2L17 3zM3 6h4.6a4 4 0 0 1 2.8 1.2l2 2L13 10.6l-2.1-2.1c-.4-.4-.9-.6-1.4-.6H3V6zm18 14v4h-4l1.5-1.5-3.2-3.2c-.4-.4-.9-.6-1.4-.6H12v-2h1.7a4 4 0 0 1 2.8 1.1l3.2 3.2L21 20zM3 18v-2h6.6c.5 0 1-.2 1.4-.6L13 13.4l1.4 1.4-2 2A4 4 0 0 1 9.6 18H3z"/>
  </svg>
</div>


      <!-- 清空队列 -->
      <div class="item" @click="clearQueue" title="清空播放队列">
        <svg class="icon"><use xlink:href="#icon-liebiao"></use></svg>
      </div>

      <!-- 歌曲图片 -->
      <div class="item-img" @click="toLyric">
        <img :src="picUrl" />
      </div>

      <!-- 播放进度 -->
      <div class="playing-speed">
        <div class="current-time">{{ nowTime }}</div>

        <div class="progress-box">
          <div class="item-song-title">
            <div>{{ title }}</div>
            <div>{{ artist }}</div>
          </div>

          <div ref="progress" class="progress" @mousemove="mousemove">
            <div ref="bg" class="bg" @click="updatemove">
              <div ref="curProgress" class="cur-progress" :style="{ width: curLength + '%' }"></div>
            </div>
            <div ref="idot" class="idot" :style="{ left: curLength + '%' }" @mousedown="mousedown" @mouseup="mouseup"></div>
          </div>
        </div>

        <div class="left-time">{{ songTime }}</div>

        <!-- 音量 -->
        <div class="item item-volume">
          <svg v-if="volume == 0" class="icon"><use xlink:href="#icon-yinliangjingyinheix"></use></svg>
          <svg v-else class="icon"><use xlink:href="#icon-yinliang1"></use></svg>
          <el-slider class="volume" v-model="volume" :vertical="true"></el-slider>
        </div>

        <!-- 收藏 -->
        <div class="item" @click="collection">
          <svg :class="{ active: isActive }" class="icon"><use xlink:href="#icon-xihuan-shi"></use></svg>
        </div>

        <!-- 分享 -->
        <div class="item" @click="share" style="padding-bottom: -5px">
          <svg width="16" height="19" xmlns="http://www.w3.org/2000/svg" class="bi bi-share-fill">
            <path d="m11,2.5a2.5,2.5 0 1 1 0.6,1.63l-6.72,3.12a2.5,2.5 0 0 1 0,1.5l6.72,3.12a2.5,2.5 0 1 1 -0.48,0.88l-6.72,-3.12a2.5,2.5 0 1 1 0,-3.26l6.72,-3.12a2.5,2.5 0 0 1 -0.12,-0.75z"/>
          </svg>
        </div>

        <!-- 下载 -->
        <div class="item" @click="download">
          <svg class="icon"><use xlink:href="#icon-xiazai"></use></svg>
        </div>

        <!-- 播放列表 / 最近播放 -->
        <div class="item" @click="changeAside">
          <svg class="icon"><use xlink:href="#icon-liebiao"></use></svg>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import { download, setCollect, getCollectOfUserId } from "../api/index";

export default {
  name: "play-bar",
  data() {
    return {
      nowTime: "00:00",
      songTime: "00:00",
      curLength: 0,
      progressLength: 0,
      mouseStartX: 0,
      tag: false,
      volume: 50,
      toggle: true,
    };
  },
  computed: {
    ...mapGetters([
      "id","url","isPlay","playButtonUrl","picUrl","title","artist","duration","curTime",
      "showAside","listIndex","listOfSongs","autoNext","loginIn","userId","isActive",
      "playMode","recentPlays"
    ]),
  },
  watch: {
    isPlay(val) {
      this.$store.commit("setPlayButtonUrl", val ? "#icon-zanting" : "#icon-bofang");
    },
    curTime() {
      this.nowTime = this.formatSeconds(this.curTime);
      this.songTime = this.formatSeconds(this.duration);
      this.curLength = this.duration ? (this.curTime / this.duration) * 100 : 0;
    },
    volume() {
      this.$store.commit("setVolume", this.volume / 100);
    },
    autoNext() {
      // ended 触发 autoNext 翻转，这里统一走 next()，next 内按模式处理
      this.next();
    },
  },
  mounted() {
    this.$nextTick(() => {
      if (this.$refs.progress) {
        this.progressLength = this.$refs.progress.getBoundingClientRect().width;
      }
    });
  },
  methods: {
    notify(title, type) {
      this.$notify({ title, type });
    },

    // ✅ 模式：order -> one -> shuffle -> order
    toggleMode() {
      const cur = this.playMode || "order";
      const next = cur === "order" ? "one" : cur === "one" ? "shuffle" : "order";
      this.$store.commit("setPlayMode", next);

      this.notify(
        next === "order" ? "播放模式：列表循环" : next === "one" ? "播放模式：单曲循环" : "播放模式：随机播放",
        "success"
      );
    },

    // ✅ 清空队列
    clearQueue() {
      this.$store.commit("clearQueue");
      this.notify("已清空播放队列", "success");
    },

    // ✅ 根据 index 播放（并写入最近播放）
    playByIndex(newIndex) {
      if (!Array.isArray(this.listOfSongs) || this.listOfSongs.length === 0) {
        this.notify("播放列表为空", "warning");
        return;
      }

      if (newIndex < 0) newIndex = 0;
      if (newIndex > this.listOfSongs.length - 1) newIndex = this.listOfSongs.length - 1;

      const song = this.listOfSongs[newIndex];
      if (!song) return;

      this.$store.commit("setListIndex", newIndex);
      this.$store.commit("setId", song.id);
      this.$store.commit("setUrl", this.$store.state.configure.HOST + song.url);
      this.$store.commit("setPicUrl", this.$store.state.configure.HOST + song.pic);
      this.$store.commit("setTitle", this.replaceFName(song.name));
      this.$store.commit("setArtist", this.replaceLName(song.name));
      this.$store.commit("setLyric", this.parseLyric(song.lyric || ""));
      this.$store.commit("setIsActive", false);

      this.$store.commit("pushRecent", song);
      this.$store.commit("setIsPlay", true);

      if (this.loginIn) {
        getCollectOfUserId(this.userId)
          .then((res) => {
            const arr = Array.isArray(res) ? res : [];
            const hit = arr.some((c) => String(c.songId) === String(song.id));
            this.$store.commit("setIsActive", hit);
          })
          .catch(() => {});
      }
    },

    // 播放/暂停
    togglePlay() {
      if (!this.id || this.id === "") {
        if (this.listOfSongs && this.listOfSongs.length > 0) {
          this.playByIndex(this.listIndex >= 0 ? this.listIndex : 0);
          return;
        }
        this.notify("当前未选择音乐。", "error");
        return;
      }
      this.$store.commit("setIsPlay", !this.isPlay);
    },

    // 上一首
    prev() {
      if (!this.listOfSongs || this.listOfSongs.length === 0) {
        this.notify("播放列表为空", "warning");
        return;
      }
      if (this.playMode === "one") {
        this.$store.commit("setChangeTime", 0);
        this.$store.commit("setIsPlay", true);
        return;
      }
      const cur = this.listIndex >= 0 ? this.listIndex : 0;
      const nextIndex = cur > 0 ? cur - 1 : this.listOfSongs.length - 1;
      this.playByIndex(nextIndex);
    },

    // 下一首（按模式）
    next() {
      if (!this.listOfSongs || this.listOfSongs.length === 0) {
        this.notify("播放列表为空", "warning");
        return;
      }

      if (this.playMode === "one") {
        this.$store.commit("setChangeTime", 0);
        this.$store.commit("setIsPlay", true);
        return;
      }

      const cur = this.listIndex >= 0 ? this.listIndex : 0;

      if (this.playMode === "shuffle") {
        let nextIndex = 0;
        if (this.listOfSongs.length === 1) {
          nextIndex = 0;
        } else {
          do {
            nextIndex = Math.floor(Math.random() * this.listOfSongs.length);
          } while (nextIndex === cur);
        }
        this.playByIndex(nextIndex);
        return;
      }

      // order：列表循环
      const nextIndex = cur < this.listOfSongs.length - 1 ? cur + 1 : 0;
      this.playByIndex(nextIndex);
    },

    // 进度条
    mousedown(e) {
      if (!this.id || this.id === "") return;
      this.mouseStartX = e.clientX;
      this.tag = true;
    },
    mouseup() { this.tag = false; },
    mousemove(e) {
      if (!this.duration) return false;
      if (this.tag) {
        let movementX = e.clientX - this.mouseStartX;
        let curLength = this.$refs.curProgress.getBoundingClientRect().width;
        let newPercent = ((movementX + curLength) / this.progressLength) * 100;
        newPercent = Math.max(0, Math.min(100, newPercent));
        this.curLength = newPercent;
        this.mouseStartX = e.clientX;
        this.changeTime(newPercent);
      }
    },
    changeTime(percent) {
      let newCurTime = percent * 0.01 * this.duration;
      this.$store.commit("setChangeTime", newCurTime);
    },
    updatemove(e) {
      if (!this.id || this.id === "") return;
      if (!this.tag) {
        let left = this.$refs.bg.getBoundingClientRect().left;
        let newPercent = ((e.clientX - left) / this.progressLength) * 100;
        newPercent = Math.max(0, Math.min(100, newPercent));
        this.curLength = newPercent;
        this.changeTime(newPercent);
      }
    },

    formatSeconds(value) {
      let t = parseInt(value || 0);
      let m = parseInt((t / 60) % 60);
      let s = parseInt(t % 60);
      return (m < 10 ? "0" : "") + m + ":" + (s < 10 ? "0" : "") + s;
    },

    replaceLName(str) {
      let arr = (str || "").split("-");
      return arr[0] || "";
    },
    replaceFName(str) {
      let arr = (str || "").split("-");
      return arr[1] || str || "";
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
          if (value !== "") result.push([parseInt(t[0], 10) * 60 + parseFloat(t[1]), value]);
        }
      }
      result.sort((a, b) => a[0] - b[0]);
      return result;
    },

    changeAside() {
      this.$store.commit("setShowAside", true);
    },

   toLyric() {
  var curPath = (this.$route && this.$route.path) ? this.$route.path : "";

  if (curPath === "/lyric") {
    var from = (this.$route && this.$route.query) ? this.$route.query.from : "";
    if (from) {
      this.$router.replace(from);
    } else {
      this.$router.back();
    }
    return;
  }

  var from2 = (this.$route && this.$route.fullPath) ? this.$route.fullPath : "/";
  var q = { from: from2 };

  if (this.id !== null && this.id !== undefined && String(this.id) !== "") {
    q.songId = this.id;
  }

  this.$router.push({ path: "/lyric", query: q });
},


    // 下载
    download() {
      if (!this.id || this.id === "") {
        this.notify("当前未选择音乐。", "error");
        return;
      }
      download(this.url)
        .then((res) => {
          let content = res.data;
          let eleLink = document.createElement("a");
          eleLink.download = `${this.artist}-${this.title}.mp3`;
          eleLink.style.display = "none";
          let blob = new Blob([content]);
          eleLink.href = URL.createObjectURL(blob);
          document.body.appendChild(eleLink);
          eleLink.click();
          document.body.removeChild(eleLink);
        })
        .catch(() => {});
    },

    // 收藏
    collection() {
      if (!this.id || this.id === "") {
        this.notify("当前未选择音乐。", "error");
        return;
      }
      if (this.loginIn) {
        var params = new URLSearchParams();
        params.append("userId", this.userId);
        params.append("type", 0);
        params.append("songId", this.id);
        setCollect(params).then((res) => {
          if (res.code == 1) {
            this.$store.commit("setIsActive", true);
            this.notify("收藏成功", "success");
          } else if (res.code == 2) {
            this.$store.commit("setIsActive", false);
            this.notify("取消收藏成功", "warning");
          } else {
            this.notify("收藏失败", "error");
          }
        });
      } else {
        this.notify("请先登录", "warning");
      }
    },

    // 分享（原逻辑保留）
    share() {
      if (this.id && this.id !== "") {
        const content =
          "分享一首《" +
          this.title +
          "》点击【" +
          window.location.origin +
          "/#/lyric?songId=" +
          this.id +
          "】与我一起聆听！";
        const aux = document.createElement("input");
        aux.setAttribute("value", content);
        document.body.appendChild(aux);
        aux.select();
        document.execCommand("copy");
        document.body.removeChild(aux);
        this.notify("您已成功获取音乐链接，快去粘贴分享吧。", "success");
      } else {
        this.notify("当前未选择音乐。", "error");
      }
    }
  }
};
</script>

<style lang="scss" scoped>
@import "../assets/css/play-bar.scss";

/* 内联 svg 默认用 currentColor，别让它黑成一坨 */
.icon path { fill: currentColor; }
</style>
