package both.video.venvy.com.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import cn.com.venvy.Platform;
import cn.com.venvy.PlatformInfo;
import cn.com.venvy.common.report.Report;
import cn.com.venvy.common.report.ReportInfo;
import cn.com.venvy.common.utils.VenvyLog;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PlatformInfo info = new PlatformInfo.Builder().setThirdPlatform("sss").setServiceVersion("v1").setSdkVersion("v1.0.0").setBuId("OS").setApplicationContext(this.getApplicationContext()).builder();
        Platform.instance().init(info);
        startReportTest();
    }

    /**
     * 测试Report
     */
    private void startReportTest() {
//        View view = null;
//        try {
//            view.postInvalidate();
//        } catch (Exception e) {
//            Report.report(e);
//        }
        for (int i = 0; i < 18; i++) {
            ReportInfo info = new ReportInfo();
            info.level = Report.ReportLevel.i;
            info.tag = this.getClass().getName();
            String demo = "aldfjalsdklaksjgiougq0oitlksdjasjldkgjaklshguw3riutlasdng,ns,.mn,sngkjhruoue890240adjfopu024u02u40tu0jlasdl124utkasjdlfaksldh124owld";
            info.message = " {i === " + i + "}" + demo;
            Report.report(info);
        }
    }

    @Override
    protected void onDestroy() {
        Platform.instance().onDestroy();
        super.onDestroy();
    }
}
