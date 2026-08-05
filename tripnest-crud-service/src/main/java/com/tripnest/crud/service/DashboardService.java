package com.tripnest.crud.service;

import com.tripnest.crud.repository.CompanyRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {
    private final JdbcTemplate jdbc;
    private final CompanyRepository companies;

    public DashboardService(JdbcTemplate jdbc, CompanyRepository companies) { this.jdbc = jdbc; this.companies = companies; }

    public Map<String, Object> company(Jwt jwt) {
        if (!"COMPANY".equalsIgnoreCase(jwt.getClaimAsString("role"))) throw new AccessDeniedException("Company access is required");
        Number claim = jwt.getClaim("userId"); if (claim == null) throw new AccessDeniedException("Company identity is missing");
        int companyId = companies.findByUserId(claim.intValue()).orElseThrow(() -> new AccessDeniedException("Company profile was not found")).getCompanyId();
        return Map.of("totalPackages", count("select count(*) from travel_package where company_id=?", companyId), "activePackages", count("select count(*) from travel_package where company_id=? and status='ACTIVE'", companyId), "inactivePackages", count("select count(*) from travel_package where company_id=? and status='INACTIVE'", companyId), "upcomingTrips", count("select count(*) from trips t join travel_package p on p.package_id=t.package_id where p.company_id=? and t.trip_status='UPCOMING'", companyId), "bookingAmount", amount("select coalesce(sum(b.total_amount),0) from booking b join trips t on t.trip_id=b.trip_id join travel_package p on p.package_id=t.package_id where p.company_id=? and b.booking_status='CONFIRMED'", companyId));
    }

    public Map<String, Object> admin(Jwt jwt) {
        if (!"ADMIN".equalsIgnoreCase(jwt.getClaimAsString("role"))) throw new AccessDeniedException("Administrator access is required");
        return Map.of(
                "totalPackages", count("select count(*) from travel_package"),
                "activePackages", count("select count(*) from travel_package where status='ACTIVE'"),
                "inactivePackages", count("select count(*) from travel_package where status='INACTIVE'"),
                "totalUsers", count("select count(*) from users"),
                "totalCompanies", count("select count(*) from company"),
                "bookingAmount", amount("select coalesce(sum(total_amount),0) from booking where booking_status='CONFIRMED'"),
                "monthlyRevenue", rows("select date_format(booking_date,'%b %Y') as label, coalesce(sum(total_amount),0) as value from booking where booking_status='CONFIRMED' group by year(booking_date), month(booking_date), date_format(booking_date,'%b %Y') order by year(booking_date), month(booking_date) limit 6"),
                "bookingStatus", rows("select booking_status as label, count(*) as value from booking group by booking_status"),
                "popularDestinations", rows("select p.destination as label, count(b.booking_id) as value from booking b join trips t on t.trip_id=b.trip_id join travel_package p on p.package_id=t.package_id where b.booking_status='CONFIRMED' group by p.destination order by value desc limit 5")
        );
    }

    private Integer count(String sql, Object... args) { return jdbc.queryForObject(sql, Integer.class, args); }
    private BigDecimal amount(String sql, Object... args) { return jdbc.queryForObject(sql, BigDecimal.class, args); }
    private List<Map<String, Object>> rows(String sql) { return jdbc.queryForList(sql); }
}
