import type { HTMLAttributes, ReactNode } from 'react'
import './Text.scss'

type TextTone = 'default' | 'muted' | 'danger'
type TextProps = HTMLAttributes<HTMLParagraphElement> & {
  as?: 'p' | 'span'
  tone?: TextTone
  children: ReactNode
}

export function Text({
  as: Tag = 'p',
  tone = 'default',
  children,
  className = '',
  ...props
}: TextProps) {
  return (
    <Tag className={`text text--${tone} ${className}`.trim()} {...props}>
      {children}
    </Tag>
  )
}
