export const mixin = {
    methods: {
        // 提示信息
        notify(title, type) {
            this.$notify({
                title,
                type
            });
        },
        // 根据相对地址获取绝对地址
        getUrl(url) {
            if (!url) return "";
            if (typeof url !== "string") return url;
            if (url.startsWith("http://") || url.startsWith("https://") || url.startsWith("//")) {
                return url;
            }
            if (url.startsWith("/")) {
                return `${this.$store.state.HOST}${url}`;
            }
            return `${this.$store.state.HOST}/${url}`;
        },
        // 获取性别中文
        changeSex(value) {
            if (value == 0) return "女";
            if (value == 1) return "男";
            if (value == 2) return "组合";
            if (value == 3) return "不明";
            return value;
        },
        // 获取生日
        attachBirth(val) {
            return String(val).substr(0, 10);
        },
        // 上传图片之前的校验
        beforeAvatorUpload(file) {
            const isJPG = file.type === "image/jpeg" || file.type === "image/png";
            if (!isJPG) {
                this.$message.error("上传头像图片只能是 jpg 或 png 格式");
                return false;
            }
            const isLt2M = file.size / 1024 / 1024 < 2;
            if (!isLt2M) {
                this.$message.error("上传头像图片大小不能超过 2MB");
                return false;
            }
            return true;
        },
        // 上传图片成功之后
        handleAvatorSuccess(res) {
            if (res.code == 1) {
                this.getData();
                this.$notify({
                    title: "上传成功",
                    type: "success"
                });
            } else {
                this.$notify({
                    title: "上传失败",
                    type: "error"
                });
            }
        },
        // 弹出删除窗口
        handleDelete(id) {
            this.idx = id;
            this.delVisible = true;
        },
        // 赋值已选择项
        handleSelectionChange(val) {
            this.multipleSelection = val;
        },
        // 批量删除已选择项
        delAll() {
            for (let item of this.multipleSelection) {
                this.handleDelete(item.id);
                this.deleteRow();
            }
            this.multipleSelection = [];
        }
    }
};
