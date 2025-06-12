package com.example.student.repository

import com.example.student.model.Enrollment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface EnrollmentRepository : JpaRepository<Enrollment, Long> {
    fun existsByStudentIdAndClassId(studentId: Long, classId: Long): Boolean

    @Query("""
        SELECT e FROM Enrollment e 
        WHERE e.classId = :classId 
        AND e.status = 'ACTIVE'
    """)
    fun findActiveEnrollmentsByClassId(@Param("classId") classId: Long): List<Enrollment>

    @Query("""
        SELECT CASE WHEN COUNT(e) > 0 THEN TRUE ELSE FALSE END 
        FROM Enrollment e 
        WHERE e.classId = :classId 
        AND e.studentId = :studentId
        AND e.status = 'ACTIVE'
    """)
    fun isStudentEnrolled(
        @Param("studentId") studentId: Long,
        @Param("classId") classId: Long
    ): Boolean

    fun countByClassIdAndStatus(classId: Long, status: Enrollment.EnrollmentStatus): Long
}