import axios from 'axios'

const BASE_URL = 'http://localhost:8081/api/books'

export const getBooks = () => axios.get(BASE_URL)

export const getBookById = (id) => axios.get(`${BASE_URL}/${id}`)

export const advancedSearch = (params) => axios.get(`${BASE_URL}/advanced-search`, { params })