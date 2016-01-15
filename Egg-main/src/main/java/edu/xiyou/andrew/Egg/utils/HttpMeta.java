package edu.xiyou.andrew.Egg.utils;

/**
 * Created by andrew on 15-8-18.
 */
public interface HttpMeta {

    public static class Method{
        public static final String GET = "GET";

        public static final String HEAD = "HEAD";

        public static final String POST = "POST";

        public static final String PUT = "PUT";

        public static final String DELETE = "DELETE";

        public static final String TRACE = "TRACE";

        public static final String CONNECT = "CONNECT";
    }

    public static interface Header{

        public static final String REFERER = "Referer";

        public static final String USER_AGENT = "User-Agent";
    }
}
