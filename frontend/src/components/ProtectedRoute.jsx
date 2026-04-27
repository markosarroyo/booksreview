import { Navigate, Outlet, useLocation } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

// Protege rutas que requieren login
export function ProtectedRoute({ requiredRoles = [] }) {
  const { isAuthenticated, user, loading } = useAuth()
  const location = useLocation()

  if (loading) {
    return (
      <div className="auth-loading">
        <div className="auth-spinner" />
      </div>
    )
  }

  if (!isAuthenticated) {
    return <Navigate to="/login" state={{ from: location }} replace />
  }

  if (requiredRoles.length > 0 && !requiredRoles.some(r => user.roles.includes(r))) {
    return <Navigate to="/" replace />
  }

  return <Outlet />
}

// Redirige a home si ya estás logado (para /login y /register)
export function PublicRoute() {
  const { isAuthenticated, loading } = useAuth()
  if (loading) return null
  if (isAuthenticated) return <Navigate to="/" replace />
  return <Outlet />
}
