package com.cum.chat.acmap;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.text.format.Time;
import android.util.Log;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cum.chat.acmap.Luban.Luban;
import com.cum.chat.acmap.Luban.OnCompressListener;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Multipart;
import retrofit2.http.POST;

/**
 * Created by 2-1Ping on 2017/2/6.
 */

public class MapActivity extends Activity {
    WebView mWebView;
    String cookie;
    String AnUser;
    String filePath;
    String name;
    public static final MediaType MEDIA_TYPE_PNG
            = MediaType.parse("image/png");
    protected ValueCallback<Uri[]> mFileUploadCallbackSecond;
    protected static final int REQUEST_CODE_FILE_PICKER = 51426;
    protected String mUploadableFileTypes = "image/*";

    Time time = new Time("GMT+8");
    Timer timer = new Timer();



    private final OkHttpClient client = new OkHttpClient();

    public void run(String filePath) throws Exception {
        File file = new File("/storage/5C2E-6F54/搜狗截图20160821232802.png");

        Request request = new Request.Builder()
                .url("http://123.207.32.211:8099/CUM/upload.php")
                .post(RequestBody.create(MEDIA_TYPE_PNG, file))
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        System.out.println(response.body().string());
    }
    private static final String IMGUR_CLIENT_ID = "...";

    private final OkHttpClient client1 = new OkHttpClient();

