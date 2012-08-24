package com.parkse.todaymenu.file;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.conn.ssl.AllowAllHostnameVerifier;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.parkse.todaymenu.MainActivity;
import com.parkse.todaymenu.R;
import com.parkse.todaymenu.ShowMenuActivity;

public class fileController {
	public String resultString = "";
	public MainActivity parent;
	public class httpConnection extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... params) {
			URL urlDoc;
		    HttpsURLConnection urlconn = null;
			try {
				urlDoc = new URL(params[0]);
				urlconn = (HttpsURLConnection) urlDoc.openConnection();
				
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
			StringBuffer result = new StringBuffer();
		    try {
				String line;
				if(urlconn.getResponseCode()==200){	//정상으로 들어왔을 경우
					BufferedReader inStr = new BufferedReader(new InputStreamReader(urlconn.getInputStream(), "UTF-8"));
					int uinumb = 0;
					while((line = inStr.readLine())!=null){
						//첫번째 라인부터 시작한다
						if(uinumb==0)
							MainActivity.downLoadURL = line;
						else
							MainActivity.DB.arrayString.add(line);
						uinumb++;
						//이렇게 하면 요일에 각 시간때 별 메뉴가 들어간다
						result.append(line);
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		    urlconn.disconnect();
		    resultString = result.toString();
			return resultString;
		}
		
		@Override
		protected void onPostExecute(String result){
	    	Intent intent = new Intent(parent, ShowMenuActivity.class);
	    	MainActivity.DB.menuName = result;
	    	parent.startActivity(intent);
		}
		
	}
	
	public void goHttps(String straddr){
		parent.proBar.setVisibility(View.GONE);
		TrustManager[] trustAllCerts = new TrustManager[]{
		         new X509TrustManager() {
		             public java.security.cert.X509Certificate[] getAcceptedIssuers() {
		    			 Log.d("SSLDemo", "getAcceptedIssuers");
		                 return null;
		             }
		             public void checkClientTrusted(
		                 java.security.cert.X509Certificate[] certs, String authType) {
		    			 Log.d("SSLDemo", "Check Client Trusted");
		             }
		             public void checkServerTrusted(
		                 java.security.cert.X509Certificate[] certs, String authType) {
		    			 Log.d("SSLDemo", "Check Server Trusted");
		             }
		         }
		     };
		
		
        HttpsURLConnection.setDefaultHostnameVerifier(new AllowAllHostnameVerifier());
        SSLContext sc;
		try {
			sc = SSLContext.getInstance("TLS");	// SSL
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
		    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new httpConnection().execute(straddr);
//	    URL urlDoc;
//	    HttpsURLConnection urlconn = null;
//		try {
//			urlDoc = new URL("https://www.dropbox.com/sh/3dcg5u0d1i91uxf/e3lEt40S98");
//			urlconn = (HttpsURLConnection) urlDoc.openConnection();
//			
//		} catch (MalformedURLException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	    
//	    
//	    
//		DataOutputStream outStr = null;
//		StringBuffer response;
//		String result="";
//	    try {
//			response = new StringBuffer();
//			String line;
//			if(urlconn.getResponseCode()==200){	//정상으로 들어왔을 경우
//				BufferedReader inStr = new BufferedReader(new InputStreamReader(urlconn.getInputStream()));
//				while((line = inStr.readLine())!=null){
//					result = result + line;
//				}
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	    urlconn.disconnect();
	    
	    	
	}
	
	 static class FakeX509TrustManager implements X509TrustManager { 
         private static TrustManager[] trustManagers; 
         private final X509Certificate[] _AcceptedIssuers = new 
        		 X509Certificate[] {}; 
         public void checkClientTrusted(X509Certificate[] chain, String authType) 
                 throws CertificateException { 
         } 
         public void checkServerTrusted(X509Certificate[] chain, String authType) 
                 throws CertificateException { 
         } 
         public boolean isClientTrusted(X509Certificate[] chain) { 
             return true; 
         } 
         public boolean isServerTrusted(X509Certificate[] chain) { 
             return true; 
         } 
         public X509Certificate[] getAcceptedIssuers() { 
             return _AcceptedIssuers; 
         } 
         public static void allowAllSSL(String url) throws IOException{ 
             HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() { 
					@Override
					public boolean verify(String hostname, SSLSession session) {
						// TODO Auto-generated method stub
						return false;
					} 
                 }); 
             SSLContext context = null; 
             if (trustManagers == null) { 
                 trustManagers = new TrustManager[] { new FakeX509TrustManager() }; 
             } 
             try { 
                 context = SSLContext.getInstance("TLS"); 
                 context.init(null, trustManagers, new SecureRandom()); 
             } catch (NoSuchAlgorithmException e) { 
                 e.printStackTrace(); 
             } catch (KeyManagementException e) { 
                 e.printStackTrace(); 
             } 
             HttpsURLConnection.setDefaultSSLSocketFactory(context 
                                                           .getSocketFactory());
			HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
			((HttpsURLConnection)connection).setHostnameVerifier(new AllowAllHostnameVerifier());
			connection.setUseCaches(false);
			connection.setDoOutput(true);
			connection.setDoInput(true);
             
         } 
     }

}
