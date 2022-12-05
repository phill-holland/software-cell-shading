package com.phillholland.app;

import java.awt.*;

public class App 
{
    public static void main(String[] args)
    {        
        Frame f = new Frame();
        f.addWindowListener(new java.awt.event.WindowAdapter() 
        {
             public void windowClosing(java.awt.event.WindowEvent e) 
             {
                System.exit(0);
             };
        });
      
        Main main = new Main();
            
        main.setSize(512, 512);
        f.add(main);
        f.pack();
        main.init();
        f.setSize(512,512 + 60);
        f.setVisible(true);        
    }
}
