package com.example.myapplication.utils;


import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;

/**
 * 描述：
 *
 * @author Created by heiduo
 * @time Created on 2019/11/20
 */
public class DownloadResponseBody extends ResponseBody {
    private Response originalResponse;
    private DownloadListener downloadListener;

    public DownloadResponseBody(Response response, DownloadListener downloadListener) {
        this.originalResponse = response;
        this.downloadListener = downloadListener;
    }

    @Override
    public MediaType contentType() {
        return originalResponse.body().contentType();
    }

    @Override
    public long contentLength() {
        return originalResponse.body().contentLength();
    }

    @Override
    public BufferedSource source() {
        return Okio.buffer(new ForwardingSource(originalResponse.body().source()) {
            private long byteReaded = 0;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long byteRead = super.read(sink, byteCount);
                byteReaded += byteRead == -1 ? 0 : byteRead;
                if (downloadListener != null) {
                    downloadListener.loading((int) (byteReaded / 1024));
                }
                return byteRead;
            }
        });
    }
}
