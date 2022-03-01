package com.pokemon.model.Networking;

import lombok.Data;

import java.io.*;

public @Data class FileTransferWrapper implements Serializable {
    private File file;
    private byte[] content = new byte[32 * 1024];

    public FileTransferWrapper(File file) throws Exception {
        this.file = file;
        FileInputStream fis = new FileInputStream(file);
        fis.read(content, 0 , content.length);
    }
}