    public void run1(String filePath) throws Exception {

        // Use the imgur image upload API as documented at https://api.imgur.com/endpoints/image
        RequestBody requestBody = new MultipartBuilder()
                .type(MultipartBuilder.FORM)
                .addPart(
                        Headers.of("Content-Disposition", "form-data; name=\"title\""),
                        RequestBody.create(null, "Square Logo"))
                .addPart(
                        Headers.of("Content-Disposition", "form-data; name=\"image\""),
                        RequestBody.create(MEDIA_TYPE_PNG, new File(filePath)))
                .build();
Log.d("data","ppppppppppppppppppppppppp");
        Request request = new Request.Builder()
                .header("Authorization", "Client-ID " + IMGUR_CLIENT_ID)
                .url("http://123.207.32.211:8099/CUM/upload")
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        System.out.println(response.body().string());
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        //保存图片的路径
        time.setToNow();

        filePath= Environment.getExternalStorageDirectory().
                getAbsolutePath()+"/a.png";
        Bitmap bitmap1= readImg();
        mWebView= (WebView) findViewById(R.id.webview1);
        initWebView();
        Intent intent=getIntent();
        cookie=intent.getStringExtra("username");
        AnUser=cookie;
        mWebView.loadUrl("http://123.207.32.211:8099/CUM/mapIndex.html");
//        timer.schedule(task, 6000);//3秒后执行TimeTask的run方法
//        timer.schedule(task1,7000);
//        timer.schedule(task2,8000);



        Log.d("shuju1111111111",cookie);


    }
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    mWebView.loadUrl("javascript:setAnCookie('"+cookie+"')");
                    Log.d("progress111111111","setAnCookie");
                }
            });

        }
    };
    TimerTask task1 = new TimerTask() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    mWebView.loadUrl("javascript:getUserCookie('AnUser')");
                    Log.d("progress222222222222","getUserCookie");

                }
            });

        }
    };
    TimerTask task2 = new TimerTask() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadUrl("javascript:AcAnPhoto('"+name+".jpg"+"')");
                    Log.d("progress333333333333333","success");

                }
            });

        }
    };




    public void initWebView(){
        mWebView.setWebViewClient(new WebViewClient() {


            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {


                return super.shouldOverrideUrlLoading(view, url);

            }


            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                Log.d("error",description);
                Log.d("error",failingUrl);

            }


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
//syncCookie("http://123.207.32.211:8099/CUM/MisU.html",mycookie);
                //                mWebViewCallBack.onPageStarted(view, url, favicon);

            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return super.shouldInterceptRequest(view, request);
            }
            @Override
            public void onPageFinished(WebView view, String url) {

                super.onPageFinished(view, url);
                if(cookie!=null){
                    mWebView.loadUrl("javascript:setAnCookie('"+cookie+"')");
                    Log.d("progress111111111","setAnCookie");

                    mWebView.loadUrl("javascript:getUserCookie('userId')");
                    Log.d("progress222222222222","getUserCookie");
                }
                cookie=null;


            }

        });


        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setGeolocationEnabled(true);
        settings.setPluginState(WebSettings.PluginState.ON);
        settings.setBuiltInZoomControls(true);
        settings.setBlockNetworkImage(false);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setLoadsImagesAutomatically(true);
        settings.setSupportMultipleWindows(true);
        settings.setAppCacheEnabled(true);
        settings.setSaveFormData(true);
        settings.setDatabaseEnabled(true);
        mWebView.addJavascriptInterface(new WebAppInterface(this), "Android");

        String dir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();

        settings.setPluginState(WebSettings.PluginState.ON);

        // settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        settings.setGeolocationDatabasePath(dir);

            /* 解决空白页问题 */
        settings.setDomStorageEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        //mWebView.loadUrl("http://123.207.32.211:8099/CUM/login.html");
        settings.setAllowFileAccess(true);
        mWebView.setWebChromeClient(new WebChromeClient() {
            @SuppressWarnings("all")
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback,
                                             WebChromeClient.FileChooserParams fileChooserParams) {

                if (Build.VERSION.SDK_INT >= 21) {
                    final boolean allowMultiple = fileChooserParams.getMode() == FileChooserParams.MODE_OPEN_MULTIPLE;//是否支持多选


                    openFileInput(null, filePathCallback, allowMultiple);

                    return true;
                }
                else {
                    return false;
                }
            }
                                        @SuppressLint("NewApi")
                                        protected void openFileInput(final ValueCallback<Uri> fileUploadCallbackFirst, final ValueCallback<Uri[]> fileUploadCallbackSecond, final boolean allowMultiple) {
                                            //Android 5.0及以上版本
                                            if (mFileUploadCallbackSecond != null) {
                                                mFileUploadCallbackSecond.onReceiveValue(null);
                                            }
                                            mFileUploadCallbackSecond = fileUploadCallbackSecond;

                                            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                                            i.addCategory(Intent.CATEGORY_OPENABLE);

                                            if (allowMultiple) {
                                                if (Build.VERSION.SDK_INT >= 18) {
                                                    i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                                                }
                                            }

                                            i.setType(mUploadableFileTypes);

                                            startActivityForResult(Intent.createChooser(i, "选择文件"), REQUEST_CODE_FILE_PICKER);

                                        }

            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {

                super.onReceivedIcon(view, icon);

            }

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin,GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
                super.onGeolocationPermissionsShowPrompt(origin, callback);

            }
            

        });
    }
    /**
     * Class to be injected in Web page.
     */
    public class WebAppInterface {

        Context context;

        /**
         * Instantiate the interface and set the context
         */
        WebAppInterface(Context c) {
            context = c;
        }

        /**
         * js调android摄像头
         */
        @JavascriptInterface
        public void photo() {
            Toast.makeText(context, "照相", Toast.LENGTH_SHORT).show();
            takephoto();
        }
        @JavascriptInterface
        public void delcookie(){
            mWebView.loadUrl("javascript:delCookie('userId')");
            Log.d("调用了啊","delllllllllllllllllllllllllllll");
        }

        /**
         * Updates the basket (in the future). Right now just shows a popup with the JSON information.
         *
         * @param basketJson Basket JSON info.
         */

    }
    @Override
    public void onResume(){
        super.onResume();

    }
    public void takephoto(){
        Intent intent;
        intent = new Intent();
        //MediaStore.ACTION_IMAGE_CAPTURE  调用系统的照相机
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 0x3);
    }

    public  void postFile() {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://123.207.32.211:8099/CUM/upload.php");
        try {
            // 需要上传的文件

            File uploadFile = new File("/storage/emulated/0/DCIM/Camera/","20170211_225753.jpg");
            ///storage/emulated/0/DCIM/Camera/20170210_214534.jpg
            //定义FileEntity对象
            HttpEntity entity = new FileEntity(uploadFile,"image/*");
            //为httpPost设置头信息

            httpPost.setEntity(entity); //设置实体对象

            // httpClient执行httpPost提交
            HttpResponse response = httpClient.execute(httpPost);
            // 得到服务器响应实体对象
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                Log.d("response",responseEntity.toString());
            } else {
            }
        } catch (Exception e) {
            Log.d("err","errrrrrrrrrrrrrrrrrrrrrrrrrr");
            e.printStackTrace();
        }
