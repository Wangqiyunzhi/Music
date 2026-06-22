<template>
    <div class="info-page">
        <div class="page-title">绯荤粺姒傝</div>

        <el-row :gutter="20" class="mgb20">
            <el-col :span="6">
                <el-card class="stat-card">
                    <div class="stat-label">鐢ㄦ埛鎬绘暟</div>
                    <div class="stat-value">{{ consumerCount }}</div>
                    <div class="stat-sub">绱娉ㄥ唽鐢ㄦ埛</div>
                </el-card>
            </el-col>
            <el-col :span="6">
                <el-card class="stat-card">
                    <div class="stat-label">姝屾洸鎬绘暟</div>
                    <div class="stat-value">{{ songCount }}</div>
                    <div class="stat-sub">宸叉敹褰曟瓕鏇?/div>
                </el-card>
            </el-col>
            <el-col :span="6">
                <el-card class="stat-card">
                    <div class="stat-label">姝屾墜鎬绘暟</div>
                    <div class="stat-value">{{ singerCount }}</div>
                    <div class="stat-sub">鍏ュ簱姝屾墜</div>
                </el-card>
            </el-col>
            <el-col :span="6">
                <el-card class="stat-card">
                    <div class="stat-label">姝屽崟鎬绘暟</div>
                    <div class="stat-value">{{ songListCount }}</div>
                    <div class="stat-sub">宸插垱寤烘瓕鍗?/div>
                </el-card>
            </el-col>
        </el-row>

        <el-row :gutter="20" class="mgb20">
            <el-col :span="24">
                <el-card class="chart-card">
                    <div class="section-title">鎸夊ぉ鏂板鐢ㄦ埛</div>
                    <ve-line
                        :data="dailyUserChart"
                        :settings="lineSettings"
                        :extend="lineExtend"
                    ></ve-line>
                </el-card>
            </el-col>
        </el-row>

        <el-row :gutter="20" class="mgb20">
            <el-col :span="12">
                <el-card class="table-card">
                    <div class="section-title">鐑棬姝屾洸姒?/div>
                    <el-table :data="hotSongs" size="small" height="320">
                        <el-table-column
                            type="index"
                            label="#"
                            width="60"
                        ></el-table-column>
                        <el-table-column
                            prop="name"
                            label="姝屾洸"
                        ></el-table-column>
                        <el-table-column
                            prop="playCount"
                            label="鎾斁閲?
                            width="110"
                            :formatter="fmtNumber"
                        ></el-table-column>
                        <el-table-column
                            prop="collectCount"
                            label="鏀惰棌閲?
                            width="110"
                            :formatter="fmtNumber"
                        ></el-table-column>
                    </el-table>
                </el-card>
            </el-col>
            <el-col :span="12">
                <el-card class="table-card">
                    <div class="section-title">鐑棬姝屾墜姒?/div>
                    <el-table :data="hotSingers" size="small" height="320">
                        <el-table-column
                            type="index"
                            label="#"
                            width="60"
                        ></el-table-column>
                        <el-table-column
                            prop="name"
                            label="姝屾墜"
                        ></el-table-column>
                        <el-table-column
                            prop="playCount"
                            label="鎾斁閲?
                            width="110"
                            :formatter="fmtNumber"
                        ></el-table-column>
                        <el-table-column
                            prop="collectCount"
                            label="鏀惰棌閲?
                            width="110"
                            :formatter="fmtNumber"
                        ></el-table-column>
                    </el-table>
                </el-card>
            </el-col>
        </el-row>

        <el-row :gutter="20" class="mgb20">
            <el-col :span="12">
                <el-card class="chart-card">
                    <div class="section-title">鎾斁閲忔帓琛?/div>
                    <ve-histogram
                        :data="playRankChart"
                        :settings="barSettings"
                    ></ve-histogram>
                </el-card>
            </el-col>
            <el-col :span="12">
                <el-card class="chart-card">
                    <div class="section-title">鏀惰棌閲忔帓琛?/div>
                    <ve-histogram
                        :data="collectRankChart"
                        :settings="barSettings"
                    ></ve-histogram>
                </el-card>
            </el-col>
        </el-row>
    </div>
</template>

<script>
import { mixin } from "../mixins/index";
import {
    getAllConsumer,
    allSong,
    getAllSinger,
    getAllSongList,
    getHotSongs,
    getHotSingers,
    getPlayRank,
    getCollectRank,
    getDailyNewUsers
} from "../api/index";

