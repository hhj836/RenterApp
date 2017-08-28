package com.ickkey.dztenant.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class PathUtil {
	private static PathUtil instance;
	private PathUtil(){
		
	}
	public static PathUtil getInstance(){
		if(instance==null){
		synchronized (PathUtil.class) {
			if(instance==null)
				instance=new PathUtil();
		}
		}
		
		return instance;
	}
	public static String getRealFilePath( final Context context, final Uri uri ) {
		if ( null == uri ) return null;
		final String scheme = uri.getScheme();
		String data = null;
		if ( scheme == null )
			data = uri.getPath();
		else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
			data = uri.getPath();
		} else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
			Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
			if ( null != cursor ) {
				if ( cursor.moveToFirst() ) {
					int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
					if ( index > -1 ) {
						data = cursor.getString( index );
					}
				}
				cursor.close();
			}
		}
		return data;
	}
	public static String getImgPathByUri(Uri uri,Context context){

		String[] proj = { MediaStore.Images.Media.DATA };

		Cursor actualimagecursor =context.getContentResolver().query(uri,proj,null,null,null);

		int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

		actualimagecursor.moveToFirst();

		String img_path = actualimagecursor.getString(actual_image_column_index);

		return img_path;
		
	}
public static String getFileName(String path){
	String fileName= path.substring(path.lastIndexOf("/") + 1, path.length());
	return fileName;
}
	/**
	 * 获得指定文件的byte数组
	 */
	public static byte[] getFileBytes(String filePath){
		byte[] buffer = null;
		try {
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}
/*public  static String getCacheDataPath(FileType type){
	String path=null;
	switch(type){
	case IMG:
		path=GlobalParams.applicationContext.getExternalFilesDir(ConstantValues.CACHEDIR).getAbsolutePath()+"/"+AotoBangApp.mPreLoad.getId()+"/"+"image/";
		break; 
	case AUDIO:
		path=GlobalParams.applicationContext.getExternalFilesDir(ConstantValues.CACHEDIR).getAbsolutePath()+"/"+AotoBangApp.mPreLoad.getId()+"/"+"audio/";
		break; 
	case VIDEO:
		path=GlobalParams.applicationContext.getExternalFilesDir(ConstantValues.CACHEDIR).getAbsolutePath()+"/"+AotoBangApp.mPreLoad.getId()+"/"+"video/";
		break; 
	
	}
	LogUtil.info(PathUtil.class, path);
	File file=new File(path);
	if(!file.exists())
		 file.mkdir();
	return path;
	
}
 public static enum FileType {
    	IMG,
    	VIDEO,
    	AUDIO;
    	
    }*/
}
