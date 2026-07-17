import type { InputHTMLAttributes, ReactNode } from 'react'
import { Input } from '../atoms/Input'
import { Label } from '../atoms/Label'
import './FormField.scss'

type FormFieldProps = InputHTMLAttributes<HTMLInputElement> & {
  id: string
  label: string
  hint?: ReactNode
}

export function FormField({ id, label, hint, ...inputProps }: FormFieldProps) {
  return (
    <div className="form-field">
      <Label htmlFor={id}>{label}</Label>
      <Input id={id} {...inputProps} />
      {hint ? <div className="form-field__hint">{hint}</div> : null}
    </div>
  )
}
