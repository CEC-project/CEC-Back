package com.backend.server.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Notice extends BaseTimeEntity{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;

  @Lob
  @Column(nullable = false)
  private String content;

  @Column(nullable = false)
  private Boolean important;

  @Column
  private String attachmentUrl;  // 첨부파일 URL

  @ManyToOne
  @JoinColumn(name = "author_id")
  private User author;
}
