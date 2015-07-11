import java.io.File;
import java.io.FileInputStream;//�ֽ�
//import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
//import java.util.ArrayList;
//import java.util.List;
//import java.io.*;

public class decrypt_File {
	public String test;
	
	public static void main(String[] args)	throws IOException	//IOException inside
	{
		String keyPath = "//root//keyAES";
		
		String Path = args[0];		
		DecryptFile(Path, keyPath);
		
		
	};
	
	private static boolean DecryptFile(String inputFilePath, String keyPath )throws IOException
	{
		byte[] key;
		byte[] decrypted;
		String encryptedFilePath = inputFilePath + ".encryped";	
		String decryptedFilePath = inputFilePath + ".decrypted."+inputFilePath.substring(inputFilePath.length()-3);
		System.out.println("---decrypt begin-----"+decryptedFilePath);
		
		OutputStream out;
		InputStream in;
		
		File f = null;
		
		//donnot delete ; others will use it 
		f = new File(keyPath);
		if(!f.exists())
		{
			System.out.println("cannot find key with "+ keyPath);
			return false;
		}
			
		in = new FileInputStream(keyPath);
		key = new byte[in.available()];
		in.read(key);
		in.close();
		
		
		
		int offset = 0;
		long blockNum = 0;

		f = new File(encryptedFilePath);
		long fileLength = 0;
		fileLength = f.length();
		boolean flag = false;
		
		if(0 != fileLength % 524304)	//512*1024<->524304
		{
			blockNum = 1 + fileLength / 524304;
//			blockNum = 1 + fileLength / 102416;
		}
		else
		{
			blockNum = fileLength / 524304;
			flag = true;
		}
		
		
		/*decrypt*/
		in = new FileInputStream(encryptedFilePath);
		out = new FileOutputStream(decryptedFilePath, false);
		
		byte[] encryptedFont;
		byte[] encryptedEnd;
		int encryptFrontLength = -1;
		int encryptEndLength = -1;
				
		/*time*/
		long lStart = 0;
		long lUseTime = 0;
		lStart = System.currentTimeMillis();
		
		if(1 == blockNum)
		{
			encryptEndLength = (int)fileLength;
			encryptedEnd = new byte[encryptEndLength];
			offset = in.read(encryptedEnd);
			decrypted = AES.Decrypt(encryptedEnd, key);
			out.write(decrypted);
			out.flush();
		}
		else
		{
			for(int i = 1; i < blockNum; i++)
			{
				encryptFrontLength = 524304;
				encryptedFont = new byte[encryptFrontLength];
				
				offset = in.read(encryptedFont);
				decrypted = AES.Decrypt(encryptedFont, key);
				out.write(decrypted);
				out.flush();
			}
			
			if(flag)
			{
				encryptEndLength = 524304;
			}
			else
			{
				encryptEndLength = (int)fileLength % 524304;
			}
			
			encryptedEnd = new byte[encryptEndLength];
			offset = in.read(encryptedEnd);
			decrypted = AES.Decrypt(encryptedEnd, key);
			out.write(decrypted);
			out.flush();
			
		}
			
		in.close();
		out.close();
		
		lUseTime = System.currentTimeMillis() - lStart;
		System.out.println(blockNum + "blocks, it takes " + lUseTime + " milliseconds");
		
		System.out.println("---decrypt success---"+decryptedFilePath);
		return true;
	}

}

