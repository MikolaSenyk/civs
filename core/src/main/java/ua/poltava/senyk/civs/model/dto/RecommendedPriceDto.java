/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ua.poltava.senyk.civs.model.dto;

/**
 * Data Transfer Object for recommended price
 * @author Msenyk
 */
public class RecommendedPriceDto extends MessageDto {
    
    // fields
	private long id;
    private AssistanceGroupDto group;
	private String name;
    private String measure;
	private double gradeOne;
    private double gradeTwo;
    private double outOfSeason;

    public RecommendedPriceDto() {
        this.id = 0L;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public AssistanceGroupDto getGroup() {
        return group;
    }

    public void setGroup(AssistanceGroupDto group) {
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public double getGradeOne() {
        return gradeOne;
    }

    public void setGradeOne(double gradeOne) {
        this.gradeOne = gradeOne;
    }

    public double getGradeTwo() {
        return gradeTwo;
    }

    public void setGradeTwo(double gradeTwo) {
        this.gradeTwo = gradeTwo;
    }

    public double getOutOfSeason() {
        return outOfSeason;
    }

    public void setOutOfSeason(double outOfSeason) {
        this.outOfSeason = outOfSeason;
    }
    
}
