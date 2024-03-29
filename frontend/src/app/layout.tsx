import type { Metadata } from "next";
import { Inter } from "next/font/google";
import "./globals.css";
import Navbar from "./components/Navbar";
import QueryClientProvider from "@/providers/QueryClientProvider";
import { AuthProvider } from "@/context/AuthContext";

const inter = Inter({ subsets: ["latin"] });

export const metadata: Metadata = {
  title: "Create Next App",
  description: "Generated by create next app",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <AuthProvider>
        <QueryClientProvider>
          <body className={inter.className + " flex flex-col w-screen h-screen"}>
            <Navbar />
            <div className="flex-grow">
              {children}
            </div>
          </body>
        </QueryClientProvider>
      </AuthProvider>
    </html>
  );
}
