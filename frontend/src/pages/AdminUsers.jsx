import { useEffect, useState } from 'react';
import api from '../api';

export default function AdminUsers() {
  const [users, setUsers] = useState([]);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api.get('/dashboard/users')
      .then(({ data }) => setUsers(data))
      .catch((err) => setError(err.response?.data?.message || 'Failed to load users'))
      .finally(() => setLoading(false));
  }, []);

  return (
    <div className="card wide">
      <h2>All Registered Users</h2>
      {error && <div className="alert error">{error}</div>}
      {loading ? 'Loading...' : (
        <table className="grid">
          <thead>
            <tr><th>ID</th><th>Full name</th><th>Email</th><th>Role</th><th>Registered</th></tr>
          </thead>
          <tbody>
            {users.map((u) => (
              <tr key={u.id}>
                <td>{u.id}</td><td>{u.fullName}</td><td>{u.email}</td>
                <td><span className={`badge ${u.role}`}>{u.role}</span></td>
                <td>{new Date(u.createdAt).toLocaleString()}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
      <p className="muted">Total: {users.length}</p>
    </div>
  );
}
