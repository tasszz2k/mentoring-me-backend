package com.labate.mentoringme.model.quiz;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.aspectj.weaver.patterns.TypePatternQuestions.Question;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.labate.mentoringme.model.Category;
import com.labate.mentoringme.model.Role;
import com.labate.mentoringme.model.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EntityListeners(AuditingEntityListener.class)
@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "quizzes")
@SQLDelete(sql = "update quizzes set is_deleted = true where id=?")
@Where(clause = "is_deleted = false")
public class Quiz {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String title;

	private String description;

	private Integer numberOfQuestion;

	@Column(name = "visible_status", columnDefinition = "BIT", length = 1)
	private Boolean visibleStatus;

	@Column(name = "editable_status", columnDefinition = "BIT", length = 1)
	private Boolean editableStatus;

	@Column(name = "is_deleted", columnDefinition = "BIT", length = 1)
	private Boolean isDeleted;

	@ManyToOne
	@JoinColumn(name = "created_by")
	private User user;
    
	private Integer type;
	
	private Integer modifiedBy;

	@CreatedDate
	@Column(name = "created", nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;

	@LastModifiedDate
	@Column(name = "modified")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedDate;

	@JsonIgnore
	@OneToMany(mappedBy = "quiz")
	private Set<Question> questions;
	
	@JsonIgnore
	@OneToMany(mappedBy = "quiz")
	private Set<QuizResult> quizResults;
	
	@JsonIgnore
	@ManyToMany
	@JoinTable(name = "quizzes_categories", joinColumns = { @JoinColumn(name = "quiz_id") }, inverseJoinColumns = {
			@JoinColumn(name = "category_id") })
	private Set<Category> categories;
}
