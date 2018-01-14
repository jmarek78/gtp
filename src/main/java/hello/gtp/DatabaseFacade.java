package hello.gtp;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;

import javax.xml.crypto.Data;
import java.util.concurrent.ConcurrentMap;

/**
 * The database facade can abstract the data storage for different environments.
 *
 * Data storage schema:
 *
 * any page that will eventually lead to philosophy will be stored in the database.
 * Key: page url
 * Value: the first link on the page, a link which will lead to philosophy
 * and therefore will also exist in this database
 */
class DatabaseFacade {

    private DB db;
    private ConcurrentMap<String, String> map;

    private static DatabaseFacade dbf;

    private static void init() {
        if (dbf == null) {
            dbf = new DatabaseFacade();
        }
    }

    private ConcurrentMap<String, String> start() {
        dbf.db = DBMaker.fileDB("gtp.db").fileMmapEnable().make();
        return dbf.db.hashMap("map", Serializer.STRING, Serializer.STRING).createOrOpen();
    }

    public static ConcurrentMap<String, String> get() {
        init();
        if (dbf.map == null) {
            dbf.map = dbf.start();
        }
        return dbf.map;
    }

    public static void close() {
        dbf.db.close();
        dbf.map = null;
    }

}
