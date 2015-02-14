package com.example.tobias.androidclientif.Persistence_Layer;

/**
 * Created by Tobias on 29.01.15.
 */
 import android.content.ContentValues;
 import android.content.Context;
 import android.database.Cursor;
 import android.database.SQLException;
 import android.database.sqlite.SQLiteDatabase;
 import android.database.sqlite.SQLiteOpenHelper;
 import android.util.Log;

 import com.example.tobias.androidclientif.Entities.Assignment;

 import java.util.ArrayList;
 import java.util.List;

public class MySQLiteHelper extends SQLiteOpenHelper {

        //Table names
        public static final String TABLE_ASSIGNMENTS = "assignments";
        public static final String TABLE_TASKS = "tasks";

        //Column names table assignments
        public static final String A_COLUMN_ROWID = "_id";
        public static final String A_COLUMN_DESCRIPTION = "description";
        public static final String A_COLUMN_ASSIGNMENTNAME = "assignmentName";
        public static final String A_COLUMN_ASSIGNMENT_ID = "assignmentId";
        public static final String A_COLUMN_STARTDATE = "startDate";
        public static final String A_COLUMN_ENDDATE = "endDate";

        //Column names table tasks
        public static final String T_COLUMN_ROWID = "_id";
        public static final String T_COLUMN_TASKNAME = "taskName";
        public static final String T_COLUMN_DESCRIPTION = "description";
        public static final String T_COLUMN_STATE = "state";
        public static final String T_TASK_ID = "taskId";
        public static final String T_PK = "PK";

        //Database information
        private static final String DATABASE_NAME = "IFtestdatabases.db";
        private static final int DATABASE_VERSION = 9;

        // Assignment Table creation sql statement
        private static final String CREATE_TABLE_ASSIGNMENTS = "CREATE TABLE "
                + TABLE_ASSIGNMENTS + "(" + A_COLUMN_ROWID + " INTEGER, " + A_COLUMN_ASSIGNMENT_ID + " TEXT PRIMARY KEY UNIQUE, "
                + A_COLUMN_DESCRIPTION + " TEXT, " + A_COLUMN_ASSIGNMENTNAME + " TEXT, " + A_COLUMN_STARTDATE + " INTEGER, " + A_COLUMN_ENDDATE + " INTEGER)";

        // Task Table creation sql statement
        private static final String CREATE_TABLE_TASKS = "CREATE TABLE "
                + TABLE_TASKS + "(" + T_COLUMN_ROWID + " INTEGER, " + T_COLUMN_TASKNAME + " TEXT, " + T_COLUMN_DESCRIPTION + " TEXT, "
                + T_COLUMN_STATE + " INTEGER, " + T_TASK_ID + " TEXT PRIMARY KEY, " + T_PK + " TEXT, " + " FOREIGN KEY(PK) REFERENCES TABLE_ASSIGNMENTS(assignmentId))";

        public MySQLiteHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase database) {
            database.execSQL(CREATE_TABLE_ASSIGNMENTS);
            database.execSQL(CREATE_TABLE_TASKS);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(MySQLiteHelper.class.getName(),
                    "Upgrading database from version " + oldVersion + " to "
                            + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ASSIGNMENTS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
            onCreate(db);
        }

    //creat a row Assignment
    public void createAssignment(String assignmentId, String assignmentName, String description, Integer startDate, Integer endDate) {

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(MySQLiteHelper.A_COLUMN_DESCRIPTION, description);
        values.put(MySQLiteHelper.A_COLUMN_ASSIGNMENTNAME, assignmentName);
        values.put(MySQLiteHelper.A_COLUMN_ASSIGNMENT_ID, assignmentId);
        values.put(MySQLiteHelper.A_COLUMN_STARTDATE, startDate);
        values.put(MySQLiteHelper.A_COLUMN_ENDDATE, endDate);

        //insert row
        long insertId = database.insert(MySQLiteHelper.TABLE_ASSIGNMENTS, null,
                values);
    }

    public void createTask(String taskId, String taskName, String description, Integer state, String PK ){

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(MySQLiteHelper.T_COLUMN_TASKNAME, taskName);
        values.put(MySQLiteHelper.T_COLUMN_DESCRIPTION, description);
        values.put(MySQLiteHelper.T_COLUMN_STATE, state);
        values.put(MySQLiteHelper.T_PK, PK);

        long insertId = database.insert(MySQLiteHelper.TABLE_TASKS, null,
                values);
    }


    //get all assignments from the database
    //returns a list with all assignments stored in the table assignments

    public List<String> getAllAssignments() {
        List<String> assignments = new ArrayList<String>();
        String selectQuery = "SELECT  * FROM " + MySQLiteHelper.TABLE_ASSIGNMENTS;



        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Assignment assignment = new Assignment();
                assignment.setAssignmentName(c.getString((c.getColumnIndex(A_COLUMN_ASSIGNMENTNAME))));


                // adding to assignment list
                assignments.add(assignment.getAssignmentName());
            } while (c.moveToNext());
        }

        return assignments;
    }

    // closing database
    public void closeDB() {
        SQLiteDatabase database = this.getReadableDatabase();
        if (database != null && database.isOpen())
            database.close();
    }

    }

