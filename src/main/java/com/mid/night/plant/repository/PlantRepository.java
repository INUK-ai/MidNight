package com.mid.night.plant.repository;

import com.mid.night.member.domain.Member;
import com.mid.night.plant.domain.Plant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlantRepository extends JpaRepository<Plant, Long> {
    Optional<Plant> findPlantByMember(Member member);
}
