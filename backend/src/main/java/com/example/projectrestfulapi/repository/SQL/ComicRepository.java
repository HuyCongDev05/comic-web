package com.example.projectrestfulapi.repository.SQL;

import com.example.projectrestfulapi.domain.SQL.Comic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComicRepository extends JpaRepository<Comic, Long> {
    @Query(value = "select * from comic where status = 'đang cập nhật' order by update_time desc limit 20 offset :offset", nativeQuery = true)
    List<Comic> getNewComic(@Param("offset") int offset);

    @Query(value = "select * from comic where status = 'đã hoàn thành' order by update_time desc limit 20 offset :offset", nativeQuery = true)
    List<Comic> getCompletedComic(@Param("offset") int offset);
}
