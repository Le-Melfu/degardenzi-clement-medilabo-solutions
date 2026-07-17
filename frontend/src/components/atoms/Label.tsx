import type { LabelHTMLAttributes, ReactNode } from 'react'
import './Label.scss'

type LabelProps = LabelHTMLAttributes<HTMLLabelElement> & {
  children: ReactNode
}

export function Label({ children, className = '', ...props }: LabelProps) {
  return (
    <label className={`label ${className}`.trim()} {...props}>
      {children}
    </label>
  )
}
