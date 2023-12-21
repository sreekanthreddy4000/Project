package com.example.findmyproject.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "project")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "projectid")
    private int projectId;

    @Column(name = "projectname")
    private String projectName;

    @Column(name = "budget")
    private double budget;

    @ManyToMany(mappedBy = "projects")
    @JsonIgnoreProperties("projects")
    private List<Researcher> researchers;

    public Project(int projectId, String projectName, double budget, List<Researcher> researchers) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.budget = budget;
        this.researchers = researchers;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public List<Researcher> getResearchers() {
        return researchers;
    }

    public void setResearchers(List<Researcher> researchers) {
        this.researchers = researchers;
    }
}