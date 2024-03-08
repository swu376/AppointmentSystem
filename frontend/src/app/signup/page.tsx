'use client'
import React, { useState } from 'react'
import AccountCircleOutlinedIcon from '@mui/icons-material/AccountCircleOutlined';
import KeyOutlinedIcon from '@mui/icons-material/KeyOutlined';
import { signup } from '@/utils/api';
import { useRouter } from 'next/navigation';
import EmailIcon from '@mui/icons-material/Email';

type Props = {

}

const page = ( {} : Props) => {
    const router = useRouter();
    const usernameRef = React.useRef<HTMLInputElement>(null);
    const passwordRef = React.useRef<HTMLInputElement>(null);

    return (
        <div className='flex items-center justify-center w-full h-full flex-grow'>
            <div className='card bg-gray-100 w-96 h-96 flex flex-col gap-2 p-3 shadow-md'>
                <h3 className='text-xl font-semibold text-center py-3'>
                    Signup
                </h3>
                <label className="input input-bordered flex items-center gap-2">
                    <EmailIcon />
                    <input type="text" className="grow" placeholder="email" ref={usernameRef}/>
                </label>

                <label className="input input-bordered flex items-center gap-2">
                    <KeyOutlinedIcon />
                    <input type="password" placeholder="********" className="grow" ref={passwordRef} />
                </label>
                <div className='flex items-center justify-end gap-2 pr-4'>
                    <button className='btn bg-slate-200' onClick={ async () => {
                        const username = usernameRef.current?.value;
                        const password = passwordRef.current?.value;
                        if (!username || !password) return;
                        try {
                            await signup(username, password);
                            router.push('/login')
                        } catch (e) {
                            console.log(`error is ${e}`)
                        }
                    }}>
                        Sign Up
                    </button>
                </div>
            </div>
        </div>
    )
}

export default page