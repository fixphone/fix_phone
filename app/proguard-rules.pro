# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-optimizationpasses 5   #指定代码的压缩级别
-dontusemixedcaseclassnames  #包明不混合大小写
-dontskipnonpubliclibraryclasses    #不去忽略非公共的库类
-dontoptimize   #优化  不优化输入的类文件
-dontpreverify   #预校验
-verbose     #混淆时是否记录日志
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*    # 混淆时所采用的算法
-keepattributes *Annotation*    #保护注解
-keep class * extends java.lang.annotation.Annotation { *; }

# 保持哪些类不被混淆
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-keep public class * extends android.support.v4.app.Fragment       #如果有引用v4包可以添加下面这行
#忽略警告
-ignorewarning
#####################记录生成的日志数据,gradle build时在本项目根目录输出#####################
#apk 包内所有 class 的内部结构
-dump class_files.txt
#未混淆的类和成员
-printseeds seeds.txt
#列出从 apk 中删除的代码
-printusage unused.txt
#混淆前后的映射
-printmapping mapping.txt
#####################记录生成的日志数据，gradle build时 在本项目根目录输出-end################

-keep class com.umeng.onlineconfig.**{ *; }
-keep class com.alipay.euler.andfix.**{ *; }
-keep class android.support.v7.**{ *; }
-keep class android.support.design.**{ *; }
-keep class android.support.percent.**{ *; }
-keep class android.support.multidex.**{ *; }
-keep class android.support.annotation.**{ *; }
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.* {  }
-keep class com.google.gson.examples.android.model.* {  }
-keep class com.google.gson.* { *;}
-keep class com.google.zxing.**{ *; }
-keep class me.dm7.barcodescanner.**{ *; }
-keep class com.bumptech.glide.**{ *; }
-keep class com.activeandroid.** { *; }
-keep class com.squareup.javapoet.**{ *; }
-keep class com.squareup.okhttp3.**{ *; }
-keep class com.squareup.okio.**{ *; }
-keep class com.zzhoujay.markdown.**{ *; }
-keep class com.google.android.gms.** { *; }
-keep class com.nostra13.universalimageloader.**{ *; }
-keep class com.unionpay.**{ *; }
-keep class cn.gov.pbc.tsm.client.mobile.android.bank.service.**{ *; }
-keep class org.json.alipay.**{ *; }
-keep class com.ut.device.**{ *; }
-keep class com.ta.utdid2.**{ *; }
-keep class com.alipay.**{ *; }
-keep class android.net.http.**{ *; }
-keep class com.hu.p7zip.**{ *; }
-keep class com.baidu.location.**{ *; }
-keep class com.baidu.**{ *; }
-keep class vi.com.gdi.bgl.android.java.**{ *; }
-keep class com.jakewharton.timber.**{ *; }
-keep class com.umeng.** { *; }
-keep class org.eclipse.paho.**{ *; }
-keep class org.hamcrest.**{ *; }
-keep class cn.jpush.** { *; }
-keep public class * extends com.umeng.**
-keep public class pl.droidsonroids.gif.GifIOException{<init>(int);}
-keep class pl.droidsonroids.gif.GifInfoHandle{<init>(long,int,int,int);}
-keep public class com.alibaba.android.arouter.routes.**{*;}
-keep class * implements com.alibaba.android.arouter.facade.template.ISyringe{*;}
-keep class *.R

-keep class dhcc.cn.com.fix_phone.rong**{ *; }
-keep class dhcc.cn.com.fix_phone.db**{ *; }
-keep class dhcc.cn.com.fix_phone.bean**{ *; }
-keep class dhcc.cn.com.fix_phone.event**{ *; }
-keep class dhcc.cn.com.fix_phone.conf**{ *; }
-keep class dhcc.cn.com.fix_phone.base**{ *; }
-keep class dhcc.cn.com.fix_phone.MyApplication {*;}
-keep class com.zhihu.matisse**{ *; }
-keep class com.github.rubensousa.bottomsheetbuilder**{ *; }

-keepattributes Exceptions,InnerClasses
-keep class dhcc.cn.com.fix_phone.rong.SealNotificationReceiver {*;}
-keepattributes Signature

# RongCloud SDK
-keep class io.rong.** {*;}
-keep class * implements io.rong.imlib.model.MessageContent {*;}
-dontwarn io.rong.push.**
-dontnote com.xiaomi.**
-dontnote com.google.android.gms.gcm.**
-dontnote io.rong.**

# VoIP
-keep class io.agora.rtc.** {*;}

# Location
-keep class com.amap.api.**{*;}
-keep class com.amap.api.services.**{*;}

# 红包
-keep class com.google.gson.** { *; }
-keep class com.uuhelper.Application.** {*;}
-keep class net.sourceforge.zbar.** { *; }
-keep class com.google.android.gms.** { *; }
-keep class com.alipay.** {*;}
-keep class com.jrmf360.rylib.** {*;}

###################################butterknife######################################
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

#####################################EventBus#######################################
-keep class org.greenrobot.eventbus.** {*;}
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keepclassmembers class ** {
    public void onEvent*(**);
    void onEvent*(**);
}

########################################retrofit2####################################
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

#########################数据库###############################
-keep class com.raizlabs.android.dbflow.** {*;}

##############混淆保护自己项目的部分代码以及引用的第三方jar包library-end#######################

-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}
-keepclassmembers class **{
    public <init>(android.content.Context);
}

#保持 native 方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}
#保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
#保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
#保持自定义控件类不被混淆
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}
#保持 Parcelable 不被混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
#保持 Serializable 不被混淆
-keepnames class * implements java.io.Serializable
#保持 Serializable 不被混淆并且enum 类也不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
#保持枚举 enum 类不被混淆 如果混淆报错，建议直接使用上面的 -keepclassmembers class * implements java.io.Serializable即可
-keepclassmembers enum * {
  public static **[] values();
  public static ** valueOf(java.lang.String);
}
-keepclassmembers class * {
    public void *ButtonClicked(android.view.View);
}
#不混淆资源类
-keepclassmembers class **.R$* {
    public static <fields>;
}
#移除log 测试了下没有用还是建议自己定义一个开关控制是否输出日志
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}
