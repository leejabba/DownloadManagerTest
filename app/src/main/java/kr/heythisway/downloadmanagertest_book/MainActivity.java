package kr.heythisway.downloadmanagertest_book;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * 이 소스코드는 실행시키기 위해서는 다음과 같은 퍼미션이 필요하다.
 * 인터넷 접속 : android.permission.INTERNET
 * 저장 : android.permission.WRITE_EXTERNAL_STORAGE
 */
public class MainActivity extends AppCompatActivity {
    // getSystemService(Context.DOWNLOAD_SERVICE);
    DownloadManager downloadManager;
    // long enqueue (DownloadManager.Request request)
    long enqueue = 0;

    BroadcastReceiver mDownComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 다운로드 매니저는 시스템 서비스이므로 getSystemService 메서드로 객체를 구한다.
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        Button btnQueue = (Button) findViewById(R.id.btnQueue);
        btnQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://www.soen.kr/data/child2.jpg");
                // Request는 다운로드 할 파일의 위치와 로컬 저장 경로, 사용할 네트워크 종류 등의 정보를 가지는 객체임
                DownloadManager.Request request = new DownloadManager.Request(uri);
                /* 다운로드시 사용할 네트워크 종류, 로밍시의 다운로드 허가 여부, 상태란에 나타날 제목과 설명, 다운로드 위치 등 지정
                Request setAllowedNetworkTypes (int flags)
                Request setAllowedOverRoaming (boolean allowed)
                Request setTitle (CharSequence title)
                Request setDescription (CharSequence description)
                Request setDestinationInExternalPublicDir (String dirType, String subPath)
                Request setDestinationUri (Uri uri)
                 */
                request.setTitle("테스트 다운로드");
                request.setDescription("이미지 파일을 다운로드 받습니다.");
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);

                enqueue = downloadManager.enqueue(request);

                IntentFilter filter = new IntentFilter();
                filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
                // registerRecevier(BroadcastReceiver receiver, IntentFilter filter);
                registerReceiver(mDownComplete, filter);
            }
        });

        mDownComplete = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(context, "다운로드 완료", Toast.LENGTH_LONG).show();
            }
        };
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (enqueue != 0) {
            unregisterReceiver(mDownComplete);
            enqueue = 0;
        }
    }
}
