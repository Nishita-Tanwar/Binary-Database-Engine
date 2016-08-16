package edu.utdallas.nxt150030;

import java.io.RandomAccessFile;
import java.io.File;

/**
 * Created by nishitatanwar on 4/5/16.
 */

public class MakeInformationSchema {

    static int schemaTable_records = 1;
    static int tableTable_records =3;
    static String schemaMain = "information_schema";

    static String schemataTableFileName = "information_schema.schemata.tbl";
    static String tablesTableFileName = "information_schema.tables.tbl";
    static String columnsTableFileName = "information_schema.columns.tbl";
    static String schemanameIndexFileName = "information_schema.schemata.schema_name.ndx";
    static String tableschemaIndexFileName = "information_schema.tables.table_schema.ndx";
    static String tablenameIndexFileName = "information_schema.tables.table_name.ndx";
    static String tablerowsIndexFileName = "information_schema.tables.table_rows.ndx";
    static String columnsTableschemaIndexFileName = "information_schema.columns.table_schema.ndx";
    static String columnsTablenameIndexFileName = "information_schema.columns.table_name.ndx";
    static String columnsnameIndexFileName = "information_schema.columns.column_name.ndx";
    static String columnsordposIndexFileName = "information_schema.columns.ordinal_position.ndx";
    static String columntypeIndexFileName = "information_schema.columns.column_type.ndx";
    static String columnsnulIndexFileName = "information_schema.columns.is_nullable.ndx";
    static String columnskeyIndexFileName = "information_schema.columns.column_key.ndx";

    public static final String PRIMARY_KEY = "PRI";

