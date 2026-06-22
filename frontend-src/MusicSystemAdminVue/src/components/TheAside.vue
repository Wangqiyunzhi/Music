<template>
    <div class="sidebar">
        <el-menu
            class="sidebar-el-menu"
            :default-active="onRoutes"
            :collapse="collapse"
            background-color="#334256"
            text-color="#ffffff"
            active-text-color="#20a0ff"
            router
         >
            <template v-for="item in items">
                <template>
                    <el-menu-item :index="item.index" :key="item.index">
                        <span class="menu-title">{{ item.title }}</span>
                    </el-menu-item>
                </template>
            </template>
        </el-menu>
    </div>
</template>

<script>
import bus from "../assets/js/bus";
export default {
    data() {
        return {
            collapse: false,
            items: [
                {
                    icon: 'iconfont icon-r-home',
                    index: 'Info',
                    title: '系统首页'
                },
                {
                    icon: 'iconfont icon-r-user1',
                    index: 'AdminInfo',
                    title: '管理员信息'
                },
                {
                    icon: 'iconfont icon-r-user1',
                    index: 'Consumer',
                    title: '用户管理'
                },
                {
                    icon: 'iconfont icon-r-love',
                    index: 'Singer',
                    title: '歌手管理'
                },
                {
                    icon: 'iconfont icon-r-list',
                    index: 'SongList',
                    title: '歌单管理'
                },
                {
                    icon: 'iconfont icon-r-mark1',
                    index: 'Banner',
                    title: '轮播图管理'
                },
                {
                    icon: 'iconfont icon-r-mark2',
                    index: 'Notice',
                    title: '公告管理'
                },
                {
                    icon: 'iconfont icon-r-mark4',
                    index: 'Stats',
                    title: '统计报表'
                }
            ]
        };
    },
    computed: {
        onRoutes() {
            return this.$route.path.replace('/', '');
        }
    },
    created() {
        bus.$on('collapse', msg => {
            this.collapse = msg;
        });
    }
};
</script>

<style scoped>
.sidebar {
    display: block;
    position: absolute;
    left: 0;
    top: 70px;
    bottom: 0;
    background: linear-gradient(180deg, #0f172a 0%, #1f2937 100%);
    overflow-y: scroll;
}

.sidebar::-webkit-scrollbar {
    width: 0;
}

.sidebar-el-menu:not(.el-menu--collapse) {
    width: 150px;
}

.sidebar > ul {
    height: 100%;
}

.sidebar-el-menu .el-menu-item .menu-title {
    display: inline-block;
    width: 110px;
    text-align: left;
}

/deep/ .el-menu {
    background: transparent !important;
}

/deep/ .el-menu-item {
    margin: 6px 8px;
    border-radius: 12px;
    transition: all 0.2s ease;
}

/deep/ .el-menu-item.is-active {
    background: rgba(59, 130, 246, 0.18) !important;
    color: #e0f2fe !important;
}

/deep/ .el-menu-item:hover {
    background: rgba(255, 255, 255, 0.08) !important;
}
</style>

