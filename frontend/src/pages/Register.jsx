import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import './Auth.css';

export function RegisterPage() {
  const { register } = useAuth();
  const navigate = useNavigate();

  const [form, setForm] = useState({ username: '', email: '', password: '', confirm: '' });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleChange = e => setForm(f => ({ ...f, [e.target.name]: e.target.value }));

  const handleSubmit = async e => {
    e.preventDefault();
    setError('');

    if (form.password !== form.confirm) {
      setError('Las contraseñas no coinciden');
      return;
    }
    if (form.password.length < 6) {
      setError('La contraseña debe tener al menos 6 caracteres');
      return;
    }

    setLoading(true);
    try {
      await register(form.username, form.email, form.password);
      navigate('/', { replace: true });
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-page">
      <div className="auth-backdrop" />

      <div className="auth-card auth-card--register">
        <div className="auth-header">
          <div className="auth-logo">
            <span className="auth-logo-icon">✦</span>
          </div>
          <h1 className="auth-title">Crear cuenta</h1>
          <p className="auth-subtitle">Únete a la comunidad de lectores</p>
        </div>

        <form className="auth-form" onSubmit={handleSubmit} noValidate>
          <div className="auth-field">
            <label htmlFor="username">Usuario</label>
            <input
              id="username"
              name="username"
              type="text"
              autoComplete="username"
              autoFocus
              value={form.username}
              onChange={handleChange}
              placeholder="tu_usuario"
              required
            />
          </div>

          <div className="auth-field">
            <label htmlFor="email">Email</label>
            <input
              id="email"
              name="email"
              type="email"
              autoComplete="email"
              value={form.email}
              onChange={handleChange}
              placeholder="correo@ejemplo.com"
              required
            />
          </div>

          <div className="auth-field">
            <label htmlFor="password">Contraseña</label>
            <input
              id="password"
              name="password"
              type="password"
              autoComplete="new-password"
              value={form.password}
              onChange={handleChange}
              placeholder="Mín. 6 caracteres"
              required
            />
          </div>

          <div className="auth-field">
            <label htmlFor="confirm">Confirmar contraseña</label>
            <input
              id="confirm"
              name="confirm"
              type="password"
              autoComplete="new-password"
              value={form.confirm}
              onChange={handleChange}
              placeholder="Repite la contraseña"
              required
            />
          </div>

          {error && (
            <div className="auth-error" role="alert">
              <span>⚠</span> {error}
            </div>
          )}

          <button className="auth-btn" type="submit" disabled={loading}>
            {loading ? <span className="auth-spinner-sm" /> : 'Crear cuenta'}
          </button>
        </form>

        <p className="auth-footer-text">
          ¿Ya tienes cuenta?{' '}
          <Link to="/login" className="auth-link">Inicia sesión</Link>
        </p>
      </div>
    </div>
  );
}
