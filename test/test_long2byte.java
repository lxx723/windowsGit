package test_long2byte;

public class test_long2byte {

	public static void main(String[] args) throws Exception
	{
		long lnum = 123;
//		byte[] abyte = new byte[8];
		byte[] abyte1 = long2Bytes(lnum);
		
		System.out.println(abyte1);
		System.out.println(lnum);
		
		 byte[] long2Bytes = long2Bytes(lnum);  
        for (int ix = 0; ix < long2Bytes.length; ++ix)
        {  
            System.out.print(long2Bytes[ix]); 
        }  
		
//		byte[] abyte2 = {'1', '2', '3'};
//		long lnum2 = bytes2Long(abyte2);
//		System.out.println(lnum2);
        
        long bytes2Long = bytes2Long(long2Bytes);  
        System.out.println("bytes×ªÐÐ³Élong: " + bytes2Long);
		
	}
	
	
	public static byte[] long2Bytes(long num) 
	{
		byte[] byteNum = new byte[8];
		for (int ix = 0; ix < 8; ++ix) 
		{
			int offset = 64 - (ix + 1) * 8;
			byteNum[ix] = (byte) ((num >> offset) & 0xff);
		}
		return byteNum;
	}

	public static long bytes2Long(byte[] byteNum) 
	{
		long num = 0;
		for (int ix = 0; ix < 8; ++ix)
		{
			num <<= 8;
			num |= (byteNum[ix] & 0xff);
		}
		return num;
	}
}




