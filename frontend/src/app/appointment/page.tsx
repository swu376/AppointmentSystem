'use client'
import React, { useEffect, useState, useRef } from 'react'
import TableItem from '../components/TableItem'
import { useQuery } from 'react-query'
import { usePathname, useRouter, useSearchParams } from 'next/navigation'

type Hospital = {
    name: string,
    address: string
}

export type Appointment = {
    hospitalName: string, 
    startTime: string,
    endTime: string,
    doctorName: string,
    doctorId: number,
    appointmentId: number
}


async function getAppointments(hospital: string, date: string) {
    if (!hospital || !date) return [];
    console.log(`get appointments for ${hospital} on ${date}`)
    try{
        const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/appointment/list?date=${date}&hospital=${hospital}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
        })

        const data = await response.json();
        return data
    } catch (error) {
        console.error('Error during getting available time slots:', error);
    }
}

async function getHospitalList() {
    try {
        const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/hospital/list`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
        })
        const data = await response.json();
        return data
    } catch (error) {
        console.log('Error fetching hospitals:', error);
    }
}

type Props = {}

const page = (props: Props) => {
    const router = useRouter();
    const pathname = usePathname();
    const searchParams = useSearchParams();
    const [isModalOpen, setModalOpen] = useState(false);
    const [hospital, setHospital] = useState(searchParams.get('hospital') || '');
    const [date, setDate] = useState(searchParams.get('date') || '');

    const { data: hospitals } = useQuery('hospitals', getHospitalList)
    const { data: appointments, refetch: refreshAppointments } : {
        data: Appointment[] | undefined,
        refetch: () => void 
    } = useQuery('appointments', 
        () => getAppointments(hospital, date), {
            enabled: !!hospital && !!date
        })
    
    useEffect(() => {
        if (hospital && date) {
            router.push(`${pathname}?hospital=${hospital}&date=${date}`)
        }
    }, [hospital, date]);
    
    useEffect(() => {
        refreshAppointments();
    }, [searchParams]);

    return (
        <>
            <div>
                {isModalOpen && (
                    <div className="modal modal-open">
                        <div className="modal-box">
                            <h3 className="text-lg font-bold">Confirmation</h3>
                            <p className="py-4">Your appointment is booked!</p>
                            <div className="modal-action">
                                <label htmlFor="my_modal_7" className="btn" onClick={() => setModalOpen(false)}>Close</label>
                            </div>
                        </div>
                    </div>
                )}
                <p className='flex flex-row text-2xl w-full h-16 items-center justify-center text-center text-gray-500'>Welcome to booking system</p>
                <div className='flex items-center justify-center gap-4 p-4'>
                    Date
                    <input 
                        onChange={(e) => setDate(e.target.value)}
                        type='date' 
                        className='border-2 rounded-lg'
                        value={date}
                    />
                    <select
                        onChange={(e) => setHospital(e.target.value)}
                        className='border-2 rounded-lg'
                        value={hospital}
                    >
                        <option value="">Select Hospital</option>
                        {hospitals && hospitals.map((h: any) => (
                            <option key={h.name} value={h?.name}>{h.name}</option>
                        ))}
                    </select>
                    <button 
                        onClick={async () => {
                            console.log(`date is ${date}`)
                            console.log(`hospital is ${hospital}`)
                            if (hospital && date) {
                                await refreshAppointments()
                            } else {
                                alert('Please select hospital and date')
                            }
                        }} 
                        className='btn btn-sm text-sm'
                    >
                        Search
                    </button>
                </div>
                <div>
                    <table className="table">
                        <thead>
                        <tr>
                            <th></th>
                            <th>Hospital Name</th>
                            <th>Doctor Name</th>
                            <th>Start Time</th>
                            <th>End Time</th>
                            <th></th>
                        </tr>
                        </thead>
                        <tbody>
                            { appointments && appointments?.map((appointment : any, index : any) => (
                            <TableItem
                                key={index}
                                index={index}
                                appointment={appointment}
                                hospital={hospital}
                                setModalOpen={setModalOpen}
                                refreshAppointments={refreshAppointments}
                            />
                        ))}
                        </tbody>
                    </table>
                </div>
            </div>
        </>
    )
}

export default page