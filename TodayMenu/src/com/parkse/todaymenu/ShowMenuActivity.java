package com.parkse.todaymenu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;

import com.parkse.todaymenu.file.ApkDownloadActivity;

import data.ParsingAdapter;

public class ShowMenuActivity extends Activity{
	String tempString = "";
	ListView showMenuList;
	ParsingAdapter parseadapter;
	public SharedPreferences prefs;
	public SharedPreferences.Editor editor;
	public static String app_ver = "APP_VERSION_NO";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		setContentView(R.layout.show_menu);
		
		showMenuList = (ListView)findViewById(R.id.show_menu_list);
		parseadapter = new ParsingAdapter(ShowMenuActivity.this, R.layout.list_row, MainActivity.DB.arrayString);
		showMenuList.setAdapter(parseadapter);
		
		super.onCreate(savedInstanceState);
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		SharedPreferences prefs = getSharedPreferences("appVer", MODE_PRIVATE);
		String text = prefs.getString(app_ver, "");
		
		if(text.equals("")){
			SharedPreferences.Editor editor = prefs.edit();
			editor.putString(app_ver, MainActivity.downLoadURL);
			editor.commit();
		}else if(!text.equals(MainActivity.downLoadURL)){
			SharedPreferences.Editor editor = prefs.edit();
			editor.putString(app_ver, MainActivity.downLoadURL);
			editor.commit();
			popUpDialog();
		}
	}
	
	public void popUpDialog(){
		 AlertDialog alert = new AlertDialog.Builder(ShowMenuActivity.this)
		    .setTitle("업데이트 안내").setMessage("새로운 오늘의 식단 앱이 업그레이드되었습니다")
		    .setPositiveButton("업데이트하러가기", new DialogInterface.OnClickListener() {
		      public void onClick(DialogInterface dialog, int whichButton) {
			  		ApkDownloadActivity task = new ApkDownloadActivity();
					task.apkName = "오늘의 식단";
					task.parent = ShowMenuActivity.this;
					task.urlName = MainActivity.downLoadURL;
					task.execute(MainActivity.downLoadURL);
		      }
		    })
		    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
		      public void onClick(DialogInterface dialog, int whichButton) {
		        dialog.cancel();
		      }
		    })
		    .create();
		 alert.show();
	}
}
