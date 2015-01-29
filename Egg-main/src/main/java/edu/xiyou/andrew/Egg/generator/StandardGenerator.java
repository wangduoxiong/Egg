package edu.xiyou.andrew.Egg.generator;

import com.sleepycat.je.*;
import edu.xiyou.andrew.Egg.pageprocessor.pageinfo.CrawlDatum;
import edu.xiyou.andrew.Egg.utils.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 任务生成器，通过调用这个类，可以获取下一个任务
 * Created by andrew on 15-1-22.
 */
public class StandardGenerator implements Generator{
    private Environment environment;
    private Cursor cursor;
    private Database database;
    private static final Logger LOG = LoggerFactory.getLogger(StandardGenerator.class);

    public StandardGenerator(Environment environment){
        this.environment = environment;
    }

    /**
     * 取出下一个链接
     * @return
     */
    @Override
    public CrawlDatum nextDatum() throws Exception {
        if (cursor == null){
            database = BerkeleyDBFactory.createDB(environment, "crawl");
            cursor = database.openCursor(null, CursorConfig.DEFAULT);
        }
        DatabaseEntry keyEntry = new DatabaseEntry();
        DatabaseEntry valueEntry = new DatabaseEntry();
        byte[] value;
        try {

            while (true) {
                if (cursor.getNext(keyEntry, valueEntry, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
                    value = valueEntry.getData();
                    if ((value[0] & CrawlDatum.STATUS_FETCHED) == CrawlDatum.STATUS_FETCHED) {
                        try {
                            if (!(System.currentTimeMillis() - SegmentWrite.bytes2Long(value, 1) > Config.interval))
                                continue;
                            else
                                return new CrawlDatum(new String(keyEntry.getData()), value[0], SegmentWrite.bytes2Long(value, 1));
                        } catch (Exception e) {
                            LOG.info("StandardGenerator {}", e);
                            continue;
                        }
                    }
                    CrawlDatum datum = new CrawlDatum(new String(keyEntry.getData()), CrawlDatum.STATUS_UNFETCH);
                    return datum;
                }
            }
        }catch (Exception e){
            LOG.info("Exception :" + e);
            return null;
        }finally {
        }
    }

    public void close(){
        if (cursor != null) {
            cursor.close();
        }
        if (database != null) {
            database.close();
        }
//        if (environment != null) {
//            environment.close();
//        }
    }
}