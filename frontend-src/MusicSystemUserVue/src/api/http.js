// ./http.js 内容应该类似这样
import axios from 'axios'

// 创建axios实例
const service = axios.create({
  baseURL: 'http://localhost:9171', // 这是您当前的baseURL
  timeout: 15000 // 请求超时时间
})

// 请求拦截器
service.interceptors.request.use(
  config => {
    // 在这里可以添加token等
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  response => {
    return response.data
  },
  error => {
    return Promise.reject(error)
  }
)

export const get = (url, params) => {
  return service.get(url, { params })
}

export const post = (url, data) => {
  return service.post(url, data)
}