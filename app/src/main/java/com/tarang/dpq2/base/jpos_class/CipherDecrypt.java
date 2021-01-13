package com.tarang.dpq2.base.jpos_class;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.io.UnsupportedEncodingException;
import java.security.*;

public class CipherDecrypt {
   public static void main(String args[]) throws Exception{
	   //Creating a Signature object
      Signature sign = Signature.getInstance("SHA256withRSA");
      
      //Creating KeyPair generator object
      KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
      
      //Initializing the key pair generator
      keyPairGen.initialize(2048);
      
      //Generate the pair of keys
      KeyPair pair = keyPairGen.generateKeyPair();
      
      PublicKey publicKey = pair.getPublic();

      Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

      //Method calls
      String text1 = "Tarang";
      String text2 = "Softwares";
      System.out.println("Original text");
      System.out.println(text1+"\n"+text2);
      byte[] nameEncyrpt1 = encyrpt(cipher,publicKey,text1);
      byte[] nameEncyrpt2 = encyrpt(cipher,publicKey,text2);
      System.out.println("Encyrpted text");
      System.out.println(new String(nameEncyrpt1,"UTF8"));
      System.out.println("Encyrpted text");
      System.out.println(new String(nameEncyrpt2,"UTF8"));
      byte[] nameDecrypt1 = decyrpt(cipher,pair,nameEncyrpt1);
      byte[] nameDecrypt2 = decyrpt(cipher,pair,nameEncyrpt2);
      System.out.println("Decrypted text");
      System.out.println(new String(nameDecrypt1));
      System.out.println("Decrypted text");
      System.out.println(new String(nameDecrypt2));
   }

   private static byte[] encyrpt(Cipher cipher,PublicKey publicKey,String name) throws BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, InvalidKeyException {
      cipher.init(Cipher.ENCRYPT_MODE, publicKey);
      byte[] input = name.getBytes();
      cipher.update(input);
      byte[] cipherText = cipher.doFinal();
      return cipherText;
   }

   private static byte[] decyrpt(Cipher cipher,KeyPair pair,byte[] cipherText) throws BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, InvalidKeyException {
      cipher.init(Cipher.DECRYPT_MODE, pair.getPrivate());
      byte[] decipheredText = cipher.doFinal(cipherText);
      return decipheredText;
   }
}