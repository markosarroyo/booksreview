import axios from "axios"

const BASE_RUL = 'http://localhost:8080/api/books'

export const getBooks = () => axios.get(BASE_URL)
export const getBookById = (id) => axios.get(`${BASE_URL}/{$id}`)
export const advancedSearch = (params) => axios.get(`${BASE_URL}/advanced-search`,{params})
