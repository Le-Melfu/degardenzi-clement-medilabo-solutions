import { apiFetch, UnauthorizedError } from '../session/apiClient'
import type { RiskAssessment } from '../types/risk'

export { UnauthorizedError }

export async function getRisk(patId: number): Promise<RiskAssessment> {
  const response = await apiFetch(`/risk/${patId}`)
  if (response.status === 503) {
    throw new Error('Le calcul de risque est indisponible pour le moment.')
  }
  if (!response.ok) {
    throw new Error('Impossible de charger le risque.')
  }
  return response.json()
}
