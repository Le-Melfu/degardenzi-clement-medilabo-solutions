import { apiFetch, UnauthorizedError } from '../session/apiClient'
import type { Patient, PatientPayload } from '../types/patient'

export { UnauthorizedError }

export async function listPatients(): Promise<Patient[]> {
  const response = await apiFetch('/patient')
  if (!response.ok) {
    throw new Error('Unable to load patients')
  }
  return response.json()
}

export async function getPatient(id: number): Promise<Patient> {
  const response = await apiFetch(`/patient/${id}`)
  if (response.status === 404) {
    throw new Error('Patient not found')
  }
  if (!response.ok) {
    throw new Error('Unable to load patient')
  }
  return response.json()
}

export async function createPatient(payload: PatientPayload): Promise<Patient> {
  const response = await apiFetch('/patient', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload),
  })
  if (!response.ok) {
    throw new Error('Unable to create patient')
  }
  return response.json()
}

export async function updatePatient(id: number, payload: PatientPayload): Promise<Patient> {
  const response = await apiFetch(`/patient/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload),
  })
  if (!response.ok) {
    throw new Error('Unable to update patient')
  }
  return response.json()
}
