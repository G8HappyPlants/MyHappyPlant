import React, { useState } from 'react';

const ForgotPassword = () => {
    const [email, setEmail] = useState('');
    const [message, setMessage] = useState('');
    const [error, setError] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        setMessage('');
        setError('');

        try {
            const response = await fetch('http://localhost:8080/api/password/forgot', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({email}),
            });

            const data = await response.json();

            if (response.ok) {
                setMessage(data.message);
            } else {
                setError(data.error || 'Something went wrong.');
            }
        } catch (err) {
            setError('Could not connect to the server.');
        }
    };
    return (
        <div style={{ padding: '20px', textAlign: 'center' }}>
            <h2>Forgot Password?</h2>
            <p>Enter your email address and we'll send you a reset link.</p>

            {message && <p style={{ color: 'green' }}>{message}</p>}
            {error && <p style={{ color: 'red' }}>{error}</p>}

            <form onSubmit={handleSubmit}>
                <input
                    type="email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    placeholder="Your email address"
                    required
                    style={{ padding: '10px', margin: '10px 0', width: '300px' }}
                />
                <br />
                <button type="submit" style={{ padding: '10px 20px' }}>
                    Send reset link
                </button>
            </form>
        </div>
    );
};

export default ForgotPassword;