//        finally {
//            // 释放资源
//            httpClient.getConnectionManager().shutdown();
//        }
    }
    /* 上传文件至Server，uploadUrl：接收文件的处理页面 */
    private void uploadFile(final String uploadfile) throws Exception
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient httpclient = new DefaultHttpClient();
                httpclient.getParams().setParameter(
                        CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
                HttpPost httppost = new HttpPost("http://123.207.32.211:8099/CUM/upload.php");

                MultipartEntity entity = new MultipartEntity();

//                String s="/storage/emulated/0/com.jianshu.haruki/images/capture/jianshu/IMG_20161116_234910.jpg";
//                String s="/storage/emulated/0/Pictures/Screenshots/Screenshot_2017-02-23-14-39-52.png";

                File file = new File(uploadfile);
                FileBody fileBody = new FileBody(file);
                entity.addPart("file", fileBody);

                httppost.setEntity(entity);
                HttpResponse response = null;
                try {
                    response = httpclient.execute(httppost);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    try {
                        Log.d("response",EntityUtils.toString(resEntity));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                timer.schedule(task2,4000);
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                else{
                    Log.d("response","error拉拉拉拉拉拉拉拉拉");
                }
            }
        }).start();


//        httpclient.getConnectionManager().shutdown();

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_FILE_PICKER) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    //Android 5.0以下版本
                    if (mFileUploadCallbackSecond != null) {//Android 5.0及以上版本
                        Uri[] dataUris = null;

                        try {
                            if (data.getDataString() != null) {
                                dataUris = new Uri[] { Uri.parse(data.getDataString()) };
                            }
                            else {
                                if (Build.VERSION.SDK_INT >= 16) {
                                    if (data.getClipData() != null) {
                                        final int numSelectedFiles = data.getClipData().getItemCount();

                                        dataUris = new Uri[numSelectedFiles];

                                        for (int i = 0; i < numSelectedFiles; i++) {
                                            dataUris[i] = data.getClipData().getItemAt(i).getUri();
                                        }
                                    }
                                }
                            }
                        }
                        catch (Exception ignored) { }
                        mFileUploadCallbackSecond.onReceiveValue(dataUris);
                        mFileUploadCallbackSecond = null;
                    }
                }
            }
            else {
                //这里mFileUploadCallbackFirst跟mFileUploadCallbackSecond在不同系统版本下分别持有了
                //WebView对象，在用户取消文件选择器的情况下，需给onReceiveValue传null返回值
                //否则WebView在未收到返回值的情况下，无法进行任何操作，文件选择器会失效
               if (mFileUploadCallbackSecond != null) {
                    mFileUploadCallbackSecond.onReceiveValue(null);
                    mFileUploadCallbackSecond = null;
                }
            }
        }


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
//                img.setImageBitmap(bitmap);
                Log.d("1111111111","0x2");

            } else {
                return;
            }
        }
        if (requestCode == 0x3) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                Bitmap bitmap = bundle.getParcelable("data");
                Uri uri = data.getData();

//                img.setImageBitmap(bitmap);
                if(filePath!=null){
                    Log.d("run",filePath);
                    try {
//                        run(filePath);
//                        postFile();
//                        httpclientClick2("/storage/5C2E-6F54/logo.png");
//                        mWebView.loadUrl("javascript:AcAnPhoto('"+"/storage/5C2E-6F54/logo.png"+"')");
                        Cursor cursor = this.getContentResolver().query(uri, null,
                                null, null, null);
                        if (cursor.moveToFirst()) {
                            String photoPath = cursor.getString(cursor
                                    .getColumnIndex("_data"));// 获取绝对路径
                            Log.d("哇哇哇哇",photoPath);

                            File file=new File(photoPath);
                             name=photoPath.substring(photoPath.indexOf("Camera/")+7, photoPath.length());
                            name = name.substring(0, name.indexOf("."));
                            Log.d("名字名字",name);
                            compressWithLs(file,name);



                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }


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

    /**
     * 压缩单张图片 Listener 方式
     */
    private void compressWithLs(File file, String name1 ){

        Luban.get(this)
                .load(file)
                .putGear(Luban.THIRD_GEAR)
                .setFilename(name1)
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                        Toast.makeText(MapActivity.this, "I'm start", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(File file) {
                        Log.i("path", file.getAbsolutePath());
                        Log.d("success","compress success!!!!!!!!!!!!!");
                        try {
                            uploadFile(file.getAbsolutePath());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                }).launch();
    }




}
