import { useEffect, useState } from 'react';
import api from '../api';
import { useAuth } from '../AuthContext';

export default function Dashboard() {
  const { saveUser } = useAuth();
  const [profile, setProfile] = useState(null);
  const [fullName, setFullName] = useState('');
  const [editing, setEditing] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  useEffect(() => {
    api.get('/dashboard')
      .then(({ data }) => { setProfile(data); setFullName(data.fullName); saveUser(data); })
      .catch((err) => setError(err.response?.data?.message || 'Failed to load profile'));
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const onSave = async (e) => {
    e.preventDefault();
    setError(''); setSuccess('');
    try {
      const { data } = await api.put('/users/me', { fullName });
      setProfile(data);
      saveUser(data);
      setEditing(false);
      setSuccess('Profile updated successfully.');
    } catch (err) {
      const d = err.response?.data;
      setError(d?.fieldErrors ? Object.values(d.fieldErrors).join(', ') : d?.message || 'Update failed');
    }
  };

  if (!profile) {
    return <div className="card">{error ? <div className="alert error">{error}</div> : 'Loading...'}</div>;
  }

  return (
    <div className="card">
      <h2>Welcome, {profile.fullName}!</h2>
      {error && <div className="alert error">{error}</div>}
      {success && <div className="alert success">{success}</div>}

      <table className="info">
        <tbody>
          <tr><th>ID</th><td>{profile.id}</td></tr>
          <tr><th>Full name</th><td>{profile.fullName}</td></tr>
          <tr><th>Email</th><td>{profile.email}</td></tr>
          <tr><th>Role</th><td><span className={`badge ${profile.role}`}>{profile.role}</span></td></tr>
          <tr><th>Member since</th><td>{new Date(profile.createdAt).toLocaleString()}</td></tr>
        </tbody>
      </table>

      {editing ? (
        <form onSubmit={onSave}>
          <label>Full name</label>
          <input value={fullName} onChange={(e) => setFullName(e.target.value)} />
          <div className="row">
            <button className="btn">Save</button>
            <button type="button" className="btn secondary"
              onClick={() => { setEditing(false); setFullName(profile.fullName); setError(''); }}>Cancel</button>
          </div>
        </form>
      ) : (
        <button className="btn" onClick={() => { setEditing(true); setSuccess(''); }}>Edit my profile</button>
      )}
    </div>
  );
}
