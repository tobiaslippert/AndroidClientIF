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
 import com.example.tobias.androidclientif.Entities.Attachment;
 import com.example.tobias.androidclientif.Entities.InspectionObject;
 import com.example.tobias.androidclientif.Entities.User;
 import com.example.tobias.androidclientif.Entities.Task;

 import java.util.ArrayList;
 import java.util.List;

    public class MySQLiteHelper extends SQLiteOpenHelper {

        //Table names
        public static final String TABLE_ASSIGNMENTS = "assignments";
        public static final String TABLE_TASKS = "tasks";
        public static final String TABLE_USERS = "users";
        public static final String TABLE_INSPECTIONOBJECTS = "inspectionobjects";
        public static final String TABLE_ATTACHMENTS = "attachments";
        public static final String TABLE_CREDENTIALS = "credentials";

        //Column names table assignments
        public static final String A_COLUMN_ROWID = "_id";
        public static final String A_COLUMN_DESCRIPTION = "description";
        public static final String A_COLUMN_ASSIGNMENTNAME = "assignmentName";
        public static final String A_COLUMN_ASSIGNMENT_ID = "assignmentId";
        public static final String A_COLUMN_STARTDATE = "startDate";
        public static final String A_COLUMN_ENDDATE = "endDate";
        public static final String A_COLUMN_USER_ID = "userId";
        public static final String A_COLUMN_INSPECTIONOBJECT_ID = "inspectionObjectId";
        public static final String A_COLUMN_ISTEMPLATE = "isTemplate";
        public static final String A_COLUMN_STATE = "state";
        public static final String A_COLUMN_VERSION = "version";

        //Column names table tasks
        public static final String T_COLUMN_ROWID = "_id";
        public static final String T_COLUMN_TASKNAME = "taskName";
        public static final String T_COLUMN_DESCRIPTION = "description";
        public static final String T_COLUMN_STATE = "state";
        public static final String T_COLUMN_TASK_ID = "taskId";
        public static final String T_COLUMN_PK = "PK";
        public static final String T_COLUMN_ERROR_DESCRIPTION = "errorDescription";


        //Column names table users
        public static final String U_COLUMN_ROWID = "_id";
        public static final String U_COLUMN_USER_ID = "userId";
        public static final String U_COLUMN_USERNAME = "userName";
        public static final String U_COLUMN_EMAIL = "email";
        public static final String U_COLUMN_ROLE = "role";
        public static final String U_COLUMN_FIRSTNAME = "firstname";
        public static final String U_COLUMN_LASTNAME = "lastname";
        public static final String U_COLUMN_PHONENUMBER = "phoneNumber";
        public static final String U_COLUMN_MOBILENUMBER = "mobileNumber";


        //Column names table inspectionobjects
        public static final String I_COLUMN_ROWID = "_id";
        public static final String I_COLUMN_OBJECT_ID = "objectId";
        public static final String I_COLUMN_OBJECTNAME = "objectName";
        public static final String I_COLUMN_DESCRIPTION = "description";
        public static final String I_COLUMN_LOCATION = "location";
        public static final String I_COLUMN_CUSTOMERNAME = "customername";

        //Column names table attachments
        public static final String AT_COLUMN_ROWID = "_id";
        public static final String AT_COLUMN_ATTACHMENT_ID = "attachmentId";
        public static final String AT_COLUMN_FILE_TYPE = "fileType";
        public static final String AT_COLUMN_BINARY_OBJECT = "binaryObject";
        public static final String AT_COLUMN_FK_TASK_ID = "fkTaskId";
        public static final String AT_COLUMN_FK_ASSIGNMENT_ID = "fkAssignmentId";

        //Column names table credentials
        public static final String C_COLUMN_ROWID = "_id";
        public static final String C_COLUMN_USERNAME = "username";
        public static final String C_COLUMN_PASSWORD = "password";


        //Database information
        private static final String DATABASE_NAME = "DBAndroidClientIF.db";
        private static final int DATABASE_VERSION = 2;

        // Assignment Table creation sql statement
        private static final String CREATE_TABLE_ASSIGNMENTS = "CREATE TABLE "
                + TABLE_ASSIGNMENTS +"(" + A_COLUMN_ROWID + " INTEGER, " + A_COLUMN_ASSIGNMENT_ID + " TEXT PRIMARY KEY UNIQUE, "
                + A_COLUMN_DESCRIPTION + " TEXT, " + A_COLUMN_ASSIGNMENTNAME + " TEXT, " + A_COLUMN_STARTDATE + " INTEGER, " + A_COLUMN_ENDDATE + " INTEGER, "
                + A_COLUMN_ISTEMPLATE + " TEXT, " + A_COLUMN_INSPECTIONOBJECT_ID + " TEXT, "  + A_COLUMN_USER_ID + " TEXT, " + A_COLUMN_STATE + " TEXT, " + A_COLUMN_VERSION + " INTEGER)";

        //Task Table creation sql statement
        private static final String CREATE_TABLE_TASKS = "CREATE TABLE "
                + TABLE_TASKS + "(" + T_COLUMN_ROWID + " INTEGER, " + T_COLUMN_TASKNAME + " TEXT, " + T_COLUMN_DESCRIPTION + " TEXT, "
                + T_COLUMN_STATE + " INTEGER, " + T_COLUMN_TASK_ID + " TEXT PRIMARY KEY, " + T_COLUMN_ERROR_DESCRIPTION + " TEXT, " + T_COLUMN_PK + " TEXT, " + " FOREIGN KEY(PK) REFERENCES TABLE_ASSIGNMENTS(assignmentId))";

        //User Table creation sql statement
        private static final String CREATE_TABLE_USERS = "CREATE TABLE "
                + TABLE_USERS + "(" + U_COLUMN_ROWID + " INTEGER, " + U_COLUMN_USER_ID + " TEXT PRIMARY KEY UNIQUE, " + U_COLUMN_USERNAME + " TEXT, "
                + U_COLUMN_FIRSTNAME + " TEXT, " + U_COLUMN_LASTNAME + " TEXT, " + U_COLUMN_ROLE + " TEXT, " + U_COLUMN_EMAIL + " TEXT, "
                + U_COLUMN_PHONENUMBER + " TEXT, " + U_COLUMN_MOBILENUMBER + " TEXT)";

        //InspectionObject Table creation sql statement
        private static final String CREATE_TABLE_INSPECTIONOBJECTS = "CREATE TABLE "
                + TABLE_INSPECTIONOBJECTS + "(" + I_COLUMN_ROWID + " INTEGER, " + I_COLUMN_OBJECT_ID + " TEXT PRIMARY KEY UNIQUE, " + I_COLUMN_OBJECTNAME + " TEXT, "
                + I_COLUMN_DESCRIPTION + " TEXT, " + I_COLUMN_LOCATION + " TEXT, " + I_COLUMN_CUSTOMERNAME + " TEXT)";

        //Attachment table creation sql statement
        private static final String CREATE_TABLE_ATTACHMENTS = "CREATE TABLE "
                + TABLE_ATTACHMENTS + "(" + AT_COLUMN_ROWID + " INTEGER, " + AT_COLUMN_ATTACHMENT_ID + " TEXT PRIMARY KEY UNIQUE, " + AT_COLUMN_FILE_TYPE + " TEXT, "
                + AT_COLUMN_BINARY_OBJECT + " BLOB, " + AT_COLUMN_FK_ASSIGNMENT_ID + " TEXT, " + AT_COLUMN_FK_TASK_ID + " TEXT, " + " FOREIGN KEY(fkTaskId) REFERENCES TABLE_TASKS(taskId))";

        //Credentials table creation sql statement
        private static final String CREATE_TABLE_CREDENTIALS = "CREATE TABLE "
                + TABLE_CREDENTIALS + "(" + C_COLUMN_ROWID + " INTEGER, " + C_COLUMN_USERNAME + " TEXT PRIMARY KEY UNIQUE, "
                + C_COLUMN_PASSWORD + " TEXT)";

        public MySQLiteHelper(Context context) {
                super(context, DATABASE_NAME, null, DATABASE_VERSION);
            }

            @Override
            public void onCreate(SQLiteDatabase database) {
                database.execSQL(CREATE_TABLE_ASSIGNMENTS);
                database.execSQL(CREATE_TABLE_TASKS);
                database.execSQL(CREATE_TABLE_USERS);
                database.execSQL(CREATE_TABLE_INSPECTIONOBJECTS);
                database.execSQL((CREATE_TABLE_ATTACHMENTS));
                database.execSQL(CREATE_TABLE_CREDENTIALS);
            }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(MySQLiteHelper.class.getName(),
                    "Upgrading database from version " + oldVersion + " to "
                            + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ASSIGNMENTS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_INSPECTIONOBJECTS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTACHMENTS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CREDENTIALS);
            onCreate(db);
        }

        //Create Methods are coded here
        //create a row User
        public void createUser(User user){
            SQLiteDatabase database = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(MySQLiteHelper.U_COLUMN_USER_ID, user.getUserId());
            values.put(MySQLiteHelper.U_COLUMN_USERNAME, user.getUserName());
            values.put(MySQLiteHelper.U_COLUMN_FIRSTNAME, user.getFirstName());
            values.put(MySQLiteHelper.U_COLUMN_LASTNAME, user.getLastName());
            values.put(MySQLiteHelper.U_COLUMN_ROLE, user.getRole());
            values.put(MySQLiteHelper.U_COLUMN_EMAIL, user.getEmail());
            values.put(MySQLiteHelper.U_COLUMN_MOBILENUMBER, user.getMobileNumber());
            values.put(MySQLiteHelper.U_COLUMN_PHONENUMBER, user.getPhoneNumber());


            long insertId = database.insert(MySQLiteHelper.TABLE_USERS, null, values);
        }

        //create a row credentials
        public void createCredentials(User user){
            SQLiteDatabase database = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(MySQLiteHelper.C_COLUMN_USERNAME, user.getUserName());
            values.put(MySQLiteHelper.C_COLUMN_PASSWORD, user.getPassword());


            long insertId = database.insert(MySQLiteHelper.TABLE_CREDENTIALS, null, values);
        }

        //create a row Assignment
        public void createAssignment(Assignment assignment){
            SQLiteDatabase database = this.getWritableDatabase();
            ContentValues values = new ContentValues();


                values.put(MySQLiteHelper.A_COLUMN_DESCRIPTION, assignment.getDescription());
                values.put(MySQLiteHelper.A_COLUMN_ASSIGNMENTNAME, assignment.getAssignmentName());
                values.put(MySQLiteHelper.A_COLUMN_ASSIGNMENT_ID, assignment.getId());
                values.put(MySQLiteHelper.A_COLUMN_STARTDATE, assignment.getStartDate());
                values.put(MySQLiteHelper.A_COLUMN_ENDDATE, assignment.getDueDate());
                values.put(MySQLiteHelper.A_COLUMN_ISTEMPLATE, assignment.getIsTemplate());
                values.put(MySQLiteHelper.A_COLUMN_INSPECTIONOBJECT_ID, assignment.getInspectionObjectId());
                values.put(MySQLiteHelper.A_COLUMN_USER_ID, assignment.getUserId());
                values.put(MySQLiteHelper.A_COLUMN_STATE, assignment.getState());
                values.put(MySQLiteHelper.A_COLUMN_VERSION, assignment.getVersion());

                //insert row
                long insertId = database.insert(MySQLiteHelper.TABLE_ASSIGNMENTS, null,
                        values);

        }

        //Create a row Task
        public void createTask(Task task){

            SQLiteDatabase database = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(MySQLiteHelper.T_COLUMN_TASK_ID, task.getId());
            values.put(MySQLiteHelper.T_COLUMN_TASKNAME, task.getTaskName());
            values.put(MySQLiteHelper.T_COLUMN_DESCRIPTION, task.getDescription());
            values.put(MySQLiteHelper.T_COLUMN_STATE, task.getState());
            values.put(MySQLiteHelper.T_COLUMN_PK, task.getAssignmentId());
            values.put(MySQLiteHelper.T_COLUMN_ERROR_DESCRIPTION, task.getErrorDescription());

            long insertId = database.insert(MySQLiteHelper.TABLE_TASKS, null,
                    values);
        }

        //Create a row InspectionObject
        public void createInspectionObject(InspectionObject inspectionObject){
            SQLiteDatabase database = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(MySQLiteHelper.I_COLUMN_OBJECT_ID, inspectionObject.getId());
            values.put(MySQLiteHelper.I_COLUMN_OBJECTNAME, inspectionObject.getObjectName());
            values.put(MySQLiteHelper.I_COLUMN_DESCRIPTION, inspectionObject.getDescription());
            values.put(MySQLiteHelper.I_COLUMN_LOCATION, inspectionObject.getLocation());
            values.put(MySQLiteHelper.I_COLUMN_CUSTOMERNAME, inspectionObject.getCustomerName());

            long insertId = database.insert(MySQLiteHelper.TABLE_INSPECTIONOBJECTS, null,
                    values);
        }

        //Create a row attachment
        public void createAttachment(Attachment attachment){

            SQLiteDatabase database = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(MySQLiteHelper.AT_COLUMN_ATTACHMENT_ID, attachment.getId());
            values.put(MySQLiteHelper.AT_COLUMN_FILE_TYPE, attachment.getFile_type());
            values.put(MySQLiteHelper.AT_COLUMN_BINARY_OBJECT, attachment.getBinaryObject());
            values.put(MySQLiteHelper.AT_COLUMN_FK_TASK_ID, attachment.getTaskId());
            values.put(MySQLiteHelper.AT_COLUMN_FK_ASSIGNMENT_ID, attachment.getAssignmentId());

            long insertId = database.insert(MySQLiteHelper.TABLE_ATTACHMENTS, null, values);
        }

        //RUD-Methods for Assignments are coded below

        //get all assignments from the database
        //returns a list with all assignments
        public List<Assignment> getAllAssignments() {
            List<Assignment> listAssignments = new ArrayList<Assignment>();
            String selectQuery = "SELECT  * FROM " + MySQLiteHelper.TABLE_ASSIGNMENTS;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (c.moveToFirst()) {
                do {
                    Assignment assignment = new Assignment();
                    assignment.setId(c.getString((c.getColumnIndex(A_COLUMN_ASSIGNMENT_ID))));
                    assignment.setAssignmentName(c.getString((c.getColumnIndex(A_COLUMN_ASSIGNMENTNAME))));
                    assignment.setDescription((c.getString(c.getColumnIndex(A_COLUMN_DESCRIPTION))));
                    assignment.setStartDate((c.getLong(c.getColumnIndex(A_COLUMN_STARTDATE))));
                    assignment.setDueDate((c.getLong(c.getColumnIndex(A_COLUMN_ENDDATE))));
                    assignment.setIsTemplate((c.getString(c.getColumnIndex(A_COLUMN_ISTEMPLATE))));
                    assignment.setState((c.getInt(c.getColumnIndex(A_COLUMN_STATE))));
                    assignment.setInspectionObjectId((c.getString(c.getColumnIndex(A_COLUMN_INSPECTIONOBJECT_ID))));
                    assignment.setUserId((c.getString(c.getColumnIndex(A_COLUMN_USER_ID))));
                    assignment.setVersion(c.getInt(c.getColumnIndex(A_COLUMN_VERSION)));
                    // adding to assignment list
                    listAssignments.add(assignment);
                } while (c.moveToNext());
            }

            return listAssignments;
        }

        // Get assignment with given assignment ID
        public Assignment getAssignmentById(String assignmentId) {
            String selectQuery = "SELECT * FROM " + MySQLiteHelper.TABLE_ASSIGNMENTS + " WHERE " + A_COLUMN_ASSIGNMENT_ID + " = " + "'" + assignmentId + "'";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);
            c.moveToFirst();

            Assignment assignment = new Assignment();
            assignment.setId(c.getString(c.getColumnIndex(A_COLUMN_ASSIGNMENT_ID)));
            assignment.setAssignmentName(c.getString((c.getColumnIndex(A_COLUMN_ASSIGNMENTNAME))));
            assignment.setDescription(c.getString(c.getColumnIndex(A_COLUMN_DESCRIPTION)));
            assignment.setStartDate(c.getLong(c.getColumnIndex(A_COLUMN_STARTDATE)));
            assignment.setDueDate(c.getLong(c.getColumnIndex(A_COLUMN_ENDDATE)));
            assignment.setIsTemplate(c.getString(c.getColumnIndex(A_COLUMN_ISTEMPLATE)));
            assignment.setUserId(c.getString(c.getColumnIndex(A_COLUMN_USER_ID)));
            assignment.setState((c.getInt(c.getColumnIndex(A_COLUMN_STATE))));
            assignment.setInspectionObjectId((c.getString(c.getColumnIndex(A_COLUMN_INSPECTIONOBJECT_ID))));
            assignment.setVersion(c.getInt(c.getColumnIndex(A_COLUMN_VERSION)));

            db.close();
            return assignment;
        }

        // Get all assignments for a given user ID
        public List<Assignment> getAssignmentsByUserId(String ID) {
            List<Assignment> listAssignments = new ArrayList<Assignment>();
            String selectQuery = "SELECT * FROM " + MySQLiteHelper.TABLE_ASSIGNMENTS + " WHERE " + A_COLUMN_USER_ID + " = " + "'" + ID + "'";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (c.moveToFirst()) {
                do {
                    Assignment assignment = new Assignment();
                    assignment.setId(c.getString((c.getColumnIndex(A_COLUMN_ASSIGNMENT_ID))));
                    assignment.setAssignmentName(c.getString((c.getColumnIndex(A_COLUMN_ASSIGNMENTNAME))));
                    assignment.setDescription((c.getString(c.getColumnIndex(A_COLUMN_DESCRIPTION))));
                    assignment.setStartDate((c.getLong(c.getColumnIndex(A_COLUMN_STARTDATE))));
                    assignment.setDueDate((c.getLong(c.getColumnIndex(A_COLUMN_ENDDATE))));
                    assignment.setIsTemplate((c.getString(c.getColumnIndex(A_COLUMN_ISTEMPLATE))));
                    assignment.setState((c.getInt(c.getColumnIndex(A_COLUMN_STATE))));
                    assignment.setInspectionObjectId((c.getString(c.getColumnIndex(A_COLUMN_INSPECTIONOBJECT_ID))));
                    assignment.setUserId((c.getString(c.getColumnIndex(A_COLUMN_USER_ID))));
                    assignment.setVersion(c.getInt(c.getColumnIndex(A_COLUMN_VERSION)));

                    // adding to assignment list
                    listAssignments.add(assignment);
                } while (c.moveToNext());
            }

            return listAssignments;
        }

        //Update an assignment
        public int updateAssignment(Assignment assignment) {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(A_COLUMN_ASSIGNMENT_ID, assignment.getId());
            values.put(A_COLUMN_ASSIGNMENTNAME, assignment.getAssignmentName());
            values.put(A_COLUMN_DESCRIPTION, assignment.getDescription());
            values.put(A_COLUMN_ISTEMPLATE, assignment.getIsTemplate());
            values.put(A_COLUMN_USER_ID, assignment.getUserId());
            values.put(A_COLUMN_INSPECTIONOBJECT_ID, assignment.getInspectionObjectId());
            values.put(A_COLUMN_STARTDATE, assignment.getStartDate());
            values.put(A_COLUMN_ENDDATE, assignment.getDueDate());
            values.put(A_COLUMN_STATE, assignment.getState());
            values.put(MySQLiteHelper.A_COLUMN_VERSION, assignment.getVersion());

            // updating row
            return db.update(TABLE_ASSIGNMENTS, values, A_COLUMN_ASSIGNMENT_ID + " = ?",
                    new String[] { String.valueOf(assignment.getId()) });
        }

        //Delete an assignment with all related tasks
        public void deleteAssignment(String assignmentId) {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_ASSIGNMENTS, A_COLUMN_ASSIGNMENT_ID + " = ?",
                    new String[] { String.valueOf(assignmentId) });
            db.delete(TABLE_TASKS, T_COLUMN_PK + " = ?",
                    new String [] { String.valueOf(assignmentId)});
        }

        //RUD-Methods for inspectionObjects

        //Read inspectionObject with given object ID
        public InspectionObject getInspectionObjectById(String inspectionObjectId) {
            String selectQuery = "SELECT * FROM " + MySQLiteHelper.TABLE_INSPECTIONOBJECTS + " WHERE " + I_COLUMN_OBJECT_ID + " = " + "'" + inspectionObjectId + "'";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);
            c.moveToFirst();

            InspectionObject inspectionObject = new InspectionObject();
            inspectionObject.setId(c.getString(c.getColumnIndex(I_COLUMN_OBJECT_ID)));
            inspectionObject.setObjectName(c.getString((c.getColumnIndex(I_COLUMN_OBJECTNAME))));
            inspectionObject.setDescription(c.getString(c.getColumnIndex(I_COLUMN_DESCRIPTION)));
            inspectionObject.setCustomerName(c.getString(c.getColumnIndex(I_COLUMN_CUSTOMERNAME)));
            inspectionObject.setLocation(c.getString(c.getColumnIndex(I_COLUMN_LOCATION)));
            return inspectionObject;
        }

        //Update an inspectionObject
        public int updateInspectionObject(InspectionObject inspectionObject){
            SQLiteDatabase database = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(MySQLiteHelper.I_COLUMN_OBJECT_ID, inspectionObject.getId());
            values.put(MySQLiteHelper.I_COLUMN_OBJECTNAME, inspectionObject.getObjectName());
            values.put(MySQLiteHelper.I_COLUMN_DESCRIPTION, inspectionObject.getDescription());
            values.put(MySQLiteHelper.I_COLUMN_LOCATION, inspectionObject.getLocation());
            values.put(MySQLiteHelper.I_COLUMN_CUSTOMERNAME, inspectionObject.getCustomerName());

            return database.update(TABLE_INSPECTIONOBJECTS, values, I_COLUMN_ROWID + " = ?",
                    new String[] { String.valueOf(inspectionObject.getId()) });
        }

        //Delete an inspectionObject
        public void deleteInspectionObject(String objectId) {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_INSPECTIONOBJECTS, I_COLUMN_OBJECT_ID + " = ?",
                    new String[] { String.valueOf(objectId) });
        }
        //RUD-Methods for User

        //Read all users from the local database
        //returns a list with all users
        public List<User> getAllUser() {
            List<User> listUser = new ArrayList<>();
            String selectQuery = "SELECT  * FROM " + MySQLiteHelper.TABLE_USERS;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (c.moveToFirst()) {
                do {
                    User user = new User();
                    user.setUserName(c.getString((c.getColumnIndex(U_COLUMN_USERNAME))));
                    user.setUserId(c.getString((c.getColumnIndex(U_COLUMN_USER_ID))));
                    user.setFirstName(c.getString((c.getColumnIndex(U_COLUMN_FIRSTNAME))));
                    user.setLastName(c.getString((c.getColumnIndex(U_COLUMN_LASTNAME))));
                    user.setEmail(c.getString((c.getColumnIndex(U_COLUMN_EMAIL))));
                    user.setRole(c.getString((c.getColumnIndex(U_COLUMN_ROLE))));
                    user.setMobileNumber(c.getString((c.getColumnIndex(U_COLUMN_MOBILENUMBER))));
                    user.setPhoneNumber(c.getString(c.getColumnIndex(U_COLUMN_PHONENUMBER)));
                    // adding to assignment list
                    listUser.add(user);
                } while (c.moveToNext());
            }

            return listUser;
        }
        //Read a user with a specific ID from the local database
        //Return a user
        public User getUserByUserId(String userId){
            User user = new User();
            String selectQuery = "SELECT * FROM " + MySQLiteHelper.TABLE_USERS + " WHERE " + U_COLUMN_USER_ID + " = " + "'" + userId + "'";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);
            c.moveToFirst();

            user.setUserName(c.getString((c.getColumnIndex(U_COLUMN_USERNAME))));
            user.setUserId(c.getString((c.getColumnIndex(U_COLUMN_USER_ID))));
            user.setFirstName(c.getString((c.getColumnIndex(U_COLUMN_FIRSTNAME))));
            user.setLastName(c.getString((c.getColumnIndex(U_COLUMN_LASTNAME))));
            user.setEmail(c.getString((c.getColumnIndex(U_COLUMN_EMAIL))));
            user.setRole(c.getString((c.getColumnIndex(U_COLUMN_ROLE))));
            user.setMobileNumber(c.getString((c.getColumnIndex(U_COLUMN_MOBILENUMBER))));
            user.setPhoneNumber(c.getString(c.getColumnIndex(U_COLUMN_PHONENUMBER)));

            return user;
        }

        public User getUserByUserName(String userName){
            User user = new User();
            String selectQuery = "SELECT * FROM " + MySQLiteHelper.TABLE_USERS + " WHERE " + U_COLUMN_USERNAME + " = " + "'" + userName + "'";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);
            c.moveToFirst();

            user.setUserName(c.getString((c.getColumnIndex(U_COLUMN_USERNAME))));
            user.setUserId(c.getString((c.getColumnIndex(U_COLUMN_USER_ID))));
            user.setFirstName(c.getString((c.getColumnIndex(U_COLUMN_FIRSTNAME))));
            user.setLastName(c.getString((c.getColumnIndex(U_COLUMN_LASTNAME))));
            user.setEmail(c.getString((c.getColumnIndex(U_COLUMN_EMAIL))));
            user.setRole(c.getString((c.getColumnIndex(U_COLUMN_ROLE))));
            user.setMobileNumber(c.getString((c.getColumnIndex(U_COLUMN_MOBILENUMBER))));
            user.setPhoneNumber(c.getString(c.getColumnIndex(U_COLUMN_PHONENUMBER)));

            return user;
        }

        //Update a user
        public int updateUser(User user){
            SQLiteDatabase database = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(MySQLiteHelper.U_COLUMN_USER_ID, user.getUserId());
            values.put(MySQLiteHelper.U_COLUMN_USERNAME, user.getUserName());
            values.put(MySQLiteHelper.U_COLUMN_FIRSTNAME, user.getFirstName());
            values.put(MySQLiteHelper.U_COLUMN_LASTNAME, user.getLastName());
            values.put(MySQLiteHelper.U_COLUMN_ROLE, user.getRole());
            values.put(MySQLiteHelper.U_COLUMN_EMAIL, user.getEmail());
            values.put(MySQLiteHelper.U_COLUMN_MOBILENUMBER, user.getMobileNumber());
            values.put(MySQLiteHelper.U_COLUMN_PHONENUMBER, user.getPhoneNumber());

            return database.update(TABLE_USERS, values, U_COLUMN_ROWID + " = ?",
                    new String[] { String.valueOf(user.getUserId()) });
        }

        //Delete a user
        public void deleteUser(String userId) {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_USERS, U_COLUMN_USER_ID + " = ?",
                    new String[] { String.valueOf(userId) });
        }

        //RUD-Methods for tasks

        //Get all tasks stored in the local database
        public List<Task> getAllTasks() {
            List<Task> tasks = new ArrayList<>();
            String selectQuery = "SELECT  * FROM " + MySQLiteHelper.TABLE_TASKS;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (c.moveToFirst()) {
                do {
                    Task task = new Task();
                    task.setTaskName(c.getString((c.getColumnIndex(T_COLUMN_TASKNAME))));
                    task.setState(c.getInt((c.getColumnIndex(T_COLUMN_STATE))));
                    task.setDescription(c.getString((c.getColumnIndex(T_COLUMN_DESCRIPTION))));
                    task.setId(c.getString((c.getColumnIndex(T_COLUMN_TASK_ID))));
                    task.setAssignmentId(c.getString((c.getColumnIndex(T_COLUMN_PK))));
                    task.setErrorDescription(c.getString(c.getColumnIndex(T_COLUMN_ERROR_DESCRIPTION)));
                    // adding to assignment list
                    tasks.add(task);
                } while (c.moveToNext());
            }

            return tasks;
        }

        //Get all tasks assigned to an assignment using the assignment ID
        public List<Task> getTasksByAssignmentId(String assignmentId) {
            List<Task> taskList = new ArrayList<Task>();
            String selectQuery = "SELECT * FROM " + MySQLiteHelper.TABLE_TASKS + " WHERE " + T_COLUMN_PK + " = " + "'" + assignmentId + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (c.moveToFirst()) {
                do {
                    Task task = new Task();
                    task.setId(c.getString((c.getColumnIndex(T_COLUMN_TASK_ID))));
                    task.setTaskName(c.getString((c.getColumnIndex(T_COLUMN_TASKNAME))));
                    task.setDescription(c.getString((c.getColumnIndex(T_COLUMN_DESCRIPTION))));
                    task.setState(c.getInt(c.getColumnIndex(T_COLUMN_STATE)));
                    task.setAssignmentId(c.getString(c.getColumnIndex(T_COLUMN_PK)));
                    task.setErrorDescription(c.getString(c.getColumnIndex(T_COLUMN_ERROR_DESCRIPTION)));

                    // adding to task list
                    taskList.add(task);
                } while (c.moveToNext());
            }
            db.close();
            return taskList;
        }

        //Get task by taskId
        public Task getTaskByTaskId(String taskId) {
            Task task = new Task();
            String selectQuery = "SELECT * FROM " + MySQLiteHelper.TABLE_TASKS + " WHERE " + T_COLUMN_TASK_ID + " = " + "'" + taskId + "'";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);
            c.moveToFirst();

                    task.setTaskName(c.getString((c.getColumnIndex(T_COLUMN_TASKNAME))));
                    task.setId(c.getString((c.getColumnIndex(T_COLUMN_TASK_ID))));
                    task.setDescription(c.getString((c.getColumnIndex(T_COLUMN_DESCRIPTION))));
                    task.setState(c.getInt(c.getColumnIndex(T_COLUMN_STATE)));
                    task.setAssignmentId(c.getString(c.getColumnIndex(T_COLUMN_PK)));
                    task.setErrorDescription(c.getString(c.getColumnIndex(T_COLUMN_ERROR_DESCRIPTION)));

            db.close();
            return task;
        }

        //Update a task
        public int updateTask(Task task){
            SQLiteDatabase database = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(MySQLiteHelper.T_COLUMN_TASK_ID, task.getId());
            values.put(MySQLiteHelper.T_COLUMN_TASKNAME, task.getTaskName());
            values.put(MySQLiteHelper.T_COLUMN_DESCRIPTION, task.getDescription());
            values.put(MySQLiteHelper.T_COLUMN_STATE, task.getState());
            values.put(MySQLiteHelper.T_COLUMN_PK, task.getAssignmentId());
            values.put(MySQLiteHelper.T_COLUMN_ERROR_DESCRIPTION, task.getErrorDescription());

            return database.update(TABLE_TASKS, values, T_COLUMN_TASK_ID + " = ?",
                    new String[] { String.valueOf(task.getId()) });
        }

        //Delete a task
        public void deleteTask(String taskId) {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_TASKS, T_COLUMN_TASK_ID + " = ?",
                    new String[] { String.valueOf(taskId) });
        }

        //RUD-Methods for Attachment
        //Read an attachment by a given task ID
        public Attachment getAttachmentsByTaskId(String taskId) {
            Attachment attachment = new Attachment();
            String selectQuery = "SELECT * FROM " + MySQLiteHelper.TABLE_ATTACHMENTS + " WHERE " + AT_COLUMN_FK_TASK_ID + " = " + "'" + taskId + "'";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);
            c.moveToFirst();

                    attachment.setId(c.getString((c.getColumnIndex(AT_COLUMN_ATTACHMENT_ID))));
                    attachment.setBinaryObject(c.getBlob(c.getColumnIndex(AT_COLUMN_BINARY_OBJECT)));
                    attachment.setFile_type(c.getString((c.getColumnIndex(AT_COLUMN_FILE_TYPE))));
                    attachment.setTaskId(c.getString((c.getColumnIndex(AT_COLUMN_FK_TASK_ID))));
                    attachment.setAssignmentId(c.getString((c.getColumnIndex(AT_COLUMN_FK_ASSIGNMENT_ID))));

            db.close();
            return attachment;
        }

        //Read an attachment photo by a given task ID
        //Returns a byteArray[]
        public byte[] getAttachmentPhotoByTaskId(String taskId){
            String selectQuery = "SELECT * FROM " + MySQLiteHelper.TABLE_ATTACHMENTS + " WHERE " + AT_COLUMN_FK_TASK_ID + " = " + "'" + taskId + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);
            c.moveToFirst();
            byte[] byteArray = (c.getBlob(c.getColumnIndex(AT_COLUMN_BINARY_OBJECT)));

            db.close();
            return byteArray;

        }

        //Read all attachments by a given assignment ID
        //Returns a list with all attachments assigned to the assignment
        public List<Attachment> getAttachmentsByAssignmentId(String assignmentId) {
            List<Attachment> attachmentList = new ArrayList<>();
            String selectQuery = "SELECT * FROM " + MySQLiteHelper.TABLE_ATTACHMENTS + " WHERE " + AT_COLUMN_FK_ASSIGNMENT_ID + " = " + "'" + assignmentId + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (c.moveToFirst()) {
                do {
                    Attachment attachment = new Attachment();
                    attachment.setId(c.getString((c.getColumnIndex(AT_COLUMN_ATTACHMENT_ID))));
                    attachment.setBinaryObject(c.getBlob(c.getColumnIndex(AT_COLUMN_BINARY_OBJECT)));
                    attachment.setFile_type(c.getString((c.getColumnIndex(AT_COLUMN_FILE_TYPE))));
                    attachment.setTaskId(c.getString((c.getColumnIndex(AT_COLUMN_FK_TASK_ID))));
                    attachment.setAssignmentId(c.getString((c.getColumnIndex(AT_COLUMN_FK_ASSIGNMENT_ID))));


                    // adding to task list
                    attachmentList.add(attachment);
                } while (c.moveToNext());
            }
            db.close();
            return attachmentList;
        }

        //Update an attachment
        public int updateAttachment(Attachment attachment){
            SQLiteDatabase database = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(MySQLiteHelper.AT_COLUMN_ATTACHMENT_ID, attachment.getId());
            values.put(MySQLiteHelper.AT_COLUMN_FILE_TYPE, attachment.getFile_type());
            values.put(MySQLiteHelper.AT_COLUMN_BINARY_OBJECT, attachment.getBinaryObject());
            values.put(MySQLiteHelper.AT_COLUMN_FK_TASK_ID, attachment.getTaskId());
            values.put(MySQLiteHelper.AT_COLUMN_FK_ASSIGNMENT_ID, attachment.getAssignmentId());

            return database.update(TABLE_ATTACHMENTS, values, AT_COLUMN_ATTACHMENT_ID + " = ?",
                    new String[] { String.valueOf(attachment.getId()) });
        }

        //Delete an attachment
        public void deleteAttachment(String taskId) {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_ATTACHMENTS, AT_COLUMN_FK_TASK_ID + " = ?",
                    new String[] { String.valueOf(taskId) });
        }

        //RUD-functions for credentials
        public List<User> getAllCredentials() {
            List<User> listCredentials = new ArrayList<>();
            String selectQuery = "SELECT  * FROM " + MySQLiteHelper.TABLE_CREDENTIALS;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (c.moveToFirst()) {
                do {
                    User user = new User();
                    user.setUserName(c.getString((c.getColumnIndex(C_COLUMN_USERNAME))));
                    user.setPassword(c.getString(c.getColumnIndex(C_COLUMN_PASSWORD)));

                    // adding to assignment list
                    listCredentials.add(user);
                } while (c.moveToNext());
            }

            return listCredentials;
        }

        // Clean the database (delete everything inside)
        public void cleanDatabase() {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_USERS, null, null);
            db.delete(TABLE_ASSIGNMENTS, null, null);
            db.delete(TABLE_ATTACHMENTS, null, null);
            db.delete(TABLE_INSPECTIONOBJECTS, null, null);
            db.delete(TABLE_TASKS, null, null);

            db.close();
        }

        // closing database
        public void closeDB() {
            SQLiteDatabase database = this.getReadableDatabase();
            if (database != null && database.isOpen())
                database.close();
        }

        }

