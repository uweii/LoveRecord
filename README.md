# LoveRecord
一个恋爱的计时桌面插件<br>
<h3>截图</h3>
 <img src="/src/mainset.png" width = "300" height = "500" alt="主界面" align=center />
 <img src="/src/date.png" width = "300" height = "500" alt="设置日期" align=center />
  <img src="/src/desktop.png" width = "300" height = "500" alt="桌面效果" align=center />
<br>
使用widget写的一个简单计时插件。
点击设置图片button后，实际是启动打开图库的intent。startActivityForResult。拿到选择的图片对应的uri后
便将图片显示这个ImageView中，其中涉及到压缩图片，因为图片内存可能太大，然后导致oom。
 private Bitmap getSmallBitmap(String path){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(path,options);
        float w = options.outWidth;
        float h = options.outHeight;
        options.inJustDecodeBounds = false;
        float ww = 240f;
        float hh = 400f;
        int scale = Math.max(Math.round(w/ww),Math.round(h/hh));
        options.inSampleSize = scale;
        return BitmapFactory.decodeFile(path,options);
    }
   返回一个较小的图片。
   然后启动service用来计时更新。
   注意
   @Override
public int onStartCommand(Intent intent, int flags, int startId) {
flags = START_STICKY; //防止因内存不足而销毁service，所以设置成START_STICKY，当因内存不足销毁后会自动启动
return super.onStartCommand(intent, flags, startId);
}
.
