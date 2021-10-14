package com.springleaf.cloud.discovery.config.datasource.datasource;

import com.springleaf.cloud.discovery.config.datasource.ReadableDataSource;
import com.springleaf.cloud.discovery.config.datasource.converter.Converter;
import com.springleaf.cloud.discovery.config.datasource.property.DynamicCustomProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

/**
 * <p>
 * A {@link ReadableDataSource} based on file. This class will automatically
 * fetches the backend file every isModified period.
 * </p>
 * <p>
 * Limitations: Default read buffer size is 1 MB. If file size is greater than
 * buffer size, exceeding bytes will be ignored. Default charset is UTF-8.
 * </p>
 *
 */
public class FileRefreshableDataSource<T> extends AutoRefreshDataSource<String, T>  {

    public static final Logger logger = LoggerFactory.getLogger(DynamicCustomProperty.class);

    private static final int MAX_SIZE = 1024 * 1024 * 4;
    private static final long DEFAULT_REFRESH_MS = 3000;
    private static final int DEFAULT_BUF_SIZE = 1024 * 1024;
    private static final Charset DEFAULT_CHAR_SET = Charset.forName("utf-8");

    private byte[] buf;
    private final Charset charset;
    private final File file;

    private long lastModified = 0L;
    private final String ruleType;
    /**
     * Create a file based {@link ReadableDataSource} whose read buffer size is
     * 1MB, charset is UTF8, and read interval is 3 seconds.
     *
     * @param file         the file to read
     * @param configParser the config decoder (parser)
     */
    public FileRefreshableDataSource(File file, Converter<String, T> configParser, String ruleType) throws FileNotFoundException {
        this(file, configParser, DEFAULT_REFRESH_MS, DEFAULT_BUF_SIZE, DEFAULT_CHAR_SET,ruleType);
    }

    public FileRefreshableDataSource(String fileName, Converter<String, T> configParser, String ruleType) throws FileNotFoundException {
        this(new File(fileName), configParser, DEFAULT_REFRESH_MS, DEFAULT_BUF_SIZE, DEFAULT_CHAR_SET,ruleType);
    }

    public FileRefreshableDataSource(File file, Converter<String, T> configParser, int bufSize, String ruleType)
        throws FileNotFoundException {
        this(file, configParser, DEFAULT_REFRESH_MS, bufSize, DEFAULT_CHAR_SET,ruleType);
    }

    public FileRefreshableDataSource(File file, Converter<String, T> configParser, Charset charset, String ruleType)
        throws FileNotFoundException {
        this(file, configParser, DEFAULT_REFRESH_MS, DEFAULT_BUF_SIZE, charset,  ruleType);
    }

    public FileRefreshableDataSource(File file, Converter<String, T> configParser, long recommendRefreshMs, int bufSize,
                                     Charset charset, String ruleType) throws FileNotFoundException {
        super(configParser, recommendRefreshMs);
        if (bufSize <= 0 || bufSize > MAX_SIZE) {
            throw new IllegalArgumentException("bufSize must between (0, " + MAX_SIZE + "], but " + bufSize + " get");
        }
        if (file == null || file.isDirectory()) {
            throw new IllegalArgumentException("File can't be null or a directory");
        }
        if (charset == null) {
            throw new IllegalArgumentException("charset can't be null");
        }
        this.buf = new byte[bufSize];
        this.file = file;
        this.ruleType = ruleType;
        this.charset = charset;
        // If the file does not exist, the last modified will be 0.
        this.lastModified = file.lastModified();
        firstLoad();
    }

    private void firstLoad() {
        try {
            T newValue = loadConfig();
            getProperty().updateValue(newValue);
        } catch (Throwable e) {
            logger.info("loadConfig exception", e);
        }
    }

    @Override
    public String readSource() throws Exception {
        if (!file.exists()) {
            // Will throw FileNotFoundException later.
            logger.warn(String.format("[FileRefreshableDataSource] File does not exist: %s", file.getAbsolutePath()));
        }
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            FileChannel channel = inputStream.getChannel();
            if (channel.size() > buf.length) {
                throw new IllegalStateException(file.getAbsolutePath() + " file size=" + channel.size()
                    + ", is bigger than bufSize=" + buf.length + ". Can't read");
            }
            int len = inputStream.read(buf);
            String value = new String(buf, 0, len, charset);
            logger.info("[FileDataSource] property value read from file ({}), value is: {}",file.getName(), value);
            return value;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception ignore) {
                }
            }
        }
    }

    @Override
    protected boolean isModified() {
        long curLastModified = file.lastModified();
        if (curLastModified != this.lastModified) {
            this.lastModified = curLastModified;
            return true;
        }
        return false;
    }

    @Override
    public void close() throws Exception {
        super.close();
        buf = null;
    }

    @Override
    public String getRuleType() {
        return ruleType;
    }
}
