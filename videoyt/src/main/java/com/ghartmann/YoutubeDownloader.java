package com.ghartmann;

import java.io.BufferedReader; // Importa BufferedReader para ler a saída de processos.
import java.io.IOException; // Importa IOException para tratamento de erros de I/O.
import java.io.InputStreamReader; // Importa InputStreamReader para ler streams de entrada.
import java.util.List; // Importa List para coleções de objetos.
import java.util.function.Consumer; // Importa Consumer para interfaces funcionais.
import java.util.regex.Matcher; // Importa Matcher para encontrar correspondências de regex.
import java.util.regex.Pattern; // Importa Pattern para compilar expressões regulares.

import javax.swing.JOptionPane; // Importa JOptionPane para exibir caixas de diálogo.
import javax.swing.SwingWorker; // Importa SwingWorker para executar tarefas em segundo plano.

/**
 * Classe utilitária para baixar vídeos e áudios do YouTube usando o yt-dlp.
 */
public class YoutubeDownloader {

    /**
     * Classe interna para armazenar informações básicas de um vídeo do YouTube.
     */
    public static class VideoInfo {
        private final String title; // Título do vídeo.
        private final String thumbnailUrl; // URL da thumbnail do vídeo.

        /**
         * Construtor para criar uma nova instância de VideoInfo.
         * @param title O título do vídeo.
         * @param thumbnailUrl A URL da thumbnail do vídeo.
         */
        public VideoInfo(String title, String thumbnailUrl) {
            this.title = title;
            this.thumbnailUrl = thumbnailUrl;
        }

        /**
         * Retorna o título do vídeo.
         * @return O título do vídeo.
         */
        public String getTitle() {
            return title;
        }

        /**
         * Retorna a URL da thumbnail do vídeo.
         * @return A URL da thumbnail.
         */
        public String getThumbnailUrl() {
            return thumbnailUrl;
        }
    }

    /**
     * Extrai o ID do vídeo de uma URL do YouTube.
     * @param url A URL completa do vídeo do YouTube.
     * @return O ID do vídeo (11 caracteres) ou null se não for encontrado.
     */
    public static String extractVideoId(String url) {
        // Padrão regex para encontrar o ID do vídeo em diferentes formatos de URL.
        Pattern pattern = Pattern.compile("(?<=v=|be/|embed/|list=)[^&?\s]{11,}");
        Matcher matcher = pattern.matcher(url);
        return matcher.find() ? matcher.group() : null; // Retorna o ID se encontrado, caso contrário null.
    }

    /**
     * Obtém informações (título e thumbnail) de um vídeo do YouTube usando yt-dlp.
     * @param videoLink O link do vídeo do YouTube.
     * @return Um objeto VideoInfo contendo o título e a URL da thumbnail, ou null se as informações não puderem ser obtidas.
     */
    public static VideoInfo getVideoInfo(String videoLink) {
        try {
            String videoId = extractVideoId(videoLink); // Extrai o ID do vídeo da URL.
            if (videoId == null) {
                return null; // Retorna null se o ID do vídeo não for encontrado.
            }

            // Constrói o comando para obter o título do vídeo usando yt-dlp.
            ProcessBuilder pb = new ProcessBuilder("yt-dlp", "--get-title", videoLink);
            Process process = pb.start(); // Inicia o processo.

            // Lê a saída padrão do processo para obter o título.
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String title = reader.readLine();
            process.waitFor(); // Espera o processo terminar.

            // Se o título foi obtido com sucesso, constrói a URL da thumbnail e retorna VideoInfo.
            if (title != null && !title.isEmpty()) {
                String thumbnailUrl = "https://img.youtube.com/vi/" + videoId + "/0.jpg";
                return new VideoInfo(title, thumbnailUrl);
            }
        } catch (IOException | InterruptedException e) {
            // Ignora exceções, retornando null em caso de erro.
        }

        return null; // Retorna null se as informações não puderem ser obtidas.
    }

