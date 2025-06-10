package com.example.auth.controller

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/test")
class TestController {
    private val logger = LoggerFactory.getLogger(javaClass)

    @GetMapping
    fun test(): String {
        logger.info("Test endpoint was called")
        return "Test endpoint is working!"
    }
}