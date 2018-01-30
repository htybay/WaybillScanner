# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\YUEYINGSK\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
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
#指定代码的压缩级别
  -optimizationpasses 5

    #包明不混合大小写
    -dontusemixedcaseclassnames

    #不去忽略非公共的库类
    -dontskipnonpubliclibraryclasses

     #优化  不优化输入的类文件
    -dontoptimize

     #预校验
    -dontpreverify

     #混淆时是否记录日志
    -verbose

     # 混淆时所采用的算法
    -optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

    #保护注解
    -keepattributes *Annotation*

    # 保持哪些类不被混淆
    -keep public class * extends android.app.Activity
    -keep public class * extends android.app.Application
    -keep public class * extends android.app.Service
    -keep public class * extends android.content.BroadcastReceiver
    -keep public class * extends android.content.ContentProvider
    #如果有引用v4包可以添加下面这行
    -keep public class * extends android.support.v4.app.Fragment



    ##佳博打印机忽略
    -keep class org.xmlpull.v1.** { *; }
    -keep class * extends java.lang.annotation.Annotation { *; }
    -keep class com.gprinter.** { *;}
    -keep class com.lidroid.xutils.** { *;}

    #忽略警告
    -ignorewarning

    ##记录生成的日志数据,gradle build时在本项目根目录输出##

    #apk 包内所有 class 的内部结构
    -dump class_files.txt
    #未混淆的类和成员
    -printseeds seeds.txt
    #列出从 apk 中删除的代码
    -printusage unused.txt
    #混淆前后的映射
    -printmapping mapping.txt

    ########记录生成的日志数据，gradle build时 在本项目根目录输出-end######


    #####混淆保护自己项目的部分代码以及引用的第三方jar包library#######

    #-libraryjars libs/umeng-analytics-v5.2.4.jar

    #三星应用市场需要添加:sdk-v1.0.0.jar,look-v1.0.1.jar
    #-libraryjars libs/sdk-v1.0.0.jar
    #-libraryjars libs/look-v1.0.1.jar

    #如果不想混淆 keep 掉

    #项目特殊处理代码

    #忽略警告
    -dontwarn com.lippi.recorder.utils**
    #保留一个完整的包
    -keep class com.lippi.recorder.utils.** {
        *;
     }

    -keep class  com.lippi.recorder.utils.AudioRecorder{*;}


    #如果引用了v4或者v7包
    -dontwarn android.support.**

    ####混淆保护自己项目的部分代码以及引用的第三方jar包library-end####

    -keep public class * extends android.view.View {
        public <init>(android.content.Context);
        public <init>(android.content.Context, android.util.AttributeSet);
        public <init>(android.content.Context, android.util.AttributeSet, int);
        public void set*(...);
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
    #-keepclassmembers enum * {
    #  public static **[] values();
    #  public static ** valueOf(java.lang.String);
    #}



    -keepclassmembers class * {
        public void *ButtonClicked(android.view.View);
    }

    #不混淆资源类
    -keepclassmembers class **.R$* {
        public static <fields>;
    }

    #避免混淆泛型 如果混淆报错建议关掉
    #–keepattributes Signature

    #移除log 测试了下没有用还是建议自己定义一个开关控制是否输出日志
    #-assumenosideeffects class android.util.Log {
    #    public static boolean isLoggable(java.lang.String, int);
    #    public static int v(...);
    #    public static int i(...);
    #    public static int w(...);
    #    public static int d(...);
    #    public static int e(...);
    #}

#    eventbus
    -keepattributes *Annotation*
    -keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
    }
    -keep enum org.greenrobot.eventbus.ThreadMode {  *; }

    # Only required if you use AsyncExecutor
    -keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
    }
    # glide 的混淆代码
    -keep public class * implements com.bumptech.glide.module.GlideModule
    -keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
      **[] $VALUES;
      public *;
    }
    # banner 的混淆代码
    -keep class com.youth.banner.** {
        *;
     }
#    baseadapterhelper
    -keep class com.chad.library.adapter.** {
    *;
    }
    -keep public class * extends com.chad.library.adapter.base.BaseQuickAdapter
    -keep public class * extends com.chad.library.adapter.base.BaseViewHolder
    -keepclassmembers public class * extends com.chad.library.adapter.base.BaseViewHolder {
         <init>(android.view.View);
    }


    #okhttputils
    -dontwarn com.zhy.http.**
    -keep class com.zhy.http.**{*;}


    #okhttp
    -dontwarn okhttp3.**
    -keep class okhttp3.**{*;}


    #okio
    -dontwarn okio.**
    -keep class okio.**{*;}
    #如果用用到Gson解析包的，直接添加下面这几行就能成功混淆，不然会报错。
    #gson
    #-libraryjars libs/gson-2.2.2.jar
    -keepattributes Signature
    # Gson specific classes
    -keep class sun.misc.Unsafe { *; }
    # Application classes that will be serialized/deserialized over Gson
    -keep class bean.** { *; }

#    极光
    -dontoptimize
    -dontpreverify

    -dontwarn cn.jpush.**
    -keep class cn.jpush.** { *; }

    -dontwarn cn.jiguang.**
    -keep class cn.jiguang.** { *; }


     -keep public class cn.jiguang.analytics.android.api.** {
            *;
        }

    #greendao3.2.0,此是针对3.2.0，如果是之前的，可能需要更换下包名
    -keep class org.greenrobot.greendao.**{*;}
    -keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
    public static java.lang.String TABLENAME;
    }
    -keep class **$Properties

   -dontwarn com.tencent.bugly.**
   -keep public class com.tencent.bugly.**{*;}

   -keep class dmax.dialog.** {
       *;
   }


   # Retrofit
   -dontnote retrofit2.Platform
   -dontnote retrofit2.Platform$IOS$MainThreadExecutor
   -dontwarn retrofit2.Platform$Java8
   -keepattributes Signature
   -keepattributes Exceptions

   # okhttp
   -dontwarn okio.**

   # Gson
   -keep class com.example.testing.retrofitdemo.bean.**{*;} # 自定义数据模型的bean目录


#Rxjava RxAndroid
-dontwarn
rx.*
-dontwarn
sun.misc.**
-keepclassmembers
class rx.internal.util.unsafe.*ArrayQuene*Field*{
long producerIndex;
long consumerIndex;
}


-keepclassmembers
class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
rx.internal.util.atomic.LinkedQueueNode producerNode;
rx.internal.util.atomic.LinkedQueueNode consumerNode;
}
-keepclassmembers
class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
rx.internal.util.atomic.LinkedQueueNode consumerNode;
}