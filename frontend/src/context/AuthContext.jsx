import { createContext, useContext, useState, useEffect, useCallback } from 'react'
import { authService } from '../services/authService'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null)
  const [token, setToken] = useState(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const stored = authService.getToken()
    if (stored && authService.isTokenValid(stored)) {
      const payload = authService.decodeToken(stored)
      setToken(stored)
      setUser({ username: payload.sub, roles: payload.roles ?? [] })
    } else {
      authService.removeToken()
    }
    setLoading(false)
  }, [])

  const login = useCallback(async (username, password) => {
    const { token } = await authService.login(username, password)
    authService.saveToken(token)
    const payload = authService.decodeToken(token)
    setToken(token)
    setUser({ username: payload.sub, roles: payload.roles ?? [] })
  }, [])

  const register = useCallback(async (username, email, password) => {
    const { token } = await authService.register(username, email, password)
    authService.saveToken(token)
    const payload = authService.decodeToken(token)
    setToken(token)
    setUser({ username: payload.sub, roles: payload.roles ?? [] })
  }, [])

  const logout = useCallback(() => {
    authService.removeToken()
    setToken(null)
    setUser(null)
  }, [])

  return (
    <AuthContext.Provider value={{ user, token, loading, login, register, logout, isAuthenticated: !!user }}>
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth() {
  const ctx = useContext(AuthContext)
  if (!ctx) throw new Error('useAuth debe usarse dentro de <AuthProvider>')
  return ctx
}
