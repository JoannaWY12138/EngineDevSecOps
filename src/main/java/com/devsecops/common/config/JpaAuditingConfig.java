package com.devsecops.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * JPA审计配置
 * 启用自动填充创建时间和更新时间
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
}