<template>
    <div class="stats-page">
        <el-row :gutter="20" class="mgb20">
            <el-col :span="24">
                <el-card>
                    <div class="section-title">按天新增用户</div>
                    <ve-line :data="dailyUserChart" :settings="lineSettings" :extend="lineExtend"></ve-line>
                </el-card>
            </el-col>
        </el-row>

        <el-row :gutter="20" class="mgb20">
            <el-col :span="12">
                <el-card>
                    <div class="section-title">热门歌曲榜</div>
                    <el-table :data="hotSongs" size="small" height="360">
                        <el-table-column type="index" label="#" width="60"></el-table-column>
                        <el-table-column prop="name" label="歌曲"></el-table-column>
                        <el-table-column prop="playCount" label="播放量" width="110" :formatter="fmtNumber"></el-table-column>
                        <el-table-column prop="collectCount" label="收藏量" width="110" :formatter="fmtNumber"></el-table-column>
                    </el-table>
                </el-card>
            </el-col>
            <el-col :span="12">
                <el-card>
                    <div class="section-title">热门歌手榜</div>
                    <el-table :data="hotSingers" size="small" height="360">
                        <el-table-column type="index" label="#" width="60"></el-table-column>
                        <el-table-column prop="name" label="歌手"></el-table-column>
                        <el-table-column prop="playCount" label="播放量" width="110" :formatter="fmtNumber"></el-table-column>
                        <el-table-column prop="collectCount" label="收藏量" width="110" :formatter="fmtNumber"></el-table-column>
                    </el-table>
                </el-card>
            </el-col>
        </el-row>

        <el-row :gutter="20" class="mgb20">
            <el-col :span="12">
                <el-card>
                    <div class="section-title">播放量排行</div>
                    <ve-histogram :data="playRankChart" :settings="barSettings"></ve-histogram>
                </el-card>
            </el-col>
            <el-col :span="12">
                <el-card>
                    <div class="section-title">收藏量排行</div>
                    <ve-histogram :data="collectRankChart" :settings="barSettings"></ve-histogram>
                </el-card>
            </el-col>
        </el-row>
    </div>
</template>

<script>
import {
    getHotSongs,
    getHotSingers,
    getPlayRank,
    getCollectRank,
    getDailyNewUsers
} from '../api/index';

export default {
    data() {
        return {
            hotSongs: [],
            hotSingers: [],
            playRank: [],
            collectRank: [],
            dailyUsers: [],
            dailyUserChart: {
                columns: ['日期', '新增用户'],
                rows: []
            },
            playRankChart: {
                columns: ['歌曲', '播放量'],
                rows: []
            },
            collectRankChart: {
                columns: ['歌曲', '收藏量'],
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
        this.fetchAll();
    },
    methods: {
        unwrap(res) {
            if (res && Object.prototype.hasOwnProperty.call(res, 'data')) {
                return res.data;
            }
            return res;
        },
        fmtNumber(row, column, value) {
            if (value === null || value === undefined || value === '') return 0;
            return value;
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
                this.playRankChart.rows = this.playRank.map(item => ({
                    '歌曲': item.name || '',
                    '播放量': item.playCount || 0
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
                this.collectRankChart.rows = this.collectRank.map(item => ({
                    '歌曲': item.name || '',
                    '收藏量': item.collectCount || 0
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
                this.dailyUserChart.rows = this.dailyUsers.map(item => ({
                    '日期': item.day || '',
                    '新增用户': item.count || 0
                }));
            } catch (e) {
                this.dailyUsers = [];
                this.dailyUserChart.rows = [];
            }
        }
    }
};
</script>

<style scoped>
.stats-page {
    padding: 20px;
}

.section-title {
    font-size: 20px;
    font-weight: bold;
    margin-bottom: 10px;
}

.mgb20 {
    margin-bottom: 20px;
}
</style>
