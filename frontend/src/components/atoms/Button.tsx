import type { ButtonHTMLAttributes, ReactNode } from 'react'
import './Button.scss'

type ButtonVariant = 'primary' | 'secondary' | 'ghost'
type ButtonProps = ButtonHTMLAttributes<HTMLButtonElement> & {
  variant?: ButtonVariant
  children: ReactNode
}

export function Button({
  variant = 'primary',
  children,
  className = '',
  type = 'button',
  ...props
}: ButtonProps) {
  return (
    <button type={type} className={`btn btn--${variant} ${className}`.trim()} {...props}>
      {children}
    </button>
  )
}
