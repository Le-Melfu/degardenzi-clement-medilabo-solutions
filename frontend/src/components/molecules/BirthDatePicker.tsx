import { useEffect, useRef, useState } from 'react'
import { DayPicker } from 'react-day-picker'
import { format, parseISO, startOfDay } from 'date-fns'
import { fr } from 'date-fns/locale'
import { Label } from '../atoms/Label'
import 'react-day-picker/style.css'
import './BirthDatePicker.scss'

type BirthDatePickerProps = {
  id: string
  label: string
  value: string
  onChange: (value: string) => void
  required?: boolean
}

export function BirthDatePicker({ id, label, value, onChange, required }: BirthDatePickerProps) {
  const [open, setOpen] = useState(false)
  const rootRef = useRef<HTMLDivElement>(null)
  const today = startOfDay(new Date())
  const selected = value ? parseISO(value) : undefined

  useEffect(() => {
    const onDocClick = (event: MouseEvent) => {
      if (!rootRef.current?.contains(event.target as Node)) {
        setOpen(false)
      }
    }
    document.addEventListener('mousedown', onDocClick)
    return () => document.removeEventListener('mousedown', onDocClick)
  }, [])

  return (
    <div className="form-field birth-date-picker" ref={rootRef}>
      <Label htmlFor={id}>{label}</Label>
      <button
        type="button"
        id={id}
        className="input birth-date-picker__trigger"
        onClick={() => setOpen((prev) => !prev)}
        aria-haspopup="dialog"
        aria-expanded={open}
      >
        {selected
          ? format(selected, 'dd/MM/yyyy', { locale: fr })
          : 'Choisir une date'}
      </button>
      <input
        type="text"
        className="birth-date-picker__required"
        value={value}
        onChange={() => undefined}
        required={required}
        tabIndex={-1}
        aria-hidden="true"
      />
      {open ? (
        <div className="birth-date-picker__popover" role="dialog" aria-label={label}>
          <DayPicker
            mode="single"
            locale={fr}
            selected={selected}
            onSelect={(day) => {
              if (!day) {
                return
              }
              onChange(format(day, 'yyyy-MM-dd'))
              setOpen(false)
            }}
            disabled={{ after: today }}
            defaultMonth={selected ?? today}
          />
        </div>
      ) : null}
    </div>
  )
}
