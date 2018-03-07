package edu.iot.contact;

import edu.iot.lib.app.Application;

public class App 
{
    public static void main( String[] args )
    {
    	Application app = new ContactApp();
        app.init();
        app.run();
    }
}
