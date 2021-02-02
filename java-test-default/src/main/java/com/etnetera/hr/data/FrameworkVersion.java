package com.etnetera.hr.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(of = {"version", "deprecationDate", "hypeLevel"})
public class FrameworkVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String version;

    @Column(nullable = false, length = 30)
    private String deprecationDate;

    @Column(nullable = false, length = 30)
    private int hypeLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "framework_id")
    @JsonIgnore
    private JavaScriptFramework framework;

    public FrameworkVersion(String version, String deprecationDate, int hypeLevel, JavaScriptFramework framework) {
        this.version = version;
        this.deprecationDate = deprecationDate;
        this.hypeLevel = hypeLevel;
        this.framework = framework;
    }

}