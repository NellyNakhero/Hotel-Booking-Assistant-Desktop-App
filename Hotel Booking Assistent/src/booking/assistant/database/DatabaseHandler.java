/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package booking.assistant.database;

import booking.assistant.ui.ListRooms.ListRoomsController.Room;
import booking.assistant.ui.ListsGuests.ListsGuestsController.Guest;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Nelly
 */
public final class DatabaseHandler {
    
    private static DatabaseHandler handler = null;
    private static final String DB_URL = "jdbc:derby:database;create=true";
    private static Connection conn = null;
    private static Statement stmt = null;
    
      
//.............................................................................................................    
      private DatabaseHandler(){
        createConnection();
        setUpReservationTable();
        setUpGuestTable();
        setUpIssueTable();
}
//.............................................................................................................
      public static DatabaseHandler getInstance(){
          if(handler == null){
              handler= new DatabaseHandler();
          }
          return handler;
      }
//..............................................................................................................
      public static void createConnection(){
        try {
           Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
            conn = DriverManager.getConnection(DB_URL);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | SQLException e) {
             JOptionPane.showMessageDialog(null, "Cant load database", "Database Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        } 
        
       }
    //..............................................................................................................
       public ResultSet execQuery(String query) {
        ResultSet result;
        try {
            stmt = conn.createStatement();
            result = stmt.executeQuery(query);
        }
        catch (SQLException ex) {
            System.out.println("Exception at execQuery:dataHandler" + ex.getLocalizedMessage());
            return null;
        }
        finally {
        }
        return result;
    }
//..................................................................................................................
         public boolean execAction(String qu) {
        try {
            stmt = conn.createStatement();
            stmt.execute(qu);
            return true;
        }
        catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error:" + ex.getMessage(), "Error Occured", JOptionPane.ERROR_MESSAGE);
            System.out.println("Exception at execQuery:dataHandler" + ex.getLocalizedMessage());
            return false;
        }
        finally {
        }
    }
//..............................................................................................................         
    void setUpReservationTable(){ 
       String  TABLE_NAME = "ROOMS";
        try {
            
            stmt= (Statement) conn.createStatement();
            
           DatabaseMetaData dbm = conn.getMetaData();
           
           ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(),null);
           
           if(tables.next()){
               System.out.println("Table "+ TABLE_NAME+" for Rooms already exists. Ready to go!");
           }
           else{
               stmt.execute("CREATE TABLE "+ TABLE_NAME + "("
               +" ID varchar(200) primary key, \n"
               +" Floor varchar(200), \n"
               + "Room_Service_One varchar(200), \n"
               +"Room_Service_Two varchar(100), \n"
               +"intcode varchar(100), \n"
               +"isAvail boolean default true"
               +")");
           }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage()+"............set up database");
        } finally {       
        }
    }
//...............................................................................................................
    public boolean deleteRoom(Room room){
        try {
            String room_delete_stmt = "DELETE FROM ROOMS WHERE ID = ?";
            PreparedStatement stmt= conn.prepareStatement(room_delete_stmt);
            stmt.setString(1, room.getID());
            int res = stmt.executeUpdate();
            System.out.println(res);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    private void setUpGuestTable() {
          String  TABLE_NAME = "GUESTS";
        try {
            
            stmt= (Statement) conn.createStatement();
            
           DatabaseMetaData dbm = conn.getMetaData();
           
           ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(),null);
           
           if(tables.next()){
               System.out.println("Table "+ TABLE_NAME+" for Guests already exists. Ready to go!");
           }
           else{
               stmt.execute("CREATE TABLE "+ TABLE_NAME + "("
               +"Name varchar(200), \n"
               + "NationalID varchar(200) primary key, \n"
               + "Telephone_Number varchar(20), \n"
               +"Email varchar(100), \n"
               +"From_Date varchar(20), \n"
               +"To_Date varchar(20)"
              // +"isAvail boolean default true"
               +")");
           }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage()+"............set up database");
        } finally {
            
        }
    }
