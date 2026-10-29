import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import api from '../api';

export default function Register() {
  const navigate = useNavigate();
  const [form, setForm] = useState({ fullName: '', email: '', password: '', confirm: '' });
  const [errors, setErrors] = useState({});
  const [message, setMessage] = useState('');
  const [loading, setLoading] = useState(false);

  const onChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const onSubmit = async (e) => {
    e.preventDefault();
    setErrors({});
    setMessage('');
    if (form.password !== form.confirm) {
      setErrors({ confirm: 'Passwords do not match' });
      return;
    }
    setLoading(true);
    try {
      await api.post('/auth/register', {
        fullName: form.fullName, email: form.email, password: form.password,
      });
      navigate('/login', { state: { registered: true } });
    } catch (err) {
      const data = err.response?.data;
      if (data?.fieldErrors) setErrors(data.fieldErrors);
      else setMessage(data?.message || 'Cannot reach the server. Is the backend running?');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="card">
      <h2>Create an account</h2>
      {message && <div className="alert error">{message}</div>}
      <form onSubmit={onSubmit}>
        <label>Full name</label>
        <input name="fullName" value={form.fullName} onChange={onChange} />
        {errors.fullName && <small className="err">{errors.fullName}</small>}

        <label>Email</label>
        <input name="email" type="email" value={form.email} onChange={onChange} />
        {errors.email && <small className="err">{errors.email}</small>}

        <label>Password (min 8 characters)</label>
        <input name="password" type="password" value={form.password} onChange={onChange} />
        {errors.password && <small className="err">{errors.password}</small>}

        <label>Confirm password</label>
        <input name="confirm" type="password" value={form.confirm} onChange={onChange} />
        {errors.confirm && <small className="err">{errors.confirm}</small>}

        <button className="btn" disabled={loading}>{loading ? 'Registering...' : 'Register'}</button>
      </form>
      <p>Already have an account? <Link to="/login">Login</Link></p>
    </div>
  );
}
