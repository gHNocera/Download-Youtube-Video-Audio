package com.ghartmann;

import java.awt.Color; // Importa a classe Color para manipulação de cores.
import java.awt.Dimension; // Importa Dimension para especificar dimensões de componentes.
import java.awt.Font; // Importa Font para definir fontes de texto.
import java.awt.Graphics; // Importa Graphics para operações de desenho.
import java.awt.Graphics2D; // Importa Graphics2D para operações de desenho 2D avançadas.
import java.awt.GridBagConstraints; // Importa GridBagConstraints para layout GridBagLayout.
import java.awt.GridBagLayout; // Importa GridBagLayout para organizar componentes em uma grade.
import java.awt.Insets; // Importa Insets para especificar o espaçamento entre componentes.
import java.awt.RenderingHints; // Importa RenderingHints para controlar a qualidade de renderização.
import java.awt.event.FocusAdapter; // Importa FocusAdapter para adaptar eventos de foco.
import java.awt.event.FocusEvent; // Importa FocusEvent para eventos de foco.
import java.awt.event.MouseAdapter; // Importa MouseAdapter para adaptar eventos do mouse.
import java.awt.event.MouseEvent; // Importa MouseEvent para eventos do mouse.
import java.awt.geom.RoundRectangle2D; // Importa RoundRectangle2D para criar retângulos arredondados.
import java.awt.image.BufferedImage; // Importa BufferedImage para trabalhar com imagens.
import java.io.File; // Importa File para manipulação de arquivos e diretórios.
import java.net.URL; // Importa URL para lidar com URLs.
import javax.swing.BorderFactory; // Importa BorderFactory para criar bordas.
import javax.swing.ImageIcon; // Importa ImageIcon para exibir ícones e imagens.
import javax.swing.JButton; // Importa JButton para criar botões.
import javax.swing.JComboBox; // Importa JComboBox para criar caixas de seleção.
import javax.swing.JFileChooser; // Importa JFileChooser para permitir a seleção de arquivos/diretórios.
import javax.swing.JFrame; // Importa JFrame para criar a janela principal da aplicação.
import javax.swing.JLabel; // Importa JLabel para exibir texto ou imagens.
import javax.swing.JOptionPane; // Importa JOptionPane para exibir caixas de diálogo.
import javax.swing.JPanel; // Importa JPanel para criar painéis.
import javax.swing.JProgressBar; // Importa JProgressBar para exibir o progresso de uma tarefa.
import javax.swing.JTextField; // Importa JTextField para campos de entrada de texto.
import javax.swing.SwingConstants; // Importa SwingConstants para constantes de alinhamento.
import javax.swing.SwingWorker; // Importa SwingWorker para executar tarefas em segundo plano.
import javax.swing.UIManager; // Importa UIManager para gerenciar a aparência da UI.
import javax.swing.plaf.basic.BasicProgressBarUI; // Importa BasicProgressBarUI para personalizar a barra de progresso.
import net.coobird.thumbnailator.Thumbnails; // Importa Thumbnails para redimensionamento de imagens.

/**
 * Classe que representa a interface gráfica principal da aplicação de download de vídeos/áudios do YouTube.
 * Estende JFrame para criar uma janela.
 */
public class telaDownload extends JFrame {

    private final Video video = new Video(); // Instância da classe Video para armazenar informações do vídeo.
    private int pX, pY; // Variáveis para armazenar as coordenadas X e Y do mouse para arrastar a janela.

    /**
     * Construtor da classe telaDownload.
     * Inicializa os componentes da UI, configura o placeholder do campo de link e torna a janela arrastável.
     */
    public telaDownload() {
        initComponents(); // Inicializa e configura todos os componentes da interface.
        setupPlaceholder(); // Configura o texto de placeholder para o campo de link.
        makeDraggable(); // Habilita a funcionalidade de arrastar a janela.
    }