//..............................................................................................................
    public boolean deleteGuestFromTable(Guest guest){
        try {
            String guest_delete_stmt = "DELETE FROM GUESTS WHERE NationalID = ?";
            PreparedStatement stmt= conn.prepareStatement(guest_delete_stmt);
            stmt.setString(1, guest.getNational_ID());
            int res = stmt.executeUpdate();
            System.out.println(res);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
//..............................................................................................................
    void setUpIssueTable(){
        String  TABLE_NAME = "ISSUE";
         try {
            
            stmt= (Statement) conn.createStatement();
            
            DatabaseMetaData dbm = conn.getMetaData();
           
           ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(),null);
           
           if(tables.next()){
               System.out.println("Table "+ TABLE_NAME+" for Issue already exists. Ready to go!");
           }
           else{
               stmt.execute("CREATE TABLE "+ TABLE_NAME + "("
               +"Room_Number varchar(200) primary key, \n"
               + "GuestID varchar(200), \n"
               + "IssueTime timestamp default CURRENT_TIMESTAMP, \n"
               + "renew_count integer default 0, \n"
               +"FOREIGN KEY (Room_Number) REFERENCES ROOMS(ID), \n"
               +"FOREIGN KEY (GuestID) REFERENCES GUESTS(NationalID)"
               +")");
           }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage()+"............set up database");
        } finally {
            
        }
    }
//..........................................................................................................
     public boolean isRoomAlreadyIssued(Room room){
        try {
            String check_room_stmt = "SELECT COUNT(*) FROM ISSUE WHERE Room_Number = ?";
            PreparedStatement stmt= conn.prepareStatement(check_room_stmt);
            stmt.setString(1, room.getID());
            ResultSet rs =stmt.executeQuery();
            if(rs.next()){
                int count = rs.getInt(1);
                System.out.println(count);
                if(count > 0){
                    
                    return true;
                }
                else{
                
                    return false;
                }
            }
            
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
//.................................................................................................................
       public boolean isGuestAlreadyIssued(Guest guest){
        try {
            String check_guest_stmt = "SELECT COUNT(*) FROM ISSUE WHERE GuestID = ?";
            PreparedStatement stmt= conn.prepareStatement(check_guest_stmt);
            stmt.setString(1, guest.getNational_ID());
            ResultSet rs =stmt.executeQuery();
            if(rs.next()){
                int count = rs.getInt(1);
                System.out.println(count);
                if(count > 0){
                    return true;
                }
                else{
                    return false;
                }
            }
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
       
//..............................................................................................................
     public boolean updatingRoom(Room room){
        try {
            String update = "UPDATE ROOMS SET Floor=?,Room_Service_One=?, Room_Service_Two=?, intcode=? WHERE ID=?";
            PreparedStatement stmt= conn.prepareStatement(update);
            
            stmt.setString(1, room.getFloor());
            stmt.setString(2, room.getRoom_Service1());
            stmt.setString(3, room.getRoom_Service2());
            stmt.setString(4, room.getCode());
            stmt.setString(5, room.getID());
            int res = stmt.executeUpdate();
            
            return (res>0);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
       return false;  
     }
//..............................................................................................................
     public boolean updatingGuest(Guest guest){
        try {
            String update = "UPDATE GUESTS SET Name=?,Telephone_Number=?, Email=?, From_Date=?, To_Date=? WHERE NationalID=?";
            PreparedStatement stmt= conn.prepareStatement(update);
            
            stmt.setString(1, guest.getGuest_Name());
            stmt.setString(2, guest.getTelephone());
            stmt.setString(3, guest.getEmail());
            stmt.setString(4, guest.getFdate());
            stmt.setString(5, guest.getTdate());
            stmt.setString(6, guest.getNational_ID());
            int res = stmt.executeUpdate();
            
            return (res>0);
        } catch (SQLException ex) { 
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
       return false;  
     }
}

