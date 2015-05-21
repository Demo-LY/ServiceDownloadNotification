package com.ljp.download;


import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.ljp.download.service.DownloadService;
import com.ljp.version.R;

public class ServiceDownLoadActivity extends ActivityGroup {
	
	private String apkurl1="http://c.uzzf.com//ddl/anzhuozhinenguanjia.apk";
	private String apkurl2="http://file.liqucn.com/upload/2011/liaotian/Youni_3.4.1.3.apk";
	private String apkurl3="http://gdown.baidu.com/data/wisegame/77dae776f870e572/PPTVjuli_61.apk";
	private String apkurl4="http://file-bak.liqucn.com/upload/2011/gouwu/cn.etouch.taoyouhui_3.1.3_liqucn.com.apk";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_main);
		Button button1=(Button)findViewById(R.id.startdown01);
		Button button2=(Button)findViewById(R.id.startdown02);
		Button button3=(Button)findViewById(R.id.startdown03);
		Button button4=(Button)findViewById(R.id.startdown04);
		
      button1.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(ServiceDownLoadActivity.this, DownloadService.class);  
				intent.putExtra("url", apkurl1);
				//由intent启动service，后台运行下载进程，在服务中调用notifycation状态栏显示进度条
				startService(intent);	
			}
		});	
      button2.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(ServiceDownLoadActivity.this, DownloadService.class);  
				//由intent启动service，后台运行下载进程，在服务中调用notifycation状态栏显示进度条
				intent.putExtra("url", apkurl2);
				startService(intent);	
			}
		});	
      button3.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(ServiceDownLoadActivity.this, DownloadService.class);  
				//由intent启动service，后台运行下载进程，在服务中调用notifycation状态栏显示进度条
				intent.putExtra("url", apkurl3);
				startService(intent);	
			}
		});	     
      button4.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(ServiceDownLoadActivity.this, DownloadService.class);  
				//由intent启动service，后台运行下载进程，在服务中调用notifycation状态栏显示进度条
				intent.putExtra("url", apkurl4);
				startService(intent);	
			}
		});	     
	}
	
}
