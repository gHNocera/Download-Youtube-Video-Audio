package com.ghartmann;

import javax.swing.SwingUtilities;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {
    private telaDownload tela;
    private Video video;

    @Before
    public void setUp() throws Exception {
        SwingUtilities.invokeAndWait(() -> tela = new telaDownload());
        video = new Video();
    }

    @Test
    public void shouldCreateTela() {
        while (true) { 
            tela.setVisible(true);
        }
    }
}

