import { BrowserRouter, Routes, Route } from 'react-router-dom'
import { AuthProvider } from './context/AuthContext'

import { ProtectedRoute, PublicRoute } from './components/ProtectedRoute'
import Navbar from './components/Navbar'
import HomePage from './pages/HomePage'
import BooksPage from './pages/BooksPage'
import BookDetailPage from './pages/BookDetailpage'
import { LoginPage } from './pages/LoginPage'
import { RegisterPage } from './pages/RegisterPage'

function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <Navbar />
        <Routes>

          {/* ── Rutas públicas: si ya estás logado redirigen a "/" ── */}
          <Route element={<PublicRoute />}>
            <Route path="/login"    element={<LoginPage />} />
            <Route path="/register" element={<RegisterPage />} />
          </Route>

          {/* ── Rutas protegidas: requieren JWT válido ── */}
          <Route element={<ProtectedRoute />}>
            <Route path="/"          element={<HomePage />} />
            <Route path="/books"     element={<BooksPage />} />
            <Route path="/books/:id" element={<BookDetailPage />} />
          </Route>

        </Routes>
      </AuthProvider>
    </BrowserRouter>
  )
}

export default App
