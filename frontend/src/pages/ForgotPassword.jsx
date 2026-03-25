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
                body: JSON.stringify({email}), // Skickar e-posten som JSON
            });

            const data = await response.json();

            if (response.ok) {
                setMessage(data.message);
            } else {
                setError(data.error || 'Något gick fel.');
            }
        } catch (err) {
            setError('Kunde inte ansluta till servern.');
        }
    };
    return (
        <div style={{ padding: '20px', textAlign: 'center' }}>
            <h2>Glömt Lösenord?</h2>
            <p>Fyll i din e-postadress så skickar vi en återställningslänk.</p>

            {message && <p style={{ color: 'green' }}>{message}</p>}
            {error && <p style={{ color: 'red' }}>{error}</p>}

            <form onSubmit={handleSubmit}>
                <input
                    type="email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    placeholder="Din e-postadress"
                    required
                    style={{ padding: '10px', margin: '10px 0', width: '300px' }}
                />
                <br />
                <button type="submit" style={{ padding: '10px 20px' }}>
                    Skicka återställningslänk
                </button>
            </form>
        </div>
    );
};

export default ForgotPassword;

