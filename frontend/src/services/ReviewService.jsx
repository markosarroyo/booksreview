import axios from "axios"

const BASE_RUL = 'http://localhost:8082/api/reviews'

export const getReviewsByBook = (bookId) => axios.get(`${BASE_URL}/book/${bookId}`)
export const getAverageRating = (bookId) => axios.get(`${BASE_URL}/book/${bookId}/rating`)
export const createReview = (review) => axios.post(BASE_URL, review)