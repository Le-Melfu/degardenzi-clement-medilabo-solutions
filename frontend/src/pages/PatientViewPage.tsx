import { useEffect, useState } from 'react'
import { Link, useNavigate, useParams } from 'react-router-dom'
import { Button } from '../components/atoms/Button'
import { AppShell } from '../components/organisms/AppShell'
import { getPatient, UnauthorizedError } from '../services/patientService'
import { useAuth } from '../session/AuthContext'
import type { Patient } from '../types/patient'
import './PatientDetailPage.scss'

export function PatientViewPage() {
  const { session, logout } = useAuth()
  const navigate = useNavigate()
  const { id } = useParams()
  const [patient, setPatient] = useState<Patient | null>(null)
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    if (!id) {
      return
    }

    let cancelled = false

    getPatient(Number(id))
      .then((data) => {
        if (!cancelled) {
          setPatient(data)
          setError('')
        }
      })
      .catch((err) => {
        if (cancelled) {
          return
        }
        if (err instanceof UnauthorizedError) {
          logout()
          navigate('/login', { replace: true })
          return
        }
        setError(err instanceof Error ? err.message : 'Impossible de charger le patient.')
      })
      .finally(() => {
        if (!cancelled) {
          setLoading(false)
        }
      })

    return () => {
      cancelled = true
    }
  }, [id, logout, navigate])

  if (!session) {
    return null
  }

  const handleLogout = () => {
    logout()
    navigate('/login', { replace: true })
  }

  return (
    <AppShell username={session.username} onLogout={handleLogout}>
      <div className="patient-detail">
        <header className="patient-detail__header">
          <div>
            <p className="patient-detail__back">
              <Link to="/">← Retour à la liste</Link>
            </p>
            <h2 className="patient-detail__title">Dossier patient</h2>
          </div>
        </header>

        {loading ? <p className="patient-detail__status">Chargement…</p> : null}
        {error ? <p className="patient-detail__error">{error}</p> : null}

        {!loading && patient ? (
          <>
            <dl className="patient-detail__view">
              <div className="patient-detail__field">
                <dt>Nom</dt>
                <dd>{patient.lastName}</dd>
              </div>
              <div className="patient-detail__field">
                <dt>Prénom</dt>
                <dd>{patient.firstName}</dd>
              </div>
              <div className="patient-detail__field">
                <dt>Date de naissance</dt>
                <dd>{patient.birthDate}</dd>
              </div>
              <div className="patient-detail__field">
                <dt>Genre</dt>
                <dd>{patient.gender}</dd>
              </div>
              <div className="patient-detail__field">
                <dt>Adresse</dt>
                <dd>{patient.address || '—'}</dd>
              </div>
              <div className="patient-detail__field">
                <dt>Téléphone</dt>
                <dd>{patient.phone || '—'}</dd>
              </div>
            </dl>
            <div className="patient-detail__actions">
              <Button type="button" onClick={() => navigate(`/patients/${patient.id}/edit`)}>
                Modifier
              </Button>
            </div>
          </>
        ) : null}
      </div>
    </AppShell>
  )
}
