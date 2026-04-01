import { useState, useEffect } from 'react'
import axios from 'axios'

function BooksPage() {
  const [books, setBooks] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    axios.get('http://localhost:8081/api/books')
      .then(response => {
        setBooks(response.data)
        setLoading(false)
      })
      .catch(error => {
        console.error('Error fetching books:', error)
        setLoading(false)
      })
  }, [])

  if (loading) return <p>Cargando libros...</p>

  return (
    <div>
      <h1>Libros</h1>
      {books.length === 0 ? (
        <p>No hay libros todavía</p>
      ) : (
        books.map(book => (
          <div key={book.id}>
            <h2>{book.title}</h2>
            <p>{book.summary}</p>
            <p>Género: {book.genre}</p>
          </div>
        ))
      )}
    </div>
  )
}

export default BooksPage