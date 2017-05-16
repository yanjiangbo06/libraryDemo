package cn.com.venvy.common.http.base;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import cn.com.venvy.okhttp3.ResponseBody;

/**
 * Created by Arthur on 2017/5/13.
 */

public abstract class OkHttpDownLoadRequestHandler extends IRequestHandler.RequestHandlerAdapter {
    /**
     * 目标文件存储的文件夹路径
     */
    private String destFileDir;
    /**
     * 目标文件存储的文件名
     */
    private String destFileName;

    public OkHttpDownLoadRequestHandler(String destFileDir, String destFileName) {
        this.destFileDir = destFileDir;
        this.destFileName = destFileName;
    }


    @Override
    public void requestFinish(Request request, IResponse response) {
        if (response.isSuccess()) {
            startDownLoad(response);
        } else {
            requestError(request, null);
        }
    }

    public abstract void downloadSuccess(String filePath);

    public abstract void dwonloadFailed(String errorMessage);

    private void startDownLoad(IResponse response) {

        InputStream is = null;
        byte[] buf = new byte[2048];

        FileOutputStream fos = null;
        ResponseBody okResponse = null;
        try {
            okResponse = (ResponseBody) response.getData();
            is = okResponse.byteStream();

            File dir = new File(destFileDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, destFileName);
            fos = new FileOutputStream(file);
            int len = 0;
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            fos.flush();

            //下载成功
            downloadSuccess(file.getAbsolutePath());
        } catch (IOException e) {
            dwonloadFailed(e.getMessage());
        } finally {
            try {
                if (okResponse != null) {
                    okResponse.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
