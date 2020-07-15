package com.kuang2010.googleplay20.util;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

public class IOUtils {
	/** 关闭流 */
	public static boolean close(Closeable io) {
		if (io != null) {
			try {
				io.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}



	/**
	 * 将流数据转成字符数据
	 * @param in
	 * @return
	 */
	public static String convert2String(InputStream in) throws IOException {
		//底层流对拷，或BufferedReader都可以

        /*BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line = reader.readLine();
        StringBuilder builder = new StringBuilder();
        builder.append(line);
        while (!TextUtils.isEmpty(line)){
            String line1 = reader.readLine();
            builder.append(line1);

        }
        String result = builder.toString();*/

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int len;
		while ((len=in.read(buf))>0){
			out.write(buf,0,len);
		}

		if (out.toString().toLowerCase().contains("utf-8")){
			return out.toString("utf-8");
		}else if (out.toString().toLowerCase().contains("iso8859-1")){
			return out.toString("iso8859-1");
		}else if (out.toString().toLowerCase().contains("gbk")){
			return out.toString("gbk");
		}
		return out.toString();
	}
}
