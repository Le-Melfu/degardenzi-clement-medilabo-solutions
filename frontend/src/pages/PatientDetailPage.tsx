import { FormEvent, useEffect, useState } from 'react'
import { Link, useNavigate, useParams } from 'react-router-dom'
import { Button } from '../components/atoms/Button'
import { FormField } from '../components/molecules/FormField'
import { BirthDatePicker } from '../components/molecules/BirthDatePicker'
import { Label } from '../components/atoms/Label'
import { AppShell } from '../components/organisms/AppShell'
import {
  createPatient,
  getPatient,
  updatePatient,
  UnauthorizedError,
} from '../services/patientService'
import { useAuth } from '../session/AuthContext'
import type { PatientPayload } from '../types/patient'
import './PatientDetailPage.scss'

const emptyForm: PatientPayload = {
  firstName: '',
  lastName: '',
  birthDate: '',
  gender: 'F',
  address: '',
  phone: '',
}

type PatientDetailPageProps = {
  mode: 'create' | 'edit'
}

export function PatientDetailPage({ mode }: PatientDetailPageProps) {
  const { session, logout } = useAuth()
  const navigate = useNavigate()
  const { id } = useParams()
  const [form, setForm] = useState<PatientPayload>(emptyForm)
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(mode === 'edit')
  const [saving, setSaving] = useState(false)

  useEffect(() => {
    if (mode !== 'edit' || !id) {
      return
    }

    let cancelled = false
    const patientId = Number(id)

    getPatient(patientId)
      .then((patient) => {
        if (cancelled) {
          return
        }
        setForm({
          firstName: patient.firstName,
          lastName: patient.lastName,
          birthDate: patient.birthDate,
          gender: patient.gender,
          address: patient.address ?? '',
          phone: patient.phone ?? '',
        })
        setError('')
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
  }, [id, logout, mode, navigate])

  if (!session) {
    return null
  }

  const handleLogout = () => {
    logout()
    navigate('/login', { replace: true })
  }

  const handleChange = (field: keyof PatientPayload, value: string) => {
    setForm((prev) => ({ ...prev, [field]: value }))
  }

  const handleGenderChange = (value: string) => {
    if (value === 'M' || value === 'F') {
      setForm((prev) => ({ ...prev, gender: value }))
    }
  }

  const handleSubmit = async (event: FormEvent) => {
    event.preventDefault()
    setSaving(true)
    setError('')

    const payload: PatientPayload = {
      firstName: form.firstName.trim(),
      lastName: form.lastName.trim(),
      birthDate: form.birthDate,
      gender: form.gender,
      address: form.address?.trim() || undefined,
      phone: form.phone?.trim() || undefined,
    }

    try {
      if (mode === 'create') {
        const created = await createPatient(payload)
        navigate(`/patients/${created.id}`, { replace: true })
      } else if (id) {
        await updatePatient(Number(id), payload)
        navigate(`/patients/${id}`, { replace: true })
      }
    } catch (err) {
      if (err instanceof UnauthorizedError) {
        logout()
        navigate('/login', { replace: true })
        return
      }
      setError(mode === 'create' ? 'Création impossible.' : 'Mise à jour impossible.')
    } finally {
      setSaving(false)
    }
  }

  return (
    <AppShell username={session.username} onLogout={handleLogout}>
      <div className="patient-detail page-enter">
        <header className="patient-detail__header">
          <div>
            <p className="patient-detail__back">
              <Link to={mode === 'create' ? '/' : `/patients/${id}`}>
                ← {mode === 'create' ? 'Retour à la liste' : 'Retour au dossier'}
              </Link>
            </p>
            <h2 className="patient-detail__title">
              {mode === 'create' ? 'Nouveau patient' : 'Modifier le patient'}
            </h2>
          </div>
        </header>

        {loading ? <p className="patient-detail__status">Chargement…</p> : null}
        {error ? <p className="patient-detail__error">{error}</p> : null}

        {!loading ? (
          <form className="patient-detail__form" onSubmit={handleSubmit}>
            <FormField
              id="lastName"
              label="Nom *"
              value={form.lastName}
              onChange={(e) => handleChange('lastName', e.target.value)}
              required
            />
            <FormField
              id="firstName"
              label="Prénom *"
              value={form.firstName}
              onChange={(e) => handleChange('firstName', e.target.value)}
              required
            />
            <BirthDatePicker
              id="birthDate"
              label="Date de naissance *"
              value={form.birthDate}
              onChange={(value) => handleChange('birthDate', value)}
              required
            />
            <div className="form-field">
              <Label htmlFor="gender">Genre *</Label>
              <select
                id="gender"
                className="input"
                value={form.gender}
                onChange={(e) => handleGenderChange(e.target.value)}
                required
              >
                <option value="F">Femme</option>
                <option value="M">Homme</option>
              </select>
            </div>
            <FormField
              id="address"
              label="Adresse (optionnel)"
              value={form.address ?? ''}
              onChange={(e) => handleChange('address', e.target.value)}
            />
            <FormField
              id="phone"
              label="Téléphone (optionnel)"
              value={form.phone ?? ''}
              onChange={(e) => handleChange('phone', e.target.value)}
            />
            <div className="patient-detail__actions">
              <Button
                type="button"
                variant="secondary"
                onClick={() => navigate(mode === 'create' ? '/' : `/patients/${id}`)}
              >
                Annuler
              </Button>
              <Button type="submit" disabled={saving}>
                {saving ? 'Enregistrement…' : mode === 'create' ? 'Créer' : 'Enregistrer'}
              </Button>
            </div>
          </form>
        ) : null}
      </div>
    </AppShell>
  )
}
