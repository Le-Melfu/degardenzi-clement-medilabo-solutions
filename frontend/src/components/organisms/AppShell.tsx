import type { ReactNode } from 'react'
import { AppHeader } from './AppHeader'
import './AppShell.scss'

type AppShellProps = {
  username: string
  onLogout: () => void
  children: ReactNode
}

export function AppShell({ username, onLogout, children }: AppShellProps) {
  return (
    <div className="app-shell">
      <AppHeader username={username} onLogout={onLogout} />
      <main className="app-shell__main">{children}</main>
    </div>
  )
}
