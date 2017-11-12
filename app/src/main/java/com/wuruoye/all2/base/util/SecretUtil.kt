package com.wuruoye.all2.base.util

import android.util.Base64
import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

/**
 * Created by wuruoye on 2017/11/6.
 * this file is to do
 */
object SecretUtil {
    private val PUBLIC_RSA_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDZUR+ZVrweku0atvEePdp/vDFP\r" +
            "PswzyqPFUJU0TdW6Fj+eLbmYEfjrJkDjZhcoI9MupdP3xbQIkKozlnzNUXQW0j77\r" +
            "h0BLToEEe7gNdN3Ro/QTmE4NxVfkXSBV4GuWNriBzzgratUvAP5K7ZC6hPJr/+a4\r" +
            "sPrBN2SLwIjuPnTU/wIDAQAB\r"
    fun getPublicSecret(content: String): String{
        val buffer = Base64.decode(PUBLIC_RSA_KEY, Base64.DEFAULT)
        val keyFactory = KeyFactory.getInstance("RSA")
        val keySpec = X509EncodedKeySpec(buffer)
        val publicKey = keyFactory.generatePublic(keySpec) as PublicKey

        val cipher = Cipher.getInstance("RSA")
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        val byte = cipher.doFinal(content.toByteArray())
        return Base64.encodeToString(byte, Base64.DEFAULT)
    }
}