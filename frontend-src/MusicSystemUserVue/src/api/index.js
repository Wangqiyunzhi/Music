import Axios from "axios";
import { get, post } from "./http";

function unwrap(res) {
  return res && res.data !== undefined ? res.data : res;
}

/** Song 缂撳瓨锛氬洜涓轰綘鍚庣鍙湁 /song/allSong锛屾病鏈?/song/detail */
var _allSongCache = null;
var _allSongPromise = null;

function fetchAllSong() {
  if (_allSongCache) return Promise.resolve(_allSongCache);
  if (_allSongPromise) return _allSongPromise;

  _allSongPromise = get("/song/allSong")
    .then(function (res) {
      var r = unwrap(res);
      var list = Array.isArray(r) ? r : (r && Array.isArray(r.data) ? r.data : []);
      _allSongCache = list;
      _allSongPromise = null;
      return list;
    })
    .catch(function (e) {
      _allSongPromise = null;
      throw e;
    });

  return _allSongPromise;
}

export function clearSongCache() {
  _allSongCache = null;
  _allSongPromise = null;
}

/* ============ 鐧诲綍/鐢ㄦ埛 ============ */
export const loginIn = (params) => post(`/user/login/status`, params);
export const userLogout = (params) => post(`/user/logout`, params);

/* 涓嬮潰杩欎簺鏄负浜嗏€滀笉浼氱紪璇戠偢鈥濓紝鍗充娇鍚庣鏆傛椂娌″仛涔熶笉浼氬奖鍝嶈繍琛?*/
export const SignUp = (params) => post(`/consumer/add`, params);
export const getUserOfId = (id) => get(`/consumer/selectByPrimaryKey?id=${id}`);
export const updateUserMsg = (params) => post(`/consumer/update`, params);
export const resetUserPassword = (params) => post(`/consumer/resetPassword`, params);

/* ============ 姝屾墜 ============ */
export const getAllSinger = () => get(`/singer/allSinger`);
export const getSingerOfSex = (sex) => get(`/singer/singerOfSex?sex=${sex}`);

/* ============ 姝屾洸 ============ */
export const songOfSongId = (id) =>
  fetchAllSong().then(function (list) {
    var sid = String(id);
    for (var i = 0; i < list.length; i++) {
      if (String(list[i].id) === sid) return list[i];
    }
    return null;
  });

export const songOfSingerId = (singerId) =>
  get(`/song/singer/detail?singerId=${singerId}`)
    .then(function (res) { return unwrap(res); })
    .catch(function () {
      return fetchAllSong().then(function (list) {
        var sid = String(singerId);
        return list.filter(function (s) { return String(s.singerId) === sid; });
      });
    });

export const likeSongOfName = (keywords) => get(`/song/likeSongOfName?songName=${keywords}`);

/* ============ 姝屽崟 ============ */
export const getAllSongList = () => get(`/songList/allSongList`);
export const getSongListOfLikeTitle = (keywords) => get(`/songList/likeTitle?title=${keywords}`);
export const getSongListOfLikeStyle = (style) => get(`/songList/likeStyle?style=${style}`);
export const songListOfSongListId = (songListId) => get(`/songList/selectByPrimaryKey?id=${songListId}`);

/* ============ 杞挱鍥?============ */
export const getBannerHome = () => get(`/banner/home`);

/* ============ 公告 ============ */
export const getNoticeList = () => get(`/notice/publish`);

/* ============ 姝屽崟-姝屾洸 ============ */
export const listSongDetail = (songListId) => get(`/listSong/detail?songListId=${songListId}`);

/* ============ 璇勮 ============ */
export const setComment = (params) => post(`/comment/add`, params);
export const deleteComment = (params) => post(`/comment/delete`, params);

export const getCommentPage = (type, id, pageNo, pageSize) => {
  if (Number(type) === 0) {
    return get(`/comment/page?type=0&songId=${id}&pageNo=${pageNo}&pageSize=${pageSize}`);
  }
  return get(`/comment/page?type=1&songListId=${id}&pageNo=${pageNo}&pageSize=${pageSize}`);
};

/* ============ 鏀惰棌 ============ */
export const setCollect = (params) => post(`/collect/add`, params);
export const getCollectOfUserId = (userId) => get(`/collect/collectOfUserId?userId=${userId}`);
export const getCollectOfUserIdAndType = (userId, type) =>
  get(`/collect/collectOfUserIdAndType?userId=${userId}&type=${type}`);

/* ============ 播放历史 ============ */
export const getPlayHistory = (userId, limit = 50) =>
  get(`/history/user?userId=${userId}&limit=${limit}`);
export const addPlayHistory = (params) => post(`/history/add`, params);
export const clearPlayHistory = (userId) => get(`/history/clear?userId=${userId}`);

/* ============ 璇勫垎锛堝搴斾綘 RankController锛?============ */
export const setRank = (params) => post(`/rank/add`, params);
export const getRankOfSongListId = (songListId) => get(`/rank?songListId=${songListId}`);

/* ============ 涓嬭浇 ============ */
export const download = (url) =>
  Axios({
    method: "get",
    url: url,
    responseType: "blob"
  });



