export type RiskLevel = 'None' | 'Borderline' | 'In Danger' | 'Early onset'

export type RiskAssessment = {
  patId: number
  risk: RiskLevel
  triggerCount: number
  triggers: string[]
  mode: 'clinical' | 'strict'
  firstName?: string
  lastName?: string
  age?: number
  gender?: string
}

export function riskModifier(risk: RiskLevel): string {
  if (risk === 'In Danger') {
    return 'in-danger'
  }
  if (risk === 'Early onset') {
    return 'early-onset'
  }
  return risk.toLowerCase()
}
