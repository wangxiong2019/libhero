package com.hero.libhero.mydb.dbinit.converter;

import android.database.Cursor;

import com.hero.libhero.mydb.dbinit.sqlite.ColumnDbType;

public interface ColumnConverter<T> {

    T getFieldValue(final Cursor cursor, int index);

    Object fieldValue2DbValue(T fieldValue);

    ColumnDbType getColumnDbType();
}
