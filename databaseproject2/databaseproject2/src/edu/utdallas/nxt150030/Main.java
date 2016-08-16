package edu.utdallas.nxt150030;
import java.io.File;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.*;


public class Main {

    static String prompt = "davisql> ";
    static HashMap<String,RandomAccessFile> TableFiles = new HashMap<>();
    public static RandomAccessFile initializeTableFile (String fileName) throws Exception{

            RandomAccessFile tableFile = new RandomAccessFile(fileName, "rw");
            TableFiles.put(fileName, tableFile);
            return tableFile;
    }

    public static RandomAccessFile getTableFile(String schemaName, String tableName) throws Exception {
        RandomAccessFile tableFile = TableFiles.get((schemaName+"."+tableName+".tbl").toLowerCase());
        if(null == tableFile){
            tableFile = new RandomAccessFile(schemaName+"."+tableName+".tbl","rw");
            TableFiles.put((schemaName+"."+tableName+".tbl").toLowerCase(), tableFile);
        }
        return tableFile;
    }

    public static void main(String[] args) throws Exception{
        MakeInformationSchema.initialize();
        splashScreen();

        Scanner scanner = new Scanner(System.in).useDelimiter(";");
        String userCommand; // Variable to collect user input from the prompt

        do {
            System.out.print(prompt);
            userCommand = scanner.next().trim();
            if ("SHOW SCHEMAS".equalsIgnoreCase(userCommand))
            {
                showschemas();
            }
            else if (userCommand.toUpperCase().startsWith("USE"))
            {
                String[] tokens = userCommand.split("\\s"); //splits the string based on string
                useschema(tokens[1]);
            }
            else if ("SHOW TABLES".equalsIgnoreCase(userCommand))
            {
                showtables();
            }
            else if (userCommand.toUpperCase().startsWith("CREATE SCHEMA"))
            {
                String[] tokens = userCommand.split("\\s"); //splits the string based on string
                createschema(tokens[2]);
            }
            else if (userCommand.toUpperCase().startsWith("CREATE TABLE"))
            {
                //how to parse command
                // Parse command - table name, columns with column information
                // insert table name into tables table (table name)
                // insert column information into columns table (table name, column info)
                // create table file
                // create index files for all columns

                String[] tokens = userCommand.split("\\s"); //splits the string based on string
                int i=tokens.length;
                createtable(tokens[2], userCommand);
            }
            else if (userCommand.toUpperCase().startsWith("INSERT INTO TABLE"))
            {
                String[] tokens = userCommand.split("\\s"); //splits the string based on string
                String[] para = userCommand.split("\\(");
                String newpara = para[1].substring(0,para[1].indexOf(')'));
                String[] value = newpara.split(",");
                for(int i = 0; i < value.length; i++){
                    value[i] = value[i].trim();
                }
                insertable(tokens[3], value);
            }
            else if (userCommand.toUpperCase().startsWith("DROP TABLE"))
            {
                //TODO
                String[] tokens = userCommand.split("\\s"); //splits the string based on string
                 droptable(tokens[2]);
            }
            else if (userCommand.toUpperCase().startsWith("SELECT"))
            {
                // parse commands
                // check if table exists
                // read columns table and print column names in order
                // 2 scenarios
                    // without where
                        // read table file and print all rows
                    // with where clause
                        // read index files (schema name, table name, column name, value)
                        // find rows to print from index file (value)
                        // find rows in table file and print (starting location(s) of row(s))


                //TODO
                String[] tokens = userCommand.split("\\s"); //splits the string based on string
                if(tokens.length >= 7){
                    selectable(tokens[3],tokens[5],tokens[6],tokens[7]);
                } else {
                    selectable(tokens[3], null, null, null);
                }

            }
            else if ("help".equalsIgnoreCase(userCommand))
            {
                help();
            }
            else if ("version".equalsIgnoreCase(userCommand))
            {
                version();
            }
            else {
                System.out.println("I didn't understand the command: \"" + userCommand + "\"");
            }

        } while(!userCommand.equals("exit"));
        System.out.println("Exiting...");
    }

