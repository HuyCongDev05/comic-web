package com.example.projectrestfulapi.mapper;

import com.example.projectrestfulapi.dto.response.Dashboard.DashboardResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public class DashboardMapper {
    public static DashboardResponseDTO.HomeDashboard HomeDashboard(Long totalVisits, Long totalComics, Long totalAccounts,
                                                                   Long totalCompletedComics, List<DashboardResponseDTO.HomeDashboard.ViewsAndVisits> viewsAndVisits,
                                                                   List<DashboardResponseDTO.HomeDashboard.CategoriesRatio> categoriesRatio,
                                                                   List<DashboardResponseDTO.HomeDashboard.ViewsRatioComics> viewsRatioComics) {
        DashboardResponseDTO.HomeDashboard dashboardResponseDTO = new DashboardResponseDTO.HomeDashboard();
        dashboardResponseDTO.setTotalVisits(totalVisits);
        dashboardResponseDTO.setViewsAndVisits(viewsAndVisits);
        dashboardResponseDTO.setTotalComics(totalComics);
        dashboardResponseDTO.setTotalAccounts(totalAccounts);
        dashboardResponseDTO.setTotalCompletedComics(totalCompletedComics);
        dashboardResponseDTO.setCategoriesRatio(categoriesRatio);
        dashboardResponseDTO.setViewsRatioComics(viewsRatioComics);
        return dashboardResponseDTO;
    }

    public static DashboardResponseDTO.AccountsDashboard AccountsDashboard(Page<DashboardResponseDTO.AccountsDashboard.Accounts> accounts, int totalPages, int currentPageSize) {
        if (accounts == null) return null;
        DashboardResponseDTO.AccountsDashboard dashboardResponseDTO = new DashboardResponseDTO.AccountsDashboard();
        dashboardResponseDTO.setContent(accounts.getContent());
        dashboardResponseDTO.setCurrentPageSize(currentPageSize);
        dashboardResponseDTO.setTotalPages(totalPages);
        return dashboardResponseDTO;
    }
}
