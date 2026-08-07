export type Note = {
  id: string
  patId: number
  note: string
  createdAt: string
}

export type NotePayload = {
  patId: number
  note: string
}
