package com.parkse.todaymenu;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parkse.todaymenu.file.fileController;

import data.dataBase;

public class MainActivity extends Activity {
	String dropURL = "https://ijooswk.appspot.com/TodaysMenu.txt";
	public static String downLoadURL = "";
	public FrameLayout startBtn;
	public String mainMenu;
	public static dataBase DB;
	public ProgressBar proBar;
	public TextView main_page_text;
	public boolean btn_joongbok = true;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startBtn = (FrameLayout)findViewById(R.id.start_btn);
        proBar = (ProgressBar)findViewById(R.id.progress_bar);
        main_page_text = (TextView)findViewById(R.id.main_page_text);
        
        DB = new dataBase();
//        webview = (WebView)findViewById(R.id.web_view);
//        
//        webview.setWebViewClient(new WebViewClient(){
//        	
//        	private void downloadFile(String fileUrl){
//    			fileOpener task = new fileOpener();
//    			task.parent = MainActivity.this;
//    			task.execute(fileUrl);
//        	}
//        	
//        	public void onReceivedSslError (WebView view, SslErrorHandler handler, SslError error) {
//        		handler.proceed() ;
//        	}
//        	
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                if (url.endsWith(".txt"))
//                {
//                	downloadFile(url);
//                	return true;
//                }
//                else
        
//                	return false;
//            }
//        });
//        webview.getSettings().setJavaScriptEnabled(true);
        
    }
    
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	if(proBar.getVisibility()==View.VISIBLE)
    		proBar.setVisibility(View.GONE);
    	main_page_text.setText("화면 클릭!!");
    	btn_joongbok = true;
    }
    
    public void mOnClick(View v){
    	int viewId = v.getId();
    	if (viewId==R.id.start_btn){
    		if(btn_joongbok){
    			initData();
        		proBar.setVisibility(View.VISIBLE);	
        		main_page_text.setText("식단을 불러오고 있습니다...");
        		btn_joongbok = false;
    		}
    		
    	}
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public void initData(){
    	fileController fileC = new fileController();
		if(DB.menuName.equals(""))
		{
			fileC.parent = MainActivity.this;
	    	fileC.goHttps(dropURL);	
		}else{
			proBar.setVisibility(View.GONE);
	    	Intent intent = new Intent(this, ShowMenuActivity.class);
	    	startActivity(intent);
	    	
		}
    	
    }

}
