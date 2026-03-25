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
            setError('Lösenorden matchar inte.');
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
                setMessage('Lösenordet har ändrats! Du skickas till inloggningen om 3 sekunder...');
                setTimeout(() => navigate('/auth'), 3000);
            } else {
                setError(data.error || 'Kunde inte återställa lösenordet.');
            }
        } catch (err) {
            setError('Något gick fel vid anslutningen.');
        }
    };

    return (
        <div style={{ padding: '20px', textAlign: 'center' }}>
            <h2>Välj nytt lösenord</h2>
            {message && <p style={{ color: 'green' }}>{message}</p>}
            {error && <p style={{ color: 'red' }}>{error}</p>}

            <form onSubmit={handleSubmit}>
                <input
                    type="password"
                    placeholder="Nytt lösenord"
                    value={newPassword}
                    onChange={(e) => setNewPassword(e.target.value)}
                    required
                    style={{ padding: '10px', margin: '5px', width: '300px' }}
                />
                <br />
                <input
                    type="password"
                    placeholder="Bekräfta nytt lösenord"
                    value={confirmPassword}
                    onChange={(e) => setConfirmPassword(e.target.value)}
                    required
                    style={{ padding: '10px', margin: '5px', width: '300px' }}
                />
                <br />
                <button type="submit" style={{ padding: '10px 20px', marginTop: '10px' }}>
                    Spara nytt lösenord
                </button>
            </form>
        </div>
    );
};

export default ResetPassword;