package com.parkse.todaymenu.file;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;

/**
 * @author Î∞ïÏÑ∏Ìõà
 *
 */
public class ApkDownloadActivity extends AsyncTask<String, Integer, String>{
	public Activity parent;
	boolean bCancel = false;
	ProgressDialog dlg = null;
	public String apkName = "";
	public String urlName = "";
    /**
    *
    */
   @Override
   protected void onPreExecute() {         
        super.onPreExecute();
        dlg = new ProgressDialog(parent);
        dlg.setMessage( apkName + "다운로드중");
        dlg.setCancelable(false);
        dlg.setButton("취소", new DialogInterface.OnClickListener(){
             public void onClick(DialogInterface arg0, int arg1) {
            	 ApkDownloadActivity.this.cancel(true);
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
     
      File path = new File(mSdPath);
     
      if(!path.isDirectory()){
           path.mkdirs();
      }

        String apkFileName = mSdPath + "TrueFriend.apk";
       
        try {
           URL fileUrl = new URL(pdfUrl);
           String[] fileNames = fileUrl.toString().split("/");
           apkFileName = mSdPath + fileNames[fileNames.length-1];
                                 																														
           HttpURLConnection c = (HttpURLConnection) fileUrl.openConnection();
           c.setRequestMethod("GET");
           c.setDoInput(true);
           c.connect();
           FileOutputStream f = new FileOutputStream(new File(apkFileName));

           InputStream in = c.getInputStream();
           
           byte[] buffer = new byte[1024];
           
            //int fileSize = c.getContentLength();
            //long total = 0;
            int len1 = 0;
            while ( (len1 = in.read(buffer)) != -1 ) {
                 if(bCancel)
                      break;

              f.write(buffer,0, len1);
             
            }
           
            f.flush();
            f.close();
            in.close();
            c.disconnect();
       } catch(Exception e){
            e.printStackTrace();
            Uri uri = Uri.parse(urlName);
            Intent browserIntent = new Intent("android.intent.action.VIEW", uri);
            parent.startActivity(browserIntent);
            return "";
         }
        if(bCancel)
             return "";
      
        return apkFileName; // Ï†ïÏÉÅÏ≤òÎ¶¨
       
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
      Uri path = Uri.parse(fileName);
      if (path.getScheme() == null) {
           path = Uri.fromFile(new File(fileName));
      }
      File apkFile = new File(fileName);
      Intent intent = new Intent(Intent.ACTION_VIEW);
      intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");

      try {
          act.startActivity(intent);
     } catch (ActivityNotFoundException ex) {}
              
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
