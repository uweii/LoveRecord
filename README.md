# LoveRecord
一个恋爱的计时桌面插件<br>
<h3>截图</h3>
 <img src="/src/mainset.png" width = "300" height = "500" alt="主界面" align=left />
 <img src="/src/date.png" width = "300" height = "500" alt="设置日期" align=left />
  <img src="/src/desktop.png" width = "300" height = "500" alt="桌面效果" align=center />
<br>
---
用到知识：service, broadcastreceiver, widget布局，图片解析处理，DatePicker,TimePicker<br>
Sharepreference,等
---
使用widget写的一个简单计时插件。<br>
点击设置图片button后，实际是启动打开图库的intent。startActivityForResult。拿到选择的图片对应的uri后<br>
便将图片显示这个ImageView中，其中涉及到压缩图片，因为图片内存可能太大，然后导致oom。<br>
---
 private Bitmap getSmallBitmap(String path){<br>
        BitmapFactory.Options options = new BitmapFactory.Options();<br>
        options.inJustDecodeBounds = true;<br>
        Bitmap bitmap = BitmapFactory.decodeFile(path,options);<br>
        float w = options.outWidth;<br>
        float h = options.outHeight;<br>
        options.inJustDecodeBounds = false;<br>
        float ww = 240f;<br>
        float hh = 400f;<br>
        int scale = Math.max(Math.round(w/ww),Math.round(h/hh));<br>
        options.inSampleSize = scale;<br>
        return BitmapFactory.decodeFile(path,options);<br>
    }<br>
   返回一个较小的图片。<br>
   然后启动service用来计时更新。<br>
   注意<br>
   @Override<br>
public int onStartCommand(Intent intent, int flags, int startId) {<br>
flags = START_STICKY; //防止因内存不足而销毁service，所以设置成START_STICKY，当因内存不足销毁后会自动启动<br>
return super.onStartCommand(intent, flags, startId);<br>
}
<br>
CSDN地址  http://blog.csdn.net/dummyo/article/details/79334068
