<template>
  <div class="table">
    <div class="crumbs" style="padding: 20px 0 5px 20px">
      <i class="iconfont icon-r-list" style="font-size: 24px">姝屽崟姝屾洸绠＄悊</i>
    </div>

    <div class="container">
      <div class="handle-box">
        <el-input v-model="select_word" placeholder="璇疯緭鍏ユ瓕鏇插悕" class="handle-input"></el-input>
        <br /><br />
        <el-button type="danger" style="font-size: 18px" @click="delAll">鎵归噺鍒犻櫎</el-button>
        <el-button type="success" style="font-size: 18px" @click="centerDialogVisible = true">娣诲姞姝屾洸</el-button>
      </div>
    </div>

    <el-table
      ref="multipleTable"
      border
      style="width: 100%"
      height="680px"
      :data="data"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="40"></el-table-column>
      <el-table-column prop="name" label="姝屾墜-姝屽悕" align="center"></el-table-column>

      <el-table-column label="鎿嶄綔" width="150" align="center" fixed="right">
        <template slot-scope="scope">
          <el-button style="font-size: 18px" type="danger" @click="handleDelete(scope.row.id)">鍒犻櫎</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination">
      <el-pagination
        background
        layout="total,prev,pager,next"
        :current-page="currentPage"
        :page-size="pageSize"
        :total="tableData.length"
        @current-change="handleCurrentChange"
      ></el-pagination>
    </div>

    <!-- 娣诲姞姝屾洸 -->
    <el-dialog title="娣诲姞姝屾洸" :visible.sync="centerDialogVisible" width="400px" center>
      <el-form :model="registerForm" ref="registerForm" label-width="80px">
        <el-form-item prop="singerName" label="姝屾墜鍚嶅瓧" size="mini">
          <el-input v-model="registerForm.singerName" placeholder="姝屾墜鍚嶅瓧"></el-input>
        </el-form-item>
        <el-form-item prop="songName" label="姝屾洸鍚嶅瓧" size="mini">
          <el-input v-model="registerForm.songName" placeholder="姝屾洸鍚嶅瓧"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button size="mini" @click="centerDialogVisible = false">鍙栨秷</el-button>
        <el-button size="mini" type="primary" @click="addToSongList">纭畾</el-button>
      </span>
    </el-dialog>

    <!-- 鍒犻櫎纭 -->
    <el-dialog title="鍒犻櫎姝屾洸" :visible.sync="delVisible" width="300px" center>
      <div align="center">鍒犻櫎涓嶅彲鎭㈠锛屾槸鍚︾‘瀹氬垹闄わ紵</div>
      <span slot="footer">
        <el-button size="mini" @click="delVisible = false">鍙栨秷</el-button>
        <el-button size="mini" type="primary" @click="deleteRow">纭畾</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { mixin } from "../mixins/index";
import { listSongDetail, listSongAdd, delListSong, songOfName, allSong } from "../api/index";

