import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.*;  
import javax.crypto.spec.*;  

/**
 * 
 * @author lixingxing
 * 
 * AES128 
 * 
 *
 */
public class AES {
	
	static final String algorithmStr="AES/ECB/PKCS5Padding";
	
	static private KeyGenerator keyGen;
	
	static private Cipher cipher;
	
	static boolean isInited=false;
	
	static private void init()
	{
		
		try {
			keyGen=KeyGenerator.getInstance("AES");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		keyGen.init(128);
		
		//��ʼ��cipher
		try {
			cipher=Cipher.getInstance(algorithmStr);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		isInited=true;
	}
	
	public static byte[] GenKey()
	{
		if(!isInited)
		{
			init();
		}
		return keyGen.generateKey().getEncoded();
	}
	
	/**
	 * @param content ����
	 * @param keyBytes key
	 */
	public static byte[] Encrypt(byte[] content,byte[] keyBytes)
	{
		byte[] encryptedText=null;
		
		if(!isInited)//Ϊ��ʼ��
		{
			init();
		}
		
		Key key=new SecretKeySpec(keyBytes,"AES");
		
		try {
			cipher.init(Cipher.ENCRYPT_MODE, key);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			encryptedText=cipher.doFinal(content);
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return encryptedText;
	}
	
	//����Ϊbyte[]
	/**
	 * ����
	 * @param content ����
	 * @param keyBytes key
	 * @return ���ܺ������
	 */
	public static byte[] Decrypt(byte[] content,byte[] keyBytes)
	{
		byte[] originBytes=null;
		if(!isInited)
		{
			init();
		}
		
		Key key=new SecretKeySpec(keyBytes,"AES");
		
		try {
			cipher.init(Cipher.DECRYPT_MODE, key);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//����
		try {
			originBytes=cipher.doFinal(content);
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return originBytes;
	}
}

