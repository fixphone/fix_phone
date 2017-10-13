package dhcc.cn.com.fix_phone.utils;

import com.nanchen.compresshelper.CompressHelper;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;

import dhcc.cn.com.fix_phone.Account;
import dhcc.cn.com.fix_phone.MyApplication;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 2017/10/12 21
 */
public class UploadFileUtil {

    public static void uploadPhotoFile(File file, final String path, final StringCallback stringCallback) {
        if (ImageUtil.isImage(file)) {
            Observable.just(CompressHelper.getDefault(MyApplication.getContext()).compressToFile(file))
                    .observeOn(Schedulers.newThread()).subscribe(new Consumer<File>() {
                @Override
                public void accept(File file) throws Exception {
                    uploadFile(path, stringCallback, file);
                }
            });
        } else {
            uploadFile(path, stringCallback, file);
        }

    }

    private static void uploadFile(String path, StringCallback stringCallback, File uploadFile) {
        OkHttpUtils.post()
                .url("http://120.77.202.151:8080" + path)
                .addFile("mFile", uploadFile.getName(), uploadFile)
                .addHeader("accessKey", "JHD2017")
                .addHeader("accessToken", Account.getAccessToken())
                .build()
                .execute(stringCallback);
    }
}
