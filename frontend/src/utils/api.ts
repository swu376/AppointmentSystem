export const API_URL = process.env.NEXT_PUBLIC_API_URL;

export const signup = async (username: string, password: string) => {
    const response = await fetch(`${API_URL}/auth/signup`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ username, password }),
    });
    if (!response.ok) {
        throw new Error('Signup request failed');
    }
    return true;
}

export const isSignedIn = async () => {
    try {
        const token = localStorage.getItem('token');
        if (!token) return false;

        const response = await fetch(`${API_URL}/auth/checkToken`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
            },
        });

        if (!response.ok) {
            localStorage.removeItem('token');
            return false;
        }
        console.log(`response is ${JSON.stringify(response)}`)

        const data = await response.json();
        return !!data;
    } catch (error) {
        console.error(error);
        return false;
    }
}