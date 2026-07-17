import { useEffect, useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { Button } from '../components/atoms/Button'
import { AppShell } from '../components/organisms/AppShell'
import { listPatients, UnauthorizedError } from '../services/patientService'
import { useAuth } from '../session/AuthContext'
import type { Patient } from '../types/patient'
import './PatientsPage.scss'

export function PatientsPage() {
  const { session, logout } = useAuth()
  const navigate = useNavigate()
  const [patients, setPatients] = useState<Patient[]>([])
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    let cancelled = false

    listPatients()
      .then((data) => {
        if (!cancelled) {
          setPatients(data)
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
        setError('Impossible de charger les patients.')
      })
      .finally(() => {
        if (!cancelled) {
          setLoading(false)
        }
      })

    return () => {
      cancelled = true
    }
  }, [logout, navigate])

  if (!session) {
    return null
  }

  const handleLogout = () => {
    logout()
    navigate('/login', { replace: true })
  }

  return (
    <AppShell username={session.username} onLogout={handleLogout}>
      <div className="patients-page">
        <header className="patients-page__header">
          <div>
            <h2 className="patients-page__title">Patients</h2>
            <p className="patients-page__subtitle">Informations personnelles des dossiers patients.</p>
          </div>
          <Button type="button" onClick={() => navigate('/patients/new')}>
            Ajouter un patient
          </Button>
        </header>

        {loading ? <p className="patients-page__status">Chargement…</p> : null}
        {error ? <p className="patients-page__error">{error}</p> : null}

        {!loading && !error ? (
          <div className="patients-page__table-wrap">
            <table className="patients-page__table">
              <thead>
                <tr>
                  <th>Nom</th>
                  <th>Prénom</th>
                  <th>Date de naissance</th>
                  <th>Genre</th>
                  <th></th>
                </tr>
              </thead>
              <tbody>
                {patients.map((patient) => (
                  <tr key={patient.id}>
                    <td>{patient.lastName}</td>
                    <td>{patient.firstName}</td>
                    <td>{patient.birthDate}</td>
                    <td>{patient.gender}</td>
                    <td>
                      <Link className="patients-page__link" to={`/patients/${patient.id}`}>
                        Voir
                      </Link>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        ) : null}
      </div>
    </AppShell>
  )
}
