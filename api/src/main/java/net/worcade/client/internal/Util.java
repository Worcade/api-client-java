// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.internal;

import com.google.common.collect.ImmutableList;
import com.google.common.net.UrlEscapers;
import lombok.experimental.UtilityClass;
import net.worcade.client.Result;

import javax.crypto.Cipher;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

@UtilityClass
class Util {
    private static final Base64.Encoder ENCODER = Base64.getEncoder();
    private static final Base64.Decoder DECODER = Base64.getDecoder();

    static String escapeUrlQueryParameter(String parameter) {
        return UrlEscapers.urlFormParameterEscaper().escape(parameter);
    }

    static String escapeUrlPathSegment(String parameter) {
        return UrlEscapers.urlPathSegmentEscaper().escape(parameter);
    }

    static String encodeBase64(String payload) {
        return withStringBytes(payload, ENCODER::encode);
    }

    static String decodeBase64(String payload) {
        return withStringBytes(payload, DECODER::decode);
    }

    static Result<String> encryptAndEncode(String payload, PublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return Result.ok(withStringBytes(payload, b -> ENCODER.encode(cipher.doFinal(b))));
        }
        catch (GeneralSecurityException e) {
            return Result.failed(ImmutableList.of(new Result.Message(null, "Encryption failed with " + e.getMessage())));
        }
    }

    static Result<String> decodeAndDecrypt(String payload, PrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return Result.ok(withStringBytes(payload, b -> cipher.doFinal(DECODER.decode(b))));
        }
        catch (GeneralSecurityException e) {
            return Result.failed(ImmutableList.of(new Result.Message(null, "Decryption failed with " + e.getMessage())));
        }
    }

    private static <E extends Exception> String withStringBytes(String payload, ThrowingFunction<byte[], byte[], E> transformation)  throws E {
        return new String(transformation.apply(payload.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
    }

    static Result<RSAPublicKeySpec> getPublicKeySpec(PublicKey publicKey) {
        try {
            return Result.ok(KeyFactory.getInstance("RSA").getKeySpec(publicKey, RSAPublicKeySpec.class));
        }
        catch (GeneralSecurityException e) {
            return Result.failed(ImmutableList.of(new Result.Message(null, "Creating public key spec failed with " + e.getMessage())));
        }
    }

    static Result<PublicKey> createPublicKey(BigInteger modulus, BigInteger exponent) {
        try {
            return Result.ok(KeyFactory.getInstance("RSA").generatePublic(new RSAPublicKeySpec(modulus, exponent)));
        }
        catch (GeneralSecurityException e) {
            return Result.failed(ImmutableList.of(new Result.Message(null, "Creating public key failed with " + e.getMessage())));
        }
    }

    private interface ThrowingFunction<F, T, E extends Exception> {
        T apply(F from) throws E;
    }
}
