package com.ljp.download.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.ljp.download.ServiceDownLoadActivity;
import com.ljp.version.R;

public class DownloadService extends Service {
	private NotificationManager notificationMrg;
	private String apkurl1="http://c.uzzf.com//ddl/anzhuozhinenguanjia.apk";
	private String apkurl2="http://file.liqucn.com/upload/2011/liaotian/Youni_3.4.1.3.apk";
	private String apkurl3="http://gdown.baidu.com/data/wisegame/77dae776f870e572/PPTVjuli_61.apk";
	private String apkurl4="http://file-bak.liqucn.com/upload/2011/gouwu/cn.etouch.taoyouhui_3.1.3_liqucn.com.apk";
	private Map<String,Notification> notificationCache = new HashMap<String,Notification>();
	private Holder holder;
	int flag=0;
	public void onCreate() {
		super.onCreate();
		notificationMrg = (NotificationManager) this.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
	}	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);	
		System.out.println("Get intent:" + intent);
		final String url = intent.getStringExtra("url");
//		注:此处url必须声明为final常量，否则是不会被子线程中读取到的
		System.out.println("Get url from intent:" + url);
        Runnable start = new Runnable() {
    	  public void run() {    		  
    		  loadFile(url,++flag);
			}
       };
		new Thread(start ){		
		}.start();		
	    return START_REDELIVER_INTENT;
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
//    状态栏视图更新
	private Notification displayNotificationMessage(Notification notification,int count,int flag,String url) 
	{	
		RemoteViews contentView1 =notification.contentView;	
		Log.i("TAG","updata   flag=="+flag);
		Log.i("TAG","updata   count=="+count);
		
		if(url.equals(apkurl1))
		contentView1.setTextViewText(R.id.n_title,"安卓智能管家");
		if(url.equals(apkurl2))
			contentView1.setTextViewText(R.id.n_title,"有你短信");
		if(url.equals(apkurl3))
			contentView1.setTextViewText(R.id.n_title,"pptv");
		if(url.equals(apkurl4))
		    contentView1.setTextViewText(R.id.n_title,"随手优惠");
		contentView1.setTextViewText(R.id.n_text, "当前进度："+count+"% ");
		contentView1.setProgressBar(R.id.n_progress, 100, count, false);
		notification.contentView = contentView1;
//		提交一个通知在状态栏中显示。如果拥有相同标签和相同id的通知已经被提交而且没有被移除，该方法会用更新的信息来替换之前的通知。	
		notificationMrg.notify(flag, notification);
		return notification;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	public void loadFile(String url,int flag)
	{
		Intent notificationIntent = new Intent(getApplicationContext(), this.getClass());
		notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//		addflag设置跳转类型
		PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0,notificationIntent, 0);
		// 创建Notifcation对象，设置图标，提示文字
		long number = 100;
		Notification notification = new Notification(R.drawable.ic_launcher,"DnwoLoadManager",number);// 设定Notification出现时的声音，一般不建议自定义 System.currentTimeMillis()
		notification.flags |= Notification.FLAG_ONGOING_EVENT;//出现在 “正在运行的”栏目下面	
		RemoteViews contentView1 = new RemoteViews(getPackageName(),R.layout.notification_version);				
		contentView1.setTextViewText(R.id.n_title,"准备下载");
		contentView1.setTextViewText(R.id.n_text, "当前进度："+0+"% ");
		contentView1.setProgressBar(R.id.n_progress, 100, 0, false);
		notification.contentView = contentView1;
		notification.contentIntent = contentIntent;
		double m= 0.0;
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		HttpResponse response;
		try {
			response = client.execute(get);
			HttpEntity entity = response.getEntity();
			double length = entity.getContentLength();
			InputStream is = entity.getContent();
//			使用InputStream对文件进行读取，就是字节流的输入
			FileOutputStream fileOutputStream = null;
			
			File file = null;
			if (is != null){
				
//				 file = new File(Environment.getExternalStorageDirectory(),url);
				if(url.equals(apkurl1)){
				file = new File(Environment.getExternalStorageDirectory(),"zhinengguanjia.apk");}
				if(url.equals(apkurl2)){
					file = new File(Environment.getExternalStorageDirectory(),"youniduanxin.apk");}
				if(url.equals(apkurl3)){
					file = new File(Environment.getExternalStorageDirectory(),"PPTVshiping.apk");}
				if(url.equals(apkurl4)){
					file = new File(Environment.getExternalStorageDirectory(),"suishouyouhui.apk");}
				
				fileOutputStream = new FileOutputStream(file);
				byte[] buf = new byte[1024];
				int ch = -1;
				float count = 0;
//				ch中存放从buf字节数组中读取到的字节个数
				while ((ch = is.read(buf)) != -1) {
					fileOutputStream.write(buf, 0, ch);
					count += ch;
//					从字节数组读取数据read(buf)后，返回，读取的个数，count中保存，已下载的数据字节数
					double temp=count/length;
					if(temp>=m){
					Log.i("TAG", "读取字节循环中的count"+temp);
					m+=0.1;
					int load=(int) ( count*100/length);
					sendMsg(1,load,url,notification,flag,null);}
//					函数调用handler发送信息
				}
			}
			
//			文件输出流为空，则表示下载完成，安装apk文件
			Uri Url=Uri.fromFile(file);
			Log.i("TAG", "下载完成，传递文件位置Url  "+Url);
				
			sendMsg(2,0,url,notification,0,Url);
			fileOutputStream.flush();
			if (fileOutputStream != null) {
				fileOutputStream.close();
			}
		} catch (Exception e) {
			sendMsg(-1,0,url,notification,0,null);
		}
	}
	
	private void sendMsg(int what,int c,String url,Notification notification,int flag,Uri uri) {
		Message msg = new Message();
		msg.what= what;//用来识别发送消息的类型
		msg.arg1=0;
		holder=new Holder();
		holder.count=c;
		holder.url=url;
		holder.flag=flag;
		holder.notify=notification;
		holder.Uri=uri;
		msg.obj=holder;//消息传递的自定义对象信息
		handler.sendMessage(msg);
	}
	// 定义一个Handler，用于处理下载线程与主线程间通讯
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			final Holder data=(Holder) msg.obj;
			if (!Thread.currentThread().isInterrupted()) {
//				判断下载线程是否中断
				switch (msg.what) {
				case 1:
					Log.i("TAG", "handlemessage中的 case 1: data.count     "+data.count);
					Log.i("TAG", "handlemessage中的 case 1: flag          "+data.flag);
					if(data.count>=99){
						notificationMrg.cancel(data.flag);	
						break;
					}
					Notification notification;
					if(notificationCache.containsKey(data.url)){	
						//每次更新时，先以key，扫描hashmap，存在则读取出来。
						notification = notificationCache.get(data.url);				
						notification=displayNotificationMessage(notification,data.count ,data.flag,data.url);
						notificationCache.put(data.url, notification);}
					else  {
//						//第一次更新时，传入notification对象每次，将notification对象存入hashmap中
					notification=data.notify;
					notification=displayNotificationMessage(notification,data.count ,data.flag,data.url);
					notificationCache.put(data.url, notification);
					}
//					}					
					break;
				case 2:
					if(data.url.equals(apkurl1)){			
						Toast it1 = null;
					it1.makeText(getApplication(), "安卓智能管家下载成功", 1).show();
			          }
					if(data.url.equals(apkurl2))
						{Toast it2 = null;
						it2.makeText(getApplication(), "有你短信下载成功", 1).show();}
					if(data.url.equals(apkurl3))
					{Toast it3 = null;
					it3.makeText(getApplication(), "pptv下载成功", 1).show();}
					if(data.url.equals(apkurl4))
					{Toast it4 = null;
					it4.makeText(getApplication(), "随手优惠下载成功", 1).show();}					
					Log.i("TAG", "case 2中Uri ：  "+data.Uri);
					openfile(data.Uri);
					break;
				case -1:
					String error = msg.getData().getString("error");
					Toast.makeText(getApplication(), error, 1).show();
					break;
//					否则输出错误提示
				}
			}
			super.handleMessage(msg);
		}
	};
	public void openfile(Uri url){
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(url,"application/vnd.android.package-archive");
		startActivity(intent);
	}
	public class Holder{
		Notification notify;
		String url;
		int count;
		int flag;
		Uri Uri;
	}
}

