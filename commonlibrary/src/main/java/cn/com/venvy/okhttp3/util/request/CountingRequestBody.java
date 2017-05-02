package cn.com.venvy.okhttp3.util.request;

import java.io.IOException;

import cn.com.venvy.okhttp3.MediaType;
import cn.com.venvy.okhttp3.RequestBody;
import cn.com.venvy.okio.Buffer;
import cn.com.venvy.okio.BufferedSink;
import cn.com.venvy.okio.ForwardingSink;
import cn.com.venvy.okio.Okio;
import cn.com.venvy.okio.Sink;

/**
 * Decorates an OkHttp request body to count the number of bytes written when writing it. Can
 * decorate any request body, but is most useful for tracking the upload progress of large
 * multipart requests.
 *
 * @author Leo Nikkilä
 */
public class CountingRequestBody extends RequestBody
{

    protected RequestBody delegate;
    protected Listener listener;

    protected CountingSink countingSink;

    public CountingRequestBody(RequestBody delegate, Listener listener)
    {
        this.delegate = delegate;
        this.listener = listener;
    }

    @Override
    public MediaType contentType()
    {
        return delegate.contentType();
    }

    @Override
    public long contentLength()
    {
        try
        {
            return delegate.contentLength();
        } catch (IOException e)
        {
            
        }
        return -1;
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException
    {

        countingSink = new CountingSink(sink);
        BufferedSink bufferedSink = Okio.buffer(countingSink);

        delegate.writeTo(bufferedSink);

        bufferedSink.flush();
    }

    protected final class CountingSink extends ForwardingSink
    {

        private long bytesWritten = 0;

        public CountingSink(Sink delegate)
        {
            super(delegate);
        }

        @Override
        public void write(Buffer source, long byteCount) throws IOException
        {
            super.write(source, byteCount);

            bytesWritten += byteCount;
            listener.onRequestProgress(bytesWritten, contentLength());
        }

    }

    public static interface Listener
    {
        public void onRequestProgress(long bytesWritten, long contentLength);
    }

}