    public static void help() {
        System.out.println(line("*",80));
        System.out.println();
        System.out.println("\tSHOW SCHEMAS;         Displays all schemas defined in your database");
        System.out.println("\tUSE schema_name;      Chooses a schema");
        System.out.println("\tSHOW TABLES;          Displays all tables in the currently chosen schema");
        System.out.println("\tCREATE SCHEMA schema_name;            Creates a new schema to hold tables");
        System.out.println("\tCREATE TABLE table_name (\n" + "\tcolumn_name1 data_type(size) [primary key|not null], ..);            Creates a new table schema");
        System.out.println("\tINSERT INTO TABLE table_name VALUES (value1,value2,value3,...);             Inserts a row/record into a table");
        System.out.println("\tDROP TABLE table_name;               Remove a table schema, and all of its contained data");
        System.out.println("\tSELECT *\n" +
                           "\tFROM table_name\n" +
                           "\tWHERE column_name operator value;            Query particular value from database");
        System.out.println("\tversion;       Show the program version.");
        System.out.println("\thelp;          Show this help information");
        System.out.println("\texit;          Exit the program and saves all information in non volatile file.");
        System.out.println();
        System.out.println();
        System.out.println(line("*",80));
    }

    public static void splashScreen() {
        System.out.println(line("*",80));
        System.out.println("Welcome to DavisBase"); // Display the string.
        version();
        System.out.println("Type \"help;\" to display supported commands.");
        System.out.println(line("*",80));
    }

    public static String line(String s,int num) {
        String a = "";
        for(int i=0;i<num;i++) {
            a += s;
        }
        return a;
    }

    public static void version() {
        System.out.println("DavisBase v1.0\n");
    }

