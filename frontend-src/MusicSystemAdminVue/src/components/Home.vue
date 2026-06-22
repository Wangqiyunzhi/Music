<template>
    <div>
        <the-header></the-header>
        <the-aside></the-aside>
        <div class="content-box" :class="{'content-collapse':collapse}">
            <router-view></router-view>
        </div>
    </div>
</template>

<script>
import TheHeader from './TheHeader';
import TheAside from './TheAside';
import bus from "../assets/js/bus";

export default {
    components:{
        TheHeader,
        TheAside
    },
    data(){
        return {
            collapse: false
        }
    },
    created(){
        //閫氳繃Bus杩涜缁勪欢闂寸殑閫氫俊锛屾潵鎶樺彔渚ц竟鏍?
        bus.$on('collapse',msg =>{
            this.collapse = msg
        })
    }
}
</script>