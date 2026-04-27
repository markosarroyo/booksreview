import axios from 'axios'

const BASE_URL = 'http://localhost:8081/auth'

export const authService = {
  async login(username, password) {
    const { data } = await axios.post(`${BASE_URL}/login`, { username, password })
    return data // { token }
  },

  async register(username, email, password) {
    const { data } = await axios.post(`${BASE_URL}/register`, { username, email, password })
    return data // { token }
  },

  saveToken(token) {
    localStorage.setItem('jwt_token', token)
  },

  getToken() {
    return localStorage.getItem('jwt_token')
  },

  removeToken() {
    localStorage.removeItem('jwt_token')
  },

  decodeToken(token) {
    try {
      return JSON.parse(atob(token.split('.')[1]))
    } catch {
      return null
    }
  },

  isTokenValid(token) {
    if (!token) return false
    const payload = this.decodeToken(token)
    return payload ? payload.exp * 1000 > Date.now() : false
  },
}
