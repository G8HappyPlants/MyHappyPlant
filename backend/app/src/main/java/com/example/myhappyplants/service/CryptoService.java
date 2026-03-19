package com.example.myhappyplants.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Locale;

@Service
public class CryptoService {
    private static final Logger log = LoggerFactory.getLogger(CryptoService.class);
    private final SecretKey AES_KEY;
    private final SecretKey HMAC_KEY;

    private static final int IV_LENGTH = 12;

    private static final String ENCRYPT_DECRYPT_ALGO = "AES";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";

    private static final String HASH_ALGO = "HmacSHA256";

    public SecureRandom secureRandom = new SecureRandom();


    public CryptoService() {
        String db_aes_key = System.getenv("DB_AES_KEY");
        String db_sha256_hmac_key = System.getenv("DB_SHA256_HMAC_KEY");

        if (db_aes_key == null) {
            log.warn("Missing \"DB_AES_KEY\" environment variable, assuming test environment");
            log.warn("A one-time DB_AES_KEY will be generated for this session");

			try {
				KeyGenerator keyGen = KeyGenerator.getInstance(ENCRYPT_DECRYPT_ALGO);
                keyGen.init(256,secureRandom);

                AES_KEY = keyGen.generateKey();
			} catch (NoSuchAlgorithmException e) {
				throw new RuntimeException(e);
			}
		} else {
            AES_KEY = new SecretKeySpec(Base64.getDecoder().decode(db_aes_key), ENCRYPT_DECRYPT_ALGO);
        }

        if (db_sha256_hmac_key == null) {
            log.warn("Missing \"DB_SHA256_HMAC_KEY\" environment variable, assuming test environment");
            log.warn("A one-time DB_SHA256_HMAC_KEY will be generated for this session");

            try {
                KeyGenerator keyGen = KeyGenerator.getInstance(HASH_ALGO);
                keyGen.init(256,secureRandom);

                HMAC_KEY = keyGen.generateKey();
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            HMAC_KEY = new SecretKeySpec(Base64.getDecoder().decode(db_sha256_hmac_key), "HmacSHA256");
        }
    }

    public String hash(String data) {
        return hash(data.getBytes(StandardCharsets.UTF_8));
    }

    public String hash(final byte[] data) {
        try {
            Mac mac = Mac.getInstance(HASH_ALGO);
            mac.init(HMAC_KEY);

            return Base64.getEncoder().encodeToString(mac.doFinal(data));
        } catch (InvalidKeyException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private String encrypt(final byte data) { // lol why would you use this
        return encrypt(new byte[]{data});
    }

    public String encrypt(final short data) {
        return encrypt(ByteBuffer.allocate(2).putShort(data).array());
    }

    public String encrypt(final int data) {
        return encrypt(ByteBuffer.allocate(4).putInt(data).array());
    }

    public String encrypt(final long data) {
        return encrypt(ByteBuffer.allocate(8).putLong(data).array());
    }

    public String encrypt(final String data) {
        return encrypt(data.getBytes(StandardCharsets.UTF_8));
    }

    public String encrypt(final Serializable data) {
        return encrypt(objectToBytes(data));
    }


    public String encrypt(final byte[] rawData) {
        try {
            byte[] init_vector = new byte[IV_LENGTH];
            secureRandom.nextBytes(init_vector);

            byte[] encryptedBytes = obtainChiper(init_vector, Cipher.ENCRYPT_MODE).doFinal(rawData);

            ByteBuffer buf = ByteBuffer.allocate(init_vector.length + encryptedBytes.length);
            buf.put(init_vector);
            buf.put(encryptedBytes);

            return Base64.getEncoder().encodeToString(buf.array());
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            log.error("An exception occured while attempting to decrypt: {}", String.valueOf(e));
            log.error("Information may have been discarded.");

            throw new RuntimeException(e);
        }
    }

    public String decryptToString(final String base64encryptedData) {
        return new String(decrypt(base64encryptedData), StandardCharsets.UTF_8);
    }

    public byte decryptToByte(final String base64encryptedData) {
        return decrypt(base64encryptedData)[0];
    }

    public short decryptToShort(final String base64encryptedData) {
        return ByteBuffer.allocate(2).put(decrypt(base64encryptedData)).getShort();
    }

    public int decryptToInt(final String base64encryptedData) {
        return ByteBuffer.allocate(4).put(decrypt(base64encryptedData)).getInt();
    }

    public long decryptToLong(final String base64encryptedData) {
        return ByteBuffer.allocate(8).put(decrypt(base64encryptedData)).getLong();
    }

    public <T extends Serializable> T decryptToObject(final String base64encryptedData) {
        return bytesToObject(decrypt(base64encryptedData));
    }

    public byte[] decrypt(final String base64encryptedData) {
        try {
            byte[] encryptedData = Base64.getDecoder().decode(base64encryptedData);
            ByteBuffer encryptedDataBuffer = ByteBuffer.wrap(encryptedData);

            byte[] initializationVector = new byte[12];
            encryptedDataBuffer.get(initializationVector);

            byte[] encryptedBytes = new byte[encryptedDataBuffer.remaining()];
            encryptedDataBuffer.get(encryptedBytes);

            return obtainChiper(initializationVector, Cipher.DECRYPT_MODE).doFinal(encryptedBytes);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            log.error("An exception occured while attempting to decrypt: {}", String.valueOf(e));
            log.error("Information may have been discarded.");

            throw new RuntimeException(e);
        }
    }


    private Cipher obtainChiper(final byte[] iv, final int cipherMode) {
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(cipherMode, AES_KEY, new GCMParameterSpec(128, iv));

            return cipher;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] objectToBytes(final Serializable object) {
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        try (ObjectOutputStream str = new ObjectOutputStream(byteOutput)) {
            str.writeObject(byteOutput);
            str.flush();
            return byteOutput.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> T bytesToObject(final byte[] bytes) {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);

        try (ObjectInput in = new ObjectInputStream(bis)) {
            return (T) in.readObject();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
