package com.backend.server.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Community extends BaseTimeEntity{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String nickname;

  @ManyToOne
  @JoinColumn(name = "author_id")
  private User author;

  @Column
  private int recommand;

  @Column
  private int view;

  @Column
  private String type;

  @Column
  private Long typeId;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String content;
}
