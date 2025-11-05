package com.example.projectrestfulapi.repository.SQL;

import com.example.projectrestfulapi.domain.SQL.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

}
