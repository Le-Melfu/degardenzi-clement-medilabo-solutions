import { apiFetch, UnauthorizedError } from '../session/apiClient'
import type { RiskAssessment } from '../types/risk'

export { UnauthorizedError }

export async function getRisk(patId: number): Promise<RiskAssessment> {
  const response = await apiFetch(`/risk/${patId}`)
  if (!response.ok) {
    throw new Error('Unable to load risk assessment')
  }
  return response.json()
}
