const song = {
  state: {
    listOfSongs: [],     // 当前歌曲列表
    isPlay: false,       // 是否播放中
    url: '',             // 歌曲地址
    id: '',              // 歌曲id
    playButtonUrl: '#icon-bofang', // 播放状态图标
    duration: 0,         // 音乐时长
    curTime: 0,          // 当前音乐播放位置
    changeTime: 0,       // 指定播放时刻
    title: '',           // 歌名
    artist: '',          // 歌手名
    picUrl: '',          // 歌曲图片
    autoNext: true,      // 自动触发下一首
    lyric: [],           // 处理后的歌词
    tempList: {},        // 单个歌单/歌手信息
    listIndex: 0,        // 当前歌曲在列表的位置（建议用 number）
    volume: 50,          // 音量

    // ✅ 新增：播放模式 order / one / shuffle
    playMode: 'order',

    // ✅ 新增：最近播放（存 song 对象数组，最多 30 条）
    recentPlays: []
  },

  getters: {
    listOfSongs: (state) => {
      let v = state.listOfSongs;
      if (!v.length) v = JSON.parse(window.sessionStorage.getItem('listOfSongs') || '[]');
      return v || [];
    },
    isPlay: (state) => {
      let v = state.isPlay;
      if (!v) v = JSON.parse(window.sessionStorage.getItem('isPlay') || 'false');
      return !!v;
    },
    url: (state) => {
      let v = state.url;
      if (!v) v = JSON.parse(window.sessionStorage.getItem('url') || '""');
      return v || '';
    },
    id: (state) => {
      let v = state.id;
      if (!v) v = JSON.parse(window.sessionStorage.getItem('id') || '""');
      return v || '';
    },
    playButtonUrl: (state) => {
      let v = state.playButtonUrl;
      if (!v) v = JSON.parse(window.sessionStorage.getItem('playButtonUrl') || '"#icon-bofang"');
      return v || '#icon-bofang';
    },
    duration: (state) => {
      let v = state.duration;
      if (!v) v = JSON.parse(window.sessionStorage.getItem('duration') || '0');
      return Number(v) || 0;
    },
    curTime: (state) => {
      let v = state.curTime;
      if (!v) v = JSON.parse(window.sessionStorage.getItem('curTime') || '0');
      return Number(v) || 0;
    },
    changeTime: (state) => {
      let v = state.changeTime;
      if (!v) v = JSON.parse(window.sessionStorage.getItem('changeTime') || '0');
      return Number(v) || 0;
    },
    title: (state) => {
      let v = state.title;
      if (!v) v = JSON.parse(window.sessionStorage.getItem('title') || '""');
      return v || '';
    },
    artist: (state) => {
      let v = state.artist;
      if (!v) v = JSON.parse(window.sessionStorage.getItem('artist') || '""');
      return v || '';
    },
    picUrl: (state) => {
      let v = state.picUrl;
      if (!v) v = JSON.parse(window.sessionStorage.getItem('picUrl') || '""');
      return v || '';
    },
    autoNext: (state) => {
      let v = state.autoNext;
      if (!v) v = JSON.parse(window.sessionStorage.getItem('autoNext') || 'true');
      return !!v;
    },
    lyric: (state) => {
      let v = state.lyric;
      if (!v || !v.length) v = JSON.parse(window.sessionStorage.getItem('lyric') || '[]');
      return v || [];
    },
    tempList: (state) => {
      let v = state.tempList;
      if (!v || !Object.keys(v).length) v = JSON.parse(window.sessionStorage.getItem('tempList') || '{}');
      return v || {};
    },
    listIndex: (state) => {
      let v = state.listIndex;
      if (v === null || v === undefined) v = JSON.parse(window.sessionStorage.getItem('listIndex') || '0');
      return Number(v) || 0;
    },
    volume: (state) => {
      let v = state.volume;
      if (!v) v = JSON.parse(window.sessionStorage.getItem('volume') || '50');
      return Number(v) || 50;
    },

    // ✅ 新增 getter
    playMode: (state) => {
      let v = state.playMode;
      if (!v) v = JSON.parse(window.sessionStorage.getItem('playMode') || '"order"');
      return v || 'order';
    },
    recentPlays: (state) => {
      let v = state.recentPlays;
      if (!v || !v.length) v = JSON.parse(window.sessionStorage.getItem('recentPlays') || '[]');
      return v || [];
    }
  },

  mutations: {
    setListOfSongs: (state, listOfSongs) => {
      state.listOfSongs = Array.isArray(listOfSongs) ? listOfSongs : [];
      window.sessionStorage.setItem('listOfSongs', JSON.stringify(state.listOfSongs));
    },
    setIsPlay: (state, isPlay) => {
      state.isPlay = !!isPlay;
      window.sessionStorage.setItem('isPlay', JSON.stringify(state.isPlay));
    },
    setUrl: (state, url) => {
      state.url = url || '';
      window.sessionStorage.setItem('url', JSON.stringify(state.url));
    },
    setId: (state, id) => {
      state.id = id || '';
      window.sessionStorage.setItem('id', JSON.stringify(state.id));
    },
    setPlayButtonUrl: (state, playButtonUrl) => {
      state.playButtonUrl = playButtonUrl || '#icon-bofang';
      window.sessionStorage.setItem('playButtonUrl', JSON.stringify(state.playButtonUrl));
    },
    setDuration: (state, duration) => {
      state.duration = Number(duration) || 0;
      window.sessionStorage.setItem('duration', JSON.stringify(state.duration));
    },
    setCurTime: (state, curTime) => {
      state.curTime = Number(curTime) || 0;
      window.sessionStorage.setItem('curTime', JSON.stringify(state.curTime));
    },
    setChangeTime: (state, changeTime) => {
      state.changeTime = Number(changeTime) || 0;
      window.sessionStorage.setItem('changeTime', JSON.stringify(state.changeTime));
    },
    setTitle: (state, title) => {
      state.title = title || '';
      window.sessionStorage.setItem('title', JSON.stringify(state.title));
    },
    setArtist: (state, artist) => {
      state.artist = artist || '';
      window.sessionStorage.setItem('artist', JSON.stringify(state.artist));
    },
    setPicUrl: (state, picUrl) => {
      state.picUrl = picUrl || '';
      window.sessionStorage.setItem('picUrl', JSON.stringify(state.picUrl));
    },
    setAutoNext: (state, autoNext) => {
      state.autoNext = !!autoNext;
      window.sessionStorage.setItem('autoNext', JSON.stringify(state.autoNext));
    },
    setLyric: (state, lyric) => {
      state.lyric = Array.isArray(lyric) ? lyric : [];
      window.sessionStorage.setItem('lyric', JSON.stringify(state.lyric));
    },
    setTempList: (state, tempList) => {
      state.tempList = tempList || {};
      window.sessionStorage.setItem('tempList', JSON.stringify(state.tempList));
    },
    setListIndex: (state, listIndex) => {
      state.listIndex = Number(listIndex) || 0;
      window.sessionStorage.setItem('listIndex', JSON.stringify(state.listIndex));
    },
    setVolume: (state, volume) => {
      state.volume = volume;
      window.sessionStorage.setItem('volume', JSON.stringify(volume));
    },

    // ✅ 新增：播放模式
    setPlayMode: (state, mode) => {
      const m = (mode === 'one' || mode === 'shuffle') ? mode : 'order';
      state.playMode = m;
      window.sessionStorage.setItem('playMode', JSON.stringify(state.playMode));
    },

    // ✅ 新增：最近播放（去重：按 song.id；最多 30 条）
    pushRecent: (state, song) => {
      if (!song || !song.id) return;

      const old = Array.isArray(state.recentPlays) ? state.recentPlays : [];
      const next = [song, ...old.filter(s => String(s.id) !== String(song.id))].slice(0, 30);

      state.recentPlays = next;
      window.sessionStorage.setItem('recentPlays', JSON.stringify(state.recentPlays));
    },

    clearRecent: (state) => {
      state.recentPlays = [];
      window.sessionStorage.setItem('recentPlays', JSON.stringify([]));
    },

    // ✅ 新增：清空播放队列
    clearQueue: (state) => {
      state.listOfSongs = [];
      state.listIndex = 0;
      state.id = '';
      state.url = '';
      state.title = '';
      state.artist = '';
      state.picUrl = '';
      state.curTime = 0;
      state.changeTime = 0;
      state.isPlay = false;

      window.sessionStorage.setItem('listOfSongs', JSON.stringify([]));
      window.sessionStorage.setItem('listIndex', JSON.stringify(0));
      window.sessionStorage.setItem('id', JSON.stringify(''));
      window.sessionStorage.setItem('url', JSON.stringify(''));
      window.sessionStorage.setItem('title', JSON.stringify(''));
      window.sessionStorage.setItem('artist', JSON.stringify(''));
      window.sessionStorage.setItem('picUrl', JSON.stringify(''));
      window.sessionStorage.setItem('curTime', JSON.stringify(0));
      window.sessionStorage.setItem('changeTime', JSON.stringify(0));
      window.sessionStorage.setItem('isPlay', JSON.stringify(false));
    },
    clearRecentPlays: (state) => {
  state.recentPlays = []
  ssSet('recentPlays', state.recentPlays)
},

  }
};

export default song;