export default {
    mixins: [mixin],
    data() {
        return {
            consumerCount: 0,
            songCount: 0,
            singerCount: 0,
            songListCount: 0,
            hotSongs: [],
            hotSingers: [],
            playRank: [],
            collectRank: [],
            dailyUsers: [],
            dailyUserChart: {
                columns: ["鏃ユ湡", "鏂板鐢ㄦ埛"],
                rows: []
            },
            playRankChart: {
                columns: ["姝屾洸", "鎾斁閲?],
                rows: []
            },
            collectRankChart: {
                columns: ["姝屾洸", "鏀惰棌閲?],
                rows: []
            },
            lineSettings: {
                area: true
            },
            lineExtend: {
                grid: { left: 40, right: 20, top: 20, bottom: 30 }
            },
            barSettings: {
                labelMap: {}
            }
        };
    },
    mounted() {
        this.loadBaseCounts();
        this.fetchAll();
    },
    methods: {
        unwrap(res) {
            if (res && Object.prototype.hasOwnProperty.call(res, "data")) {
                return res.data;
            }
            return res;
        },
        fmtNumber(row, column, value) {
            if (value === null || value === undefined || value === "") return 0;
            return value;
        },
        async loadBaseCounts() {
            try {
                const list = await getAllConsumer();
                this.consumerCount = Array.isArray(list) ? list.length : 0;
            } catch (e) {
                this.consumerCount = 0;
            }
            try {
                const list = await allSong();
                this.songCount = Array.isArray(list) ? list.length : 0;
            } catch (e) {
                this.songCount = 0;
            }
            try {
                const list = await getAllSinger();
                this.singerCount = Array.isArray(list) ? list.length : 0;
            } catch (e) {
                this.singerCount = 0;
            }
            try {
                const list = await getAllSongList();
                this.songListCount = Array.isArray(list) ? list.length : 0;
            } catch (e) {
                this.songListCount = 0;
            }
        },
        async fetchAll() {
            await Promise.all([
                this.fetchHotSongs(),
                this.fetchHotSingers(),
                this.fetchPlayRank(),
                this.fetchCollectRank(),
                this.fetchDailyUsers()
            ]);
        },
        async fetchHotSongs() {
            try {
                const res = await getHotSongs(10);
                const list = this.unwrap(res);
                this.hotSongs = Array.isArray(list) ? list : [];
            } catch (e) {
                this.hotSongs = [];
            }
        },
        async fetchHotSingers() {
            try {
                const res = await getHotSingers(10);
                const list = this.unwrap(res);
                this.hotSingers = Array.isArray(list) ? list : [];
            } catch (e) {
                this.hotSingers = [];
            }
        },
        async fetchPlayRank() {
            try {
                const res = await getPlayRank(10);
                const list = this.unwrap(res);
                this.playRank = Array.isArray(list) ? list : [];
                this.playRankChart.rows = this.playRank.map((item) => ({
                    姝屾洸: item.name || "",
                    鎾斁閲? item.playCount || 0
                }));
            } catch (e) {
                this.playRank = [];
                this.playRankChart.rows = [];
            }
        },
        async fetchCollectRank() {
            try {
                const res = await getCollectRank(10);
                const list = this.unwrap(res);
                this.collectRank = Array.isArray(list) ? list : [];
                this.collectRankChart.rows = this.collectRank.map((item) => ({
                    姝屾洸: item.name || "",
                    鏀惰棌閲? item.collectCount || 0
                }));
            } catch (e) {
                this.collectRank = [];
                this.collectRankChart.rows = [];
            }
        },
        async fetchDailyUsers() {
            try {
                const res = await getDailyNewUsers(14);
                const list = this.unwrap(res);
                this.dailyUsers = Array.isArray(list) ? list : [];
                this.dailyUserChart.rows = this.dailyUsers.map((item) => ({
                    鏃ユ湡: item.day || "",
                    鏂板鐢ㄦ埛: item.count || 0
                }));
            } catch (e) {
                this.dailyUsers = [];
                this.dailyUserChart.rows = [];
            }
        },
    }
};
</script>

<style scoped>
.info-page {
    padding: 20px;
}

.page-title {
    font-size: 22px;
    font-weight: 700;
    margin-bottom: 16px;
    color: #0f172a;
}

.section-title {
    font-size: 18px;
    font-weight: 700;
    margin-bottom: 12px;
    color: #0f172a;
}

.stat-card {
    min-height: 120px;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
}

.stat-label {
    font-size: 14px;
    color: #64748b;
}

.stat-value {
    font-size: 30px;
    font-weight: 700;
    color: #111827;
    margin: 6px 0;
}

.stat-sub {
    font-size: 12px;
    color: #94a3b8;
}

.chart-card,
.table-card {
    min-height: 360px;
}
</style>
