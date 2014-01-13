package com.tuxkids.batterynotification;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.provider.SyncStateContract.Constants;
import android.util.Log;

public class CMDProcessor {
	private final static int BUFFER_SIZE = 2048;

	public static String readString(String filename) {
		try {
			File f = new File(filename);
			if (f.exists()) {
				InputStream is = null;
				if (f.canRead()) {
					is = new FileInputStream(f);
				} else {
					Log.w(Constants._COUNT, "read-only file, trying w/ root: " + filename);
					/*
					 * Try reading as root.
					 */
					String[] commands = {
							"cat " + filename + "\n", "exit\n"
					};
					Process p = Runtime.getRuntime().exec(getSUbinaryPath());
					DataOutputStream dos = new DataOutputStream(p.getOutputStream());
					for (String command : commands) {
						dos.writeBytes(command);
						dos.flush();
					}
					if (p.waitFor() == 0) {
						is = p.getInputStream();
					} else {
						// is = p.getErrorStream();
						return null;
					}
				} // end-if: f.canRead()
				BufferedReader br = new BufferedReader(new InputStreamReader(is), BUFFER_SIZE);
				String line = br.readLine();
				br.close();
				return line;
			} else {
				/*
				 * File does not exist.
				 */
				Log.e(Constants._COUNT, "file does not exist: " + filename);
				return null;
			}
		} catch (InterruptedException iex) {
			Log.e(Constants._COUNT, iex.getMessage(), iex);
			return null;
		} catch (IOException ioex) {
			Log.e(Constants._COUNT, ioex.getMessage(), ioex);
			return null;
		}
	}
	
	public static String getSUbinaryPath() {
		String s = "/system/bin/su";
		File f = new File(s);
		if (f.exists()) {
			return s;
		}
		s = "/system/xbin/su";
		f = new File(s);
		if (f.exists()) {
			return s;
		}
		return null;
	}
	
	public Boolean execCommand(String command) 
    {
        try {
            Runtime rt = Runtime.getRuntime();
            //request permission
            Process process = rt.exec("su");
            DataOutputStream os = new DataOutputStream(process.getOutputStream()); 
            os.writeBytes(command + "\n");
            os.flush();
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (IOException e) {
            return false;
        } catch (InterruptedException e) {
            return false;
        }
        return true; 
    
	}
}
