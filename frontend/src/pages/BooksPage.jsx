import { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'
import { getBooks } from '../services/BookService'

function BooksPage() {
  const [books, setBooks] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    getBooks()
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