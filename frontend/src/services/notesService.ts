import { apiFetch, UnauthorizedError } from '../session/apiClient'
import type { Note, NotePayload } from '../types/note'

export { UnauthorizedError }

export async function listNotesByPatient(patId: number): Promise<Note[]> {
  const response = await apiFetch(`/notes/patient/${patId}`)
  if (!response.ok) {
    throw new Error('Unable to load notes')
  }
  return response.json()
}

export async function createNote(payload: NotePayload): Promise<Note> {
  const response = await apiFetch('/notes', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload),
  })
  if (!response.ok) {
    throw new Error('Unable to create note')
  }
  return response.json()
}
