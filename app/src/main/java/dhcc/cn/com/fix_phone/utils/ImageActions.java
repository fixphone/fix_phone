
package dhcc.cn.com.fix_phone.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.CheckResult;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;

/**
 * 图片相关
 */
public class ImageActions {

    @CheckResult
    public static Intent actionPickImage() {
        return new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    }

    @CheckResult
    public static Intent actionPickImageFromInternal() {
        return new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
    }

    @CheckResult
    public static Uri pickedImage(int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK || data == null) {
            return null;
        }
        return data.getData();
    }


    private static boolean hasResolvedActivity(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        return intent.resolveActivity(packageManager) != null;
    }

    public static boolean isImageCaptureAvailable(Context context) {
        return hasResolvedActivity(context, CaptureCompat.baseAction());
    }

    public static boolean isImageCropAvailable(Context context) {
        return hasResolvedActivity(context, Crop.baseAction());
    }

    @CheckResult
    public static File generateImageFile(Context context, String suffix) {
        File imagesDir = IoUtil.fromPublicPath(context, "images");
        return IoUtil.generateDatedFile(imagesDir, suffix, true);
    }

    @CheckResult
    public static Intent actionCapture(Context context) throws IOException {
        File outputImage = generateImageFile(context, ".jpg");
        return CaptureCompat.actionCaptureImage(Uri.fromFile(outputImage));
    }

    @CheckResult
    public static Uri capturedImage(Context context, Intent requestIntent, int resultCode, Intent data) {
        return CaptureCompat.getCapturedImage(context, requestIntent, resultCode, data);
    }

    @CheckResult
    public static Intent actionCrop(Context context, Uri image, int size) {
        return actionCrop(context, image, 1, 1, size, size);
    }

    @CheckResult
    public static Intent actionCrop(Context context, Uri image,
                                    int aspectX, int aspectY, int outputX, int outputY) {
        return Crop.actionCropImage(context, image, aspectX, aspectY, outputX, outputY);
    }

    @CheckResult
    public static Uri croppedImage(Context context, Intent requestIntent, int resultCode, Intent data) {
        return Crop.getCroppedImage(context, requestIntent, resultCode, data);
    }

    private static class Crop {
        private static final String ACTION_CROP = "com.android.camera.action.CROP";
        private static final String MIME_TYPE = "image/*";

        private static final String RETURN_DATA = "return-data";

        private static Intent baseAction() {
            return new Intent(ACTION_CROP).setType(MIME_TYPE);
        }

        private static Intent actionCropImage(Context context, Uri image,
                                              int aspectX, int aspectY, int outputX, int outputY) {
            Intent intent = baseAction();
            if (image != null) {
                intent.setDataAndType(image, MIME_TYPE);
            } else {
                // NOT SUPPORTED!!!
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MIME_TYPE);
            }
            intent.putExtra("crop", "true");

            // aspectX aspectY 是宽高的比例
            intent.putExtra("aspectX", aspectX);
            intent.putExtra("aspectY", aspectY);

            // outputX,outputY 是剪裁图片的宽高
            intent.putExtra("outputX", outputX);
            intent.putExtra("outputY", outputY);

            intent.putExtra("noFaceDetection", true);

            intent.putExtra("scale", true);
            intent.putExtra("scaleUpIfNeeded", true); //剪裁区域太小去除黑边
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(generateImageFile(context, ".jpg")));
            intent.putExtra("return-data", false);
//            intent.putExtra(RETURN_DATA, true);
            return intent;
        }

        private static Uri getCroppedImage(Context context, Intent requestIntent, int resultCode, Intent data) {
            if (resultCode != Activity.RESULT_OK || requestIntent == null) {
                return null;
            }
            boolean returnData = requestIntent.getBooleanExtra(RETURN_DATA, false);
            if (returnData && data == null) {
                return null;
            }
            Uri output = requestIntent.getParcelableExtra(MediaStore.EXTRA_OUTPUT);
            if (output != null) {
                return output;
            }
            Bitmap bm = data.getParcelableExtra("data");
            if (bm == null) {
                return null;
            }
            try {
                File dst = generateImageFile(context, ".jpg");
                if (!IoUtil.saveBitmap(dst, bm, Bitmap.CompressFormat.JPEG, 80)) {
                    return null;
                }
                return Uri.fromFile(dst);
            } finally {
                bm.recycle();
            }
        }
    }

    private static class CaptureCompat {

        private static Intent baseAction() {
            return new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        }

        private static Intent actionCaptureImage(Uri output) {
            return baseAction().putExtra(MediaStore.EXTRA_OUTPUT, output);
        }

        private static Uri getCapturedImage(Context context, Intent requestIntent, int resultCode, Intent data) {
            if (resultCode == Activity.RESULT_OK) {
                Uri output = requestIntent.getParcelableExtra(MediaStore.EXTRA_OUTPUT);
                File file = IoUtil.toFile(output);
                if (file == null) {
                    return output;
                }
                if (file.length() == 0) {
                    return null;
                }

                return output;
//                try {
//                    Timber.v("Image file's length: %s", file.length());
//                    Uri capturedUri = insertToMediaImages(context, file);
//                    if (capturedUri == null) {
//                        capturedUri = insertToMediaImagesCompat(context, file);
//                    }
//                    return capturedUri;
//                } finally {
//                    if (!file.delete()) {
//                        Timber.w("Could not delete tmp image file: %s", file);
//                    }
//                }
            }
            return null;
        }

        private static Uri insertToMediaImages(Context context, File file) {
            Bitmap bm = null;
            try {
                // FIXME: process exif
                bm = BitmapFactory.decodeFile(file.getAbsolutePath());
                if (bm == null) {
                    return null;
                }
                return Uri.parse(MediaStore.Images.Media.insertImage(
                        context.getContentResolver(),
                        bm,
                        "",
                        ""));
            } catch (Throwable e) {
                return null;
            } finally {
                if (bm != null) {
                    bm.recycle();
                }
            }
        }
    }
    private static Uri insertToMediaImagesCompat(Context context, File file) {
        File cameraDirectory = IoUtil.getDCIMDirectory("Camera");
        File dst = IoUtil.generateDatedFile(cameraDirectory, ".jpg");
        if (IoUtil.copyFile(file, dst)) {
            Uri uri = Uri.fromFile(dst);
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            mediaScanIntent.setData(uri);
            context.sendBroadcast(mediaScanIntent);
            return uri;
        }
        return null;
    }

    public static void loadImage(String url,ImageView image){
//        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .cacheInMemory(true)
//                .cacheOnDisk(true)
//                .bitmapConfig(Bitmap.Config.RGB_565)
//                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
//                .resetViewBeforeLoading(true)
//                .build();
//
//        ImageLoader.getInstance().displayImage(url, image, options, new ImageLoadingListener() {
//            @Override
//            public void onLoadingStarted(String imageUri, View view) {
//
//            }
//
//            @Override
//            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//
//            }
//
//            @Override
//            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                view.setTag(imageUri);
//            }
//
//            @Override
//            public void onLoadingCancelled(String imageUri, View view) {
//
//            }
//        });
    }
}
