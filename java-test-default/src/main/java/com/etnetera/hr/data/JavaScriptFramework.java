package com.etnetera.hr.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Simple data entity describing basic properties of every JavaScript framework.
 * 
 * @author Etnetera
 *
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JavaScriptFramework {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 30)
	private String name;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "framework", orphanRemoval = true)
	private List<FrameworkVersion> versions;

	public JavaScriptFramework(String name) {
		this.name = name;
	}
}
