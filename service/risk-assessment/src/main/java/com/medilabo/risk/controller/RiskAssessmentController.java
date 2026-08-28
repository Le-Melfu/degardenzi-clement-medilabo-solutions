package com.medilabo.risk.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.medilabo.risk.domain.DetectionMode;
import com.medilabo.risk.dto.RiskAssessmentResponse;
import com.medilabo.risk.service.RiskAssessmentService;

@RestController
@RequestMapping("/risk")
public class RiskAssessmentController {

    private final RiskAssessmentService riskAssessmentService;

    public RiskAssessmentController(RiskAssessmentService riskAssessmentService) {
        this.riskAssessmentService = riskAssessmentService;
    }

    @GetMapping("/{patId}")
    public RiskAssessmentResponse assess(
            @PathVariable Long patId,
            @RequestParam(required = false) String mode) {
        return riskAssessmentService.assess(patId, DetectionMode.fromParam(mode));
    }
}
