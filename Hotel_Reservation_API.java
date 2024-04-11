//new reservation
//check reservation
//Get Room No.
//Update reservation
//Delete Reservation
//Exit

import java.util.Scanner;

import javax.swing.plaf.nimbus.State;

import java.sql.*;

public class hotel{

    private static final String url="jdbc:mysql://localhost:3306/hotel_db";
    private static final String username ="root";
    private static final String password ="Kartik";
    @SuppressWarnings("resource")
    public static void main(String args[]) throws ClassNotFoundException , SQLException
    {
        try{
            Class.forName("com.mysql.jdbc.Drivers");

        }catch(ClassNotFoundException e)
        {
            System.out.println(e.getMessage());
        }
        try{
            Connection con = DriverManager.getConnection(url, username, password);
            Statement smt = con.createStatement();
            while(true)
            {
                System.out.println();
                System.out.println("WELCOME TO HOTEL MANAGEMENT SYSTEM");
                Scanner sc = new Scanner(System.in);
                System.out.println("1. Reserve a room");
                System.out.println("2. View Reservation");
                System.out.println("3. Get the Room Number");
                System.out.println("4. Update Reservtaion");
                System.out.println("5. Delete the Reservation");
                System.out.println("0. QUIT");
                System.out.println("Choose one option");
                int choice = sc.nextInt();
                switch (choice) {
                    case 1:
                        reserveRoom(con,sc, smt);
                        break;
                    case 2:
                        viewReservation(con , smt);
                        break;
                    case 3:
                        getroomNumber(con, sc, smt);
                        break;
                    case 4:
                        updateReservtaion(con,sc , smt);
                        break;
                    case 5:
                        deleteReservation(con, sc, smt);
                        break;
                    case 0:
                        exit();
                        return;
                    default:
                        System.out.println("Invalid choice . Please try again");
                        break;
                }

            }

        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }
    
    private static void reserveRoom(Connection con, Scanner sc, Statement smt) {
        try{
            System.out.print("Enter the name of the quest");
            String guestname = sc.next();
            sc.nextLine();
            System.out.print("Enter the room number:");
            int roomnumber = sc.nextInt();
            System.out.print("Enter the contact number:");
            String mobile = sc.next();

            String sql ="INSERT INTO reservation (guest_name ,room_number ,contact_number)"+" VALUES('"+ guestname +"',"+roomnumber +",'"+ mobile+"');";
            int affectedrows = smt.executeUpdate(sql);
            if(affectedrows >0)
            {
                System.out.println("Reservation Sucessfull");
            }
            else{
                System.out.println("Reservatio FAILED");
            }
            }
            catch(SQLException e)
            {
                System.out.print(e.getMessage());

            }
        
    }


    private static void viewReservation(Connection con, Statement smt) throws SQLException {
       String sql ="SELECT * from reservation;";
       try(ResultSet rst = smt.executeQuery(sql))
       {
        
        System.out.println("Current Reservations:");
        System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");
        System.out.println("| Reservation ID | Guest           | Room Number   | Contact Number      | Reservation Date        |");
        System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");
        while(rst.next())
        {
            int reservationid = rst.getInt("reservation_id");
            String guestname = rst.getString("guest_name");
            int roomnumber = rst.getInt("room_number");
            String contact = rst.getString("contact_number");
            String reservationdate = rst.getTimestamp("reservation_date").toString();

           // Format and display the reservation data in a table-like format
           System.out.printf("| %-14d | %-15s | %-13d | %-20s | %-19s   |\n",
           reservationid, guestname, roomnumber, contact, reservationdate);
        }
        System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");11111

       }
       catch(SQLException e)
       {
        System.out.println(e.getMessage());

       }
    }
    private static void getroomNumber(Connection con, Scanner sc, Statement smt) {
       try{
        System.out.println("Enter the reservation id:");
        int reservationId = sc.nextInt();
        // System.out.println("Enter the name of the guest");
        // String name = sc.next();
        String sql ="SELECT room_number FROM reservation WHERE reservation_id = "+ reservationId +";";
        ResultSet rst = smt.executeQuery(sql);
        if(rst.next())
        {
            int roomnumber = rst.getInt("room_number");
            System.out.println("Room number for reservation id" +reservationId+ " is "+roomnumber);
        }
        else{
            System.out.println("Reservation not found for the reservationid ");
        }
       }catch(Exception e)
       {
        System.out.println(e.getMessage());
       }
    }

    public static void updateReservtaion(Connection con , Scanner sc , Statement smt)
    {
        try{
            System.out.println("Enter the reservation id to update:");
            int reservationId = sc.nextInt();
            if(!(reservationexist(con, 0, smt)))
            {
                System.out.println("reservation Id doesnot exist");
                return;
            }
           
            System.out.println("Enter the new guest name");
            String newguestname = sc.next();
            System.out.println("Enter the new room number");
            int newroom = sc.nextInt();
            System.out.println("Enter the new contact number");
            String newmobile = sc.next();
            String sql ="UPDATE reservation SET guest_name = '"+newguestname +"',"+ "room_number = " +newroom +", " +"contact_number= '" +newmobile+"' "  +"WHERE reservation_id ="+reservationId;
            int affectedrows = smt.executeUpdate(sql);
            if(affectedrows >0)
            {
                System.out.println("Upadtion sucessfull");
            }
            else{
                System.out.println("Updation Failed");
            }}
            catch(Exception e)
            {
                System.out.println(e.getMessage());
            }
        
    }

    
    private static void deleteReservation(Connection con, Scanner sc, Statement smt) {
       try{
        System.out.println("Enter the reservation Id to be deleted");
        int reservationId = sc.nextInt();
        if(!(reservationexist(con, 0, smt)))
            {
                System.out.println("reservation Id doesnot exist");
                return;
            }
        String sql ="DELETE FROM reservation WHERE reservation_id =" +reservationId ;
        int affectedrows = smt.executeUpdate(sql);
        if(affectedrows >0)
            {
                System.out.println("Deletion sucessfull");
            }
            else{
                System.out.println("Deletion Failed");
            }
        }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
            }
       }
       public static boolean reservationexist(Connection con , int reservationId , Statement smt)
       {
        try{
            String sql = "Select from reservation where reservation_id =" +reservationId;
            ResultSet rst = smt.executeQuery(sql);
            return rst.next();

        }catch(Exception e)
        {
            System.out.println(e.getMessage());
            return false;
        }
       }

       private static void exit()
       {
        System.out.println("Exiting System");
        int i =5;
        try{
        while(i!=5)
        {
            System.out.print(".");
            Thread.sleep(450);
            i--;
        }
        System.out.println();
        System.out.println("Thank you for using the Hotel Reservation Sytem");
    }catch(Exception e)
    {
        System.out.println(e.getMessage());
    }
       }
    }
