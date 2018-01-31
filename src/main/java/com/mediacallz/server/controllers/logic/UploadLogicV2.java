package com.mediacallz.server.controllers.logic;

import com.mediacallz.server.model.response.Response;
import com.mediacallz.server.utils.PathUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by Mor on 1/16/2017.
 */
@Component
@Slf4j
public class UploadLogicV2 extends AbstractServerLogic {

    @Value("${upload.folder.path}")
    private String uploadFolderPath;

    @Autowired
    private PathUtils pathUtils;

    public Response<String> execute(String uid, MultipartFile fileForUpload, HttpServletResponse servletResponse) {
        String uploadFilePath = uploadFolderPath + uid + "/" + fileForUpload.getOriginalFilename();
        Response<String> response = null;

        try {
             initUploadFileFlow(fileForUpload, uploadFilePath);
            String uploadedFileUrl = pathUtils.getUploadUrl() + uid + "/" + fileForUpload.getOriginalFilename();
            response = new Response<>(uploadedFileUrl);
        } catch (Exception e) {
            e.printStackTrace();
            servletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        return response;
    }

    private void initUploadFileFlow(MultipartFile fileForUpload, String uploadedFilePath) throws Exception {
        long bytesLeft = fileForUpload.getSize();
        // Preparing file placeholder
        File newFile = new File(uploadedFilePath);
        newFile.getParentFile().mkdirs();
        newFile.createNewFile();

        BufferedOutputStream bos = null;
        try {
            FileOutputStream fos = new FileOutputStream(newFile);
            bos = new BufferedOutputStream(fos);
            DataInputStream dis = new DataInputStream(fileForUpload.getInputStream());

            log.info("Reading data...");
            byte[] buf = new byte[1024 * 8];
            int bytesRead;
            while (bytesLeft > 0 && (bytesRead = dis.read(buf, 0, (int) Math.min(buf.length, bytesLeft))) != -1) {
                bos.write(buf, 0, bytesRead);
                bytesLeft -= bytesRead;
            }

            if (bytesLeft > 0) {
                throw new IOException("Upload was stopped abruptly");
            }
            else if (bytesLeft < 0) {
                throw new IOException("Read too many bytes. Upload seems corrupted.");
            }

        } finally {
            if (bos != null) {
                bos.close();
            }
        }
    }

}
