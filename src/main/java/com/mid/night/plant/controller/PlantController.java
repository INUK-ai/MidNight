package com.mid.night.plant.controller;

import com.mid.night._core.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/plant")
public class PlantController {

    private static final String CurrentNickName = "MTVS";

    /*
        식물 정보 가져오기
     */

    @GetMapping
    public ResponseEntity<?> getPlantInfo() {

        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    /*
        식물 성장 완료
     */
    @PostMapping
    public ResponseEntity<?> addPlant() {

        return ResponseEntity.ok().body(ApiUtils.success(null));
    }
}
