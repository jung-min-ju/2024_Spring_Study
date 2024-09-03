package com.example.filemanage.dto;

import com.example.filemanage.fileMetaData.FileMetaData;

public record FileRes(
        Integer id,
        String fileUrl
) {
    public static FileRes fromEntity(FileMetaData fileMetaData) {
        return new FileRes(
                fileMetaData.getId(),
                fileMetaData.getFile_path()
        );
    }
}
