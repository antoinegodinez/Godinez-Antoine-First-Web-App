import { useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { useAuth } from '../AuthContext';

export default function Login() {
  const { login } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const [form, setForm] = useState({ email: '', password: '' });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const onChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const onSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);
    try {
      await login(form.email, form.password);
      navigate('/dashboard');
    } catch (err) {
      const data = err.response?.data;
      setError(data?.fieldErrors ? Object.values(data.fieldErrors).join(', ')
        : data?.message || 'Cannot reach the server. Is the backend running?');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="card">
      <h2>Login</h2>
      {location.state?.registered && (
        <div className="alert success">Registration successful. Please log in.</div>
      )}
      {error && <div className="alert error">{error}</div>}
      <form onSubmit={onSubmit}>
        <label>Email</label>
        <input name="email" type="email" value={form.email} onChange={onChange} />
        <label>Password</label>
        <input name="password" type="password" value={form.password} onChange={onChange} />
        <button className="btn" disabled={loading}>{loading ? 'Logging in...' : 'Login'}</button>
      </form>
      <p>No account yet? <Link to="/register">Register</Link></p>
    </div>
  );
}
