package spring.sw.week4.domain.file.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.sw.week4.domain.file.model.FileMetaInfo;

public interface FileRepository extends JpaRepository<FileMetaInfo, Integer> {

}
