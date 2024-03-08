'use client'
import React from 'react'
import NavbarItem from './NavbarItem'
import { useRouter } from 'next/navigation'
import Link from 'next/link'
import { useAuth } from '@/context/AuthContext'

type Props = {}

const Navbar = ( {} : Props) => {
    const { isAuthenticated, logout } = useAuth()
    const router = useRouter();
    return (
        <nav 
            className='w-full h-24 bg-slate-200 flex-none px-10 rounded-md shadow-md flex items-center justify-between'>

            <NavbarItem>
                <Link href='/'>
                    Home
                </Link>
            </NavbarItem>

            <nav className='flex items-center gap-4'>
                <NavbarItem> 
                    <Link href='/appointment'>
                        Appointment
                    </Link>
                </NavbarItem>
                { !isAuthenticated && <NavbarItem>
                    <Link href='/signup'>
                        Signup
                    </Link>
                </NavbarItem>  }
                { !isAuthenticated && <NavbarItem>
                    <Link href='/login'>
                        Login
                    </Link>
                </NavbarItem>  }
                { isAuthenticated && <NavbarItem>
                    <button className="hover:underline" onClick={() => {
                        logout()
                    }}>
                        Logout
                    </button>
                </NavbarItem> }
            </nav>
        </nav>
    )
}

export default Navbar