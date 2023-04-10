package project1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(serviceName = "BPMService")
public class BPMService
{
  @WebMethod(exclude = true)
  public static Connection getConnection()
  {
    Connection connection = null;
    try
    {
      String driver = "oracle.jdbc.driver.OracleDriver";
      String url = "jdbc:oracle:thin:@localhost:1521:xe";
      String username = "bpm";
      String password = "bpm";
      Class.forName(driver); // load Oracle driver
      connection = DriverManager.getConnection(url, username, password);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return connection;
  }

  @Oneway
  @WebMethod
  public void insertNewEmployee(@WebParam(name = "empName")
    String empName, @WebParam(name = "empPosition")
    String empPosition, @WebParam(name = "salary")
    Double salary, @WebParam(name = "deptName")
    String deptName)
  {
    Connection conn = null;
    Statement stat = null;
    String sql = null;
    try
    {
      conn = getConnection();
      stat = conn.createStatement();
      sql = "Insert into EMPLOYEES (EMPLOYEE_NAME,EMPLOYEE_SALARY,EMPLOYEE_POSITION,EMPLOYEE_DEPT_NAME) values ('" + empName + "'," + salary + ",'" + empPosition + "','" + deptName+"')";
      stat.executeUpdate(sql);
      conn.commit();
    }
    catch (Exception sqle)
    {
      sqle.printStackTrace();
    }
    finally
    {
      try
      {        
        if (stat != null)
          stat.close();
        
        if (conn != null)
          conn.close();
      }
      catch (Exception e)
      {
        e.getMessage();
      }
    }
  }

  @WebMethod
  public String checkItemAvailability(@WebParam(name = "itemCode")
    String itemCode, @WebParam(name = "qty")
    int qty)
  {
    Connection conn = null;
    Statement stat = null;
    ResultSet rs = null;
    String sql = null;
    try
    {
      conn = getConnection();
      stat = conn.createStatement();
      sql = "select quantity from stock where item_code='" + itemCode + "'";
      rs = stat.executeQuery(sql);
      while(rs.next())
      {
        int quantity = rs.getInt(1);
        if(quantity >= qty)
        {
          return "true";
        }
      }
    }
    catch (Exception sqle)
    {
      sqle.printStackTrace();
    }
    finally
    {
      try
      {       
        if (rs != null)
          rs.close();
        
        if (stat != null)
          stat.close();
        
        if (conn != null)
          conn.close();
      }
      catch (Exception e)
      {
        e.getMessage();
      }
    }
    return "false";
  }

  @WebMethod
  @Oneway
  public void updateStock(@WebParam(name = "itemCode")
    String itemCode, @WebParam(name = "qty")
    int qty, @WebParam(name = "operation")
    String operation)
  {
    Connection conn = null;
    Statement stat = null;
    String sql = null;
    try
    {
      conn = getConnection();
      stat = conn.createStatement();
      if (operation.equals("A"))
      {
        sql = "update stock set quantity = quantity + " + qty + " where ITEM_CODE = '" + itemCode + "'";
      }
      else
      {
        sql = "update stock set quantity = quantity - " + qty + " where ITEM_CODE = '" + itemCode + "'";
      }
      
      stat.executeUpdate(sql);
      conn.commit();
    }
    catch (Exception sqle)
    {
      sqle.printStackTrace();
    }
    finally
    {
      try
      {        
        if (stat != null)
          stat.close();
        
        if (conn != null)
          conn.close();
      }
      catch (Exception e)
      {
        e.getMessage();
      }
    }
  }
}
