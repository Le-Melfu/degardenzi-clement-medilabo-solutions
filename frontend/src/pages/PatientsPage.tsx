import { useEffect, useMemo, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { Button } from '../components/atoms/Button'
import { Input } from '../components/atoms/Input'
import { AppShell } from '../components/organisms/AppShell'
import { listPatients, UnauthorizedError } from '../services/patientService'
import { useAuth } from '../session/AuthContext'
import type { Patient } from '../types/patient'
import { calcAge, formatGender } from '../utils/patientDisplay'
import './PatientsPage.scss'

export function PatientsPage() {
  const { session, logout } = useAuth()
  const navigate = useNavigate()
  const [patients, setPatients] = useState<Patient[]>([])
  const [query, setQuery] = useState('')
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

  const filtered = useMemo(() => {
    const q = query.trim().toLowerCase()
    if (!q) {
      return patients
    }
    return patients.filter((patient) => {
      const haystack = `${patient.lastName} ${patient.firstName}`.toLowerCase()
      return haystack.includes(q)
    })
  }, [patients, query])

  if (!session) {
    return null
  }

  const handleLogout = () => {
    logout()
    navigate('/login', { replace: true })
  }

  return (
    <AppShell username={session.username} onLogout={handleLogout}>
      <div className="patients-page page-enter">
        <header className="patients-page__header">
          <div>
            <div className="patients-page__title-row">
              <h2 className="patients-page__title">Patients</h2>
              {!loading && !error ? (
                <span className="patients-page__count">{patients.length}</span>
              ) : null}
            </div>
            <p className="patients-page__subtitle">
              Consultez et mettez à jour les dossiers démographiques.
            </p>
          </div>
          <Button type="button" onClick={() => navigate('/patients/new')}>
            Ajouter un patient
          </Button>
        </header>

        {!loading && !error ? (
          <div className="patients-page__toolbar">
            <Input
              className="patients-page__search"
              type="search"
              placeholder="Rechercher un patient…"
              value={query}
              onChange={(e) => setQuery(e.target.value)}
              aria-label="Rechercher un patient"
            />
          </div>
        ) : null}

        {loading ? (
          <div className="patients-page__skeleton" aria-hidden>
            <div className="patients-page__skeleton-row" />
            <div className="patients-page__skeleton-row" />
            <div className="patients-page__skeleton-row" />
          </div>
        ) : null}

        {error ? <p className="patients-page__error">{error}</p> : null}

        {!loading && !error && filtered.length === 0 ? (
          <div className="patients-page__empty">
            <p className="patients-page__empty-title">Aucun patient trouvé</p>
            <p className="patients-page__empty-text">
              {query.trim()
                ? 'Essayez un autre nom ou prénom.'
                : 'Ajoutez un premier dossier pour commencer.'}
            </p>
          </div>
        ) : null}

        {!loading && !error && filtered.length > 0 ? (
          <div className="patients-page__table-wrap">
            <table className="patients-page__table">
              <thead>
                <tr>
                  <th>Patient</th>
                  <th>Date de naissance</th>
                  <th>Âge</th>
                  <th>Genre</th>
                  <th></th>
                </tr>
              </thead>
              <tbody>
                {filtered.map((patient) => (
                  <tr
                    key={patient.id}
                    className="patients-page__row"
                    onClick={() => navigate(`/patients/${patient.id}`)}
                    onKeyDown={(e) => {
                      if (e.key === 'Enter' || e.key === ' ') {
                        e.preventDefault()
                        navigate(`/patients/${patient.id}`)
                      }
                    }}
                    tabIndex={0}
                    role="link"
                  >
                    <td>
                      <div className="patients-page__identity">
                        <span className="patients-page__avatar" aria-hidden>
                          {patient.firstName.charAt(0)}
                          {patient.lastName.charAt(0)}
                        </span>
                        <span>
                          <strong>
                            {patient.lastName} {patient.firstName}
                          </strong>
                        </span>
                      </div>
                    </td>
                    <td>{patient.birthDate}</td>
                    <td>{calcAge(patient.birthDate)} ans</td>
                    <td>
                      <span
                        className={`patients-page__chip patients-page__chip--${patient.gender.toLowerCase()}`}
                      >
                        {formatGender(patient.gender)}
                      </span>
                    </td>
                    <td>
                      <span className="patients-page__link">Voir</span>
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
