package com.example.student.repository

import com.example.student.model.Class
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ClassRepository : JpaRepository<Class, Long> {
    fun findByName(name: String): Optional<Class>

    @Query("SELECT c FROM Class c WHERE c.teacherUsername = :teacherUsername")
    fun findByTeacherUsername(teacherUsername: String): List<Class>

    fun existsByNameAndTeacherUsername(name: String, teacherUsername: String): Boolean
}