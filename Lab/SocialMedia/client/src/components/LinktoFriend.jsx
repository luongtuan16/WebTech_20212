import React from 'react'
import { Link } from 'react-router-dom'
import CloseFriend from './closeFriend/CloseFriend'
export default function LinktoFriend({ u }) {
  return (
    <Link to={`/profile/${u.user_id}`}>
      <CloseFriend key={u.id} user={u} />
    </Link>
  )
}
