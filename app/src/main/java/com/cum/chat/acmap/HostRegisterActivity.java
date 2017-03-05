package com.cum.chat.acmap;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ziyi on 2017/2/6.
 */

public class HostRegisterActivity extends Activity {
    EditText register_name;
    EditText register_pass;
    EditText register_repass;
    private String username;
    private String password;
    private String repassword;
    private Button register;
    WebView webView;
    WebViewClient mWebViewClient;
    static String waring=null;
    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_register);
        register_name= (EditText) findViewById(R.id.username);
        register_pass= (EditText) findViewById(R.id.password);
        register_repass= (EditText) findViewById(R.id.repassword);
        register= (Button) findViewById(R.id.btn_register);
        back= (ImageButton) findViewById(R.id.returnIndependentLoginActivity);
        initWebView();
        register_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    // 此处为得到焦点时的处理内容
                } else {
                    checkname(String.valueOf(register_name.getText().toString()));
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = register_name.getText().toString();
                password = register_pass.getText().toString();
                repassword=register_repass.getText().toString();
               boolean b= checkagain(username);
//                httpclientregister(username,password);//呵这个用HttpClient居然能成功
                if (waring != null) {
                    if(password.equals(repassword)&&b)
                    {
                        if (waring.equals("该用户名可用")){
                        Log.d("succ", "可以注册了");
                        httpClientRegister(username, password);
                            Toast.makeText(HostRegisterActivity.this,"注册成功",Toast.LENGTH_LONG).show();
                            HostRegisterActivity.this.onBackPressed();

                        }

                    }
                    else{
                        Toast.makeText(HostRegisterActivity.this,"两次输入的密码不一样",Toast.LENGTH_LONG).show();
                    }


                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HostRegisterActivity.this.onBackPressed();
            }
        });


    }
    public void initWebView(){
        webView= (WebView) findViewById(R.id.webview);
        webView.setVerticalScrollbarOverlay(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
        webView.loadUrl("http://123.207.32.211:8099/CUM/register.html");
        // 添加客户端支持
        webView.setWebChromeClient(new WebChromeClient());
        mWebViewClient=new WebViewClient();
    }
    public void checkname(String name){
        webView.loadUrl("javascript:showHint('"+name+"')");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        webView.loadUrl("javascript:window.local_obj.showSource('<head>'+"
                + "document.getElementsByTagName('html')[0].innerHTML+'</head>');");


    }
    final class InJavaScriptLocalObj {
        @JavascriptInterface
        synchronized   public void  showSource(String html) {
//            System.out.println("====>html="+html);
            if(html.indexOf("<h6 id=\"spanName\">请不要用数字开头的用户名</h6>")!=-1){
                waring="请不要用数字开头的用户名";
                Toast.makeText(HostRegisterActivity.this,"请不要用数字开头的用户名",Toast.LENGTH_LONG).show();

                Log.d("warning",waring);
            }else if(html.indexOf("<h6 id=\"spanName\">用户名已存在")!=-1){
                System.out.println("====>html="+html);
                waring="用户名已存在";
                Toast.makeText(HostRegisterActivity.this,"用户名已存在",Toast.LENGTH_LONG).show();

                Log.d("warning",waring);
            }
            else{
                System.out.println("====>html="+html);
                waring="该用户名可用";
                Log.d("ok","该用户名肯用");
                Toast.makeText(HostRegisterActivity.this,"该用户名可用",Toast.LENGTH_LONG).show();

            }


        }


    }


    public void httpClientRegister(final String username, final String userpassword){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost("http://123.207.32.211:8099/CUM/php/register.php");

                List<NameValuePair>params=new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username",username));
                params.add(new BasicNameValuePair("userpassword",userpassword));
                try {
                    UrlEncodedFormEntity entity=new UrlEncodedFormEntity(params,"utf-8");
                    httpPost.setEntity(entity);
                    httpClient.execute(httpPost);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    HttpResponse httpResponse=httpClient.execute(httpPost);
                    if(httpResponse.getStatusLine().getStatusCode()==200){
                        Log.d("data","成功了");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public boolean checkagain(String name){

        if(Character.isDigit(name.charAt(0))){
            Toast.makeText(HostRegisterActivity.this,"用户名不能以数字开头",Toast.LENGTH_LONG).show();
            return false;
        }
        else
            return true;
    }
}
