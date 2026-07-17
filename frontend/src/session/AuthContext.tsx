import { createContext, useContext, useState, type ReactNode } from 'react'
import type { LoginCredentials, Session } from '../types/auth'
import * as authService from './authService'

type AuthContextValue = {
  session: Session | null
  isAuthenticated: boolean
  login: (credentials: LoginCredentials) => Promise<void>
  logout: () => void
}

const AuthContext = createContext<AuthContextValue | null>(null)

type AuthProviderProps = {
  children: ReactNode
}

export function AuthProvider({ children }: AuthProviderProps) {
  const [session, setSession] = useState<Session | null>(() => authService.getSession())

  const login = async (credentials: LoginCredentials) => {
    const nextSession = await authService.login(credentials)
    setSession(nextSession)
  }

  const logout = () => {
    authService.logout()
    setSession(null)
  }

  const value: AuthContextValue = {
    session,
    isAuthenticated: session !== null,
    login,
    logout
  }

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

export function useAuth(): AuthContextValue {
  const context = useContext(AuthContext)
  if (!context) {
    throw new Error('useAuth must be used within AuthProvider')
  }
  return context
}
