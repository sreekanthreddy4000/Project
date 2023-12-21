package com.example.findmyproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.findmyproject.model.Researcher;
import com.example.findmyproject.model.Project;
import com.example.findmyproject.repository.ProjectJpaRepository;
import com.example.findmyproject.repository.ResearcherJpaRepository;
import com.example.findmyproject.repository.ProjectRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ProjectJpaService implements ProjectRepository {

    @Autowired
    private ResearcherJpaRepository researcherJpaRepository;

    @Autowired
    private ProjectJpaRepository projectJpaRepository;

    @Override
    public ArrayList<Project> getProjects() {
        List<Project> projectList = projectJpaRepository.findAll();
        ArrayList<Project> projects = new ArrayList<>(projectList);
        return projects;
    }

    @Override
    public Project getProjectById(int projectId) {
        try {
            Project project = projectJpaRepository.findById(projectId).get();
            return project;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Project addProject(Project project) {

        List<Integer> researcherIds = new ArrayList<>();

        for (Researcher researcher : project.getResearchers()) {
            researcherIds.add(researcher.getResearcherId());
        }

        List<Researcher> researchers = researcherJpaRepository.findAllById(researcherIds);
        project.setResearchers(researchers);

        for (Researcher researcher : researchers) {
            researcher.getProjects().add(project);
        }

        Project savedProject = projectJpaRepository.save(project);
        researcherJpaRepository.saveAll(researchers);

        return savedProject;
    }

    @Override
    public Project updateProject(int projectId, Project project) {
        try {
            Project newProject = projectJpaRepository.findById(projectId).get();
            if (project.getProjectName() != null) {
                newProject.setProjectName(project.getProjectName());
            }
            if (project.getBudget() != 0) {
                newProject.setBudget(project.getBudget());
            }
            if (project.getResearchers() != null) {

                List<Researcher> researchers = newProject.getResearchers();

                for (Researcher researcher : researchers) {
                    researcher.getProjects().remove(newProject);
                }

                researcherJpaRepository.saveAll(researchers);

                List<Integer> newResearcherIds = new ArrayList<>();

                for (Researcher researcher : project.getResearchers()) {
                    newResearcherIds.add(researcher.getResearcherId());
                }

                List<Researcher> newResearchers = researcherJpaRepository.findAllById(newResearcherIds);

                for (Researcher researcher : newResearchers) {
                    researcher.getProjects().add(newProject);
                }

                researcherJpaRepository.saveAll(newResearchers);
                newProject.setResearchers(newResearchers);
            }
            newProject = projectJpaRepository.save(newProject);
            return newProject;

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void deleteProject(int projectId) {
        try {
            Project project = projectJpaRepository.findById(projectId).get();

            List<Researcher> researchers = project.getResearchers();
            for (Researcher researcher : researchers) {
                researcher.getProjects().remove(project);
            }

            researcherJpaRepository.saveAll(researchers);

            projectJpaRepository.deleteById(projectId);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }

    @Override
    public List<Researcher> getProjectResearchers(int projectId) {
        try {
            Project project = projectJpaRepository.findById(projectId).get();
            return project.getResearchers();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
