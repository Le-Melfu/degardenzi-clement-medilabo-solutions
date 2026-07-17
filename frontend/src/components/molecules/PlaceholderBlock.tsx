import { Text } from '../atoms/Text'
import './PlaceholderBlock.scss'

type PlaceholderBlockProps = {
  title: string
  description: string
}

export function PlaceholderBlock({ title, description }: PlaceholderBlockProps) {
  return (
    <section className="placeholder-block" aria-label={title}>
      <h2 className="placeholder-block__title">{title}</h2>
      <Text tone="muted">{description}</Text>
      <div className="placeholder-block__body">Contenu à venir</div>
    </section>
  )
}
