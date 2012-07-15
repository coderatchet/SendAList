package com.thenaglecode.sendalist.server.servlets;

import com.thenaglecode.sendalist.server.Globals;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 8/07/12
 * Time: 10:51 PM
 */
public class OnStartup extends HttpServlet{

     public void init() throws ServletException {
         Globals.init();
         System.out.println("************************************");
         System.out.println("------------INIT COMPLETE-----------");
         System.out.println("************************************");
     }
}
