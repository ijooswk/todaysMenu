package com.parkse.todaymenu.file;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;

import javax.net.ssl.HttpsURLConnection;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;

import com.parkse.todaymenu.ShowMenuActivity;

public class fileOpener extends AsyncTask<String, Integer, String> {
	public Activity parent;
	boolean bCancel = false;
	ProgressDialog dlg = null;
	
	/**
	 * 
	 */
	@Override
	protected void onPreExecute() {		
		super.onPreExecute();
		dlg = new ProgressDialog(parent);
		dlg.setMessage("식단 여는중...");
		dlg.setCancelable(false);
		dlg.setButton("취소", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface arg0, int arg1) {
				fileOpener.this.cancel(true);
			}});
		dlg.setMax(100);
		dlg.show();
	}

	/**
	 * 
	 */
	@Override
	protected String doInBackground(String... strData) {

		String pdfUrl = strData[0];
		String mSdPath = "";
        String ext = Environment.getExternalStorageState();
        if (ext.equals(Environment.MEDIA_MOUNTED)) {
            mSdPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/";
        } else {
            mSdPath = Environment.MEDIA_UNMOUNTED+"/";
        }
        mSdPath = mSdPath + "TrueFriend/";
        
        //폴더를 만들자
        File path = new File(mSdPath);
        
        if(!path.isDirectory()){
        	path.mkdirs();
        }
        
        //파일 열기를 시작하면 해당 파일을 무조건 삭제함
		File file = new File(mSdPath);
		String[] children = file.list();
		if(children!=null){
			for(int i=0;i<children.length;i++){
				String fileName = children[i];
				File f = new File(mSdPath + fileName);
				if(f.exists()){
					f.delete();
				}
			}
		}
		String pdfFileName = mSdPath + "Today.txt";
		
		try {
			 pdfUrl += "?dl=1";
	         URL fileUrl = new URL(pdfUrl);
	         String[] fileNames = fileUrl.toString().split("/");
             pdfFileName = mSdPath + fileNames[fileNames.length-1];

		    HttpsURLConnection urlconn = null;
			try {
				urlconn = (HttpsURLConnection) fileUrl.openConnection();
				
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
			StringBuffer result = null;
		    try {
				String line;
				if(urlconn.getResponseCode()==200){	//정상으로 들어왔을 경우
					BufferedReader inStr = new BufferedReader(new InputStreamReader(urlconn.getInputStream(), "euc-kr"));
					while((line = inStr.readLine())!=null){
						result.append(line);
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		    urlconn.disconnect();
		    
  	         FileOutputStream f = new FileOutputStream(new File(pdfFileName));  
	         f.write(result.toString().getBytes());
	         
	         f.flush();
	         f.close(); 
         } catch(Exception e){
		    e.printStackTrace();
		    return "";
		 }
         
		return pdfFileName; // 정상처리
		
	}
	
	@Override
	protected void onProgressUpdate(Integer... progress) 
	{
		dlg.setProgress(progress[0]);
	}
	
	@Override
	protected void onPostExecute(String fileName) {
		dlg.dismiss();
		dlg=null;
		
		if(fileName.length()<1||fileName==null)
		{
			return;
		}		
		
		final Activity act = parent;
//        Uri path = Uri.parse(fileName);
//        if (path.getScheme() == null) {
//        	path = Uri.fromFile(new File(fileName));
//        }
        File mfile = new File(fileName);
        StringBuffer mString = null;
        String line;
        try {
			BufferedReader br1 = new BufferedReader(new InputStreamReader(new FileInputStream(mfile),"euc-kr"));
			while((line = br1.readLine())!=null){
				mString.append(line);
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        line = mString.toString();
        
        Intent go = new Intent(parent, ShowMenuActivity.class);
        go.putExtra("DO_MENU_LIST", line);
        parent.startActivity(go);
	}
	
	/**
	 * 
	 */
	@Override
	protected void onCancelled() {
		super.onCancelled();
		bCancel=true;
	}    

}
