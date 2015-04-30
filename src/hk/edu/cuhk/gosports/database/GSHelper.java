package hk.edu.cuhk.gosports.database;

import hk.edu.cuhk.gosports.utils.GSConstants;

import java.util.concurrent.atomic.AtomicInteger;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * database base operation
 * 
 * @author EdwardChou edwardchou_gmail_com
 * @date 2015-4-22 下午5:50:26
 * @version V1.0
 * 
 */
public class GSHelper extends SQLiteOpenHelper {

	private final static String TAG = GSHelper.class.getSimpleName();

	// multi-thread security for opening and closing database
	private AtomicInteger mOpenCounter = new AtomicInteger();
	private SQLiteDatabase mDatabase;

	public GSHelper(Context context) {
		super(context, GSConstants.DATABASE_NAME, null,
				GSConstants.DATABASE_VERSION);
	}

	public GSHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(GSConstants.SQL_CREATE_TABLE_EVENTS);
		db.execSQL(GSConstants.SQL_CREATE_TABLE_MY_EVENTS);
		db.execSQL(GSConstants.SQL_CREATE_TABLE_CREDIT);
		db.execSQL(GSConstants.SQL_CREATE_USER);
	}

	@Override
	public synchronized void close() {
		if (mOpenCounter.decrementAndGet() == 0) {
			// Closing database
			super.close();
			mDatabase.close();
		}
	}

	@Override
	public synchronized SQLiteDatabase getReadableDatabase() {
		return super.getReadableDatabase();
	}

	@Override
	public synchronized SQLiteDatabase getWritableDatabase() {
		if (mOpenCounter.incrementAndGet() == 1) {
			// Opening new database
			mDatabase = super.getWritableDatabase();
		}
		return mDatabase;
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// db.execSQL("DROP TABLE IF EXISTS " + GSConstants.TABLE_INBOX);
		db.execSQL("DROP TABLE IF EXISTS "
				+ GSConstants.SQL_CREATE_TABLE_EVENTS);
		db.execSQL("DROP TABLE IF EXISTS "
				+ GSConstants.SQL_CREATE_TABLE_MY_EVENTS);
		db.execSQL("DROP TABLE IF EXISTS "
				+ GSConstants.SQL_CREATE_TABLE_CREDIT);
		db.execSQL("DROP TABLE IF EXISTS " + GSConstants.SQL_CREATE_USER);
		Log.i(TAG, "Upgrade database success!");
		onCreate(db);
	}
}
