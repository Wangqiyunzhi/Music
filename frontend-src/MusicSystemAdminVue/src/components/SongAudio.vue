<template>
    <div class="song-audio">
        <audio id="player" 
            :src="url" 
            controls = "controls" 
            preload = "true"
            @canplay="startPlay"
            @ended="ended"
        ></audio>
    </div>
</template>
<script>
import {mapGetters} from 'vuex';
export default {
    name: 'song-audio',
    computed: {
        ...mapGetters([
            'id',
            'url',
            'isPlay'
        ])
    },
    watch:{
        //鐩戝惉鎾斁杩樻槸鏆傚仠
        isPlay: function(){
            this.togglePlay();
        }
    },
    methods:{
        //鑾峰彇閾炬帴鍚庡噯澶囨挱鏀?
        startPlay(){
            let player = document.querySelector('#player');
            //寮€濮嬫挱鏀?
            player.play();
        },
        //鎾斁瀹屾垚涔嬪悗瑙﹀彂
        ended(){
           this.isPlay = false
        },
        //寮€濮嬨€佹殏鍋?
        togglePlay() {
            let player = document.querySelector('#player');
            if(this.isPlay){
                player.play();
            }else{
                player.pause();
            }
        }
    }
}
</script>

<style>
    .song-audio {
        display: none;
    }
</style>