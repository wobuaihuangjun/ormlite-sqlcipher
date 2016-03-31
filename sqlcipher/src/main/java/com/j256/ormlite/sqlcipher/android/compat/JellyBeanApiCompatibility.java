package com.j256.ormlite.sqlcipher.android.compat;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.CancellationSignal;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

/**
 * Basic class which provides no-op methods for all Android version.
 * <p/>
 * <p>
 * <b>NOTE:</b> Will show as in error if compiled with previous Android versions.
 * </p>
 *
 * @author graywatson
 */
public class JellyBeanApiCompatibility extends BasicApiCompatibility {

    @Override
    public Cursor rawQuery(SQLiteDatabase db, String sql, String[] selectionArgs, CancellationHook cancellationHook) {
        // NOTE: in patched version this is the same as BasicApiCompatibility
        // because SqlCipher supports Android version lower than API level 16 (Jelly Bean)
//		if (cancellationHook == null) {
        return db.rawQuery(sql, selectionArgs);
//		} else {
//			return db.rawQuery(sql, selectionArgs, ((JellyBeanCancellationHook) cancellationHook).cancellationSignal);
//		}
    }

    @Override
    public CancellationHook createCancellationHook() {
        return new JellyBeanCancellationHook();
    }

    protected static class JellyBeanCancellationHook implements CancellationHook {

        private final CancellationSignal cancellationSignal;

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public JellyBeanCancellationHook() {
            this.cancellationSignal = new CancellationSignal();
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public void cancel() {
            cancellationSignal.cancel();
        }
    }
}
