# WaybillScanner
运单扫描模块，支持PDA和android手机。v2.0.3提供扫码装车和查看装车概况。后续版本支持扫码卸车、返回场地、返库再送。<br/>
# 使用

第一步，添加依赖：</br>
1.在Project的gradle文件中<br/>
<pre><code>
allprojects {

    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }

}
</code></pre>
2.在Module的grable文件中<br/>
<pre></code>
compile ('com.github.the-pig-of-jungle:WaybillScanner:v2.0.3'){
            //如果你的项目中已有下面的库，则剔除，避免类库冲突
            exclude group: 'com.android.support'
            exclude group: 'cn.yipianfengye.android'
            exclude group: 'com.github.CymChad:BaseRecyclerViewAdapterHelper'
            exclude group: 'com.github.d-max:spots-dialog'
}
</code></pre>
第二步，在你需要进入扫码模块的入口处调用代码：<br/>
<pre><code>
        //获取运单扫描器

        WaybillScanner.get(this)

                //设置设备类型，是手机还是PDA

                .deviceType(WaybillScanner.DEVICE_PHONE)

                //设置操作类型，如扫码装车、扫码卸车

                .operateType(WaybillScanner.OPT_SCAN_LOAD)

                //设置账号信息

                .userInfo(UserInfo.create().userId("58").branchCode("2001").companyCode("002"))

                //运行环境，可通过三种环境配置同名变量动态设置

                .runningEnvironment(BuildConfig.RUNNING_ENVIRONMENT)

                //跳到扫码界面

                .toScan();
</code></pre>
