package com.example.loverappwidget;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Calendar;

public class SetActivity extends AppCompatActivity {
    private Button mChooseImg;
    private TextView mChooseTime;
    private TextView mChooseDate;
    private TextView mShowDate, mShowTime;
    private EditText mBoyName, mGirlName,mShowTitle;
    private final int IMAGE_REQUEST_CODE = 0x123;
    private final int PHOTO_REQUEST_CUT = 0x125;
    public static final String IMAGE_FILE_NAME = "CropedImgName.jpg";
    private String mTime, mDate;
    Uri uritempFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        mChooseImg = findViewById(R.id.choose_img);
        mChooseTime = findViewById(R.id.choose_time);
        mChooseDate = findViewById(R.id.choose_date);
        mShowDate = findViewById(R.id.show_date);
        mShowTime = findViewById(R.id.show_time);
        mBoyName = findViewById(R.id.boy_name);
        mGirlName = findViewById(R.id.girl_name);
        mShowTitle = findViewById(R.id.show_title);
        init();
        mChooseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // 打开手机相册,设置请求码
                startActivityForResult(intent, IMAGE_REQUEST_CODE);
            }
        });
        mChooseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                new TimePickerDialog(SetActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                mShowTime.setText(hourOfDay + "时" + minute + "分");
                                mTime = "" + hourOfDay + ":" + minute + ":" + 00;
                            }
                        }, c.get(Calendar.HOUR), c.get(Calendar.MINUTE), true).show();
            }
        });
        mChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                new DatePickerDialog(SetActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                mShowDate.setText(year + "年" + (month + 1) + "月" + dayOfMonth + "日");
                                mDate = "" + year + "-" + (month + 1) + "-" + dayOfMonth;
                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        } else {
            switch (requestCode) {
                case IMAGE_REQUEST_CODE:
                    resizeImage(data.getData());
                    break;
                case PHOTO_REQUEST_CUT:
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uritempFile));
                        Drawable drawable = new BitmapDrawable(bitmap);
                        mChooseImg.setBackground(drawable);
                        String path = getFilesDir().getPath() + File.separator + IMAGE_FILE_NAME;
                        File file = new File(path);
                        try {
                            FileOutputStream outputStream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    //TODO，将裁剪的bitmap显示在imageview控件上
                    break;
            }
        }
    }

    public void resizeImage(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //裁剪的大小
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        //intent.putExtra("return-data", true);
        //设置返回码
        uritempFile = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + "small.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }


    protected void save(View v) {
        if (mDate == null || mTime == null) {
            Snackbar.make(v, "请完整设置好信息", Snackbar.LENGTH_SHORT).show();
        }
        SharedPreferences sharedPreferences = getSharedPreferences("setting", MODE_PRIVATE);
        sharedPreferences.edit().putString("boy_name", mBoyName.getText().toString()).apply();
        sharedPreferences.edit().putString("girl_name", mGirlName.getText().toString()).apply();
        sharedPreferences.edit().putString("title", mShowTitle.getText().toString()).apply();
        sharedPreferences.edit().putString("date", mDate ).apply();
        sharedPreferences.edit().putString("time",mTime).apply();
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        sendBroadcast(intent);
    }
    private void init(){
        SharedPreferences sharedPreferences = getSharedPreferences("setting", MODE_PRIVATE);
        mDate = sharedPreferences.getString("date",null);
        mTime = sharedPreferences.getString("time",null);
        mBoyName.setText(sharedPreferences.getString("boy_name",""));
        mGirlName.setText(sharedPreferences.getString("girl_name",""));
        mShowDate.setText(sharedPreferences.getString("date","未设置日期"));
        mShowTime.setText(sharedPreferences.getString("time","未设置时间"));
        mShowTitle.setText(sharedPreferences.getString("title","未设置前缀"));
        String path = getFilesDir().getPath() + File.separator + IMAGE_FILE_NAME;
        Drawable drawable = new BitmapDrawable(path);
        mChooseImg.setBackground(drawable);
    }
}