    public static void initialize() throws Exception{


        RandomAccessFile schemataTableFile = Main.initializeTableFile(schemataTableFileName);
        RandomAccessFile tablesTableFile = Main.initializeTableFile(tablesTableFileName);
        RandomAccessFile columnsTableFile = Main.initializeTableFile(columnsTableFileName);
        RandomAccessFile schemanameIndexFile = Main.initializeTableFile(schemanameIndexFileName);
        RandomAccessFile tableschemaIndexFile = Main.initializeTableFile(tableschemaIndexFileName);
        RandomAccessFile tablenameIndexFile = Main.initializeTableFile(tablenameIndexFileName);
        RandomAccessFile tablerowsIndexFile = Main.initializeTableFile(tablerowsIndexFileName);
        RandomAccessFile columnsTableschemaIndexFile = Main.initializeTableFile(columnsTableschemaIndexFileName);
        RandomAccessFile columnsTablenameIndexFile = Main.initializeTableFile(columnsTablenameIndexFileName);
        RandomAccessFile columnsnameIndexFile = Main.initializeTableFile(columnsnameIndexFileName);
        RandomAccessFile columnsordposIndexFile = Main.initializeTableFile(columnsordposIndexFileName);
        RandomAccessFile columntypeIndexFile = Main.initializeTableFile(columntypeIndexFileName);
        RandomAccessFile columnsnulIndexFile = Main.initializeTableFile(columnsnulIndexFileName);
        RandomAccessFile columnskeyIndexFile = Main.initializeTableFile(columnskeyIndexFileName);

        if (schemataTableFile.length()==0) {

			/*
			 *  Create the SCHEMATA table file.
			 *  Initially it has only one entry:
			 *      information_schema
			 */
                // ROW 1: information_schema.schemata.tbl
                long startPointer = schemataTableFile.getFilePointer();
                schemataTableFile.writeByte("information_schema".length());
                schemataTableFile.writeBytes("information_schema");
                schemanameIndexFile.writeBytes("information_schema");
                schemanameIndexFile.writeInt(1);
                schemanameIndexFile.writeLong(startPointer);

			/*
			 *  Create the TABLES table file.
			 *  Remember!!! Column names are not stored in the tables themselves
			 *              The column names (TABLE_SCHEMA, TABLE_NAME, TABLE_ROWS)
			 *              and their order (ORDINAL_POSITION) are encoded in the
			 *              COLUMNS table.
			 *  Initially it has three rows (each row may have a different length):
			 */
                // ROW 1: information_schema.tables.tbl
                long startPointer1 = tablesTableFile.getFilePointer();
                tablesTableFile.writeByte("information_schema".length()); // TABLE_SCHEMA
                tablesTableFile.writeBytes("information_schema");

                tableschemaIndexFile.writeBytes("information_schema"); //Index for table_schema
                tableschemaIndexFile.writeInt(1);
                tableschemaIndexFile.writeLong(startPointer1);

                tablesTableFile.writeByte("SCHEMATA".length()); // TABLE_NAME
                tablesTableFile.writeBytes("SCHEMATA");

                tablenameIndexFile.writeBytes("SCHEMATA"); //Index for table_name
                tablenameIndexFile.writeInt(1);
                tablenameIndexFile.writeLong(startPointer1);

                tablesTableFile.writeLong(1); // TABLE_ROWS

                tablerowsIndexFile.writeLong(1); //Index for table_row
                tablerowsIndexFile.writeInt(1);
                tablerowsIndexFile.writeLong(startPointer1);

                // ROW 2: information_schema.tables.tbl
                tablesTableFile.writeByte("information_schema".length()); // TABLE_SCHEMA
                tablesTableFile.writeBytes("information_schema");

                tableschemaIndexFile.writeBytes("information_schema"); //Index for table_schema
                tableschemaIndexFile.writeInt(1);
                tableschemaIndexFile.writeLong(startPointer1);

                tablesTableFile.writeByte("TABLES".length()); // TABLE_NAME
                tablesTableFile.writeBytes("TABLES");

                tablenameIndexFile.writeBytes("TABLES"); //Index for table_name
                tablenameIndexFile.writeInt(1);
                tablenameIndexFile.writeLong(startPointer1);

                tablesTableFile.writeLong(3); // TABLE_ROWS

                tablerowsIndexFile.writeLong(3); //Index for table_row
                tablerowsIndexFile.writeInt(1);
                tablerowsIndexFile.writeLong(startPointer1);

                // ROW 3: information_schema.tables.tbl
                tablesTableFile.writeByte("information_schema".length()); // TABLE_SCHEMA
                tablesTableFile.writeBytes("information_schema");

                tableschemaIndexFile.writeBytes("information_schema"); //Index for table_schema
                tableschemaIndexFile.writeInt(1);
                tableschemaIndexFile.writeLong(startPointer1);

                tablesTableFile.writeByte("COLUMNS".length()); // TABLE_NAME
                tablesTableFile.writeBytes("COLUMNS");

                tablenameIndexFile.writeBytes("COLUMNS"); //Index for table_name
                tablenameIndexFile.writeInt(1);
                tablenameIndexFile.writeLong(startPointer1);

                tablesTableFile.writeLong(7); // TABLE_ROWS

                tablerowsIndexFile.writeLong(7); //Index for table_row
                tablerowsIndexFile.writeInt(1);
                tablerowsIndexFile.writeLong(startPointer1);

			/*
			 *  Create the COLUMNS table file.
			 *  Initially it has 11 rows:
			 */
            long startPointer2 = columnsTableFile.getFilePointer();
                // ROW 1: information_schema.columns.tbl
                columnsTableFile.writeByte("information_schema".length()); // TABLE_SCHEMA
                columnsTableFile.writeBytes("information_schema");

                columnsTableschemaIndexFile.writeBytes("information_schema"); //Index for table_schema
                columnsTableschemaIndexFile.writeInt(1);
                columnsTableschemaIndexFile.writeLong(startPointer2);

                columnsTableFile.writeByte("SCHEMATA".length()); // TABLE_NAME
                columnsTableFile.writeBytes("SCHEMATA");

                columnsTablenameIndexFile.writeBytes("SCHEMATA"); //Index for table_name
                columnsTablenameIndexFile.writeInt(1);
                columnsTablenameIndexFile.writeLong(startPointer2);

                columnsTableFile.writeByte("SCHEMA_NAME".length()); // COLUMN_NAME
                columnsTableFile.writeBytes("SCHEMA_NAME");

                columnsnameIndexFile.writeBytes("SCHEMA_NAME"); //Index for column_name
                columnsnameIndexFile.writeInt(1);
                columnsnameIndexFile.writeLong(startPointer2);

                columnsTableFile.writeInt(1); // ORDINAL_POSITION

                columnsordposIndexFile.writeInt(1); //Index for ORDINAL_POSITION
                columnsordposIndexFile.writeInt(1);
                columnsordposIndexFile.writeLong(startPointer2);

                columnsTableFile.writeByte("varchar(64)".length()); // COLUMN_TYPE
                columnsTableFile.writeBytes("varchar(64)");

                columntypeIndexFile.writeBytes("varchar(64)"); //Index for column_type
                columntypeIndexFile.writeInt(1);
                columntypeIndexFile.writeLong(startPointer2);

                columnsTableFile.writeByte("NO".length()); // IS_NULLABLE
                columnsTableFile.writeBytes("NO");

                columnsnulIndexFile.writeBytes("NO"); //Index for is_nullable
                columnsnulIndexFile.writeInt(1);
                columnsnulIndexFile.writeLong(startPointer2);

                columnsTableFile.writeByte("".length()); // COLUMN_KEY
                columnsTableFile.writeBytes("");

                columnskeyIndexFile.writeBytes(""); //Index for column_key
                columnskeyIndexFile.writeInt(1);
                columnskeyIndexFile.writeLong(startPointer2);

                // ROW 2: information_schema.columns.tbl
                columnsTableFile.writeByte("information_schema".length()); // TABLE_SCHEMA
                columnsTableFile.writeBytes("information_schema");

                columnsTableschemaIndexFile.writeBytes("information_schema"); //Index for table_schema
                columnsTableschemaIndexFile.writeInt(1);
                columnsTableschemaIndexFile.writeLong(startPointer2);

                columnsTableFile.writeByte("TABLES".length()); // TABLE_NAME
                columnsTableFile.writeBytes("TABLES");

                columnsTablenameIndexFile.writeBytes("TABLES"); //Index for table_name
                columnsTablenameIndexFile.writeInt(1);
                columnsTablenameIndexFile.writeLong(startPointer2);

                columnsTableFile.writeByte("TABLE_SCHEMA".length()); // COLUMN_NAME
                columnsTableFile.writeBytes("TABLE_SCHEMA");

                columnsnameIndexFile.writeBytes("TABLE_SCHEMA"); //Index for column_name
                columnsnameIndexFile.writeInt(1);
                columnsnameIndexFile.writeLong(startPointer2);

                columnsTableFile.writeInt(1); // ORDINAL_POSITION

                columnsordposIndexFile.writeInt(1); //Index for ORDINAL_POSITION
                columnsordposIndexFile.writeInt(1);
                columnsordposIndexFile.writeLong(startPointer2);

                columnsTableFile.writeByte("varchar(64)".length()); // COLUMN_TYPE
                columnsTableFile.writeBytes("varchar(64)");

                columntypeIndexFile.writeBytes("varchar(64)"); //Index for column_type
                columntypeIndexFile.writeInt(1);
                columntypeIndexFile.writeLong(startPointer2);

                columnsTableFile.writeByte("NO".length()); // IS_NULLABLE
                columnsTableFile.writeBytes("NO");

                columnsnulIndexFile.writeBytes("NO"); //Index for is_nullable
                columnsnulIndexFile.writeInt(1);
                columnsnulIndexFile.writeLong(startPointer2);

                columnsTableFile.writeByte("".length()); // COLUMN_KEY
                columnsTableFile.writeBytes("");

                columnskeyIndexFile.writeBytes(""); //Index for column_key
                columnskeyIndexFile.writeInt(1);
                columnskeyIndexFile.writeLong(startPointer2);

                // ROW 3: information_schema.columns.tbl
                columnsTableFile.writeByte("information_schema".length()); // TABLE_SCHEMA
                columnsTableFile.writeBytes("information_schema");

                columnsTableschemaIndexFile.writeBytes("information_schema"); //Index for table_schema
                columnsTableschemaIndexFile.writeInt(1);
                columnsTableschemaIndexFile.writeLong(startPointer2);

                columnsTableFile.writeByte("TABLES".length()); // TABLE_NAME
                columnsTableFile.writeBytes("TABLES");

                columnsTablenameIndexFile.writeBytes("TABLES"); //Index for table_name
                columnsTablenameIndexFile.writeInt(1);
                columnsTablenameIndexFile.writeLong(startPointer2);

                columnsTableFile.writeByte("TABLE_NAME".length()); // COLUMN_NAME
                columnsTableFile.writeBytes("TABLE_NAME");

                columnsnameIndexFile.writeBytes("TABLE_NAME"); //Index for column_name
                columnsnameIndexFile.writeInt(1);
                columnsnameIndexFile.writeLong(startPointer2);

                columnsTableFile.writeInt(2); // ORDINAL_POSITION

                columnsordposIndexFile.writeInt(2); //Index for ORDINAL_POSITION
                columnsordposIndexFile.writeInt(1);
                columnsordposIndexFile.writeLong(startPointer2);

                columnsTableFile.writeByte("varchar(64)".length()); // COLUMN_TYPE
                columnsTableFile.writeBytes("varchar(64)");

                columntypeIndexFile.writeBytes("varchar(64)"); //Index for column_type
                columntypeIndexFile.writeInt(1);
                columntypeIndexFile.writeLong(startPointer2);

                columnsTableFile.writeByte("NO".length()); // IS_NULLABLE
                columnsTableFile.writeBytes("NO");

                columnsnulIndexFile.writeBytes("NO"); //Index for is_nullable
                columnsnulIndexFile.writeInt(1);
                columnsnulIndexFile.writeLong(startPointer2);

                columnsTableFile.writeByte("".length()); // COLUMN_KEY
                columnsTableFile.writeBytes("");

                columnskeyIndexFile.writeBytes(""); //Index for column_key
                columnskeyIndexFile.writeInt(1);
                columnskeyIndexFile.writeLong(startPointer2);

                // ROW 4: information_schema.columns.tbl
                columnsTableFile.writeByte("information_schema".length()); // TABLE_SCHEMA
                columnsTableFile.writeBytes("information_schema");

                columnsTableschemaIndexFile.writeBytes("information_schema"); //Index for table_schema
                columnsTableschemaIndexFile.writeInt(1);
                columnsTableschemaIndexFile.writeLong(startPointer2);

                columnsTableFile.writeByte("TABLES".length()); // TABLE_NAME
                columnsTableFile.writeBytes("TABLES");

                columnsTablenameIndexFile.writeBytes("TABLES"); //Index for table_name
                columnsTablenameIndexFile.writeInt(1);
                columnsTablenameIndexFile.writeLong(startPointer2);

                columnsTableFile.writeByte("TABLE_ROWS".length()); // COLUMN_NAME
                columnsTableFile.writeBytes("TABLE_ROWS");

                columnsnameIndexFile.writeBytes("TABLE_ROWS"); //Index for column_name
                columnsnameIndexFile.writeInt(1);
                columnsnameIndexFile.writeLong(startPointer2);

                columnsTableFile.writeInt(3); // ORDINAL_POSITION

                columnsordposIndexFile.writeInt(3); //Index for ORDINAL_POSITION
                columnsordposIndexFile.writeInt(1);
                columnsordposIndexFile.writeLong(startPointer2);

                columnsTableFile.writeByte("long int".length()); // COLUMN_TYPE
                columnsTableFile.writeBytes("long int");

                columntypeIndexFile.writeBytes("varchar(64)"); //Index for column_type
                columntypeIndexFile.writeInt(1);
                columntypeIndexFile.writeLong(startPointer2);

                columnsTableFile.writeByte("NO".length()); // IS_NULLABLE
                columnsTableFile.writeBytes("NO");

                columnsnulIndexFile.writeBytes("NO"); //Index for is_nullable
                columnsnulIndexFile.writeInt(1);
                columnsnulIndexFile.writeLong(startPointer2);

                columnsTableFile.writeByte("".length()); // COLUMN_KEY
                columnsTableFile.writeBytes("");

                columnskeyIndexFile.writeBytes(""); //Index for column_key
                columnskeyIndexFile.writeInt(1);
                columnskeyIndexFile.writeLong(startPointer2);

                // ROW 5: information_schema.columns.tbl
                columnsTableFile.writeByte("information_schema".length()); // TABLE_SCHEMA
                columnsTableFile.writeBytes("information_schema");

                columnsTableschemaIndexFile.writeBytes("information_schema"); //Index for table_schema
                columnsTableschemaIndexFile.writeInt(1);
                columnsTableschemaIndexFile.writeLong(startPointer2);

                columnsTableFile.writeByte("COLUMNS".length()); // TABLE_NAME
                columnsTableFile.writeBytes("COLUMNS");

                columnsTablenameIndexFile.writeBytes("COLUMNS"); //Index for table_name
                columnsTablenameIndexFile.writeInt(1);
                columnsTablenameIndexFile.writeLong(startPointer2);

                columnsTableFile.writeByte("TABLE_SCHEMA".length()); // COLUMN_NAME
                columnsTableFile.writeBytes("TABLE_SCHEMA");

                columnsnameIndexFile.writeBytes("TABLE_SCHEMA"); //Index for column_name
                columnsnameIndexFile.writeInt(1);
                columnsnameIndexFile.writeLong(startPointer2);

                columnsTableFile.writeInt(1); // ORDINAL_POSITION

                columnsordposIndexFile.writeInt(1); //Index for ORDINAL_POSITION
                columnsordposIndexFile.writeInt(1);
                columnsordposIndexFile.writeLong(startPointer2);

                columnsTableFile.writeByte("varchar(64)".length()); // COLUMN_TYPE
                columnsTableFile.writeBytes("varchar(64)");

                columntypeIndexFile.writeBytes("varchar(64)"); //Index for column_type
                columntypeIndexFile.writeInt(1);
                columntypeIndexFile.writeLong(startPointer2);

                columnsTableFile.writeByte("NO".length()); // IS_NULLABLE
                columnsTableFile.writeBytes("NO");

                columnsnulIndexFile.writeBytes("NO"); //Index for is_nullable
                columnsnulIndexFile.writeInt(1);
                columnsnulIndexFile.writeLong(startPointer2);

                columnsTableFile.writeByte("".length()); // COLUMN_KEY
                columnsTableFile.writeBytes("");

                columnskeyIndexFile.writeBytes(""); //Index for column_key
                columnskeyIndexFile.writeInt(1);
                columnskeyIndexFile.writeLong(startPointer2);

                // ROW 6: information_schema.columns.tbl
                columnsTableFile.writeByte("information_schema".length()); // TABLE_SCHEMA
                columnsTableFile.writeBytes("information_schema");

                columnsTableschemaIndexFile.writeBytes("information_schema"); //Index for table_schema
                columnsTableschemaIndexFile.writeInt(1);
                columnsTableschemaIndexFile.writeLong(startPointer2);

                columnsTableFile.writeByte("COLUMNS".length()); // TABLE_NAME
                columnsTableFile.writeBytes("COLUMNS");

                columnsTablenameIndexFile.writeBytes("COLUMNS"); //Index for table_name
                columnsTablenameIndexFile.writeInt(1);
                columnsTablenameIndexFile.writeLong(startPointer2);

                columnsTableFile.writeByte("TABLE_NAME".length()); // COLUMN_NAME
                columnsTableFile.writeBytes("TABLE_NAME");

                columnsnameIndexFile.writeBytes("TABLE_NAME"); //Index for column_name
                columnsnameIndexFile.writeInt(1);
                columnsnameIndexFile.writeLong(startPointer2);

                columnsTableFile.writeInt(2); // ORDINAL_POSITION

                columnsordposIndexFile.writeInt(2); //Index for ORDINAL_POSITION
                columnsordposIndexFile.writeInt(1);
                columnsordposIndexFile.writeLong(startPointer2);

                columnsTableFile.writeByte("varchar(64)".length()); // COLUMN_TYPE
                columnsTableFile.writeBytes("varchar(64)");

                columntypeIndexFile.writeBytes("varchar(64)"); //Index for column_type
                columntypeIndexFile.writeInt(1);
                columntypeIndexFile.writeLong(startPointer2);

                columnsTableFile.writeByte("NO".length()); // IS_NULLABLE
                columnsTableFile.writeBytes("NO");

                columnsnulIndexFile.writeBytes("NO"); //Index for is_nullable
                columnsnulIndexFile.writeInt(1);
                columnsnulIndexFile.writeLong(startPointer2);

                columnsTableFile.writeByte("".length()); // COLUMN_KEY
                columnsTableFile.writeBytes("");

                columnskeyIndexFile.writeBytes(""); //Index for column_key
                columnskeyIndexFile.writeInt(1);
                columnskeyIndexFile.writeLong(startPointer2);

                // ROW 7: information_schema.columns.tbl
                columnsTableFile.writeByte("information_schema".length()); // TABLE_SCHEMA
                columnsTableFile.writeBytes("information_schema");

                columnsTableschemaIndexFile.writeBytes("information_schema"); //Index for table_schema
                columnsTableschemaIndexFile.writeInt(1);
                columnsTableschemaIndexFile.writeLong(startPointer2);

                columnsTableFile.writeByte("COLUMNS".length()); // TABLE_NAME
                columnsTableFile.writeBytes("COLUMNS");

                columnsTablenameIndexFile.writeBytes("COLUMNS"); //Index for table_name
                columnsTablenameIndexFile.writeInt(1);
                columnsTablenameIndexFile.writeLong(startPointer2);

                columnsTableFile.writeByte("COLUMN_NAME".length()); // COLUMN_NAME
                columnsTableFile.writeBytes("COLUMN_NAME");

                columnsnameIndexFile.writeBytes("COLUMN_NAME"); //Index for column_name
                columnsnameIndexFile.writeInt(1);
                columnsnameIndexFile.writeLong(startPointer2);

                columnsTableFile.writeInt(3); // ORDINAL_POSITION

                columnsordposIndexFile.writeInt(3); //Index for ORDINAL_POSITION
                columnsordposIndexFile.writeInt(1);
                columnsordposIndexFile.writeLong(startPointer2);

                columnsTableFile.writeByte("varchar(64)".length()); // COLUMN_TYPE
                columnsTableFile.writeBytes("varchar(64)");

                columntypeIndexFile.writeBytes("varchar(64)"); //Index for column_type
                columntypeIndexFile.writeInt(1);
                columntypeIndexFile.writeLong(startPointer2);

                columnsTableFile.writeByte("NO".length()); // IS_NULLABLE
                columnsTableFile.writeBytes("NO");

                columnsnulIndexFile.writeBytes("NO"); //Index for is_nullable
                columnsnulIndexFile.writeInt(1);
                columnsnulIndexFile.writeLong(startPointer2);

                columnsTableFile.writeByte("".length()); // COLUMN_KEY
                columnsTableFile.writeBytes("");

                columnskeyIndexFile.writeBytes(""); //Index for column_key
                columnskeyIndexFile.writeInt(1);
                columnskeyIndexFile.writeLong(startPointer2);

                // ROW 8: information_schema.columns.tbl
                columnsTableFile.writeByte("information_schema".length()); // TABLE_SCHEMA
                columnsTableFile.writeBytes("information_schema");

                columnsTableschemaIndexFile.writeBytes("information_schema"); //Index for table_schema
                columnsTableschemaIndexFile.writeInt(1);
                columnsTableschemaIndexFile.writeLong(startPointer2);

                columnsTableFile.writeByte("COLUMNS".length()); // TABLE_NAME
                columnsTableFile.writeBytes("COLUMNS");

                columnsTablenameIndexFile.writeBytes("COLUMNS"); //Index for table_name
                columnsTablenameIndexFile.writeInt(1);
                columnsTablenameIndexFile.writeLong(startPointer2);

                columnsTableFile.writeByte("ORDINAL_POSITION".length()); // COLUMN_NAME
                columnsTableFile.writeBytes("ORDINAL_POSITION");

                columnsnameIndexFile.writeBytes("ORDINAL_POSITION"); //Index for column_name
                columnsnameIndexFile.writeInt(1);
                columnsnameIndexFile.writeLong(startPointer2);

                columnsTableFile.writeInt(4); // ORDINAL_POSITION

                columnsordposIndexFile.writeInt(4); //Index for ORDINAL_POSITION
                columnsordposIndexFile.writeInt(1);
                columnsordposIndexFile.writeLong(startPointer2);

                columnsTableFile.writeByte("int".length()); // COLUMN_TYPE
                columnsTableFile.writeBytes("int");

                columntypeIndexFile.writeBytes("int"); //Index for column_type
                columntypeIndexFile.writeInt(1);
                columntypeIndexFile.writeLong(startPointer2);

                columnsTableFile.writeByte("NO".length()); // IS_NULLABLE
                columnsTableFile.writeBytes("NO");

                columnsnulIndexFile.writeBytes("NO"); //Index for is_nullable
                columnsnulIndexFile.writeInt(1);
                columnsnulIndexFile.writeLong(startPointer2);

                columnsTableFile.writeByte("".length()); // COLUMN_KEY
                columnsTableFile.writeBytes("");

                columnskeyIndexFile.writeBytes(""); //Index for column_key
                columnskeyIndexFile.writeInt(1);
                columnskeyIndexFile.writeLong(startPointer2);

                // ROW 9: information_schema.columns.tbl
                columnsTableFile.writeByte("information_schema".length()); // TABLE_SCHEMA
                columnsTableFile.writeBytes("information_schema");

                columnsTableschemaIndexFile.writeBytes("information_schema"); //Index for table_schema
                columnsTableschemaIndexFile.writeInt(1);
                columnsTableschemaIndexFile.writeLong(startPointer2);

                columnsTableFile.writeByte("COLUMNS".length()); // TABLE_NAME
                columnsTableFile.writeBytes("COLUMNS");

                columnsTablenameIndexFile.writeBytes("COLUMNS"); //Index for table_name
                columnsTablenameIndexFile.writeInt(1);
                columnsTablenameIndexFile.writeLong(startPointer2);

                columnsTableFile.writeByte("COLUMN_TYPE".length()); // COLUMN_NAME
                columnsTableFile.writeBytes("COLUMN_TYPE");

                columnsnameIndexFile.writeBytes("COLUMN_TYPE"); //Index for column_name
                columnsnameIndexFile.writeInt(1);
                columnsnameIndexFile.writeLong(startPointer2);

                columnsTableFile.writeInt(5); // ORDINAL_POSITION

                columnsordposIndexFile.writeInt(5); //Index for ORDINAL_POSITION
                columnsordposIndexFile.writeInt(1);
                columnsordposIndexFile.writeLong(startPointer2);

                columnsTableFile.writeByte("varchar(64)".length()); // COLUMN_TYPE
                columnsTableFile.writeBytes("varchar(64)");

                columntypeIndexFile.writeBytes("varchar(64)"); //Index for column_type
                columntypeIndexFile.writeInt(1);
                columntypeIndexFile.writeLong(startPointer2);

                columnsTableFile.writeByte("NO".length()); // IS_NULLABLE
                columnsTableFile.writeBytes("NO");

                columnsnulIndexFile.writeBytes("NO"); //Index for is_nullable
                columnsnulIndexFile.writeInt(1);
                columnsnulIndexFile.writeLong(startPointer2);

                columnsTableFile.writeByte("".length()); // COLUMN_KEY
                columnsTableFile.writeBytes("");

                columnskeyIndexFile.writeBytes(""); //Index for column_key
                columnskeyIndexFile.writeInt(1);
                columnskeyIndexFile.writeLong(startPointer2);

                // ROW 10: information_schema.columns.tbl
                columnsTableFile.writeByte("information_schema".length()); // TABLE_SCHEMA
                columnsTableFile.writeBytes("information_schema");

                columnsTableschemaIndexFile.writeBytes("information_schema"); //Index for table_schema
                columnsTableschemaIndexFile.writeInt(1);
                columnsTableschemaIndexFile.writeLong(startPointer2);

                columnsTableFile.writeByte("COLUMNS".length()); // TABLE_NAME
                columnsTableFile.writeBytes("COLUMNS");

                columnsTablenameIndexFile.writeBytes("COLUMNS"); //Index for table_name
                columnsTablenameIndexFile.writeInt(1);
                columnsTablenameIndexFile.writeLong(startPointer2);

                columnsTableFile.writeByte("IS_NULLABLE".length()); // COLUMN_NAME
                columnsTableFile.writeBytes("IS_NULLABLE");

                columnsnameIndexFile.writeBytes("IS_NULLABLE"); //Index for column_name
                columnsnameIndexFile.writeInt(1);
                columnsnameIndexFile.writeLong(startPointer2);

                columnsTableFile.writeInt(6); // ORDINAL_POSITION

                columnsordposIndexFile.writeInt(6); //Index for ORDINAL_POSITION
                columnsordposIndexFile.writeInt(1);
                columnsordposIndexFile.writeLong(startPointer2);

                columnsTableFile.writeByte("varchar(3)".length()); // COLUMN_TYPE
                columnsTableFile.writeBytes("varchar(3)");

                columntypeIndexFile.writeBytes("varchar(3)"); //Index for column_type
                columntypeIndexFile.writeInt(1);
                columntypeIndexFile.writeLong(startPointer2);

                columnsTableFile.writeByte("NO".length()); // IS_NULLABLE
                columnsTableFile.writeBytes("NO");

                columnsnulIndexFile.writeBytes("NO"); //Index for is_nullable
                columnsnulIndexFile.writeInt(1);
                columnsnulIndexFile.writeLong(startPointer2);

                columnsTableFile.writeByte("".length()); // COLUMN_KEY
                columnsTableFile.writeBytes("");

                columnskeyIndexFile.writeBytes(""); //Index for column_key
                columnskeyIndexFile.writeInt(1);
                columnskeyIndexFile.writeLong(startPointer2);

                // ROW 11: information_schema.columns.tbl
                columnsTableFile.writeByte("information_schema".length()); // TABLE_SCHEMA
                columnsTableFile.writeBytes("information_schema");

                columnsTableschemaIndexFile.writeBytes("information_schema"); //Index for table_schema
                columnsTableschemaIndexFile.writeInt(1);
                columnsTableschemaIndexFile.writeLong(startPointer2);

                columnsTableFile.writeByte("COLUMNS".length()); // TABLE_NAME
                columnsTableFile.writeBytes("COLUMNS");

                columnsTablenameIndexFile.writeBytes("COLUMNS"); //Index for table_name
                columnsTablenameIndexFile.writeInt(1);
                columnsTablenameIndexFile.writeLong(startPointer2);

                columnsTableFile.writeByte("COLUMN_KEY".length()); // COLUMN_NAME
                columnsTableFile.writeBytes("COLUMN_KEY");

                columnsnameIndexFile.writeBytes("COLUMN_KEY"); //Index for column_name
                columnsnameIndexFile.writeInt(1);
                columnsnameIndexFile.writeLong(startPointer2);

                columnsTableFile.writeInt(7); // ORDINAL_POSITION

                columnsordposIndexFile.writeInt(7); //Index for ORDINAL_POSITION
                columnsordposIndexFile.writeInt(1);
                columnsordposIndexFile.writeLong(startPointer2);

                columnsTableFile.writeByte("varchar(3)".length()); // COLUMN_TYPE
                columnsTableFile.writeBytes("varchar(3)");

                columntypeIndexFile.writeBytes("varchar(3)"); //Index for column_type
                columntypeIndexFile.writeInt(1);
                columntypeIndexFile.writeLong(startPointer2);

                columnsTableFile.writeByte("NO".length()); // IS_NULLABLE
                columnsTableFile.writeBytes("NO");

                columnsnulIndexFile.writeBytes("NO"); //Index for is_nullable
                columnsnulIndexFile.writeInt(1);
                columnsnulIndexFile.writeLong(startPointer2);

                columnsTableFile.writeByte("".length()); // COLUMN_KEY
                columnsTableFile.writeBytes("");

                columnskeyIndexFile.writeBytes(""); //Index for column_key
                columnskeyIndexFile.writeInt(1);
                columnskeyIndexFile.writeLong(startPointer2);

            }

        }
    }
