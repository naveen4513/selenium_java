package office.sirion.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class sqlConnection {

    static int suiteId = 2;

    public static void main(String[] args) {
        try {
            Connection conn = createConnection();
            fetchCount(conn, null);
            System.out.println("Done");

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    static Data fetchCount(Connection conn, Integer parentId) throws Exception {
        Statement st = conn.createStatement();
        String sql = null;
        if (parentId == null) {
            sql = String.format("select id from sections where parent_id is null and suite_id = %s order by id",
                    suiteId);
        }else {
            sql = String.format("select id from sections where parent_id = %s and suite_id = %s order by id",
                    parentId, suiteId);
        }
        ResultSet rs = st.executeQuery(sql);

        Data data = new Data();
        while (rs.next()) {
            int childId = rs.getInt(1);
            Data childData = fetchCount(conn, childId);
            data.sc += childData.sc;
            data.cc += childData.cc;
            System.out.println(childId + " : " + parentId + " : " + childData.sc + " : " + childData.cc);
            if(parentId==null) {
                System.out.println("::::::::::::::::::::::::::::::::::::::::::::::");
            }
        }
        if(data.sc==0) {
            data.sc=1;
        }
        data.cc = data.cc + caseCount(conn, parentId);

        return data;
    }

    static int caseCount(Connection conn, Integer sectionId) throws Exception{
        String sql = String.format("select count(*) from cases where  section_id = %s and suite_id = 2 and priority_id in (5,6,7,8) and custom_tc_autostatus in (1,3)",
                sectionId, suiteId);
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while(rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    static class Data{
        int sc;
        int cc;
    }

    static Connection createConnection() throws Exception {
        String myDriver = "com.mysql.cj.jdbc.Driver";
        String myUrl = "jdbc:mysql://192.168.2.112/testrail";
        Class.forName(myDriver);
        Connection conn = DriverManager.getConnection(myUrl, "root", "admin@123");
        return conn;
    }

}
