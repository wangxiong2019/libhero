package com.hero.libhero.mydb;

import com.hero.libhero.mydb.dbinit.exception.DbException;
import com.hero.libhero.mydb.dbinit.other.DbManager;
import com.hero.libhero.mydb.dbinit.other.Selector;
import com.hero.libhero.utils.ActivityUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DbUtil<T> implements DbDao<T> {
    private DbManager dbManager;
    private static DbUtil dbUtil;


    public static DbUtil getInstance() {
        if (dbUtil == null) {
            synchronized (DbUtil.class) {
                if (dbUtil == null) {
                    dbUtil = new DbUtil();
                }
            }
        }
        return dbUtil;
    }


    private DbUtil() {
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
                .setDbName(ActivityUtil.getAppLastName() + ".db")
                .setDbVersion(1)
                .setDbOpenListener(new DbManager.DbOpenListener() {
                    @Override
                    public void onDbOpened(DbManager db) {
                        db.getDatabase().enableWriteAheadLogging();
                    }
                })
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                    }
                });

        dbManager = XDb.getDb(daoConfig);
    }

    @Override
    public T selectClassByKey(Class<T> clazz, String key, String value) {
        T t = null;
        try {
            t = dbManager.selector(clazz).where(key, "=", value).findFirst();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return t;
    }

    @Override
    public List<T> selectClassByMap(Class<T> clazz, String key, String value, Map<String, Object> map) {
        List<T> list = new ArrayList<>();
        try {
            Selector selector = dbManager.selector(clazz);
            selector.where(key, "=", value);
            if (null != map && map.size() > 0) {
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    String key1 = entry.getKey();
                    Object object = entry.getValue();
                    selector.and(key1, "=", object);
                }
            }

            list = selector.findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return list;
    }


    @Override
    public List<T> selectList(Class<T> clazz) {
        List<T> list = new ArrayList<>();
        try {
            list = dbManager.selector(clazz).findAll();
            if (list == null) {
                list = new ArrayList<>();
            }

        } catch (DbException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<T> selectListByKey(Class<T> clazz, String key, String value) {
        List<T> list = new ArrayList<>();
        try {
            list = dbManager.selector(clazz).where(key, "=", value).findAll();

        } catch (DbException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<T> selectClassByKeyLike(Class<T> clazz, String key, String value) {
        List<T> list = new ArrayList<>();
        try {
            list = dbManager.selector(clazz).where(key, "LIKE", value).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<T> selectListByOrder(Class<T> clazz, String order) {
        List<T> list = new ArrayList<>();
        try {
            list = dbManager.selector(clazz).orderBy(order, true).findAll();

        } catch (DbException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void saveOrUpdate(Object t) {
        try {
            dbManager.saveOrUpdate(t);
            LogUtil.e("saveOrUpdate=成功");
        } catch (DbException e) {
            LogUtil.e("saveOrUpdate失败=" + e.getMessage());
            //e.printStackTrace();
        }
    }

    @Override
    public void deleteById(Class<T> clazz, Object id) {
        try {
            dbManager.deleteById(clazz, id);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void deleteList(Class<T> clazz) {
        try {
            dbManager.delete(clazz);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}
