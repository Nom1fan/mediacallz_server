package com.mediacallz.server.utils;

import org.springframework.stereotype.Component;

import java.io.File;

/**
 * Created by Mor on 03/06/2017.
 */
@Component
public class FileExplorerImpl implements FileExplorer {
    @Override
    public File[] getFiles(String folderPath) {
        return new File(folderPath).listFiles();
    }

    /**
     * Allows to delete a file safely (renaming first)
     *
     * @param file - The file to delete
     */
    @Override
    public void delete(File file) {

        final File to = new File(file.getAbsolutePath() + System.currentTimeMillis());
        file.renameTo(to);
        to.delete();
    }
}
