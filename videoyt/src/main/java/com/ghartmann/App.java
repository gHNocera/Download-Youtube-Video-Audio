package com.ghartmann;

import javax.swing.SwingUtilities; // Importa a classe SwingUtilities para lidar com a thread de eventos do Swing.

/**
 * Classe principal da aplicação.
 * Responsável por iniciar a interface gráfica do usuário.
 */
public class App 
{
    static telaDownload tela; // Declara uma instância estática da classe telaDownload, que representa a janela principal da aplicação.
    
    /**
     * Método principal que inicia a execução da aplicação.
     * @param args Argumentos de linha de comando (não utilizados nesta aplicação).
     * @throws Exception Lança exceções que podem ocorrer durante a inicialização da UI.
     */
    public static void main( String[] args ) throws Exception{
        setUp(); // Chama o método setUp para configurar e exibir a interface do usuário.
    }

    /**
     * Configura e exibe a janela principal da aplicação.
     * Garante que a criação e exibição da UI ocorram na Event Dispatch Thread (EDT) do Swing.
     * @throws Exception Lança exceções se houver problemas ao invocar a UI na EDT.
     */
    public static void setUp() throws Exception {
        // Garante que a criação da tela seja executada na Event Dispatch Thread (EDT) do Swing.
        SwingUtilities.invokeAndWait(() -> tela = new telaDownload()); 
        tela.setVisible(true); // Torna a janela principal visível para o usuário.
    }
}
