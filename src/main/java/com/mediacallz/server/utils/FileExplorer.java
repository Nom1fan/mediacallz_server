package com.mediacallz.server.utils;

import java.io.File;

/**
 * Created by Mor on 03/06/2017.
 */
public interface FileExplorer {

    File[] getFiles(String folderPath);

    void delete(File file);
}
