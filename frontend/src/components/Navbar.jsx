import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

function Navbar() {
  const { user, logout } = useAuth()
  const navigate = useNavigate()

  const handleLogout = () => {
    logout()
    navigate('/login', { replace: true })
  }

  return (
    <nav>
      <Link to="/">Inicio</Link>
      {' | '}
      <Link to="/books">Libros</Link>
      {user && (
        <>
          {' | '}
          <span>Hola, {user.username}</span>
          {' | '}
          <button onClick={handleLogout}>Cerrar sesión</button>
        </>
      )}
    </nav>
  )
}

export default Navbar