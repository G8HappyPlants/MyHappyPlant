package com.example.myhappyplants.auxillary;

import com.example.myhappyplants.service.CryptoService;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Optional;

@Converter
public class StringCryptograhicConverter implements AttributeConverter<String, String> {
    private final CryptoService cryptoService;

    public StringCryptograhicConverter(CryptoService cryptoService) {
        this.cryptoService = cryptoService;
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        return Optional.ofNullable(attribute).map(cryptoService::encrypt).orElse(null);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return Optional.ofNullable(dbData).map(cryptoService::decryptToString).orElse(null);
    }
}
