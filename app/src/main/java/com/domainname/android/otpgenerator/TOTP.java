/*******************************************************************************
 * Copyright (C) 2008-2012 CEFRIEL
 *  
 * Title to Software and all associated intellectual property rights is retained by 
 * "CEFRIEL Societa' Consortile a Responsabilita' Limitata" located in via Renato
 * Fucini 2, 20133 Milano (Italy)
 *  
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *  	
 * COVERED CODE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT
 * WARRANTY OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING, WITHOUT 
 * LIMITATION, WARRANTIES THAT THE COVERED CODE IS FREE OF DEFECTS, MERCHANTABLE,
 * FIT FOR A PARTICULAR PURPOSE OR NON-INFRINGING. THE ENTIRE RISK AS TO THE
 * QUALITY AND PERFORMANCE OF THE COVERED CODE IS WITH YOU. SHOULD ANY COVERED
 * CODE PROVE DEFECTIVE IN ANY RESPECT, YOU (NOT THE INITIAL DEVELOPER OR ANY
 * OTHER CONTRIBUTOR) ASSUME THE COST OF ANY NECESSARY SERVICING, REPAIR OR
 * CORRECTION.
 ******************************************************************************/
package com.domainname.android.otpgenerator;

import java.lang.reflect.UndeclaredThrowableException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;


/**
 * This an example implementation of the OATH TOTP algorithm. Visit
 * www.openauthentication.org for more information. There have been
 * modifications to work within the mOTP framework for OTPs by Chris Miceli
 * 
 * @author Johan Rydell, PortWise, Inc.
 */
public class TOTP
{
	

	private static String crypto = "HmacSHA512";
	private static int DEFAULT_OTP_LENGTH = 8;
	public static final int DEFAULT_OTP_LENGTH_MOD = 10000;
    private static final int[] DIGITS_POWER
            // 0 1  2   3    4     5      6       7        8
            = { 1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000 };
	
	public static void setDefaultOtpLength(int otpLength) {
		DEFAULT_OTP_LENGTH = DEFAULT_OTP_LENGTH % DEFAULT_OTP_LENGTH_MOD;
		DEFAULT_OTP_LENGTH += (otpLength * DEFAULT_OTP_LENGTH_MOD);
	}
	
	public static int getDefaultOtpLength() {
		return DEFAULT_OTP_LENGTH / DEFAULT_OTP_LENGTH_MOD;
	}

    private TOTP()
    {}

    /**
     * This method uses the JCE to provide the crypto algorithm. HMAC computes a
     * Hashed Message Authentication Code with the crypto hash algorithm as a
     * parameter.
     * 
     * @param keyBytes
     *            the bytes to use for the HMAC key
     * @param text
     *            the message or text to be authenticated.
     */
    private static byte[] hmac_sha(byte[] keyBytes, byte[] text)
    {
        try
        {
            Mac hmac;
            hmac = Mac.getInstance(crypto);
            SecretKeySpec macKey = new SecretKeySpec(keyBytes, "RAW");
            hmac.init(macKey);
            return hmac.doFinal(text);
        } catch (GeneralSecurityException gse)
        {
            throw new UndeclaredThrowableException(gse);
        }
    }

    /**
     * This method converts HEX string to Byte[]
     * 
     * @param hex
     *            the HEX string
     * 
     * @return A byte array
     */
    private static byte[] hexStr2Bytes(String hex)
    {
        // Adding one byte to get the right conversion
        // values starting with "0" can be converted
        byte[] bArray = new BigInteger("10" + hex, 16).toByteArray();

        // Copy all the REAL bytes, not the "first"
        byte[] ret = new byte[bArray.length - 1];
        for (int i = 0; i < ret.length; i++)
            ret[i] = bArray[i + 1];
        return ret;
    }

    public static String gen(String key, int otpLength, long time, int timeInterval)
    {
        long T0 = 0;
//        long X = 60;
        long currentTimestamp = (long) (System.currentTimeMillis() / 1000L);

        String timeStr = "0";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));

//        long T = (testTime - T0) / X;
        long T = (time * timeInterval) / (currentTimestamp - T0);
        timeStr = Long.toHexString(T).toUpperCase();
        while (timeStr.length() < 16)
            timeStr = "0" + timeStr;

        String result = null;
        byte[] hash;

        // Get the HEX in a Byte[]
        byte[] msg = hexStr2Bytes(timeStr);

        // Adding one byte to get the right conversion
        byte[] k = hexStr2Bytes(key);

        hash = hmac_sha(k, msg);

        // put selected bytes into result int
        int offset = hash[hash.length - 1] & 0xf;

        int binary = ((hash[offset] & 0x7f) << 24)
                | ((hash[offset + 1] & 0xff) << 16)
                | ((hash[offset + 2] & 0xff) << 8) | (hash[offset + 3] & 0xff);

        int otp = binary % (int) (Math.pow(10, otpLength % DEFAULT_OTP_LENGTH_MOD));

        result = Integer.toString(otp);
        while (result.length() < (otpLength % DEFAULT_OTP_LENGTH_MOD))
        {
            result = "0" + result;
        }
        return result;
    }
    /**
     * This method generates a TOTP value for the given
     * set of parameters.
     *
     * @param key: the shared secret, HEX encoded
     * @param time: a value that reflects a time
     * @param returnDigits: number of digits to return
     * @param crypto: the crypto function to use
     *
     * @return: a numeric String in base 10 that includes
     */
    public static String generateTOTP(String key, String time,
                                      String returnDigits, String crypto) {
        int codeDigits = Integer.decode(returnDigits).intValue();
        String result = null;

        // Using the counter
        // First 8 bytes are for the movingFactor
        // Compliant with base RFC 4226 (HOTP)
        while (time.length() < 16)
            time = "0" + time;

        // Get the HEX in a Byte[]
        byte[] msg = hexStr2Bytes(time);
        byte[] k = hexStr2Bytes(key);
        byte[] hash = hmac_sha(k, msg);

        // put selected bytes into result int
        int offset = hash[hash.length - 1] & 0xf;

        int binary = ((hash[offset] & 0x7f) << 24) |
                ((hash[offset + 1] & 0xff) << 16) |
                ((hash[offset + 2] & 0xff) << 8) | (hash[offset + 3] & 0xff);

        int otp = binary % DIGITS_POWER[codeDigits];

        result = Integer.toString(otp);

        while (result.length() < codeDigits) {
            result = "0" + result;
        }

        return result;
    }
}