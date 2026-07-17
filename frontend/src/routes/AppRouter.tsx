import { Navigate, Route, Routes } from 'react-router-dom'
import { PatientDetailPage } from '../pages/PatientDetailPage'
import { PatientsPage } from '../pages/PatientsPage'
import { LoginPage } from '../pages/LoginPage'
import { useAuth } from '../session/AuthContext'
import { ProtectedRoute } from './ProtectedRoute'

export function AppRouter() {
  const { isAuthenticated } = useAuth()

  return (
    <Routes>
      <Route
        path="/login"
        element={isAuthenticated ? <Navigate to="/" replace /> : <LoginPage />}
      />
      <Route element={<ProtectedRoute />}>
        <Route path="/" element={<PatientsPage />} />
        <Route path="/patients/new" element={<PatientDetailPage mode="create" />} />
        <Route path="/patients/:id" element={<PatientDetailPage mode="edit" />} />
      </Route>
      <Route path="*" element={<Navigate to={isAuthenticated ? '/' : '/login'} replace />} />
    </Routes>
  )
}
