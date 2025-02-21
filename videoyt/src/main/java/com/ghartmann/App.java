package com.ghartmann;

import javax.swing.SwingUtilities;

/**
 * Hello world!
 *
 */
public class App 
{
    static telaDownload tela;
    
        public static void main( String[] args ) throws Exception{
            setUp();
        }

        public static void setUp() throws Exception {
            SwingUtilities.invokeAndWait(() -> tela = new telaDownload());
            tela.setVisible(true);
        }
}
