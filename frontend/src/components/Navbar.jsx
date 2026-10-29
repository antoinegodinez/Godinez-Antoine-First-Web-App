import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../AuthContext';

export default function Navbar() {
  const { token, user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = async () => {
    await logout();
    navigate('/login');
  };

  return (
    <nav className="navbar">
      <Link to="/" className="brand">UserHub</Link>
      <div className="nav-links">
        {token ? (
          <>
            <Link to="/dashboard">Dashboard</Link>
            {user?.role === 'ADMIN' && <Link to="/admin/users">Users</Link>}
            <span className="who">{user?.fullName} ({user?.role})</span>
            <button className="btn small" onClick={handleLogout}>Logout</button>
          </>
        ) : (
          <>
            <Link to="/login">Login</Link>
            <Link to="/register">Register</Link>
          </>
        )}
      </div>
    </nav>
  );
}
