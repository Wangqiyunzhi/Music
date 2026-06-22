import { get, post } from "./http";
// 鍒ゆ柇绠＄悊鍛樻槸鍚︾櫥褰曟垚鍔?
export const getLoginStatus = (params) => post(`admin/login/status`,params);


//============姝屾墜鐩稿叧================
//鏌ヨ姝屾墜
export const getAllSinger =() => get(`singer/allSinger`);
//娣诲姞姝屾墜
export const setSinger = (params) => post(`singer/add`,params);
//缂栬緫姝屾墜
export const updateSinger = (params) => post(`singer/update`,params);
//鍒犻櫎姝屾墜
export const delSinger = (id) => get(`singer/delete?id=${id}`);

//============姝屾洸鐩稿叧================
//缂栬緫姝屾洸
export const updateSong = (params) => post(`song/update`,params);
//鍒犻櫎姝屾洸
export const delSong = (id) => get(`song/delete?id=${id}`);
//鏍规嵁姝屾洸id鏌ヨ姝屾洸瀵硅薄
export const songOfSongId = (songId) => {
  const extractStatus = (err) => {
    if (!err) return null;
    if (err.response && err.response.status) return err.response.status;
    if (err.status) return err.status;
    if (err.data && err.data.status) return err.data.status;
    return null;
  };

  const tryCalls = [
    // 鐜版湁椤圭洰甯歌鍐欐硶
    () => get("song/detail", { songId }),
    () => get("song/detail", { id: songId }),
    () => get("song/detail", { song_id: songId }),

    // 涔熸湁浜烘彁渚?/song/songId 涔嬬被
    () => get("song/songId", { songId }),
    () => get("song/songId", { id: songId }),

    // 杩樻湁鎺ュ彛鍚嶄笉鍚岀殑鎯呭喌
    () => get("song/getById", { id: songId }),
  ];

  let p = Promise.reject({ response: { status: 404 } });
  for (const fn of tryCalls) {
    p = p.catch((err) => {
      const status = extractStatus(err);
      if (status && status !== 404) throw err;
      return fn();
    });
  }
  return p;
};
//鏍规嵁姝屾洸鍚嶈幏鍙栨瓕鏇插璞?
export const songOfSongName =(songName) => get(`song/songOfSongName?songName=${songName}`);
export const songOfName = (songName) => get(`song/songOfSongName?songName=${songName}`);
//鏌ヨ鎵€鏈夋瓕鏇?
export const allSong =() => get(`song/allSong`);

//============姝屽崟鐩稿叧================
//鏌ヨ姝屽崟
export const getAllSongList = () => {
  const extractStatus = (err) => {
    if (!err) return null;
    if (err.response && err.response.status) return err.response.status;
    if (err.status) return err.status;
    if (err.data && err.data.status) return err.data.status;
    return null;
  };

  const tryGet = (path) =>
    get(path)
      .then((res) => {
        if (typeof window !== "undefined") {
          window.__songListPath__ = path;
          window.__songListRaw__ = res;
        }
        if (typeof console !== "undefined" && console.info) {
          console.info("getAllSongList success:", path, res);
        }
        return res;
      })
      .catch((err) => {
        if (typeof console !== "undefined" && console.warn) {
          console.warn("getAllSongList failed:", path, err);
        }
        throw err;
      });

  const tryCalls = [
    () => tryGet("songList/allSongList"),
    () => tryGet("songList/all"),
    () => tryGet("songList/list"),
    () => tryGet("songlist/list"),
    () => tryGet("songList"),
    () => tryGet("song/list"),
    () => tryGet("/songList/allSongList"),
    () => tryGet("/songList/all"),
    () => tryGet("/songList/list"),
    () => tryGet("/songList"),
    () => tryGet("/song/list"),
    () => tryGet("admin/songList/allSongList"),
    () => tryGet("admin/songList/all"),
    () => tryGet("admin/songList/list"),
    () => tryGet("admin/songList"),
    () => tryGet("/admin/songList/allSongList"),
    () => tryGet("/admin/songList/all"),
    () => tryGet("/admin/songList/list"),
    () => tryGet("/admin/songList"),
    () => tryGet("music/songList/allSongList"),
    () => tryGet("music/songList/list"),
    () => tryGet("music/songList/all"),
    () => tryGet("/music/songList/allSongList"),
    () => tryGet("/music/songList/list"),
    () => tryGet("/music/songList/all"),
    () => tryGet("api/songList/allSongList"),
    () => tryGet("api/songList/list"),
    () => tryGet("api/songList/all"),
    () => tryGet("/api/songList/allSongList"),
    () => tryGet("/api/songList/list"),
    () => tryGet("/api/songList/all"),
  ];

  let p = Promise.reject({ response: { status: 404 } });
  for (const fn of tryCalls) {
    p = p.catch((err) => {
      const status = extractStatus(err);
      if (status && status !== 404) throw err;
      return fn();
    });
  }
  return p.catch((err) => {
    if (typeof console !== "undefined" && console.warn) {
      console.warn("getAllSongList: all endpoints failed", err);
    }
    throw err;
  });
};
//娣诲姞姝屽崟
export const setSongList = (params) => post(`songList/add`,params);
//缂栬緫姝屽崟
export const updateSongList = (params) => post(`songList/update`,params);
//鍒犻櫎姝屽崟
export const delSongList = (id) => get(`songList/delete?id=${id}`);

