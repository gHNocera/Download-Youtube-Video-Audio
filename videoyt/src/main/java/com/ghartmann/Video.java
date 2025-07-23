/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.ghartmann;

/**
 * Classe que representa um objeto de vídeo, armazenando informações relevantes para download.
 */
class Video {

    String link; // Armazena o link (URL) do vídeo do YouTube.

    String formato; // Armazena o formato de download selecionado (ex: MP4 - 1080p, MP3 - 320kbps).

    String thumb; // Armazena a URL da thumbnail do vídeo.

    String diretorio; // Armazena o caminho do diretório onde o vídeo/áudio será salvo.

    String titulo; // Armazena o título do vídeo.

    /**
     * Retorna o link (URL) do vídeo.
     * @return O link do vídeo.
     */
    public String getLink() {
        return link;
    }

    /**
     * Define o link (URL) do vídeo.
     * @param link O link do vídeo a ser definido.
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * Retorna o formato de download selecionado.
     * @return O formato de download.
     */
    public String getFormato() {
        return formato;
    }

    /**
     * Define o formato de download.
     * @param formato O formato de download a ser definido.
     */
    public void setFormato(String formato) {
        this.formato = formato;
    }

    /**
     * Retorna a URL da thumbnail do vídeo.
     * @return A URL da thumbnail.
     */
    public String getThumb() {
        return thumb;
    }

    /**
     * Define a URL da thumbnail do vídeo.
     * @param thumb A URL da thumbnail a ser definida.
     */
    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    /**
     * Retorna o diretório de download selecionado.
     * @return O caminho do diretório de download.
     */
    public String getDiretorio() {
        return diretorio;
    }

    /**
     * Define o diretório de download.
     * @param diretorio O caminho do diretório de download a ser definido.
     */
    public void setDiretorio(String diretorio) {
        this.diretorio = diretorio;
    }

    /**
     * Retorna o título do vídeo.
     * @return O título do vídeo.
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Define o título do vídeo.
     * @param titulo O título do vídeo a ser definido.
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

}
