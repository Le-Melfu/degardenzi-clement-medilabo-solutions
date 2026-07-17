import type { InputHTMLAttributes } from 'react'
import './Input.scss'

type InputProps = InputHTMLAttributes<HTMLInputElement>

export function Input({ className = '', ...props }: InputProps) {
  return <input className={`input ${className}`.trim()} {...props} />
}
