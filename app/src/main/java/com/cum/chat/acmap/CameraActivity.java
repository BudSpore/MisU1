package com.cum.chat.acmap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by 2-1Ping on 2017/2/8.
 */

public class CameraActivity extends Activity implements View.OnClickListener{
    private Button btn2;
    private ImageView img;
    public String filePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        //保存图片的路径
        filePath= Environment.getExternalStorageDirectory().
                getAbsolutePath()+"/a.png";
        btn2 = (Button) findViewById(R.id.btn2);
        img = (ImageView) findViewById(R.id.img);
        btn2.setOnClickListener(this);
        //读取保存的位图（图片）
        Bitmap bitmap1= readImg();
        if(bitmap1!=null){
            img.setImageBitmap(bitmap1);
        }else{
            return;
        }
    }
    @Override
    public void onClick(View v) {
        Intent intent;
                intent = new Intent();
                //MediaStore.ACTION_IMAGE_CAPTURE  调用系统的照相机
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0x3);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0x1) {
            //裁剪保存
            if (data != null) {
                Uri uri = data.getData();
                getImg(uri);
                Log.d("1111111111","0x1");
            } else {
                return;
            }
        }
        if (requestCode == 0x2) {
            //保存
            if (data != null) {
                Bundle bundle = data.getExtras();
                //得到图片
                Bitmap bitmap = bundle.getParcelable("data");
                //保存到图片到本地
                saveImg(bitmap);
                //设置图片
                img.setImageBitmap(bitmap);
                Log.d("1111111111","0x2");

            } else {
                return;
            }
        }
        if (requestCode == 0x3) {
            if (data != null) {

                Bundle bundle = data.getExtras();
                Bitmap bitmap = bundle.getParcelable("data");
                img.setImageBitmap(bitmap);
                Log.d("1111111111","0x3");

            } else {
                return;
            }
        }
    }

    //读取位图（图片）
    private Bitmap readImg() {
        File mfile = new File(filePath);
        Bitmap bm = null;
        if (mfile.exists()) {        //若该文件存在
            bm = BitmapFactory.decodeFile(filePath);
        }
        return bm;
    }
    //保存图片到本地，下次直接读取
    private void saveImg(Bitmap mBitmap)  {
        File f = new File(filePath);
        try {
            //如果文件不存在，则创建文件
            if(!f.exists()){
                f.createNewFile();
            }
            //输出流
            FileOutputStream out = new FileOutputStream(f);
            /** mBitmap.compress 压缩图片
             *
             *  Bitmap.CompressFormat.PNG   图片的格式
             *   100  图片的质量（0-100）
             *   out  文件输出流
             */
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            Toast.makeText(this,f.getAbsolutePath().toString(),Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void getImg(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            //从输入流中解码位图
            // Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            //保存位图
            // img.setImageBitmap(bitmap);
            cutImg(uri);
            //关闭流
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//    裁剪图片
    private void cutImg(Uri uri) {
        if (uri != null) {
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(uri, "image/*");
            //true:出现裁剪的框
            intent.putExtra("crop", "true");
            //裁剪宽高时的比例
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            //裁剪后的图片的大小
            intent.putExtra("outputX", 300);
            intent.putExtra("outputY", 300);
            intent.putExtra("return-data", true);  // 返回数据
            intent.putExtra("output", uri);
            intent.putExtra("scale", true);
            startActivityForResult(intent, 0x2);
        } else {
            return;
        }
    }


}
