'use client'
import React, { useState } from 'react'
import AccountCircleOutlinedIcon from '@mui/icons-material/AccountCircleOutlined';
import EmailIcon from '@mui/icons-material/Email';
import KeyOutlinedIcon from '@mui/icons-material/KeyOutlined';
import { useRouter } from 'next/navigation';
import { useAuth } from '@/context/AuthContext';

type Props = {

}

const page = ( {} : Props) => {
    const { login } = useAuth();
    const router = useRouter();
    const usernameRef = React.useRef<HTMLInputElement>(null);
    const passwordRef = React.useRef<HTMLInputElement>(null);

    return (
        <div className='flex items-center justify-center w-full h-full flex-grow'>
            <div className='card bg-gray-100 w-96 h-96 flex flex-col gap-2 p-3 shadow-md'>
                <h3 className='text-xl font-semibold text-center py-3'>
                    Login 
                </h3>
                <label className="input input-bordered flex items-center gap-2">
                    <EmailIcon />
                    <input type="text" className="grow" placeholder="Email" ref={usernameRef}/>
                </label>

                <label className="input input-bordered flex items-center gap-2">
                    <KeyOutlinedIcon />
                    <input type="password" placeholder="********" className="grow" ref={passwordRef} />
                </label>
                <div className='flex items-center justify-end gap-2 pr-4'>
                    <button className='btn bg-gray-300' onClick={ async () => {
                        const username = usernameRef.current?.value;
                        const password = passwordRef.current?.value;
                        if (!username || !password) return;
                        await login(username, password)
                        router.push('/');
                    }}>
                        Login
                    </button>
                </div>
            </div>
        </div>
    )
}

export default page