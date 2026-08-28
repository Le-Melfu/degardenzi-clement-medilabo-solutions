export type RiskLevel = 'None' | 'Borderline' | 'In Danger' | 'Early onset'

export type RiskAssessment = {
  patId: number
  risk: RiskLevel
  firstName?: string
  lastName?: string
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

export function riskLabel(risk: RiskLevel): string {
  if (risk === 'None') {
    return 'Aucun risque'
  }
  if (risk === 'Borderline') {
    return 'Risque limité'
  }
  if (risk === 'In Danger') {
    return 'Danger'
  }
  return 'Apparition précoce'
}
