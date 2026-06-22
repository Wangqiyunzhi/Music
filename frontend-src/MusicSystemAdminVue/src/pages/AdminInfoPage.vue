<template>
    <div class="admin-info-page">
        <div class="page-title">绠＄悊鍛樹俊鎭?/div>

        <el-row :gutter="20">
            <el-col :span="12">
                <el-card class="admin-card">
                    <el-form
                        :model="adminForm"
                        label-width="90px"
                        class="admin-form"
                    >
                        <el-form-item label="澶村儚">
                            <el-upload
                                class="avatar-uploader"
                                :action="avatarAction"
                                :data="{ id: adminForm.id, currentName: adminForm.name }"
                                :show-file-list="false"
                                :before-upload="beforeAvatarUpload"
                                :on-success="handleAvatarSuccess"
                            >
                                <img
                                    v-if="adminForm.avator"
                                    :src="getUrl(adminForm.avator)"
                                    class="avatar"
                                />
                                <div v-else class="avatar-placeholder">
                                    鐐瑰嚮涓婁紶
                                </div>
                            </el-upload>
                        </el-form-item>
                        <el-form-item label="璐﹀彿">
                            <el-input
                                v-model="adminForm.name"
                                placeholder="璇疯緭鍏ヨ处鍙?
                            ></el-input>
                        </el-form-item>
                        <el-form-item label="鏂板瘑鐮?>
                            <el-input
                                v-model="adminForm.password"
                                type="password"
                                show-password
                                placeholder="涓嶄慨鏀瑰彲鐣欑┖"
                            ></el-input>
                        </el-form-item>
                        <el-form-item>
                            <el-button type="primary" @click="saveAdmin"
                                >淇濆瓨淇℃伅</el-button
                            >
                        </el-form-item>
                    </el-form>
                </el-card>
            </el-col>
            <el-col :span="12">
                <el-card class="tips-card">
                    <div class="section-title">浣跨敤璇存槑</div>
                    <ul class="tips-list">
                        <li>淇敼璐﹀彿鍚庨渶瑕侀噸鏂扮櫥褰曟墠鑳界敓鏁堛€?/li>
                        <li>鏂板瘑鐮佺暀绌哄垯涓嶄細淇敼瀵嗙爜銆?/li>
                        <li>澶村儚浠呮敮鎸?JPG/PNG锛屽ぇ灏忎笉瓒呰繃 2MB銆?/li>
                    </ul>
                </el-card>
            </el-col>
        </el-row>
    </div>
</template>

<script>
import { mixin } from "../mixins/index";
import { getAdminInfo, updateAdminInfo } from "../api/index";
import bus from "../assets/js/bus";

export default {
    mixins: [mixin],
    data() {
        return {
            adminForm: {
                id: null,
                name: "",
                password: "",
                avator: ""
            }
        };
    },
    computed: {
        avatarAction() {
            return `${this.$store.state.HOST}/admin/updateAvatar`;
        }
    },
    mounted() {
        this.loadAdmin();
    },
    methods: {
        unwrap(res) {
            if (res && Object.prototype.hasOwnProperty.call(res, "data")) {
                return res.data;
            }
            return res;
        },
        async loadAdmin() {
            const name = localStorage.getItem("userName") || "";
            try {
                const res = await getAdminInfo(name);
                if (res && res.code === 1 && res.data) {
                    if (typeof res.data === "string") {
                        this.adminForm.name = res.data;
                    } else {
                        const info = Array.isArray(res.data)
                            ? res.data[0]
                            : res.data;
                        if (info) {
                            this.adminForm.id = info.id || null;
                            this.adminForm.name = info.name || name;
                            this.adminForm.avator = info.avator || "";
                        }
                    }
                    return;
                }

                const data = this.unwrap(res);
                const info = Array.isArray(data) ? data[0] : data;
                if (info) {
                    this.adminForm.id = info.id || null;
                    this.adminForm.name = info.name || name;
                    this.adminForm.avator = info.avator || "";
                } else {
                    this.adminForm.name = name;
                }
            } catch (e) {
                this.adminForm.name = name;
            }
        },
        async saveAdmin() {
            const payload = {
                name: this.adminForm.name
            };
            if (this.adminForm.password) {
                payload.password = this.adminForm.password;
            }
            try {
                const res = await updateAdminInfo(payload);
                if (res && res.code === 1) {
                    localStorage.setItem("userName", this.adminForm.name);
                    this.adminForm.password = "";
                    this.notify("淇濆瓨鎴愬姛", "success");
                    await this.loadAdmin();
                } else {
                    this.notify(res && res.msg ? res.msg : "淇濆瓨澶辫触", "error");
                }
            } catch (e) {
                this.notify("淇濆瓨澶辫触", "error");
            }
        },
        beforeAvatarUpload(file) {
            const isJpgOrPng =
                file.type === "image/jpeg" || file.type === "image/png";
            if (!isJpgOrPng) {
                this.$message.error("澶村儚鍙兘鏄?JPG 鎴?PNG 鏍煎紡");
                return false;
            }
            const isLt2M = file.size / 1024 / 1024 < 2;
            if (!isLt2M) {
                this.$message.error("澶村儚澶у皬涓嶈兘瓒呰繃 2MB");
                return false;
            }
            return true;
        },
        handleAvatarSuccess(res) {
            if (res && res.code === 1) {
                if (res.data && res.data.pic) this.adminForm.avator = res.data.pic;
                if (res.pic) this.adminForm.avator = res.pic;
                if (res.avator) this.adminForm.avator = res.avator;
                if (this.adminForm.avator) {
                    localStorage.setItem("adminAvatar", this.adminForm.avator);
                }
                this.notify("澶村儚涓婁紶鎴愬姛", "success");
                bus.$emit("admin-avatar-updated", this.adminForm.avator);
            } else {
                this.notify("澶村儚涓婁紶澶辫触", "error");
            }
        }
    }
};
</script>

<style scoped>
.admin-info-page {
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

.admin-card {
    min-height: 360px;
}

.admin-form {
    margin-top: 8px;
}

.avatar-uploader {
    display: inline-block;
}

.avatar {
    width: 96px;
    height: 96px;
    border-radius: 16px;
    object-fit: cover;
    border: 1px solid #e2e8f0;
}

.avatar-placeholder {
    width: 96px;
    height: 96px;
    border-radius: 16px;
    border: 1px dashed #cbd5f5;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #64748b;
    background: #f8fafc;
    cursor: pointer;
}

.tips-card {
    min-height: 360px;
}

.tips-list {
    list-style: none;
    padding-left: 0;
    color: #475569;
}

.tips-list li {
    padding: 8px 0;
    border-bottom: 1px dashed #e2e8f0;
}
</style>