    /**
     * Inicia o download de um vídeo/áudio do YouTube em segundo plano.
     * @param video O objeto Video contendo as informações do download.
     * @param progressConsumer Um Consumer para atualizar o progresso do download (0-100).
     * @param onDone Um Runnable a ser executado quando o download for concluído.
     */
    public static void download(Video video, Consumer<Integer> progressConsumer, Runnable onDone) {
        // Cria um SwingWorker para executar o download em uma thread separada.
        SwingWorker<Void, Integer> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                String[] command = buildCommand(video); // Constrói o comando yt-dlp.
                try {
                    ProcessBuilder pb = new ProcessBuilder(command);
                    pb.redirectErrorStream(true); // Redireciona o erro para a saída padrão.
                    Process process = pb.start(); // Inicia o processo de download.

                    // Lê a saída do processo para capturar o progresso.
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                        String line;
                        // Regex para capturar porcentagem, mais flexível para inteiros e decimais.
                        Pattern progressPattern = Pattern.compile("\\b(\\d{1,3}(?:\\.\\d+)?)%");
                        while ((line = reader.readLine()) != null) {
                            Matcher matcher = progressPattern.matcher(line);
                            if (matcher.find()) {
                                double percent = Double.parseDouble(matcher.group(1));
                                publish((int) percent); // Publica o progresso para a thread de UI.
                            }
                        }
                    }

                    process.waitFor(); // Espera o processo de download terminar.
                } catch (Exception e) {
                    e.printStackTrace(); // Imprime o stack trace em caso de erro.
                    // Exibe uma mensagem de erro para o usuário.
                    JOptionPane.showMessageDialog(null, "Ocorreu um erro durante o download.", "Erro de Download", JOptionPane.ERROR_MESSAGE);
                }
                return null; // Retorna null, pois o resultado final não é necessário.
            }

            @Override
            protected void process(List<Integer> chunks) {
                // Atualiza a barra de progresso na thread de UI com o último valor publicado.
                if (!chunks.isEmpty()) {
                    progressConsumer.accept(chunks.get(chunks.size() - 1));
                }
            }

            @Override
            protected void done() {
                onDone.run(); // Executa o callback de conclusão na thread de UI.
            }
        };

        worker.execute(); // Inicia o SwingWorker.
    }

    /**
     * Constrói o array de comandos para o yt-dlp com base nas informações do vídeo.
     * @param video O objeto Video contendo as informações do download.
     * @return Um array de String representando o comando yt-dlp.
     */
    private static String[] buildCommand(Video video) {
        String downloadPath = video.getDiretorio(); // Obtém o diretório de download.
        String linkVideo = video.getLink(); // Obtém o link do vídeo.
        String formato = video.getFormato(); // Obtém o formato de download.

        boolean isPlaylist = linkVideo.contains("list="); // Verifica se o link é de uma playlist.

        // Define o template de nome de arquivo de saída, diferente para playlists.
        String outputTemplate = isPlaylist
            ? "%(playlist_title)s/%(playlist_index)s - %(title)s.%(ext)s" // Para playlists.
            : "%(title)s.%(ext)s"; // Para vídeos únicos.

        // Lista para construir o comando yt-dlp.
        List<String> command = new java.util.ArrayList<>();
        command.add("yt-dlp"); // Comando base.

        // Adiciona a opção de playlist ou não playlist.
        if (isPlaylist) {
            command.add("--yes-playlist");
        } else {
            command.add("--no-playlist");
        }

        // Configurações específicas para formatos de vídeo ou áudio.
        if (!formato.contains("MP3")) { // Se for formato de vídeo.
            switch (formato) {
                case "(MP4 - 1080p60)":
                    command.add("-S");
                    command.add("res:1920x1080,fps");
                    break;
                case "(MP4 - 720p60)":
                    command.add("-S");
                    command.add("res:1280:720,fps");
                    break;
                case "(MP4 - 480p)":
                    command.add("-S");
                    command.add("res:824:480,fps");
                    break;
                case "(MP4 - 360p)":
                    command.add("-S");
                    command.add("res:640:360,fps");
                    break;
                case "(MP4 - 240p)":
                    command.add("-S");
                    command.add("res:426:240,fps");
                    break;
                case "(MP4 - 144p)":
                    command.add("-S");
                    command.add("res:256:144,fps");
                    break;
            }
        } else { // Se for formato de áudio (MP3).
            command.add("-f");
            command.add("bestaudio"); // Seleciona a melhor faixa de áudio.
            command.add("--extract-audio"); // Extrai apenas o áudio.
            command.add("--audio-format");
            command.add("mp3"); // Define o formato de saída como MP3.
            command.add("--embed-thumbnail"); // Incorpora a thumbnail no arquivo de áudio.
            command.add("--add-metadata");    // Adiciona metadados ao arquivo de áudio.

            // Configura a qualidade do áudio MP3.
            switch (formato) {
                case "(MP3 - 320kbps)":
                    command.add("--audio-quality");
                    command.add("0"); // Qualidade máxima (VBR 0).
                    break;
                case "(MP3 - 256kbps)":
                    command.add("--audio-quality");
                    command.add("1"); // Qualidade alta (VBR 1).
                    break;
                case "(MP3 - 128kbps)":
                    command.add("--audio-quality");
                    command.add("5"); // Qualidade média (VBR 5).
                    break;
            }
        }

        command.add("-P");
        command.add(downloadPath);           // Adiciona o caminho da pasta de download.
        command.add("-o");
        command.add(outputTemplate);         // Adiciona o template de nome do arquivo.
        command.add(linkVideo);              // Adiciona o link do vídeo/playlist.

        return command.toArray(new String[0]); // Converte a lista de comandos para um array de String.
    }
}