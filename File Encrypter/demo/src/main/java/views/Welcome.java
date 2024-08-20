package views;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Scanner;

import dao.UserDAO;
import model.User;
import service.GenerateOTP;
import service.SendOTPService;
import service.UserService;

public class Welcome {
    public void welcomeScreen(){
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Welcome to the App");
        System.out.println("Press 1 to Login");
        System.out.println("Press 2 to Signup");
        System.out.println("Press 0 to Exit");

        int choice=0;
        try {
            choice=Integer.parseInt(br.readLine());
        } catch(IOException ex){
            ex.printStackTrace();
        }

        switch (choice) {
            case 1-> login();
            case 2-> signUp();
            case 0-> System.exit(0);
        }
    }
    private void login(){
        Scanner sc=new Scanner(System.in);
        System.out.print("Enter your email : ");
        String email=sc.nextLine();

        try{
            if(UserDAO.isExists(email)){
                String genOTP=GenerateOTP.getOTP();
                SendOTPService.sendOTP(email, genOTP);
                System.err.print("Enter the OTP : ");
                String otp=sc.nextLine();
                System.err.println();
                if(otp.equals(genOTP)){
                    System.err.println("Login Successfully");
                    new UserView(email).home();
                }else{
                    System.out.println("Invalid OTP");
                }
            }else{
                System.out.println("User does not exist");
            }
        } catch(SQLException ex){
            ex.printStackTrace();
        }

    }
    private void signUp(){
        Scanner sc=new Scanner(System.in);
        System.err.print("Enter your name : ");
        String name=sc.nextLine();
        System.err.print("Enter your email : ");
        String email=sc.nextLine();

        String genOTP=GenerateOTP.getOTP();
        SendOTPService.sendOTP(email, genOTP);
        // System.out.println("OTP has been sent to your email");
        System.err.print("Enter the OTP : ");
        String otp=sc.nextLine();

        if(otp.equals(genOTP)){
            // System.out.println("Signup Successfully");
            User user=new User(name,email);
            int response=UserService.saveUser(user);
            switch(response){
                case 0 -> System.out.println("User already exists");
                case 1 -> System.out.println("Signup Successfully");
            }
        }else{
            System.out.println("Invalid OTP");
        }

    }

}
