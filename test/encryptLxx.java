package encryptLxx;


import java.io.File;
import java.io.FileInputStream;//字节
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
//import java.io.*;

public class encryptLxx {
	public String test;
	
	public static void main(String[] args)	throws IOException	//IOException inside
	{	
		String keyPath = "F:\\key";
		//1
		String inputFilePath1 = "F:\\movie\\创世纪i20.wmv";		
		encryptFile(inputFilePath1, keyPath);
		//2
		String inputFilePath2 = "F:\\movie\\创世纪i21.wmv";		
		encryptFile(inputFilePath2, keyPath);
		//3
		String inputFilePath3 = "F:\\movie\\创世纪i32.wmv";				
		encryptFile(inputFilePath3, keyPath);
	}
	
	public static void encryptFile(String inputFilePath, String keyPath) throws IOException
	{
		byte[] key;
		// 原始内容分成的块
		byte[] frontBytes = new byte[512*1024];
		byte[] endBytes;
		byte[] encrypted = null;
		String encryptedFilePath = inputFilePath + ".encryped";
		System.out.println(inputFilePath.substring(inputFilePath.length()-3));
		
		OutputStream out;
		InputStream in;
		File f = null;
		
		/*key alread exit ??*/
		f = new File(keyPath);
		if(!f.exists())
		{			
			key = AES.GenKey();
			System.out.println("will create new key that is "+ key);
			out = new FileOutputStream(keyPath, false);//no append
			out.write(key);
			out.close();
		}
		else
		{
			in = new FileInputStream(keyPath);
			key = new byte[in.available()];
			in.read(key);
			System.out.println("key already exits, that is:"+ key);
			in.close();
		}
			
				
		
		
		int offset = 0;
		long blockNum = 0;

		in = new FileInputStream(inputFilePath);
		out = new FileOutputStream(encryptedFilePath, false);//no append
				
		/*get the file length*/
		f = new File(inputFilePath);
		long fileLength = 0;
		fileLength = f.length();
		
		/*exactly flag*/
		boolean flag = false;
		
		if(0 != fileLength % (512 * 1024))
		{
			blockNum = 1 + fileLength / (512 * 1024);
		}
		else
		{
			blockNum = fileLength / (512 * 1024);
			flag = true;
		}
		
		long lStart = 0; //time
		long lUseTime = 0;
		
		lStart = System.currentTimeMillis();
		if(1 == blockNum)
		{
			/*just one block*/
			int lastLength = (int)fileLength;
			endBytes = new byte[lastLength];
			offset = in.read(endBytes);
			encrypted = AES.Encrypt(endBytes, key);		
			out.write(encrypted);
			out.flush();
		}
		else
		{
			for(int i = 1; i < blockNum; i++)
			{
				offset = in.read(frontBytes);
				encrypted = AES.Encrypt(frontBytes, key);
				out.write(encrypted);
				out.flush();
			}
			
			/*last one block*/
			int lastLength;
			if(flag)
			{
				/*exatly*/
				lastLength = 512 * 1024;
				System.out.println("exactly"+lastLength);
			}
			else
			{
				lastLength = (int)(fileLength % (512 * 1024));				
			}			
			
			endBytes = new byte[lastLength];
			offset = in.read(endBytes);
			encrypted = AES.Encrypt(endBytes, key);
			out.write(encrypted);
			out.flush();
		}	
		
		lUseTime = System.currentTimeMillis() - lStart;
		System.out.println(blockNum + "blocks, it takes"+lUseTime+"milliseconds");
		
		in.close();
		out.close();
						
		System.out.println(encryptedFilePath+"-------------encrypt success---------------");
	}

}

