import Link from 'next/link'
import React from 'react'

type Props = {
    children?: React.ReactNode
}

const NavbarItem = ( { children } : Props) => {
  return (
    <div className='hover:underline hover:pointer-cursor text-xl font-medium text-gray-700'>
        { children }
    </div>
  )
}

export default NavbarItem