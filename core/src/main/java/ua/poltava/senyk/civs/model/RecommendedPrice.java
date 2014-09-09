/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ua.poltava.senyk.civs.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Entity for recommended price
 * @author Msenyk
 */
@Entity
@Table(name = "prices")
@NamedQueries({
	@NamedQuery(
		name = "RecommendedPrices.findByGroup",
		query = "SELECT p FROM RecommendedPrice p WHERE p.group.id = :groupId ORDER BY p.name"
	)
})
public class RecommendedPrice implements Serializable {

	private static final long serialVersionUID = 1L;
	// fields
	@Id
	@Column(name = "id")
	@GeneratedValue
	private long id;
    @ManyToOne
    @JoinColumn(name = "group_id")
    private AssistanceGroup group;
    @Column(name = "name", nullable = false, length = 64)
	private String name;
    @Column(name = "measure", nullable = true, length = 16)
	private String measure;
    @Column(name = "grade_one")
    private double gradeOne;
    @Column(name = "grade_two")
    private double gradeTwo;
    @Column(name = "out_of_season")
    private double outOfSeason;

    public RecommendedPrice() {
        this.id = 0L;
    }

    public RecommendedPrice(AssistanceGroup group, String name) {
        this.group = group;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public AssistanceGroup getGroup() {
        return group;
    }

    public void setGroup(AssistanceGroup group) {
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
