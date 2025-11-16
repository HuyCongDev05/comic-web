package com.example.projectrestfulapi.service;

import com.example.projectrestfulapi.domain.SQL.TotalVisit;
import com.example.projectrestfulapi.dto.response.Dashboard.DashboardResponseDTO;
import com.example.projectrestfulapi.repository.SQL.TotalVisitRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TotalVisitService {
    private final TotalVisitRepository totalVisitRepository;
    public TotalVisitService(TotalVisitRepository totalVisitRepository) {
        this.totalVisitRepository = totalVisitRepository;
    }

    public void handleSaveTotalVisit() {

        TotalVisit totalVisit = totalVisitRepository.findByDate(LocalDate.now())
                .orElseGet(() -> {
                    TotalVisit tv = new TotalVisit();
                    tv.setDate(LocalDate.now());
                    tv.setTotalRequest(0L);
                    return tv;
                });
        totalVisit.setTotalRequest(totalVisit.getTotalRequest() + 1);
        totalVisitRepository.save(totalVisit);
    }

    public Long handleGetTotalVisit() {
        return totalVisitRepository.totalVisit();
    }

    public List<DashboardResponseDTO.HomeDashboard.ViewsAndVisits> handleViewsAndVisits() {
        return totalVisitRepository.findViewsAndVisits();
    }
}
