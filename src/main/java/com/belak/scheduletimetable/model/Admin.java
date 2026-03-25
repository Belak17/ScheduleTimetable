package com.belak.scheduletimetable.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "admin")
@Getter
@Setter
public class Admin extends User {
}
