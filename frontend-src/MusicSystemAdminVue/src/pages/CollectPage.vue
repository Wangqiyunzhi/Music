<template>
    <div class="table">
        <div class="crumbs" style="padding: 20px 0 5px 20px">
            <i class="iconfont icon-r-list" style="font-size: 24px">
                鏀惰棌淇℃伅</i
            >
        </div>
        <div class="container">
            <div class="handle-box">
                <el-input
                    v-model="select_word"
                    placeholder="绛涢€夊叧閿瘝"
                    class="handle-input"
                ></el-input>
                <br /><br />
                <el-button type="danger" style="font-size: 18px" @click="delAll"
                    >
                    鎵归噺鍒犻櫎</el-button
                >
            </div>
        </div>
        <el-table
            ref="multipleTable"
            border
            style="width: 100%"
            height="680px"
            :data="tableData"
            @selection-change="handleSelectionChange"
        >
            <el-table-column type="selection" width="40"></el-table-column>
            <el-table-column
                prop="name"
                label="姝屾墜-姝屽悕"
                align="center"
            ></el-table-column>
            <el-table-column label="鎿嶄綔" width="150" align="center">
                <template slot-scope="scope">
                    <el-button
                        style="font-size: 18px"
                        type="danger"
                        @click="handleDelete(scope.row)"
                        >
                        鍒犻櫎</el-button
                    >
                </template>
            </el-table-column>
        </el-table>

        <el-dialog
            title="鍒犻櫎姝屾洸"
            :visible.sync="delVisible"
            width="300px"
            center
        >
            <div align="center">鍒犻櫎涓嶅彲鎭㈠锛屾槸鍚︾‘瀹氬垹闄わ紵</div>
            <span slot="footer">
                <el-button size="mini" @click="delVisible = false"
                    > 鍙栨秷</el-button
                >
                <el-button size="mini" @click="deleteRow" type="primary"> 纭畾</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
import { mixin } from "../mixins/index";
import {
    songOfSongId,
    allSong,
    getCollectOfUserId,
    deleteCollection,
} from "../api/index";

export default {
    mixins: [mixin],
    props: ["id"],
    data() {
        return {
            delVisible: false, //鍒犻櫎寮圭獥鏄惁鏄剧ず
            tableData: [],
            tempData: [],
            select_word: "",
            idx: -1, //褰撳墠閫夋嫨椤?
            multipleSelection: [], //鍝簺椤瑰凡缁忔墦鍕?
        };
    },
    watch: {
        //鎼滅储妗嗛噷闈㈢殑鍐呭鍙戠敓鍙樺寲鐨勬椂鍊欙紝鎼滅储缁撴灉table鍒楄〃鐨勫唴瀹硅窡鐫€瀹冪殑鍐呭鍙戠敓鍙樺寲
        select_word: function () {
            if (this.select_word == "") {
                this.tableData = this.tempData;
            } else {
                this.tableData = [];
                for (let item of this.tempData) {
                    if (item.name.includes(this.select_word)) {
                        this.tableData.push(item);
                    }
                }
            }
        },
    },
    created() {
        this.getData();
    },
    methods: {
        //鏌ヨ璇ョ敤鎴锋墍鏈夋敹钘忕殑姝屾洸
        getData() {
            this.tempData = [];
            this.tableData = [];
            const userId = this.$route.query.id;
            if (!userId) return;
            Promise.all([getCollectOfUserId(userId), allSong()])
                .then(([collectRes, songRes]) => {
                    let collects = [];
                    if (Array.isArray(collectRes)) collects = collectRes;
                    else if (collectRes && Array.isArray(collectRes.data)) collects = collectRes.data;
                    else if (collectRes && collectRes.data && Array.isArray(collectRes.data.data)) collects = collectRes.data.data;
                    else if (collectRes && collectRes.data && Array.isArray(collectRes.data.list)) collects = collectRes.data.list;
                    else if (collectRes && Array.isArray(collectRes.list)) collects = collectRes.list;

                    let songs = [];
                    if (Array.isArray(songRes)) songs = songRes;
                    else if (songRes && Array.isArray(songRes.data)) songs = songRes.data;
                    else if (songRes && songRes.data && Array.isArray(songRes.data.data)) songs = songRes.data.data;
                    else if (songRes && songRes.data && Array.isArray(songRes.data.list)) songs = songRes.data.list;
                    else if (songRes && Array.isArray(songRes.list)) songs = songRes.list;

                    const songMap = new Map();
                    for (const s of songs) {
                        if (s && (s.id !== undefined && s.id !== null)) songMap.set(String(s.id), s);
                    }

                    for (let item of collects) {
                        if (!item || !item.songId) continue;
                        const song = songMap.get(String(item.songId));
                        if (song) {
                            this.tempData.push(song);
                            this.tableData.push(song);
                        } else {
                            // fallback to detail api if needed
                            this.getSong(item.songId);
                        }
                    }
                })
                .catch((err) => {
                    console.log(err);
                });
        },
        //鏍规嵁姝屾洸id鏌ヨ姝屾洸瀵硅薄锛屾斁鍒皌empData鍜宼ableData閲岄潰
        getSong(id) {
            songOfSongId(id)
                .then((res) => {
                    let song = res;
                    if (res && res.data) song = res.data;
                    if (res && res.data && res.data.data) song = res.data.data;
                    if (song) {
                        this.tempData.push(song);
                        this.tableData.push(song);
                    }
                })
                .catch((err) => {
                    console.log(err);
                });
        },
        //閹垫挸绱戦崚鐘绘珟绾喛顓?        handleDelete(row) {
            this.idx = row;
            this.delVisible = true;
        },
        //鍒犻櫎涓€鏉℃瓕鏇?
        deleteRow() {
            deleteCollection(this.$route.query.id, this.idx.id)
                .then((res) => {
                    if (res) {
                        this.getData();
                        this.notify("鍒犻櫎鎴愬姛", "success");
                    } else {
                        this.notify("鍒犻櫎澶辫触", "error");
                    }
                })
                .catch((err) => {
                    console.log(err);
                });
            this.delVisible = false;
        },
        //鎵归噺鍒犻櫎宸茬粡閫夋嫨鐨勯」
        delAll() {
            for (let item of this.multipleSelection) {
                this.handleDelete(item);
                this.deleteRow();
            }
            this.multipleSelection = [];
        },
    },
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
</style>
