import { Link } from 'react-router-dom'

function Navbar() {
  return (
    <nav>
      <Link to="/">Inicio</Link>
      {' | '}
      <Link to="/books">Libros</Link>
    </nav>
  )
}

export default Navbar