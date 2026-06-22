import { mapGetters } from "vuex";
import { likeSongOfName, getCollectOfUserId, addPlayHistory } from "../api/index";

export const mixin = {
  computed: {
    ...mapGetters(["loginIn", "userId"])
  },
  methods: {
    notify(title, type) {
      this.$notify({ title, type });
    },

    attachImageUrl(srcUrl) {
      return srcUrl
        ? this.$store.state.configure.HOST + srcUrl
        : this.$store.state.configure.HOST + "/img/user.jpg";
    },

    getSong() {
      if (!this.$route.query.keywords) {
        this.$store.commit("setListOfSongs", []);
        this.notify("请输入搜索内容", "warning");
      } else {
        likeSongOfName(this.$route.query.keywords)
          .then((res) => {
            if (!res || !res.length) {
              this.$store.commit("setListOfSongs", []);
              this.notify("暂无符合条件的歌曲", "warning");
            } else {
              this.$store.commit("setListOfSongs", res);
            }
          })
          .catch((err) => console.log(err));
      }
    },

    replaceLName(str) {
      let arr = (str || "").split("-");
      return arr[0] || "";
    },

    replaceFName(str) {
      let arr = (str || "").split("-");
      return arr[1] || str || "";
    },

    toplay: function (id, url, pic, index, name, lyric, sourceList) {
      if (Array.isArray(sourceList) && sourceList.length) {
        this.$store.commit("setListOfSongs", sourceList);
      }

      this.$store.commit("setId", id);
      this.$store.commit("setUrl", this.$store.state.configure.HOST + url);
      this.$store.commit("setPicUrl", this.$store.state.configure.HOST + pic);
      this.$store.commit("setListIndex", typeof index === "number" ? index : 0);
      this.$store.commit("setTitle", this.replaceFName(name));
      this.$store.commit("setArtist", this.replaceLName(name));
      this.$store.commit("setLyric", this.parseLyric(lyric || ""));
      this.$store.commit("setIsActive", false);

      this.$store.commit("pushRecent", { id, url, pic, name, lyric });
      this.$store.commit("setIsPlay", true);

      if (this.loginIn) {
        getCollectOfUserId(this.userId).then((res) => {
          const arr = Array.isArray(res) ? res : [];
          for (let item of arr) {
            if (String(item.songId) === String(id)) {
              this.$store.commit("setIsActive", true);
              break;
            }
          }
        });

        if (this.userId) {
          const params = new URLSearchParams();
          params.append("userId", this.userId);
          params.append("songId", id);
          addPlayHistory(params).catch(() => {});
        }
      }
    },

    parseLyric(text) {
      text = text || "";
      let lines = text.split("\n");
      let pattern = /\[\d{2}:\d{2}.(\d{3}|\d{2})\]/g;
      let result = [];

      if (!/\[.+\]/.test(text)) return [[0, text]];

      while (lines.length && !pattern.test(lines[0])) {
        lines = lines.slice(1);
      }

      for (let item of lines) {
        let time = item.match(pattern);
        let value = item.replace(pattern, "");
        if (!time || value === undefined) continue;
        for (let tItem of time) {
          let t = tItem.slice(1, -1).split(":");
          if (value !== "") {
            result.push([parseInt(t[0], 10) * 60 + parseFloat(t[1]), value]);
          }
        }
      }

      result.sort((a, b) => a[0] - b[0]);
      return result;
    },

    attachBirth(val) {
      return (val || "").substr(0, 10);
    }
  }
};
