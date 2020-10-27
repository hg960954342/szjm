package com.prolog.eis.util.generator;



import org.apache.commons.text.CaseUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 生成数据库表的实体工具类
 * @author chengxudong
 * @description
 **/
public class EntitiesGenerator {
    // 数据库连接
    private String URL;
    private String DBName;
    private String NAME;
    private String PASS;
    // 作者名字
    private String authorName = "chenxudong";
    // 列名数组
    private String[] colnames;
    // 列名类型数组
    private String[] colTypes;

    // 列名注释
    private Map<String, String> listComments = new LinkedHashMap<>();
    ;
    // 列名大小数组
    private int[] colSizes;
    // 是否需要导入包java.util.*
    private boolean f_util = false;
    // 是否需要导入包java.sql.*
    private boolean f_sql = false;

    private SqlHelper sqlHelper = null;


    public EntitiesGenerator(String url, String dbname, String username, String password) {
        this.URL = url + "/" + dbname;
        this.DBName = dbname;
        this.NAME = username;
        this.PASS = password;

        sqlHelper = new SqlHelper(this.URL, this.NAME, this.PASS);
    }

    public void Generate() {
        List<String> tableNames = sqlHelper.Get(
                "SELECT * FROM INFORMATION_SCHEMA.TABLES where TABLE_SCHEMA='" + DBName + "';", "TABLE_NAME");
        Connection con = null;

        try {
            con = sqlHelper.getConnection();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        for (String table : tableNames) {
            Generate(table, con);
            resetTableInfo();
        }
        try {
            sqlHelper.closeConnection(con);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void resetTableInfo() {
        colnames = null;
        colTypes = null;
        colSizes = null;
        f_util = false;
        f_sql = false;
    }

    private String getPackageOutPath() {
        return "com.prolog.eis.base.model." + DBName;
    }


    private void Generate(String tablename, Connection con) {
        if (con == null) {
            System.out.println("------------------Connection to database was not set up------------------");
            return;
        }
        // 查要生成实体类的表
        String sql = "SELECT * FROM " + tablename + " limit 0, 1;";
        PreparedStatement pStemt = null;
        try {


            ResultSet rsSet = null;
            String exeSQL = String.format("SELECT column_comment,column_name FROM information_schema.COLUMNS  WHERE TABLE_NAME = '%s' and table_schema='%s'", tablename, DBName);
            pStemt = con.prepareStatement(exeSQL);
            rsSet = pStemt.executeQuery(exeSQL);


            while (rsSet.next()) {
                listComments.put(rsSet.getString("column_name"), rsSet.getString("column_comment") == null ? "" : rsSet.getString("column_comment"));
            }


            pStemt = con.prepareStatement(sql);
            ResultSetMetaData rsmd = pStemt.getMetaData();

            // 统计列
            int size = rsmd.getColumnCount();
            colnames = new String[size];
            colTypes = new String[size];
            colSizes = new int[size];
            for (int i = 0; i < size; i++) {
                colnames[i] = rsmd.getColumnName(i + 1).replace(" ", "");
                colTypes[i] = rsmd.getColumnTypeName(i + 1);

                if (colTypes[i].equalsIgnoreCase("datetime") || colTypes[i].equalsIgnoreCase("date")) {
                    f_util = true;
                }
                if (colTypes[i].equalsIgnoreCase("image") || colTypes[i].equalsIgnoreCase("text")
                        || colTypes[i].equalsIgnoreCase("TIMESTAMP")) {
                    f_sql = true;
                }
                colSizes[i] = rsmd.getColumnDisplaySize(i + 1);
            }

            String content = parse(colnames, colTypes, colSizes, tablename);

            try {
                File directory = new File("");
                File dir = new File(directory.getAbsolutePath() + "/src/" + this.getPackageOutPath().replace(".", "/"));
                dir.mkdirs();

                String outputPath = dir.getAbsolutePath() + "/" + CaseUtils.toCamelCase(tablename, true, new char[]{'_'}) + ".java";
                FileWriter fw = new FileWriter(outputPath);
                PrintWriter pw = new PrintWriter(fw);
                pw.println(content);
                pw.flush();
                pw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pStemt != null) {
                    pStemt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 功能：生成实体类主体代码
     *
     * @param colnames
     * @param colTypes
     * @param colSizes
     * @return
     */
    private String parse(String[] colnames, String[] colTypes, int[] colSizes, String tablename) {
        StringBuffer sb = new StringBuffer();
        sb.append("package " + this.getPackageOutPath() + ";\r\n");
        // 判断是否导入工具包
        if (f_util) {
            sb.append("import java.util.Date;\r\n");
        }
        if (f_sql) {
            sb.append("import java.sql.*;\r\n");
        }

        sb.append(
                           "import com.baomidou.mybatisplus.annotation.IdType;\n" +
                           "import com.baomidou.mybatisplus.annotation.TableField;\n" +
                           "import com.baomidou.mybatisplus.annotation.TableId;\n" +
                           "import com.baomidou.mybatisplus.annotation.TableName;\n" +
                           "import io.swagger.annotations.ApiModelProperty;\n" +
                           "import lombok.Data;"
        );


        sb.append("\r\n");
        // 注释部分
        sb.append("/**\r\n");
        sb.append(" * " + tablename);

        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
        sb.append("  generated at " + formater.format(new Date()) + " by: " + this.authorName + "\r\n");

        sb.append(" */");
        // 实体部分
        sb.append("\r\n@TableName(\"").append(tablename).append("\"").append(")");
        sb.append("\r\n@Setter");
        sb.append("\r\n@Getter");
        sb.append("\r\n@Accessors(chain = true)");
        sb.append("\r\npublic class " + CaseUtils.toCamelCase(tablename, true, new char[]{'_'}) + "{\r\n");
        // 属性
        processAllAttrs(sb);
        sb.append("}");

        return sb.toString();
    }

    /**
     * 功能：生成所有属性
     *
     * @param sb
     */
    private void processAllAttrs(StringBuffer sb) {
        for (int i = 0; i < colnames.length; i++) {
            if (colnames[i].toLowerCase().equals("id")) {
                sb.append("\r\n@ApiModelProperty(\"主键\")");
                sb.append("\r\n@TableId(type= IdType.AUTO)");
            }
            sb.append("\r\n@TableField(").append("\"").append(colnames[i]).append("\"").append(")");
            if (listComments.containsKey(colnames[i])) {
                sb.append("\r\n@ApiModelProperty(").append("\"").append(listComments.get(colnames[i])).append("\"").append(")");
            }
            sb.append("\r\nprivate " + sqlType2JavaType(colTypes[i]) + " " + CaseUtils.toCamelCase(colnames[i], false, new char[]{'_'}) + ";\r\n");
        }
        sb.append(System.lineSeparator());
    }


    /**
     * 功能：将输入字符串的首字母改成大写
     *
     * @param str
     * @return
     */
    private String initcap(String str) {

        char[] ch = str.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            ch[0] = (char) (ch[0] - 32);
        }

        return new String(ch);
    }

    /**
     * 功能：获得列的数据类型
     *
     * @param sqlType
     * @return
     */
    private String sqlType2JavaType(String sqlType) {

        if (sqlType.equalsIgnoreCase("bit")) {
            return "boolean";
        } else if (sqlType.equalsIgnoreCase("tinyint") || sqlType.equalsIgnoreCase("tinyINT UNSIGNED")) {
            return "byte";
        } else if (sqlType.equalsIgnoreCase("smallint")) {
            return "short";
        } else if (sqlType.equalsIgnoreCase("int") || sqlType.equalsIgnoreCase("INT UNSIGNED")) {
            return "int";
        } else if (sqlType.equalsIgnoreCase("bigint")) {
            return "long";
        } else if (sqlType.equalsIgnoreCase("float")) {
            return "float";
        } else if (sqlType.equalsIgnoreCase("decimal") || sqlType.equalsIgnoreCase("numeric")
                || sqlType.equalsIgnoreCase("real") || sqlType.equalsIgnoreCase("money")
                || sqlType.equalsIgnoreCase("smallmoney") || sqlType.equalsIgnoreCase("DOUBLE")) {
            return "double";
        } else if (sqlType.equalsIgnoreCase("varchar") || sqlType.equalsIgnoreCase("char")
                || sqlType.equalsIgnoreCase("nvarchar") || sqlType.equalsIgnoreCase("nchar")
                || sqlType.equalsIgnoreCase("text")) {
            return "String";
        } else if (sqlType.equalsIgnoreCase("datetime") || sqlType.equalsIgnoreCase("date")) {
            return "Date";
        } else if (sqlType.equalsIgnoreCase("image")) {
            return "Blod";
        } else if (sqlType.equalsIgnoreCase("TIMESTAMP")) {
            return "Timestamp";
        }

        return null;
    }

    public static void main(String[] args) {
        EntitiesGenerator gen = new EntitiesGenerator("jdbc:mysql://39.96.69.213:3306", "eis_hf", "root", "eisalicloud");
        gen.Generate();
    }

}
