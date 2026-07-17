import { Button } from '../atoms/Button'
import { Text } from '../atoms/Text'
import './UserMenu.scss'

type UserMenuProps = {
  username: string
  onLogout: () => void
}

export function UserMenu({ username, onLogout }: UserMenuProps) {
  return (
    <div className="user-menu">
      <Text as="span" tone="muted" className="user-menu__name">
        Connecté : <strong>{username}</strong>
      </Text>
      <Button variant="secondary" onClick={onLogout}>
        Déconnexion
      </Button>
    </div>
  )
}
