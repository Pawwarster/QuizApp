package pl.pawwar.quizapp.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBOpenHelper(context: Context,
                   factory: SQLiteDatabase.CursorFactory?) :
        SQLiteOpenHelper(context, DATABASE_NAME,
                factory, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        createDb(db)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        //probably I won't be upgrading :)
        //but if so I would do something like this:

        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZ_QUESTION)
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZ)
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTION)
        //onCreate(db)
    }
//    fun addName(name: Name) {
//        val values = ContentValues()
//        values.put(COLUMN_NAME, name.userName)
//        val db = this.writableDatabase
//        db.insert(TABLE_NAME, null, values)
//        db.close()
//    }
//    fun getAllName(): Cursor? {
//        val db = this.readableDatabase
//        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
//    }

    private fun createDb(db: SQLiteDatabase) {
        // creating required tables
        db.execSQL(CREATE_TABLE_QUIZ)
        db.execSQL(CREATE_TABLE_QUESTION)
        db.execSQL(CREATE_TABLE_QUIZ_QESTION)
    }

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "QuizApp.db"

        // Table Names
        private val TABLE_QUIZ = "quizes"
        private val TABLE_QUESTION = "questions"
        private val TABLE_QUIZ_QUESTION = "quizes_questions"

        // Common column names
        private val KEY_ID = "id"
        private val KEY_CREATED_AT = "created_at"

        //QUIZ Table - column names

        //QUESTION Table - column names

        // QUIZ_QUESTIONS Table - column names
        private val KEY_QUIZ_ID = "todo_id"
        private val KEY_QUESTION_ID = "tag_id"

        private val CREATE_TABLE_QUIZ = ("CREATE TABLE " + TABLE_QUIZ
                + "(" + KEY_ID + " INTEGER PRIMARY KEY," + "kolejno kolumny" + " TEXT,"
                + KEY_CREATED_AT + " DATETIME" + ")")

        private val CREATE_TABLE_QUESTION = ("CREATE TABLE " + TABLE_QUESTION
                + "(" + KEY_ID + " INTEGER PRIMARY KEY," + "kolejno kolumny" + " TEXT,"
                + KEY_CREATED_AT + " DATETIME" + ")")

        // quiz_question table create statement
        private val CREATE_TABLE_QUIZ_QESTION = ("CREATE TABLE "
                + TABLE_QUIZ_QUESTION + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_QUIZ_ID + " INTEGER," + KEY_QUESTION_ID + " INTEGER,"
                + KEY_CREATED_AT + " DATETIME" + ")")
    }
}