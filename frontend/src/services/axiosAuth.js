import axios from 'axios'
import { authService } from './authService'

/**
 * Instancia de axios con el token JWT inyectado automáticamente.
 * Úsala en BookService y ReviewService en lugar del axios normal.
 *
 * También maneja el 401: si el token expira, limpia la sesión
 * y redirige al login (sin depender del contexto de React).
 */
const axiosAuth = axios.create()

// Request interceptor → añade Authorization header
axiosAuth.interceptors.request.use((config) => {
  const token = authService.getToken()
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

// Response interceptor → si 401, logout y redirige
axiosAuth.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      authService.removeToken()
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

export default axiosAuth
