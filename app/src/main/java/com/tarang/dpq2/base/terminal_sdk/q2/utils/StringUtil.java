package com.tarang.dpq2.base.terminal_sdk.q2.utils;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.android.gms.common.util.Hex;

import java.nio.charset.StandardCharsets;

public class StringUtil
{
	private static final String HexChars = "1234567890abcdefABCDEF";
	public static final int LCD_WIDTH = 16;

	/** A table of hex digits */
	public static final char[] hexDigit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

  /**
   * Convert a nibble to a hex character
   * 
   * @param nibble
   *          the nibble to convert.
   */
  public static char toHexChar(int nibble)
  {
    return hexDigit[(nibble & 0xF)];
  }

  /**
  * Method trim space
  *
  * //@param The string to be format.
  *
  */
  public static String trimSpace(String oldString)
  {
    if (null == oldString)
      return null;
    if (0 == oldString.length())
      return "";
      
    StringBuffer sbuf = new StringBuffer();
    int oldLen = oldString.length();
    for(int i = 0; i < oldLen; i++)
    {
      if (' ' != oldString.charAt(i))
        sbuf.append(oldString.charAt(i));
    }     
    String returnString = sbuf.toString();
    sbuf = null;
    return returnString;
  }
   
  /**
  * Method convert byte[] to String
  *
//  * @param The string to be format.
  *
  */
  public static String toString(byte abyte0[])
  {
    if(null == abyte0)
      return null;
    else
      return new String(abyte0);
  }

  /**
  * Method fill string
  *
  * //@param The string to be format.
  *
  */
  public static String fillString(String formatString, int length, char fillChar, boolean leftFillFlag)
  {
    if (null == formatString)
    {
      formatString = "";
    }
    int strLen = formatString.length();
    if (strLen >= length)
    {
      if (true == leftFillFlag)  // left fill 
        return formatString.substring(strLen - length, strLen);
      else
        return formatString.substring(0, length);
    } else
    {
      StringBuffer sbuf = new StringBuffer();
      int fillLen = length - formatString.length();
      for (int i = 0; i < fillLen; i++)
      { 
        sbuf.append(fillChar);
      }
      
      if (true == leftFillFlag)  // left fill 
      {
        sbuf.append(formatString);
      } else
      {
        sbuf.insert(0, formatString);
      }
      String returnString = sbuf.toString();
      sbuf = null;
      return returnString;
    }
  }

  /**
  * Method fill string
  *
  * @param The string to be format.
  *
  */
  public static String fillSpace(String formatString, int length)
  {
    return fillString(formatString, length, ' ', false);
  }


  /**
  * Method Format string
  *
  * @param The string to be format.
  *
  */
  public static String fillZero(String formatString, int length)
  {
    return fillString(formatString, length, '0', true);
  }

  /**
       * @param s source string (with Hex representation)
       * @return byte array
       */
  public static byte[] hexString2bytes (String s)
  {
    if (null == s)
      return null;
  
    s = trimSpace(s);
    
    if (false == isHexChar(s, false))
      return null;
      
    return hex2byte (s, 0, s.length() >> 1);
  }


  public static byte[] intToByte4(int number)
  {
    int tmp_num = number;
    byte[] byte4 = new byte[4];

    for (int i = byte4.length - 1; i > -1; i--)
    {
      byte4[i] = (byte)(tmp_num & 0xff);
      tmp_num = tmp_num >> 8;
    }
    return byte4;
  }

  /**
   * @param   s       source string
   * @param   offset  starting offset
   * @param   len     number of bytes in destination (processes len*2)
   * @return  byte[len]
   */
  public static byte[] hex2byte (String s, int offset, int len) {
      byte[] d = new byte[len];
      int byteLen = len * 2;
      for (int i=0; i < byteLen; i++) {
        int shift = (i%2 == 1) ? 0 : 4;
        d[i>>1] |= Character.digit(s.charAt(offset+i), 16) << shift;
      }
      return d;
  }
  
  private static void appendHex(StringBuffer stringbuffer, byte byte0)
  {
    stringbuffer.append(toHexChar(byte0 >> 4));
    stringbuffer.append(toHexChar(byte0));
  }

  public static String toHexString(byte abyte0[], int beginIndex, int endIndex, boolean spaceFlag)
  {
    if(null == abyte0)
      return null;
    if(0 == abyte0.length)
      return "";
    StringBuffer sbuf = new StringBuffer();
    appendHex(sbuf, abyte0[beginIndex]);
    for(int i = (beginIndex + 1); i < endIndex; i++)
    {
      if (spaceFlag)
        sbuf.append(" ");
      appendHex(sbuf, abyte0[i]);
    }
    String returnString = sbuf.toString();
    sbuf = null;
    return returnString;
  }

  public static String toHexString(byte abyte0[], boolean spaceFlag)
  {
    if(null == abyte0)
      return null;
    return toHexString(abyte0, 0, abyte0.length, spaceFlag);
  }

  /**
  * Method Check String 
  *
  * @param The string to be format.
  *
  */  
  public static boolean isHexChar(String hexString, boolean trimSpaceFlag)
  {
    if (null == hexString || 0 == hexString.length())
      return false;
  
    if (trimSpaceFlag)
      hexString = trimSpace(hexString);
      
    if (hexString.length() % 2 != 0)
      return false;
    int hexLen = hexString.length();
    for(int i = 0; i < hexLen; i++)
    {
      if (HexChars.indexOf(hexString.charAt(i)) < 0)
        return false;
    }
    
    return true;
  }
  public static boolean isHexChar(String hexString)
  {
    return isHexChar(hexString, true);
  }


/*  @RequiresApi(api = Build.VERSION_CODES.KITKAT)
  public static String convertStringToHex(String str) {

    // display in uppercase
    //char[] chars = Hex.encodeHex(str.getBytes(StandardCharsets.UTF_8), false);

    // display in lowercase, default
    char[] chars = Hex.encodeHex(str.getBytes(StandardCharsets.UTF_8));


    return String.valueOf(chars);
  }

  @RequiresApi(api = Build.VERSION_CODES.KITKAT)
  public static String convertHexToString(String hex) {

    String result = "";
    try {
      byte[] bytes = Hex.decodeHex(hex);
      result = new String(bytes, StandardCharsets.UTF_8);
    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid Hex format!");
    }
    return result;
  }*/
 /* public static String convertStringToHex(String str) {

    StringBuffer hex = new StringBuffer();

    // loop chars one by one
    for (char temp : str.toCharArray()) {

      // convert char to int, for char `a` decimal 97
      int decimal = (int) temp;

      // convert int to hex, for decimal 97 hex 61
      hex.append(Integer.toHexString(decimal));
    }

    return hex.toString();

  }*/

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


  public static byte[] hexStringToByteArray(String s) {
    int len = s.length();
    byte[] data = new byte[len / 2];
    for (int i = 0; i < len; i += 2) {
      data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
              + Character.digit(s.charAt(i+1), 16));
    }
    return data;
  }

}
