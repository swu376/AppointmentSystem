import React, { useState } from 'react'
import { Appointment } from '../appointment/page';
import { useAuth } from '@/context/AuthContext';
import { useRouter } from 'next/navigation';
import { useQuery } from 'react-query';

type Props = {
    index: number,
    appointment: Appointment,
    hospital: string,
    setModalOpen: React.Dispatch<React.SetStateAction<boolean>>,
    refreshAppointments: () => void
}

async function bookAppointment(appointmentId: number) {
    const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/appointment/update`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('token')}`
        },
        body: JSON.stringify({
            appointmentId
        })
    })
    return res.json()
}

const TableItem = ({ refreshAppointments, index, appointment, hospital, setModalOpen}: Props) => {
    const { isAuthenticated } = useAuth();
    const router = useRouter();
    const { refetch: confirm } = useQuery(`appointment-${appointment.appointmentId}`, () => bookAppointment(appointment.appointmentId), {
        enabled: false,
        onSuccess: () => {
            setModalOpen(true);
            refreshAppointments();
        }
    })

    return (
        <tr className="hover">
            <th>{index+1}</th>
            <td>{hospital}</td>
            <td>{appointment.doctorName}</td>
            <td>{new Date(appointment.startTime).toLocaleTimeString()}</td>
            <td>{new Date(appointment.endTime).toLocaleTimeString()}</td>
            <td>
                <div className="dropdown dropdown-bottom">
                    <div tabIndex={0} role="button" className="btn btn-outline btn-success">Book</div>
                    <ul tabIndex={0} className="dropdown-content z-[1] menu p-2 shadow bg-base-100 rounded-box w-52">
                        <li><a onClick={() => {
                            if (!isAuthenticated) router.push('/login')
                            else confirm()
                        }}>Confirm</a></li>
                    </ul>
                </div>
            </td>
        </tr>
    )
}

export default TableItem