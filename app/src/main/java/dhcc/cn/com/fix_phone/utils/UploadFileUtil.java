package dhcc.cn.com.fix_phone.utils;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;

import dhcc.cn.com.fix_phone.Account;

/**
 * 2017/10/12 21
 */
public class UploadFileUtil {

    private static void uploadPhotoFile(File file, String path, StringCallback stringCallback) {
        OkHttpUtils.post()
                .url("http://120.77.202.151:8080" + path)
                .addFile("mFile", file.getName(), file)
                .addHeader("accessKey", "JHD2017")
                .addHeader("accessToken", Account.getAccessToken())
                .build()
                .execute(stringCallback);
    }
}
