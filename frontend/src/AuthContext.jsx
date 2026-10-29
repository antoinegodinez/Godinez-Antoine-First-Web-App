import { createContext, useContext, useState } from 'react';
import api from './api';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [token, setToken] = useState(localStorage.getItem('token'));
  const [user, setUser] = useState(() => {
    try { return JSON.parse(localStorage.getItem('user')); } catch { return null; }
  });

  const saveUser = (u) => {
    localStorage.setItem('user', JSON.stringify(u));
    setUser(u);
  };

  const login = async (email, password) => {
    const { data } = await api.post('/auth/login', { email, password });
    localStorage.setItem('token', data.token);
    setToken(data.token);
    saveUser(data.user);
    return data.user;
  };

  // Server-side logout: invalidates the token on the backend, then clears local state
  const logout = async () => {
    try { await api.post('/auth/logout'); } catch { /* token may already be invalid */ }
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    setToken(null);
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ token, user, login, logout, saveUser }}>
      {children}
    </AuthContext.Provider>
  );
}

export const useAuth = () => useContext(AuthContext);
