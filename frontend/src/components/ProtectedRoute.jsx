import { Navigate } from 'react-router-dom';
import { useAuth } from '../AuthContext';

export default function ProtectedRoute({ children, adminOnly = false }) {
  const { token, user } = useAuth();
  if (!token) return <Navigate to="/login" replace />;
  if (adminOnly && user?.role !== 'ADMIN') return <Navigate to="/dashboard" replace />;
  return children;
}
