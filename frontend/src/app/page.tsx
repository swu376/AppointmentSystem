'use client'
import { useAuth } from "@/context/AuthContext";
import { useQuery } from "react-query";
import { Appointment } from "./appointment/page";
import AppointmentRow from "./components/AppointmentRow";

async function fetchAppointmentsHistory() {
  const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/appointment/history`, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      "Authorization": `Bearer ${localStorage.getItem("token")}`
    },
  });
  return response.json();
} 

export default function Home() {
  const { isAuthenticated } = useAuth();
  const { data: appointments, refetch: refreshHistory } : 
    { data: undefined | Appointment[], refetch: () => void }  = useQuery("apointments-history", () => fetchAppointmentsHistory(), {
      enabled: isAuthenticated,
      refetchOnMount: 'always',
  })
  return (
    <main className="flex items-center justify-center w-full h-full flex-grow">
      <div className="bg-gray-100 w-full h-full flex flex-col gap-2 p-3 shadow-md">
        {isAuthenticated? (
          <>
            <div className="flex items-center justify-center text-2xl font-bold">
              View Your Appointments History
            </div>
            <div className="overflow-x-auto w-full h-full">
              <table className="table">
                {/* head */}
                <thead>
                  <tr>
                    <th>AppointmentID</th>
                    <th>Hospital</th>
                    <th>Doctor</th>
                    <th>Start Time</th>
                    <th>End Time</th>
                  </tr>
                </thead>
                <tbody>
                  {appointments?.map((appointment, index) => (
                    <AppointmentRow 
                      key={index} 
                      appointment={appointment} 
                      refreshHistory={refreshHistory}
                    />
                  ))}
                </tbody>
              </table>
            </div>
          </>
        ) : (
          <div>
          </div>
        )}
      </div>
    </main>
  );
}
