import axiosAuth from './axiosAuth'

const BASE_URL = 'http://localhost:8083/api/reviews'

export const getReviewsByBook = (bookId) => axiosAuth.get(`${BASE_URL}/book/${bookId}`)
export const getAverageRating = (bookId) => axiosAuth.get(`${BASE_URL}/book/${bookId}/rating`)
export const createReview = (review) => axiosAuth.post(BASE_URL, review)