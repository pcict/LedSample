package com.websarva.wings.android.ledsample;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button _btOnOff;
    private CameraManager _cameraManager;
    private String _cameraId;
    private boolean _sw;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //レイアウトファイルのボタンを登録
        _btOnOff = (Button)findViewById(R.id.btLightOnOff);
        //カメラマネージャーを生成
        _cameraManager = (CameraManager)getSystemService(Context.CAMERA_SERVICE);

        //カメラマネージャーにトーチモードのコールバックを登録
        _cameraManager.registerTorchCallback(new CameraManager.TorchCallback() {
            //トーチモードが変更された時の処理
            @Override
            public void onTorchModeChanged(String cameraId, boolean enabled) {
                super.onTorchModeChanged(cameraId, enabled);
                //カメラIDをセット
                _cameraId = cameraId;
                //SWに現在の状態をセット
                _sw = enabled;
            }
        }, new Handler());

        _btOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //カメラを取得できなかった時は何もしない
                if(_cameraId == null){
                    return;
                }
                try {
                    if(_sw == false){
                        //SWがfalseならばトーチモードをtrueにしてLDEオン
                        _cameraManager.setTorchMode(_cameraId, true);
                    }else{
                        _cameraManager.setTorchMode(_cameraId, false);
                        //SWがtrueならばトーチモードをfalseにしてLDEオフ
                    }

                } catch (CameraAccessException e) {
                    //エラー処理
                    e.printStackTrace();
                }
            }
        });
    }
}