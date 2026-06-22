import axios from "axios";
import store from "@/store";

axios.defaults.timeout = 1000000;
axios.defaults.withCredentials = true;

export function post(url, params) {
  return new Promise((resolve, reject) => {
    axios({
      method: "post",
      url,
      data: params,
      transformRequest: [
        function(data) {
          let ret = "";
          for (let it in data) {
            ret +=
              encodeURIComponent(it) + "=" + encodeURIComponent(data[it]) + "&";
          }
          return ret;
        }
      ],
      headers: {
        "Content-Type": "application/x-www-form-urlencoded"
      },
      baseURL: store.state.HOST
    })
      .then((response) => {
        resolve(response.data);
      })
      .catch((error) => {
        reject(error);
      });
  });
}

export function get(url, params) {
  return new Promise((resolve, reject) => {
    axios({
      method: "get",
      url,
      params,
      headers: {
        "Content-Type": "application/x-www-form-urlencoded"
      },
      baseURL: store.state.HOST
    })
      .then((response) => {
        resolve(response.data);
      })
      .catch((error) => {
        reject(error);
      });
  });
}

export function deletes(url, params) {
  return new Promise((resolve, reject) => {
    axios({
      method: "delete",
      url,
      params,
      headers: {
        "Content-Type": "application/x-www-form-urlencoded"
      },
      baseURL: store.state.HOST
    })
      .then((response) => {
        resolve(response.data);
      })
      .catch((error) => {
        reject(error);
      });
  });
}
