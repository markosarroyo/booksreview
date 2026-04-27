import axiosAuth from './axiosAuth'

const BASE_URL = 'http://localhost:8082/api/books'

export const getBooks = () => axiosAuth.get(BASE_URL)

export const getBookById = (id) => axiosAuth.get(`${BASE_URL}/${id}`)

export const advancedSearch = (params) => axiosAuth.get(`${BASE_URL}/advanced-search`, { params })
