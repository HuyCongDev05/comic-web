package com.example.projectrestfulapi.service;

import com.example.projectrestfulapi.domain.SQL.TotalVisit;
import com.example.projectrestfulapi.repository.SQL.TotalVisitRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class TotalVisitService {
    private final TotalVisitRepository totalVisitRepository;
    public TotalVisitService(TotalVisitRepository totalVisitRepository) {
        this.totalVisitRepository = totalVisitRepository;
    }

    public void handleSaveTotalVisit(LocalDate localDate) {

        TotalVisit totalVisit = totalVisitRepository.findByDay(localDate)
                .orElseGet(() -> {
                    TotalVisit tv = new TotalVisit();
                    tv.setDay(localDate);
                    tv.setTotalRequest(0L);
                    return tv;
                });

        totalVisit.setTotalRequest(totalVisit.getTotalRequest() + 1);

        totalVisitRepository.save(totalVisit);
    }
}
