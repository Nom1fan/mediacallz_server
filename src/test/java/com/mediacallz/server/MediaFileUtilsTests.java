package com.mediacallz.server;

import com.mediacallz.server.model.dto.MediaFileDTO;
import com.mediacallz.server.utils.FileExplorer;
import com.mediacallz.server.utils.MediaFileUtils;
import com.mediacallz.server.utils.MediaFilesUtilsImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

import static org.mockito.Mockito.*;

/**
 * Created by Mor on 03/06/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MediaFileUtilsTests {

    @Mock
    FileExplorer fileExplorer;

    MediaFilesUtilsImpl mediaFileUtils = new MediaFilesUtilsImpl(); // Class under test

    @Test
    public void deleteVideoByImageTest() {
        String folderPath = "/fake/path/";
        File[] files = new File[] { new File(folderPath + "someFile.mp4") };
        MediaFileDTO mediaFile = new MediaFileDTO();
        mediaFile.setFileType(MediaFileDTO.FileType.IMAGE);

        when(fileExplorer.getFiles(folderPath))
                .thenReturn(files);

        mediaFileUtils.setFileExplorer(fileExplorer);

        mediaFileUtils.deleteFilesIfNecessary(folderPath, mediaFile);

        verify(fileExplorer, times(1)).delete(files[0]);
    }

    @Test
    public void deleteAudioByVideoTest() {
        String folderPath = "/fake/path/";
        File[] files = new File[] { new File(folderPath + "someFile.mp3") };
        MediaFileDTO mediaFile = new MediaFileDTO();
        mediaFile.setFileType(MediaFileDTO.FileType.VIDEO);

        when(fileExplorer.getFiles(folderPath))
                .thenReturn(files);

        mediaFileUtils.setFileExplorer(fileExplorer);

        mediaFileUtils.deleteFilesIfNecessary(folderPath, mediaFile);

        verify(fileExplorer, times(1)).delete(files[0]);
    }

    @Test
    public void deleteAudioAndImageByVideoTest() {
        String folderPath = "/fake/path/";
        File[] files = new File[]{
                new File(folderPath + "someFile.mp3"),
                new File(folderPath + "someFile.jpg")
        };
        MediaFileDTO mediaFile = new MediaFileDTO();
        mediaFile.setFileType(MediaFileDTO.FileType.VIDEO);

        when(fileExplorer.getFiles(folderPath))
                .thenReturn(files);

        mediaFileUtils.setFileExplorer(fileExplorer);

        mediaFileUtils.deleteFilesIfNecessary(folderPath, mediaFile);

        verify(fileExplorer, times(1)).delete(files[0]);
        verify(fileExplorer, times(1)).delete(files[1]);
    }

    @Test
    public void audioDoesNotDeleteImageTest() {
        String folderPath = "/fake/path/";
        File[] files = new File[] { new File(folderPath + "someFile.jpeg") };
        MediaFileDTO mediaFile = new MediaFileDTO();
        mediaFile.setFileType(MediaFileDTO.FileType.AUDIO);

        when(fileExplorer.getFiles(folderPath))
                .thenReturn(files);

        mediaFileUtils.setFileExplorer(fileExplorer);

        mediaFileUtils.deleteFilesIfNecessary(folderPath, mediaFile);

        verify(fileExplorer, times(0)).delete(files[0]);
    }

    @Test
    public void imageDoesNotDeleteAudioTest() {
        String folderPath = "/fake/path/";
        File[] files = new File[] { new File(folderPath + "someFile.flac") };
        MediaFileDTO mediaFile = new MediaFileDTO();
        mediaFile.setFileType(MediaFileDTO.FileType.IMAGE);

        when(fileExplorer.getFiles(folderPath))
                .thenReturn(files);

        mediaFileUtils.setFileExplorer(fileExplorer);

        mediaFileUtils.deleteFilesIfNecessary(folderPath, mediaFile);

        verify(fileExplorer, times(0)).delete(files[0]);
    }
}