    public static void showschemas(){
        try {
            RandomAccessFile schemaTableFile = TableFiles.get(MakeInformationSchema.schemataTableFileName);
            System.out.println("SCHEMA_NAME");
             schemaTableFile.seek(0);

            for(int bite = 0;bite < schemaTableFile.length(); bite++) {
                byte len = schemaTableFile.readByte();
                byte[] schemaNameBytes = new byte[len];
                schemaTableFile.read(schemaNameBytes);
                System.out.println(new String (schemaNameBytes));
                bite= bite+len;
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void useschema(String schemaName){

    try {
        RandomAccessFile schemaTableFile = TableFiles.get(MakeInformationSchema.schemataTableFileName);
        schemaTableFile.seek(0);
        String word;
        if (schemaName.equals(MakeInformationSchema.schemaMain)) {
            System.out.println(MakeInformationSchema.schemaMain);
        } else {
            Boolean flag = true;
            for (long bite = 0; bite < schemaTableFile.length(); bite++) {
                byte len = schemaTableFile.readByte();
                byte[] schemaNameBytes = new byte[len];
                schemaTableFile.read(schemaNameBytes);
                word = new String(schemaNameBytes);
                if (schemaName.equals(word)) {
                    System.out.println(word);
                    flag= true;
                    MakeInformationSchema.schemaMain=word;
                }
                else {
                    flag = false;
                }
                bite = bite + len;
            }
            if (flag == false)
            {
                System.out.println("schema does not exist");
            }
        }
    }
    catch(Exception e) {
        e.printStackTrace();
    }
    }

    public static void showtables(){
        try {
            RandomAccessFile tableTableFile = TableFiles.get(MakeInformationSchema.tablesTableFileName);
            System.out.println("TABLE_SCHEMA | TABLE_NAME | TABLE_ROWS");
            System.out.print("\n");
                tableTableFile.seek(0);
                for (int bite = 0; bite < tableTableFile.length(); bite+=2) {
                    byte len1 = tableTableFile.readByte();
                    byte[] tableNameBytes = new byte[len1];
                    tableTableFile.read(tableNameBytes);
                    System.out.print(new String(tableNameBytes));
                    bite = bite + len1;
                    System.out.print("|");
                    byte len2 = tableTableFile.readByte();
                    byte[] tableNameBytes2 = new byte[len2];
                    tableTableFile.read(tableNameBytes2);
                    System.out.print(new String(tableNameBytes2));
                    bite = bite + len2;
                    System.out.print("|");
                    System.out.print(tableTableFile.readLong());
                    bite = bite+8;
                    System.out.println("\n");
                }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void createtable(String tableName, String userCommand){
        try {
            RandomAccessFile schema = TableFiles.get(MakeInformationSchema.tablesTableFileName);

            schema.seek(0);
            String existingSchema;
            String existingTable;
            Boolean flag = true;
            for (int bite = 0; bite < schema.length(); bite+=2) {
                byte schemaLength = schema.readByte();
                byte[] schemaNameBytes = new byte[schemaLength];
                schema.read(schemaNameBytes);
                bite=bite+schemaLength;
                existingSchema = new String(schemaNameBytes);
                byte tableLength = schema.readByte();
                byte[] schemaNameBytes1 = new byte[tableLength];
                schema.read(schemaNameBytes1);
                bite = bite + tableLength;
                existingTable = new String(schemaNameBytes1);
                schema.readLong();
                bite+=8;
                if (MakeInformationSchema.schemaMain.equals(existingSchema) && tableName.equals(existingTable)) {
                        System.out.println("Table already exist!");
                        flag = true;
                }
                else {
                    flag = false;
                }

            }
            if (flag == false) {
                schema.seek(schema.length());
                schema.writeByte(MakeInformationSchema.schemaMain.length());
                schema.writeBytes(MakeInformationSchema.schemaMain);
                schema.writeByte(tableName.length());
                schema.writeBytes(tableName);
                schema.writeLong(0);
                MakeInformationSchema.tableTable_records++;
                System.out.println("successfully created table '" + tableName + "'");
                createTableColumns(tableName, userCommand);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void createschema(String schemaName){
        try {
            RandomAccessFile schema = TableFiles.get(MakeInformationSchema.schemataTableFileName);

            schema.seek(0);
            String word;
            Boolean flag = true;
            for (long bite = 0; bite < schema.length(); bite++) {
                byte len = schema.readByte();
                byte[] schemaNameBytes = new byte[len];
                schema.read(schemaNameBytes);
                word = new String(schemaNameBytes);
                if (schemaName.equals(word)) {
                    System.out.println("Schema already exist!");
                    flag= true;
                }
                else {
                    flag = false;
                }
                bite = bite + len;
            }
            if (flag == false)
            {
                //System.out.println("schema does not exist");
                schema.seek(schema.length());
                schema.writeByte(schemaName.length());
                schema.writeBytes(schemaName);
                MakeInformationSchema.schemaTable_records++;
                System.out.println("successfully created schema '" + schemaName + "'");
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        }
    public static void createTableColumns(String tableName, String userCommand){
        try {
           //parse userCommand for column information
            //pass each column information to createTableColumn(tableName, column information)
            String columnInfoSection = userCommand.substring(userCommand.indexOf("(") + 1, userCommand.lastIndexOf(")"));
            String columns[] = columnInfoSection.split(",");
            for(int i = 0; i < columns.length; i++){
                columns[i] = columns[i].trim();
            }
            String constraint = null;
            for(String column : columns){
                int ord =1;
                String columnInfoTokens[] = column.split("\\s");
                if(columnInfoTokens.length == 4 || columnInfoTokens.length == 2 ) {
                    if(columnInfoTokens.length == 4) {
                        constraint = columnInfoTokens[2] + columnInfoTokens[3];
                    }
                } else {
                        System.out.println("Syntax error in column information");
                }
                createTableColumn(tableName, columnInfoTokens[0], columnInfoTokens[1], constraint, ord);
                ord++;
            }

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void createTableColumn(String tableName, String columnName, String type, String constraint, int ord){
        //update columns table
        //create index files
        try{
        RandomAccessFile column = TableFiles.get(MakeInformationSchema.columnsTableFileName);
           column.seek(column.length());
            int ordinalPosition = 1;
                column.writeByte(MakeInformationSchema.schemaMain.length());//Schema Length
                column.writeBytes(MakeInformationSchema.schemaMain);//Schema Name
                column.writeByte(tableName.length());// Table Length
                column.writeBytes(tableName);//Table Name
                column.writeByte(columnName.length());//Column Length
                column.writeBytes(columnName);//Column Name
                column.writeInt(ord);// Position of the column within the table
                column.writeByte(type.length());// DataType length
                column.writeBytes(type);// Data Type
                if (constraint.startsWith("primary")||constraint.startsWith("Not null"))
                {
                    column.writeByte("NO".length());// IS_NULLABLE Length
                    column.writeBytes("NO");// IS_NULLABLE
                    if(constraint.toUpperCase().startsWith(MakeInformationSchema.PRIMARY_KEY)){
                        column.writeByte(MakeInformationSchema.PRIMARY_KEY.length());//COLUMN_KEY length
                        column.writeBytes(MakeInformationSchema.PRIMARY_KEY);
                    } else {
                        column.writeByte("".length());//COLUMN_KEY length
                        column.writeBytes("");
                    }
                }else {
                    column.writeByte("YES".length());// IS_NULLABLE Length
                    column.writeBytes("YES");// IS_NULLABLE
                    column.writeByte("".length());//COLUMN_KEY length
                    column.writeBytes("");
                }
                    ordinalPosition++;
                //create new table file and index files
                RandomAccessFile table = new RandomAccessFile(MakeInformationSchema.schemaMain+"."+tableName+".tbl", "rw");
                RandomAccessFile tableColumn1 = new RandomAccessFile(MakeInformationSchema.schemaMain+"."+tableName+"."+ columnName + ".ndx", "rw");

    }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean doesTableExist(String tableName) throws Exception {
        RandomAccessFile schema = TableFiles.get(MakeInformationSchema.tablesTableFileName);

        schema.seek(0);
        String existingSchema;
        String existingTable;
        for (int bite = 0; bite < schema.length(); bite += 2) {
            byte schemaLength = schema.readByte();
            byte[] schemaNameBytes = new byte[schemaLength];
            schema.read(schemaNameBytes);
            bite = bite + schemaLength;
            existingSchema = new String(schemaNameBytes);
            byte tableLength = schema.readByte();
            byte[] schemaNameBytes1 = new byte[tableLength];
            schema.read(schemaNameBytes1);
            bite = bite + tableLength;
            existingTable = new String(schemaNameBytes1);
            schema.readLong();
            bite += 8;
            if (MakeInformationSchema.schemaMain.equalsIgnoreCase(existingSchema) && tableName.equalsIgnoreCase(existingTable)) {
                return true;
            }
        }
        return false;
    }

    public static void insertable(String tableName, String[] values){
        String newTable;
        try {
            if(doesTableExist(tableName)){

                RandomAccessFile table = getTableFile(MakeInformationSchema.schemaMain, tableName);
                //get column types and order
                List<ColumnType> columnTypes = getColumnTypes(MakeInformationSchema.schemaMain, tableName);
                //insert based on type
                table.seek(table.length());
                int numberOfValues = values.length;
                for (int i = 0; i < numberOfValues; i++)
                {
                    ColumnType ct = columnTypes.get(i);
                    if ("BYTE".equalsIgnoreCase(ct.getType())) {
                        table.writeByte(Byte.parseByte(values[i]));
                    } else if (ct.getType().toUpperCase().startsWith("SHORT")) {
                        table.writeShort(Short.parseShort(values[i]));
                    } else if (ct.getType().toUpperCase().startsWith("LONG")) {
                        table.writeLong(Long.parseLong(values[i]));
                    } else if ("INT".equalsIgnoreCase(ct.getType())) {
                        table.writeInt(Integer.parseInt(values[i]));
                    } else if (ct.getType().toUpperCase().startsWith("CHAR")) {
                        char characters[] = new char[ct.getLength()];
                        values[i].getChars(0, values[i].length(), characters, 0);
                        for(int j = values[i].length(); j < characters.length; j++){
                            characters[j] = 0;
                        }
                        table.write(new String(characters).getBytes());
                    } else if (ct.getType().toUpperCase().startsWith("VARCHAR")) {
                        table.write(values[i].length());
                        table.writeBytes(values[i]);
                    } else if ("FLOAT".equalsIgnoreCase(ct.getType())) {
                        table.writeFloat(Float.parseFloat(values[i]));
                    } else if ("DOUBLE".equalsIgnoreCase(ct.getType())) {
                        table.writeDouble(Double.parseDouble(values[i]));
                    } else if ("DATETIME".equalsIgnoreCase(ct.getType())) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss");
                        table.writeLong(sdf.parse(values[i]).getTime());
                    } else if ("DATE".equalsIgnoreCase(ct.getType())){
                        //DATE
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        table.writeLong(sdf.parse(values[i].replace("'", "")).getTime());
                    }
                    else {
                    //unsupported
                }
                }

                RandomAccessFile tableFile = getTableFile("information_schema", "tables");
                tableFile.seek(0);
                String existingSchema;
                String existingTable;
                for (int bite = 0; bite < tableFile.length(); bite += 2) {
                    byte schemaLength = tableFile.readByte();
                    byte[] schemaNameBytes = new byte[schemaLength];
                    tableFile.read(schemaNameBytes);
                    bite = bite + schemaLength;
                    existingSchema = new String(schemaNameBytes);
                    byte tableLength = tableFile.readByte();
                    byte[] schemaNameBytes1 = new byte[tableLength];
                    tableFile.read(schemaNameBytes1);
                    bite = bite + tableLength;
                    existingTable = new String(schemaNameBytes1);
                    long fp = tableFile.getFilePointer();
                    long rows = tableFile.readLong();
                    bite += 8;
                    if (MakeInformationSchema.schemaMain.equalsIgnoreCase(existingSchema) && tableName.equalsIgnoreCase(existingTable)) {
                        tableFile.seek(fp);
                        tableFile.writeLong(++rows);
                    }
                }

            } else {
                System.out.println("Table does not exist/Invalid syntax");
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void droptable(String tableName) {
        try {
            RandomAccessFile schema = getTableFile("information_schema", "tables");
            schema.seek(0);
            String existingSchema;
            String existingTable;
            boolean deleted = false;
            for (int bite = 0; bite < schema.length(); bite += 2) {
                long fp = schema.getFilePointer();
                byte schemaLength = schema.readByte();
                byte[] schemaNameBytes = new byte[schemaLength];
                schema.read(schemaNameBytes);
                bite = bite + schemaLength;
                existingSchema = new String(schemaNameBytes);
                byte tableLength = schema.readByte();
                byte[] schemaNameBytes1 = new byte[tableLength];
                schema.read(schemaNameBytes1);
                bite = bite + tableLength;
                existingTable = new String(schemaNameBytes1);
                schema.readLong();
                bite += 8;
                if (MakeInformationSchema.schemaMain.equalsIgnoreCase(existingSchema) && tableName.equalsIgnoreCase(existingTable)) {
                    byte[] bytes = new byte[(int)(schema.length() - schema.getFilePointer())];
                    schema.read(bytes);
                    schema.seek(fp);
                    schema.write(bytes);
                    schema.getChannel().truncate(schema.getFilePointer());

                    RandomAccessFile columnTableFile = TableFiles.get(MakeInformationSchema.columnsTableFileName);
                    columnTableFile.seek(0);
                    while(columnTableFile.getFilePointer() < columnTableFile.length()) {
                        long rowFP = columnTableFile.getFilePointer();
                        byte len1 = columnTableFile.readByte();
                        byte[] columnNameBytes = new byte[len1];
                        columnTableFile.read(columnNameBytes);
                        String schemaName = new String(columnNameBytes);
                        byte len2 = columnTableFile.readByte();
                        byte[] columnNameBytes2 = new byte[len2];
                        columnTableFile.read(columnNameBytes2);
                        String existingTableName = new String(columnNameBytes2);
                        byte len3 = columnTableFile.readByte();
                        byte[] columnNameBytes3 = new byte[len3];
                        columnTableFile.read(columnNameBytes3);
                        String columnName = new String(columnNameBytes3);
                        int ordinalPosition = columnTableFile.readInt();
                        byte len4 = columnTableFile.readByte();
                        byte[] columnNameBytes4 = new byte[len4];
                        columnTableFile.read(columnNameBytes4);
                        String columnType = new String(columnNameBytes4);
                        byte len5 = columnTableFile.readByte();
                        byte[] columnNameBytes5 = new byte[len5];
                        columnTableFile.read(columnNameBytes5);
                        String isNullable = new String(columnNameBytes5);
                        byte len6 = columnTableFile.readByte();
                        byte[] columnNameBytes6 = new byte[len6];
                        columnTableFile.read(columnNameBytes6);
                        String columnKey = new String(columnNameBytes6);
                        if(schemaName.equalsIgnoreCase(MakeInformationSchema.schemaMain) && existingTableName.equalsIgnoreCase(tableName)){
                            byte[] columnBytes = new byte[(int)(columnTableFile.length() - columnTableFile.getFilePointer())];
                            columnTableFile.read(columnBytes);
                            columnTableFile.seek(rowFP);
                            columnTableFile.write(columnBytes);
                            columnTableFile.seek(rowFP);
                            columnTableFile.getChannel().truncate(columnTableFile.getFilePointer());

                            System.out.println("successfully dropped table '" + tableName + "'");

                            //finally delete table file
                            TableFiles.remove(MakeInformationSchema.schemaMain + "." + tableName + ".tbl");
                            File f = new File(MakeInformationSchema.schemaMain + "." + tableName + ".tbl");
                            f.delete();
                            deleted = true;
                        }
                    }


                    break;
                }
            }
            if(!deleted){
                System.out.println("Table does not exist.");
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void selectable(String tableName, String columnName, String operator, String value){

        try {
            if(doesTableExist(tableName)) {
                    List<ColumnType> columnTypes = getColumnTypes(MakeInformationSchema.schemaMain, tableName);
                    columnTypes.sort(new OrdinalComparator());
                    for (int i = 0; i < columnTypes.size(); i++) {
                        System.out.print(columnTypes.get(i).getName());
                        if (i < columnTypes.size() - 1) {
                            System.out.print(" | ");
                        }
                    }
                    System.out.println();

                    RandomAccessFile table = getTableFile(MakeInformationSchema.schemaMain, tableName);
                    table.seek(0);
                    ArrayList<ArrayList<Object>> results = new ArrayList<>();
                    while (table.getFilePointer() < table.length()) {
                        ArrayList<Object> row = new ArrayList<>();
                        for (ColumnType ct : columnTypes) {
                            if ("BYTE".equalsIgnoreCase(ct.getType())) {
                                byte b = table.readByte();
                                row.add(b);
                            } else if (ct.getType().toUpperCase().startsWith("SHORT")) {
                                short s = table.readShort();
                                row.add(s);
                            } else if (ct.getType().toUpperCase().startsWith("LONG")) {
                                long l = table.readLong();
                                row.add(l);
                            } else if ("INT".equalsIgnoreCase(ct.getType())) {
                                int i = table.readInt();
                                row.add(i);
                            } else if (ct.getType().toUpperCase().startsWith("CHAR")) {
                                int bytes = ct.getLength();
                                byte characters[] = new byte[bytes];
                                table.read(characters);
                                row.add(new String(characters));
                            } else if (ct.getType().toUpperCase().startsWith("VARCHAR")) {
                                byte varcharLength = table.readByte();
                                byte[] varchar = new byte[varcharLength];
                                table.read(varchar);
                                row.add(new String(varchar));
                            } else if ("FLOAT".equalsIgnoreCase(ct.getType())) {
                                float f = table.readFloat();
                                row.add(f);
                            } else if ("DOUBLE".equalsIgnoreCase(ct.getType())) {
                                double d = table.readDouble();
                                row.add(d);
                            } else if ("DATETIME".equalsIgnoreCase(ct.getType())) {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss");
                                row.add(sdf.format(new Date(table.readLong()))); //assuming long is positive
                            } else if ("DATE".equalsIgnoreCase(ct.getType())){
                                //DATE
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                row.add(sdf.format(new Date(table.readLong()))); //assuming long is positive
                            } else {
                                //unsupported
                            }
                        }
                        results.add(row);
                    }


                    for (List row : results) {
                        if (columnName == null) {
                            for (int i = 0; i < row.size(); i++) {

                                System.out.print(row.get(i));
                                if (i < row.size() - 1) {
                                    System.out.print(" | ");
                                }
                            }
                            System.out.println();
                        } else {
                            int columnIndex = 0;
                            for (int j = 0; j < columnTypes.size(); j++) {
                                if (columnTypes.get(j).getName().equals(columnName)) {
                                    columnIndex = j;
                                    break;
                                }
                            }
                            if (operator.equals("=")) {
                                if (columnTypes.get(columnIndex).getType().toUpperCase().startsWith("DATETIME")) {
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss");
                                    Date d1 = sdf.parse(row.get(columnIndex).toString());
                                    Date d2 = sdf.parse(value.replace("'", ""));
                                    if (d1.compareTo(d2) == 0) {

                                        for (int i = 0; i < row.size(); i++) {

                                            System.out.print(row.get(i));
                                            if (i < row.size() - 1) {
                                                System.out.print(" | ");
                                            }
                                        }
                                        System.out.println();
                                    }
                                }
                                else if (columnTypes.get(columnIndex).getType().toUpperCase().startsWith("DATE")) {
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                    Date d1 = sdf.parse(row.get(columnIndex).toString());
                                    Date d2 = sdf.parse(value.replace("'", ""));
                                    if (d1.compareTo(d2) == 0) {

                                        for (int i = 0; i < row.size(); i++) {

                                            System.out.print(row.get(i));
                                            if (i < row.size() - 1) {
                                                System.out.print(" | ");
                                            }
                                        }
                                        System.out.println();
                                    }
                                }
                                else {
                                        if (row.get(columnIndex).toString().equals(value)) {
                                            for (int i = 0; i < row.size(); i++) {

                                                System.out.print(row.get(i));
                                                if (i < row.size() - 1) {
                                                    System.out.print(" | ");
                                                }
                                            }
                                            System.out.println();
                                        }
                                    }
                                }
                            else if (operator.equals("<")){
                                if(columnTypes.get(columnIndex).getType().toUpperCase().startsWith("VARCHAR")
                                        || columnTypes.get(columnIndex).getType().toUpperCase().startsWith("CHAR")){

                                    System.out.println("Invalid column type for operator: <");
                                }
                                else if (columnTypes.get(columnIndex).getType().toUpperCase().startsWith("DATETIME")){
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss");
                                    Date d1 = sdf.parse(row.get(columnIndex).toString());
                                    Date d2 = sdf.parse(value.replace("'", ""));
                                    if (d1.compareTo(d2)<0) {

                                        for (int i = 0; i < row.size(); i++) {

                                            System.out.print(row.get(i));
                                            if (i < row.size() - 1) {
                                                System.out.print(" | ");
                                            }
                                        }
                                        System.out.println();
                                    }
                                }
                                else if (columnTypes.get(columnIndex).getType().toUpperCase().startsWith("DATE")){
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                    Date d1 = sdf.parse(row.get(columnIndex).toString());
                                    Date d2 = sdf.parse(value.replace("'", ""));
                                    if (d1.compareTo(d2)<0) {

                                        for (int i = 0; i < row.size(); i++) {

                                            System.out.print(row.get(i));
                                            if (i < row.size() - 1) {
                                                System.out.print(" | ");
                                            }
                                        }
                                        System.out.println();
                                    }
                                }
                                else {
                                    if (Long.parseLong(row.get(columnIndex).toString()) < Long.parseLong(value)) {

                                        for (int i = 0; i < row.size(); i++) {

                                            System.out.print(row.get(i));
                                            if (i < row.size() - 1) {
                                                System.out.print(" | ");
                                            }
                                        }
                                        System.out.println();
                                    }
                                }
                            }else if (operator.equals(">")){
                                if(columnTypes.get(columnIndex).getType().toUpperCase().startsWith("VARCHAR")
                                        || columnTypes.get(columnIndex).getType().toUpperCase().startsWith("CHAR") ){

                                    System.out.println("Invalid column type for operator: >");
                                }
                                else if (columnTypes.get(columnIndex).getType().toUpperCase().startsWith("DATETIME")){
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss");
                                    Date d1 = sdf.parse(row.get(columnIndex).toString());
                                    Date d2 = sdf.parse(value.replace("'", ""));
                                    if (d1.compareTo(d2)>0) {

                                        for (int i = 0; i < row.size(); i++) {

                                            System.out.print(row.get(i));
                                            if (i < row.size() - 1) {
                                                System.out.print(" | ");
                                            }
                                        }
                                        System.out.println();
                                    }
                                }
                                else if (columnTypes.get(columnIndex).getType().toUpperCase().startsWith("DATE")){
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                    Date d1 = sdf.parse(row.get(columnIndex).toString());
                                    Date d2 = sdf.parse(value.replace("'", ""));
                                    if (d1.compareTo(d2)>0) {

                                        for (int i = 0; i < row.size(); i++) {

                                            System.out.print(row.get(i));
                                            if (i < row.size() - 1) {
                                                System.out.print(" | ");
                                            }
                                        }
                                        System.out.println();
                                    }
                                }
                                else {
                                    if (Long.parseLong(row.get(columnIndex).toString()) > Long.parseLong(value)) {

                                        for (int i = 0; i < row.size(); i++) {

                                            System.out.print(row.get(i));
                                            if (i < row.size() - 1) {
                                                System.out.print(" | ");
                                            }
                                        }
                                        System.out.println();
                                    }
                                }
                            }
                            else
                            {

                            }
                    }
                }

            }else{
                System.out.println("Table does not exist!");
            }
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }

    private static List<ColumnType> getColumnTypes(String schema, String table) throws Exception {
        RandomAccessFile columnsTable = TableFiles.get(MakeInformationSchema.columnsTableFileName);

        List<ColumnType> columnTypes = new ArrayList<ColumnType>();
        columnsTable.seek(0);
        columnsTable.seek(0);
        String existingSchema;
        String existingTable;
        String existingColumn;
        String type;
        int ordinal;
        String nullable;
        String key;
        for (int bite = 0; bite < columnsTable.length(); bite += 6) {
            byte schemaLength = columnsTable.readByte();
            byte[] schemaNameBytes = new byte[schemaLength];
            columnsTable.read(schemaNameBytes);
            bite = bite + schemaLength;
            existingSchema = new String(schemaNameBytes);
            byte tableLength = columnsTable.readByte();
            byte[] schemaNameBytes1 = new byte[tableLength];
            columnsTable.read(schemaNameBytes1);
            bite = bite + tableLength;
            existingTable = new String(schemaNameBytes1);
            byte columnLength = columnsTable.readByte();
            byte[] schemaNameBytes2 = new byte[columnLength];
            columnsTable.read(schemaNameBytes2);
            bite = bite + columnLength;
            existingColumn = new String(schemaNameBytes2);
            ordinal = columnsTable.readInt();
            bite += 4;
            byte typeLength = columnsTable.readByte();
            byte[] schemaNameBytes3 = new byte[typeLength];
            columnsTable.read(schemaNameBytes3);
            bite = bite + typeLength;
            type = new String(schemaNameBytes3);
            byte nullLength = columnsTable.readByte();
            byte[] schemaNameBytes4 = new byte[nullLength];
            columnsTable.read(schemaNameBytes4);
            bite = bite + nullLength;
            nullable = new String(schemaNameBytes4);
            byte keyLength = columnsTable.readByte();
            byte[] schemaNameBytes5 = new byte[keyLength];
            columnsTable.read(schemaNameBytes5);
            bite = bite + keyLength;
            key = new String(schemaNameBytes5);
            if(existingSchema.equalsIgnoreCase(schema) && existingTable.equalsIgnoreCase(table)){
                ColumnType ct = new ColumnType();
                ct.setName(existingColumn);
                ct.setType(type);
                if(type.contains("(")){
                    String length = type.substring(type.indexOf("(") + 1, type.indexOf(")"));
                    ct.setLength(Integer.parseInt(length));
                }
                if(MakeInformationSchema.PRIMARY_KEY.equals(key)){
                    ct.setPrimaryKey(true);
                } else {
                    ct.setPrimaryKey(false);
                }
                if("NO".equals(nullable)){
                    ct.setNullable(false);
                } else {
                    ct.setNullable(true);
                }
                ct.setOrdinal(ordinal);
                columnTypes.add(ct);
            }
        }

        return columnTypes;

    }
}
