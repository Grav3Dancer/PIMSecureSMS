package com.example.securesms.Services

import java.security.spec.KeySpec
import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

class EncryptionService {
    companion object {
        private val salt = "ghdsajkdoaskjdlkasd";
        var ips: IvParameterSpec =
            IvParameterSpec(byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));

        fun Encrypt(message: String, specialKey: String): String {
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            val key = GetKey(specialKey);
            cipher.init(Cipher.ENCRYPT_MODE, key, ips);
            val cipherText = cipher.doFinal(message.toByteArray())
            return Base64.getEncoder()
                .encodeToString(cipherText)
        }

        fun Decrypt(message: String, specialKey: String): String {
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            val key = GetKey(specialKey);
            cipher.init(Cipher.DECRYPT_MODE, key, ips);
            val messageBytes = cipher.doFinal(Base64.getDecoder().decode(message.toByteArray()));
            return String(messageBytes);
        }

        private fun GetKey(specialKey: String): SecretKey {
            val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            val spec: KeySpec =
                PBEKeySpec(specialKey.toCharArray(), salt.toByteArray(), 65536, 256);
            val secret: SecretKey = SecretKeySpec(
                factory.generateSecret(spec).getEncoded(), "AES"
            );
            return secret;
        }

        fun CalculateKey(modulo: Int, powerBase: Int, powerExponent: Int): Int {
            return Math.pow(powerBase.toDouble(), powerExponent.toDouble()).toInt() % modulo;
        }
    }
}