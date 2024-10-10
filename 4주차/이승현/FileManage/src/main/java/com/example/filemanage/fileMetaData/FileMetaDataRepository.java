package com.example.filemanage.fileMetaData;

import com.example.filemanage.user.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileMetaDataRepository extends JpaRepository<FileMetaData, Integer> {

    Page<FileMetaData> findAllByUser(Users user, Pageable pageable);
}
