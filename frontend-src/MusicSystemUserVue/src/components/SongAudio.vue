<template>
  <div class="song-audio">
    <audio
      ref="player"
      :src="url"
      preload="auto"
      @canplay="startPlay"
      @ended="ended"
      @timeupdate="timeupdate"
    ></audio>
  </div>
</template>

<script>
import { mapGetters } from "vuex";

export default {
  name: "song-audio",
  computed: {
    ...mapGetters([
      "id",
      "url",
      "isPlay",
      "listOfSongs",
      "curTime",
      "changeTime",
      "autoNext",
      "volume",
    ]),
  },
  watch: {
    // 播放/暂停
    isPlay: {
      immediate: true,
      handler() {
        this.togglePlay();
      },
    },
    // 跳转播放进度
    changeTime(val) {
      const player = this.$refs.player;
      if (!player) return;
      try {
        player.currentTime = val || 0;
      } catch (e) {
        // ignore
      }
    },
    // 音量（兼容 0~1 或 0~100）
    volume: {
      immediate: true,
      handler(val) {
        const player = this.$refs.player;
        if (!player) return;

        let v = Number(val);
        if (Number.isNaN(v)) v = 0.5;

        // 兼容你以前的 50 / 100 这种
        if (v > 1) v = v / 100;

        // clamp 到 0~1
        v = Math.max(0, Math.min(1, v));
        player.volume = v;
      },
    },
  },
  methods: {
    // 资源可播时：写入 duration，并且如果当前状态是要播放就直接 play
    startPlay() {
      const player = this.$refs.player;
      if (!player) return;

      this.$store.commit("setDuration", player.duration || 0);

      // 如果还没设播放状态（比如点歌时），直接置为播放
      if (!this.isPlay) {
        this.$store.commit("setIsPlay", true);
      }

      // 真正触发播放
      player.play().catch(() => {
        // 某些浏览器需要用户手势；你是点击触发，一般不会进这里
        // 进这里就保持 isPlay=false，避免 UI 假播放
        this.$store.commit("setIsPlay", false);
      });
    },

    ended() {
      this.$store.commit("setIsPlay", false);
      this.$store.commit("setCurTime", 0);
      this.$store.commit("setAutoNext", !this.autoNext);
    },

    togglePlay() {
      const player = this.$refs.player;
      if (!player) return;

      if (!this.url) {
        player.pause();
        return;
      }

      if (this.isPlay) {
        player.play().catch(() => {
          this.$store.commit("setIsPlay", false);
        });
      } else {
        player.pause();
      }
    },

    timeupdate() {
      const player = this.$refs.player;
      if (!player) return;
      this.$store.commit("setCurTime", player.currentTime || 0);
    },
  },
};
</script>

<style>
.song-audio {
  display: none;
}
</style>
