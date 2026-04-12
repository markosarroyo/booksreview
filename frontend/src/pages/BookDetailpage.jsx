import { useState, useEffect } from 'react'
import { useParams } from 'react-router-dom'
import { getBookById } from '../services/BookService'
import { getReviewsByBook, getAverageRating } from '../services/ReviewService'

function BookDetailPage() {
  const { id } = useParams()
  const [book, setBook] = useState(null)
  const [reviews, setReviews] = useState([])
  const [rating, setRating] = useState(0)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    Promise.all([
      getBookById(id),
      getReviewsByBook(id),
      getAverageRating(id)
    ])
      .then(([bookRes, reviewsRes, ratingRes]) => {
        setBook(bookRes.data)
        setReviews(reviewsRes.data)
        setRating(ratingRes.data)
        setLoading(false)
      })
      .catch(error => {
        console.error('Error:', error)
        setLoading(false)
      })
  }, [id])

  if (loading) return <p>Cargando...</p>
  if (!book) return <p>Libro no encontrado</p>

  return (
    <div>
      <h1>{book.title}</h1>
      <p>{book.summary}</p>
      <p>Género: {book.genre}</p>
      <p>Rating medio: {rating.toFixed(1)} / 5</p>

      <h2>Reseñas ({reviews.length})</h2>
      {reviews.length === 0 ? (
        <p>No hay reseñas todavía</p>
      ) : (
        reviews.map(review => (
          <div key={review.id}>
            <p>⭐ {review.rating}/5</p>
            <p>{review.comment}</p>
            <small>{new Date(review.createdAt).toLocaleDateString()}</small>
          </div>
        ))
      )}
    </div>
  )
}

export default BookDetailPage