import React, { useState } from 'react';
import { useSearchParams, useNavigate } from 'react-router-dom';

const ResetPassword = () => {
    const [searchParams] = useSearchParams();
    const navigate = useNavigate();
    const token = searchParams.get('token');

    const [newPassword, setNewPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [message, setMessage] = useState('');
    const [error, setError] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (newPassword !== confirmPassword) {
            setError('Passwords do not match.');
            return;
        }

        try {
            const response = await fetch('http://localhost:8080/api/password/reset', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    token: token,
                    newPassword: newPassword
                }),
            });

            const data = await response.json();

            if (response.ok) {
                setMessage('Password updated! Redirecting to login in 3 seconds...');
                setTimeout(() => navigate('/auth'), 3000);
            } else {
                setError(data.error || 'Could not reset password.');
            }
        } catch (err) {
            setError('Something went wrong with the connection.');
        }
    };

    return (
        <div style={{ padding: '20px', textAlign: 'center' }}>
            <h2>Choose a new password</h2>
            {message && <p style={{ color: 'green' }}>{message}</p>}
            {error && <p style={{ color: 'red' }}>{error}</p>}

            <form onSubmit={handleSubmit}>
                <input
                    type="password"
                    placeholder="New password"
                    value={newPassword}
                    onChange={(e) => setNewPassword(e.target.value)}
                    required
                    style={{ padding: '10px', margin: '5px', width: '300px' }}
                />
                <br />
                <input
                    type="password"
                    placeholder="Confirm new password"
                    value={confirmPassword}
                    onChange={(e) => setConfirmPassword(e.target.value)}
                    required
                    style={{ padding: '10px', margin: '5px', width: '300px' }}
                />
                <br />
                <button type="submit" style={{ padding: '10px 20px', marginTop: '10px' }}>
                    Save new password
                </button>
            </form>
        </div>
    );
};

export default ResetPassword;