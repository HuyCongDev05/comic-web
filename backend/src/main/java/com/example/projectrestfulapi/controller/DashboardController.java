package com.example.projectrestfulapi.controller;

import com.example.projectrestfulapi.dto.request.account.ChangeAccountDTO;
import com.example.projectrestfulapi.dto.response.Dashboard.DashboardResponseDTO;
import com.example.projectrestfulapi.mapper.DashboardMapper;
import com.example.projectrestfulapi.service.*;
import com.example.projectrestfulapi.util.Security.CheckRoleUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

    private final TotalVisitService totalVisitService;
    private final AccountService accountService;
    private final ComicService comicService;
    private final CategoriesService categoriesService;
    private final ComicDailyViewsService comicDailyViewsService;
    private final CheckRoleUtil checkRoleUtil;

    public DashboardController(TotalVisitService totalVisitService, AccountService accountService, ComicService comicService,
                               CategoriesService categoriesService, ComicDailyViewsService comicDailyViewsService, CheckRoleUtil checkRoleUtil) {
        this.totalVisitService = totalVisitService;
        this.accountService = accountService;
        this.comicService = comicService;
        this.categoriesService = categoriesService;
        this.comicDailyViewsService = comicDailyViewsService;
        this.checkRoleUtil = checkRoleUtil;
    }

    @GetMapping("home")
    public ResponseEntity<DashboardResponseDTO.HomeDashboard> home(HttpServletRequest request) {
        checkRoleUtil.checkRole(request);
        DashboardResponseDTO.HomeDashboard homeDashboard = DashboardMapper.HomeDashboard(totalVisitService.handleGetTotalVisit(),
                comicService.handleGetTotalComics(), accountService.handleGetTotalAccounts(),
                comicService.handleGetTotalCompletedComics(), totalVisitService.handleViewsAndVisits(),
                categoriesService.handleGetCategoryRatio(), comicDailyViewsService.handleViewsRatioComics());
        return ResponseEntity.ok().body(homeDashboard);
    }

    @GetMapping("/accounts")
    public ResponseEntity<DashboardResponseDTO.AccountsDashboard> accounts(@RequestParam(name = "page", defaultValue = "1") int pageNumber, HttpServletRequest request) {
        checkRoleUtil.checkRole(request);
        Pageable pageable = PageRequest.of(pageNumber - 1, 8);
        Page<DashboardResponseDTO.AccountsDashboard.Accounts> accounts = accountService.handleGetAllAccounts(pageable);
        DashboardResponseDTO.AccountsDashboard accountsDashboard = DashboardMapper.AccountsDashboard(accounts, accounts.getTotalPages(), accounts.getNumberOfElements());
        return ResponseEntity.ok().body(accountsDashboard);
    }

    @PostMapping("/accounts")
    public ResponseEntity<Void> updateStatusAccounts(@RequestParam(name = "account_uuid") String accountUuid, @Valid @RequestBody ChangeAccountDTO.changeStatusAccounts status, HttpServletRequest request) {
        checkRoleUtil.checkRole(request);
        accountService.handleUpdateStatusAccounts(status.getStatus(), accountUuid);
        return ResponseEntity.ok().build();
    }
}
