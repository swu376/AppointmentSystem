import React from 'react'
import { Appointment } from '../appointment/page'
import { useAuth } from '@/context/AuthContext'
import { useQuery } from 'react-query'
import { useRouter } from 'next/navigation'

type Props = {
    appointment: Appointment,
    refreshHistory: () => void
}

async function deleteAppointment(id: number) {
    const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/appointment/delete`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('token')}`
        },
        body: JSON.stringify({ appointmentId: id })
    })
    if (response.ok) {
        window.location.reload()
    }
}

const AppointmentRow = ({ appointment, refreshHistory } : Props) => {
    const { isAuthenticated } = useAuth()
    const router = useRouter()
    const { refetch: removeAppointment } = useQuery(
        `delete-appointment-${appointment.appointmentId}`, 
        () => deleteAppointment(appointment.appointmentId), { 
            enabled: false,
            onSuccess: () => {
                refreshHistory()
            },
            onError: (err) => {
                console.log(err)
            }
        })    

    return (
        <tr className="hover">
            <th>{appointment.appointmentId}</th>
            <td>{appointment.hospitalName}</td>
            <td>{appointment.doctorName}</td>
            <td>{new Date(appointment.startTime).toLocaleString()}</td>
            <td>{new Date(appointment.endTime).toLocaleString()}</td>
            <td>
                <div className='dropdown dropdown-buttom'>
                    <div tabIndex={0} role="button" className="btn btn-outline btn-error">Delete</div>
                    <ul tabIndex={0} className="dropdown-content z-[20] menu p-2 shadow bg-base-100 rounded-box w-52">
                        <li><a onClick={() => {
                            if (!isAuthenticated) router.push('/login')
                            else removeAppointment()
                        }}>Confirm</a></li>
                    </ul>
                </div>
            </td>
        </tr>
    )
}

export default AppointmentRow