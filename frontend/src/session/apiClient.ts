import { readSession, clearSession } from './sessionStorage'

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080'

export class UnauthorizedError extends Error {
  constructor() {
    super('Unauthorized')
    this.name = 'UnauthorizedError'
  }
}

export function apiFetch(path: string, init: RequestInit = {}): Promise<Response> {
  const session = readSession()
  const headers = new Headers(init.headers)

  if (session) {
    headers.set('Authorization', `Basic ${session.credentials}`)
  }

  return fetch(`${API_BASE_URL}${path}`, { ...init, headers }).then((response) => {
    if (response.status === 401) {
      clearSession()
      throw new UnauthorizedError()
    }
    return response
  })
}
