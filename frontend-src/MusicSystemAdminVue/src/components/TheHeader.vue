<template>
    <div class="header">
        <div class="collapse-btn" @click="collapseChange">
            <i class="el-icon-menu"></i>
        </div>
        <div class="header-back" v-if="showBack" @click="goBack" title="杩斿洖涓婁竴椤?>
            <i class="el-icon-arrow-left"></i>
            <span>杩斿洖</span>
        </div>
        <div class="logo">娆㈣繋璁块棶闊充箰鍚庡彴绠＄悊绯荤粺</div>
        <div class="header-right">
            <div class="btn-fullscreen" @click="handleFullScreen">
                <el-tooltip :content="fullscreen ? `鍙栨秷鍏ㄥ睆` : `鍏ㄥ睆`" placement="bottom">
                    <i class="el-icon-rank"></i>
                </el-tooltip>
            </div>
            <div class="user-avator">
                <img :src="userAvatar"/>
            </div>
            <el-dropdown class="user-name" trigger="click" @command="handleCommand">
                <span class="el-dropdown-link">
                    {{ userName }}
                    <i class="el-icon-caret-bottom"></i>
                </span>
                <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item command="logout">閫€鍑虹櫥褰?/el-dropdown-item>
                </el-dropdown-menu>
            </el-dropdown>
        </div>
    </div>
</template>

<script>
import bus from "../assets/js/bus";
import { getAdminInfo } from "../api/index";
export default {
    data() {
        return {
            collapse: false,
            fullscreen: false,
            avatarPath: ""
        };
    },
    computed: {
        userName() {
            return localStorage.getItem("userName");
        },
        userAvatar() {
            if (this.avatarPath) {
                if (
                    this.avatarPath.startsWith("http://") ||
                    this.avatarPath.startsWith("https://") ||
                    this.avatarPath.startsWith("//")
                ) {
                    return this.avatarPath;
                }
                if (this.avatarPath.startsWith("/")) {
                    return `${this.$store.state.HOST}${this.avatarPath}`;
                }
                return `${this.$store.state.HOST}/${this.avatarPath}`;
            }
            return require("../assets/img/user.jpg");
        },
        showBack() {
            const path = (this.$route && this.$route.path) || "";
            if (path === "/" || path === "/Home" || path === "/Info") return false;
            return true;
        }
    },
    mounted() {
        const cached = localStorage.getItem("adminAvatar");
        if (cached) this.avatarPath = cached;
        this.loadAvatar();
        bus.$on("admin-avatar-updated", (path) => {
            if (path) {
                this.avatarPath = path;
                localStorage.setItem("adminAvatar", path);
            }
        });
    },
    methods: {
        async loadAvatar() {
            const name = localStorage.getItem("userName") || "";
            if (!name) return;
            try {
                const res = await getAdminInfo(name);
                const data = res && Object.prototype.hasOwnProperty.call(res, "data") ? res.data : res;
                if (data && data.avator) {
                    this.avatarPath = data.avator;
                    localStorage.setItem("adminAvatar", data.avator);
                }
            } catch (e) {}
        },
        collapseChange() {
            this.collapse = !this.collapse;
            bus.$emit("collapse", this.collapse);
        },
        handleFullScreen() {
            if (this.fullscreen) {
                if (document.exitFullscreen) {
                    document.exitFullscreen();
                } else if (document.webkitCancelFullScreen) {
                    document.webkitCancelFullScreen();
                } else if (document.mozCancelFullScreen) {
                    document.mozCancelFullScreen();
                } else if (document.msExitFullScreen) {
                    document.msExitFullScreen();
                }
            } else {
                let element = document.documentElement;
                if (element.requestFullscreen) {
                    element.requestFullscreen();
                } else if (element.webkitRequestFullScreen) {
                    element.webkitRequestFullScreen();
                } else if (element.mozRequestFullScreen) {
                    element.mozRequestFullScreen();
                } else if (element.msRequestFullScreen) {
                    element.msRequestFullScreen();
                }
            }
            this.fullscreen = !this.fullscreen;
        },
        handleCommand(command) {
            if (command === "logout") {
                localStorage.removeItem("userName");
                this.$router.push("/");
            }
        },
        goBack() {
            if (window.history.length > 1) {
                this.$router.back();
            } else {
                this.$router.push("/Home");
            }
        }
    }
};
</script>

<style scoped>
.header{
    position: relative;
    background-color: #253041;
    box-sizing: border-box;
    width: 100%;
    height: 70px;
    font-size: 22px;
    color: #ffffff;
}

.collapse-btn{
    float: left;
    padding: 0 21px;
    cursor: pointer;
    line-height: 70px;
}

.header-back{
    float: left;
    margin-top: 18px;
    margin-right: 10px;
    padding: 6px 14px;
    border-radius: 999px;
    background: #e8f0ff;
    font-size: 12px;
    border: 1px solid #c7d2fe;
    color: #1d4ed8;
    box-shadow: 0 8px 18px rgba(15, 23, 42, 0.18);
    cursor: pointer;
    display: flex;
    align-items: center;
    gap: 6px;
    line-height: 1;
    height: 28px;
}

.header-back:hover{
    transform: translateY(-1px);
    background: #dbe7ff;
}

.header .logo{
    float: left;
    line-height: 70px;
}

.header-right{
    float: right;
    padding-right: 50px;
    display:flex;
    height: 70px;
    align-items: center;
}

.btn-fullscreen{
    transform: rotate(45deg);
    margin-right: 5px;
    font-size: 24px;
}

.user-avator{
    margin-left: 20px;
}

.user-avator img{
    display: block;
    width: 63px;
    height: 63px;
    border-radius: 5px;
}

.user-name{
    margin-left: 10px;
    font-size: 22px;
}

.el-dropdown-link{
    color: #ffffff;
    cursor: pointer;
}
</style>