    private void initComponents() {
        // Configuração do Frame (janela principal)
        setUndecorated(true); // Remove a barra de título e bordas da janela.
        setSize(800, 600); // Define o tamanho da janela (largura, altura).
        setResizable(false); // Impede que o usuário redimensione a janela.
        setLocationRelativeTo(null); // Centraliza a janela na tela.
        // Define a forma da janela como um retângulo arredondado para um visual moderno.
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));

        // Painel principal que contém todos os outros componentes.
        JPanel mainPanel = new JPanel(new GridBagLayout()); // Usa GridBagLayout para posicionamento flexível.
        mainPanel.setBackground(new Color(45, 45, 45)); // Define a cor de fundo do painel principal.
        setContentPane(mainPanel); // Define este painel como o conteúdo principal da janela.
        GridBagConstraints gbc = new GridBagConstraints(); // Objeto para configurar as restrições do GridBagLayout.
        gbc.insets = new Insets(10, 10, 10, 10); // Define o espaçamento (margem) entre os componentes.

        // Painel da barra de título (para botões de minimizar e fechar)
        JPanel titleBar = new JPanel(new GridBagLayout());
        titleBar.setBackground(new Color(45, 45, 45)); // Cor de fundo da barra de título.
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Ocupa duas colunas.
        gbc.anchor = GridBagConstraints.EAST; // Alinha os componentes à direita.
        mainPanel.add(titleBar, gbc); // Adiciona a barra de título ao painel principal.

        // Botão de minimizar a janela.
        JButton minimizeButton = new JButton("_");
        minimizeButton.setFont(new Font("Arial", Font.BOLD, 16)); // Define a fonte do texto.
        minimizeButton.setForeground(Color.WHITE); // Define a cor do texto.
        minimizeButton.setBackground(new Color(60, 60, 60)); // Define a cor de fundo do botão.
        minimizeButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Remove a borda padrão.
        minimizeButton.setFocusPainted(false); // Remove o destaque de foco.
        // Adiciona um listener para minimizar a janela quando o botão é clicado.
        minimizeButton.addActionListener(e -> setState(JFrame.ICONIFIED));
        titleBar.add(minimizeButton); // Adiciona o botão à barra de título.

        // Botão de fechar a aplicação.
        JButton closeButton = new JButton("X");
        closeButton.setFont(new Font("Arial", Font.BOLD, 16)); // Define a fonte do texto.
        closeButton.setForeground(Color.WHITE); // Define a cor do texto.
        closeButton.setBackground(new Color(231, 76, 60)); // Define a cor de fundo do botão (vermelho).
        closeButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Remove a borda padrão.
        closeButton.setFocusPainted(false); // Remove o destaque de foco.
        // Adiciona um listener para encerrar a aplicação quando o botão é clicado.
        closeButton.addActionListener(e -> System.exit(0));
        titleBar.add(closeButton); // Adiciona o botão à barra de título.

        // JLabel para exibir a thumbnail do vídeo.
        jThumb = new JLabel();
        jThumb.setPreferredSize(new Dimension(480, 270)); // Define o tamanho preferencial.
        jThumb.setHorizontalAlignment(SwingConstants.CENTER); // Centraliza horizontalmente.
        jThumb.setVerticalAlignment(SwingConstants.CENTER); // Centraliza verticalmente.
        jThumb.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1)); // Adiciona uma borda.
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL; // Preenche horizontalmente.
        mainPanel.add(jThumb, gbc); // Adiciona a thumbnail ao painel principal.

        // JLabel para exibir o título do vídeo ou mensagens de status.
        jInfo = new JLabel("Insira o link do vídeo para começar", SwingConstants.CENTER);
        jInfo.setFont(new Font("Arial", Font.BOLD, 16)); // Define a fonte do texto.
        jInfo.setForeground(Color.WHITE); // Define a cor do texto.
        jInfo.setPreferredSize(new Dimension(780, 40)); // Define o tamanho preferencial.
        gbc.gridy = 2;
        mainPanel.add(jInfo, gbc); // Adiciona o label de informação ao painel principal.

        // JTextField para o usuário inserir o link do vídeo.
        jLink = new JTextField();
        jLink.setFont(new Font("Arial", Font.PLAIN, 14)); // Define a fonte do texto.
        jLink.setBackground(new Color(60, 60, 60)); // Define a cor de fundo.
        jLink.setForeground(Color.WHITE); // Define a cor do texto.
        // Define uma borda composta para o campo de texto.
        jLink.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(80, 80, 80)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        gbc.gridy = 3;
        mainPanel.add(jLink, gbc); // Adiciona o campo de link ao painel principal.

        // JComboBox para selecionar o formato de download (vídeo/áudio e qualidades).
        jFormato = new JComboBox<>(new String[]{"Selecione um formato", "VIDEO", "(MP4 - 1080p60)", "(MP4 - 720p60)", "(MP4 - 480p)", "(MP4 - 360p)", "(MP4 - 240p)", "(MP4 - 144p)", "AUDIO", "(MP3 - 320kbps)", "(MP3 - 256kbps)", "(MP3 - 128kbps)"});
        jFormato.setBackground(new Color(60, 60, 60)); // Define a cor de fundo.
        jFormato.setForeground(Color.WHITE); // Define a cor do texto.
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(jFormato, gbc); // Adiciona o combobox de formato ao painel principal.

        // Botão para selecionar o diretório de download.
        jDiretorio = new RoundedButton("Diretório"); // Usa a classe customizada RoundedButton.
        gbc.gridx = 1;
        mainPanel.add(jDiretorio, gbc); // Adiciona o botão de diretório ao painel principal.

        // Botão de download.
        jDownload = new RoundedButton("Download"); // Usa a classe customizada RoundedButton.
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        mainPanel.add(jDownload, gbc); // Adiciona o botão de download ao painel principal.

        // JProgressBar para exibir o progresso do download.
        jProgressBar = new JProgressBar(0, 100);
        jProgressBar.setStringPainted(true); // Exibe o valor percentual na barra.
        jProgressBar.setForeground(new Color(52, 152, 219)); // Define a cor da barra de progresso.
        jProgressBar.setBackground(new Color(60, 60, 60)); // Define a cor de fundo da barra.
        jProgressBar.setBorder(BorderFactory.createEmptyBorder()); // Remove a borda padrão.
        // Personaliza a UI da barra de progresso para cores de seleção.
        jProgressBar.setUI(new BasicProgressBarUI() {
            protected Color getSelectionBackground() { return Color.WHITE; }
            protected Color getSelectionForeground() { return Color.BLACK; }
        });
        gbc.gridy = 6;
        mainPanel.add(jProgressBar, gbc); // Adiciona a barra de progresso ao painel principal.

        // Adiciona listeners de ação aos componentes.
        // Listener para o campo de link, para atualizar informações do vídeo ao digitar.
        jLink.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updateVideoInfo(); } // Chamado quando texto é inserido.
            public void removeUpdate(javax.swing.event.DocumentEvent e) {} // Não faz nada quando texto é removido.
            public void changedUpdate(javax.swing.event.DocumentEvent e) {} // Não faz nada quando atributos do texto são alterados.
        });
        // Listener para o combobox de formato.
        jFormato.addActionListener(this::jFormatoActionPerformed);
        // Listener para o botão de diretório.
        jDiretorio.addActionListener(this::jDiretorioActionPerformed);
        // Listener para o botão de download.
        jDownload.addActionListener(this::jDownloadActionPerformed);
    }

    /**
     * Atualiza as informações do vídeo (título e thumbnail) com base no link inserido.
     * Executa a busca de informações em uma thread separada para não travar a UI.
     */
    private void updateVideoInfo() {
        String link = jLink.getText(); // Obtém o texto do campo de link.
        // Verifica se o link está vazio ou é o texto de placeholder.
        if (link.isEmpty() || link.equals("Insira o link do video")) {
            return; // Sai do método se o link não for válido para busca.
        }
        video.setLink(link); // Define o link no objeto Video.

        // Cria um SwingWorker para executar a operação de busca de informações em segundo plano.
        new SwingWorker<YoutubeDownloader.VideoInfo, Void>() {
            @Override
            protected YoutubeDownloader.VideoInfo doInBackground() throws Exception {
                // Executa a busca de informações do vídeo usando YoutubeDownloader.
                return YoutubeDownloader.getVideoInfo(link);
            }

            @Override
            protected void done() {
                try {
                    // Obtém o resultado da operação em segundo plano.
                    YoutubeDownloader.VideoInfo videoInfo = get();
                    if (videoInfo != null) {
                        // Se as informações foram encontradas, atualiza o título e a thumbnail.
                        video.setTitulo(videoInfo.getTitle());
                        loadThumbnail(videoInfo.getThumbnailUrl(), jThumb); // Carrega a thumbnail.
                        // Atualiza o JLabel com o título do vídeo, centralizado.
                        jInfo.setText("<html><div style='width: 760px; text-align: center;'>" + video.getTitulo() + "</div></html>");
                    } else {
                        // Se as informações não foram encontradas, limpa a thumbnail e exibe mensagem de erro.
                        jThumb.setIcon(null);
                        jInfo.setText("Link inválido ou não foi possível carregar as informações.");
                    }
                } catch (Exception e) {
                    // Em caso de erro, imprime o stack trace, limpa a thumbnail e exibe mensagem de erro.
                    e.printStackTrace();
                    jThumb.setIcon(null);
                    jInfo.setText("Erro ao carregar informações do vídeo.");
                }
            }
        }.execute(); // Inicia a execução do SwingWorker.
    }

    /**
     * Manipula o evento de clique do botão de download.
     * Inicia o processo de download após validar o link, diretório e formato.
     * @param evt O evento de ação.
     */
    private void jDownloadActionPerformed(java.awt.event.ActionEvent evt) {
        jDownload.setEnabled(false); // Desabilita o botão de download para evitar múltiplos cliques.
        String linkVideo = video.getLink(); // Obtém o link do vídeo do objeto Video.
        // Verifica se o link do vídeo é válido.
        if (linkVideo != null && !linkVideo.trim().isEmpty()) {
            String downloadPath = video.getDiretorio(); // Obtém o diretório de download.
            // Valida se um diretório de download foi selecionado.
            if (downloadPath == null || downloadPath.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, selecione um diretório de download.", "Diretório Inválido", JOptionPane.WARNING_MESSAGE);
                jDownload.setEnabled(true); // Reabilita o botão de download.
                return;
            }
            // Valida se um formato de download válido foi selecionado.
            if (video.getFormato() == null || video.getFormato().equals("Selecione um formato") || video.getFormato().equals("VIDEO") || video.getFormato().equals("AUDIO")) {
                JOptionPane.showMessageDialog(this, "Por favor, selecione um formato de download válido.", "Formato Inválido", JOptionPane.WARNING_MESSAGE);
                jDownload.setEnabled(true); // Reabilita o botão de download.
                return;
            }

            // Inicia o download usando YoutubeDownloader, passando callbacks para progresso e conclusão.
            YoutubeDownloader.download(video,
                    (progress) -> jProgressBar.setValue(progress), // Callback para atualizar a barra de progresso.
                    () -> {
                        jProgressBar.setValue(100); // Define o progresso para 100% ao concluir.
                        JOptionPane.showMessageDialog(this, "Download concluído!"); // Exibe mensagem de sucesso.
                        jProgressBar.setValue(0); // Reseta a barra de progresso.
                        jDownload.setEnabled(true); // Reabilita o botão de download.
                    });
        } else {
            // Se o link do vídeo não for válido, exibe uma mensagem de erro.
            JOptionPane.showMessageDialog(this, "Por favor, insira o link do video");
            jDownload.setEnabled(true); // Reabilita o botão de download.
        }
    }

    /**
     * Manipula o evento de seleção do JComboBox de formato.
     * Define o formato de download no objeto Video.
     * @param evt O evento de ação.
     */
    private void jFormatoActionPerformed(java.awt.event.ActionEvent evt) {
        String selectedFormat = (String) jFormato.getSelectedItem(); // Obtém o item selecionado.
        // Se o formato selecionado não for um dos placeholders, define-o no objeto Video.
        if (!selectedFormat.equals("Selecione um formato") && !selectedFormat.equals("VIDEO") && !selectedFormat.equals("AUDIO")) {
            video.setFormato(selectedFormat);
        }
    }

    /**
     * Manipula o evento de clique do botão de diretório.
     * Abre um JFileChooser para o usuário selecionar a pasta de download.
     * @param evt O evento de ação.
     */
    private void jDiretorioActionPerformed(java.awt.event.ActionEvent evt) {
        JFileChooser fileChooser = new JFileChooser(); // Cria um novo seletor de arquivos.
        fileChooser.setDialogTitle("Escolha a pasta para download"); // Define o título da caixa de diálogo.
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); // Permite selecionar apenas diretórios.
        int result = fileChooser.showOpenDialog(this); // Exibe a caixa de diálogo e obtém o resultado.
        // Se o usuário aprovou a seleção.
        if (result == JFileChooser.APPROVE_OPTION) {
            File diretorioSelecionado = fileChooser.getSelectedFile(); // Obtém o diretório selecionado.
            video.setDiretorio(diretorioSelecionado.getAbsolutePath()); // Define o diretório no objeto Video.
            JOptionPane.showMessageDialog(this, "Diretorio Escolhido: " + diretorioSelecionado.getAbsolutePath()); // Exibe mensagem de confirmação.
        }
    }

    /**
     * Carrega e exibe a thumbnail de um vídeo a partir de uma URL.
     * Redimensiona a imagem para o tamanho adequado.
     * @param thumbUrl A URL da thumbnail.
     * @param jThumb O JLabel onde a thumbnail será exibida.
     */
    private void loadThumbnail(String thumbUrl, JLabel jThumb) {
        try {
            URL url = new URL(thumbUrl); // Cria um objeto URL a partir da string.
            // Redimensiona a imagem da URL para 480x270 e a converte para BufferedImage.
            BufferedImage resizedImage = Thumbnails.of(url).size(480, 270).asBufferedImage();
            jThumb.setIcon(new ImageIcon(resizedImage)); // Define a imagem redimensionada como ícone do JLabel.
        } catch (Exception e) {
            e.printStackTrace(); // Imprime o stack trace em caso de erro.
        }
    }

    /**
     * Configura o comportamento de placeholder para o campo de texto do link.
     * Exibe um texto padrão quando o campo está vazio e o remove ao focar.
     */
    private void setupPlaceholder() {
        jLink.setText("Insira o link do video"); // Define o texto inicial de placeholder.
        jLink.setForeground(Color.GRAY); // Define a cor do texto de placeholder.
        // Adiciona um FocusListener para gerenciar o comportamento do placeholder.
        jLink.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                // Quando o campo ganha foco e contém o texto de placeholder, limpa o campo e muda a cor do texto.
                if (jLink.getText().equals("Insira o link do video")) {
                    jLink.setText("");
                    jLink.setForeground(Color.WHITE);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                // Quando o campo perde foco e está vazio, restaura o texto de placeholder e a cor.
                if (jLink.getText().isEmpty()) {
                    jLink.setText("Insira o link do video");
                    jLink.setForeground(Color.GRAY);
                }
            }
        });
    }

    /**
     * Habilita a funcionalidade de arrastar a janela sem a barra de título padrão.
     * Permite que o usuário mova a janela clicando e arrastando em qualquer parte dela.
     */
    private void makeDraggable() {
        // Adiciona um MouseListener para capturar o ponto inicial do clique.
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                pX = me.getX(); // Armazena a coordenada X do clique.
                pY = me.getY(); // Armazena a coordenada Y do clique.
            }
        });
        // Adiciona um MouseMotionListener para mover a janela enquanto o mouse é arrastado.
        addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent me) {
                // Define a nova localização da janela com base no movimento do mouse.
                setLocation(getLocation().x + me.getX() - pX, getLocation().y + me.getY() - pY);
            }
        });
    }

    /**
     * Método main para iniciar a aplicação.
     * Configura o Look and Feel do sistema e executa a interface gráfica na Event Dispatch Thread (EDT).
     * @param args Argumentos de linha de comando (não utilizados).
     */
    public static void main(String args[]) {
        try {
            // Tenta definir o Look and Feel da UI para o do sistema operacional.
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace(); // Imprime o stack trace em caso de erro ao definir o Look and Feel.
        }
        // Garante que a criação e exibição da janela ocorram na Event Dispatch Thread (EDT).
        java.awt.EventQueue.invokeLater(() -> {
            new telaDownload().setVisible(true);
        });
    }

    // Declaração dos componentes da interface gráfica.
    private JButton jDiretorio;
    private JButton jDownload;
    private JComboBox<String> jFormato;
    private JLabel jInfo;
    private JTextField jLink;
    private JLabel jThumb;
    private JProgressBar jProgressBar;

    /**
     * Classe interna para criar botões com cantos arredondados e efeitos visuais.
     * Estende JButton e sobrescreve o método paintComponent para desenho customizado.
     */
    class RoundedButton extends JButton {
        /**
         * Construtor da classe RoundedButton.
         * @param text O texto a ser exibido no botão.
         */
        public RoundedButton(String text) {
            super(text);
            setFocusPainted(false); // Remove o destaque de foco.
            setContentAreaFilled(false); // Torna a área de conteúdo transparente para o desenho customizado.
            setBorderPainted(false); // Remove a borda padrão.
            setForeground(Color.WHITE); // Define a cor do texto.
            setFont(new Font("Arial", Font.BOLD, 14)); // Define a fonte do texto.
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create(); // Cria um contexto Graphics2D para desenho.
            // Habilita o antialiasing para suavizar as bordas.
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            // Define a cor de preenchimento com base no estado do botão (pressionado, hover, normal).
            if (getModel().isPressed()) {
                g2.setColor(new Color(41, 128, 185)); // Cor quando pressionado.
            } else if (getModel().isRollover()) {
                g2.setColor(new Color(52, 152, 219).brighter()); // Cor quando o mouse está sobre o botão.
            } else {
                g2.setColor(new Color(52, 152, 219)); // Cor normal.
            }
            // Desenha um retângulo arredondado preenchido como fundo do botão.
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            super.paintComponent(g); // Chama o método paintComponent da superclasse para desenhar o texto e ícone.
            g2.dispose(); // Libera os recursos gráficos.
        }
    }
}