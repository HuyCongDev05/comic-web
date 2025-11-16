package com.example.projectrestfulapi.mapper;

import com.example.projectrestfulapi.dto.response.Dashboard.DashboardResponseDTO;

import java.util.List;

public class DashboardMapper {
    public static DashboardResponseDTO.HomeDashboard HomeDashboard (Long totalVisits, Long totalComics, Long totalAccounts,
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
}
