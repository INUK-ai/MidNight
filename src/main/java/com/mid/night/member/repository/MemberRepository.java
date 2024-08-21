package com.mid.night.member.repository;

import aj.org.objectweb.asm.commons.Remapper;
import com.mid.night.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findMemberByNickName(String nickName);

    Optional<Member> findMemberById(Long currentMemberId);
}