export default {
  mixins: [mixin],
  data() {
    return {
      centerDialogVisible: false,
      delVisible: false,

      // 姝屽崟鍙傛暟
      songListId: "",
      songListTitle: "",

      registerForm: {
        singerName: "",
        songName: ""
      },

      tableData: [],
      tempData: [],
      select_word: "",
      pageSize: 10,
      currentPage: 1,

      idx: -1,
      multipleSelection: []
    };
  },

  computed: {
    data() {
      return this.tableData.slice(
        (this.currentPage - 1) * this.pageSize,
        this.currentPage * this.pageSize
      );
    }
  },

  watch: {
    select_word() {
      if (!this.select_word) {
        this.tableData = this.tempData;
      } else {
        var list = this.tempData || [];
        var kw = this.select_word;
        var out = [];
        for (var i = 0; i < list.length; i++) {
          var item = list[i] || {};
          var name = item.name || "";
          if (name.indexOf(kw) !== -1) out.push(item);
        }
        this.tableData = out;
      }
    }
  },

  created() {
    // 绾﹀畾锛歋ongListPage 鐐瑰嚮鈥滄瓕鍗曠鐞嗏€濊烦杞埌 /ListSong?id=xxx&title=xxx
    this.songListId = this.$route.query.id || this.$route.query.songListId || "";
    this.songListTitle = this.$route.query.title || this.$route.query.name || "";

    this.getData();
  },

  methods: {
    handleCurrentChange(val) {
      this.currentPage = val;
    },

    // 閫変腑琛?    handleSelectionChange(val) {
      this.multipleSelection = val || [];
    },

    // 鎵归噺鍒犻櫎
    delAll() {
      var arr = this.multipleSelection || [];
      if (!arr.length) return this.$message && this.$message.warning("璇峰厛鍕鹃€夎鍒犻櫎鐨勬瓕鏇?);

      // 閫愪釜鍒犻櫎锛堜繚鎸佷綘鐨勫悗绔帴鍙ｄ笉鍙橈級
      var _this = this;
      var count = 0;

      function next(i) {
        if (i >= arr.length) {
          _this.getData();
          _this.$message && _this.$message.success("鎵归噺鍒犻櫎瀹屾垚锛? + count + " 鏉?);
          return;
        }
        delListSong(arr[i].id, _this.songListId)
          .then(function () {
            count++;
            next(i + 1);
          })
          .catch(function () {
            next(i + 1);
          });
      }
      next(0);
    },

    // 鎷夊彇姝屽崟閲岀殑姝屾洸
    getData() {
      var _this = this;
      _this.tempData = [];
      _this.tableData = [];

      if (!_this.songListId) {
        _this.currentPage = 1;
        return;
      }

      Promise.all([listSongDetail(_this.songListId), allSong()])
        .then(function ([listRes, songRes]) {
          var arr = [];
          if (Array.isArray(listRes)) arr = listRes;
          else if (listRes && Array.isArray(listRes.data)) arr = listRes.data;
          else if (listRes && listRes.data && Array.isArray(listRes.data.data)) arr = listRes.data.data;
          else if (listRes && listRes.data && Array.isArray(listRes.data.list)) arr = listRes.data.list;
          else if (listRes && Array.isArray(listRes.list)) arr = listRes.list;

          var songs = [];
          if (Array.isArray(songRes)) songs = songRes;
          else if (songRes && Array.isArray(songRes.data)) songs = songRes.data;
          else if (songRes && songRes.data && Array.isArray(songRes.data.data)) songs = songRes.data.data;
          else if (songRes && songRes.data && Array.isArray(songRes.data.list)) songs = songRes.data.list;
          else if (songRes && Array.isArray(songRes.list)) songs = songRes.list;

          var songMap = new Map();
          for (var i = 0; i < songs.length; i++) {
            var s = songs[i];
            if (s && s.id !== undefined && s.id !== null) songMap.set(String(s.id), s);
          }

          var merged = [];
          for (var j = 0; j < arr.length; j++) {
            var item = arr[j] || {};
            var songId = item.songId || item.song_id || item.songID || item.songid || item.id;
            var song = songMap.get(String(songId));
            if (song) merged.push(song);
            else if (item.name) merged.push(item);
          }

          _this.tempData = merged;
          _this.tableData = merged;
          _this.currentPage = 1;

          if (!merged.length && listRes && listRes.code === 0 && listRes.msg) {
            _this.notify && _this.notify(listRes.msg, "error");
          }
        })
        .catch(function (err) {
          console.log(err);
          _this.tempData = [];
          _this.tableData = [];
          _this.currentPage = 1;
        });
    },

    // 鎵撳紑鍒犻櫎纭
    handleDelete(id) {
      this.idx = id;
      this.delVisible = true;
    },

    // 鍒犻櫎涓€鏉?    deleteRow() {
      var _this = this;
      delListSong(this.idx, this.songListId)
        .then(function (res) {
          if (res && res.code == 1) {
            _this.getData();
            _this.notify && _this.notify("鍒犻櫎鎴愬姛", "success");
          } else {
            _this.notify && _this.notify("鍒犻櫎澶辫触", "error");
          }
        })
        .catch(function (err) {
          console.log(err);
        });

      this.delVisible = false;
    },

    // 娣诲姞鍒版瓕鍗曪細鍏堟寜鈥滄瓕鎵?姝屽悕鈥濈簿纭煡鍒?songId锛屽啀璋冪敤 listSongAdd
    addToSongList() {
      var _this = this;
      if (!_this.songListId) return;

      var fullName = (_this.registerForm.singerName || "") + "-" + (_this.registerForm.songName || "");
      fullName = fullName.replace(/^-|-$|\s+/g, "");

      if (!fullName || fullName.indexOf("-") === -1) {
        return _this.$message && _this.$message.warning("璇疯緭鍏ワ細姝屾墜鍚嶅瓧 + 姝屾洸鍚嶅瓧");
      }

      songOfName(fullName)
        .then(function (songList) {
          var song = null;
          if (Array.isArray(songList) && songList.length) song = songList[0];

          if (!song || !song.id) {
            _this.$message && _this.$message.error("鏈壘鍒版瓕鏇诧細" + fullName);
            return;
          }

          var params = new URLSearchParams();
          params.append("songListId", _this.songListId);
          params.append("songId", song.id);

          listSongAdd(params).then(function (res) {
            if (res && res.code == 1) {
              _this.centerDialogVisible = false;
              _this.registerForm.singerName = "";
              _this.registerForm.songName = "";
              _this.getData();
              _this.notify && _this.notify("娣诲姞鎴愬姛", "success");
            } else {
              _this.notify && _this.notify("娣诲姞澶辫触", "error");
            }
          });
        })
        .catch(function (err) {
          console.log(err);
          _this.$message && _this.$message.error("鏌ヨ姝屾洸澶辫触");
        });
    }
  }
};
</script>

<style scoped>
.handle-box {
  margin-bottom: 20px;
}
.handle-input {
  width: 300px;
  display: inline-block;
}
.pagination {
  display: flex;
  justify-content: center;
}
</style>