//============?????================
export const getAllBanner = () => get(`banner/all`);
export const setBanner = (params) => post(`banner/add`, params);
export const updateBanner = (params) => post(`banner/update`, params);
export const delBanner = (id) => get(`banner/delete?id=${id}`);
export const updateBannerStatus = (params) => post(`banner/status`, params);

//============管理员信息================
export const getAdminInfo = async (name) => {
  const params = name ? { name } : {};
  try {
    const res = await get(`admin/login/info`, params);
    if (res && res.code === 1) {
      // login/info 只返回账号名，继续拉完整信息
      const adminName = typeof res.data === "string" ? res.data : name;
      return get(`admin/info`, adminName ? { name: adminName } : {});
    }
    return res;
  } catch (err) {
    const status =
      (err && err.response && err.response.status) ||
      err.status ||
      (err.data && err.data.status);
    if (status && status !== 401) throw err;
    return get(`admin/info`, params);
  }
};
export const updateAdminInfo = (params) => {
  const currentName = localStorage.getItem("userName");
  const payload = { ...params };
  if (currentName && !payload.currentName) {
    payload.currentName = currentName;
  }
  return post(`admin/update`, payload);
};

//============公告管理================
export const getAllNotice = () => get(`notice/all`);
export const setNotice = (params) => post(`notice/add`, params);
export const updateNotice = (params) => post(`notice/update`, params);
export const delNotice = (id) => get(`notice/delete?id=${id}`);
export const updateNoticeStatus = (params) => post(`notice/status`, params);

//============缁熻鎶ヨ〃================
export const getHotSongs = (limit = 10) =>
  get(`stats/hot/songs`, { limit });
export const getHotSingers = (limit = 10) =>
  get(`stats/hot/singers`, { limit });
export const getPlayRank = (limit = 10) =>
  get(`stats/rank/play`, { limit });
export const getCollectRank = (limit = 10) =>
  get(`stats/rank/collect`, { limit });
export const getDailyNewUsers = (days = 14) =>
  get(`stats/user/daily`, { days });


//============姝屽崟鐨勬瓕鏇茬浉鍏?===========
//鏍规嵁姝屽崟id鏌ヨ姝屾洸鍒楄〃
export const listSongDetail = (songListId) => get(`listSong/detail?songListId=${songListId}`);
//缁欐瓕鍗曞鍔犳瓕鏇?
export const listSongAdd = (params) => post(`listSong/add`,params);
//鍒犻櫎姝屽崟鐨勬瓕鏇?
export const delListSong = (songId,songListId) => get(`listSong/delete?songId=${songId}&songListId=${songListId}`);

//============鐢ㄦ埛鐩稿叧================
//鏌ヨ鐢ㄦ埛
export const getAllConsumer =() => get(`consumer/allConsumer`);
//娣诲姞鐢ㄦ埛
export const setConsumer = (params) => post(`consumer/add`,params);
//缂栬緫鐢ㄦ埛
export const updateConsumer = (params) => post(`consumer/update`,params);
//鍒犻櫎鐢ㄦ埛
export const delConsumer = (id) => get(`consumer/delete?id=${id}`);
//鏍规嵁鐢ㄦ埛id鏌ヨ璇ョ敤鎴风殑璇︾粏淇℃伅
export const getUserOfId =(id) => get(`/consumer/selectByPrimaryKey?id=${id}`);

//===============鏀惰棌===================
//鎸囧畾鐢ㄦ埛鐨勬敹钘忓垪琛?
export const getCollectOfUserId = (userId) => get(`/collect/collectOfUserId?userId=${userId}`);
//鍒犻櫎鐢ㄦ埛鏀惰棌鐨勬瓕鏇?
export const deleteCollection = (userId,songId) => post(`collect/add`, { userId, songId, type: 0 });

//===============璇勮===================
//鎸囧畾姝屽崟鐨勮瘎璁哄垪琛?
export const getCommentOfSongListId = (songListId) => get(`/comment/commentOfSongListId?songListId=${songListId}`);
//鍒犻櫎璇勮
export const deleteComment = (params) => post(`comment/delete`, params);
export const getCommentPage = (params) => get(`comment/page`, params);
export const songOfSingerId = (singerId) => {
  const extractStatus = (err) => {
    if (!err) return null;
    if (err.response && err.response.status) return err.response.status;
    if (err.status) return err.status;
    if (err.data && err.data.status) return err.data.status;
    return null;
  };
  const tryCalls = [
    // 浣犵幇鍦ㄥ墠绔啓鐨勮繖涓紙鍏堣瘯锛?
    () => get("song/singer/detail", { singerId }),
    // 鏈変簺鍚庣鍙傛暟鍚嶅彨 id
    () => get("song/singer/detail", { id: singerId }),

    // 寰堝椤圭洰鏄繖鏍峰啓鐨勶細/song/detail?singerId=xx
    () => get("song/detail", { singerId }),
    () => get("song/detail", { id: singerId }),

    // 涔熸湁浜哄啓鎴?/song/singer?singerId=xx
    () => get("song/singer", { singerId }),
    () => get("song/singer", { id: singerId }),
  ];

  // 閫愪釜灏濊瘯锛氶亣鍒?404 鎵嶆崲涓嬩竴涓紱閬囧埌鍒殑閿欒鐩存帴鎶涘嚭
  let p = Promise.reject({ response: { status: 404 } });

  for (const fn of tryCalls) {
    p = p.catch((err) => {
      const status = extractStatus(err);
      if (status && status !== 404) throw err;
      return fn();
    });
  }
  return p;
};






