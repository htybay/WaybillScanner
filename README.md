# WaybillScanner
运单扫描模块，支持PDA和android手机。v1.0.0提供扫码装车和查看装车概况。后续版本支持扫码卸车、返回场地、返库再送。<br/>
# 使用
在你需要进入扫码模块的入口处：<br/>
第一步，添加依赖：</br>

第二步，在你需要进入扫码模块的入口处：<br/>
<pre><code>
        //获取运单扫描器

        WaybillScanner.get(this)

                //设置设备类型，是手机还是PDA

                .deviceType(WaybillScanner.DEVICE_PHONE)

                //设置操作类型，如扫码装车

                .operateType(WaybillScanner.OPT_SCAN_LOAD)

                //设置账号信息

                .userInfo(UserInfo.create().userId("58").branchCode("2001").companyCode("002"))

                //跳到扫码界面，你可以喝茶了。

                .toScan();
</code></pre>
