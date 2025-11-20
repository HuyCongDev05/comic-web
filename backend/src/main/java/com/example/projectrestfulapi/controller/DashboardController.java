package com.example.projectrestfulapi.controller;

import com.example.projectrestfulapi.dto.response.Dashboard.DashboardResponseDTO;
import com.example.projectrestfulapi.mapper.DashboardMapper;
import com.example.projectrestfulapi.service.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

    private final TotalVisitService totalVisitService;
    private final AccountService accountService;
    private final ComicService comicService;
    private final CategoriesService categoriesService;
    private final ComicDailyViewsService comicDailyViewsService;

    public DashboardController(TotalVisitService totalVisitService, AccountService accountService, ComicService comicService,
                               CategoriesService categoriesService, ComicDailyViewsService comicDailyViewsService) {
        this.totalVisitService = totalVisitService;
        this.accountService = accountService;
        this.comicService = comicService;
        this.categoriesService = categoriesService;
        this.comicDailyViewsService = comicDailyViewsService;
    }

    @GetMapping("home")
    public ResponseEntity<DashboardResponseDTO.HomeDashboard> home() {
        DashboardResponseDTO.HomeDashboard homeDashboard = DashboardMapper.HomeDashboard(totalVisitService.handleGetTotalVisit(),
                comicService.handleGetTotalComics(), accountService.handleGetTotalAccounts(),
                comicService.handleGetTotalCompletedComics(), totalVisitService.handleViewsAndVisits(),
                categoriesService.handleGetCategoryRatio(), comicDailyViewsService.handleViewsRatioComics());
        return ResponseEntity.ok().body(homeDashboard);
    }

    @GetMapping("/accounts")
    public ResponseEntity<DashboardResponseDTO.AccountsDashboard> accounts(@RequestParam(name = "page", defaultValue = "1") int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1, 8);
        Page<DashboardResponseDTO.AccountsDashboard.Accounts> accounts = accountService.handleGetAllAccounts(pageable);
        DashboardResponseDTO.AccountsDashboard accountsDashboard = DashboardMapper.AccountsDashboard(accounts, accounts.getTotalPages(), accounts.getNumberOfElements());
        return ResponseEntity.ok().body(accountsDashboard);
    }
}
