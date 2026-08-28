import { FormEvent, useEffect, useState } from 'react'
import { Link, useNavigate, useParams } from 'react-router-dom'
import { Button } from '../components/atoms/Button'
import { Label } from '../components/atoms/Label'
import { AppShell } from '../components/organisms/AppShell'
import { createNote, listNotesByPatient, UnauthorizedError } from '../services/notesService'
import { getPatient } from '../services/patientService'
import { getRisk } from '../services/riskService'
import { useAuth } from '../session/AuthContext'
import type { Note } from '../types/note'
import type { Patient } from '../types/patient'
import { riskLabel, riskModifier, type RiskAssessment } from '../types/risk'
import {
  calcAge,
  formatBirthDate,
  formatGender,
  formatNoteDate,
  patientInitials,
} from '../utils/patientDisplay'
import './PatientDetailPage.scss'

export function PatientViewPage() {
  const { session, logout } = useAuth()
  const navigate = useNavigate()
  const { id } = useParams()
  const [patient, setPatient] = useState<Patient | null>(null)
  const [notes, setNotes] = useState<Note[]>([])
  const [risk, setRisk] = useState<RiskAssessment | null>(null)
  const [newNote, setNewNote] = useState('')
  const [error, setError] = useState('')
  const [notesError, setNotesError] = useState('')
  const [riskError, setRiskError] = useState('')
  const [loading, setLoading] = useState(true)
  const [savingNote, setSavingNote] = useState(false)

  useEffect(() => {
    if (!id) {
      return
    }

    let cancelled = false
    const patId = Number(id)

    Promise.all([getPatient(patId), listNotesByPatient(patId)])
      .then(([patientData, notesData]) => {
        if (!cancelled) {
          setPatient(patientData)
          setNotes(notesData)
          setError('')
          setNotesError('')
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

    getRisk(patId)
      .then((riskData) => {
        if (!cancelled) {
          setRisk(riskData)
          setRiskError('')
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
        setRiskError(err instanceof Error ? err.message : 'Impossible de charger le risque.')
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

  const handleAddNote = (event: FormEvent) => {
    event.preventDefault()
    if (!patient || !newNote) {
      return
    }

    setSavingNote(true)
    setNotesError('')

    createNote({ patId: patient.id, note: newNote })
      .then((created) => {
        setNotes((current) => [...current, created])
        setNewNote('')
        getRisk(patient.id)
          .then((riskData) => {
            setRisk(riskData)
            setRiskError('')
          })
          .catch((err) => {
            if (err instanceof UnauthorizedError) {
              logout()
              navigate('/login', { replace: true })
              return
            }
            setRiskError(err instanceof Error ? err.message : 'Impossible de charger le risque.')
          })
      })
      .catch((err) => {
        if (err instanceof UnauthorizedError) {
          logout()
          navigate('/login', { replace: true })
          return
        }
        setNotesError(err instanceof Error ? err.message : "Impossible d'ajouter la note.")
      })
      .finally(() => {
        setSavingNote(false)
      })
  }

  return (
    <AppShell username={session.username} onLogout={handleLogout}>
      <div className="patient-detail patient-detail--dossier page-enter">
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
          <div className="patient-detail__layout">
            <aside className="patient-detail__sidebar">
              <section className="patient-detail__identity" aria-labelledby="patient-identity-heading">
                <h3 id="patient-identity-heading" className="patient-detail__identity-title">
                  Identité
                </h3>
                <div className="patient-detail__hero">
                  <span className="patient-detail__avatar" aria-hidden>
                    {patientInitials(patient.firstName, patient.lastName)}
                  </span>
                  <h4 className="patient-detail__name">
                    {patient.lastName} {patient.firstName}
                  </h4>
                </div>
                <dl className="patient-detail__view">
                  <div className="patient-detail__field">
                    <dt>Date de naissance</dt>
                    <dd>
                      {formatBirthDate(patient.birthDate)}{' '}
                      <span className="patient-detail__hint">({calcAge(patient.birthDate)} ans)</span>
                    </dd>
                  </div>
                  <div className="patient-detail__field">
                    <dt>Genre</dt>
                    <dd>
                      <span
                        className={`patient-detail__chip patient-detail__chip--${patient.gender.toLowerCase()}`}
                      >
                        {formatGender(patient.gender)}
                      </span>
                    </dd>
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
              </section>

              <section
                className={`patient-detail__risk-card${risk ? ` patient-detail__risk-card--${riskModifier(risk.risk)}` : ''}`}
                aria-labelledby="patient-risk-heading"
              >
                <h3 id="patient-risk-heading" className="patient-detail__risk-title">
                  Risque de diabète
                </h3>
                {risk ? (
                  <span
                    className={`patient-detail__risk-badge patient-detail__risk-badge--${riskModifier(risk.risk)}`}
                  >
                    {riskLabel(risk.risk)}
                  </span>
                ) : null}
                {riskError ? <p className="patient-detail__error">{riskError}</p> : null}
                {!risk && !riskError ? (
                  <p className="patient-detail__status">Chargement…</p>
                ) : null}
              </section>
            </aside>

            <section className="patient-detail__notes">
              <h3 className="patient-detail__notes-title">Notes du médecin</h3>

              <div className="patient-detail__notes-scroll">
                {notes.length === 0 ? (
                  <p className="patient-detail__status">Aucune note pour ce patient.</p>
                ) : (
                  <ul className="patient-detail__notes-list">
                    {notes.map((item) => (
                      <li key={item.id} className="patient-detail__note">
                        <time className="patient-detail__note-date" dateTime={item.createdAt}>
                          {formatNoteDate(item.createdAt)}
                        </time>
                        <p className="patient-detail__note-text">{item.note}</p>
                      </li>
                    ))}
                  </ul>
                )}
              </div>

              <form className="patient-detail__notes-form" onSubmit={handleAddNote}>
                <Label htmlFor="new-note">Ajouter une note</Label>
                <textarea
                  id="new-note"
                  className="patient-detail__notes-textarea"
                  value={newNote}
                  onChange={(event) => setNewNote(event.target.value)}
                  rows={3}
                  required
                />
                {notesError ? <p className="patient-detail__error">{notesError}</p> : null}
                <div className="patient-detail__actions">
                  <Button type="submit" disabled={savingNote || !newNote}>
                    {savingNote ? 'Ajout…' : 'Ajouter'}
                  </Button>
                </div>
              </form>
            </section>
          </div>
        ) : null}
      </div>
    </AppShell>
  )
}
