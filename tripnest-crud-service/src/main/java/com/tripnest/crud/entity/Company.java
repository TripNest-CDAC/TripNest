package com.tripnest.crud.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "company")
public class Company {

    @Id
    @Column(name = "company_id")
    private Integer companyId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    public Integer getCompanyId() { return companyId; }
    public Integer getUserId() { return userId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyId(Integer companyId) { this.companyId = companyId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
}
