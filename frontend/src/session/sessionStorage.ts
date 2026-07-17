import type { Session } from '../types/auth'

const SESSION_KEY = 'medilabo.session'

export function readSession(): Session | null {
  const raw = sessionStorage.getItem(SESSION_KEY)
  if (!raw) {
    return null
  }
  return JSON.parse(raw) as Session
}

export function writeSession(session: Session): void {
  sessionStorage.setItem(SESSION_KEY, JSON.stringify(session))
}

export function clearSession(): void {
  sessionStorage.removeItem(SESSION_KEY)
}

export function toBasicCredentials(username: string, password: string): string {
  return btoa(`${username}:${password}`)
}
