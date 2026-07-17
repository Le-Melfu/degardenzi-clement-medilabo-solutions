import type { AuthMeResponse, LoginCredentials, Session } from '../types/auth'
import { clearSession, readSession, toBasicCredentials, writeSession } from './sessionStorage'

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL ?? ''

export function getSession(): Session | null {
  return readSession()
}

export function logout(): void {
  clearSession()
}

export function login({ username, password }: LoginCredentials): Promise<Session> {
  const credentials = toBasicCredentials(username.trim(), password)

  return fetch(`${API_BASE_URL}/auth/me`, {
    headers: { Authorization: `Basic ${credentials}` }
  }).then((response) => {
    if (!response.ok) {
      throw new Error('INVALID_CREDENTIALS')
    }
    return response.json() as Promise<AuthMeResponse>
  }).then((body) => {
    const session: Session = {
      username: body.username,
      credentials
    }
    writeSession(session)
    return session
  })
}
