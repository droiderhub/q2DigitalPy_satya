package com.tarang.dpq2.base.jpos_class;

import com.tarang.dpq2.base.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;


public class ByteConversionUtils {

    /**
     * Method to convert int to a byte[2] array
     *
     * @param value value to be converted
     * @return byte array
     */
    public static byte[] intToByteArray(int value) {
        byte[] bytes = new byte[2];
        for (int i = 0, shift = 8; i < 2; i++, shift -= 8)
            bytes[i] = (byte) (0xFF & (value >> shift));
        return bytes;
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    /**
     * Method to convert integer to Byte array
     *
     * @param value  value to be converted
     * @param length length of byte array
     * @return byte array
     */
    public static byte[] intToByteArray(int value, int length) {
        byte[] bytes = new byte[length];
        for (int i = 0, shift = 24; i < length; i++, shift -= 8) {
            bytes[i] = (byte) (0xFF & (value >> shift));
        }
        return bytes;
    }

    /**
     * Method to extract int from byte array
     *
     * @param ba byte array
     * @return int value represented by byte array
     */
    public static int getIntFromByteArray(byte[] ba) {
        int intValue = 0;

        for (int j = 0; j < 2; j++) {
            int temp = ba[j];
            if (temp < 0) {
                // Since we support only 2 bytes
                temp += 256;
            }
            intValue = (intValue <<= 8) + temp;
        }
        return intValue;
    }

    /**
     * Method for setting even parity bit
     * <p>
     * //@param bytes
     * byte array
     *
     * @param length length of byte array
     * @return void
     */
    public static void setEvenParity(byte[] data, int length) {
        // iterate through the byte array
        for (int i = 0; i < length; i++) {
            // count number of bits set
            int count = 0;
            for (int j = 0; j < 7; j++)
                if (((data[i] >> j) & 1) == 1)
                    count++;
            // check if count is even, if not set the parity bit
            if ((count % 2) != 0)
                data[i] |= 0x80;
        }
    }

    /**
     * Method to convert 7 bit parity data to 8 bit no parity
     *
     * @param bytes  byte array
     * @param length length of byte array
     * @return void
     */
    public static void clearParityBit(byte[] bytes, int length) {
        for (int i = 0; i < length; i++)
            bytes[i] = (byte) (bytes[i] & 0x7F);
    }

    /**
     * Method to extract int from byteArray
     *
     * @param ba     byte array
     * @param length length of returned byte array
     * @return int value
     */
    public static int getIntFromByteArray(byte[] ba, int length) {
        int intValue = 0;
        for (int j = 0; j < length; j++) {
            intValue = (intValue <<= 8) + ba[j];
        }
        return intValue;
    }

    /**
     * Method to convert long to byte array
     *
     * @param value long value to be converted
     * @return byte array
     */
    public static byte[] longToByteArray(long value) {
        System.out.print(value + "-->  ");
        byte[] bytes = new byte[8];
        for (int i = 0, shift = 56; i < 8; i++, shift -= 8)
            bytes[i] = (byte) (0xFF & (value >> shift));
        return bytes;
    }

    /**
     * Method to extract long from byte array
     *
     * @param ba byte array
     * @return long value
     */
    public static long getLongFromByteArray(byte[] ba) {
        long i = 0;
        for (int j = 0; j < 8; j++) {
            i = (i <<= 8) + ba[j];
        }
        return i;
    }

    /**
     * Method to convert 1 byte to hex string. Handle leading zeroes as necessary
     *
     * @param b byte to be converted
     * @return string representation
     */
    public static String byteToHexString(byte b) {
        String hex = Integer.toString((b & 0xff) + 0x100, 16).substring(1);
        return hex;
    }

    /**
     * Method to convert array of bytes to a hex string without '|'
     *
     * @param b byte array
     * @return return hex string representation
     */
    public static String byteArrayToHexString(byte[] b, int length, boolean withDelimitter) {
        char[] hexChar = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        StringBuffer sb = new StringBuffer(length * 2);
        if (withDelimitter)
            sb.append('|');

        for (int i = 0; i < length; i++) {
            // look up high nibble char
            sb.append(hexChar[(b[i] & 0xf0) >>> 4]);
            // look up low nibble char
            sb.append(hexChar[b[i] & 0x0f]);
            if (withDelimitter)
                sb.append('|');
        }
        return sb.toString();
    }
    public static String byteArrayToHexString(byte[] b) {
        int length = b.length;
        boolean withDelimitter = false;
        char[] hexChar = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        StringBuffer sb = new StringBuffer(length * 2);
        if (withDelimitter)
            sb.append('|');

        for (int i = 0; i < length; i++) {
            // look up high nibble char
            sb.append(hexChar[(b[i] & 0xf0) >>> 4]);
            // look up low nibble char
            sb.append(hexChar[b[i] & 0x0f]);
            if (withDelimitter)
                sb.append('|');
        }
        return sb.toString();
    }

    /**
     * This method used for printing the byte array into String format.
     *
     * @param byteArray
     * @param withDelimitter
     * @param bitLength
     * @return String
     */
    public static String printByteArrayToHexString(byte[] byteArray, boolean withDelimitter, int bitLength) {
        if (null == byteArray || byteArray.length == 0)
            return "";
        int length = byteArray.length;
        char[] hexChar = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        StringBuffer sb = new StringBuffer(length * 2);
        for (int i = 0; i < length; i++) {
            if (withDelimitter) {
                if ((i == 0 || (i % bitLength) == 0)) {
                    sb.append("\n");
                    sb.append(i / 2 + " : ");
                }
                if (i % 2 == 0) {
                    sb.append("  ");
                } else {
                    sb.append(" ");
                }
            }
            sb.append(hexChar[(byteArray[i] & 0xf0) >>> 4]);
            // look up low nibble char
            sb.append(hexChar[byteArray[i] & 0x0f]);
        }
        return sb.toString();
    }

    /***
     * Converts an String representing hexidecimal values into an array of bytes
     * of those same values. The returned array will be half the length of the
     * passed array, as it takes two characters to represent any given byte. An
     * exception is thrown if the passed char array has an odd number of elements.
     *
     * @param hexString
     *          String containing hexidecimal digits
     * @return A byte array containing binary data decoded from the supplied char
     *         array.
     */
    public static byte[] HexStringToByteArray(String hexString) {
        char[] data = null;
        data = hexString.toCharArray();
        int len = hexString.length();
        if ((len & 0x01) != 0) {
            System.err.println("Odd Number of Characters");
        }
        byte[] out = new byte[len >> 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; j < len; i++) {
            int f = toDigit(data[j], j) << 4;
            j++;
            f = f | toDigit(data[j], j);
            j++;
            out[i] = (byte) (f & 0xFF);
        }
        return out;
    }

    /***
     * Converts a hexadecimal character to an integer.
     *
     * @param ch
     *          A character to convert to an integer digit
     * @param index
     *          The index of the character in the source
     * @return An integer
     */
    protected static int toDigit(char ch, int index) {
        int digit = Character.digit(ch, 16);
        if (digit == -1) {
            System.err.println("Illegal hexadecimal charcter " + ch + " at index " + index);
        }
        return digit;
    }

    /**
     * Method to convert array of bytes to an ASCII string
     *
     * @param bytes  byte array
     * @param length
     * @return ASCII string
     */
    public static String byteArrayToString(byte[] bytes, int length) {
        // initialize byte string
        StringBuffer buffer = new StringBuffer(length);

        // iterate through bytes in message
        String cString = null;
        for (int i = 0; i < length; i++) {
            // check for control characters
            if ((cString = getControlChar(bytes[i])) != null) {
                buffer.append(cString);
            } else {
                // convert to char and add to string
                buffer.append((char) bytes[i]);
            }
        }
        return buffer.toString();
    }

    /**
     * Method to return byte for an integer
     * <p>
     * // *@param buffer
     * byte buffer
     *
     * @return int length
     */
    public static byte intToByte(int number) {
        byte intByte;
        Integer intInteger = new Integer(number);
        intByte = intInteger.byteValue();
        return intByte;
    }

    /**
     * Method to return control character string representation
     *
     * @param b ASCII byte
     * @return String representation or null if not control character
     */
    public static String getControlChar(byte b) {
        int NUM_CONTROL_CHARS = 32;
        String[] ControlChars = {"NUL",
                "SOH",
                "STX",
                "ETX",
                "EOT",
                "ENQ",
                "ACK",
                "BEL",
                "BS",
                "HT",
                "LF",
                "VT",
                "FF",
                "CR",
                "SO",
                "SI",
                "DLE",
                "DC1",
                "DC2",
                "DC3",
                "DC4",
                "NAK",
                "SYN",
                "ETB",
                "CAN",
                "EM",
                "SUB",
                "ESC",
                "FS",
                "GS",
                "RS",
                "US"};
        if (b >= 0 && b < NUM_CONTROL_CHARS)
            return "<" + ControlChars[b] + ">";
        else
            return null;
    }

    /**
     * This method used for convert string to hex string
     *
     * @param base
     * @return String
     */
    public static String convertStringToHex(String base) {
        StringBuffer buffer = new StringBuffer();
        int intValue;
        for (int x = 0; x < base.length(); x++) {
            int cursor = 0;
            intValue = base.charAt(x);
            String binaryChar = new String(Integer.toBinaryString(base.charAt(x)));
            for (int i = 0; i < binaryChar.length(); i++) {
                if (binaryChar.charAt(i) == '1') {
                    cursor += 1;
                }
            }
            if ((cursor % 2) > 0) {
                intValue += 128;
            }
            buffer.append(Integer.toHexString(intValue));
        }
        return buffer.toString();
    }

    /**
     * Method to convert array of bytes to a hex string
     * <p>
     * // * @param b
     * byte array
     *
     * @return return hex string representation
     */
    public static String convertHexToString(String hex) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < hex.length() - 1; i += 2) {
            // grab the hex in pairs
            String output = hex.substring(i, (i + 2));
            // convert hex to decimal
            int decimal = Integer.parseInt(output, 16);
            // convert the decimal to character
            sb.append((char) decimal);
        }
        return sb.toString();
    }

    /**
     * This method used for generate the random password pin.
     *
     * @param length
     * @return String - random pin
     */
    public static String generateTraceNumber(int length) {
        String charString = "0123456789";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(charString.charAt(rnd.nextInt(charString.length())));
        }
        return sb.toString();
    }

    /**
     * This method will format specific date (YYYYMMDD) for transaction, it will
     * add one day and return the date in YYYYMMDD format.
     * <p>
     * //* @param tranDate
     *
     * @return String
     */
    public static String formatTranDate(String formatDate) {
        String outputDate = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(formatDate);
            outputDate = sdf.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Logger.v("UTC -11--" + outputDate);
        return outputDate;
    }

    public static String formatTranDate(String newFormat,String date,String formatDate1) {
        String outputDate = "";
        try {
            SimpleDateFormat sdfOld = new SimpleDateFormat(formatDate1);
            Date newDate = sdfOld.parse(date);
            newDate.getHours();
            SimpleDateFormat sdf = new SimpleDateFormat(newFormat);
            outputDate = sdf.format(newDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Logger.v("UTC -Date--" + outputDate);
        return outputDate;
    }


    //TODO online changes
    public static String formatTranDateUTC(String formatDate) {
        String outputDate = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MMddHHmmss");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            outputDate = sdf.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Logger.v("UTC ---" + outputDate);
        return outputDate;
    }
    /*public static String formatTranDateUTC(String formatDate) {
        String outputDate = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(formatDate);
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            outputDate = sdf.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Logger.v("UTC ---" + outputDate);
        return outputDate;
    }*/

    /**
     * Method to get Parsed String from response
     *
     * @param value
     * @return String
     */
    public static String getParsedString(String value, String regExpStr) {
        if (value != null && value.length() % 2 == 0) {
            if (value.indexOf(regExpStr) != -1) {
                return (value.substring(value.indexOf(regExpStr)));
            }
        }
        return value;
    }

    public static String changeDateFormat(String date, String format) {
        String finalDate = "";
        if(date != null && date.trim().length() != 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            SimpleDateFormat sdf2 = new SimpleDateFormat(format);
            try {
                finalDate = sdf2.format(sdf.parse(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return finalDate;
    }

    public static String fetchTagValu(byte[] bcdByte, int offset, int length)
    {
        byte[] returnByte = new byte[length * 2];
        byte value;
        for(int i = offset; i < length; i++)
        {
            value = (byte)(bcdByte[i] >> 4 & 0xF);
            if( value > 9){
                returnByte[i*2] = (byte)(value + (byte)0x37);
            }
            else{
                returnByte[i*2] = (byte)(value + (byte)0x30);
            }
            value = (byte)(bcdByte[i] & 0xF);
            if( value > 9){
                returnByte[i*2+1] = (byte)(value + (byte)0x37);
            }
            else{
                returnByte[i*2+1] = (byte)(value + (byte)0x30);
            }
        }
        return new String(removeTailF(returnByte));
    }

    public static byte[] removeTailF(byte[] buffer)
    {
        int length = buffer.length;
        for(; length > 0; length--)
        {
            if(buffer[length - 1] != 'F')
                break;
        }
        if(length == buffer.length)
        {
            return buffer;
        }else{
            byte[] destBuffer = new byte[length];
            System.arraycopy(buffer, 0, destBuffer, 0, length);
            return destBuffer;
        }
    }

}