package com.etnetera.hr.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Simple data entity describing basic properties of every JavaScript framework.
 * 
 * @author Etnetera
 *
 */
@Entity
public class JavaScriptFramework {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(nullable = false, length = 30)
	private String name;

	@Column(nullable = false, length = 30)
	private String version;

	@Column(nullable = false, length = 30)
	private String deprecationDate;


	@Column(nullable = false, length = 30)
	private BigDecimal hypeLevel;

	public JavaScriptFramework() {
	}

	public JavaScriptFramework(String name, String version, String deprecationDate, BigDecimal hypeLevel) {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDeprecationDate() {
		return deprecationDate;
	}

	public void setDeprecationDate(String deprecationDate) {
		this.deprecationDate = deprecationDate;
	}

	public BigDecimal getHypeLevel() {
		return hypeLevel;
	}

	public void setHypeLevel(BigDecimal hypeLevel) {
		this.hypeLevel = hypeLevel;
	}

	@Override
	public String toString() {
		return "JavaScriptFramework [id=" + id + ", name=" + name + ", version]";
	}

}
